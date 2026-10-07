package com.interruptsimulator.core;

import com.interruptsimulator.model.ProcessState;
import com.interruptsimulator.model.PCB;
import com.interruptsimulator.model.Process;


public class CPU {

    private Process currentProcess;
    private PCB savedPCB;

    private int totalExecutions;
    private int contextSaveCount;
    private int contextRestoreCount;

    public void loadProcess(Process process) {

        currentProcess = process;

        currentProcess.setState(ProcessState.RUNNING);
    }

    public void execute() {

        if (currentProcess == null) {
            return;
        }

        if (currentProcess.getState() != ProcessState.RUNNING) {
            return;
        }

        currentProcess.run();

        totalExecutions++;
    }

    /*
     * Save current CPU context into PCB.
     */
    public PCB saveContext() {

        if (currentProcess == null) {
            return null;
        }

        savedPCB = new PCB(currentProcess);

        currentProcess.setState(ProcessState.INTERRUPTED);

        contextSaveCount++;

        return savedPCB;
    }

    /*
     * Restore saved CPU context.
     */
    public void restoreContext() {

        if (currentProcess == null) {
            return;
        }

        if (savedPCB == null) {
            return;
        }

        currentProcess.restoreContext(savedPCB);

        savedPCB = null;

        contextRestoreCount++;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public PCB getSavedPCB() {
        return savedPCB;
    }

    public int getTotalExecutions() {
        return totalExecutions;
    }

    public int getContextSaveCount() {
        return contextSaveCount;
    }

    public int getContextRestoreCount() {
        return contextRestoreCount;
    }
}