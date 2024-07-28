//SoxPitchShifter.java
//converts original .dat sound reference file to other keys by shifting using pre-installed sox
//Gwen Goetz
//last edited; 7/16/24


import java.io.IOException;
import java.util.Scanner;

public class SoxPitchShifter {

	// Original sample file
	public static void main(String[] args) {

		Scanner kb = new Scanner(System.in);
		System.out.print("Please enter the name of your original file: ");
		String originalName = kb.next();

		// Array of pitch shifts (in cents) for one octave
		int[] shifts = {
				-800, -700, -600, -500, -400, -300, -200, -100, 0, 100, 200, 300, 400,
				500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600,
				1700, 1800, 1900, 2000, 2100, 2200, 2300, 2400, 2500, 2600, 2700, 2800
		};

		// Array of note names
		String[] notes = {
				"F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4", "C#4", "D4", "D#4", "E4", "F4",
				"F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5", "F5",
				"F#5", "G5", "G#5", "A5", "A#5", "B5", "C6", "C#6", "D6", "D#6", "E6", "F6"
		};

		try {
			generatePitchShiftedFiles(originalName, shifts, notes);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void generatePitchShiftedFiles(String original, int[] shifts, String[] notes)
			throws IOException, InterruptedException {
		for (int i = 0; i < shifts.length; i++) {
			int pitchShift = shifts[i];
			String noteName = notes[i];
			String outputFile = "ee_" + noteName + ".wav";
			runSoXCommand(original, outputFile, pitchShift);
		}
	}

	private static void runSoXCommand (String inputFile, String outputFile,int pitchShift) throws
	IOException, InterruptedException {
		// Build the SoX command
		String command = String.format("sox %s %s pitch %d", inputFile, outputFile, pitchShift);

		// Create a process builder
		ProcessBuilder processBuilder = new ProcessBuilder("sh", "-c", command);
		processBuilder.inheritIO();  // Redirects standard output and error to the console

		// Start the process
		Process process = processBuilder.start();

		// Wait for the process to complete
		int exitCode = process.waitFor();

		if (exitCode != 0) {
			System.err.println("SoX command failed with exit code " + exitCode);
		}
	}
}
