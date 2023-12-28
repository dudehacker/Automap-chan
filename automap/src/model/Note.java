package model;

import java.text.DecimalFormat;
import java.util.Comparator;

public class Note {
	// Instance variables
	private String name; // redundant with pitch
	private int column; // the 0-n val corresponding to game chart position (0-6
						// for 7Keys)
	private int velocity;
	private long time; // start or end time in ticks
	private int duration; // in ticks
	private int LNduration; // in ticks
	private final int key; // pitch
	private int instrument;
	private int channelVolume;
	private int channel = 0;
	private int bpm;
	private long abstime;				
	private boolean customHS = false;

	// Constructor
	public Note(String n, int v, long t, int k, int BPM, long abs,
			int instrument, int channelVolume) {
		if (t < 0) {
			throw new IllegalArgumentException("Time can't be negative!");
		} else if (instrument < 0) {
			throw new IllegalArgumentException("Instrument can't be negative!");
		} else if (channelVolume < 0) {
			throw new IllegalArgumentException(
					"Channel Volume can't be negative!");
		}

		name = n;
		velocity = v;
		time = t;
		key = k;
		bpm = BPM;
		abstime = abs;
		column = -2;
		this.instrument = instrument;
		this.channelVolume = channelVolume;
	}

	public Note(String n, int v, long t, int k, int BPM) {
		if (t < 0) {
			throw new IllegalArgumentException("Time can't be negative!");
		}
		name = n;
		velocity = v;
		time = t;
		key = k;
		bpm = BPM;
	}
	
	public int getBPM(){
		return bpm;
	}

	public void setBPM(int b){
		bpm = b;
	}
	
	public void setChannel(int ch) {
		channel = ch;
	}

	public int getChannel() {
		return channel;
	}

	public void setAbs(long abst) {
		abstime = abst;
	}

	public long getAbs() {
		return abstime;
	}
	

	public static Comparator<Note> StartTimeComparator = new Comparator<Note>() {
		@Override
		public int compare(Note n1, Note n2) {
			long t1 = n1.abstime;
			long t2 = n2.abstime;
			/* For ascending order */
			return (int) (t1 - t2);
		}
	};

	public static Comparator<Note> NoteNameComparator = new Comparator<Note>() {
		@Override
		public int compare(Note n1, Note n2) {
			String NoteName1 = n1.getName();
			String NoteName2 = n2.getName();
			// ascending order
			return NoteName1.compareTo(NoteName2);
		}
	};

	public static Comparator<Note> DurationComparator = new Comparator<Note>() {

		@Override
		public int compare(Note s1, Note s2) {

			int rollno1 = s1.getDuration();
			int rollno2 = s2.getDuration();
			/* For ascending order */
			return rollno1 - rollno2;

		}
	};

	public static Comparator<Note> PitchComparator = new Comparator<Note>() {
		@Override
		public int compare(Note n1, Note n2) {
			/* For ascending order */
			return n1.key - n2.key;
		}
	};
	
	public static Comparator<Note> ChannelComparator = new Comparator<Note>() {
		@Override
		public int compare(Note n1, Note n2) {
			/* For ascending order */
			return n1.channel - n2.channel;
		}
	};

	public void setCustomHS(boolean value) {
		customHS = value;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int new_column) {
		column = new_column;
	}

	public String getName() {
		return name;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(Note n) {
		velocity = n.getVelocity();
	}

	public void setVelocity(int v){
		velocity = v;
	}
	public long getTime() {
		return time;
	}

	public void setTime(long t) {
		if (t < 0) {
			throw new IllegalArgumentException("time can't be negative!");
		}
		time = t;
	}

	public int getDuration() {
		return duration;
	}

	public int getKey() {
		return key;
	}

	public void setDuration(int d) {
		if (d < 0) {
			throw new IllegalArgumentException("WTF");
		}
		duration = d;
	}

	@Override
	public Note clone() {
		Note n = new Note(name, velocity, time, key, bpm, abstime, instrument,
				channelVolume);
		n.setDuration(duration);
		n.setCustomHS(customHS);
		n.setChannel(channel);
		n.setLNduration(LNduration);
		return n;
	}

	public String getHitSound() {
		String instr = getInstrumentName();
		if (customHS) {
			return name + ".wav";
		}
		return instr + "_" + name + "_" + bpm + "_" + duration + ".wav";
	}

	public String toSample(int volume) {

		volume = velocityToVolume(volume);

		String sample = "Sample," + abstime + ",0,\"" + getHitSound()
				+ "\"," + volume + "\n";
		return sample;

	}
	
	private int velocityToVolume(int volume){
		return (int) ((velocity/127.0)*volume);
	}

	/**
	 * Convert a note object to string representation of an osu hit object
	 * 
	 * @param x
	 *            the x position of hitobject in terms of 0 to max key value -1,
	 *            0-6 for 7K
	 * @param bpm
	 *            beats per minute
	 * @param resolution
	 *            ticks/beat
	 * @return string representation of an osu hit object
	 */
	public String toHitObject(int column, int resolution, int keyCount,
			int LN_Cutoff, int volume) {
		volume = velocityToVolume(volume);
		String s = "";
		HitObject ho = null;
		int track = 512 / keyCount;
		
		column = column * track + 10;

		if (abstime < 0) {
			throw new IllegalArgumentException("t = " + time + ",abstime = "
					+ abstime + ", bpm = " + bpm + ", resolution = "
					+ resolution + "\n");
		}
		//disabled LN
		/*
		if (LNduration < resolution * LN_Cutoff) {
			// short note if duration is less than LN_Cutoff beat(s)
			ho = new model.HitObject(column, abstime, volume, getHitSound());
		} else {
			// LN
			long end = abstime + (LNduration * 60000L) / (bpm * resolution);
			ho = new model.HitObject(column, abstime, volume, end,
					getHitSound());
		}
		*/
		// Warning
		ho = new HitObject(column, abstime, volume, getHitSound());
		// Above needs to be changed when implementing LN
		
		s = ho.toString();
		return s;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(1);
		return "Channel " + channel + " time " + time + " key " +key + " velocity=" + velocity + " duration=" + LNduration;
	}

	public String debugTime() {
		return name + ", time = " + abstime;

	}

	public void setName(String string) {
		name = string;
	}

	public void setInstrument(int instr) {
		instrument = instr;
	}

	public int getInstrument() {
		return instrument;
	}

	public String getInstrumentName() {
		if (channel == 9) {
			return "drum" + instrument;
		} else {
			return "" + instrument;
		}
	}

	public void setChannelVolume(int vol) {
		channelVolume = vol;
	}

	public int getChannelVolume() {
		return channelVolume;
	}

	public int getLNduration() {
		return LNduration;
	}

	public void setLNduration(int lNduration) {
		LNduration = lNduration;
	}

}
