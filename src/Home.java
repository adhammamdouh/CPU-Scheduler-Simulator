import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Home extends Thread {
    private JPanel pnlMain;
    private JTable queueTbl;
    private JButton simulateButton;
    private JButton endSimulationButton;
    private JTextField nameText;
    private JSpinner arrivalSpinner;
    private JSpinner burstSpinner;
    private JSpinner prioritySpinner;
    private JSpinner quantumSpinner;
    private JButton addToQueueButton;
    private JComboBox<String> ScheduleType;
    private JTextField colorField;
    private JCheckBox colorBox;
    private JLabel colorLabel;

    private int id = 0;

    ArrayList<Process> queue = new ArrayList<>();

    Home() {


    }

    @Override
    public void run() {
        JFrame form = new JFrame("CPU Scheduler");
        form.setResizable(false);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(pnlMain);
        form.pack();
        form.setVisible(true);

        form.setSize(600, 500);
        DefaultTableModel modelTable = new DefaultTableModel(null, new String[]{"ID", "Name", "Arrival Time", "Burst Time", "Priority", "Quantum", "AG Factor"});
        queueTbl.setModel(modelTable);

        SpinnerNumberModel modelSpinner;

        modelSpinner = (SpinnerNumberModel) arrivalSpinner.getModel();
        modelSpinner.setMinimum(0);
        modelSpinner.setValue(0);

        modelSpinner = (SpinnerNumberModel) burstSpinner.getModel();
        modelSpinner.setMinimum(1);
        modelSpinner.setValue(1);

        modelSpinner = (SpinnerNumberModel) prioritySpinner.getModel();
        modelSpinner.setMinimum(0);
        modelSpinner.setValue(0);

        modelSpinner = (SpinnerNumberModel) quantumSpinner.getModel();
        modelSpinner.setMinimum(1);
        modelSpinner.setValue(1);

        ScheduleType.addItem("Shortest Job first");
        ScheduleType.addItem("Shortest Remaining Time first");
        ScheduleType.addItem("Priority Scheduler");
        ScheduleType.addItem("AG Factor Scheduler");
        ScheduleType.setSelectedIndex(0);

        addToQueueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addToQueue();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        simulateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<Process> copyQueue = getListCopy(queue);
                            startSimulation(copyQueue);
                            System.out.println(queue);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                t.start();
            }
        });
        colorField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                String black = "000000";
                colorBox.setBackground(Color.decode('#' + colorField.getText() + black.substring(colorField.getText().length())));
            }
        });
    }

    void resetVariables() {
        nameText.setText("");
        burstSpinner.setValue(1);
        arrivalSpinner.setValue(0);
        quantumSpinner.setValue(1);
        prioritySpinner.setValue(0);
        colorField.setText("");
        colorBox.setBackground(Color.WHITE);
    }

    void addToQueue() throws Exception {
        String name = nameText.getText();
        if (name == null || name.isEmpty()) throw new Exception("Name cannot be empty.");

        int burst = (int) burstSpinner.getValue();
        int arrival = (int) arrivalSpinner.getValue();
        int priority = (int) prioritySpinner.getValue();
        int quantum = (int) quantumSpinner.getValue();
        Color color = Color.decode('#' + colorField.getText());

        Process CurrentProcess = new Process(id++, name, arrival, burst, priority, quantum, color);
        queue.add(CurrentProcess);

        DefaultTableModel queueTable = (DefaultTableModel) queueTbl.getModel();
        queueTable.addRow(new Object[]{CurrentProcess.getId()
                , CurrentProcess.getName()
                , CurrentProcess.getArrivalTime()
                , CurrentProcess.getBurstTime()
                , CurrentProcess.getPriority()
                , CurrentProcess.getQuantum()
                , CurrentProcess.getAG_Factor()});

        resetVariables();
    }

    void startSimulation(ArrayList<Process> queue) throws Exception {
        if (queue == null || queue.isEmpty()) throw new Exception("Queue cannot be empty.");
        System.out.println("Size : " + queue.size());
        int type = ScheduleType.getSelectedIndex();

        switch (type) {
            case 0:
                SJF sjf = new SJF();
                sjf.start(queue);
                break;
            case 1:
                SRTF srtf = new SRTF();
                srtf.start(queue, 1);
                break;
            case 2:
                PriorityScheduling ps = new PriorityScheduling();
                ps.start(queue);
                break;
            case 3:
                AG ag = new AG();
                ag.start(queue);
                break;
        }

    }

    private synchronized ArrayList<Process> getListCopy(final ArrayList<Process> queue) {
        ArrayList<Process> copy = new ArrayList<>();
        for (int i = 0; i < queue.size(); ++i) {
            Process p = queue.get(i);
            copy.add(new Process(p.getId()
                    , p.getName()
                    , p.getArrivalTime()
                    , p.getBurstTime()
                    , p.getPriority()
                    , p.getQuantum()
                    , p.getColor()));
        }

        return copy;
    }

    public static void main(String[] args) {
        Home h = new Home();
        h.run();
    }
}
