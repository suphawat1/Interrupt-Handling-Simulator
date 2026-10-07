package com.interruptsimulator.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.interruptsimulator.model.PCB;
import com.interruptsimulator.model.Process;
import com.interruptsimulator.model.ProcessState;

class CPUTest {

    private CPU cpu;
    private Process process;

    @BeforeEach
    void setUp() {
        cpu = new CPU();
        process = new Process(1, "P1");
        cpu.loadProcess(process);
    }

    @Test
    void loadProcessMakesItRunning() {

        assertEquals(ProcessState.RUNNING, process.getState());
    }

    @Test
    void executeAdvancesProcessAndCountsExecutions() {

        cpu.execute();
        cpu.execute();

        assertEquals(20, process.getProgramCounter());
        assertEquals(10, process.getRegisterA());
        assertEquals(4, process.getRegisterB());
        assertEquals(2, cpu.getTotalExecutions());
    }

    @Test
    void executeDoesNothingWhileInterrupted() {

        cpu.saveContext();
        cpu.execute();

        assertEquals(0, process.getProgramCounter());
        assertEquals(0, cpu.getTotalExecutions());
    }

    @Test
    void saveContextCapturesRegistersAndInterruptsProcess() {

        cpu.execute();

        PCB pcb = cpu.saveContext();

        assertNotNull(pcb);
        assertEquals(10, pcb.getProgramCounter());
        assertEquals(ProcessState.INTERRUPTED, process.getState());
        assertEquals(1, cpu.getContextSaveCount());
    }

    @Test
    void restoreContextBringsBackSavedValues() {

        cpu.execute();
        cpu.saveContext();

        // simulate the ISR clobbering the registers
        process.run();
        process.run();
        assertEquals(30, process.getProgramCounter());

        cpu.restoreContext();

        assertEquals(10, process.getProgramCounter());
        assertEquals(5, process.getRegisterA());
        assertEquals(2, process.getRegisterB());
        assertEquals(ProcessState.RUNNING, process.getState());
        assertNull(cpu.getSavedPCB());
        assertEquals(1, cpu.getContextRestoreCount());
    }

    @Test
    void restoreWithoutSaveChangesNothing() {

        cpu.execute();

        cpu.restoreContext();

        assertEquals(10, process.getProgramCounter());
        assertEquals(0, cpu.getContextRestoreCount());
    }
}
