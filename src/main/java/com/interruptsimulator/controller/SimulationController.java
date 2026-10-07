package com.interruptsimulator.controller;

import com.interruptsimulator.model.ProcessState;
import com.interruptsimulator.core.CPU;
import com.interruptsimulator.model.Interrupt;
import com.interruptsimulator.model.PCB;
import com.interruptsimulator.model.Process;
import com.interruptsimulator.model.SimulationState;
import com.interruptsimulator.util.Constants;


import javax.swing.Timer;

import static com.interruptsimulator.util.Constants.ISR_STEP_DELAY;
import static com.interruptsimulator.util.Constants.NORMAL_STEP_DELAY;
import static com.interruptsimulator.util.Constants.RESUME_STEP_DELAY;

/**
 * ควบคุม timeline ของการจำลอง (state machine STEP 1-9)
 * ไม่มี code ที่เกี่ยวกับหน้าจอ ติดต่อ View ผ่าน SimulationListener เท่านั้น
 */
public class SimulationController {

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

    private SimulationListener listener;


    public SimulationController() {

        initializeSystem();
    }

    public void setListener(
            SimulationListener listener) {

        this.listener = listener;
    }

    /*
     * ============================================================
     * GETTERS (ให้ View อ่านค่าไปแสดงผล)
     * ============================================================
     */

    public CPU getCpu() {
        return cpu;
    }

    public Process getProcess() {
        return process;
    }

    public InterruptController getInterruptController() {
        return interruptController;
    }

    public SimulationState getSimulationState() {
        return simulationState;
    }

    public int getAnimationStep() {
        return animationStep;
    }

    public Interrupt getCurrentInterrupt() {
        return currentInterrupt;
    }

    public PCB getCurrentPCB() {
        return currentPCB;
    }

    public int getInterruptCount() {
        return interruptCount;
    }

    /*
     * ============================================================
     * NOTIFY VIEW
     * ============================================================
     */

    private void addLog(String message) {

        if (listener != null) {

            listener.onLog(message);
        }
    }

    private void updateDisplay() {

        if (listener != null) {

            listener.onStateChanged();
        }
    }

    /*
     * ============================================================
     * RESET
     * ============================================================
     */

    public void reset() {

        if (animationTimer != null) {

            animationTimer.stop();

            animationTimer = null;
        }

        initializeSystem();

        if (listener != null) {

            listener.onReset();
        }
    }

    /*
     * ============================================================
     * INITIALIZE SYSTEM
     * ============================================================
     */

    private void initializeSystem() {

        cpu = new CPU();

        process = new Process(
                1,
                "P1"
        );

        interruptController =
                new InterruptController();

        cpu.loadProcess(
                process
        );

        simulationState =
                SimulationState.READY;

        interruptId = 1;

        interruptCount = 0;

        animationStep = 0;

        currentInterrupt = null;

        currentPCB = null;

        autoMode = false;
    }



    /*
     * ============================================================
     * EXECUTE PROCESS
     * ============================================================
     */

    public void executeProcess() {

        if (animationTimer != null) {

            addLog(
                    "Cannot execute while "
                    + "interrupt handling is active or paused"
            );

            return;
        }


        if (process.getState() != ProcessState.RUNNING) {

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


    /*
     * ============================================================
     * GENERATE INTERRUPT
     * ============================================================
     */

    public void generateInterrupt(
            String type,
            int priority) {

        if (animationTimer != null) {

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


        /*
         * ส่ง state จริงไปทั้งสอง visualization
         */

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


    /*
     * ============================================================
     * START INTERRUPT HANDLING
     * ============================================================
     */

    public void startInterruptHandling() {

        if (!interruptController.hasInterrupt()) {

            addLog(
                    "No interrupt in queue"
            );

            return;
        }


        /*
         * ถ้ามี Timer อยู่
         * หมายถึงกำลังทำงานหรือถูก Pause
         */

        if (animationTimer != null) {

            addLog(
                    "Interrupt handling is "
                    + "already running or paused"
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
         * Timer เป็นตัวควบคุม Timeline จริง
         *
         * ทุกครั้งที่ Timer tick
         * จะเปลี่ยน SimulationState
         */

        animationTimer =
                new Timer(
                        NORMAL_STEP_DELAY,
                        e -> processAnimation()
                );


        animationTimer.start();
    }


    /*
     * ============================================================
     * PROCESS ANIMATION
     * ============================================================
     *
     * State ของระบบจริงถูกเปลี่ยนที่นี่
     *
     * StateDiagramPanel
     * และ
     * InterruptIllustrationPanel
     *
     * จะได้รับ state เดียวกันผ่าน updateDisplay()
     */

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
                        interruptController
                                .peekNextInterrupt();


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


                process.setState(ProcessState.INTERRUPTED);


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
             * ISR EXECUTING
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


                    addLog(
                            result
                    );
                }


                /*
                 * ISR ค้างนานกว่า State อื่น
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


                process.setState(ProcessState.RUNNING);


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
                 * ค้าง RESUMED
                 * เพื่อให้เห็น transition
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


                process.setState(ProcessState.RUNNING);


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


                    process.setState(ProcessState.RUNNING);


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


                else if (
                        interruptController.hasInterrupt()
                ) {

                    addLog(
                            "Next interrupt in queue..."
                    );


                    currentInterrupt = null;

                    currentPCB = null;


                    /*
                     * เริ่ม interrupt ตัวถัดไป
                     *
                     * รอบใหม่เริ่มจาก STEP 1
                     */

                    animationStep = 0;
                }


                else {

                    simulationState =
                            SimulationState.RUNNING;


                    process.setState(ProcessState.RUNNING);


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
    }


    /*
     * ============================================================
     * PAUSE / RESUME
     * ============================================================
     */

    public void togglePause() {

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


    /*
     * ============================================================
     * AUTO SIMULATION
     * ============================================================
     */

    public void autoSimulation() {

        if (animationTimer != null) {

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
             * เพิ่ม Interrupt 4 ตัว
             *
             * Timer     = Priority 1
             * Keyboard  = Priority 2
             * Disk      = Priority 1
             * Network   = Priority 3
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
}
