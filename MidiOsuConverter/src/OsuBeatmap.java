
public class OsuBeatmap {

	// Instance Variables
	private int OsuVersion = 14;
	// General
	public static String audioName = "audio";
	private String audio = audioName + ".mp3";
	private int audioLeadIn = 0;
	private int previewTime = -1;
	private int countdown = 0;
	private String sampleSet = "Normal";
	private double stackLeniency = 0.7;
	private int mode = 3;
	private int letterboxInBreaks = 0;
	private int specialStyle = 0;
	private int widecreenStoryboard = 0;
	// Editor
	private double distanceSpacing = 1.2;
	private int beatDivisor = 4;
	private int gridSize = 4;
	private double timelineZoom = 1;
	// Metadata
	private String title = "";
	private String titleUnicode = "";
	private String artist = "";
	private String artistUnicode = "";
	private String creator = "Automap-chan";
	private String version = "KS";
	private String source = "";
	private String tags = "";
	private int beatmapID = 0;
	private int beatmapSetID = -1;
	// Difficulty
	private int HP = 8;
	private int keyCount = 7;
	private int OD = 8;
	private int approchRate = 5;
	private double sliderMultiplier = 1.4;
	private double sliderTickRate = 1;
	// Events
	private String bgSamples = "";
	// Timing Points
	private String timingPoints = "";
	// Hit Objects
	private String hitObjects="";
	
	
	// Constructor
	public OsuBeatmap(String title, String hitObjects){
		this.title = title;
		this.hitObjects = hitObjects;
	}
	
	/**
	 * 
	 * @param title name of the song
	 * @param bgNotes storyboard samples
	 * @param hitObjects 
	 */
	public OsuBeatmap(String title, String bgNotes, String hitObjects){
		this.title = title;
		this.titleUnicode = title;
		this.hitObjects = hitObjects;
		bgSamples = bgNotes;
	}
	
	@Override
	public String toString(){
		String output = 
				  "osu file format v" + OsuVersion + "\n\n"
				  
				+ "[General]\n"
				+ "AudioFilename: " + audio + "\n"
				+ "AudioLeadIn: " + audioLeadIn + "\n"
				+ "PreviewTime: " + previewTime + "\n"
				+ "Countdown: " + countdown + "\n"
				+ "SampleSet: " + sampleSet + "\n"
				+ "StackLeniency: " + stackLeniency + "\n" 
				+ "Mode: " + mode + "\n"
				+ "LetterboxInBreaks: " + letterboxInBreaks + "\n"
				+ "SpecialStyle: " + specialStyle + "\n"
				+ "WidecreenStoryboard: " + widecreenStoryboard + "\n\n"
				
				+ "[Editor]\n"
				+ "DistanceSpacing: " + distanceSpacing + "\n"
				+ "BeatDivisor: " + beatDivisor + "\n"
				+ "GridSize: " + gridSize + "\n"
				+ "TimelineZoom: " + timelineZoom + "\n\n"
				
				+ "[Metadata]\n"
				+ "Title: " + title + "\n"
				+ "TitleUnicode: " + titleUnicode + "\n"
				+ "Artist: " + artist + "\n"
				+ "ArtistUnicode: " + artistUnicode + "\n"
				+ "Creator: " + creator + "\n"
				+ "Version: " + version + "\n"
				+ "Source: " + source + "\n"
				+ "Tags: " + tags + "\n"
				+ "BeatmapID: " + beatmapID + "\n"
				+ "MeatmapSetID: " + beatmapSetID + "\n\n"
				
				+ "[Difficulty]\n"
				+ "HPDrainRate: " + HP + "\n"
				+ "CircleSize: " + keyCount + "\n"
				+ "OverallDifficulty: " + OD + "\n"
				+ "ApproachRate: " + approchRate + "\n"
				+ "SliderMultiplier: " + sliderMultiplier + "\n"
				+ "SliderTickRate: " + sliderTickRate + "\n\n"
				
				+ "[Events]\n"
				+ "//Background and Video events\n"
				+ "//Break Periods\n"
				+ "//Storyboard Layer 0 (Background)\n"
				+ "//Storyboard Layer 1 (Fail)\n"
				+ "//Storyboard Layer 2 (Pass)\n"
				+ "//Storyboard Layer 3 (Foreground)\n"
				+ bgSamples + "\n"
				
				+ "[TimingPoints]\n"
				+ timingPoints + "\n"
				
				+ "[HitObjects]\n"
				+ hitObjects ;
		
		return output;
	}


	public int getKeyCount() {
		return keyCount;
	}


	public void setKeyCount(int keyCount) {
		this.keyCount = keyCount;
	}


	public int getHp() {
		return HP;
	}


	public void setHp(int hp) {
		this.HP = hp;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public int getOD() {
		return OD;
	}


	public void setOD(int oD) {
		OD = oD;
	}

	public String getTimingPoints() {
		return timingPoints;
	}

	public void setTimingPoints(String timingPoints) {
		this.timingPoints = timingPoints;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
		this.artistUnicode = artist;
	}
}
