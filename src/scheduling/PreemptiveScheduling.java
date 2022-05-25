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
        this.tat = wt;
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
                System.out.println("[A] Shortest Job First (SJF)");
                System.out.println("[B] Round Robin (RR)");
                System.out.println("[C] Exit");
                System.out.print("Compute for: ");
                inputAlgo = keyboard.next().charAt(0);
                chosenAlgo = Character.toUpperCase(inputAlgo);

                System.out.println("------------------------------------------------------------");
                switch (chosenAlgo) {
                    case 'A':
                        System.out.println("Shortest Job First (SJF)");
                        break;
                    case 'B':
                        System.out.println("Round Robin (RR)");
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

                //puts the input into the array to be used in the computation of SJF
                Process proc[] = {};
                for (int i = 1; i <= processNum; i++) {
                    proc = Arrays.copyOf(proc, proc.length + 1);
                    proc[proc.length - 1] = new Process(i, burstTime.get(i - 1), arrivalTime.get(i - 1));
                }
                
                //puts the input into the array to be used in the computation of RR
                int n,tq, timer = 0, maxProccessIndex = 0;
                float avgWait = 0, avgTT = 0;
                int arrival[] = new int[processNum];
                int burst[] = new int[processNum];
                int wait[] = new int[processNum];
                int turn[] = new int[processNum];
                int queue[] = new int[processNum];
                int temp_burst[] = new int[processNum];
                boolean complete[] = new boolean[processNum];
                for(int i = 1; i <= processNum; i++){
                    arrival[i - 1] = arrivalTime.get(i - 1);
                }
                for(int i = 1; i <= processNum; i++){
                    burst[i - 1] = burstTime.get(i - 1);
                    temp_burst[i - 1] = burst[i - 1];
                }


                //Redirects to methods for chosen algorithms
                if (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C') {
                    System.out.println("Invalid input. Please select a valid input from the example above.");
                } else if (chosenAlgo == 'A') {
                    //Shortest Job First chosen
                    SJFfindavgTime(proc, proc.length);
                } else if (chosenAlgo == 'B') {
                    //Round Robin Chosen
                    System.out.print("Enter quantum number: ");
                    tq = keyboard.nextInt();
                    
                    System.out.println(Arrays.toString(arrival));
                    System.out.println(Arrays.toString(burst));

                    while (timer < arrival[0]) //Incrementing Timer until the first process arrives
                    {
                        timer++;
                    }
                    queue[0] = 1;

                    while (true) {
                        boolean flag = true;
                        for (int i = 0; i < processNum; i++) {
                            if (temp_burst[i] != 0) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            break;
                        }

                        for (int i = 0; (i < processNum) && (queue[i] != 0); i++) {
                            int ctr = 0;
                            while ((ctr < tq) && (temp_burst[queue[0] - 1] > 0)) {
                                temp_burst[queue[0] - 1] -= 1;
                                timer += 1;
                                ctr++;

                                //Updating the ready queue until all the processes arrive
                                checkNewArrival(timer, arrival, processNum, maxProccessIndex, queue);
                            }
                            if ((temp_burst[queue[0] - 1] == 0) && (complete[queue[0] - 1] == false)) {
                                turn[queue[0] - 1] = timer;        //turn currently stores exit times
                                complete[queue[0] - 1] = true;
                            }

                            //checks whether or not CPU is idle
                            boolean idle = true;
                            if (queue[processNum - 1] == 0) {
                                for (int k = 0; k < processNum && queue[k] != 0; k++) {
                                    if (complete[queue[k] - 1] == false) {
                                        idle = false;
                                    }
                                }
                            } else {
                                idle = false;
                            }

                            if (idle) {
                                timer++;
                                checkNewArrival(timer, arrival, processNum, maxProccessIndex, queue);
                            }

                            //Maintaining the entries of processes after each premption in the ready Queue
                            queueMaintainence(queue, processNum);
                        }
                    }

                    for (int i = 0; i < processNum; i++) {
                        turn[i] = turn[i] - arrival[i];
                        wait[i] = turn[i] - burst[i];
                    }

                    System.out.print("\nProgram No.\tArrival Time\tBurst Time\tWait Time\tTurnAround Time"
                            + "\n");
                    for (int i = 0; i < processNum; i++) {
                        System.out.print(i + 1 + "\t\t" + arrival[i] + "\t\t" + burst[i]
                                + "\t\t" + wait[i] + "\t\t" + turn[i] + "\n");
                    }
                    for (int i = 0; i < processNum; i++) {
                        avgWait += wait[i];
                        avgTT += turn[i];
                    }
                    System.out.print("\nAverage wait time : " + (avgWait / processNum)
                            + "\nAverage Turn Around Time : " + (avgTT / processNum));
                    
                    //end of RR
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

    //computation methods for SJF
    static void findWaitingTime(Process proc[], int n, int wt[]) {
        int rt[] = new int[n];

        // Copy the burst time into rt[]
        for (int i = 0; i < n; i++) {
            rt[i] = proc[i].bt;
        }

        int complete = 0, t = 0, minm = Integer.MAX_VALUE;
        int shortest = 0, finish_time;
        boolean check = false;

        // Process until all processes gets completed
        while (complete != n) {

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
    static void SJFfindavgTime(Process proc[], int n) {
        int wt[] = new int[n], tat[] = new int[n];
        int total_wt = 0, total_tat = 0;
        String gantt = "";
        // Function to find waiting time of all processes
        findWaitingTime(proc, n, wt);

        // Function to find turn around time for all processes
        findTurnAroundTime(proc, n, wt, tat);

        // Display processes along with all details
        System.out.println("Processes "
                + " Arrival Time "
                + " Burst time "
                + " Waiting time "
                + " Turn around time");

        //Arrange based on smallest burst time
        ArrayList<Process> arranged = new ArrayList<Process>(n);

        for (int i = 0; i < n; i++) {
            Process temp = new Process(proc[i].pid, proc[i].bt, proc[i].art, wt[i], tat[i]);
            arranged.add(temp);
        }
        Collections.sort(arranged, new Comparator<Process>() {
            public int compare(Process e1, Process e2) {
                return Integer.compare(e1.getBurstTime(), e2.getBurstTime());
            }
        });
        System.out.println("start");
        for (int i = 0; i < n; i++) {
            System.out.println(" P" + arranged.get(i).pid + "\t\t"
                    + arranged.get(i).art + "\t\t"
                    + arranged.get(i).bt + "\t\t " + arranged.get(i).wt
                    + "\t\t" + arranged.get(i).tat);
        }

        System.out.println("end");

        // not arranged output
        // Calculate total waiting time and total turnaround time
        for (int i = 0; i < n; i++) {
            total_wt = total_wt + wt[i];
            total_tat = total_tat + tat[i];
            System.out.println(" P" + proc[i].pid + "\t\t"
                    + proc[i].art + "\t\t"
                    + proc[i].bt + "\t\t " + wt[i]
                    + "\t\t" + tat[i]);
        }

        System.out.println("Average waiting time = "
                + (float) total_wt / (float) n);
        System.out.println("Average turn around time = "
                + (float) total_tat / (float) n);
        for (int i = 0; i < n; i++) {
            gantt = gantt + "P" + proc[i].pid + ", ";
        }
        gantt = gantt.substring(0, gantt.length() - 2);
        System.out.println("Gantt Chart: " + gantt);
        gantt = "";

        return;
    }

    //computation methods for RR
//    static void RRfindWaitingTime(int processes[], int n,
//            int bt[], int wt[], int quantum) {
//        // Make a copy of burst times bt[] to store remaining
//        // burst times.
//        int rem_bt[] = new int[n];
//        for (int i = 0; i < n; i++) {
//            rem_bt[i] = bt[i];
//        }
//
//        int t = 0; // Current time
//
//        // Keep traversing processes in round robin manner
//        // until all of them are not done.
//        while (true) {
//            boolean done = true;
//
//            // Traverse all processes one by one repeatedly
//            for (int i = 0; i < n; i++) {
//                // If burst time of a process is greater than 0
//                // then only need to process further
//                if (rem_bt[i] > 0) {
//                    done = false; // There is a pending process
//
//                    if (rem_bt[i] > quantum) {
//                        // Increase the value of t i.e. shows
//                        // how much time a process has been processed
//                        t += quantum;
//
//                        // Decrease the burst_time of current process
//                        // by quantum
//                        rem_bt[i] -= quantum;
//                    } // If burst time is smaller than or equal to
//                    // quantum. Last cycle for this process
//                    else {
//                        // Increase the value of t i.e. shows
//                        // how much time a process has been processed
//                        t = t + rem_bt[i];
//
//                        // Waiting time is current time minus time
//                        // used by this process
//                        wt[i] = t - bt[i];
//
//                        // As the process gets fully executed
//                        // make its remaining burst time = 0
//                        rem_bt[i] = 0;
//                    }
//                }
//            }
//
//            // If all processes are done
//            if (done == true) {
//                break;
//            }
//        }
//    }
//
//    // Method to calculate turn around time
//    static void RRfindTurnAroundTime(int processes[], int n,
//            int bt[], int wt[], int tat[]) {
//        // calculating turnaround time by adding
//        // bt[i] + wt[i]
//        for (int i = 0; i < n; i++) {
//            tat[i] = bt[i] + wt[i];
//        }
//    }
//
//    // Method to calculate average time
//    static void RRfindavgTime(int processes[], int n, int bt[],
//            int quantum) {
//        int wt[] = new int[n], tat[] = new int[n];
//        int total_wt = 0, total_tat = 0;
//
//        // Function to find waiting time of all processes
//        RRfindWaitingTime(processes, n, bt, wt, quantum);
//
//        // Function to find turn around time for all processes
//        RRfindTurnAroundTime(processes, n, bt, wt, tat);
//
//        // Display processes along with all details
//        System.out.println("Processes "
//                + " Burst time "
//                + " Waiting time "
//                + " Turn around time");
//
//        // Calculate total waiting time and total turn
//        // around time
//        for (int i = 0; i < n; i++) {
//            total_wt = total_wt + wt[i];
//            total_tat = total_tat + tat[i];
//            System.out.println(" " + (i + 1) + "\t\t" + bt[i] + "\t "
//                    + wt[i] + "\t\t " + tat[i]);
//        }
//
//        System.out.println("Average waiting time = "
//                + (float) total_wt / (float) n);
//        System.out.println("Average turn around time = "
//                + (float) total_tat / (float) n);
//        return;
//    }
    
    //code methods for RR
    public static void queueUpdation(int queue[],int timer,int arrival[],int n, int maxProccessIndex){
        int zeroIndex = -1;
        for(int i = 0; i < n; i++){
            if(queue[i] == 0){
                zeroIndex = i;
                break;
            }
        }
        if(zeroIndex == -1)
            return;
        queue[zeroIndex] = maxProccessIndex + 1;
    }
 
    public static void checkNewArrival(int timer, int arrival[], int n, int maxProccessIndex,int queue[]){
        if(timer <= arrival[n-1]){
            boolean newArrival = false;
            for(int j = (maxProccessIndex+1); j < n; j++){
                if(arrival[j] <= timer){
                    if(maxProccessIndex < j){
                        maxProccessIndex = j;
                        newArrival = true;
                    }
                }
            }
            if(newArrival)    //adds the index of the arriving process(if any)
                queueUpdation(queue,timer,arrival,n, maxProccessIndex);       
        }
    }
   
    public static void queueMaintainence(int queue[], int n){
 
        for(int i = 0; (i < n-1) && (queue[i+1] != 0) ; i++){
            int temp = queue[i];
            queue[i] = queue[i+1];
            queue[i+1] = temp;
        }
    }
}
