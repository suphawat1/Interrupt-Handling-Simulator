package com.interruptsimulator.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.interruptsimulator.model.Interrupt;

class InterruptVectorTableTest {

    @Test
    void registersTheFourStandardInterrupts() {

        InterruptVectorTable table = new InterruptVectorTable();

        assertEquals(4, table.size());
        assertTrue(table.contains("Timer"));
        assertTrue(table.contains("Keyboard"));
        assertTrue(table.contains("Disk"));
        assertTrue(table.contains("Network"));
    }

    @Test
    void unknownTypeHasNoHandler() {

        InterruptVectorTable table = new InterruptVectorTable();

        assertFalse(table.contains("Mouse"));
        assertNull(table.getHandler("Mouse"));
    }

    @Test
    void registerHandlerReplacesExisting() {

        InterruptVectorTable table = new InterruptVectorTable();
        InterruptHandler custom = interrupt -> "custom";

        table.registerHandler("Timer", custom);

        assertSame(custom, table.getHandler("Timer"));
        assertEquals(4, table.size());
    }

    @ParameterizedTest
    @CsvSource({
            "Timer,processing timer event",
            "Keyboard,processing keyboard input",
            "Disk,processing disk I/O",
            "Network,processing network packet"
    })
    void eachHandlerProducesItsOwnLog(String type, String action) {

        InterruptVectorTable table = new InterruptVectorTable();

        String log = table
                .getHandler(type)
                .handle(new Interrupt(1, type, 1));

        assertEquals(
                "ISR started: " + type + " Interrupt\n"
                        + type + " ISR: " + action + "\n"
                        + "ISR completed",
                log
        );
    }
}
