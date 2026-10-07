package com.interruptsimulator.core;

import java.util.HashMap;
import java.util.Map;

public class InterruptVectorTable {

    private Map<String, InterruptHandler> handlers;

    public InterruptVectorTable() {

        handlers = new HashMap<>();

        registerHandler(
                "Timer",
                new InterruptHandler()
        );

        registerHandler(
                "Keyboard",
                new InterruptHandler()
        );

        registerHandler(
                "Disk",
                new InterruptHandler()
        );

        registerHandler(
                "Network",
                new InterruptHandler()
        );
    }

    public void registerHandler(
            String interruptType,
            InterruptHandler handler) {

        handlers.put(
                interruptType,
                handler
        );
    }

    public InterruptHandler getHandler(
            String interruptType) {

        return handlers.get(
                interruptType
        );
    }

    public boolean contains(
            String interruptType) {

        return handlers.containsKey(
                interruptType
        );
    }

    public int size() {

        return handlers.size();
    }
}