public class HitObject {
	// Instance Variables
	private int type; // 1=circle 128=LN
	private long endLN;
	private int xposition;
	private final int ypos = 192;
	private long startTime;
	private String hitSound;
	private int volume;

	/**
	 * Constructor for a single note
	 * 
	 * @param xpos
	 *            x-position of hit object, max 512
	 * @param t
	 *            start time of hit object
	 * @param vol
	 *            volume from 0 to 100
	 * @param hitSound
	 *            filename of hitsound, in .wav format
	 */
	public HitObject(int xpos, long t, int vol, String hitSound) {
		xposition = xpos;
		startTime = t;
		type = 1;
		endLN = 0;
		volume = vol;
		this.hitSound = hitSound;
	}

	/**
	 * Constructor for a LN
	 * 
	 * @param xpos
	 *            x-position of hit object, max 512
	 * @param t
	 *            start time of hit object
	 * @param volume
	 *            volume from 0 to 100
	 * @param end
	 *            end time of hit object
	 * @param hitSound
	 *            filename of hitsound, in .wav format
	 */
	public HitObject(int xpos, long t, int volume, long end, String hitSound) {
		type = 128;
		xposition = xpos;
		startTime = t;
		endLN = end;
		this.volume = volume;
		this.hitSound = hitSound;
	}

	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		if (endLN == 0) {
			// for a single note
			return "" + xposition + "," + ypos + "," + startTime + "," + 1
					+ "," + 0 + "," + "0:0:0:" + volume + ":" + hitSound + "\n";
		}
		// for a LN
		return "" + xposition + "," + ypos + "," + startTime + "," + 128 + ","
				+ 0 + "," + endLN + ":0:0:0:" + volume + ":" + hitSound + "\n";
	}

}
