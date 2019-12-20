import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityScheduling {
    private Comparator<Process> sortByPriority = Comparator.comparing(Process::getPriority);
    private PriorityQueue<Process> readyQueue = new PriorityQueue<>(sortByPriority);
    private ArrayList<Process> arrivalQueue = new ArrayList<Process>();
    private ArrayList<String> order = new ArrayList<String>();
    int currentTime = 0;
    private Graph graph = new Graph("priority scheduling ");

    public PriorityScheduling() throws InterruptedException {
    }

    public List<String> start(ArrayList<Process> arrivalQueue) throws InterruptedException {
        this.arrivalQueue = arrivalQueue;
        int i = 0, changeValue = 0;
        Process currentProcess = new Process();
        while (true) {
            if (!arrivalQueue.isEmpty() && currentTime < arrivalQueue.get(0).getArrivalTime()) {
                order.add(null);
                currentTime++;
                continue;
            }
            while (i != arrivalQueue.size() && currentTime >= arrivalQueue.get(i).getArrivalTime()) {
                currentProcess = arrivalQueue.get(i);
                readyQueue.add(currentProcess);
                ++i;
            }
            break;
        }
        currentProcess = readyQueue.poll();

        while (true) {

            if (readyQueue.isEmpty() && isDead(currentProcess) && i == arrivalQueue.size()) break;


            while (i < arrivalQueue.size() && currentTime >= arrivalQueue.get(i).getArrivalTime()) {
                changePriority(arrivalQueue.get(i), changeValue);
                readyQueue.add(arrivalQueue.get(i));
                ++i;
            }

            if (isDead(currentProcess)) {
                changeWaitingTime(currentProcess);
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.poll();
                    --changeValue;
                }
            }


            secondPassed(currentProcess, ++changeValue);

        }
        return order;
    }


    private void secondPassed(Process currentProcess, int changeVlaue) throws InterruptedException {
        if (!isDead(currentProcess)) {
            int burst = currentProcess.getBurstTime();
            currentProcess.setBurstTime(burst - 1);
            graph.add(currentProcess, currentTime);
            order.add(currentProcess.getName());
        } else {
            order.add(null);
        }
        ++currentTime;
    }


    private void changeWaitingTime(Process currentProcess) {
        int arrival = currentProcess.getArrivalTime();
        int burst = currentProcess.getBurstTime();
        currentProcess.setWaitingTime(currentTime - (arrival + burst));
    }


    private void changePriority(Process process, int changeValue) {
        int priority = process.getPriority();
        process.setPriority(priority + changeValue);

    }


    private boolean isDead(Process currentProcess) {
        if (currentProcess.getBurstTime() == 0) {
            graph.addTerminatedProcess(currentProcess);
            currentProcess.setTurnAroundTime(currentProcess.getWaitingTime() + currentProcess.getConstBurstTime());
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
//        Process p1 = new Process("p1",Color.RED,0,3,4);
//        Process p2 = new Process("p2",Color.BLACK,0,5,3);
//        Process p3 = new Process("p3",Color.BLACK,10,5,4);
//
//        PriorityScheduling tst = new PriorityScheduling();
//        tst.arrivalQueue.add(p1);
//        tst.arrivalQueue.add(p2);
//        tst.arrivalQueue.add(p3);
//        List<String> data=tst.start();
//        for(String t:data) {
//            System.out.println(t);
//        }
    }
}