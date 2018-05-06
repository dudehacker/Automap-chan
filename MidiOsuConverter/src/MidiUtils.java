import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class MidiUtils {
	//http://www.ccarh.org/courses/253/handout/gminstruments/
	public final static String[] NORMAL_INSTRUMENT_SET= {
		// Piano Timbres
		"Acoustic Grand Piano",
		"Bright Acoustic Piano",
		"Electric Grand Piano",
		"Honky-tonk Piano",
		"Rhodes Piano",
		"Chorused Piano",
		"Harpsichord",
		"Clavinet",
		// Chromatic Percussion
		"Celesta",
		"Glockenspiel",
		"Music Box",
		"Vibraphone",
		"Marimba",
		"Xylophone",
		"Tubular Bells",
		"Dulcimer",
		// Organ Timbres
		"Hammond Organ",
		"Percussive Organ",
		"Rock Organ",
		"Church Organ",
		"Reed Organ",
		"Accordion",
		"Harmonica",
		"Tango Accordion",
		// Guitar Timbres
		"Acoustic Nylon Guitar",
		"Acoustic Steel Guitar",
		"Electric Jazz Guitar",
		"Electric Clean Guitar",
		"Electric Muted Guitar",
		"Overdriven Guitar",
		"Distortion Guitar",
		"Guitar Harmonics",
		// Bass Timbres
		"Acoustic Bass",
		"Fingered Electric Bass",
		"Plucked Electric Bass",
		"Fretless Bass",
		"Slap Bass 1",
		"Slap Bass 2",
		"Synth Bass 1",
		"Synth Bass 2",
		// String Timbres
		"Violin",
		"Viola",
		"Cello",
		"Contrabass",
		"Tremolo Strings",
		"Pizzicato Strings",
		"Orchestral Harp",
		"Timpani",
		// Ensemble Timbres
		"String Ensemble 1",
		"String Ensemble 2",
		"Synth Strings 1",
		"Synth Strings 2",
		"Choir Aah",
		"Choir Ooh",
		"Synth Voice",
		"Orchestral Hit",
		// Brass Timbres
		"Trumpet",
		"Trombone",
		"Tuba",
		"Muted Trumpet",
		"French Horn",
		"Brass Section",
		"Synth Brass 1",
		"Synth Brass 2",
		// Reed Timbres
		"Soprano Sax",
		"Alto Sax",
		"Tenor Sax",
		"Baritone Sax",
		"Oboe",
		"English Horn",
		"Bassoon",
		"Clarinet",
		// Pipe Timbres
		"Piccolo",
		"Flute",
		"Recorder",
		"Pan Flute",
		"Bottle Blow",
		"Shakuhachi",
		"Whistle",
		"Ocarina",
		// Synth Lead
		"Square Wave Lead",
		"Sawtooth Wave Lead",
		"Calliope Lead",
		"Chiff Lead",
		"Charang Lead",
		"Voice Lead",
		"Fifths Lead",
		"Bass Lead",
		// Synth Pad
		"New Age Pad",
		"Warm Pad",
		"Polysynth Pad",
		"Choir Pad",
		"Bowed Pad",
		"Metallic Pad",
		"Halo Pad",
		"Sweep Pad",
		// Synth Effect
		"Rain Effect",
		"Soundtrack Effect",
		"Crystal Effect",
		"Atmosphere Effect",
		"Brightness Effect",
		"Goblins Effect",
		"Echoes Effect",
		"Sci-Fi Effect",
		// Ethnic Timbres
		"Sitar",
		"Banjo",
		"Shamisen",
		"Koto",
		"Kalimba",
		"Bagpipe",
		"Fiddle",
		"Shanai",
		// Sound Effects
		"Tinkle Bell",
		"Agogo",
		"Steel Drums",
		"Woodblock",
		"Taiko Drum",
		"Melodic Tom",
		"Synth Drum",
		"Reverse Cymbal",
		"Guitar Fret Noise",
		"Breath Noise",
		"Seashore",
		"Bird Tweet",
		"Telephone Ring",
		"Helicopter",
		"Applause",
		"Gun Shot"
	};
	
	
	// https://en.wikipedia.org/wiki/Roland_GS
	public final static String[] PERCUSSION_SET = {
		// 0
		"Standard Kit",
		// 8
		"Room Kit",
		// 16
		"Power Kit",
		// 24
		"Electronic Kit",
		// 25
		"TR-808 Kit",
		//32
		"Jazz Kit",
		// 34-80
		"Acoustic Bass Drum",
		"Bass Drum 1",
		"Side Stick",
		"Acoustic Snare",
		"Hand Clap",
		"Electric Snare",
		"Low Floor Tom",
		"Closed High Hat",
		"High Floor Tom",
		"Pedal High Hat",
		"Low Tom",
		"Open High Hat",
		"Low Mid Tom",
		"High Mid Tom",
		"Crash Cymbal 1",
		"High Tom",
		"Ride Cymbal 1",
		"Chinese Cymbal",
		"Ride Bell",
		"Tambourine",
		"Splash Cymbal",
		"Cowbell",
		"Crash Cymbal 2",
		"Vibraslap",
		"Ride Cymbal 2",
		"High Bongo",
		"Low Bongo",
		"Mute High Conga",
		"Open High Conga",
		"Low Conga",
		"High Timbale",
		"Low Timbale",
		"High Agogo",
		"Low Agogo",
		"Cabasa",
		"Maracas",
		"Short Whistle",
		"Long Whistle",
		"Short Guiro",
		"Long Guiro",
		"Claves",
		"High Wood Block",
		"Low Wood Block",
		"Mute Cuica",
		"Open Cuica",
		"Mute Triangle",
		"Open Triangle",
		// 127
		"CM-64/CM-32L"
	};
	
	public static String[] NORMAL_INSTRUMENT_TYPE = {
		"Keyboards",
		"Chromatic Percussion",
		"Organs",
		"Guitars",
		"Basses",
		"Strings",
		"Ensemble & Voices",
		"Brass",
		"Reed Winds",
		"Pipe Winds",
		"Synth Lead",
		"Synth Pad",
		"Synth Effects",
		"Ethnic Timbres",
		"Sound Effects",
	};
	/**
	 * Only used for channel 9 percussion instruments
	 * @param instrumentID
	 * @param channel
	 * @return
	 */
	public static int getInstrumentNameIndex(int instrumentID, int channel){
		int nameIndex = 0;
		if (channel == 9){
			if (instrumentID>= 34 && instrumentID <= 80){
				   nameIndex = (instrumentID - 34) + 6;
			   } else if (instrumentID<34){
				   switch(instrumentID){
				   case 0:
					   nameIndex = 0;
					   break;
				   case 8:
					   nameIndex = 1;
					   break;
				   case 16:
					   nameIndex = 2;
					   break;
				   case 24:
					   nameIndex = 3;
					   break;
				   case 25:
					   nameIndex = 4;
					   break;
				   case 32:
					   nameIndex = 5;
					   break;
				
				   }
			   } else {
				   nameIndex = PERCUSSION_SET.length-1;
			   }
		} else {
			
		}
		
		return nameIndex;
	}
	
	public static void stopSequence(Sequencer sequencer) throws InvalidMidiDataException{
		pauseSequence(sequencer);
		resetSequence(sequencer);
	}
	
	public static void playSequence(Sequencer sequencer) throws MidiUnavailableException{
		sequencer.open();
		sequencer.start();
	}
	
	public static void resumeSequence(Sequencer sequencer, long tick){
		sequencer.setTickPosition(tick);
		sequencer.start();
	}
	
	public static long pauseSequence(Sequencer sequencer){
		if(sequencer.isOpen()){
			sequencer.stop();
			return sequencer.getTickPosition();
		}
		return 0;
	}
	
	public static void resetSequence(Sequencer sequencer) throws InvalidMidiDataException{
		sequencer.setTickPosition(0);
	}
	
	public static Sequencer getSequencer(String filepath) throws InvalidMidiDataException, IOException, MidiUnavailableException {
		File f = new File(filepath);
		if (f.isFile()){
			Sequence sequence = MidiSystem.getSequence(f);
			System.out.println(sequence.getMicrosecondLength());
			Sequencer sequencer = MidiSystem.getSequencer();
			sequencer.setSequence(sequence);
			return sequencer;
		}
		return null;
	}
	
	public static int getChannel(Sequencer sequencer, int trackID){
		Track track = sequencer.getSequence().getTracks()[trackID];
		for (int i = 0; i< track.size(); i ++){
			MidiEvent event = track.get(i);
			MidiMessage msg = event.getMessage();
			if (msg instanceof ShortMessage){
				ShortMessage sm = (ShortMessage) msg;
				int status = sm.getStatus();
				if (status >= 0xC0 && status <= 0xCF) {
					// Program change (instrument)
					return sm.getChannel();
				}
			}
		}
		return -1;
	}
	
	public static void muteTrack(Sequencer sequencer, int trackID){
		sequencer.setTrackMute(trackID, true);
		if (!sequencer.getTrackMute(trackID)){
			throw new IllegalArgumentException("Could not mute track "+trackID);
		}
	}
	
	public static void unmuteTrack(Sequencer sequencer, int trackID){
		sequencer.setTrackMute(trackID, false);
		if (sequencer.getTrackMute(trackID)){
			throw new IllegalArgumentException("Could not unmute track "+trackID);
		}
	}
	
	public static void setTrackSolo(Sequencer sequencer, int trackID){
		for(int i = 0; i < sequencer.getSequence().getTracks().length;i++){
			if (i != trackID){
				sequencer.setTrackSolo(i, false);
			}
		}
		sequencer.setTrackSolo(trackID, true);
		if (!sequencer.getTrackSolo(trackID)){
			throw new IllegalArgumentException("Could not set track "+trackID + " to solo mode");
		}
	}
	
	public static void setTrackNotSolo(Sequencer sequencer, int trackID){
		sequencer.setTrackSolo(trackID, false);
		if (sequencer.getTrackSolo(trackID)){
			throw new IllegalArgumentException("Could not set track "+trackID + " to not solo mode");
		}
	}
	
	
	public static int getInstrumentID(Sequencer sequencer, int trackID){
		int instrumentID = -1;
		int channel = getChannel(sequencer, trackID);
		Track track= sequencer.getSequence().getTracks()[trackID];
		for (int i = 0; i < track.size();i++){
			MidiEvent event = track.get(i);
			MidiMessage message = event.getMessage();
			if (message instanceof ShortMessage){
				ShortMessage sm = (ShortMessage) message;
				int status = sm.getStatus();
				if (status >= 0xC0 && status <= 0xCF && channel == sm.getChannel()) {
					// Program change (instrument)
					System.out.println("Instrument ID is " + sm.getData1());
					System.out.println("channel " + channel);
					return sm.getData1();
					
				}
			}
		}
		if (instrumentID == -1){
			try {
				channel = trackID;
				track.add(new MidiEvent(new ShortMessage(0xC0+channel,channel,0,0),0));
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
			return 0;
			//throw new IllegalArgumentException("can't find instrument for "+trackID+" out of " + sequencer.getSequence().getTracks().length + " tracks");
		}
		return instrumentID;
	}
	
	private static long getFirstNoteTick(Sequence sequence ){
		Track[] tracks = sequence.getTracks();
		long tick = Long.MAX_VALUE;
		for(int trackID = 0; trackID < tracks.length; trackID++){
			Track track = tracks[trackID];
			for (int i= 0; i<track.size();i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;
					if (sm.getCommand() >= 0x90 && sm.getCommand() <= 0x9F) {
						int volume = sm.getData2();
						if (volume>0){
							// NOTE ON
							if (event.getTick()< tick){
								tick = event.getTick();
							}
						}
					}
				}
			}
		}
		return tick;
	}
	
	public static void setTempos(Sequencer sequencer, long[] timings, int[] bpms){
		Sequencer seq = emptyTempos(sequencer);
		Sequence sequence = seq.getSequence();
		int res = sequence.getResolution();
		Track track = sequence.getTracks()[0];
		long startTick = getFirstNoteTick(sequence);
		for (int i = 0; i< bpms.length;i++){
			MetaMessage msg = new MetaMessage();
			long tempo = (long) (Utils.bpmTomspb(bpms[i]) * 1000);
			byte[] data = tempoToDataBytes(tempo);
			startTick += timings[i] * res;
			try {
				msg.setMessage(81, data, 3);
			} catch (InvalidMidiDataException e1) {
				e1.printStackTrace();
			}
			MidiEvent e = new MidiEvent(msg,startTick);
			track.add(e);
		}
	}
		
	public static byte[] tempoToDataBytes(long tempo){
		String tempoHexString = Long.toHexString(tempo);
		if (tempoHexString.length()>6){
			throw new IllegalArgumentException("Tempo Hex String is longer than 6!");
		}
		while (tempoHexString.length() != 6) {
			tempoHexString = "0" + tempoHexString;
		}
		byte[] data = Utils.hexStringToByteArray(tempoHexString);
		return data;
	}
	
	public static void saveMidi(Sequencer sequencer, String outputFile){
		File f = new File(outputFile);
		Sequence seq = sequencer.getSequence();
		try {
			MidiSystem.write(seq,1,f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Sequencer keepOnlyTrack(Sequencer seq, int trackID) throws MidiUnavailableException, InvalidMidiDataException{
		Sequencer output = MidiSystem.getSequencer();
		Sequence oldSeq = seq.getSequence();
		Track[] tracks = oldSeq.getTracks();
		ArrayList<Track> tracksToRemove = new ArrayList<>();
		for (int i = 2; i<tracks.length;i++){
			if (i!=trackID){
				tracksToRemove.add(tracks[i]);
			}
		}
		for (Track t : tracksToRemove){
			oldSeq.deleteTrack(t);
		}
		output.setSequence(oldSeq);
		return output;
	}
	
	public static Sequencer emptyTempos(Sequencer sequencer){
		Sequence sequence = sequencer.getSequence();
		Track[] tracks =  sequence.getTracks();

		for (int j = 0; j< tracks.length;j++){
			Track track = tracks[j];
			ArrayList<MidiEvent> eventList = new ArrayList<>();
			for (int i = 0; i < track.size();i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof MetaMessage) {
					MetaMessage mm = (MetaMessage) message;
					if (mm.getType() == 81) {
						// tempo message
						eventList.add(event);
					}
				}
				
			}// end of 1 track
			for (MidiEvent e : eventList){
				track.remove(e);
			}
		}// end of tracks
		return sequencer;
	}
	
	public static int getInstrumentID(String name){
		int ID = -1;
		List<String> percussionSet =  Arrays.asList(PERCUSSION_SET);
		List<String> normalSet = Arrays.asList(NORMAL_INSTRUMENT_SET);
		if (percussionSet.contains(name)){
			ID = percussionSet.indexOf(name) + 34; 
		} else if (normalSet.contains(name)) {
			ID = normalSet.indexOf(name);
		} else {
			throw new IllegalArgumentException("Invalid Instrument ID, can't find instrument name!");
		}
		return ID;
	}
	
	public static String getInstrumentName(int channel, int instrumentID){
		String name = "";
		if (channel==9 && instrumentID<81 && instrumentID>=34){
			name = PERCUSSION_SET[instrumentID-34];
		} else if (instrumentID>=0 && instrumentID<128) {
			name = NORMAL_INSTRUMENT_SET[instrumentID];
		} else {
			throw new IllegalArgumentException("Invalid Instrument ID, can't find instrument name!");
		}
		return name;
	}
	
	/**
	 * Change the instrument of a channel
	 * @param sequencer
	 * @param trackID
	 * @param instrumentID
	 * @throws InvalidMidiDataException
	 */
	public static void setInstrument(Sequencer sequencer,int trackID,int instrumentID) throws InvalidMidiDataException{
		int channelID = getChannel(sequencer, trackID);
		for (Track track: sequencer.getSequence().getTracks()){
			for (int i=0; i<track.size();i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;
					int status = sm.getStatus();
					if (status >= 0xC0 && status <= 0xCF) {
						// Program change (instrument)
						if (sm.getChannel()==channelID){
							track.remove(event);
						}
						
					}
				}
			}
		}
		ShortMessage newMessage = new ShortMessage();
		newMessage.setMessage(0xC0+channelID, instrumentID, 0);
		System.out.println("Changed track " + trackID + " channel " + channelID + " to Instrument ID " + instrumentID);
		MidiEvent newEvent = new MidiEvent(newMessage,0);
		sequencer.getSequence().getTracks()[trackID].add(newEvent);
	}
	
	/**
	 * Find the velocity of the least loud note in a given track
	 * @param sequencer
	 * @param trackID
	 * @return
	 */
	private static int getTrackMinVolume(Sequencer sequencer, int trackID){
		int minVolume = 127;
		Track track = sequencer.getSequence().getTracks()[trackID];
		for (int i=0; i<track.size();i++){
			MidiEvent event = track.get(i);
			MidiMessage message = event.getMessage();
			if (message instanceof ShortMessage){
				ShortMessage sm = (ShortMessage) message;
				if (sm.getCommand() >= 0x90 && sm.getCommand() <= 0x9F) {
					int volume = sm.getData2();
					if (volume>0){
						// NOTE ON
						if (volume<minVolume){
							minVolume=volume;
						}
					}
				}
			}
			
		}
		return minVolume;
	}
	
	public static Sequencer parseToNormalSequencer(Sequencer oldSeq){
		Sequencer sequencer=null;
		try {
			sequencer = MidiSystem.getSequencer();
			ArrayList<Integer> channels = getChannels(oldSeq);
			// add tempos to track 0
			Sequence seq = null;
			try {
				seq = new Sequence(oldSeq.getSequence().getDivisionType(), oldSeq.getSequence().getResolution());
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
			Track tempoTrack = seq.createTrack();
			Track oldTrack = oldSeq.getSequence().getTracks()[0];
			for (int j = 0; j<oldTrack.size();j++){
				MidiEvent event = oldTrack.get(j);
				MidiMessage msg = event.getMessage();
				if (msg instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) msg;
					if (!(sm.getCommand() >= 0x80 && sm.getCommand() <= 0x9F)) {
						// if msg is not Note On or Note Off
						int status = sm.getStatus();
						if (!(status >= 0xC0 && status <= 0xCF)) {
							// if msg is not instrument change
							tempoTrack.add(event);
						}
					}
					
				} else {
					// add meta msg and sys msg
					tempoTrack.add(event);
				}
			}
			// other tracks containing notes
			for (int i = 0; i < channels.size(); i++){
				Track t = seq.createTrack();
				int ch = channels.get(i);
				for (int x = 0; x<oldTrack.size();x++){
					MidiEvent event = oldTrack.get(x);
					MidiMessage msg = event.getMessage();
					if (msg instanceof ShortMessage){
						ShortMessage sm = (ShortMessage) msg;
						if (ch == sm.getChannel()){
							t.add(event);
						}
					}
				}
			}
			try {
				sequencer.setSequence(seq);
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
			
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		}
		
		return sequencer;
	}
	
	
	private static ArrayList<Integer> getChannels(Sequencer seq){
		ArrayList<Integer> channels = new ArrayList<>();
		Track[] tracks = seq.getSequence().getTracks();
		if (tracks.length==1){
			Track track = tracks[0];
			for (int i = 0; i<track.size();i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;
					if (sm.getCommand() >= 0x90 && sm.getCommand() <= 0x9F) {
						int volume = sm.getData2();
						if (volume>0){
							// NOTE ON
							if (!channels.contains(sm.getChannel())){
								channels.add(sm.getChannel());
							}
						}
					}
				}
			}
		}
		Collections.sort(channels);
		System.out.println("Channels : \n" + channels);
		return channels;
	}
	
	public static boolean fixChannels(Sequencer seq){
		Track[] tracks = seq.getSequence().getTracks();
		for (int trackID = 0; trackID < tracks.length; trackID++){
			Track track = tracks[trackID];
			int channel = trackID;
			for (int i = 0; i<track.size();i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;
					try {
						sm.setMessage(sm.getCommand(), channel, sm.getData1(), sm.getData2());
					} catch (InvalidMidiDataException e) {
						e.printStackTrace();
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static boolean isMultiInstrumentInSingleTrack(Sequencer seq){
		Track[] tracks = seq.getSequence().getTracks();
		ArrayList<Integer> channels = new ArrayList<>();
		if (tracks.length==1){
			Track track = tracks[0];
			for (int i = 0; i<track.size();i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;
					if (sm.getCommand() >= 0x90 && sm.getCommand() <= 0x9F) {
						int volume = sm.getData2();
						if (volume>0){
							// NOTE ON
							if (!channels.contains(sm.getChannel())){
								channels.add(sm.getChannel());
							}
						}
					}
				}
			}
			if (channels.size()>1){
				return true;
			}
		}
		return false;
	}
	
	public static List<Integer> getTracks(Sequencer seq){
		ArrayList<Integer> tracks = new ArrayList<Integer>();
		Track[] t = seq.getSequence().getTracks();
		//System.out.println("midi file contains : "+t.length + " tracks");
		for (int j = 0; j< t.length ; j++){
			Track track = t[j];
			for (int i = 0; i<track.size();i++){
				MidiEvent event = track.get(i);
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;
					if (sm.getCommand() >= 0x90 && sm.getCommand() <= 0x9F) {
						int volume = sm.getData2();
						if (volume>0){
							// NOTE ON
							tracks.add(new Integer(j));
							break;
						}
					}
				}
			}
		}
		return tracks;
	}
	
	/**
	 * Find the velocity of the loudest note in a given track
	 * @param sequencer
	 * @param trackID
	 * @return
	 */
	private static int getTrackMaxVolume(Sequencer sequencer, int trackID){
		int maxVolume = 0;
		Track track = sequencer.getSequence().getTracks()[trackID];
		for (int i=0; i<track.size();i++){
			MidiEvent event = track.get(i);
			MidiMessage message = event.getMessage();
			if (message instanceof ShortMessage){
				ShortMessage sm = (ShortMessage) message;
				if (sm.getCommand() >= 0x90 && sm.getCommand() <= 0x9F) {
					int volume = sm.getData2();
					if (volume>0){
						// NOTE ON
						if (volume>maxVolume){
							maxVolume=volume;
						}
					}
				}
			}
			
		}
		return maxVolume;
	}
	
	public static int[] getTrackDynamicVolumeRange(Sequencer sequencer, int trackID){
		int[] volumeRange = new int[2];
		int lowerLimit = getTrackMinVolume(sequencer, trackID) - 1;
		if (lowerLimit != 0){
			lowerLimit = -1*lowerLimit;
		}
		volumeRange[0] = lowerLimit;
		int upperLimit = 127 - getTrackMaxVolume(sequencer,trackID);
		volumeRange[1] = upperLimit;
		return volumeRange;
	}
	
	/**
	 * Change the velocity of every notes in a track
	 * @param sequencer
	 * @param trackID
	 * @param volumeChange , change in volume, can be positive or negative
	 * @throws InvalidMidiDataException
	 */
	public static void setTrackVolume(Sequencer sequencer, int trackID, int volumeChange) throws InvalidMidiDataException {
		int channelMaxVolume = getTrackMaxVolume(sequencer,trackID);
		int channelMinVolume = getTrackMinVolume(sequencer,trackID);
		ArrayList<MidiEvent> newEventList = new ArrayList<>();
		ArrayList<MidiEvent> oldEventList = new ArrayList<>();
		Track track = sequencer.getSequence().getTracks()[trackID];
		if (volumeChange+channelMaxVolume>127){
			throw new IllegalArgumentException("Channel Volume can't exceed max value of 127!");
		} else if(channelMinVolume+volumeChange<0){
			throw new IllegalArgumentException("Channel Volume can't be negative!");
		}
		else {
			for (int i=0; i<track.size();i++){
				MidiEvent event = track.get(i);
				long tick = event.getTick();
				MidiMessage message = event.getMessage();
				if (message instanceof ShortMessage){
					ShortMessage sm = (ShortMessage) message;
					int status = sm.getStatus();
					if (sm.getCommand() >= 0x90 && sm.getCommand() <= 0x9F) {
						int oldVolume = sm.getData2();
						int pitch = sm.getData1();
						if (oldVolume>0){
							// NOTE ON
							oldEventList.add(event);
							ShortMessage newMessage = new ShortMessage();
							newMessage.setMessage(status, pitch, oldVolume+volumeChange);
							MidiEvent newEvent = new MidiEvent(newMessage,tick);
							newEventList.add(newEvent);	
							
						}
					}
				}
				
			}
			for (MidiEvent e : oldEventList){
				track.remove(e);
			}
			
			for (MidiEvent e : newEventList){
				track.add(e);
			}
		}
	}
	
}
