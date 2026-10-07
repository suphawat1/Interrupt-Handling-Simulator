package com.interruptsimulator.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PCBTest {

    @Test
    void copiesProcessValuesAtCreationTime() {

        Process process = new Process(1, "P1");
        process.run();

        PCB pcb = new PCB(process);

        assertEquals(1, pcb.getProcessId());
        assertEquals(10, pcb.getProgramCounter());
        assertEquals(5, pcb.getRegisterA());
        assertEquals(2, pcb.getRegisterB());
        assertEquals(ProcessState.RUNNING, pcb.getState());
    }

    @Test
    void isASnapshotNotALiveView() {

        Process process = new Process(1, "P1");
        process.run();

        PCB pcb = new PCB(process);

        process.run();
        process.run();

        assertEquals(10, pcb.getProgramCounter());
        assertEquals(30, process.getProgramCounter());
    }
}
