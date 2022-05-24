package scheduling;

import java.util.Scanner;

public class OSScheduling {

    public static void main(String[] args) {

        char chosenAlgo;
        char quit;
        Scanner sc = new Scanner(System.in);
        do {
            do {
                System.out.println("Scheduling Algorithms");
                System.out.println("[A] Preemptive Scheduling");
                System.out.println("[B] Non-Preemptive Scheduling");
                System.out.println("[C] Disk Scheduling");
                System.out.print("Compute for");

                chosenAlgo = sc.next().charAt(0);
                chosenAlgo = Character.toUpperCase(chosenAlgo);

                if (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C') {
                    System.out.println("Invalid input. Please select a valid input from the choices above.");
                } else if (chosenAlgo == 'A') {
                    PreemptiveScheduling.main(new String[10]);
                } else if (chosenAlgo == 'B') {
                    NonPreemptiveScheduling.main(new String[10]);
                } else if (chosenAlgo == 'C') {
                    DiskScheduling.main(new String[10]);
                } else {
                    System.out.println("Invalid Input. ");
                }
            } while (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C');

            System.out.println("Choose another scheduling algorithm? Type Q to quit program.");
            quit = sc.next().charAt(0);
            quit = Character.toUpperCase(quit);
        } while (quit != 'Q');

    }
}
