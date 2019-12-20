/**
 * Date 11/2/2019
 * By Ahmed Sayed
 * <p>
 * Shortest remaining time first(SRTF)
 * Using context Switching
 */

import java.awt.*;
import java.util.*;


public class SRTF  {
    /**
     * Instructions:
     * <p>
     * First you can add any function or attribute you want
     * but you have to write
     * 1- clean code
     * 2- readable Names
     * 3- clear Structure
     */

    private static ArrayList<String> processess = new ArrayList<>();
    private static double averageTurnAroundTime = 0;
    private static double averageWaitingTime = 0;

    private Queue<Process> readyQueue; //The ready Processes Queue you can use any data Structure you want
    private Graph graph = new Graph("SRTF"); // graph object not implemented yet.

    public SRTF() throws InterruptedException {
    }

    public static void main(String[] args) throws InterruptedException {

    }

    public ArrayList<Process> start(ArrayList<Process> processes, int cs) throws InterruptedException {
        reArrange(processes);
        int currentTime = processes.get(0).getArrivalTime();
        int contextSwitching = cs;
        int currentProcessIndex = getSmallestBurst(processes, currentTime);
        int smallestBurstTime = currentProcessIndex;
        int counter = 0;
        boolean flag = false;

        while (counter != processes.size()) {
            Process currentProcess = processes.get(currentProcessIndex);
            if (smallestBurstTime != -1 ) {
                processess.add(currentProcess.getName());
                graph.add(currentProcess, currentTime);
                //Thread.sleep(1000);
                //System.out.println(processess);
                currentProcess.setBurstTime(currentProcess.getBurstTime() - 1);
                currentTime += 1;

                if (processes.get(currentProcessIndex).getBurstTime() == 0) {
                    //TODO do not add context switch to process does not has context switch
                    currentProcess.setFinishTime(currentTime + contextSwitching);
                    currentProcess.setTurnAroundTime(currentProcess.getFinishTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getFinishTime() - currentProcess.getConstBurstTime() - currentProcess.getArrivalTime());
                    System.out.println(currentProcess.getFinishTime() + " " + currentProcess.getConstBurstTime() + " " + currentProcess.getArrivalTime());
                    graph.addTerminatedProcess(processes.get(currentProcessIndex));
                    //Thread.sleep(1000);
                    averageTurnAroundTime += currentProcess.getTurnAroundTime();
                    averageWaitingTime += currentProcess.getWaitingTime();
                    counter++;
                }
            }
            else{

                currentTime+=1;
            }

            smallestBurstTime = getSmallestBurst(processes, currentTime);

            if (smallestBurstTime == -1){
                flag = true;
                continue;
            }

            if (smallestBurstTime != currentProcessIndex) {

                if (flag != true) {currentTime += contextSwitching;}
                else{flag = false;}

                for (int j = 0; j < contextSwitching; j++) {

                    processess.add("--");
                }
                smallestBurstTime = getSmallestBurst(processes, currentTime);
                currentProcessIndex = smallestBurstTime;
            }

        }

        return processes;
    }

    private ArrayList reArrange(ArrayList<Process> list){
        Collections.sort(list,Collections.reverseOrder());
        return list;
    }

    private int getSmallestBurst(ArrayList<Process> arr, int currentTime) {

        int smallestBurst = -1;
        int tmp = -1;
        for (int i = 0; i < arr.size(); i++) {

            if (arr.get(i).getBurstTime() != 0 && arr.get(i).getArrivalTime() <= currentTime) {
                smallestBurst = arr.get(i).getBurstTime();
                tmp = i;
                break;
            }
        }
        if (tmp < 0){return -1;}

        int p = tmp;
        for (int i = tmp; i < arr.size(); i++) {
            if (arr.get(i).getBurstTime() < smallestBurst && arr.get(i).getArrivalTime() <= currentTime && arr.get(i).getBurstTime() != 0) {
                smallestBurst = arr.get(i).getBurstTime();
                p = i;
            }
        }
        return p;
    }
}