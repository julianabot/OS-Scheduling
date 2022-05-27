package scheduling;

import java.util.*;

class ProcessSJF {

    int pid; // Process ID
    int bt; // Burst Time
    int art; // Arrival Time
    int wt;
    int tat;
    int ct;

    public ProcessSJF(int pid, int art, int bt, int wt, int tat, int ct) {
        this.pid = pid;
        this.bt = bt;
        this.art = art;
        this.wt = wt;
        this.tat = tat;
        this.ct = ct;
    }
}

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
        String completion = "";
        int n;
        boolean notvalidint = true;
        do {
            do{
                display_menu();
                chosenAlgo = sc.next().charAt(0);
                chosenAlgo = Character.toUpperCase(chosenAlgo);
                
                System.out.println("------------------------------------------------------------");
                switch (chosenAlgo) {
                    case 'A':
                        System.out.println("Shortest Job First (SJF)");
                        break;
                    case 'B':
                        System.out.println("First Come First Serve (FCSF)");
                        break;
                }
                if (chosenAlgo == 'C') {
                    System.out.println("End of program.");
                    System.exit(0);
                }
                if(chosenAlgo < 'A' || chosenAlgo >'C'){
                     System.out.println("Invalid input. Please choose between the given choices.");
                }
            }while (chosenAlgo < 'A' || chosenAlgo >'C');
                do {
                    System.out.print("Input no. of processes [2-9]: ");
                    n = sc.nextInt();
                    if (n > 9 || n < 2 ) {
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
                do{
                  
                    try{
                        System.out.print("AT" + (i + 1) + ": ");
                        at[i] = sc.nextInt();
                        pid[i] = i + 1;
                        f[i] = 0;
                        notvalidint = false;
                    }
                    catch(InputMismatchException e){
                        sc.next();
                        System.out.println("Invalid input. Please enter an integer.");
                        notvalidint = true;
                        
                    }
                }while(notvalidint);
               
            }

            //to check if there is a 0 burst time
            int badBurst = 0;
            boolean valid;

            for (int i = 0; i < n; i++) {
                //System.out.print("BT" + (i + 1) + ": ");
                //bt[i] = sc.nextInt();
                //if (bt[i] == 0) {
                //    valid = false;
                //}
                do{
                    try{
                        do {
                                System.out.print("BT" + (i + 1) + ": ");
                                bt[i] = sc.nextInt();
                                valid = bt[i] > 0;
                                if (!valid) {
                                    System.out.println("Invalid Input. Input  nonzero number");
                                }
                        } while (!valid);
                        notvalidint = false;
                    }
                    catch(InputMismatchException e){
                        sc.next();
                        System.out.println("Invalid input. Please enter an integer.");
                        notvalidint = true;
                        
                    }
                }while(notvalidint);
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

                        ArrayList<ProcessSJF> arr = new ArrayList<ProcessSJF>(n);

                        for (int i = 0; i < n; i++) {
                            int index = pid[i] - 1;
                            ProcessSJF temp = new ProcessSJF(index + 1, at[index], bt[index], wt[index], ta[index], ct[index]);
                            arr.add(temp);
                        }

                        System.out.println("Processes "
                                + " Arrival Time "
                                + " Burst Time "
                                + " Turnaround Time "
                                + " Waiting Time ");

                        for (int i = 0; i < n; i++) {
                            avgwt += arr.get(i).wt;
                            avgta += arr.get(i).tat;
                            System.out.println(" P" + arr.get(i).pid + "\t\t" + arr.get(i).art + "\t\t" + arr.get(i).bt + "\t\t" + arr.get(i).tat + "\t\t" + arr.get(i).wt);
                        }

                        System.out.println("Average Waiting Time = " + (float) (avgwt / n));
                        System.out.println("Average Turnaround Time = " + (float) (avgta / n));

                        for (int i = 0; i < n; i++) {
                            gantt = gantt + "P" + arr.get(i).pid + " | ";
                        }

                        gantt = gantt.substring(0, gantt.length() - 2);

                        for (int i = 0; i < n; i++) {
                            if (i == 0) {
                                completion = completion + "\t\t     " + arr.get(i).art + "    ";
                            }
                            if (String.valueOf(arr.get(i).ct).length() == 1) {
                                completion = completion + arr.get(i).ct + "    ";
                            } else if (String.valueOf(arr.get(i).ct).length() == 2) {
                                completion = completion + arr.get(i).ct + "   ";
                            } else if (String.valueOf(arr.get(i).ct).length() == 3) {
                                completion = completion + arr.get(i).ct + "  ";
                            }
                        }

                        System.out.println("_______________________________________________________________________________________");
                        System.out.println("Gantt Chart: [START] | " + gantt + "| [END]");
                        System.out.println(completion);
                        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                        completion = "";
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
                            ta[i] = ct[i] - at[i];                              // turnaround time= completion time- arrival time
                            wt[i] = ta[i] - bt[i];                              // waiting time= turnaround time- burst time
                            avgwt += wt[i];                                     // total waiting time
                            avgta += ta[i];                                     // total turnaround time
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

                        for (int i = 0; i < n; i++) {
                            if (i == 0) {
                                completion = completion + "\t\t     " + at[i] + "    ";
                            }
                            if (String.valueOf(ct[i]).length() == 1) {
                                completion = completion + ct[i] + "    ";
                            } else if (String.valueOf(ct[i]).length() == 2) {
                                completion = completion + ct[i] + "   ";
                            } else if (String.valueOf(ct[i]).length() == 3) {
                                completion = completion + ct[i] + "  ";
                            }
                        }

                        gantt = gantt.substring(0, gantt.length() - 2);
                        System.out.println("_______________________________________________________________________________________");
                        System.out.println("Gantt Chart: [START] | " + gantt + "| [END]");
                        System.out.println(completion);
                        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
                        completion = "";
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
