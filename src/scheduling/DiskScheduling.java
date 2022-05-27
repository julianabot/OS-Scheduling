/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduling;

import java.util.*;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author Monica Mendiola
 */
class node { //sstf code

    // represent difference between head position and track number
    int distance = 0;

    // true if track has been accessed
    boolean accessed = false;
}

public class DiskScheduling {

//SSTF==========================================================================
    static void calculateDifference(int queue[], int head, node diff[]) {
        for (int i = 0; i < diff.length; i++) {
            diff[i].distance = Math.abs(queue[i] - head);
        }
    }

    // find unaccessed track
    // which is at minimum distance from head
    public static int findMin(node diff[]) {
        int index = -1, minimum = Integer.MAX_VALUE;

        for (int i = 0; i < diff.length; i++) {
            if (!diff[i].accessed && minimum > diff[i].distance) {

                minimum = diff[i].distance;
                index = i;
            }
        }
        return index;
    }

    static void shortestSeekTimeFirst(int request[], int head) {
        if (request.length == 0) {
            return;
        }

        // create array of objects of class node   
        node diff[] = new node[request.length];

        // initialize array
        for (int i = 0; i < diff.length; i++) {
            diff[i] = new node();
        }

        // count total number of seek operation   
        int seek_count = 0;

        // stores sequence in which disk access is done
        int[] seek_sequence = new int[request.length + 1];

        for (int i = 0; i < request.length; i++) {

            seek_sequence[i] = head;
            calculateDifference(request, head, diff);

            int index = findMin(diff);

            diff[index].accessed = true;

            // increase the total count
            seek_count += diff[index].distance;

            // accessed track is now new head
            head = request[index];
        }

        // for last accessed track
        seek_sequence[seek_sequence.length - 1] = head;

        double seek_time = (double) seek_count / (seek_sequence.length - 1);
        
         System.out.println(" ");
        System.out.println("Total head movement: " + seek_count);
        System.out.println("Average Seek Time: " + seek_time);

        System.out.println("_______________________________________________________________________________________");
        System.out.print("Seek Sequence: [START] →");
         for (int i = 0; i < seek_sequence.length; i++) {
            System.out.print(" " + seek_sequence[i] + " →");
        }
        System.out.println(" [END]");
        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
  
    }

//==============================================================================
//FCFS==========================================================================
    static void aFCFS(int arr[], int pos, int size, int head) {
        int seek_count = 0;
        int distance, cur_track;

        for (int i = 0; i < size; i++) {
            cur_track = arr[i];

            // calculate absolute distance
            distance = Math.abs(cur_track - pos);

            // increase the total count
            seek_count += distance;

            // accessed track is now new pos
            pos = cur_track;
        }

        double seek_time = (double) seek_count / size;

        System.out.println(" ");
        System.out.println("Total head movement: " + seek_count);
        System.out.println("Average Seek Time: " + seek_time);

        System.out.println("_______________________________________________________________________________________");
        System.out.print("Seek Sequence: [START] → " + head + " →");
        for (int i = 0; i < size; i++) {
            System.out.print(" " + arr[i] + " →");
        }
        System.out.println(" [END]");
        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
        
    }

//==============================================================================
//cscan=========================================================================
    static void aCSCAN(int arr[], int head, int size, int disk_size) {
        int seek_count = 0;
        int distance, cur_track;
        int fhead = head;
        Vector<Integer> left = new Vector<Integer>();
        Vector<Integer> right = new Vector<Integer>();
        Vector<Integer> seek_sequence = new Vector<Integer>();

        // Appending end values which has
        // to be visited before reversing
        // the direction
        left.add(0);
        right.add(disk_size - 1);

        // Tracks on the left of the
        // head will be serviced when
        // once the head comes back
        // to the beggining (left end).
        for (int i = 0; i < size; i++) {
            if (arr[i] < head) {
                left.add(arr[i]);
            }
            if (arr[i] > head) {
                right.add(arr[i]);
            }
        }

        // Sorting left and right vectors
        Collections.sort(left);
        Collections.sort(right);

        // First service the requests
        // on the right side of the
        // head.
        for (int i = 0; i < right.size(); i++) {
            cur_track = right.get(i);

            // Appending current track to seek sequence
            seek_sequence.add(cur_track);

            // Calculate absolute distance
            distance = Math.abs(cur_track - head);

            // Increase the total count
            seek_count += distance;

            // Accessed track is now new head
            head = cur_track;
        }

        // Once reached the right end
        // jump to the beggining.
        head = 0;

        // adding seek count for head returning from 199 to
        // 0
        seek_count += (disk_size - 1);

        // Now service the requests again
        // which are left.
        for (int i = 0; i < left.size(); i++) {
            cur_track = left.get(i);

            // Appending current track to
            // seek sequence
            seek_sequence.add(cur_track);

            // Calculate absolute distance
            distance = Math.abs(cur_track - head);

            // Increase the total count
            seek_count += distance;

            // Accessed track is now the new head
            head = cur_track;
        }

        double seek_time = (double) seek_count / size;

        System.out.println(" ");
        System.out.println("Total head movement: " + seek_count);
        System.out.println("Average Seek Time: " + seek_time);

        System.out.println("_______________________________________________________________________________________");
        System.out.print("Seek Sequence: [START] → " + fhead + " →");
        for (int i = 0; i < seek_sequence.size(); i++) {
            System.out.print(" " + seek_sequence.get(i) + " →");
        }
        System.out.println(" [END]");
        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
    }

//==============================================================================
//clook=========================================================================
    static void aCLOOK(int arr[], int head, int size, int disk_size) {
        int seek_count = 0;
        int distance, cur_track;
        int fhead = head;

        Vector<Integer> left = new Vector<Integer>();
        Vector<Integer> right = new Vector<Integer>();
        Vector<Integer> seek_sequence = new Vector<Integer>();

        // Tracks on the left of the
        // head will be serviced when
        // once the head comes back
        // to the beginning (left end)
        for (int i = 0; i < size; i++) {
            if (arr[i] < head) {
                left.add(arr[i]);
            }
            if (arr[i] > head) {
                right.add(arr[i]);
            }
        }

        // Sorting left and right vectors
        Collections.sort(left);
        Collections.sort(right);

        // First service the requests
        // on the right side of the
        // head
        for (int i = 0; i < right.size(); i++) {
            cur_track = right.get(i);

            // Appending current track
            // to seek sequence
            seek_sequence.add(cur_track);

            // Calculate absolute distance
            distance = Math.abs(cur_track - head);

            // Increase the total count
            seek_count += distance;

            // Accessed track is now new head
            head = cur_track;
        }

        // Once reached the right end
        // jump to the last track that
        // is needed to be serviced in
        // left direction
        seek_count += Math.abs(head - left.get(0));
        head = left.get(0);

        // Now service the requests again
        // which are left
        for (int i = 0; i < left.size(); i++) {
            cur_track = left.get(i);

            // Appending current track to
            // seek sequence
            seek_sequence.add(cur_track);

            // Calculate absolute distance
            distance = Math.abs(cur_track - head);

            // Increase the total count
            seek_count += distance;

            // Accessed track is now the new head
            head = cur_track;
        }

        double seek_time = (double) seek_count / size;

        System.out.println(" ");
        System.out.println("Total head movement: " + seek_count);
        System.out.println("Seek Time: " + seek_time);
        System.out.println("_______________________________________________________________________________________");
        System.out.print("Seek Sequence: [START] → " + fhead + " →");
        for (int i = 0; i < seek_sequence.size(); i++) {
            System.out.print(" " + seek_sequence.get(i) + " →");
        }
        System.out.println(" [END]");
        System.out.println("¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
    }
//==============================================================================

// Driver code
    public static void main(String[] args) {
        
        char chosenAlgo;
        char inputAlgo;
        char chooseContinue;
        int size;
        int tsize;

        do {
            Scanner sc = new Scanner(System.in);

            do {
                System.out.println("Disk Scheduling Algorithms");
                System.out.println("[A] First Come First Serve (FCFS)");
                System.out.println("[B] Shortest Seek Time First (SSTF)");
                System.out.println("[C] Circular Scan (CSCAN)");
                System.out.println("[D] Circular Look (CLOOK)");
                System.out.println("[E] Exit");
                
                do{
                    System.out.print("Compute for: ");
                    inputAlgo = sc.next().charAt(0);
                    chosenAlgo = Character.toUpperCase(inputAlgo);
                    
                    if(chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C' && chosenAlgo != 'D' && chosenAlgo != 'E'){
                         System.out.println("Invalid input. Please select a valid input from the example above.");
                    }
                  } while(chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C' && chosenAlgo != 'D' && chosenAlgo != 'E');
                    
                
                if (chosenAlgo == 'E') {
                    System.out.println("End of program.");
                    break;
                }
                System.out.print("Input Current Position: ");
                int pos = sc.nextInt();

                do{
                    System.out.print("Input Track Size: ");
                    tsize = sc.nextInt();
                    
                    if (tsize < pos){
                            System.out.println("Track size must not be smaller than current head position.");
                    }
                }while (tsize < pos);
                
                do {
                    System.out.print("Input no. of processes [2-9]: ");
                    size = sc.nextInt();

                    if (size > 9 || size < 2) {
                        System.out.println("Invalid Value. Please choose a number between 2 and 9.");
                    }
                } while (size > 9 || size < 2);

                int arr[] = new int[size];
                for (int i = 0; i < size; i++) {
                    do{
                    System.out.print("Location" + " " + (i + 1) + ": ");
                    arr[i] = sc.nextInt();
                        
                        if(arr[i] > tsize){
                            System.out.println("Process location must not be greater than the track size.");
                        }
                    }while(arr[i] > tsize);
                }

                //Redirects to methods for chosen algorithms
                if (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C' && chosenAlgo != 'D' && chosenAlgo != 'E') {
                    System.out.println("Invalid input. Please select a valid input from the example above.");
                } else if (chosenAlgo == 'A') {
                    aFCFS(arr, pos, size, pos);
                } else if (chosenAlgo == 'B') {
                    shortestSeekTimeFirst(arr, pos);
                } else if (chosenAlgo == 'C') {
                    //Shortest Job First chosen
                    aCSCAN(arr, pos, size, tsize);
                } else if (chosenAlgo == 'D') {
                    //Shortest Job First chosen
                    aCLOOK(arr, pos, size, tsize);
                } else {
                    System.out.println("End of program.");
                }

            } while (chosenAlgo != 'A' && chosenAlgo != 'B' && chosenAlgo != 'C' && chosenAlgo != 'D' && chosenAlgo != 'E');

            if (chosenAlgo == 'E') {
                break;
            }

            System.out.print("Input again? (Y/N): ");
            chooseContinue = Character.toUpperCase(sc.next().charAt(0));

            if (chooseContinue != 'Y' && chooseContinue != 'N') {
                System.out.println("Invalid input. Program will now be terminated");
            }
        } while (chooseContinue == 'Y');

    }
}
