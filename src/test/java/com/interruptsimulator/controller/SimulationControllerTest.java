package com.interruptsimulator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.interruptsimulator.model.ProcessState;
import com.interruptsimulator.model.SimulationState;

class SimulationControllerTest {

    /** บันทึกสิ่งที่ controller แจ้งมาที่ View */
    private static class Recorder implements SimulationListener {

        final List<String> logs = new ArrayList<>();
        int stateChanges;
        int resets;

        @Override
        public void onLog(String message) {
            logs.add(message);
        }

        @Override
        public void onStateChanged() {
            stateChanges++;
        }

        @Override
        public void onReset() {
            resets++;
        }
    }

    private SimulationController controller;
    private Recorder recorder;

    @BeforeEach
    void setUp() {
        controller = new SimulationController();
        recorder = new Recorder();
        controller.setListener(recorder);
    }

    @Test
    void startsReadyWithRunningProcess() {

        assertEquals(SimulationState.READY, controller.getSimulationState());
        assertEquals(ProcessState.RUNNING, controller.getProcess().getState());
        assertEquals(0, controller.getInterruptCount());
        assertNull(controller.getCurrentInterrupt());
    }

    @Test
    void executeProcessAdvancesTheProgramCounter() {

        controller.executeProcess();

        assertEquals(10, controller.getProcess().getProgramCounter());
        assertEquals(SimulationState.RUNNING, controller.getSimulationState());
        assertTrue(recorder.stateChanges > 0);
    }

    @Test
    void generateInterruptQueuesItAndNotifiesView() {

        controller.generateInterrupt("Timer", 1);

        assertEquals(1, controller.getInterruptCount());
        assertEquals(1, controller.getInterruptController().getQueueSize());
        assertEquals(SimulationState.INTERRUPT_RECEIVED, controller.getSimulationState());
        assertTrue(recorder.logs.contains("Interrupt received"));
        assertTrue(recorder.stateChanges > 0);
    }

    @Test
    void interruptIdsIncreaseFromOne() {

        controller.generateInterrupt("Timer", 1);
        controller.generateInterrupt("Disk", 1);

        assertEquals(1, controller.getInterruptController().getNextInterrupt().getInterruptId());
        assertEquals(2, controller.getInterruptController().getNextInterrupt().getInterruptId());
    }

    @Test
    void handlingWithEmptyQueueDoesNothing() {

        controller.startInterruptHandling();

        assertTrue(recorder.logs.contains("No interrupt in queue"));
        assertEquals(SimulationState.READY, controller.getSimulationState());
    }

    @Test
    void resetRestoresInitialStateAndNotifiesView() {

        controller.executeProcess();
        controller.generateInterrupt("Keyboard", 2);

        controller.reset();

        assertEquals(1, recorder.resets);
        assertEquals(SimulationState.READY, controller.getSimulationState());
        assertEquals(0, controller.getInterruptCount());
        assertEquals(0, controller.getProcess().getProgramCounter());
        assertEquals(0, controller.getInterruptController().getQueueSize());
    }

    @Test
    void worksWithoutAListener() {

        SimulationController bare = new SimulationController();

        bare.generateInterrupt("Disk", 1);
        bare.executeProcess();

        assertEquals(1, bare.getInterruptCount());
    }
}
