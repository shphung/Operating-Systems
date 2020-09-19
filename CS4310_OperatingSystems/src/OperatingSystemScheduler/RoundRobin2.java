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
*   Round-Robin with Time Slice = 2, algorithm
* 
****************************************************************************/
package OperatingSystemScheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class RoundRobin2 {
	
	//Amount of times to do experimental runs
	final static int AMOUNT_OF_TRIALS = 100;
	
	//Main Method: Allows user to choose whether to simulate Job Scheduling
	//using input from files or randomly generated input
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		String input = "";
		do {
			System.out.println("Round-Robin with Time Slice = 2:\n"
					+ "1. Solve RR-2 using input from files\n"
					+ "2. Solve RR-2 using randomly generated input\n"
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
			//left over burst time[]
			int leftOverBurstTime[] = new int[lines];
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

			//for loop to copy burstTime to leftOverBurstTime to access and mutate
			for(int i = 0; i < lines; i++) {
				leftOverBurstTime[i] = burstTime[i];
			}

			//Close scanner object when done with file
			scan.close();
			
			System.out.println(" __________________________________________________________________________________ ");
			System.out.println("|       |                |                 |               |                       |");
			System.out.println("| Jobs  |   Burst Time   |   Start Time    |   End Time    |    Job Completion     |");
			System.out.println("|_______|________________|_________________|_______________|_______________________|");
			
			//Time slice = 2
			int timeSlice = 2;
			float meanTurnAroundTime = 0;
			int totalEndTime = 0;
			int endTime1 = 0;
			//While loop to continue loop until all leftOverBurstTimes = 0
			while(true) {
				boolean done = true;
				//for loop to go through each job
				for(int i = 0; i < lines; i++) {
					//Check job's current burst time
					if(leftOverBurstTime[i] > 0) {
						done = false;
						//Set start time, end time, and recalculate leftOverBurstTime based on timeSlice and print out info
						if(leftOverBurstTime[i] > timeSlice) {
							endTime1 = endTime1 + timeSlice;
							endTime[i] = endTime1;
							startTime[i] = endTime1 - timeSlice;
							leftOverBurstTime[i] = leftOverBurstTime[i] - timeSlice;
							System.out.println("|       |                |                 |               |                       |");
							System.out.printf("| Job%-3d| Burst Time: %-3d| Start Time: %-4d| End Time: %-4d|           ~           |\n",
									job[i], leftOverBurstTime[i], startTime[i], endTime[i], job[i], endTime[i]);
							System.out.println("|_______|________________|_________________|_______________|_______________________|");
						//Set start time, end time, and recalculate leftOverBurstTime based on what's left in
						//leftOverBurstTime, set leftOverBurstTime to 0, and print out info
						} else {
							endTime1 = endTime1 + leftOverBurstTime[i];
							endTime[i] = endTime1;
							startTime[i] = endTime1 - leftOverBurstTime[i];
							leftOverBurstTime[i] = 0;
							totalEndTime = totalEndTime + endTime[i];
							System.out.println("|       |                |                 |               |                       |");
							System.out.printf("| Job%-3d| Burst Time: %-3d| Start Time: %-4d| End Time: %-4d| Job %-3dcompleted @%-3d |\n",
									job[i], leftOverBurstTime[i], startTime[i], endTime[i], job[i], endTime[i]);
							System.out.println("|_______|________________|_________________|_______________|_______________________|");
						}
					}
				}
				//If all values in leftOverBurstTime = 0, break out of loop
				if(done)
					break;
			}
			//Solve for averageTurnAroundTime
			meanTurnAroundTime =  (float)totalEndTime / (float)lines;
			
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
					+ " with job inputs of length: " + jobLengthBank[i] + " for RR-2: " + averageOfAverageTurnAroundTimes + "");
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
		//left over burst time[]
		int leftOverBurstTime[] = new int[jobLength];

		//for loop to randomly generate input of values between 1 - 50 for burst time
		for(int i = 0; i < jobLength; i++) {
			Random rng = new Random();
			job[i] = (i+1);
			burstTime[i] = rng.nextInt(50)+1;
			leftOverBurstTime[i] = burstTime[i];
		}
		
		//Time slice = 2
		int timeSlice = 2;
		int totalEndTime = 0;
		int time = 0;
		//While loop to continue loop until all leftOverBurstTimes = 0
		while(true) {
			boolean done = true;
			//for loop to go through each job
			for(int i = 0; i < jobLength; i++) {
				//Check job's current burst time
				if(leftOverBurstTime[i] > 0) {
					done = false;
					//Set start time, end time, and recalculate leftOverBurstTime based on timeSlice and print out info
					if(leftOverBurstTime[i] > timeSlice) {
						time = time + timeSlice;
						endTime[i] = time;
						startTime[i] = time - timeSlice;
						leftOverBurstTime[i] = leftOverBurstTime[i] - timeSlice;
					//Set start time, end time, and recalculate leftOverBurstTime based on what's left in
					//leftOverBurstTime, set leftOverBurstTime to 0, and print out info
					} else {
						time = time + leftOverBurstTime[i];
						endTime[i] = time;
						startTime[i] = time - leftOverBurstTime[i];
						leftOverBurstTime[i] = 0;
						totalEndTime = totalEndTime + endTime[i];
					}
				}
			}
			//If all values in leftOverBurstTime = 0, break out of loop
			if(done == true)
				break;
		}
		//Solve for averageTurnAroundTime and return it
		meanTurnAroundTime =  (float)totalEndTime / (float)jobLength;
		return meanTurnAroundTime;
	}
}
