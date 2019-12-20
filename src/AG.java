import java.awt.Color;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Date 28/11/2019
 * By Abd Ul-Rahman Okasha
 */

public class AG {
    ArrayList<Process> process = new ArrayList<>();
    ArrayList<Process> arrivalQueue = new ArrayList<>();
    ArrayList<String> quantumInfo = new ArrayList<String>();
    ArrayList<String> excutionOrder = new ArrayList<String>();
    ArrayList<String> order = new ArrayList<String>();
    private int currentTime = 0;
    Graph graph = new Graph("AG Scheduling");

    AG(ArrayList<Process> process) throws InterruptedException {
        this.process = process;
    }

    AG() throws InterruptedException {

    }

    public void start(ArrayList<Process> arrivalQueue) throws InterruptedException {
        this.arrivalQueue = arrivalQueue;
        int i = 0, processRunningTime = 0;
        Process currentProcess = new Process();

        while (true) {
            if (!arrivalQueue.isEmpty() && currentTime < arrivalQueue.get(0).getArrivalTime()) {
                order.add(null);
                currentTime++;
                continue;
            }
            while (i != arrivalQueue.size() && currentTime >= arrivalQueue.get(i).getArrivalTime()) {
                currentProcess = arrivalQueue.get(i);
                calculateFactor(currentProcess);
                process.add(currentProcess);
                ++i;
            }
            break;
        }
        int ans = getMinimumFactorProcess(currentProcess);
        if (ans != -1) {
            currentProcess = process.get(ans);
            process.remove(ans);
        } else {
            process.remove(process.size() - 1);
        }

        while (true) {

            if (process.isEmpty() && isDead(currentProcess) && i == arrivalQueue.size()) break;

//			System.out.println(process.size() + " " + processRunningTime + " " +currentProcess.getBurstTime()+ " "+currentTime);

            while (i < arrivalQueue.size() && currentTime >= arrivalQueue.get(i).getArrivalTime()) {
//				System.out.println("tima added is  "+currentTime);
                calculateFactor(arrivalQueue.get(i));
                process.add(arrivalQueue.get(i));
                ++i;
            }

            if (isDead(currentProcess)) {
                if (processRunningTime != currentProcess.getQuantum()) {
                    int result = getMinimumFactorProcess(currentProcess);
                    if (result != -1) {
                        currentProcess = changeProcess(result, currentProcess);
                    } else
                        currentProcess = changeProcess(0, currentProcess);
                } else
                    currentProcess = changeProcess(0, currentProcess);
                processRunningTime = 0;
            } else if (processRunningTime >= Math.ceil(currentProcess.getQuantum() / 2.0)) {
                if (processRunningTime < currentProcess.getQuantum()) {
                    int result = getMinimumFactorProcess(currentProcess);
//					System.out.println("result is "+ result);
                    if (result != -1) {
                        changeProcessQuantum(currentProcess, processRunningTime);
                        currentProcess = changeProcess(result, currentProcess);
                        processRunningTime = 0;
                    }
                } else {
                    changeProcessQuantum(currentProcess, processRunningTime);
                    currentProcess = changeProcess(0, currentProcess);
                    processRunningTime = 0;

                }
            }

            secondPassed(currentProcess, ++processRunningTime);
        }
    }

    public void addChangedQantum(int unChangedQuantum, Process currentProcess) {
        String change = "( ";
        for (Process i : arrivalQueue) {
            if (i.getName().equals(currentProcess.getName())) {
                if (unChangedQuantum != 0)
                    change += unChangedQuantum + "+" + (currentProcess.getQuantum() - unChangedQuantum);
                else change += "0";
            } else change += i.getQuantum();
            change += " , ";
        }
        change = change.substring(0, change.length() - 2);
        change += " )";
        if (quantumInfo.size() > 0 && change.equals(quantumInfo.get(quantumInfo.size() - 1)))
            return;
        quantumInfo.add(change);

    }

    private void changeProcessQuantum(Process currentProcess, int processRunningTime) {
        int unChangedQuantum = currentProcess.getQuantum();
        if (currentProcess.getQuantum() == processRunningTime) {
            currentProcess.setQuantum((int) getMeanQuantum(currentProcess));
//			System.out.println("upper quantum is " +currentProcess.getQuantum());
        } else {
            int quantum = 2 * currentProcess.getQuantum();
            currentProcess.setQuantum(quantum - processRunningTime);
//			System.out.println("lower quantum is " +currentProcess.getQuantum());
        }
        addChangedQantum(unChangedQuantum, currentProcess);

    }

    private Process changeProcess(int result, Process toAdd) {
        Process currentProcess = new Process();
        if (!isDead(toAdd)) process.add(toAdd);
        if (process.size() > 0) {
            currentProcess = process.get(result);
            process.remove(result);
        } else currentProcess = toAdd;

//		System.out.println("current process "+currentProcess.getName());
//		System.out.println("current process "+currentProcess.getName());
        return currentProcess;
    }

    public void secondPassed(Process currentProcess, int processRunningTime) throws InterruptedException {
        if (!isDead(currentProcess)) {
            int burst = currentProcess.getBurstTime();
            currentProcess.setBurstTime(burst - 1);
            graph.add(currentProcess, currentTime);
        }
        ++currentTime;
        for (Process i : process) {
            i.increaseWaitingTimeBy(1);
            i.increaseTurnAroundTimeBy(1);
        }

    }

    private double getMeanQuantum(Process currentProcess) {
        double result = 0;
        for (Process i : process) {
            result += i.getQuantum();
        }
        result += currentProcess.getQuantum();
        result /= process.size() + 1;
        result = Math.ceil(result * 0.1) + currentProcess.getQuantum();
        return result;
    }


    private boolean isDead(Process currentProcess) {
        if (currentProcess.getBurstTime() <= 0) {
            currentProcess.setQuantum(0);
            addChangedQantum(0, currentProcess);
            if (excutionOrder.size() > 0 && excutionOrder.get(excutionOrder.size() - 1).equals(currentProcess.getName()))
                return true;
            excutionOrder.add(currentProcess.getName());
            graph.addTerminatedProcess(currentProcess);
            return true;
        }
        return false;
    }

    public int getMinimumFactorProcess(Process currentProcess) {
        int res = -1;
        for (int i = 0; i < process.size(); ++i) {
            if (process.get(i).getAG_Factor() < currentProcess.getAG_Factor()) {
                currentProcess = process.get(i);
                res = i;
            }
        }
        return res;
    }

    public void calculateFactor(Process process) {
        int arrival = process.getArrivalTime();
        int burst = process.getBurstTime();
        int priority = process.getPriority();

        process.setAG_Factor(arrival + burst + priority);
        process.setTurnAroundTime(burst);
    }

    public static void main(String[] args) throws InterruptedException {
//        Process Process = new Process("p1", Color.black, 0, 4, 14, 4);
//        Process Process2 = new Process("p2", Color.black, 3, 6, 19, 4);
//        Process Process3 = new Process("p3", Color.black, 4, 10, 13, 4);
//        Process Process4 = new Process("p4", Color.black, 7, 4, 17, 4);
//        AG tst = new AG();
//        tst.arrivalQueue.add(Process);
//        tst.arrivalQueue.add(Process2);
////		tst.arrivalQueue.add(Process3);
////		tst.arrivalQueue.add(Process4);
//        tst.start();
//        for (String change : tst.quantumInfo) {
//            System.out.println(change);
//        }
//        for (String order : tst.excutionOrder) {
//            System.out.println(order);
//        }
//        for (String order : tst.order) {
//            System.out.print(order + " ");
//        }
    }

}