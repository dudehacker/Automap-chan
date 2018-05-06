import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;



public class Utils {
	//Constants
	private static float sampleRate = 4000;
	private static int sampleSizeInBits = 8;
	private static int channels = 2; //stereo
	
	public static void createEmptyWAV(int durationMS,String fileName){
		File outputFile = new File(fileName+".wav");
		int durationBits = durationMS*8;
		byte[] data = new byte [durationBits];
		for (int i = 0; i<durationBits;i++){
			data[i]=0;
		}
		AudioFormat audioFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels , true, false);
		//AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,sampleRate,sampleSizeInBits,channels,frameSize,frameRate,bigEndian);
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		AudioInputStream audioInputStream = new AudioInputStream(bis, audioFormat, data.length/audioFormat.getFrameSize());
		try {
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void convertHStoOGG(String path){
		File folder = new File(path);
		if (folder.isDirectory() && folder.listFiles().length != 0){
			File[] files = folder.listFiles();
			for (int i = 0; i<files.length;i++){
				File source = files[i];
				File target = new File(getFilenameWithoutExtensionFromPath(source.getAbsolutePath())+".ogg");
				AudioAttributes audio = new AudioAttributes();
				audio.setCodec("libvorbis");
				audio.setBitRate(new Integer(256000));
				audio.setChannels(channels);
				audio.setSamplingRate(new Integer(44100));
				EncodingAttributes attrs = new EncodingAttributes();
				attrs.setFormat("ogg");
				attrs.setAudioAttributes(audio);

				Encoder encoder = new Encoder();

				try {
					encoder.encode(source, target, attrs);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InputFormatException e) {
					System.out.println(i);
					System.out.println(source + "\n" + target + "\n" + attrs);
					
					e.printStackTrace();
				} catch (EncoderException e) {
					
					e.printStackTrace();
				}
				// Delete wav
				source.delete();
				target.renameTo(source);
			}
		}
	}
	
	public static void createEmptyMp3FromWAV(String fileName){
		File source = new File(fileName+".wav");
		File target = new File(fileName+".mp3");
		AudioAttributes audio = new AudioAttributes();
		audio.setCodec("libmp3lame");
		audio.setBitRate(new Integer(250));
		audio.setChannels(channels);
		audio.setSamplingRate(new Integer(8000));
		EncodingAttributes attrs = new EncodingAttributes();
		attrs.setFormat("mp3");
		attrs.setAudioAttributes(audio);
		Encoder encoder = new Encoder();
		try {
			encoder.encode(source, target, attrs);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InputFormatException e) {
			e.printStackTrace();
		} catch (EncoderException e) {
			e.printStackTrace();
		}
		// Delete wav
		source.delete();
		
	}
	
	/**
	 * 
	 * @param timeline
	 *            in ticks
	 * @param bpms
	 *            array of bpm
	 * @param time
	 *            in tick, current time
	 * @return bpm of that section
	 */
	public static int getBpm(ArrayList<Long> timeline, ArrayList<Long> tempoArray,
			long time) {
		if (timeline.size() == 1) {
			return tempoToBpm(tempoArray.get(0));
		}
		int bpm = -1;
		for (int i = 0; i < timeline.size() - 1; i++) {
			long t1 = timeline.get(i);
			long t2 = timeline.get(i + 1);
			if (time == t1) {
				return tempoToBpm(tempoArray.get(i));
			} else if (time > t1 && time < t2) {
				return tempoToBpm(tempoArray.get(i));
			} else if (time == t2) {
				return tempoToBpm(tempoArray.get(i+1));
			} else if (i == timeline.size() - 2) {
				return tempoToBpm(tempoArray.get(i+1));
			}
		}
		return bpm;
	}
	
	public static int tempoToBpm(long tempo){
		return (int) Math.round(60000000.0/tempo);
	}

	/**
	 * Find the index of the section the input time belongs to within a timeline
	 * 
	 * @param currentTime
	 *            in tick or ms
	 * @param timeline
	 *            in tick or ms
	 * @return index of timeline section
	 */
	public static int getIndexFromTimeline(long currentTime,
			ArrayList<Long> timeline) {
		int output = -1;
		if (timeline.size() == 1) {
			return 0;
		}
		for (int i = 0; i < timeline.size() - 1; i++) {
			long t1 = timeline.get(i);
			long t2 = timeline.get(i + 1);
			if (currentTime == t1) {
				return i;
			} else if (currentTime > t1 && currentTime < t2) {
				return i;
			} else if (currentTime == t2) {
				return i + 1;
			} else if (i == timeline.size() - 2) {
				return i + 1;
			}
		}
		return output;

	}
	
	/**
	 * Convert bpm to ms per beat
	 * @param bpm 
	 * @return float mspb
	 */
	public static float bpmTomspb(int bpm){
		float mspb;
		mspb = (float) (60000L*1.0/bpm);
		return mspb;
	}

	public static float tempoToMilliSec(long tempo){
		float output = (float) (tempo/1000.0);
		return output;  
	}
	
	/**
	 * 
	 * @param v velocity of note
	 * @return rounded velocity to multiple of 10, with min of 10
	 */
	public static int roundVelocity10(int v){
		int factor = (int) Math.round(v/10.0);
		return factor * 10;
	}
	
	public static int roundVelocity20(int v){
		int factor = (int) Math.round(v/20.0);
		return factor * 20;
	}
	
	public static byte[] toByteArray(double value) {
	    byte[] bytes = new byte[8];
	    ByteBuffer.wrap(bytes).putDouble(value);
	    return bytes;
	}

	public static double toDouble(byte[] bytes) {
	    return ByteBuffer.wrap(bytes).getDouble();
	}
	
	private static byte[] removeHitSoundStartSilence(byte[] input){
		// find index of first non-zero sample
		int index=0;
		for (int j = 0; j<input.length;j++){
			if (input[j]!=0){
				index = j;
				break;
			}
		}

		byte[] output = new byte[input.length-index];
		for (int i = 0; i<output.length; i++){
			output[i] = input[index+i];
		}
		return output;
	}
	
	
	public static double[] toDoubleArray(byte[] input,int bytesPerFrame){
		double[] output = new double[input.length/bytesPerFrame];
		int counter=0;
		for (int i=0;i<input.length;i=i+bytesPerFrame){
			
			byte[] data = new byte[bytesPerFrame];
			for (int j=0;j<bytesPerFrame;j++){
				data[j] = input[i+j];
			}
			double value = toDouble(data);
			//System.out.println(value);
			output[counter]=value;
			counter++;
		}

		return output;
	}
	
	
	/**
	 * 
	 * @param m midi note number (related to pitch)
	 * @return frequency of that sound
	 */
	public static int pitchToFrequency(int m){
		int f = (int) (Math.pow(2, ((m-69)/12.0)  )*440);
		System.out.println("m = "+m +" , f = "+ f);
		return f;

	}
	
	public static byte[] convertToBytesArray(short[] shorts, ByteOrder order){
		byte[] bytes = new byte[shorts.length * 2];
		ByteBuffer.wrap(bytes).order(order).asShortBuffer().put(shorts);
		return bytes;
	}
	
	public static byte findPeak(byte[] input){
		byte peak = 0;
		for (int i = 0; i<input.length;i++){
			byte value = (byte) Math.abs(input[i]);
			if (value>peak){
				peak = value;
			}
		}
		return peak;
	}
	
	public static short findPeak(short[] input){
		short peak = 0;
		for (int i = 0; i<input.length;i++){
			short value = (short) Math.abs(input[i]);
			if (value>peak){
				peak = value;
			}
		}
		return peak;
	}
	
	/**
	 * Write to text file at specified location
	 * 
	 * @param filePath
	 *            output location
	 * @param text
	 *            data to write to file
	 */
	public static void writeToFile(WindowProgress window,String filePath, String text) {
		OutputStreamWriter writer = null;
		try {
			// create a temporary file
			File logFile = new File(filePath);

			// This will output the full path where the file will be written
			// to...
			window.display(logFile.getCanonicalPath());

			writer = new OutputStreamWriter(new FileOutputStream(logFile),"UTF-8");
			writer.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.flush();
				writer.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static short[] convertToShortArray(byte[] bytes, ByteOrder order ){
		short[] shorts = new short[bytes.length/2];
		ByteBuffer.wrap(bytes).order(order).asShortBuffer().get(shorts);
		return shorts;
	}
	
	private static byte[] removeHitSoundEndSilence(byte[] input, int bitDepth, ByteOrder order){
		byte [] output;
		int index = 0;
		if (bitDepth == 16){
			short[] shorts = convertToShortArray(input, order);
			index = shorts.length-1;
			while(index >= 0){
				int value = shorts[index];
				//System.out.println(value);
				if (value > 50){
					//System.out.println("value "+value + " > " + peak*threshold );
					break;
				}
				index--;
			}
			short[] cutArray = new short[index+1];
			for (int i = 0; i <= index; i++){
				cutArray[i] = shorts[i];
			}
			output = convertToBytesArray(cutArray,order);
			return output;
		} else if(bitDepth==8){
			index = input.length-1;
			while(index >= 0){
				int value = input[index];
				//System.out.println(value);
				if (value > 0){
					//System.out.println("value "+value + " > " + peak*threshold );
					break;
				}
				index--;
			}
			byte[] cutArray = new byte[index+1];
			for (int i = 0; i <= index; i++){
				cutArray[i] = input[i];
			}
			return cutArray;
		}
		
		return null;
	}
	
	public static byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}

	
	public static byte[] removeHitSoundSilence(byte[] input, int bitDepth, ByteOrder order){
		//System.out.println("length at start "+input.length);
		input = removeHitSoundStartSilence(input);
		//System.out.println("length before cut "+input.length);
		input = removeHitSoundEndSilence(input,bitDepth,order);
		//System.out.println("length after cut "+input.length);
		return input;
	}
	
	
	public static int findAverage(ArrayList<Integer> array) {
		int avg = 0;
		int sum = 0;

		for (int i = 0; i < array.size(); i++) {
			sum = sum + array.get(i);
		}
		avg = (int) Math.round(sum * 1.0 / array.size());
		// System.out.println("average = "+avg);
		return avg;
	}

	/**
	 * return the ms duration of a tick duration
	 * 
	 * @param tickDuration
	 *            in ticks
	 * @param resolution
	 *            ticks per beat
	 * @param bpm
	 *            beat per minute
	 * @return ms duration
	 */
	public static long tickToMilliSec(long tickDuration, int resolution, int bpm) {
		long absT;
		double nb_beats = (tickDuration * 1.0) / resolution;
		double nb_min = nb_beats / bpm;
		absT = Math.round(nb_min * 60000);
		return absT;
	}
	
	public static long tickToMilliSec(long tickDuration, int resolution, long microSecPerBeat){
		double nb_beats = tickDuration * 1.0/ resolution;
		long ms = Math.round(nb_beats * microSecPerBeat / 1000.0);
		return ms;
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	

	public static long tickToMidiTempo(int bpm){
		 if (bpm==0){
			 throw new IllegalArgumentException("bpm is zero");
		 }
		long tempo = (long) ((60.0/bpm)*1000*1000);

		return tempo;
	}

	public static int findMode(ArrayList<Integer> array) {
		Collections.sort(array);
		int i = 1;
		int mode = array.get(0);
		int count = findAppearance(array, mode);
		while (i < array.size()) {
			if (array.get(i) != mode) {
				int tempCount = findAppearance(array, array.get(i));
				if (tempCount > count) {
					mode = array.get(i);
					count = tempCount;
				}
			}
			i++;
		}

		return mode;
	}

	public static int findAppearance(ArrayList<Integer> array, Integer nb) {
		int count = 0;
		@SuppressWarnings("unchecked")
		ArrayList<Integer> tempArray = (ArrayList<Integer>) array.clone();
		while (tempArray.contains(new Integer(nb))) {
			count++;
			tempArray.remove(new Integer(nb));
		}
		return count;
	}

	public static void createFolder(String path) {
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.mkdir();
			} catch (SecurityException se) {
				se.printStackTrace();
			}

		}
	}

	public static String getFilenameWithoutExtensionFromPath(String filename){
		String name = filename.substring(0, filename.length()-4);
		return name;
	}
	
	public static boolean isFolderEmpty(String path) {
		boolean result = true;
		File file = new File(path);
		if (file.isDirectory() && file.list().length != 0) {
			result = false;
		}
		return result;
	}
	
	public static void emptyFolder(String path) {
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null) { // some JVMs return null for empty dirs
				for (File f : files) {
					if (f.isDirectory()) {
						emptyFolder(f.getAbsolutePath());
					} else {
						f.delete();
					}
				}
			}
		}
	}
}
