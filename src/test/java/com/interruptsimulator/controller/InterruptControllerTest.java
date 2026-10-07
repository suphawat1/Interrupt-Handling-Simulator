package com.interruptsimulator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.interruptsimulator.model.Interrupt;

class InterruptControllerTest {

    private InterruptController controller;

    @BeforeEach
    void setUp() {
        controller = new InterruptController();
    }

    @Test
    void receivedInterruptsAreQueuedByPriority() {

        controller.receiveInterrupt(new Interrupt(1, "Network", 3));
        controller.receiveInterrupt(new Interrupt(2, "Timer", 1));

        assertTrue(controller.hasInterrupt());
        assertEquals(2, controller.getQueueSize());
        assertEquals("Timer", controller.peekNextInterrupt().getType());
        assertEquals("Timer", controller.getNextInterrupt().getType());
        assertEquals(1, controller.getQueueSize());
    }

    @Test
    void emptyControllerHasNoInterrupt() {

        assertFalse(controller.hasInterrupt());
    }

    @Test
    void handleNullInterrupt() {

        assertEquals("No interrupt", controller.handleInterrupt(null));
    }

    @Test
    void handleUnknownInterrupt() {

        String result = controller.handleInterrupt(new Interrupt(1, "Mouse", 1));

        assertEquals("No handler registered for Mouse", result);
    }

    @Test
    void handleKnownInterruptRunsItsIsr() {

        String result = controller.handleInterrupt(new Interrupt(1, "Disk", 1));

        assertTrue(result.contains("Disk ISR: processing disk I/O"));
    }
}
