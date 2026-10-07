package com.interruptsimulator.model;

public class PCB {

    private int processId;
    private int programCounter;
    private int registerA;
    private int registerB;
    private ProcessState state;

    public PCB(Process process) {
        this.processId = process.getProcessId();
        this.programCounter = process.getProgramCounter();
        this.registerA = process.getRegisterA();
        this.registerB = process.getRegisterB();
        this.state = process.getState();
    }

    public int getProcessId() {
        return processId;
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

    public ProcessState getState() {
        return state;
    }

    @Override
    public String toString() {
        return "PID=" + processId
                + ", PC=" + programCounter
                + ", A=" + registerA
                + ", B=" + registerB
                + ", State=" + state;
    }
}