package scheduling;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import java.util.Arrays;
import java.io.*;
import java.util.List;

class Process {

    int pid; // Process ID
    int bt; // Burst Time
    int art; // Arrival Time
    int wt;
    int tat;

    public Process(int pid, int bt, int art) {
        this.pid = pid;
        this.bt = bt;
        this.art = art;
    }

    public Process(int pid, int bt, int art, int wt, int tat) {
        this.pid = pid;
        this.bt = bt;
        this.art = art;
        this.wt = wt;
        this.tat = tat;
    }

    public int getBurstTime() {
        return this.bt;
    }

    // overriding the compareTo method of Comparable class
    public int compareTo(Process comparestu) {
        int burstTime
                = ((Process) comparestu).getBurstTime();

        //  For Ascending order
        return this.bt - burstTime;

    }

}

public class PreemptiveScheduling {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        int processNum;
        ArrayList<Integer> arrivalTime = new ArrayList<>();
        ArrayList<Integer> burstTime = new ArrayList<>();
        char chosenAlgo;
        char chooseContinue;
        char inputAlgo;

        do {
            do {
                System.out.println("Preemptive Scheduling Algorithms");
                System.out.println("[A] Shortest Remaining Time First (SRTF)");
                System.out.println("[B] Preemptive Priority (P-Prio)");
                System.out.println("[C] Exit");
                System.out.print("Compute for: ");
                inputAlgo = keyboard.next().charAt(0);
                chosenAlgo = Character.toUpperCase(inputAlgo);

                System.out.println("---------------------------------------------------------------------------------------");
                switch (chosenAlgo) {
                    case 'A':
                        System.out.println("Shortest Remaining Time First (SRTF)");
                        break;
                    case 'B':
                        System.out.println("Preemptive Priority (P-Prio)");
                        break;
                }
                if (inputAlgo == 'C') {
                    System.out.println("End of program.");
                    break;
                }
                //Code block for accepting a input number for processes
                do {
                    System.out.print("Input no. of processes [2-9]: ");
                    processNum = keyboard.nextInt();

                    if (processNum > 9 || processNum < 2) {
                        System.out.println("Invalid Value. Please choose a number between 2 and 9.");
                    }
                } while (processNum > 9 || processNum < 2);

                //Accepting the arrival time input
                for (int i = 1; i <= processNum; i++) {
                    System.out.print("AT" + i + ": ");
                    int inputArrival = keyboard.nextInt();
                    arrivalTime.add(inputArrival);
                }

                //Accepting burst time input
                for (int i = 1; i <= processNum; i++) {
                    System.out.print("BT" + i + ": ");
                    int inputBurst = keyboard.nextInt();
                    burstTime.add(inputBurst);
                }

                //Puts the input into the array to be used in the computation of SJF
                Process proc[] = {};
                for (int i = 1; i <= processNum; i++) {
                    proc = Arrays.copyOf(proc, proc.length + 1);
                    proc[proc.length - 1] = new Process(i, burstTime.get(i - 1), arrivalTime.get(i - 1));
                }

                //Puts the input into the array to be used in the computation of P-Prio
                int arrivalTimePPrio[] = new int[processNum];
                for (int i = 1; i <= processNum; i++) {
                    arrivalTimePPrio[i - 1] = arrivalTime.get(i - 1);
                }
                int burstTimePPrio[] = new int[processNum];
                for (int i = 1; i <= processNum; i++) {
                    burstTimePPrio[i - 1] = burstTime.get(i - 1);
                }

                int processID[] = new int[processNum];
                for (int i = 0; i < processNum; i++) {
                    processID[i] = i + 1;
                }

                //Creates an array to accept list of priorities if P-Prio is chosen
                int priority[] = new int[processNum + 1];
                int x[] = new int[processNum];

                //sort array by arrival time
                int sortedArrivalTime[] = Arrays.copyOf(arrivalTimePPrio, processNum);
                //sorted burst time at the top
                int tempArrival;

                //sorts the input elements according to arrival time
                for (int i = 0; i < processNum; i++) {
                    for (int k = i + 1; k < sortedArrivalTime.length; k++) {
                        if (sortedArrivalTime[i] > sortedArrivalTime[k]) {      //swap elements if not in order
                            tempArrival = sortedArrivalTime[i];

                            sortedArrivalTime[i] = sortedArrivalTime[k];

                            sortedArrivalTime[k] = tempArrival;
                        }
                    }
                }

                //Redirects to methods for chosen algorithms
                if (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C') {
                    System.out.println("Invalid input. Please select a valid input from the example above.");
                } else if (chosenAlgo == 'A') {
                    //Shortest Job First chosen
                    findavgTime(proc, proc.length);
                } else if (chosenAlgo == 'B') {
                    //Preemptive Priority Chosen
                    System.out.println("Input individual priority number");

                    //Accepts input for individual priorities
                    for (int i = 1; i <= processNum; i++) {
                        System.out.print("Prio" + i + ": ");
                        int inputPrio = keyboard.nextInt();
                        priority[i - 1] = inputPrio;
                    }

                    int waitingTimePPrio[] = new int[processNum]; //Array to store waiting time
                    int turnaroundTimePPrio[] = new int[processNum]; //Array to store turnaround time
                    int completionTimePPrio[] = new int[processNum]; //Array to store completion time

                    int i, j, smallest, count = 0, time;
                    //count pertains to the number of processes completed
                    //smallest pertains to the index of the array item with the smallest value
                    //i and j are initialized for the for loops
                    //time pertains to the time consumed by the process

                    double avg = 0, tt = 0;
                    //avg is initialized to hold the computed waiting time

                    int end;
                    //end is initialized for the computation using a incremented time value

                    //populates the x array with values from the burst time array
                    for (i = 0; i < processNum; i++) {
                        x[i] = burstTimePPrio[i];
                    }

                    //sets a high number for priority numbers in order to account for any combination
                    priority[processNum] = 10000;

                    //creates a array lists used by the Gantt Chart
                    ArrayList<Integer> completionTimesOutput = new ArrayList<>();
                    ArrayList<Integer> ganttOutput = new ArrayList<>();

                    for (time = sortedArrivalTime[0]; count != processNum; time++) {
                        smallest = processNum;
                        for (i = 0; i < processNum; i++) {
                            //sets the smallest (index) value to the array index with the least value
                            if (arrivalTimePPrio[i] <= time && priority[i] < priority[smallest] && burstTimePPrio[i] > 0) {
                                smallest = i;
                            }
                        }

                        burstTimePPrio[smallest]--;

                        if (arrivalTimePPrio[smallest] == time) {
                            completionTimesOutput.add(time);
                            if (burstTimePPrio[smallest] != 0) {
                                ganttOutput.add(processID[smallest]);
                            }
                        }

                        if (burstTimePPrio[smallest] == 0) {
                            count++;
                            end = time + 1;
                            completionTimePPrio[smallest] = end;
                            completionTimesOutput.add(completionTimePPrio[smallest]);
                            if (ganttOutput.get(ganttOutput.size() - 1) != processID[smallest]) {
                                ganttOutput.add(processID[smallest]);
                            }
                            waitingTimePPrio[smallest] = end - arrivalTimePPrio[smallest] - x[smallest];
                            turnaroundTimePPrio[smallest] = end - arrivalTimePPrio[smallest];
                        }
                    }
                    
                    ArrayList<Integer>cleanCompletionTimesOutput = removeDuplicates(completionTimesOutput);

                    System.out.println("Process\tBurst-time\tarrival-time\twaiting-time\tturnaround-time\tcompletion-time\tpriority");

                    for (i = 0; i < processNum; i++) {
                        System.out.println("p" + (i + 1) + "\t\t" + x[i] + "\t\t" + arrivalTimePPrio[i] + "\t\t" + waitingTimePPrio[i] + "\t\t" + turnaroundTimePPrio[i] + "\t\t" + completionTimePPrio[i] + "\t\t" + priority[i]);
                        avg = avg + waitingTimePPrio[i];
                        tt = tt + turnaroundTimePPrio[i];
                    }

                    System.out.println("Average waiting time = " + (avg / processNum));
                    System.out.println("Average turnaround time = " + (tt / processNum));

                    //creating P-Prio Gantt Chart
                    System.out.println("_______________________________________________________________________________________");
                    System.out.print("[START]  |");
                    for (j = 0; j < ganttOutput.size(); j++) {
                        System.out.print("  P" + ganttOutput.get(j) + "   |");
                    }
                    System.out.print("[END]");

                    System.out.println("");

                    for (j = 0; j < cleanCompletionTimesOutput.size(); j++) {
                        System.out.print("\t" + cleanCompletionTimesOutput.get(j));
                    }

                    System.out.println("");

                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");

                    //end of P-Prio
                } else {
                    System.out.println("End of program.");
                }

            } while (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C');

            if (inputAlgo == 'C') {
                break;
            }
            System.out.print("Input again? (Y/N): ");
            chooseContinue = Character.toUpperCase(keyboard.next().charAt(0));

            if (chooseContinue != 'Y' && chooseContinue != 'N') {
                System.out.println("Invalid input. Program will now be terminated");
            }
            
            
            
        } while (chooseContinue == 'Y');

    }

    //computation methods for SRTF
    //this method also computes for the gantt chart output
    static void findWaitingTime(Process proc[], int n, int wt[], ArrayList<Integer> completionTimesOutput, ArrayList<Integer> ganttOutput) {
        int rt[] = new int[n];

        // Copy the burst time into rt[]
        for (int i = 0; i < n; i++) {
            rt[i] = proc[i].bt;
        }

        int complete = 0, t = 0, minm = Integer.MAX_VALUE, changed = 0;
        int shortest = 0, finish_time;
        boolean check = false;
        
        //arrage process ids ascending according to arrival time
        ArrayList<Integer> listOfPassingIDs = new ArrayList<>();

        // Process until all processes gets completed
        while (complete != n) {
            int copyOfPrevID = 0;
            
            
            // Find process with minimum remaining time among the processes that arrives till the current time
            for (int j = 0; j < n; j++) {
                if ((proc[j].art <= t)
                        && (rt[j] < minm) && rt[j] > 0) {
                    minm = rt[j];
                    shortest = j;
                    check = true;
                }
            }
            
            
            
            if (check == false) {
                t++;
                continue;
            }

            // Reduce remaining time by one
            rt[shortest]--;
            
            if(proc[shortest].art == t){
                
                if (proc[shortest].bt != 0){
                    completionTimesOutput.add(t);
                }
            }
            
            // Update minimum
            minm = rt[shortest];
            if (minm == 0) {
                minm = Integer.MAX_VALUE;
            }

            // If a process gets completely executed
            if (rt[shortest] == 0) {

                // Increment complete
                complete++;
                check = false;

                // Find finish time of current process
                finish_time = t + 1;
                if (completionTimesOutput.get(completionTimesOutput.size() - 1) != finish_time){
                    completionTimesOutput.add(finish_time);
                } 
                
                // Calculate waiting time
                wt[shortest] = finish_time
                        - proc[shortest].bt
                        - proc[shortest].art;

                if (wt[shortest] < 0) {
                    wt[shortest] = 0;
                }
            }
            // Increment time
            t++;
            
            //System.out.println(proc[shortest].pid);
            
            copyOfPrevID = proc[shortest].pid;
            listOfPassingIDs.add(copyOfPrevID);
            
        }
        
        ArrayList<Integer> newProcessQ = new ArrayList<>();
        newProcessQ.add(listOfPassingIDs.get(0));
        int j = 0;
        
        for(int i = 0 ; i < listOfPassingIDs.size() - 1 ; i++){
            if(newProcessQ.get(j) != listOfPassingIDs.get(i + 1)){
                newProcessQ.add(listOfPassingIDs.get(i + 1));
                j++;
            }
        }
        
        for(int i = 0; i < newProcessQ.size(); i++){
            ganttOutput.add(newProcessQ.get(i));
        }
        
    }

    // Method to calculate turn around time
    static void findTurnAroundTime(Process proc[], int n, int wt[], int tat[]) {
        // calculating turnaround time by adding bt[i] + wt[i]
        for (int i = 0; i < n; i++) {
            tat[i] = proc[i].bt + wt[i];
        }
    }

    // Method to calculate average time
    static void findavgTime(Process proc[], int n) {
        int wt[] = new int[n], tat[] = new int[n];
        int total_wt = 0, total_tat = 0;
        
        ArrayList<Integer> completionTimesOutput = new ArrayList<>();
        ArrayList<Integer> ganttOutput = new ArrayList<>();

        // Function to find waiting time of all processes
        findWaitingTime(proc, n, wt, completionTimesOutput, ganttOutput);

        // Function to find turn around time for all processes
        findTurnAroundTime(proc, n, wt, tat);

        // Display processes along with all details
        System.out.println("Processes "
                + " Arrival Time "
                + " Burst Time "
                + " Waiting Time "
                + " Turn around Time");

        // Calculate total waiting time and total turnaround time
        for (int i = 0; i < n; i++) {
            total_wt = total_wt + wt[i];
            total_tat = total_tat + tat[i];
            System.out.println("P" + proc[i].pid + "\t\t"
                    + proc[i].art+ "\t\t" +
                    + proc[i].bt + "\t\t " + wt[i]
                    + "\t\t" + tat[i]);
        }

        System.out.println("Average waiting time = "
                + (float) total_wt / (float) n);
        System.out.println("Average turn around time = "
                + (float) total_tat / (float) n);
        
        //Gantt Chart
        ArrayList<Integer>cleanCompletionTimesOutput = removeDuplicates(completionTimesOutput);
        
        System.out.println("_______________________________________________________________________________________");
        System.out.print("[START]  |");
        for (int j = 0; j < ganttOutput.size(); j++) {
            System.out.print("  P" + ganttOutput.get(j) + "   |");
        }
        System.out.print("[END]");

        System.out.println("");

        for (int j = 0; j < cleanCompletionTimesOutput.size(); j++) {
            System.out.print("\t" + cleanCompletionTimesOutput.get(j));
        }
        
        
        System.out.println("");

        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");

        
        
        return;
    }
    
    //method to resolve duplicates in the gantt output
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
  
        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();
  
        // Traverse through the first list
        for (T element : list) {
  
            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {
  
                newList.add(element);
            }
        }
  
        // return the new list
        return newList;
    }

}
