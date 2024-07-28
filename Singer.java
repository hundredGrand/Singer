//Singer.java
// uses sound sample files to turn user-input file containing pitch, syllable, and duration of a
// collection of notes to make a .dat file representing a new song
// Gwen Goetsz
// last edited: 7/20/24


//Reference of ipa symbols that have letter crossover:
// ch: 1
// sh: 2
// ng: 4
// th unvoiced: 0
// th voiced: 9
// zh: 8
// a(cat): a
// ahh(dog): A
// e(able): e
// ee(bee): i
// eh(edible): 3
// ih(in): I
// o(boat): o
// oo(move): O
// u(book): U
// uh(up): u

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.TreeMap;

public class Singer {
	private static final double CONS_DURATION = .18; // Duration for consonant sounds
	private static final int SAMPLE_RATE = 44100; // Sample rate for the output file

	public static void main(String[] args) throws FileNotFoundException {
		Scanner kb = new Scanner(System.in);
		String songFileName = "songFile.txt";

		// Create TreeMaps for vowel and consonant values
		TreeMap<String, Double> vowelVals = createVowelTree(openInFile("vowelVals.txt"));
		TreeMap<String, String> consVals = consonantTree();

		// Open song file and output file
		Scanner songFileScanner = openInFile(songFileName);
		PrintStream outFile = openOutputFile(kb);

		// Write header to output file
		String firstLine  = "; Sample Rate " + SAMPLE_RATE;
		String secondLine = "; Channels 1";
		outFile.println(firstLine);
		outFile.println(secondLine);

		// Process the song file
		processSong(songFileScanner, outFile, vowelVals, consVals);
	}

	/**
	 * Opens a file and returns a Scanner to read it
	 * @param fileName name of the file to open
	 * @return Scanner reading the input file
	 * @throws FileNotFoundException
	 */
	public static Scanner openInFile(String fileName) throws FileNotFoundException {
		System.out.println("Attempting to open file: " + fileName); // Debugging statement
		File f = new File(fileName);
		System.out.println("Absolute path: " + f.getAbsolutePath()); // Debugging statement
		if (!f.canRead()) {
			System.out.println("File not found or cannot be read.");
		}
		Scanner infile = new Scanner(f);
		return infile;
	}

	/**
	 * Opens an output file
	 * @param keyboard Scanner object to read in file name
	 * @return PrintStream object opened on output file
	 * @throws FileNotFoundException
	 */
	private static PrintStream openOutputFile(Scanner keyboard) throws FileNotFoundException {
		// Open output data file
		String outFileName;
		System.out.print("Enter the name of the output file: ");
		outFileName = keyboard.nextLine();
		File f2 = new File(outFileName);
		PrintStream outfile = new PrintStream(f2);
		return outfile;
	}

	/**
	 * Adds a vowel sound to the output file for the allotted time
	 * @param pitch value of pitch to be printed to outfile
	 * @param ipa International Phonetic Alphabet (IPA) symbol for the vowel
	 * @param timeVal amount of time the vowel sound should be sung for
	 * @param vowelVals TreeMap of vowel values
	 * @param timePassed total amount of time passed
	 * @param outFile output file stream
	 * @return double total amount of time passed
	 */
	public static double addVowel(String pitch, String ipa, Double timeVal, TreeMap<String, Double> vowelVals, double timePassed, PrintStream outFile) {
		double sound = vowelVals.get(ipa + "_" + pitch); // Get the sound value for the vowel and pitch

		// Write the sound value to the output file for the duration of the vowel
		for (double i = timePassed; i < timePassed + timeVal; i += (1.00 / SAMPLE_RATE)) {
			outFile.printf("  %14.8g", timePassed);
			outFile.print("\t");
			outFile.printf("%10.6g", sound);
			outFile.println();
		}
		return timeVal;
	}

	/**
	 * Adds a consonant sound to the output file
	 * @param ipa International Phonetic Alphabet (IPA) symbol for the consonant
	 * @param pitch value of pitch to be printed to outfile
	 * @param timePassed total amount of time passed
	 * @param consVals TreeMap of consonant values
	 * @param outFile output file stream
	 * @return double total amount of time passed
	 */
	public static double addConsonant(String ipa, String pitch, double timePassed, TreeMap<String, String> consVals, PrintStream outFile) {
		Scanner stringScanner;
		if (isVoiced(ipa)) {
			// Get the consonant values for the voiced consonant and pitch
			stringScanner = new Scanner(consVals.get(ipa + "_" + pitch));
		} else {
			// Get the consonant values for the unvoiced consonant
			stringScanner = new Scanner(consVals.get(ipa));
		}

		// Write the consonant values to the output file
		while (stringScanner.hasNextLine()) {
			double time = stringScanner.nextDouble() + timePassed;
			double sound = stringScanner.nextDouble();

			outFile.printf("  %14.8g", time);
			outFile.print("\t");
			outFile.printf("%10.6g", sound);
			outFile.println();
		}
		return CONS_DURATION;
	}

	/**
	 * Reads consonant sound files and creates a TreeMap mapping each symbol to its .18s long clip
	 * @return TreeMap of consonant values
	 * @throws FileNotFoundException
	 */
	public static TreeMap<String, String> consonantTree() throws FileNotFoundException {
		TreeMap<String, String> consTree = new TreeMap<>();
		String[] consFileNames = new String[] {
				"b_C4.dat", "1.dat", "d_C4.dat", "f.dat", "g_C4.dat", "h.dat", "j_C4.dat", "k.dat",
				"l_C4.dat", "m_C4.dat", "n_C4.dat", "4_C4.dat", "p.dat", "r_C4.dat", "s.dat", "2.dat",
				"t.dat", "0.dat", "9_C4.dat", "z_C4.dat", "8_C4.dat"
		};

		// Read each consonant file and add its values to the TreeMap
		for (int i = 0; i < consFileNames.length; i++) {
			Scanner fileScanner = openInFile(consFileNames[i]);
			String vals = "";
			while (fileScanner.hasNextLine()) {
				vals += fileScanner.nextLine() + "\n";
			}
			consTree.put(consFileNames[i].substring(0, consFileNames[i].length() - 4), vals);
		}
		return consTree;
	}

	/**
	 * Creates a TreeMap of vowel values from a file
	 * @param fileScanner Scanner object reading the vowel values file
	 * @return TreeMap of vowel values
	 */
	public static TreeMap<String, Double> createVowelTree(Scanner fileScanner) {
		TreeMap<String, Double> vowelTree = new TreeMap<>();

		// Read each line from the file and add the vowel value to the TreeMap
		while (fileScanner.hasNextLine()) {
			String note = fileScanner.next();
			Double val = fileScanner.nextDouble();
			vowelTree.put(note, val);
		}
		return vowelTree;
	}

	/**
	 * Processes the song file and writes the corresponding sound values to the output file
	 * @param songFileScanner Scanner object reading the song file
	 * @param outFile PrintStream object for the output file
	 * @param vowelVals TreeMap of vowel values
	 * @param consVals TreeMap of consonant values
	 */
	public static void processSong(Scanner songFileScanner, PrintStream outFile, TreeMap<String, Double> vowelVals, TreeMap<String, String> consVals) {
		double timePassed = 0.0;
		while (songFileScanner.hasNextLine()) {
			double duration = songFileScanner.nextDouble();
			String ipa = songFileScanner.next();
			String pitch = songFileScanner.next();

			int numCons = numCons(ipa);
			int numVowels = ipa.length() - numCons;

			// Process each character in the IPA string
			for (int i = 0; i < ipa.length(); ++i) {
				if (isCons(ipa.substring(i, i + 1))) {
					// Add consonant sound to the output file
					timePassed += addConsonant(ipa.substring(i, i + 1), pitch, timePassed, consVals, outFile);
				} else {
					// Calculate the duration for each vowel and add it to the output file
					double timeVal = (duration - (CONS_DURATION * numCons)) / numVowels;
					timePassed += addVowel(pitch, ipa.substring(i, i + 1), timeVal, vowelVals, timePassed, outFile);
				}
			}
		}
	}

	/**
	 * Counts the number of consonants in a word
	 * @param word the word
	 */
	public static int numCons(String word) {
		int num = 0;

		for (int i = 0; i < word.length(); ++i) {
			String letter = word.substring(i,i);
			if (isCons(letter)) {
				num++;
			}
		}
		return num;
	}

	/**
	 * isVoiced
	 * determines whether a consonant is voiced
	 * @param cons consonant to be judged
	 * @return boolean if it is voiced
	 */
	public static boolean isVoiced(String cons) {
		return (cons.equals("m") || cons.equals("b") || cons.equals("d") || cons.equals("g") ||
		cons.equals("j") || cons.equals("l") || cons.equals("n") || cons.equals("4") ||
				cons.equals("r") || cons.equals("ð") || cons.equals("z") ||cons.equals("ʒ"));
	}

	/**
	 * isCons
	 * determines whether a letter is a consonant
	 * @param letter consonant to be judged
	 * @return boolean if it is consonant
	 */
	public static boolean isCons(String letter) {
		return (letter.equals("m") || letter.equals("1") || letter.equals("b") ||
				letter.equals("d") || letter.equals("g") || letter.equals("f") || letter.equals("h") ||
				letter.equals("j") || letter.equals("l") || letter.equals("n") || letter.equals("4") ||
				letter.equals("r") || letter.equals("9") || letter.equals("z") || letter.equals("8")
				|| letter.equals("k") || letter.equals("p") || letter.equals("s") || letter.equals("2")
				|| letter.equals("t") || letter.equals("0"));
	}
}
