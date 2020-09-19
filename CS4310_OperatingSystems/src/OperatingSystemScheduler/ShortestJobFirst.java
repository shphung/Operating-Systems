/****************************************************************************
*
*   File: FirstComeFirstServe.java
*   Author: Steven Phung
*   Class: CS 4310.01 - Operating Systems
*
*   Assignment: Project 1: Simulating Job Scheduler and Performance Analysis
*   Date last modified: 10/2/2019
*
*   Purpose: This program will either read from job.txt files or randomly
*   generate job timings m amount of times and simulate Job Scheduling using
*   Shortest Job First algorithm
* 
****************************************************************************/
package OperatingSystemScheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class ShortestJobFirst {
	
	//Amount of times to do experimental runs
	final static int AMOUNT_OF_TRIALS = 100;

	//Main Method: Allows user to choose whether to simulate Job Scheduling
	//using input from files or randomly generated input
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		do {
			System.out.println("Shortest Job First:\n"
					+ "1. Solve SJF using input from files\n"
					+ "2. Solve SJF using randomly generated input\n"
					+ "3. Exit");
			input = keyboard.next();
			if(Integer.parseInt(input) == 1) {
				fileGivenInput();
			} else if(Integer.parseInt(input) == 2) {
				experimentalAverages();
			} else if(Integer.parseInt(input) == 3) {
				System.exit(0);
			}
		} while(!input.matches("[3]"));
		keyboard.close();
	}
	
	//Method: fileGivenInput()
	//Purpose: Reads from 3 given files, places data into array structures,
	//then algorithm finds start and end times for each job and prints out
	//an appropriate schedule table
	private static void fileGivenInput() throws IOException {
		//Three predetermined files with input
		String fileBank[] = new String[] {"job1.txt", "job2.txt", "job3.txt"};
		//for loop to go through each file
		for(int files = 0; files < fileBank.length;  files++) {
			System.out.println("\nTest Case #" + (files+1));
			String fileName = fileBank[files];
			
			//Gets amount of lines in the file and divides by 2 to get total number of elements
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			int lines = 0;
			while (reader.readLine() != null) lines++;
			reader.close();
			lines = lines / 2;
			
			//processes[]
			int job[] = new int[lines];
			//burst time[]
			int burstTime[] = new int[lines];
			//start Time[]
			int startTime[] = new int[lines];
			//end Time[]
			int endTime[] = new int[lines];

			//Scanner object to read file
			File jobFile = new File(fileName);
			Scanner scan = new Scanner(jobFile);

			//for loop to get job and burst time data from file
			for(int i = 0; i < lines; i++) {
				//Adds job
				String jobInFile = scan.nextLine();
				if(jobInFile.matches("Job\\d+$")) {
					job[i] = i+1;
				}
				//Adds burst time
				String burstTimeInFile = scan.nextLine();
				if(Integer.parseInt(burstTimeInFile) > 0) {
					burstTime[i] = Integer.parseInt(burstTimeInFile);
				}
			}

			//Close scanner object when done with file
			scan.close();
			
			//Sort array by least to greatest
			int temp;
			for(int i = 0; i < lines - 1; i++) {
				for(int j = 1; j < lines; j++) {
					if(burstTime[j] < burstTime[j-1]) {
						temp = burstTime[j];
						burstTime[j] = burstTime[j-1];
						burstTime[j-1] = temp;
						
						temp = job[j];
						job[j] = job[j-1];
						job[j-1] = temp;
					}
				}
			}

			//Start time is always 0
			startTime[0] = 0;
			//for loop to set all iterative start times by adding burst time
			for(int i = 1; i < lines; i++) {
				startTime[i] = startTime[i-1] + burstTime[i-1];
			}

			//for loop to set end time using start time and burst time
			for(int i = 0; i < lines; i++) {
				endTime[i] = startTime[i] + burstTime[i];
			}
			

			//Turn-around time
			float meanTurnAroundTime = 0;
			//total amount of end time
			int totalEndTime = 0;
			//for loop to add all completed job times to total amount of end time
			for(int i = 0; i < lines; i++) {
				totalEndTime = totalEndTime + endTime[i];
			}
			//Find average of turn-around time
			meanTurnAroundTime =  (float)totalEndTime / (float)lines;
			
			System.out.println(" __________________________________________________________________________________ ");
			System.out.println("|       |                |                 |               |                       |");
			System.out.println("| Jobs  |   Burst Time   |   Start Time    |   End Time    |    Job Completion     |");
			System.out.println("|_______|________________|_________________|_______________|_______________________|");

			//Print format of job, burst time, start time, end time, and job completed at what time
			for(int i = 0; i < lines; i++) {
				System.out.println("|       |                |                 |               |                       |");
				System.out.printf("| Job%-3d| Burst Time: %-3d| Start Time: %-4d| End Time: %-4d| Job %-3dcompleted @%-3d |\n",
						job[i], burstTime[i], startTime[i], endTime[i], job[i], endTime[i]);
				System.out.println("|_______|________________|_________________|_______________|_______________________|");
			}

			//Print format of average turn around time
			System.out.println("|                                                                                  |");
			System.out.printf("|    Average turn-around time: %-3.3f                                              |\n", meanTurnAroundTime);
			System.out.println("|__________________________________________________________________________________|");
		}
	}
	
	//Method: experimentalAverages()
	//Purpose: For input sizes 5, 10, and 15, prints out average of turn around averages for amount of trials.
	//This method does not print out each schedule table since trial runs are a large amount.
	private static void experimentalAverages() {
		//Predetermined job input sizes
		int jobLengthBank[] = new int[] {5, 10, 15};
		//for loop to find average of average turn around time for inputs 5, 10, and 15; each 100 times.
		for(int i = 0; i < jobLengthBank.length; i++) {
			float averageOfAverageTurnAroundTimes = 0, totalAverageTurnAroundTime = 0;
			int counter = 0;
			//Find individual turn around times 100 times and add it to totalAverageTurnAroundTime
			while(counter < AMOUNT_OF_TRIALS) {
				totalAverageTurnAroundTime = totalAverageTurnAroundTime + randomlyGeneratedInput(jobLengthBank[i]);
				counter++;
			}
			//Finds average and prints
			averageOfAverageTurnAroundTimes = totalAverageTurnAroundTime / (float)AMOUNT_OF_TRIALS;
			System.out.println("Average of average turn-around times for " + AMOUNT_OF_TRIALS 
					+ " with job inputs of length: " + jobLengthBank[i] + " for SJF: " + averageOfAverageTurnAroundTimes + "");
		}
	}
	
	//Method: randomlyGeneratedInput(int)
	//Purpose: Takes in input size, randomly generates data, and then finds the correct meanTurnAroundTime
	//based on the randomly generated input
	//Returns: meanTurnAroundTime
	private static float randomlyGeneratedInput(int jobLength) {
		float meanTurnAroundTime = 0;
		//processes[]
		int job[] = new int[jobLength];
		//burst time[]
		int burstTime[] = new int[jobLength];
		//start time[]
		int startTime[] = new int[jobLength];
		//end time[]
		int endTime[] = new int[jobLength];

		//for loop to randomly generate input of values between 1 - 50 for burst time
		for(int i = 0; i < jobLength; i++) {
			Random rng = new Random();
			job[i] = (i+1);
			burstTime[i] = rng.nextInt(50)+1;
		}
		
		//Sort arrays from least to greatest
		int temp;
		for(int i = 0; i < jobLength - 1; i++) {
			for(int j = 1; j < jobLength; j++) {
				if(burstTime[j] < burstTime[j-1]) {
					temp = burstTime[j];
					burstTime[j] = burstTime[j-1];
					burstTime[j-1] = temp;
					
					temp = job[j];
					job[j] = job[j-1];
					job[j-1] = temp;
				}
			}
		}

		//Start time is 0
		startTime[0] = 0;
		//for loop to set all iterative start times by adding burst time
		for(int i = 1; i < jobLength; i++) {
			startTime[i] = startTime[i-1] + burstTime[i-1];
		}

		//for loop to set end time using start time and burst time
		for(int i = 0; i < jobLength; i++) {
			endTime[i] = startTime[i] + burstTime[i];
		}
		
		int totalEndTime = 0;
		//for loop to add all completed job times to total amount of end time
		for(int i = 0; i < jobLength; i++) {
			totalEndTime = totalEndTime + endTime[i];
		}

		//Find average of turn around time
		meanTurnAroundTime =  (float)totalEndTime / (float)jobLength;
		return meanTurnAroundTime;
	}
}
