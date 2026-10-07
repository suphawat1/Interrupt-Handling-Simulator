package com.interruptsimulator.view;
import com.interruptsimulator.controller.InterruptController;
import com.interruptsimulator.controller.SimulationController;
import com.interruptsimulator.controller.SimulationListener;
import com.interruptsimulator.core.CPU;
import com.interruptsimulator.model.Interrupt;
import com.interruptsimulator.model.PCB;
import com.interruptsimulator.model.Process;
import com.interruptsimulator.model.SimulationState;
import com.interruptsimulator.view.panel.InterruptIllustrationPanel;
import com.interruptsimulator.view.panel.InterruptQueuePanel;
import com.interruptsimulator.view.panel.StateDiagramPanel;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JProgressBar;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

public class SimulatorGUI implements SimulationListener {

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

    /*
     * ============================================================
     * VISUAL PANELS
     * ============================================================
     */

    private StateDiagramPanel stateDiagramPanel;

    private InterruptQueuePanel interruptQueuePanel;

    private InterruptIllustrationPanel interruptIllustrationPanel;


    /*
     * ============================================================
     * GRAPH VIEW SWITCH
     * ============================================================
     *
     * CardLayout ใช้สำหรับสลับระหว่าง
     *
     * 1. PROCESS STATE / INTERRUPT HANDLING
     * 2. INTERRUPT ILLUSTRATION
     */

    private JPanel graphContainer;

    private CardLayout graphCardLayout;

    private JButton stateDiagramButton;

    private JButton illustrationButton;


    /*
     * ============================================================
     * EVENT LOG
     * ============================================================
     */

    private JTextArea logArea;



    /*
     * ============================================================
     * CONTROLLER
     * ============================================================
     */

    private SimulationController controller;



    /*
     * ============================================================
     * CONSTRUCTOR
     * ============================================================
     */

    public SimulatorGUI() {

        controller =
                new SimulationController();

        controller.setListener(
                this
        );

        createGUI();

        updateDisplay();

        addLog("================================");
        addLog("Interrupt Handling Simulator");
        addLog("System initialized");
        addLog("Process P1 loaded into CPU");
        addLog("Simulation state: READY");
        addLog("================================");
    }


    /*
     * ============================================================
     * CREATE GUI
     * ============================================================
     */

    private void createGUI() {

        frame =
                new JFrame(
                        "Interrupt Handling Simulator"
                );

        frame.setSize(
                1200,
                800
        );

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setLocationRelativeTo(
                null
        );

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


    /*
     * ============================================================
     * TOP PANEL
     * ============================================================
     */

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


    /*
     * ============================================================
     * CPU / PROCESS PANEL
     * ============================================================
     */

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

        panel.add(
                processLabel
        );

        panel.add(
                stateLabel
        );

        panel.add(
                pcLabel
        );

        panel.add(
                registerLabel
        );

        panel.add(
                simulationStateLabel
        );

        panel.add(
                handlerLabel
        );

        return panel;
    }


    /*
     * ============================================================
     * INTERRUPT SYSTEM PANEL
     * ============================================================
     */

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
                        "Priority: "
                        + "1 = High, "
                        + "2 = Medium, "
                        + "3 = Low"
                );

        panel.add(
                queueLabel
        );

        panel.add(
                nextInterruptLabel
        );

        panel.add(
                pcbLabel
        );

        panel.add(
                vectorLabel
        );

        panel.add(
                priorityLabel
        );

        return panel;
    }


    /*
     * ============================================================
     * STATISTICS PANEL
     * ============================================================
     */

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

        panel.add(
                executionLabel
        );

        panel.add(
                saveLabel
        );

        panel.add(
                restoreLabel
        );

        panel.add(
                interruptCountLabel
        );

        panel.add(
                progressBar
        );

        return panel;
    }


    /*
     * ============================================================
     * CENTER PANEL
     * ============================================================
     */

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


        /*
         * ========================================================
         * PIPELINE
         * ========================================================
         */

        JPanel pipelinePanel =
                createPipelinePanel();

        centerPanel.add(
                pipelinePanel,
                BorderLayout.NORTH
        );


        /*
         * ========================================================
         * STATE DIAGRAM
         * ========================================================
         */

        stateDiagramPanel =
                new StateDiagramPanel();


        /*
         * ========================================================
         * INTERRUPT ILLUSTRATION
         * ========================================================
         */

        interruptIllustrationPanel =
                new InterruptIllustrationPanel();


        /*
         * ========================================================
         * GRAPH CONTAINER
         * ========================================================
         *
         * ใช้ CardLayout เพื่อสลับกราฟ
         */

        graphCardLayout =
                new CardLayout();

        graphContainer =
                new JPanel(
                        graphCardLayout
                );

        graphContainer.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        5,
                        5,
                        5
                )
        );


        graphContainer.add(
                stateDiagramPanel,
                "STATE_DIAGRAM"
        );

        graphContainer.add(
                interruptIllustrationPanel,
                "ILLUSTRATION"
        );


        /*
         * ========================================================
         * INTERRUPT QUEUE
         * ========================================================
         */

        interruptQueuePanel =
                new InterruptQueuePanel();


        /*
         * ========================================================
         * GRAPH + QUEUE
         * ========================================================
         */

        JPanel visualPanel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        visualPanel.add(
                graphContainer,
                BorderLayout.CENTER
        );

        visualPanel.add(
                interruptQueuePanel,
                BorderLayout.EAST
        );


        /*
         * ========================================================
         * GRAPH SWITCH BUTTONS
         * ========================================================
         */

        JPanel graphControlPanel =
                createGraphControlPanel();


        /*
         * ========================================================
         * EVENT TIMELINE
         * ========================================================
         */

        logArea =
                new JTextArea();

        logArea.setEditable(
                false
        );

        logArea.setFont(
                new Font(
                        Font.MONOSPACED,
                        Font.PLAIN,
                        13
                )
        );

        logArea.setLineWrap(
                true
        );

        logArea.setWrapStyleWord(
                true
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        logArea
                );

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "EVENT TIMELINE"
                )
        );


        /*
         * ========================================================
         * VISUAL AREA
         * ========================================================
         *
         * graphControlPanel อยู่เหนือกราฟ
         */

        JPanel visualArea =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                        )
                );

        visualArea.add(
                graphControlPanel,
                BorderLayout.NORTH
        );

        visualArea.add(
                visualPanel,
                BorderLayout.CENTER
        );


        /*
         * ========================================================
         * SPLIT PANE
         * ========================================================
         */

        JSplitPane splitPane =
                new JSplitPane(
                        JSplitPane.VERTICAL_SPLIT,
                        scrollPane,
                        visualArea
                );

        splitPane.setContinuousLayout(
                true
        );

        splitPane.setResizeWeight(
                0.45
        );

        splitPane.setDividerSize(
                6
        );

        splitPane.setOneTouchExpandable(
                true
        );


        centerPanel.add(
                splitPane,
                BorderLayout.CENTER
        );


        frame.add(
                centerPanel,
                BorderLayout.CENTER
        );
    }


    /*
     * ============================================================
     * GRAPH CONTROL PANEL
     * ============================================================
     */

    private JPanel createGraphControlPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                8,
                                5
                        )
                );

        panel.setBorder(
                BorderFactory.createTitledBorder(
                        "MONITOR VIEW"
                )
        );


        stateDiagramButton =
                new JButton(
                        "PROCESS STATE / INTERRUPT HANDLING"
                );


        illustrationButton =
                new JButton(
                        "INTERRUPT ILLUSTRATION"
                );


        /*
         * เริ่มต้นให้ดู State Diagram
         */

        stateDiagramButton.setEnabled(
                false
        );

        illustrationButton.setEnabled(
                true
        );


        /*
         * ========================================================
         * STATE DIAGRAM BUTTON
         * ========================================================
         */

        stateDiagramButton.addActionListener(
                e -> showStateDiagram()
        );


        /*
         * ========================================================
         * ILLUSTRATION BUTTON
         * ========================================================
         */

        illustrationButton.addActionListener(
                e -> showIllustration()
        );


        panel.add(
                stateDiagramButton
        );

        panel.add(
                illustrationButton
        );


        return panel;
    }


    /*
     * ============================================================
     * SHOW STATE DIAGRAM
     * ============================================================
     */

    private void showStateDiagram() {

        if (graphCardLayout == null) {
            return;
        }

        graphCardLayout.show(
                graphContainer,
                "STATE_DIAGRAM"
        );

        stateDiagramButton.setEnabled(
                false
        );

        illustrationButton.setEnabled(
                true
        );

        addLog(
                "Monitor view: "
                + "Process State / Interrupt Handling"
        );
    }


    /*
     * ============================================================
     * SHOW INTERRUPT ILLUSTRATION
     * ============================================================
     */

    private void showIllustration() {

        if (graphCardLayout == null) {
            return;
        }

        graphCardLayout.show(
                graphContainer,
                "ILLUSTRATION"
        );

        stateDiagramButton.setEnabled(
                true
        );

        illustrationButton.setEnabled(
                false
        );

        addLog(
                "Monitor view: "
                + "Interrupt Illustration"
        );
    }


    /*
     * ============================================================
     * PIPELINE
     * ============================================================
     */

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

            label.setOpaque(
                    true
            );

            label.setBackground(
                    Color.LIGHT_GRAY
            );

            label.setBorder(
                    BorderFactory.createLineBorder(
                            Color.GRAY
                    )
            );

            panel.add(
                    label
            );
        }

        return panel;
    }


    /*
     * ============================================================
     * BOTTOM BUTTON PANEL
     * ============================================================
     */

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


        JButton autoButton =
                new JButton(
                        "Auto Simulation"
                );


        JButton resetButton =
                new JButton(
                        "Reset"
                );


        bottomPanel.add(
                executeButton
        );

        bottomPanel.add(
                timerButton
        );

        bottomPanel.add(
                keyboardButton
        );

        bottomPanel.add(
                diskButton
        );

        bottomPanel.add(
                networkButton
        );

        bottomPanel.add(
                handleButton
        );

        bottomPanel.add(
                pauseButton
        );

        bottomPanel.add(
                autoButton
        );

        bottomPanel.add(
                resetButton
        );


        frame.add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        /*
         * ========================================================
         * BUTTON ACTIONS
         * ========================================================
         */

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



    /*
     * ============================================================
     * ACTIONS -> SimulationController
     * ============================================================
     */

    private void executeProcess() {

        controller.executeProcess();
    }

    private void generateInterrupt(
            String type,
            int priority) {

        controller.generateInterrupt(
                type,
                priority
        );
    }

    private void startInterruptHandling() {

        controller.startInterruptHandling();
    }

    private void togglePause() {

        controller.togglePause();
    }

    private void autoSimulation() {

        controller.autoSimulation();
    }

    private void resetSimulator() {

        controller.reset();
    }


    /*
     * ============================================================
     * SimulationListener (Controller -> View)
     * ============================================================
     */

    @Override
    public void onLog(String message) {

        addLog(message);
    }

    @Override
    public void onStateChanged() {

        updateDisplay();
    }

    @Override
    public void onReset() {

        if (logArea != null) {

            logArea.setText("");
        }


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


        /*
         * Reset Interrupt Illustration
         */

        if (interruptIllustrationPanel != null) {

            interruptIllustrationPanel.resetIllustration();
        }


        /*
         * กลับไปดู State Diagram เป็นค่าเริ่มต้น
         */

        if (graphCardLayout != null) {

            graphCardLayout.show(
                    graphContainer,
                    "STATE_DIAGRAM"
            );
        }


        if (stateDiagramButton != null) {

            stateDiagramButton.setEnabled(
                    false
            );
        }


        if (illustrationButton != null) {

            illustrationButton.setEnabled(
                    true
            );
        }


        updateDisplay();
    }



    /*
     * ============================================================
     * UPDATE DISPLAY
     * ============================================================
     *
     * จุดสำคัญ:
     *
     * SimulationState มีเพียงตัวเดียว
     *
     * StateDiagramPanel
     * และ
     * InterruptIllustrationPanel
     *
     * จะได้รับ state เดียวกันตรงนี้
     *
     * ดังนั้นกราฟไม่ควรสร้าง state เอง
     * และไม่ควรมี timeline แยกจาก simulator
     */

    private void updateDisplay() {

        SimulationState simulationState =
                controller.getSimulationState();
        Process process =
                controller.getProcess();
        CPU cpu =
                controller.getCpu();
        InterruptController interruptController =
                controller.getInterruptController();
        Interrupt currentInterrupt =
                controller.getCurrentInterrupt();
        PCB currentPCB =
                controller.getCurrentPCB();
        int interruptCount =
                controller.getInterruptCount();
        int animationStep =
                controller.getAnimationStep();

        /*
         * ========================================================
         * PROCESS STATE DIAGRAM
         * ========================================================
         */

        if (stateDiagramPanel != null) {

            stateDiagramPanel.setState(
                    simulationState.toString()
            );
        }


        /*
         * ========================================================
         * INTERRUPT ILLUSTRATION
         * ========================================================
         *
         * ส่ง SimulationState จริงของ Simulator
         */

        if (interruptIllustrationPanel != null) {

            interruptIllustrationPanel.setSimulationState(
                    simulationState.name()
            );


            /*
             * ส่ง Interrupt ปัจจุบัน
             *
             * ถ้ามี
             */

            interruptIllustrationPanel.setInterrupt(
                    currentInterrupt
            );
        }


        /*
         * ========================================================
         * INTERRUPT QUEUE
         * ========================================================
         */

        if (interruptQueuePanel != null) {

            interruptQueuePanel.updateQueue(
                    interruptController
                            .getInterruptQueue()
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


        /*
         * ========================================================
         * SIMULATION STATE
         * ========================================================
         */

        simulationStateLabel.setText(
                "Simulation State: "
                + simulationState
        );


        /*
         * ========================================================
         * CURRENT ISR
         * ========================================================
         */

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
         *
         * มี 8 ช่วงหลัก
         *
         * 0 = READY
         * 1 = INTERRUPT RECEIVED
         * 2 = SAVE
         * 3 = INTERRUPTED
         * 4 = LOOKUP
         * 5 = ISR
         * 6 = RESTORE
         * 7 = RESUMED
         * 8 = RUNNING
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


    /*
     * ============================================================
     * ADD EVENT LOG
     * ============================================================
     */

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
                logArea
                        .getDocument()
                        .getLength()
        );
    }


    /*
     * ============================================================
     * SHOW GUI
     * ============================================================
     */

    public void show() {

        frame.setVisible(
                true
        );
    }
}