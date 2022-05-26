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
                System.out.println("[A] Shortest Job First (SJF)");
                System.out.println("[B] Preemptive Priority (P-Prio)");
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
                //Creates an array to accept list of priorities if P-Prio is chosen
                int priority[] = new int[processNum + 1];
                int x[] = new int[processNum];

                //Redirects to methods for chosen algorithms
                if (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C') {
                    System.out.println("Invalid input. Please select a valid input from the example above.");
                } else if (chosenAlgo == 'A') {
                    //Shortest Job First chosen
                    SJFfindavgTime(proc, proc.length);
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

                    for (time = 0; count != processNum; time++) {
                        smallest = processNum;
                        for (i = 0; i < processNum; i++) {
                            //sets the smallest (index) value to the array index with the least value
                            if (priority[i] < priority[smallest] && burstTimePPrio[i] > 0) {
                                smallest = i;
                            }
                        }

                        burstTimePPrio[smallest]--;
                        if (burstTimePPrio[smallest] == 0) {
                            count++;
                            end = time + 1;
                            completionTimePPrio[smallest] = end + 1;
                            waitingTimePPrio[smallest] = end - arrivalTimePPrio[smallest] - x[smallest] + 1;
                            turnaroundTimePPrio[smallest] = end - arrivalTimePPrio[smallest] + 1;
                        }
                    }

                    System.out.println("Process\tBurst-time\tarrival-time\twaiting-time\tturnaround-time\tcompletion-time\tpriority");

                    for (i = 0; i < processNum; i++) {
                        System.out.println("p" + (i + 1) + "\t\t" + x[i] + "\t\t" + arrivalTimePPrio[i] + "\t\t" + waitingTimePPrio[i] + "\t\t" + turnaroundTimePPrio[i] + "\t\t" + completionTimePPrio[i] + "\t\t" + priority[i]);
                        avg = avg + waitingTimePPrio[i];
                        tt = tt + turnaroundTimePPrio[i];
                    }

                    System.out.println("Average waiting time = " + (avg / processNum));
                    System.out.println("Average turnaround time = " + (tt / processNum));

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

    //computation methods for SJF
    static void findWaitingTime(Process proc[], int n, int wt[], ArrayList<Integer> gProcess, ArrayList<Integer> gTime) {
        int rt[] = new int[n];

        // Copy the burst time into rt[]
        for (int i = 0; i < n; i++) {
            rt[i] = proc[i].bt;
        }

        int complete = 0, t = 0, minm = Integer.MAX_VALUE;
        int shortest = 0, finish_time;
        boolean check = false;
        int i = 0;
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
                if (t == 0) {
                } else if (gProcess.isEmpty()) {
                    gProcess.add(proc[shortest].pid);
                    gTime.add(t);
                    i++;
                } else if (gProcess.get(i - 1) != proc[shortest].pid) {
                    gProcess.add(proc[shortest].pid);
                    gTime.add(t);
                    i++;
                }
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
            if (gProcess.isEmpty()) {
                gProcess.add(proc[shortest].pid);
                gTime.add(t);
                i++;
            } else if (gProcess.get(i - 1) != proc[shortest].pid) {
                gProcess.add(proc[shortest].pid);
                gTime.add(t);
                i++;
            }

            t++;
        }
        gTime.add(t);
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
        String completion = "";

        ArrayList<Integer> gProcess = new ArrayList<Integer>();
        ArrayList<Integer> gTime = new ArrayList<Integer>();

        // Function to find waiting time of all processes
        findWaitingTime(proc, n, wt, gProcess, gTime);

        // Function to find turn around time for all processes
        findTurnAroundTime(proc, n, wt, tat);

        // Display processes along with all details
        System.out.println("Processes "
                + " Arrival Time "
                + " Burst time "
                + " Turnaround time"
                + " Waiting time "
        );

        //Arrange based on smallest burst time
        ArrayList<Process> arranged = new ArrayList<Process>(n);

        for (int i = 0; i < n; i++) {
            Process temp = new Process(proc[i].pid, proc[i].bt, proc[i].art, wt[i], tat[i]);
            arranged.add(temp);
        }
        Collections.sort(arranged, new Comparator<Process>() {
            public int compare(Process e1, Process e2) {
                return Integer.compare(e1.art, e2.art);
            }
        });

        for (int i = 0; i < n; i++) {
            System.out.println(" P" + arranged.get(i).pid + "\t\t"
                    + arranged.get(i).art + "\t\t"
                    + arranged.get(i).bt + "\t\t " + arranged.get(i).tat
                    + "\t\t" + arranged.get(i).wt);
            total_wt += arranged.get(i).wt;
            total_tat += arranged.get(i).tat;
        }

        // Calculate total waiting time and total turnaround time
        System.out.println("Average Waiting Time = "
                + (float) total_wt / (float) n);
        System.out.println("Average Turnaround Time = "
                + (float) total_tat / (float) n);

        for (int i = 0; i < gProcess.size(); i++) {
            gantt = gantt + "P" + gProcess.get(i) + " | ";
        }

        gantt = gantt.substring(0, gantt.length() - 2);

        for (int i = 0; i < gTime.size(); i++) {
            if (i == 0) {
                completion = completion + "\t\t     " + gTime.get(i) + "    ";
            } else if (String.valueOf(gTime.get(i)).length() == 1) {
                completion = completion + gTime.get(i) + "    ";
            } else if (String.valueOf(gTime.get(i)).length() == 2) {
                completion = completion + gTime.get(i) + "   ";
            } else if (String.valueOf(gTime.get(i)).length() == 3) {
                completion = completion + gTime.get(i) + "  ";
            }
        }
        System.out.println("____________________________________________________________");
        System.out.println("Gantt Chart: [START] | " + gantt + "| [END]");
        System.out.println(completion);
        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
        return;
    }

}
