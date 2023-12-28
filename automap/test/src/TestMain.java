

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class TestMain {
	private static int key = 7*12+ 7;  //F#5
	private static int[] durations = {3840};
	
	private static int resolution = 480;
	private static int bpm = 270;
	private static int instr = 51;
	private static float divisionType = Sequence.PPQ;
	private static String outputPath = "C:\\Users\\DH\\Desktop\\Kai Dan";
	
	private static int velocity = 127;
	private static final int NOTE_ON = 0x90;
	private static final int NOTE_OFF = 0x80;
	private static int sampleRate = 16000;
	private static int bitDepth = 16;
	private static int channelMode = 2;
	private static final String[] NOTE_NAMES = 
		// 0    1     2    3     4    5    6    7    8     9    10    11
		{ "C", "C#", "D", "D#", "E", "F", "F#","G", "G#", "A", "A#", "B" };
	
	public static void main(String args[])throws MidiUnavailableException, InvalidMidiDataException,
	IOException {
				
		
			try {
				int endT;
				for (int i=0;i<durations.length;i++){
					endT = durations[i];
					int channel = 0;
					Sequencer sequencer = MidiSystem.getSequencer();
					Sequence seq = new Sequence(divisionType, resolution);
					sequencer.setSequence(seq);
					Track track = seq.createTrack();
					// set instrument
					ShortMessage instrumentChange = new ShortMessage();
					
					instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel,
							instr, 0);
					track.add(new MidiEvent(instrumentChange, 0));
					
					// set channel volume
					int channelVolume = 100;
					// System.out.println(channelVolume);
					ShortMessage channelVolumeChange = new ShortMessage();
					channelVolumeChange.setMessage(ShortMessage.CONTROL_CHANGE,
							channel, 7, channelVolume);
					track.add(new MidiEvent(channelVolumeChange, 0));
					// add note
					
					int startT = 0;
					// set tempo, us per tick
					long tempo = Utils.tickToMidiTempo(bpm);
					MetaMessage mm = new MetaMessage();
					byte[] data = MidiUtils.tempoToDataBytes(tempo);
					mm.setMessage(81, data, 3);
					track.add(new MidiEvent(mm, 0));
					// start
					ShortMessage msg = new ShortMessage();
					msg.setMessage(NOTE_ON + channel, key, velocity);
					track.add(new MidiEvent(msg, startT));
					// end
					ShortMessage msg2 = new ShortMessage();
					msg2.setMessage(NOTE_OFF + channel, key, 0);
					track.add(new MidiEvent(msg2, endT));
					// output wav
					int SR = sampleRate;
					double duration = Utils.tickToMilliSec(endT,
							resolution, bpm) / 1000.0;
					if (key > 69) {
						SR = 16000;
					}
					duration = duration + 10;
					MidiToWavRenderer w = new MidiToWavRenderer(SR, bitDepth,
							channelMode);
					
					String filename;
					int octave = (key / 12) - 1;
					int note = key % 12;
					String noteName = NOTE_NAMES[note];
					if (octave < 0) {
						octave = Math.abs(octave);
						filename = instr + "_" + noteName + "_" + octave + "_" + bpm + "_" + endT + ".wav" ;
					} else {
						filename = instr + "_" + noteName + octave + "_" + bpm + "_" + endT + ".wav" ;
					}
					
					w.createWavFile(seq, key, new File(outputPath+ "\\" + filename), duration);
				}
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				
	
		
			}
}
