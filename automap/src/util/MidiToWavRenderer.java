package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import be.hogent.tarsos.midi.MidiCommon;
import be.hogent.tarsos.midi.MidiUtils;

import com.sun.media.sound.AudioSynthesizer;

/**
 * <p>
 * Uses a modified version of a file of the <a
 * href="http://www.jfugue.org/download.html">JFugue</a> API for Music
 * Programming. More precisely the <a
 * href="http://www.jfugue.org/code/Midi2WavRenderer.java"
 * >Midi2WavRenderer.java</a> file.
 * </p>
 * 
 * @author Karl Helgason
 * @author David Koelle
 * @author Joren Six
 */
public final class MidiToWavRenderer {
	private int sampleRate;
	private int bitDepth;
	// 1 = mono  2 = stereo
	private int channel; 
	/**
	 * Log messages.
	 */
	private static final Logger LOG = Logger.getLogger(MidiToWavRenderer.class
			.getName());

	/**
	 * The synth used to render the audio.
	 */
	private final transient AudioSynthesizer synth;

	private double[] rebasedTuning;

	/**
	 * 
	 * @param sampleRate in Hz
	 * @param bitDepth usually 8 or 16
	 * @param channel 1 for mono and 2 for stereo
	 * @throws MidiUnavailableException
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 */
	public MidiToWavRenderer(int sampleRate, int bitDepth, int channel) throws MidiUnavailableException,
			InvalidMidiDataException, IOException {
		this.sampleRate = sampleRate;
		this.bitDepth = bitDepth;
		this.channel = channel;
		try {
			synth = (AudioSynthesizer) MidiSystem.getSynthesizer();
		} catch (ClassCastException e) {
			throw new Error(
					"Please make sure Gervill is included in the classpath: "
							+ "it should be de default synth. These are the currently installed synths: "
							+ MidiSystem.getMidiDeviceInfo().toString(), e);
		}
	}

	/**
	 * Changes the tuning of the synth.
	 * 
	 * @param tuning
	 * @throws InvalidMidiDataException
	 * @throws IOException
	 * @throws MidiUnavailableException
	 */
	public void setTuning(final double[] tuning) throws IOException,
			InvalidMidiDataException, MidiUnavailableException {
		rebasedTuning = MidiCommon.tuningFromPeaks(tuning);
	}

	public void saveSongWAV(final Sequence sequence, final String outputFile,double duration) throws MidiUnavailableException, IOException{
			final AudioFormat format = new AudioFormat(44100, 24, 2, true, false);
			final Map<String, Object> map = new HashMap<String, Object>();
			map.put("interpolation", "sinc");
			map.put("max polyphony", "1024");
			AudioInputStream stream = synth.openStream(format, map);
			send(sequence, synth.getReceiver());
			final long len = (long) (stream.getFormat().getFrameRate() * (duration));
			stream = new AudioInputStream(stream, stream.getFormat(), len);
			AudioSystem.write(stream, AudioFileFormat.Type.WAVE, new File(outputFile));
			this.synth.close();
			stream.close();
	}
	
	public void createWavFile(final Sequence sequence, int midiNote ,final File outputFile, double duration ) throws MidiUnavailableException,
			InvalidMidiDataException, IOException {
		/*
		 * Open synthesizer in pull mode. model.Sample rate depends on pitch
		 * using Sinc interpolation for highest quality. With 1024 as max
		 * polyphony.
		 */
		
		// Hz model.Sample Rate(bit) strereo=2
		final AudioFormat format = new AudioFormat(sampleRate, bitDepth, channel, true, false);
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("interpolation", "sinc");
		map.put("max polyphony", "1024");
		AudioInputStream stream = synth.openStream(format, map);

		// Play Sequence into AudioSynthesizer Receiver.
		send(sequence, synth.getReceiver());
		// Calculate how long the WAVE file needs to be.
		final long len = (long) (stream.getFormat().getFrameRate() * (duration));
		stream = new AudioInputStream(stream, stream.getFormat(), len);
		int bytesPerFrame = stream.getFormat().getFrameSize();
		//System.out.println("Number of bytes per frame : "+bytesPerFrame);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024*bytesPerFrame];
		while (true) {
             int nBytesRead = stream.read(buffer);
             if (nBytesRead == -1) {
                 break;
             }
             baos.write(buffer, 0, nBytesRead);
         }
		byte[] data = baos.toByteArray();
		ByteOrder byteorder;
		if (format.isBigEndian()){
			byteorder = ByteOrder.BIG_ENDIAN;
		} else {
			byteorder = ByteOrder.LITTLE_ENDIAN;
		}
		byte[] newdata = Utils.removeHitSoundSilence(data,bitDepth,byteorder);
		ByteArrayInputStream byteInputStream = new ByteArrayInputStream(newdata);
		AudioInputStream outputAIS = new AudioInputStream(byteInputStream, format,
                    newdata.length/bytesPerFrame);

		// Write WAVE file to disk.
		AudioSystem.write(outputAIS, AudioFileFormat.Type.WAVE, outputFile);
		this.synth.close();
		stream.close();
	}

	/**
	 * Send entry MIDI Sequence into Receiver using time stamps.
	 * 
	 * @return The total length of the sequence.
	 */
	private double send(final Sequence seq, final Receiver recv) {
		assert seq.getDivisionType() == Sequence.PPQ;

		final float divtype = seq.getDivisionType();
		final Track[] tracks = seq.getTracks();

		tune(recv);

		final int[] trackspos = new int[tracks.length];
		int mpq = 500000;
		final int seqres = seq.getResolution();
		long lasttick = 0;
		long curtime = 0;
		while (true) {
			MidiEvent selevent = null;
			int seltrack = -1;
			for (int i = 0; i < tracks.length; i++) {
				final int trackpos = trackspos[i];
				final Track track = tracks[i];
				if (trackpos < track.size()) {
					final MidiEvent event = track.get(trackpos);
					if (selevent == null
							|| event.getTick() < selevent.getTick()) {
						selevent = event;
						seltrack = i;
					}
				}
			}
			if (seltrack == -1) {
				break;
			}
			trackspos[seltrack]++;
			final long tick = selevent.getTick();
			if (divtype == Sequence.PPQ) {
				curtime += (tick - lasttick) * mpq / seqres;
			} else {
				curtime = (long) (tick * 1000000.0 * divtype / seqres);
			}
			lasttick = tick;
			final MidiMessage msg = selevent.getMessage();
			if (msg instanceof MetaMessage) {
				if (divtype == Sequence.PPQ
						&& ((MetaMessage) msg).getType() == 0x51) {
					final byte[] data = ((MetaMessage) msg).getData();
					mpq = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8
							| data[2] & 0xff;
				}
			} else if (recv != null) {
				recv.send(msg, curtime);
			}
		}
		return curtime / 1000000.0;
	}

	private void tune(final Receiver recv) {
		try {
			if (rebasedTuning != null) {
				for (int i = 0; i < 16; i++) {
					MidiUtils.sendTunings(recv, i, 0, "african", rebasedTuning);
					MidiUtils.sendTuningChange(recv, i, 0);
				}
			}
		} catch (final IOException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		} catch (final InvalidMidiDataException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}