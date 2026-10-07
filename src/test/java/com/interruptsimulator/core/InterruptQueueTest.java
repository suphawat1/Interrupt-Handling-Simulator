package com.interruptsimulator.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.interruptsimulator.model.Interrupt;

class InterruptQueueTest {

    private InterruptQueue queue;

    @BeforeEach
    void setUp() {
        queue = new InterruptQueue();
    }

    @Test
    void startsEmpty() {

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
        assertNull(queue.getNextInterrupt());
        assertNull(queue.peekNextInterrupt());
    }

    @Test
    void lowerPriorityNumberComesFirst() {

        queue.addInterrupt(new Interrupt(1, "Network", 3));
        queue.addInterrupt(new Interrupt(2, "Keyboard", 2));
        queue.addInterrupt(new Interrupt(3, "Timer", 1));

        assertEquals("Timer", queue.getNextInterrupt().getType());
        assertEquals("Keyboard", queue.getNextInterrupt().getType());
        assertEquals("Network", queue.getNextInterrupt().getType());
    }

    @Test
    void samePriorityIsServedInArrivalOrder() {

        queue.addInterrupt(new Interrupt(1, "Timer", 1));
        queue.addInterrupt(new Interrupt(3, "Disk", 1));
        queue.addInterrupt(new Interrupt(2, "Keyboard", 1));

        assertEquals(1, queue.getNextInterrupt().getInterruptId());
        assertEquals(2, queue.getNextInterrupt().getInterruptId());
        assertEquals(3, queue.getNextInterrupt().getInterruptId());
    }

    @Test
    void peekDoesNotRemove() {

        queue.addInterrupt(new Interrupt(1, "Timer", 1));

        assertEquals("Timer", queue.peekNextInterrupt().getType());
        assertEquals(1, queue.size());
    }

    @Test
    void getAllInterruptsReturnsACopy() {

        queue.addInterrupt(new Interrupt(1, "Timer", 1));

        queue.getAllInterrupts().clear();

        assertEquals(1, queue.size());
    }
}
