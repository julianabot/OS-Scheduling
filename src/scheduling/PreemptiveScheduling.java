package javaapplication6;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import java.util.Arrays;
import java.io.*;
import java.util.List;


class Process
{
    int pid; // Process ID
    int bt; // Burst Time
    int art; // Arrival Time
      
    public Process(int pid, int bt, int art)
    {
        this.pid = pid;
        this.bt = bt;
        this.art = art;
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
        
        do{
        
        //Code block for accepting a input number for processes
        do {
            System.out.print("Input no. of processes [2-9]: ");
            processNum = keyboard.nextInt();
            
            if(processNum > 9 || processNum < 2){
                System.out.println("Invalid Value. Please choose a number between 2 and 9.");
            }
        } while (processNum > 9 || processNum < 2);

        //Accepting the arrival time input
        for(int i = 1; i <= processNum; i++){
            System.out.print("AT" + i + ": ");
            int inputArrival = keyboard.nextInt();
            arrivalTime.add(inputArrival);
        }
        
        //Accepting burst time input
        for(int i = 1; i <= processNum; i++){
            System.out.print("BT" + i + ": ");
            int inputBurst = keyboard.nextInt();
            burstTime.add(inputBurst);
        }
        
        //puts the input into the array to be used in the computation of SJF
        Process proc[] = {};
        for(int i = 1; i <= processNum; i++){
            proc = Arrays.copyOf(proc, proc.length + 1);
            proc[proc.length - 1] = new Process(i, burstTime.get(i - 1), arrivalTime.get(i - 1));
        }
        
        //puts the input into the necessary objects to be used in the computation of RR
        int quantum;
        int processes[] = {};
        int burstTimeInput[] = {};
        for(int i = 1; i <= processNum; i++){
            processes = Arrays.copyOf(processes, processes.length + 1);
            processes[processes.length - 1] = i;
        }
        for(int i = 1; i <= processNum; i++){
            burstTimeInput = Arrays.copyOf(burstTimeInput, burstTimeInput.length + 1);
            burstTimeInput[burstTimeInput.length - 1] = burstTime.get(i - 1);
        }
        
        do{
            System.out.println("CPU Scheduling Algorithm");
            System.out.println("[A] Shortest Job First (SJF)");
            System.out.println("[B] Round Robin (RR)");
            System.out.println("[C] Exit Program");
            System.out.print("Compute for: ");
            char inputAlgo = keyboard.next().charAt(0);
            chosenAlgo = Character.toUpperCase(inputAlgo);
            
            //Redirects to methods for chosen algorithms
            if(chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C'){
                System.out.println("Invalid input. Please select a valid input from the example above.");
            } 
            else if(chosenAlgo == 'A'){
                //Shortest Job First chosen
                findavgTime(proc, proc.length);
            }
            else if(chosenAlgo == 'B'){
                //Round Robin Chosen
                System.out.print("Enter quantum number: ");
                quantum = keyboard.nextInt();
                RRfindavgTime(processes, processes.length, burstTimeInput, quantum);
            }
            else System.out.println("End of program.");
            
        }while(chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C');
        
        System.out.println("Input again? (Y/N): ");
        chooseContinue = Character.toUpperCase(keyboard.next().charAt(0));
        
        if(chooseContinue != 'Y' && chooseContinue != 'N'){
            System.out.println("Invalid input. Program will now be terminated");
        }
        }while(chooseContinue == 'Y');
        
    }
    
    
    //computation methods for SJF
    static void findWaitingTime(Process proc[], int n, int wt[])
    {
        int rt[] = new int[n];
       
        // Copy the burst time into rt[]
        for (int i = 0; i < n; i++)
            rt[i] = proc[i].bt;
       
        int complete = 0, t = 0, minm = Integer.MAX_VALUE;
        int shortest = 0, finish_time;
        boolean check = false;
       
        // Process until all processes gets completed
        while (complete != n) {
       
            // Find process with minimum remaining time among the processes that arrives till the current time
            for (int j = 0; j < n; j++) 
            {
                if ((proc[j].art <= t) &&
                  (rt[j] < minm) && rt[j] > 0) {
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
       
            // Update minimum
            minm = rt[shortest];
            if (minm == 0)
                minm = Integer.MAX_VALUE;
       
            // If a process gets completely executed
            if (rt[shortest] == 0) {
       
                // Increment complete
                complete++;
                check = false;
       
                // Find finish time of current process
                finish_time = t + 1;
       
                // Calculate waiting time
                wt[shortest] = finish_time -
                             proc[shortest].bt -
                             proc[shortest].art;
       
                if (wt[shortest] < 0)
                    wt[shortest] = 0;
            }
            // Increment time
            t++;
        }
    }
       
    // Method to calculate turn around time
    static void findTurnAroundTime(Process proc[], int n, int wt[], int tat[])
    {
        // calculating turnaround time by adding bt[i] + wt[i]
        for (int i = 0; i < n; i++)
            tat[i] = proc[i].bt + wt[i];
    }
       
    // Method to calculate average time
    static void findavgTime(Process proc[], int n)
    {
        int wt[] = new int[n], tat[] = new int[n];
        int  total_wt = 0, total_tat = 0;
       
        // Function to find waiting time of all processes
        findWaitingTime(proc, n, wt);
       
        // Function to find turn around time for all processes
        findTurnAroundTime(proc, n, wt, tat);
       
        // Display processes along with all details
        System.out.println("Processes " +
                           " Burst time " +
                           " Waiting time " +
                           " Turn around time");
       
        // Calculate total waiting time and total turnaround time
        for (int i = 0; i < n; i++) {
            total_wt = total_wt + wt[i];
            total_tat = total_tat + tat[i];
            System.out.println(" " + proc[i].pid + "\t\t"
                             + proc[i].bt + "\t\t " + wt[i]
                             + "\t\t" + tat[i]);
        }
       
        System.out.println("Average waiting time = " +
                          (float)total_wt / (float)n);
        System.out.println("Average turn around time = " +
                           (float)total_tat / (float)n);
        return;
    }
    
    //computation methods for RR
    static void RRfindWaitingTime(int processes[], int n,
                 int bt[], int wt[], int quantum)
    {
        // Make a copy of burst times bt[] to store remaining
        // burst times.
        int rem_bt[] = new int[n];
        for (int i = 0 ; i < n ; i++)
            rem_bt[i] =  bt[i];
      
        int t = 0; // Current time
      
        // Keep traversing processes in round robin manner
        // until all of them are not done.
        while(true)
        {
            boolean done = true;
      
            // Traverse all processes one by one repeatedly
            for (int i = 0 ; i < n; i++)
            {
                // If burst time of a process is greater than 0
                // then only need to process further
                if (rem_bt[i] > 0)
                {
                    done = false; // There is a pending process
      
                    if (rem_bt[i] > quantum)
                    {
                        // Increase the value of t i.e. shows
                        // how much time a process has been processed
                        t += quantum;
      
                        // Decrease the burst_time of current process
                        // by quantum
                        rem_bt[i] -= quantum;
                    }
      
                    // If burst time is smaller than or equal to
                    // quantum. Last cycle for this process
                    else
                    {
                        // Increase the value of t i.e. shows
                        // how much time a process has been processed
                        t = t + rem_bt[i];
      
                        // Waiting time is current time minus time
                        // used by this process
                        wt[i] = t - bt[i];
      
                        // As the process gets fully executed
                        // make its remaining burst time = 0
                        rem_bt[i] = 0;
                    }
                }
            }
      
            // If all processes are done
            if (done == true)
              break;
        }
    }
      
    // Method to calculate turn around time
    static void RRfindTurnAroundTime(int processes[], int n,
                            int bt[], int wt[], int tat[])
    {
        // calculating turnaround time by adding
        // bt[i] + wt[i]
        for (int i = 0; i < n ; i++)
            tat[i] = bt[i] + wt[i];
    }
      
    // Method to calculate average time
    static void RRfindavgTime(int processes[], int n, int bt[],
                                         int quantum)
    {
        int wt[] = new int[n], tat[] = new int[n];
        int total_wt = 0, total_tat = 0;
      
        // Function to find waiting time of all processes
        RRfindWaitingTime(processes, n, bt, wt, quantum);
      
        // Function to find turn around time for all processes
        RRfindTurnAroundTime(processes, n, bt, wt, tat);
      
        // Display processes along with all details
        System.out.println("Processes " +
                           " Burst time " +
                           " Waiting time " +
                           " Turn around time");
      
        // Calculate total waiting time and total turn
        // around time
        for (int i=0; i<n; i++)
        {
            total_wt = total_wt + wt[i];
            total_tat = total_tat + tat[i];
            System.out.println(" " + (i+1) + "\t\t" + bt[i] +"\t " +
                              wt[i] +"\t\t " + tat[i]);
        }
      
        System.out.println("Average waiting time = " +
                          (float)total_wt / (float)n);
        System.out.println("Average turn around time = " +
                           (float)total_tat / (float)n);
        return;
    }
}
