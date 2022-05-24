package scheduling;

import java.util.*;

public class NonPreemptiveScheduling {

    public static void main(String[] args) {
        new NonPreemptiveScheduling();
    }

    public void display_menu() {
        System.out.println("Non-Preemptive Scheduling Algorithms");
        System.out.println("[A] SJF");
        System.out.println("[B] FCFS");
        System.out.print("Compute for: ");
    }

    public NonPreemptiveScheduling() {
        Scanner sc = new Scanner(System.in);
        char chooseContinue;
        char chosenAlgo;

        do {
            display_menu();
            chosenAlgo = sc.next().charAt(0);

            System.out.println("Input no. of processes [2-9]: ");
            int n = sc.nextInt();
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
                            // tinanggal ko muna para same sa iba  + " Completion Time "
                            + " Turn around Time "
                            + " Waiting Time ");
                    for (int i = 0; i < n; i++) {
                        avgwt += wt[i];
                        avgta += ta[i];
                        System.out.println(" " + pid[i] + "\t\t" + at[i] + "\t\t" + bt[i] + "\t\t" + ta[i] + "\t\t" + wt[i]);
                    }
                    System.out.println("Average waiting time = " + (float) (avgwt / n));

                    System.out.println("Average turn around time = " + (float) (avgta / n));
//                    for (int i = 0; i < n; i++) {
//                        System.out.println(pid[i] + " ");
//                    }
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
                        ta[i] = ct[i] - at[i];          // turnaround time= completion time- arrival time
                        wt[i] = ta[i] - bt[i];          // waiting time= turnaround time- burst time
                        avgwt += wt[i];               // total waiting time
                        avgta += ta[i];               // total turnaround time
                    }
                    System.out.println("Processes "
                            + " Arrival Time "
                            + " Burst Time "
                            // tinanggal ko muna para same sa iba  + " Completion Time "
                            + " Turn around Time "
                            + " Waiting Time ");

                    for (int i = 0; i < n; i++) {
                        //System.out.println("P" + pid[i] + "\t" + wt[i]);
                        System.out.println(" " + pid[i] + "\t\t" + at[i] + "\t\t" + bt[i] + "\t\t" + ta[i] + "\t\t" + wt[i]);
                    }
//                    System.out.println("\n   Turnaround time");
//                    for (int i = 0; i < n; i++) {
//                        System.out.println("P" + pid[i] + "\t" + ta[i]);
//                    }
                    System.out.println("\nAverage Waiting Time: " + (avgwt / n));     // printing average waiting time.
                    System.out.println("Average Turnaround Time: " + (avgta / n));    // printing average turnaround time.
                    break;
                default:
                    System.err.println("Unrecognized option");
                    break;
            }

            System.out.println("Input again? (Y/N): ");
            chooseContinue = Character.toUpperCase(sc.next().charAt(0));

        } while (chooseContinue != 'N');

    }
}
