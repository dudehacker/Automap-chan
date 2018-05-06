
public class Timing {
	// Osu Syntax
	// Offset, Milliseconds per Beat, Meter, Sample Type, Sample Set, Volume, Inherited, Kiai Mode
	
	// offset is time in ms 
	private long offset;
	// mspb is ms per beat, negative if it's non inherited timing point
	private float mspb;
	// meter is beat per measure
	private int meter;
	// st is sample type [1-4]
	private int st;
	// ss is sample set [1-4]
	private int ss;
	// volume is volume of hitsounds [0-100]
	private int volume;
	// inherited 1 for true and 0 for false
	private int inherited;
	// kiai 1 for active 0 for inactive
	private int kiai;
	
	// Constructor
	public Timing(long time, float tempo){
		meter = 4;
		st = 1;
		ss = 1;	
		volume = 100;
		inherited = 1;
		kiai = 0;
		setOffset(time);
		setMspb(tempo);
	}
	
	// default instructor
	public Timing(){
		meter = 4;
		st = 1;
		ss = 1;	
		volume = 100;
		inherited = 1;
		kiai = 0;
	}
	
	public String toOsuTimingPoint(){
		return "" + offset + "," + mspb + "," + meter + "," + st +"," + ss + "," + volume + "," + inherited + "," + kiai + "\n";
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public float getMspb() {
		return mspb;
	}

	public void setMspb(float mspb) {
		this.mspb = mspb;
	}
	
}
