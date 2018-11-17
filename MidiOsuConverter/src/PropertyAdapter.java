import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Properties;

import javax.swing.JOptionPane;


/**
 * user preference settings
 * @author DH
 *
 */
public final class PropertyAdapter {
	
	// Path
	public static final String PATH = getCurrentWorkingPath();
	public static final String FULL_PATH = PATH + "\\config.properties";
	// Boolean key
	public static final String CUSTOM_HITSOUND = "CustomHS";
	public static final String EXTRACT_HITSOUND = "extraction";
	public static final String COOP = "coop";
	public static final String MERGE_HITSOUND = "mergeHS";
	// Integer key
	public static final String KEY_COUNT = "keyCount";
	public static final String OVERALL_DIFFICULTY = "OD";
	public static final String MAX_CHORD = "maxChord";
	// String key
	public  static final String MIDI_PATH = "midiPath";
	
	// Default values
	private static final boolean DEFAULTS_CUSTOM_HITSOUND = false;
	private static final boolean DEFAULTS_EXTRACT_HITSOUND = true;
	private static final boolean DEFAULTS_COOP = false;
	private static final boolean DEFAULTS_MERGE_HITSOUND = true;
	private static final int DEFAULTS_KEY_COUNT = 7;
	private static final int DEFAULTS_OVERALL_DIFFICULTY = 10;
	private static final int DEFAULTS_MAX_CHORD = 5;
	
	public static String getCurrentWorkingPath() {
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		File file = new File(loader.getResource("Main.class").getPath()).getParentFile().getParentFile();
		String path = file.getPath();
		path = path.replace("file:\\", "");
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(path);
		
		return path;
	}
	
	public static void writeToProperty(String key, Object value){
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			File f = new File(FULL_PATH);
        	if (!f.exists()){
        		f.createNewFile();
        	}
			FileInputStream input = new FileInputStream(FULL_PATH);
			prop.load(input);
			prop.setProperty(key,value.toString());
			input.close();
			// save properties to project root folder
			output = new FileOutputStream(FULL_PATH);
			prop.store(output, null);
			output.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,e.getMessage());
		}
	}
	
	public static String readFromProperty(String key){
		Properties prop = new Properties();
        InputStream input = null;
        try {
        	File f = new File(FULL_PATH);
        	if (!f.exists()){
        		f.createNewFile();
        		return null;
        	}
            input = new FileInputStream(FULL_PATH);
            prop.load(input);
            String s = prop.getProperty(key);
            input.close();
            /* Prevent exception in case the properties file is changed and no value is found with correct key.
            In that case, the default value is used.
            */
            if ( s != null){
            	return s;
            }
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null,"PropertyAdapter got error reading from config file for key = " + key);
        }
        return "";
        
	}
	

	
	public static int readIntegerFromProperty(String key) {
		String value = readFromProperty(key);
		if (value != null){
			return Integer.parseInt(value);
		}
		switch(key){
		case KEY_COUNT:
			return DEFAULTS_KEY_COUNT;
		case OVERALL_DIFFICULTY:
			return DEFAULTS_OVERALL_DIFFICULTY;
		case MAX_CHORD:
			return DEFAULTS_MAX_CHORD;
			
		}
		//should never return this value
		return -1;

    }
	
	  
	  public static boolean readBooleanFromProperty(String key) {
		  String value = readFromProperty(key);
			if (value != null){
				return value.equalsIgnoreCase("true");
			}
		  switch(key){
		  case CUSTOM_HITSOUND:
			  return DEFAULTS_CUSTOM_HITSOUND;
		  case EXTRACT_HITSOUND:
			  return DEFAULTS_EXTRACT_HITSOUND;
		  case COOP:
			  return DEFAULTS_COOP;
		  case MERGE_HITSOUND:
			  return DEFAULTS_MERGE_HITSOUND;
	    }
		  // should never return this value
		  return false;
	  } 
}
