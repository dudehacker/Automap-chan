package util;

import model.NoteArray;

import java.util.ArrayList;
import java.util.Random;

public class ChartingAlgorithm {

	/*
	 * This class expects to get a ChordArray What it returns is also a
	 * ChordArray
	 * 
	 * it turns garbage column data into playable column data by editing the
	 * column data per note in each chord also, some notes may change into
	 * background notes
	 */

	public ChartingAlgorithm() {

	}

	public void setColumns(ArrayList<NoteArray> chordList, int keyCount,
						   int maxChord, int Difficulty) {
		long LastColumnTime[] = new long[keyCount];
		//long tempLastColumnTime[] = new long[keyCount];
		
		int farBack = 6; //how far back do we remember repetitions
		int HighP; // used to store highest priority column for note placement
		int RepeatP[] = new int[farBack]; // used to store amount of repeating notes per chord from last
		//long JackP[] = new long[farBack]; // timing differences between chords to check if they're jacks
		int RepeatPlaced = 0; //how many repeating notes were placed
		RepeatP[0] = 0;
		int LastNote = 0; //stores the last note column placed for hand spread
		
		reduceAllChords(chordList, maxChord, Difficulty);

		for (int i = 0; i < keyCount; i++) { // initialize to times before
												// offset
			LastColumnTime[i] = -1000;
		}
		
		// test code: get list of repetitions between chords


		for (int i = 0; i < chordList.size(); i++) {
			
			RepeatPlaced = 0;

			
			// System.out.println("");
			// default note spread algorithm
			// for each note per chord, place it down at the most far back used
			// column
			// if some have equal property, randomize placement between those
			for (int j = RepeatPlaced; j < chordList.get(i).getSize(); j++) {
				// for each note, place at far down column...
				HighP = newHighestPriority(keyCount, LastColumnTime, LastNote);
				
				//if(j == 0 && i > (chordList.size() + 1))
				//	HighP = 6 - chordList.get(i).notePitchCompare(chordList.get(i-2));
				
				chordList.get(i).getNoteFromIndex(j).setColumn(HighP);
				LastNote = HighP;
				LastColumnTime[HighP] = chordList.get(i).getNoteFromIndex(j).getTime();
			}
			
			//sort the chord's play notes by pitch when done
			chordList.get(i).sortByPitch();
		}

	}

	private void reduceAllChords(ArrayList<NoteArray> chordList, int maxChord,
			int Difficulty) {
		int[] ChordTimeLimits = { 200, 100, 50, 25, 12 };
		int[] ChordNeighborLimits = { 1, 1, 1, 1, 1 };

		if (Difficulty >= 10) {
			Difficulty = 10;
		} else if (Difficulty <= 1) {
			Difficulty = 1;
		}

		for (int i = 0; i < 5; i++) {
			ChordTimeLimits[i] *= 100 / Difficulty / Difficulty;
		}

		// first cut down entire list up to maxChord limiter
		for (int i = 0; i < chordList.size(); i++) {
			reduceChordSize(chordList.get(i), maxChord);
		}

		// then reduce inside list based on inner logic except for first and last
		for (int i = 1; i < (chordList.size() - 1); i++) { // run over entire chord list
			for (int j = 0; j < 5; j++) { // run over 5 pressure levels
				if ((chordList.get(i + 1).getNoteFromIndex(0).getTime() - chordList
						.get(i - 1).getNoteFromIndex(0).getTime()) < ChordTimeLimits[j]) { 
					// neighbor chords are within time threshold
					if ((chordList.get(i - 1).getSize() + chordList.get(i + 1)
							.getSize()) > ChordNeighborLimits[j]) {
						// neighbor chords are over size threshold
						reduceChordSize(chordList.get(i), 5 - j);
					}
				}
			}
		}
	}

	private void reduceChordSize(NoteArray Chord, int newSize) {
		Chord.sortByPitch();

		while (Chord.getSize() > newSize) {
			Chord.throwNoteIntoBG(0);
		}
	}

	private int newHighestPriority(int keys, long lastUsedTime[], int LastNote) {
		long minTime = lastUsedTime[0] + 1;
		int minIndexList[] = new int[keys];
		int amountOfMins = 0;

		for (int i = 0; i < keys; i++) {
			if (lastUsedTime[i] < minTime) { // found a new minimum last used
												// time

				// first reset all information about last min time used as old
				// info is no longer relevant
				for (int j = 1; j < keys; j++) { // no need to reset first
													// member since we set it
													// later
					minIndexList[j] = -1;
				}

				// update minimum time information to the new one we found
				minTime = lastUsedTime[i];
				minIndexList[0] = i; // init first member of list of relevant
										// min time indexes
				amountOfMins = 1; // right now we only found one member with
									// this min time
			} else if (lastUsedTime[i] <= (minTime + 24)
					&& lastUsedTime[i] >= (minTime - 24)) { // found a minimum
															// last used time
															// equal (or close)
															// to current
															// minimum
				minIndexList[amountOfMins] = i; // remember this index as part
												// of min chord
				amountOfMins++; // remember that we have another index saved /
								// min chord size
			}
			// if index is larger, just iterate, not going to use it
		}
		
		//midindexlist might have either only right, only left, or r+l notes
		//if lastnote was on the right, remove all right options in case r+l
		//if lastnote was on the left, remove all left options in case r+l
		//so... first check if the case is r+l. on others no change
		//if it is, then remove some accordingly
		int noteR = 0, noteL = 0;
		
		for(int p = 0; p < amountOfMins; p++) {
			if(minIndexList[p] < (keys/2)) { //it's on the left
				noteL++;
			}
			else if(minIndexList[p] >= ((keys+1)/2)) { //it's on the right
				noteR++;
			}
		}
		
		int newList[] = new int[9];
		int newAmountOfMins = 0;
		
		//System.out.println("L:" + noteL + " R:" + noteR + ".");
		
		if(noteR > 0 && noteL > 0) { // we have R+L case
			if(LastNote < (keys/2)) { //last note is on the left
				for(int p = 0; p < amountOfMins; p++) {
					//System.out.println("A:" + amountOfMins + " N:" + newAmountOfMins + ".");
					if(minIndexList[p] < (keys/2)) { //it's on the left
						//remove this
					}
					else if(minIndexList[p] >= ((keys+1)/2)) { //it's on the right
						//save this
						newList[newAmountOfMins] = minIndexList[p];
						newAmountOfMins++;
					}
				}
			}
			else if(LastNote >= ((keys+1)/2)) { //last note is on the right
				for(int p = 0; p < amountOfMins; p++) {
					//System.out.println("A:" + amountOfMins + " N:" + newAmountOfMins + ".");
					if(minIndexList[p] < (keys/2)) { //it's on the left
						//save this
						newList[newAmountOfMins] = minIndexList[p];
						newAmountOfMins++;
					}
					else if(minIndexList[p] >= ((keys+1)/2)) { //it's on the right
						//remove this
					}
				}
			}
		}

		// at this point what is expected:
		// minTime is no longer relevant, but it stores the most far back time
		// of a column used
		// minIndexList stores all the indexes that were on that last used time
		// amountOfMins tells us how many indexes like that are there
		//
		// what we do now is randomize a number for the range of amountOfMins
		// then pick an index from minIndexList and return that
		
		Random rand = new Random();
		
		if(newAmountOfMins>0) {
			return newList[rand.nextInt(newAmountOfMins)];
		}
		return minIndexList[rand.nextInt(amountOfMins)];
	}
	
	//gives priority to highest last used time except for current
	private int newRepeatPriority(int keys, long lastUsedTime[], long currentTime, NoteArray Chord) {
		int potentialPlacementColumn[] = new int[keys];
		int amountOfOptions = 0;
		
		for (int i = 0; i < keys; i++)
			potentialPlacementColumn[i] = -1;
		
		for (int i = 0; i < Chord.getSize(); i++) {
			if (Chord.getNoteFromIndex(i).getColumn() >= 0 && //corner case that shouldn't happen... but does?
				lastUsedTime[Chord.getNoteFromIndex(i).getColumn()] < (currentTime - 300)) {
				//not too jacky -> reduce times by -140
				potentialPlacementColumn[amountOfOptions] = Chord.getNoteFromIndex(i).getColumn();
				amountOfOptions++;
			}
		}
		
		Random rand = new Random();
		if(amountOfOptions > 0) {
			return potentialPlacementColumn[rand.nextInt(amountOfOptions)];
		}
		else
			return -1;
	}

}
