import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class NoteArray {
	// Instance variables
	private ArrayList<Note> notes;
	private ArrayList<Note> BGnotes;
	private int counter = 0;

	// Constructor
	public NoteArray() {
		notes = new ArrayList<Note>();
		BGnotes = new ArrayList<Note>();
	}

	public void add(Note n) {
		if (n.getName() == null){
			System.out.println("NoteArray.add(Note n) Exception: Name is null!");
			System.exit(-1);
		}
		notes.add(n.clone());
		counter++;
	}
	
	public void addBGNote(Note n){
		BGnotes.add(n.clone());
	}
	
	public void addAllBGNotes(NoteArray bgnotes){
		for (Note n : bgnotes.BGnotes){
			addBGNote(n);
		}
	}
	public void addAll(NoteArray array) {
		for (Note n : array.notes) {
			notes.add(n.clone());
			counter++;
		}
	}

	public void remove(String name) {
		Iterator<Note> ite = notes.iterator();
		while (ite.hasNext()) {
			String value = ite.next().getName();
			if (value.equals(name)) {
				ite.remove();
				return;
			}
		}
	}

	public void remove(Note n) {
		String name = n.getName();
		remove(name);

	}

	@Override
	@SuppressWarnings("unchecked")
	public NoteArray clone() {
		NoteArray result = new NoteArray();
		result.notes = (ArrayList<Note>) notes.clone();
		result.counter = counter;

		return result;
	}

	/**
	 * Search through this NoteArray object to find a Note object of same name
	 * as Note n
	 * 
	 * @param n
	 *            Note object to search for
	 * @return a Note object of same name as input if it exist, else throw
	 *         exception
	 */
	public Note getNote(Note n) {
		Iterator<Note> ite = notes.iterator();
		Note note = null;
		while (ite.hasNext()) {
			note = ite.next();
			if (note.getName().equals(n.getName())) {
				return note;
			}
		}

		System.out.println("Could not find the specific note");
		throw new IllegalArgumentException();
	}

	public Note getNoteFromIndex(int n) {
		return notes.get(n);
	}
	
	public Note getBGNoteFromIndex(int n) {
		return BGnotes.get(n);
	}

	public boolean contains(Note n) { // check if the array of notes contains a
										// note with same name
		Iterator<Note> ite = notes.iterator();
		while (ite.hasNext()) {
			Note note = ite.next();
			if (note.getName().equals(n.getName())) {
				return true;
			}
		}
		System.out.println("Note being serached " + n.toString());
		System.out.println("Note is not found");
		System.out.println("Note array: \n" + toString());

		//System.exit(-1);
		return false;
	}

	public boolean contains(Note n, int tempo) {
		// check if the arraylist contains the note with unique duration
		if (n.getName() == null){
			System.out.println("cant check note with null name");
			System.exit(-1);
		}
		//System.out.println(n.getName() + " duration " +n.getDuration() + "  BPM  " + n.getBPM());
		for (Note note : notes) {
			if (note.getInstrumentName().equals(n.getInstrumentName())) {
				//System.out.println(note.getName());

				if (note.getName().equals(n.getName())
						 && note.getBPM() == tempo) {
					return true;
				}
			}

		}
		return false;
	}
	
	public boolean contains(Note n, int d, int tempo) {
		// check if the arraylist contains the note with unique duration
		if (n.getName() == null){
			System.out.println("cant check note with null name");
			System.exit(-1);
		}
		//System.out.println(n.getName() + " duration " +n.getDuration() + "  BPM  " + n.getBPM());
		for (Note note : notes) {
			if (note.getInstrumentName().equals(n.getInstrumentName())) {
				//System.out.println(note.getName());

				if (note.getName().equals(n.getName())
						&& note.getDuration() == d && note.getBPM() == tempo) {
					return true;
				}
			}

		}
		return false;
	}

	public void sortByName() {
		Collections.sort(notes, Note.NoteNameComparator);
	}

	public void sortByDuration() {
		Collections.sort(notes, Note.DurationComparator);
	}

	public void sortByPitch() {
		Collections.sort(notes, Note.PitchComparator);
	}

	@Override
	public String toString() {
		String s = "Total of " + notes.size() + " notes\n";
		for (Note n : notes) {
			s = s + n.getName() + ", start time = " + n.getTime()
					+ ", duration = " + n.getDuration() + " ticks, velocity = "
					+ n.getVelocity() + "\n";
		}
		return s;
	}

	public String debugTime() {
		String s = "Total of " + notes.size() + " notes\n";
		for (Note n : notes) {
			s = s + n.getName() + ", start time = " + n.getAbs()
					+ ", duration = " + n.getDuration() + " ticks, velocity = "
					+ n.getVelocity() + "\n";
		}
		return s;
	}

	/**
	 * Sort the list of notes by its absolute start time and return an array
	 * containing groups of notes that start at same time
	 * 
	 * @return ArrayList of NoteArray
	 */
	public ArrayList<NoteArray> sortNotesByTime() {
		if (notes.size()==0){
			throw new IllegalArgumentException("NoteArray is empty!");
		}
		// sort the whole array by its start time
		Collections.sort(notes, Note.StartTimeComparator);
		ArrayList<NoteArray> output = new ArrayList<>();
		Iterator<Note> ite = notes.iterator();
		long previousT = -1;
		NoteArray temp = new NoteArray(); // group all notes with same
													// start time together
		while (ite.hasNext()) {
			Note n = ite.next();
			// System.out.println(n.debugTime());
			if (previousT == -1) {
				// first iteration of loop
				previousT = n.getAbs();
				temp.add(n);
			} else {
				// for every other iterations
				if (n.getAbs() == previousT) {
					temp.add(n);
				} else {
					if (temp.getSize()!=0){
						output.add(temp);
					}
					
					previousT = n.getAbs();
					temp = new NoteArray();
					temp.add(n);
				}
			}
		}
		if (temp.getSize()!=0){
			output.add(temp);
		}
		return output;
	}

	public ArrayList<NoteArray> sortBGNotesByTime() {
		// sort the whole array by its start time
		Collections.sort(BGnotes, Note.StartTimeComparator);
		ArrayList<NoteArray> output = new ArrayList<>();
		Iterator<Note> ite = BGnotes.iterator();
		long previousT = -1;
		NoteArray temp = new NoteArray(); // group all notes with same
													// start time together
		while (ite.hasNext()) {
			Note n = ite.next();
			// System.out.println(n.debugTime());
			if (previousT == -1) {
				// first iteration of loop
				previousT = n.getAbs();
				temp.addBGNote(n);
			} else {
				// for every other iterations
				if (n.getAbs() == previousT) {
					temp.addBGNote(n);
				} else {
					output.add(temp);
					previousT = n.getAbs();
					temp = new NoteArray();
					temp.addBGNote(n);
				}
			}
		}
		if (temp.getSize()!=0){
			output.add(temp);
		}
		return output;
	}
	
	
	
	/**
	 * remove notes if the size exceed maxChord and output the string
	 * representation of the Osu background sample
	 * 
	 * @return String
	 */
	public String toBackgroundSample(int volume) {
		String output = "";
		if (BGnotes.size()==0){
			return "";
		}
		int i = 0;
		while (i < BGnotes.size()) {
			output += BGnotes.get(i).toSample(volume);
			i++;
		}

		return output;
	}

	public String toHitObjects(int keyCount, int resolution, int LN_Cutoff,
			int volume) {
		
		if (notes.size()==0){
			return "";
		}
		String output = "";
		int iniKeyCount = keyCount;
		while (notes.size() > keyCount) {
			// if there are too many keys then stack them on top of each other
			keyCount = keyCount + 1;
		}

		ArrayList<Integer> columns = new ArrayList<>();
		for (int i = 0; i < notes.size(); i++){
			columns.add(notes.get(i).getColumn());
		}
		Collections.sort(columns);
		for (int i = 0; i < notes.size(); i++) { // we work under the assumption
													// that notes.size() is
													// smaller than keyCount
			Note n = notes.get(i);
			output += n.toHitObject(columns.get(i), resolution, iniKeyCount,
					LN_Cutoff, volume);
		}

		return output;
		
	}

	public Iterator<Note> iterator() {
		return notes.iterator();
	}

	public int getMaxSize() {
		return counter;
	}

	public int getSize() {
		return notes.size();
	}
	
	public int getBGSize() {
		return BGnotes.size();
	}
	
	public void throwNoteIntoBG(int index) {
		BGnotes.add((notes.get(index)).clone());
		notes.remove(index);
	}
	
	public int notePitchCompare(NoteArray Chord) {
		//compares all pitch levels of current chord to another
		//returns amount of similar ones
		
		ArrayList<Note> currentNoteSet = new ArrayList<Note>();
		ArrayList<Note> otherNoteSet = new ArrayList<Note>();
		
		//add all current chord notes into one list
		for(int i = 0; i < notes.size(); i++)
			currentNoteSet.add(notes.get(i));
		for(int i = 0; i < BGnotes.size(); i++)
			currentNoteSet.add(BGnotes.get(i));
		
		//add all other chord notes into one list
		for(int i = 0; i < Chord.getSize(); i++)
			otherNoteSet.add(Chord.getNoteFromIndex(i));
		for(int i = 0; i < Chord.getBGSize(); i++)
			otherNoteSet.add(Chord.getBGNoteFromIndex(i));
		
		//sort both lists by channel and pitch (with priority on channel)
		Collections.sort(currentNoteSet, Note.PitchComparator);
		Collections.sort(otherNoteSet, Note.PitchComparator);
		Collections.sort(currentNoteSet, Note.ChannelComparator);
		Collections.sort(otherNoteSet, Note.ChannelComparator);
		
		int totalRepeats = 0;
		int i_1 = 0, i_2 = 0;
		
		//go over both lists and count repeating notes
		//we start the loop at indexes 0 0
		//we finish the loop when we reach last index for each list
		//advance the index of smaller channel,key each time
		//if we advanced and reached the end of the list on either one
		//we can just stop looping because there would be no matches no more
		while(i_1 < currentNoteSet.size() && i_2 < otherNoteSet.size()) {
			if(currentNoteSet.get(i_1).getChannel() == otherNoteSet.get(i_2).getChannel() && 
				currentNoteSet.get(i_1).getKey() == otherNoteSet.get(i_2).getKey()) {
				totalRepeats++; //found a perfect match, increments and advance both
				i_1++;
				i_2++;
			}
			else { //if we got here it means there is no match, so we just need to know who to increment
				if(currentNoteSet.get(i_1).getChannel() > otherNoteSet.get(i_2).getChannel() || 
						currentNoteSet.get(i_1).getKey() > otherNoteSet.get(i_2).getKey()) {
					//we found that note1 has higher value than note2
					//so we advance in note2 list
					i_2++;
				}
				else { //otherwise, the reverse
					i_1++;
				}
			}
		}
		
		/* test code
		System.out.print("ChordA C-P List: ");
		for(int i = 0; i < currentNoteSet.size(); i++) {
			System.out.print("" + currentNoteSet.get(i).getChannel() + "," + 
									currentNoteSet.get(i).getKey() + ". ");
		}
		System.out.print(" ChordB C-P List: ");
		for(int i = 0; i < otherNoteSet.size(); i++) {
			System.out.print("" + otherNoteSet.get(i).getChannel() + "," + 
									otherNoteSet.get(i).getKey() + ". ");
		}
		System.out.println("*");
		*/
		
		return totalRepeats;
	}
}
