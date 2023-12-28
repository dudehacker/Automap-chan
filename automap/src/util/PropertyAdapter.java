package util;

import javax.swing.*;
import java.io.*;
import java.util.Properties;


/**
 * user preference settings
 *
 * @author DH
 */
public final class PropertyAdapter {

    // Path

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
    public static final String MIDI_PATH = "midiPath";
    public static final String PATH = System.getProperty("user.dir");
    private static final String propertyName = "config.properties";
    public static final String FULL_PATH = PATH + File.separator + propertyName;
    // Default values
    private static final boolean DEFAULTS_CUSTOM_HITSOUND = false;
    private static final boolean DEFAULTS_EXTRACT_HITSOUND = true;
    private static final boolean DEFAULTS_COOP = false;
    private static final boolean DEFAULTS_MERGE_HITSOUND = true;
    private static final int DEFAULTS_KEY_COUNT = 7;
    private static final int DEFAULTS_OVERALL_DIFFICULTY = 10;
    private static final int DEFAULTS_MAX_CHORD = 5;

    public static void writeToProperty(String key, Object value) {
        Properties prop = new Properties();
        String propertyPath = FULL_PATH;
        try {
            File f = new File(propertyPath);
            if (!f.exists()) {
                f.createNewFile();
            }
            FileInputStream input = new FileInputStream(propertyPath);
            prop.load(input);
            // System.out.println(OsuPath);
            prop.setProperty(key, value.toString());
            input.close();
            // save properties to project root folder
            OutputStream output = new FileOutputStream(propertyPath);
            prop.store(output, null);
            output.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("debug write to property file: " + propertyPath);
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public static String readFromProperty(String key) {
        Properties prop = new Properties();
        InputStream input;
        try {
            String propertyPath = FULL_PATH;
            File f = new File(propertyPath);
            if (f.exists() && f.isFile()) {
                input = new FileInputStream(propertyPath);
                prop.load(input);
                String s = prop.getProperty(key);
                input.close();
                return s;
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "util.PropertyAdapter got error reading from config file for key = " + key);
        }
        return "";
    }

    private static boolean checkStringInput(String string) {
        return (string != null && !string.isEmpty());
    }

    public static int readIntegerFromProperty(String key) {
        String value = readFromProperty(key);
        if (checkStringInput(value)) {
            return Integer.parseInt(value);
        }
        switch (key) {
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
        if (checkStringInput(value)) {
            return value.equalsIgnoreCase("true");
        }
        switch (key) {
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
