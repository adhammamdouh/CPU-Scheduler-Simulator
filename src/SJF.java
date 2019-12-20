/**
 * Date 11/25/2019
 * By Ashraf Samer
 * Shortest Job First Algorithm
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class SJF {
    private Graph graph;
    private ArrayList<Process> Out = new ArrayList<Process>();

    public static void main(String[] args) throws InterruptedException {

    }

    private void rearrangement(ArrayList<Process> sortedByArrival) {
        Collections.sort(sortedByArrival, (o1, o2) -> {
            int value = o1.getArrivalTime();
            if (o2.getArrivalTime() == value) {

                if (o2.getBurstTime() < value) return 1;
                else if (o2.getBurstTime() > value) return -1;
                else return 0;
            } else {

                if (o2.getArrivalTime() < value) return 1;
                else if (o2.getArrivalTime() > value) return -1;
                else return 0;
            }
        });
    }

    public SJF() throws InterruptedException {
        graph = new Graph("SJF");
    }

    public void start(ArrayList<Process> sortedByArrival) {
        rearrangement(sortedByArrival);
        ArrayList<Process> output = new ArrayList<>();
        Process process;

        int lastFinishTime = 0;

        int time = 0;
        int counter = 0;
        while (counter < sortedByArrival.size()) {
            ArrayList<Process> windowOfArrived = new ArrayList<>();
            for (int j = 0; j < sortedByArrival.size(); j++) {
                if (sortedByArrival.get(j).getArrivalTime() <= lastFinishTime) {
                    windowOfArrived.add(sortedByArrival.get(j));
                }
            }
            if ((windowOfArrived.size() == 0)) {
                output.add(sortedByArrival.get(0));
                time = sortedByArrival.get(0).getArrivalTime();
                for (int j = 0; j < sortedByArrival.get(0).getBurstTime(); j++) {
                    try {
                        graph.add(sortedByArrival.get(0), time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time++;
                }
                sortedByArrival.get(0).setWaitingTime(0);
                sortedByArrival.get(0).setTurnAroundTime(sortedByArrival.get(0).getBurstTime());
                lastFinishTime = sortedByArrival.get(0).getArrivalTime() + sortedByArrival.get(0).getBurstTime();
                graph.addTerminatedProcess(sortedByArrival.get(0));
                //sortedByArrival.remove(sortedByArrival.get(0));
                counter++;
            } else {
                process = getSmallestBurst(windowOfArrived);
                process.setWaitingTime(lastFinishTime - process.getArrivalTime());
                process.setTurnAroundTime(process.getWaitingTime() + process.getBurstTime());
                lastFinishTime += process.getBurstTime();
                output.add(process);
                for (int j = 0; j < process.getBurstTime(); j++) {
                    try {
                        graph.add(process, time);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time++;
                }
                graph.addTerminatedProcess(process);
                windowOfArrived.clear();
                //sortedByArrival.remove(process);
                counter++;
            }
        }

        Out = output;
    }

    private Process getSmallestBurst(ArrayList<Process> arr) {
        int smallestBurst = arr.get(0).getBurstTime();
        Process p = arr.get(0);
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).getBurstTime() < smallestBurst) {
                smallestBurst = arr.get(i).getBurstTime();
                p = arr.get(i);
            }
        }
        return p;
    }

    private void print(ArrayList<Process> p) {
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i) == null) {
                System.out.println("Null");
                continue;
            }
            System.out.println(p.get(i).toString());
        }


    }

    private void print(Process p) {
        System.out.println(p.toString());
    }

    private double averageWaiting() {
        double sum = 0;
        for (int i = 0; i < Out.size(); i++) {
            sum += Out.get(i).getWaitingTime();
        }
        return (sum / Out.size());
    }

    private double averageTurnaround() {
        double sum = 0;
        for (int i = 0; i < Out.size(); i++) {
            sum += Out.get(i).getTurnAroundTime();
        }
        return (sum / Out.size());
    }

    /*
0 5 P1
2 3 P2
4 2 P3
6 4 P4
7 1 P5

0 7 P1
2 4 P2
4 1 P3
5 2 P4

0 1 P1
3 5 P2
4 2 P3
6 4 P4
7 1 P5
 */

}