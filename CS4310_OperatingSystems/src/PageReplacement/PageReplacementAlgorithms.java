/****************************************************************************
*
*   File: PageReplaceAlgorithms.java
*   Author: Steven Phung
*   Class: CS 4310.01 - Operating Systems
*
*   Project 2: Simulating Page Replacement Manager and Performance Analysis
*   Date last modified: 11/20/2019
*
*   Purpose: This program will either read from 4 different ReferenceString
*   text files or randomly generate simulate Page Replacement using
*   First In First Out, Least Recently Used, and Optimal Algorithms
* 
****************************************************************************/
package PageReplacement;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class PageReplacementAlgorithms {
	
	//Main Method: Allows user to choose which algorithm to run and to use file
	//input or use randomly generated input
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		do {
			System.out.println("Page Replacement Algorithms:\n"
					+ "1. FIFO with file input\n"
					+ "2. LRU with file input\n"
					+ "3. Optimal Algorithm with file input\n"
					+ "4. FIFO, LRU, Optimal Algorithm with randomly generated input\n"
					+ "5. Exit");
			input = keyboard.next();
			if(Integer.parseInt(input) == 1) {
				firstInFirstOut();
			} else if(Integer.parseInt(input) == 2) {
				leastRecentlyUsed();
			} else if(Integer.parseInt(input) == 3) {
				optimalAlgorithm();
			} else if(Integer.parseInt(input) == 4) {
				randomlyGeneratedInput();
			} else if(Integer.parseInt(input) == 5) {
				System.exit(0);
			}
		} while(!input.matches("5"));
		keyboard.close();
	}
	
	//Method: firstInFirstOut()
	//Purpose: Reads from 4 given files, places data into array structures, then sorts
	//them based on First In First Out algorithm. Prints out frame at each step and page fault total
	private static void firstInFirstOut() throws IOException {
		
		//File names
		String fileBank[] = new String[] {"ReferenceString1.txt", "ReferenceString2.txt",
				"ReferenceString3.txt", "ReferenceString4.txt"};
		
		//for-loop: go through each file
		for(int currentFile = 0; currentFile < fileBank.length; currentFile++) {
			
			//Indicate which algorithm is being used and which test case is being ran
			System.out.println("\nFIFO using file: " + fileBank[currentFile]
					+ "\nTest Case #" + (currentFile+1));
			String fileName = fileBank[currentFile];
			
			//Page numbers from reference string
			int pages[] = new int[30];
			//Number of Page Frames value
			int numberOfPageFrames = 0;
			
			//Scan file for number page frames value and reference string
			File pageFile = new File(fileName);
			Scanner scan = new Scanner(pageFile);
			for(int i = 0; i < 4; i++) {
				if(i == 1) {
					String numberOfPageFrameValue = scan.nextLine();
					numberOfPageFrames = Integer.parseInt(numberOfPageFrameValue);
				} else if(i == 3) {
					String referenceString = scan.nextLine();
					for(int currentCharacter = 0; currentCharacter < 30; currentCharacter++) {
						pages[currentCharacter] = Character.getNumericValue(referenceString.charAt(currentCharacter));
					}
				} else {
					scan.nextLine();
				}
			}
			scan.close();
			
			//Frame array size depends on number of page frame value
			int frames[] = new int[numberOfPageFrames];
			
			//Set all values in frame to -1
			for(int i = 0; i < frames.length; i++) {
				frames[i] = -1;
			}
			
			//Pointer to our index in frame array
			int frameIndex = 0;
			//Total page faults
			int pageFaults = 0;
			//Check tests
			boolean check = true;
			
			//for-loop: Goes through all pages[] elements 
			for(int i = 0; i < pages.length; i++) {
				//Current page number
				int page = pages[i];
				check = true;
				
				//If page is already in frames, do nothing
				for(int j = 0; j < frames.length; j++) {
					if(frames[j] == page) {
						check = false;
						break;
					}
				}
				
				//If page is not in frames, add page to frames and update pointer
				if(check) {
					frames[frameIndex] = page;
					frameIndex++;
					//If pointer goes out of bounds, move it back to beginning
					if(frameIndex == frames.length) {
						frameIndex = 0;
					}
					//Print out current frame
					System.out.print("Frames: ");
					for(int j = 0; j < frames.length; j++) {
						if(frames[j] != -1) {
							System.out.print(frames[j] + "  ");
						}
					}
					System.out.println();
					//Increment page faults
					pageFaults++;
				} else {
					//Print out current frame
					System.out.print("Frames: ");
					for(int j = 0; j < frames.length; j++) {
						if(frames[j] != -1) {
							System.out.print(frames[j] + "  ");
						}
					}
					System.out.println();
				}
			}
			
			//Print total page faults
			System.out.println("Page faults: " + pageFaults);
		}
	}
	
	
	
	//Method: leastRecentlyUsed()
	//Purpose: Reads from 4 given files, places data into array structures, then sorts
	//them based on Least Recently Used algorithm. Prints out frame at each step and page fault total
	private static void leastRecentlyUsed() throws IOException {
		
		//File names
		String fileBank[] = new String[] {"ReferenceString1.txt", "ReferenceString2.txt",
				"ReferenceString3.txt", "ReferenceString4.txt"};
		
		//for-loop: go through each of the four files
		for(int currentFile = 0; currentFile < fileBank.length; currentFile++) {
			
			//Indicate LRU is being used and which test case is being ran
			System.out.println("\nLRU using file: " + fileBank[currentFile]
					+ "\nTest Case #" + (currentFile+1));
			String fileName = fileBank[currentFile];
			
			//Page numbers for reference string
			int pages[] = new int[30];
			//Number of Page Frames value
			int numberOfPageFrames = 0;
			
			//Scan file for number page frames value and reference string
			File pageFile = new File(fileName);
			Scanner scan = new Scanner(pageFile);
			for(int i = 0; i < 4; i++) {
				if(i == 1) {
					String numberOfPageFrameValue = scan.nextLine();
					numberOfPageFrames = Integer.parseInt(numberOfPageFrameValue);
				} else if(i == 3) {
					String referenceString = scan.nextLine();
					for(int currentCharacter = 0; currentCharacter < 30; currentCharacter++) {
						pages[currentCharacter] = Character.getNumericValue(referenceString.charAt(currentCharacter));
					}
				} else {
					scan.nextLine();
				}
			}
			scan.close();
			
			//Frame array size depends on number of page frame value
			int frames[] = new int[numberOfPageFrames];
			//temporary arrays to reference recently used values
			int tempFramesA[] = new int[numberOfPageFrames];
			int tempFramesB[] = new int[numberOfPageFrames];
			
			//Set all values in frame arrays to -1
			for(int i = 0; i < frames.length; i++) {
				frames[i] = -1;
				tempFramesA[i] = -1;
				tempFramesB[i] = -1;
			}
			
			//Recent page
			int recent = 0;
			//Total page faults
			int pageFaults = 0;
			//Check tests
			boolean check = true;
			
			//for-loop: Goes through all pages[] element
			for(int i = 0; i < pages.length; i++) {
				//Current page number
				int page = pages[i];
				check = true;
				
				//If page is already in frames, nothing
				for(int j = 0; j < frames.length; j++) {
					if(frames[j] == page) {
						check = false;
						break;
					}
				}
				//If page is already in frame, page is now our most recent
				for(int j = 0; j < frames.length && check; j++) {
					if(frames[j] == tempFramesA[frames.length-1]) {
						recent = j;
						break;
					}
				}
				//If page is not in frames, add page to frames based on most recently used
				if(check) {
					frames[recent] = page;
					//Print our current frame
					System.out.print("Frames: ");
					for(int j = 0; j < frames.length; j++) {
						if(frames[j] != -1) {
							System.out.print(frames[j] + "  ");
						}
					}
					System.out.println();
					//Increment page faults
					pageFaults++;
				} else {
					//Print our current frame
					System.out.print("Frames: ");
					for(int j = 0; j < frames.length; j++) {
						if(frames[j] != -1) {
							System.out.print(frames[j] + "  ");
						}
					}
					System.out.println();
				}
				
				//Page number
				int pageNumber = 1;
				//Copy page into b[]
				tempFramesB[0] = page;
				
				//for every element in a, copy into b[]
				for(int j = 0; j < tempFramesA.length; j++) {
					if(page != tempFramesA[j] && pageNumber < frames.length) {
						tempFramesB[pageNumber] = tempFramesA[j];
						pageNumber++;
					}
				}
				
				//Copy b[] into a[]
				for(int j = 0; j < frames.length; j++) {
					tempFramesA[j] = tempFramesB[j];
				}
			}
			//Print out total page faults
			System.out.println("Page faults: " + pageFaults);
		}
	}
	
	
	
	//Method: optimalAlgorithm()
	//Purpose: Reads from 4 given files, places data into array structures.
	//For every page, if the page is not found in frame[], a temp[] copy of page[]
	//is used to check future of reference string. Frame[] to be replaced based on furthest
	//referenced page or page that was never referenced.
	//Prints out frame at each step and page fault total.
	private static void optimalAlgorithm() throws IOException {
		String fileBank[] = new String[] {"ReferenceString1.txt", "ReferenceString2.txt",
				"ReferenceString3.txt", "ReferenceString4.txt"};
		for(int currentFile = 0; currentFile < fileBank.length; currentFile++) {
			System.out.println("\nOptimal Algorithm using file: " + fileBank[currentFile]
					+ "\nTest Case #" + (currentFile+1));
			String fileName = fileBank[currentFile];
			
			//Pages for reference string
			int pages[] = new int[30];
			//Temp array to check "future" reference string
			int temp[] = new int[30];
			//Number of page frames value
			int numberOfPageFrames = 0;
			
			//Scan file for number page frames value and reference string
			File pageFile = new File(fileName);
			Scanner scan = new Scanner(pageFile);
			for(int i = 0; i < 4; i++) {
				if(i == 1) {
					String numberOfPageFrameValue = scan.nextLine();
					numberOfPageFrames = Integer.parseInt(numberOfPageFrameValue);
				} else if(i == 3) {
					String referenceString = scan.nextLine();
					for(int currentCharacter = 0; currentCharacter < 30; currentCharacter++) {
						pages[currentCharacter] = Character.getNumericValue(referenceString.charAt(currentCharacter));
					}
				} else {
					scan.nextLine();
				}
			}
			scan.close();
			
			//Frame array size depends on number of page frame value
			int frames[] = new int[numberOfPageFrames];
			
			//Set all values in frame arrays to -1
			for(int i = 0; i < frames.length; i++) {
				frames[i] = -1;
			}
			
			//Check tests
			int check1 = 0, check2 = 0, check3 = 0;
			//Current position
			int position = 0;
			//Max value
			int max = 0;
			//Total page faults
			int pageFaults = 0;
			
			//for-loop goes through all pages[] element
			for(int i = 0; i < pages.length; i++) {
				
				//Set check tests to 0
				check1 = check2 = 0;
				
				//If page number is already in page frames, set both checks to 1
				for(int j = 0; j < frames.length; j++) {
					if(frames[j] == pages[i]) {
						check1 = check2 = 1;
						break;
					}
				}
				
				//If check 1 is 0, page fault occured
				if(check1 == 0) {
					for(int j = 0; j < frames.length; j++) {
						if(frames[j] == -1) {
							pageFaults++;
							frames[j] = pages[i];
							check2 = 1;
							break;
						}
					}
				}
				
				//If check 2 is 0, page fault occurred, find a page that is never referenced in the future
				if(check2 == 0) {
					check3 = 0;
					
					//Go through each element in frames
					for(int j = 0; j < frames.length; j++) {
						temp[j] = -1;
						//For every page number after our current page number, check
						//if there a page referenced in our frames
						for(int k = (i + 1); k < pages.length; k++) {
							if(frames[j] == pages[k]) {
								temp[j] = k;
								break;
							}
						}
					}
					
					//Skip elements that are not referenced
					for(int j = 0; j < frames.length; j++) {
						if(temp[j] == -1) {
							position = j;
							check3 = 1;
							break;
						}
					}
					
					//For pages referenced
					if(check3 == 0) {
						//Set max
						max = temp[0];
						//Position of our farthest referenced page
						position = 0;
						
						//loop through temp array to find farthest referenced page
						for(int j = 1; j < frames.length; j++) {
							if(temp[j] > max) {
								max = temp[j];
								position = j;
							}
						}
					}
					
					//Place page where farthest referenced page is at
					frames[position] = pages[i];
					//Increment page fault
					pageFaults++;
				}
				
				//Print out frames
				System.out.print("Frames: ");
				for(int j = 0; j < frames.length; j++) {
					if(frames[j] != -1) {
						System.out.print(frames[j] + "  ");
					}
				}
				System.out.println();
			}
			
			//Print out total page faults
			System.out.println("Page faults: " + pageFaults);
		}
	}
	
	//Method: randomlyGeneratedInput()
	//Purpose: Generate 50 different testing cases.
	//For each trial, print trial number and page fault for each algorithm
	//Print average page fault at the end
	private static void randomlyGeneratedInput() {
		int pages[] = new int[50];
		int numOfFrames[] = new int[] {3, 4, 5, 6};
		
		//Sum of page faults for each algorithm
		int totalPageFaultsFIFO = 0;
		int totalPageFaultsLRU = 0;
		int totalPageFaultsOptimal = 0;
		
		for(int frameAmount = 0; frameAmount < numOfFrames.length; frameAmount++) {
			for(int trials = 0; trials < 50; trials++) {
				for(int i = 0; i < pages.length; i++) {
					Random rng = new Random();
					pages[i] = rng.nextInt(8);	
				}
				int pageFaultsFIFO = fifoRNG(pages, numOfFrames[frameAmount]);
				totalPageFaultsFIFO = totalPageFaultsFIFO + pageFaultsFIFO;
				int pageFaultsLRU = lruRNG(pages, numOfFrames[frameAmount]);
				totalPageFaultsLRU = totalPageFaultsLRU + pageFaultsLRU;
				int pageFaultsOptimal = optimalRNG(pages, numOfFrames[frameAmount]);
				totalPageFaultsOptimal = totalPageFaultsOptimal + pageFaultsOptimal;
				System.out.println("Number of page frames: " + numOfFrames[frameAmount] + " Trial #: " + (trials+1));
				System.out.println("FIFO page fault: " + pageFaultsFIFO);
				System.out.println("LRU page fault: " + pageFaultsLRU);
				System.out.println("Optimal page fault: " + pageFaultsOptimal);
				System.out.println();
			}
			System.out.println("Number of page frames: " + numOfFrames[frameAmount]);
			System.out.println("Average FIFO Page Faults after 50 trials: " + (float) totalPageFaultsFIFO / 50f);
			System.out.println("Average LRU Page Faults after 50 trials: " + (float) totalPageFaultsLRU / 50f);
			System.out.println("Average Optimal Page Faults after 50 trials: " + (float) totalPageFaultsOptimal / 50f);
			System.out.println();
			
			totalPageFaultsFIFO = 0;
			totalPageFaultsLRU = 0;
			totalPageFaultsOptimal = 0;
		}
	}
	
	//Method: fIFORandomlyGeneratedInput()
	// Purpose: Prints out each trial's page fault total as well as the average of all the trials.
	private static int fifoRNG(int[] pages, int frameAmount) {
		// Page frame size
		int frames[] = new int[frameAmount];

		// Set all values to -1
		for (int i = 0; i < frames.length; i++) {
			frames[i] = -1;
		}

		// Pointer to our index in frame
		int frameIndex = 0;
		// Page faults
		int pageFaults = 0;
		// Check tests
		boolean check = true;

		// for-loop: Goes through all pages[] elements
		for (int i = 0; i < pages.length; i++) {
			// Current page
			int page = pages[i];
			check = true;
			// If page is already in frames, do nothing
			for (int j = 0; j < frames.length; j++) {
				if (frames[j] == page) {
					check = false;
					break;
				}
			}
			// If page is not in frames, add page to frames and update pointer
			if (check) {
				frames[frameIndex] = page;
				frameIndex++;
				// If pointer goes out of bounds, move it back to beginning
				if (frameIndex == frames.length) {
					frameIndex = 0;
				}
				// Increment page faults
				pageFaults++;
			}
		}
		return pageFaults;
	}

	// Method: lRURandomlyGeneratedInput()
	// Purpose: Prints out each trial's page fault total as well as the average of all the trials.
	private static int lruRNG(int[] pages, int frameAmount) {
		// Frame array size depends on number of page frame value
		int frames[] = new int[frameAmount];
		// Temporary arrays to copy and reference recently used values
		int tempFramesA[] = new int[frameAmount];
		int tempFramesB[] = new int[frameAmount];

		// Set all values in frame arrays to -1
		for (int i = 0; i < frames.length; i++) {
			frames[i] = -1;
			tempFramesA[i] = -1;
			tempFramesB[i] = -1;
		}

		// Recently used
		int recent = 0;
		// Total page faults
		int pageFaults = 0;
		// Check tests
		boolean check = true;

		// for-loop: goes through all pages[] elements
		for (int i = 0; i < pages.length; i++) {
			// Current page number
			int page = pages[i];
			check = true;

			// If page is already in frames, nothing
			for (int j = 0; j < frames.length; j++) {
				if (frames[j] == page) {
					check = false;
					break;
				}
			}

			// If page is already in frame, page is now our most recent
			for (int j = 0; j < frames.length && check; j++) {
				if (frames[j] == tempFramesA[frames.length - 1]) {
					recent = j;
					break;
				}
			}

			// If page is not in frames, add page to frames based on most recently used
			// and then increment page faults
			if (check) {
				frames[recent] = page;
				pageFaults++;
			}

			// Page number
			int pageNumber = 1;
			// Copy page into b[]
			tempFramesB[0] = page;

			// For every element in a[], copy into b[]
			for (int j = 0; j < tempFramesA.length; j++) {
				if (page != tempFramesA[j] && pageNumber < frames.length) {
					tempFramesB[pageNumber] = tempFramesA[j];
					pageNumber++;
				}
			}

			// Copy a[] into b[]
			for (int j = 0; j < frames.length; j++) {
				tempFramesA[j] = tempFramesB[j];
			}
		}
		return pageFaults;
	}
	
	//Method: optimalAlgorithmRandomlyGeneratedInput()
	//Purpose: For every page, if the page is not found in frame[], a temp[] copy of page[]
	//is used to check future of reference string. Frame[] to be replaced based on furthest
	//referenced page or page that was never referenced.
	private static int optimalRNG(int[] pages, int frameAmount) {
		//Temp array to check "future" reference string
		int temp[] = new int [50];

		// Frame array size depends on number of page frame value
		int frames[] = new int[frameAmount];

		// Set all values in frame arrays to -1
		for (int i = 0; i < frames.length; i++) {
			frames[i] = -1;
		}

		// Check tests
		int check1 = 0, check2 = 0, check3 = 0;
		// Current position
		int position = 0;
		// Max value
		int max = 0;
		// Total page faults
		int pageFaults = 0;

		// for-loop goes through all pages[] elements
		for (int i = 0; i < pages.length; i++) {

			// Set check tests to 0
			check1 = check2 = 0;

			// If page number is already in page frames, set both checks to 1
			for (int j = 0; j < frames.length; j++) {
				if (frames[j] == pages[i]) {
					check1 = check2 = 1;
					break;
				}
			}

			// If check1 is 0, page fault occured
			if (check1 == 0) {
				for (int j = 0; j < frames.length; j++) {
					if (frames[j] == -1) {
						pageFaults++;
						frames[j] = pages[i];
						check2 = 1;
						break;
					}
				}
			}

			// If check2 = 0, page fault occured, will need to find a page that is never
			// referenced in the future
			if (check2 == 0) {
				check3 = 0;

				// Go through each element in frames
				for (int j = 0; j < frames.length; j++) {
					temp[j] = -1;
					// For every page number after our current page number, check
					// if there is a page referenced in our frames
					for (int k = (i + 1); k < pages.length; k++) {
						if (frames[j] == pages[k]) {
							temp[j] = k;
							break;
						}
					}
				}

				// Skip elements that are not referenced
				for (int j = 0; j < frames.length; j++) {
					if (temp[j] == -1) {
						position = j;
						check3 = 1;
						break;
					}
				}

				// For pages referenced
				if (check3 == 0) {
					// Set max
					max = temp[0];
					// Position of our farthest referenced page
					position = 0;

					// loop through temp array to find farthest referenced page
					for (int j = 1; j < frames.length; j++) {
						if (temp[j] > max) {
							max = temp[j];
							position = j;
						}
					}
				}

				// Place page where farthest referenced page is at
				frames[position] = pages[i];
				// Increment page fault
				pageFaults++;
			}
		}
		return pageFaults;
	}
}