# Singer
converts user input notes to sound reference dat file

This repository contains two main components:
1. **Singer**: A Java program that converts phonetic notations and musical notes into synthesized singing.
2. **SoX Pitch Shifter**: A script for pitch shifting audio files using SoX (Sound eXchange), a command-line utility.

## Singing Synthesizer

### Overview
The Singer program reads phonetic notations (IPA) and corresponding musical notes from a text file and generates synthesized singing. It writes the output to a text file with time-stamped sound values.

### Requirements
- Java 8 or higher
- Input files: `vowelVals.txt`, consonant `.dat` files (e.g., `b_C4.dat`), and `songFile.txt`

### Usage
1. **Compile the program**:
    ```sh
    javac Singer.java
    ```
2. **Run the program**:
    ```sh
    java Singer
    ```
    You will be prompted to enter the name of the output file.

### File Formats

#### input song:
A text file containing the song to be synthesized. Should have the format of "(double)length (String)syllable (String)note
Example: 0.5 mik C4

###Note: ipa symbols cannot be processed by this program so I used these substitutions:
 ch: 1
 sh: 2
 ng: 4
 th unvoiced: 0
 th voiced: 9
 zh: 8
 a(cat): a
 ahh(dog): A
 e(able): e
 ee(bee): i
 eh(edible): 3
 ih(in): I
 o(boat): o
 oo(move): O
 u(book): U
 uh(up): u

 
## SoX Pitch Shifter

### Overview
The SoX Pitch Shifter script uses SoX to pitch shift audio files. It can adjust the pitch of audio files by a specified number of semitones.

### Requirements
- SoX (Sound eXchange) installed

### Installation
1. **Install SoX**:
    - **Mac**:
      ```sh
      brew install sox
      ```
    - **Linux**:
      ```sh
      sudo apt-get install sox
      ```
    - **Windows**:
      Download and install from the [official website](http://sox.sourceforge.net/).

### Usage
```sh
sox input.wav output.wav pitch <semitones>
