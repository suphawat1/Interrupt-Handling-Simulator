package com.interruptsimulator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class SimulatorGUI {

    private JFrame frame;

    private JLabel processLabel;
    private JLabel stateLabel;
    private JLabel pcLabel;
    private JLabel registerLabel;

    private JLabel queueLabel;
    private JLabel nextInterruptLabel;

    private JLabel simulationStateLabel;
    private JLabel handlerLabel;

    private JLabel pcbLabel;

    private JLabel executionLabel;
    private JLabel saveLabel;
    private JLabel restoreLabel;
    private JLabel interruptCountLabel;

    private JProgressBar progressBar;

    private StateDiagramPanel stateDiagramPanel;
    private InterruptQueuePanel interruptQueuePanel;

    private JTextArea logArea;

    private CPU cpu;
    private Process process;
    private InterruptController interruptController;

    private SimulationState simulationState;

    private Timer animationTimer;

    private int animationStep;
    private int interruptId;

    private Interrupt currentInterrupt;
    private PCB currentPCB;

    private boolean autoMode = false;

    private int interruptCount;


    /*
     * ============================================================
     * TIMING
     * ============================================================
     *
     * ทุก STEP จะค้างตามเวลาที่กำหนด
     * เพื่อให้ Monitor และ EVENT TIMELINE
     * แสดงผลได้ทันกัน
     */

    private static final int NORMAL_STEP_DELAY = 2600;

    private static final int ISR_STEP_DELAY = 3200;

    private static final int RESUME_STEP_DELAY = 2800;


    public SimulatorGUI() {

        initializeSystem();

        createGUI();

        updateDisplay();

        addLog("================================");
        addLog("Interrupt Handling Simulator");
        addLog("System initialized");
        addLog("Process P1 loaded into CPU");
        addLog("Simulation state: READY");
        addLog("================================");
    }


    private void initializeSystem() {

        cpu = new CPU();

        process = new Process(
                1,
                "P1"
        );

        interruptController =
                new InterruptController();

        cpu.loadProcess(process);

        simulationState =
                SimulationState.READY;

        interruptId = 1;

        interruptCount = 0;

        animationStep = 0;

        currentInterrupt = null;

        currentPCB = null;

        autoMode = false;
    }


    private void createGUI() {

        frame = new JFrame(
                "Interrupt Handling Simulator"
        );

        frame.setSize(
                1200,
                800
        );

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(null);

        frame.setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        createTopPanel();

        createCenterPanel();

        createBottomPanel();
    }


    private void createTopPanel() {

        JPanel topPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                3,
                                10,
                                10
                        )
                );

        topPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        0,
                        10
                )
        );

        topPanel.add(
                createCPUPanel()
        );

        topPanel.add(
                createInterruptPanel()
        );

        topPanel.add(
                createStatisticsPanel()
        );

        frame.add(
                topPanel,
                BorderLayout.NORTH
        );
    }


    private JPanel createCPUPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                6,
                                1
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "CPU / PROCESS"
                )
        );

        processLabel =
                new JLabel();

        stateLabel =
                new JLabel();

        pcLabel =
                new JLabel();

        registerLabel =
                new JLabel();

        simulationStateLabel =
                new JLabel();

        handlerLabel =
                new JLabel();

        panel.add(processLabel);
        panel.add(stateLabel);
        panel.add(pcLabel);
        panel.add(registerLabel);
        panel.add(simulationStateLabel);
        panel.add(handlerLabel);

        return panel;
    }


    private JPanel createInterruptPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                5,
                                1
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "INTERRUPT SYSTEM"
                )
        );

        queueLabel =
                new JLabel();

        nextInterruptLabel =
                new JLabel();

        pcbLabel =
                new JLabel();

        JLabel vectorLabel =
                new JLabel(
                        "Vector Table: "
                        + "Timer | Keyboard | Disk | Network"
                );

        JLabel priorityLabel =
                new JLabel(
                        "Priority: 1 = High, 2 = Medium, 3 = Low"
                );

        panel.add(queueLabel);
        panel.add(nextInterruptLabel);
        panel.add(pcbLabel);
        panel.add(vectorLabel);
        panel.add(priorityLabel);

        return panel;
    }


    private JPanel createStatisticsPanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                5,
                                1
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "STATISTICS"
                )
        );

        executionLabel =
                new JLabel();

        saveLabel =
                new JLabel();

        restoreLabel =
                new JLabel();

        interruptCountLabel =
                new JLabel();

        progressBar =
                new JProgressBar(
                        0,
                        100
                );

        progressBar.setStringPainted(
                true
        );

        panel.add(executionLabel);
        panel.add(saveLabel);
        panel.add(restoreLabel);
        panel.add(interruptCountLabel);
        panel.add(progressBar);

        return panel;
    }


    private void createCenterPanel() {

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        10,
                        0,
                        10
                )
        );


        JPanel pipelinePanel =
                createPipelinePanel();

        centerPanel.add(
                pipelinePanel,
                BorderLayout.NORTH
        );


        stateDiagramPanel =
                new StateDiagramPanel();


        interruptQueuePanel =
                new InterruptQueuePanel();


        JPanel visualPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );


        visualPanel.add(
                stateDiagramPanel,
                BorderLayout.CENTER
        );


        visualPanel.add(
                interruptQueuePanel,
                BorderLayout.EAST
        );


        logArea =
                new JTextArea();

        logArea.setEditable(false);

        logArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        13
                )
        );

        logArea.setLineWrap(true);

        logArea.setWrapStyleWord(true);


        JScrollPane scrollPane =
                new JScrollPane(
                        logArea
                );

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "EVENT TIMELINE"
                )
        );


        JSplitPane splitPane =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        scrollPane,
                        visualPanel
                );

        splitPane.setContinuousLayout(true);

        splitPane.setResizeWeight(0.55);

        splitPane.setDividerSize(6);

        splitPane.setOneTouchExpandable(true);


        centerPanel.add(
                splitPane,
                BorderLayout.CENTER
        );


        frame.add(
                centerPanel,
                BorderLayout.CENTER
        );
    }


    private JPanel createPipelinePanel() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                1,
                                8,
                                5,
                                5
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "INTERRUPT HANDLING PIPELINE"
                )
        );

        String[] steps = {
                "RUNNING",
                "INTERRUPT",
                "SAVE",
                "INTERRUPTED",
                "LOOKUP",
                "ISR",
                "RESTORE",
                "RESUMED"
        };

        for (String step : steps) {

            JLabel label =
                    new JLabel(
                            step,
                            JLabel.CENTER
                    );

            label.setOpaque(true);

            label.setBackground(
                    Color.LIGHT_GRAY
            );

            label.setBorder(
                    BorderFactory.createLineBorder(
                            Color.GRAY
                    )
            );

            panel.add(label);
        }

        return panel;
    }


    private void createBottomPanel() {

        JPanel bottomPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                8,
                                8
                        )
                );

        JButton executeButton =
                new JButton(
                        "Execute Process"
                );

        JButton timerButton =
                new JButton(
                        "Timer Interrupt"
                );

        JButton keyboardButton =
                new JButton(
                        "Keyboard Interrupt"
                );

        JButton diskButton =
                new JButton(
                        "Disk Interrupt"
                );

        JButton networkButton =
                new JButton(
                        "Network Interrupt"
                );

        JButton handleButton =
                new JButton(
                        "Handle Interrupt"
                );

        JButton pauseButton =
                new JButton(
                        "Pause"
                );

        JButton resetButton =
                new JButton(
                        "Reset"
                );

        JButton autoButton =
                new JButton(
                        "Auto Simulation"
                );


        bottomPanel.add(executeButton);
        bottomPanel.add(timerButton);
        bottomPanel.add(keyboardButton);
        bottomPanel.add(diskButton);
        bottomPanel.add(networkButton);
        bottomPanel.add(handleButton);
        bottomPanel.add(pauseButton);
        bottomPanel.add(autoButton);
        bottomPanel.add(resetButton);


        frame.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        executeButton.addActionListener(
                e -> executeProcess()
        );

        timerButton.addActionListener(
                e -> generateInterrupt(
                        "Timer",
                        1
                )
        );

        keyboardButton.addActionListener(
                e -> generateInterrupt(
                        "Keyboard",
                        2
                )
        );

        diskButton.addActionListener(
                e -> generateInterrupt(
                        "Disk",
                        1
                )
        );

        networkButton.addActionListener(
                e -> generateInterrupt(
                        "Network",
                        3
                )
        );

        handleButton.addActionListener(
                e -> startInterruptHandling()
        );

        pauseButton.addActionListener(
                e -> togglePause()
        );

        autoButton.addActionListener(
                e -> autoSimulation()
        );

        resetButton.addActionListener(
                e -> resetSimulator()
        );
    }


    private void executeProcess() {

        if (animationTimer != null
                && animationTimer.isRunning()) {

            addLog(
                    "Cannot execute while animation is running"
            );

            return;
        }


        if (!process.getState().equals("RUNNING")) {

            addLog(
                    "Cannot execute process. "
                    + "Current state: "
                    + process.getState()
            );

            return;
        }


        simulationState =
                SimulationState.RUNNING;

        cpu.execute();

        updateDisplay();

        addLog(
                "CPU executed Process P1"
        );

        addLog(
                "PC = "
                + process.getProgramCounter()
        );
    }


    private void generateInterrupt(
            String type,
            int priority) {

        if (animationTimer != null
                && animationTimer.isRunning()) {

            addLog(
                    "Cannot generate interrupt "
                    + "during animation"
            );

            return;
        }


        Interrupt interrupt =
                new Interrupt(
                        interruptId++,
                        type,
                        priority
                );

        interruptController.receiveInterrupt(
                interrupt
        );

        interruptCount++;

        simulationState =
                SimulationState.INTERRUPT_RECEIVED;

        updateDisplay();


        addLog(
                "--------------------------------"
        );

        addLog(
                "Interrupt received"
        );

        addLog(
                "Type: "
                + type
                + " | Priority: "
                + priority
                + " | ID: "
                + interrupt.getInterruptId()
        );

        addLog(
                "Added to interrupt queue"
        );
    }


    private void startInterruptHandling() {

        if (!interruptController.hasInterrupt()) {

            addLog(
                    "No interrupt in queue"
            );

            return;
        }


        if (animationTimer != null
                && animationTimer.isRunning()) {

            addLog(
                    "Interrupt handling already running"
            );

            return;
        }


        animationStep = 1;

        currentInterrupt = null;

        currentPCB = null;


        addLog(
                "================================"
        );

        addLog(
                "Starting interrupt handling"
        );


        /*
         * ใช้ Timer เดียวเป็นตัวควบคุม Timeline
         *
         * แต่ละ STEP จะถูกเปลี่ยน state
         * และค้างไว้นานพอให้ Monitor แสดงผล
         */
        animationTimer =
                new Timer(
                        NORMAL_STEP_DELAY,
                        e -> processAnimation()
                );

        animationTimer.start();
    }


    private void processAnimation() {

        switch (animationStep) {

            /*
             * ====================================================
             * STEP 1
             * INTERRUPT RECEIVED
             * ====================================================
             */
            case 1:

                simulationState =
                        SimulationState.INTERRUPT_RECEIVED;

                updateDisplay();

                addLog(
                        "STEP 1: Interrupt received"
                );

                Interrupt pendingInterrupt =
                        interruptController.peekNextInterrupt();

                if (pendingInterrupt != null) {

                    addLog(
                            "Interrupt: "
                            + pendingInterrupt.getType()
                    );
                }

                animationTimer.setDelay(
                        NORMAL_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 2
             * SAVE CONTEXT
             * ====================================================
             */
            case 2:

                simulationState =
                        SimulationState.SAVING_CONTEXT;

                updateDisplay();

                addLog(
                        "STEP 2: Saving CPU context"
                );

                currentPCB =
                        cpu.saveContext();

                if (currentPCB != null) {

                    addLog(
                            "Context saved to PCB:"
                    );

                    addLog(
                            currentPCB.toString()
                    );
                }

                animationTimer.setDelay(
                        NORMAL_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 3
             * INTERRUPTED
             * ====================================================
             */
            case 3:

                simulationState =
                        SimulationState.INTERRUPTED;

                process.setState(
                        "INTERRUPTED"
                );

                updateDisplay();

                addLog(
                        "STEP 3: Process interrupted"
                );

                addLog(
                        "Process P1 state: "
                        + "RUNNING -> INTERRUPTED"
                );

                animationTimer.setDelay(
                        NORMAL_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 4
             * LOOKUP HANDLER
             * ====================================================
             */
            case 4:

                simulationState =
                        SimulationState.LOOKUP_HANDLER;

                currentInterrupt =
                        interruptController
                                .getNextInterrupt();

                updateDisplay();

                addLog(
                        "STEP 4: Looking up "
                        + "Interrupt Vector Table"
                );

                if (currentInterrupt != null) {

                    addLog(
                            "Interrupt: "
                            + currentInterrupt.getType()
                    );

                    if (interruptController
                            .getVectorTable()
                            .contains(
                                    currentInterrupt.getType()
                            )) {

                        addLog(
                                "ISR found: "
                                + currentInterrupt.getType()
                                + " Interrupt Service Routine"
                        );
                    }
                }

                animationTimer.setDelay(
                        NORMAL_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 5
             * ISR
             * ====================================================
             */
            case 5:

                simulationState =
                        SimulationState.ISR_EXECUTING;

                updateDisplay();

                addLog(
                        "STEP 5: Executing ISR"
                );

                if (currentInterrupt != null) {

                    String result =
                            interruptController
                                    .handleInterrupt(
                                            currentInterrupt
                                    );

                    addLog(result);
                }

                /*
                 * ISR ให้เวลานานกว่า step ปกติ
                 * เพื่อให้เห็นว่ากำลังทำงานจริง
                 */
                animationTimer.setDelay(
                        ISR_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 6
             * RESTORE CONTEXT
             * ====================================================
             */
            case 6:

                simulationState =
                        SimulationState.RESTORING_CONTEXT;

                updateDisplay();

                addLog(
                        "STEP 6: Restoring CPU context"
                );

                if (currentPCB != null) {

                    addLog(
                            "Restoring context from PCB:"
                    );

                    addLog(
                            "PID="
                            + currentPCB.getProcessId()
                            + ", PC="
                            + currentPCB.getProgramCounter()
                            + ", A="
                            + currentPCB.getRegisterA()
                            + ", B="
                            + currentPCB.getRegisterB()
                    );
                }

                cpu.restoreContext();

                animationTimer.setDelay(
                        NORMAL_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 7
             * RESUMED
             * ====================================================
             */
            case 7:

                simulationState =
                        SimulationState.RESUMED;

                process.setState(
                        "RUNNING"
                );

                updateDisplay();

                addLog(
                        "STEP 7: Process P1 resumed"
                );

                addLog(
                        "Process P1 state: "
                        + "INTERRUPTED -> RUNNING"
                );

                addLog(
                        "PC restored to "
                        + process.getProgramCounter()
                );

                /*
                 * ค้างไว้ให้เห็นเส้น
                 * INTERRUPTED -> RUNNING
                 */
                animationTimer.setDelay(
                        RESUME_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 8
             * RUNNING
             * ====================================================
             */
            case 8:

                simulationState =
                        SimulationState.RUNNING;

                process.setState(
                        "RUNNING"
                );

                updateDisplay();

                addLog(
                        "Process P1 is RUNNING again"
                );

                animationTimer.setDelay(
                        NORMAL_STEP_DELAY
                );

                break;


            /*
             * ====================================================
             * STEP 9
             * COMPLETE
             * ====================================================
             */
            case 9:

                if (!autoMode) {

                    simulationState =
                            SimulationState.RUNNING;

                    process.setState(
                            "RUNNING"
                    );

                    updateDisplay();

                    addLog(
                            "Interrupt handling completed"
                    );

                    addLog(
                            "Process P1 is RUNNING..."
                    );

                    animationTimer.stop();

                    animationTimer = null;

                    currentInterrupt = null;

                    currentPCB = null;
                }

                else if (interruptController.hasInterrupt()) {

                    addLog(
                            "Next interrupt in queue..."
                    );

                    currentInterrupt = null;

                    currentPCB = null;

                    animationStep = 0;

                }

                else {

                    simulationState =
                            SimulationState.RUNNING;

                    process.setState(
                            "RUNNING"
                    );

                    updateDisplay();

                    addLog(
                            "Interrupt handling completed"
                    );

                    addLog(
                            "Process P1 is RUNNING"
                    );

                    addLog(
                            "================================"
                    );

                    addLog(
                            "AUTO SIMULATION COMPLETED"
                    );

                    addLog(
                            "================================"
                    );

                    animationTimer.stop();

                    animationTimer = null;

                    currentInterrupt = null;

                    currentPCB = null;

                    autoMode = false;
                }

                break;


            default:

                break;
        }


        /*
         * ========================================================
         * NEXT STEP
         * ========================================================
         */

        if (animationTimer != null
                && animationStep < 9) {

            animationStep++;
        }


        /*
         * Auto mode:
         *
         * หลังจาก STEP 9 ถ้ายังมี interrupt
         * จะกลับไป STEP 1
         */
        if (animationTimer != null
                && autoMode
                && animationStep == 9
                && interruptController.hasInterrupt()) {

            /*
             * ไม่เพิ่มตรงนี้
             * เพราะ case 9 จะจัดการรอบถัดไป
             */
        }
    }


    private void togglePause() {

        if (animationTimer == null) {

            addLog(
                    "No animation is running"
            );

            return;
        }


        if (animationTimer.isRunning()) {

            animationTimer.stop();

            addLog(
                    "Simulation paused"
            );

        } else {

            animationTimer.start();

            addLog(
                    "Simulation resumed"
            );
        }
    }


    private void autoSimulation() {

        if (animationTimer != null
                && animationTimer.isRunning()) {

            addLog(
                    "Simulation already running"
            );

            return;
        }


        if (!interruptController.hasInterrupt()) {

            addLog(
                    "================================"
            );

            addLog(
                    "AUTO SIMULATION STARTED"
            );


            /*
             * เพิ่ม Interrupt เข้า Queue
             *
             * generateInterrupt() จะไม่ start
             * animation เพราะ animationTimer ยัง null
             */
            generateInterrupt(
                    "Timer",
                    1
            );

            generateInterrupt(
                    "Keyboard",
                    2
            );

            generateInterrupt(
                    "Disk",
                    1
            );

            generateInterrupt(
                    "Network",
                    3
            );

        } else {

            addLog(
                    "================================"
            );

            addLog(
                    "AUTO SIMULATION STARTED"
            );

            addLog(
                    "Using existing interrupt queue"
            );
        }


        autoMode = true;

        startInterruptHandling();
    }


    private void resetSimulator() {

        if (animationTimer != null) {

            animationTimer.stop();

            animationTimer = null;
        }

        initializeSystem();

        logArea.setText("");


        addLog(
                "================================"
        );

        addLog(
                "Simulator reset"
        );

        addLog(
                "Process P1 loaded"
        );

        addLog(
                "CPU ready"
        );

        addLog(
                "Interrupt queue empty"
        );

        addLog(
                "================================"
        );


        updateDisplay();
    }


    private void updateDisplay() {

        /*
         * ========================================================
         * STATE DIAGRAM
         * ========================================================
         */

        if (stateDiagramPanel != null) {

            stateDiagramPanel.setState(
                    simulationState.toString()
            );
        }


        /*
         * ========================================================
         * INTERRUPT QUEUE
         * ========================================================
         */

        if (interruptQueuePanel != null) {

            interruptQueuePanel.updateQueue(
                    interruptController.getInterruptQueue()
            );
        }


        /*
         * ========================================================
         * PROCESS
         * ========================================================
         */

        processLabel.setText(
                "Process: "
                + process.getProcessName()
                + " (PID "
                + process.getProcessId()
                + ")"
        );

        stateLabel.setText(
                "Process State: "
                + process.getState()
        );

        pcLabel.setText(
                "Program Counter: "
                + process.getProgramCounter()
        );

        registerLabel.setText(
                "Registers: A = "
                + process.getRegisterA()
                + " | B = "
                + process.getRegisterB()
        );


        /*
         * ========================================================
         * INTERRUPT
         * ========================================================
         */

        queueLabel.setText(
                "Interrupt Queue: "
                + interruptController.getQueueSize()
                + " interrupt(s)"
        );


        Interrupt next =
                interruptController
                        .peekNextInterrupt();


        if (next == null) {

            nextInterruptLabel.setText(
                    "Next Interrupt: None"
            );

        } else {

            nextInterruptLabel.setText(
                    "Next Interrupt: "
                    + next.getType()
                    + " | Priority "
                    + next.getPriority()
            );
        }


        simulationStateLabel.setText(
                "Simulation State: "
                + simulationState
        );


        if (currentInterrupt == null) {

            handlerLabel.setText(
                    "Current ISR: None"
            );

        } else {

            handlerLabel.setText(
                    "Current ISR: "
                    + currentInterrupt.getType()
            );
        }


        /*
         * ========================================================
         * PCB
         * ========================================================
         */

        if (currentPCB == null) {

            pcbLabel.setText(
                    "Saved PCB: None"
            );

        } else {

            pcbLabel.setText(
                    "Saved PCB: "
                    + currentPCB
            );
        }


        /*
         * ========================================================
         * STATISTICS
         * ========================================================
         */

        executionLabel.setText(
                "CPU Executions: "
                + cpu.getTotalExecutions()
        );

        saveLabel.setText(
                "Context Saves: "
                + cpu.getContextSaveCount()
        );

        restoreLabel.setText(
                "Context Restores: "
                + cpu.getContextRestoreCount()
        );

        interruptCountLabel.setText(
                "Interrupts Received: "
                + interruptCount
        );


        /*
         * ========================================================
         * PROGRESS
         * ========================================================
         */

        int progress =
                (animationStep * 100) / 8;


        if (progress > 100) {

            progress = 100;
        }


        if (progress < 0) {

            progress = 0;
        }


        progressBar.setValue(
                progress
        );

        progressBar.setString(
                simulationState.toString()
        );
    }


    private void addLog(
            String message) {

        if (logArea == null) {

            return;
        }


        logArea.append(
                message
                + "\n"
        );


        logArea.setCaretPosition(
                logArea.getDocument().getLength()
        );
    }


    public void show() {

        frame.setVisible(true);
    }
}