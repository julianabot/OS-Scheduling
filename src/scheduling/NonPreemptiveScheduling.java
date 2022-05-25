package scheduling;

import java.util.*;

public class NonPreemptiveScheduling {

    public static void main(String[] args) {
        new NonPreemptiveScheduling();
    }

    public void display_menu() {
        System.out.println("Non-Preemptive Scheduling Algorithms");
        System.out.println("[A] Shortest Job First (SJF)");
        System.out.println("[B] First Come First Serve (FCFS)");
        System.out.println("[C] Exit");
        System.out.print("Compute for: ");
    }

    public NonPreemptiveScheduling() {
        Scanner sc = new Scanner(System.in);
        char chooseContinue;
        char chosenAlgo;
        String gantt = "";
        int n;
        do {
            display_menu();
            chosenAlgo = sc.next().charAt(0);
            System.out.println("------------------------------------------------------------");
            switch (chosenAlgo) {
                case 'A':
                    System.out.println("Shortest Job First (SJF)");
                    break;
                case 'B':
                    System.out.println("First Come First Serve (FCSF)");
                    break;
            }
            if (chosenAlgo == 'C' || chosenAlgo != 'A' || chosenAlgo != 'B') {
                System.out.println("End of program.");
                break;
            }

            do {
                System.out.print("Input no. of processes [2-9]: ");
                n = sc.nextInt();
                if (n > 9 || n < 2) {
                    System.out.println("Invalid value. Please choose a number between 2 and 9");
                }
            } while (n > 9 || n < 2);

            int pid[] = new int[n];
            int at[] = new int[n];
            int bt[] = new int[n];
            int ct[] = new int[n];
            int ta[] = new int[n];
            int wt[] = new int[n];
            int f[] = new int[n];

            for (int i = 0; i < n; i++) {
                System.out.print("AT" + (i + 1) + ": ");
                at[i] = sc.nextInt();
                pid[i] = i + 1;
                f[i] = 0;
            }

            for (int i = 0; i < n; i++) {
                System.out.print("BT" + (i + 1) + ": ");
                bt[i] = sc.nextInt();
            }

            switch (chosenAlgo) {
                case 'A':
                    int st = 0,
                     tot = 0;
                    float avgwt = 0,
                     avgta = 0;

                    while (true) {
                        int c = n, min = 999999;
                        if (tot == n) {
                            break;
                        }
                        for (int i = 0; i < n; i++) {
                            if ((at[i] <= st) && (f[i] == 0) && (bt[i] < min)) {
                                min = bt[i];
                                c = i;
                            }
                        }
                        if (c == n) {
                            st++;
                        } else {
                            ct[c] = st + bt[c];
                            st += bt[c];
                            ta[c] = ct[c] - at[c];
                            wt[c] = ta[c] - bt[c];
                            f[c] = 1;
                            pid[tot] = c + 1;
                            tot++;
                        }
                    }

                    System.out.println("Processes "
                            + " Arrival Time "
                            + " Burst Time "
                            + " Turnaround Time "
                            + " Waiting Time ");

                    for (int i = 0; i < n; i++) {
                        avgwt += wt[i];
                        avgta += ta[i];
                        System.out.println(" P" + pid[i] + "\t\t" + at[i] + "\t\t" + bt[i] + "\t\t" + ta[i] + "\t\t" + wt[i]);
                    }
                    System.out.println("Average Waiting Time = " + (float) (avgwt / n));

                    System.out.println("Average Turnaround Time = " + (float) (avgta / n));

                    for (int i = 0; i < n; i++) {
                        gantt = gantt + "P" + pid[i] + " | ";
                    }

                    gantt = gantt.substring(0, gantt.length() - 2);

                    System.out.println("____________________________________________________________");
                    System.out.println("Gantt Chart: [START] " + gantt + " [END]");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");

                    gantt = "";
                    break;

                case 'B':
                    int temp;
                    avgwt = 0;
                    avgta = 0;

                    //sorting according to arrival times
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n - (i + 1); j++) {
                            if (at[j] > at[j + 1]) {
                                temp = at[j];
                                at[j] = at[j + 1];
                                at[j + 1] = temp;
                                temp = bt[j];
                                bt[j] = bt[j + 1];
                                bt[j + 1] = temp;
                                temp = pid[j];
                                pid[j] = pid[j + 1];
                                pid[j + 1] = temp;
                            }
                        }
                    }

                    // finding completion times
                    for (int i = 0; i < n; i++) {
                        if (i == 0) {
                            ct[i] = at[i] + bt[i];
                        } else {
                            if (at[i] > ct[i - 1]) {
                                ct[i] = at[i] + bt[i];
                            } else {
                                ct[i] = ct[i - 1] + bt[i];
                            }
                        }
                        ta[i] = ct[i] - at[i];        // turnaround time= completion time- arrival time
                        wt[i] = ta[i] - bt[i];        // waiting time= turnaround time- burst time
                        avgwt += wt[i];               // total waiting time
                        avgta += ta[i];               // total turnaround time
                    }

                    System.out.println("Processes "
                            + " Arrival Time "
                            + " Burst Time "
                            + " Turnaround Time "
                            + " Waiting Time ");

                    for (int i = 0; i < n; i++) {
                        System.out.println(" P" + pid[i] + "\t\t" + at[i] + "\t\t" + bt[i] + "\t\t" + ta[i] + "\t\t" + wt[i]);
                    }
                    System.out.println("Average Waiting Time: " + (float) (avgwt / n));     // printing average waiting time.
                    System.out.println("Average Turnaround Time: " + (float) (avgta / n));  // printing average turnaround time.

                    //Printing for gantt chart
                    for (int i = 0; i < n; i++) {
                        gantt = gantt + "P" + pid[i] + " | ";
                    }

                    gantt = gantt.substring(0, gantt.length() - 2);
                    System.out.println("____________________________________________________________");
                    System.out.println("Gantt Chart: [START] " + gantt + " [END]");
                    System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");

                    gantt = "";

                    break;
                case 'C':
                    System.out.println("End of program");
                    break;
                default:
                    System.err.println("Unrecognized option");
                    break;
            }

            System.out.print("Input again? (Y/N): ");

            chooseContinue = Character.toUpperCase(sc.next().charAt(0));
        } while (chooseContinue != 'N');

    }
}
