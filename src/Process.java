import java.awt.*;

public class Process {
    private int id;
    private String name;
    private Color color;
    private int arrivalTime;
    private int burstTime;
    private int waitingTime;
    private int turnAroundTime;
    private int priority;
    private int finishTime;
    private int constBurstTime;
    private int quantum;
    private int AG_Factor;


    public Process() {

    }

    public Process(int id, String name, int arrivalTime, int burstTime, int priority, int quantum, Color color) {
        this.id = id;
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantum = quantum;
        this.color = color;
        this.constBurstTime = burstTime;
        this.AG_Factor = priority + burstTime + arrivalTime;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public String getName() {
        return name;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnAroundTime() {
        return turnAroundTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public int getConstBurstTime() {
        return constBurstTime;
    }

    public int getQuantum() {
        return quantum;
    }

    public int getAG_Factor() {
        return AG_Factor;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = (burstTime <= 0) ? 0 : burstTime;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = (waitingTime <= 0) ? 0 : waitingTime;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnAroundTime = turnAroundTime;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public void setConstBurstTime(int constBurstTime) {
        this.constBurstTime = constBurstTime;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void setAG_Factor(int AG_Factor) {
        this.AG_Factor = AG_Factor;
    }

    public void increaseWaitingTimeBy(int val) {
        this.setWaitingTime(val + this.getWaitingTime());
    }

    public void increaseTurnAroundTimeBy(int val) {
        this.setTurnAroundTime(val + this.getTurnAroundTime());
    }

    @Override
    public String toString() {
        return "Process{" +
                "name='" + name + '\'' +
                ", color=" + color +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", waitingTime=" + waitingTime +
                ", TurnaroundTime=" + turnAroundTime +
                ", finishTime=" + finishTime +
                '}';
    }
}