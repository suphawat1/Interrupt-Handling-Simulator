package com.interruptsimulator.model;

public class Process {

    private int processId;
    private String processName;
    private String state;

    private int programCounter;
    private int registerA;
    private int registerB;

    public Process(int processId, String processName) {

        this.processId = processId;
        this.processName = processName;

        state = "READY";

        programCounter = 0;
        registerA = 0;
        registerB = 0;
    }

    public void run() {

        state = "RUNNING";

        programCounter += 10;
        registerA += 5;
        registerB += 2;
    }

    public void setState(String state) {
        this.state = state;
    }

    /*
     * Restore CPU context from PCB.
     */
    public void restoreContext(PCB pcb) {

        if (pcb == null) {
            return;
        }

        programCounter = pcb.getProgramCounter();
        registerA = pcb.getRegisterA();
        registerB = pcb.getRegisterB();

        state = "RUNNING";
    }

    public int getProcessId() {
        return processId;
    }

    public String getProcessName() {
        return processName;
    }

    public String getState() {
        return state;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public int getRegisterA() {
        return registerA;
    }

    public int getRegisterB() {
        return registerB;
    }
}