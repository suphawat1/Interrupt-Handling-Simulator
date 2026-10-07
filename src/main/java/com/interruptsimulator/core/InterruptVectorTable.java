package com.interruptsimulator.core;

import java.util.HashMap;
import java.util.Map;

import com.interruptsimulator.core.handler.DiskInterruptHandler;
import com.interruptsimulator.core.handler.KeyboardInterruptHandler;
import com.interruptsimulator.core.handler.NetworkInterruptHandler;
import com.interruptsimulator.core.handler.TimerInterruptHandler;

public class InterruptVectorTable {

    private Map<String, InterruptHandler> handlers;

    public InterruptVectorTable() {

        handlers = new HashMap<>();

        registerHandler("Timer", new TimerInterruptHandler());
        registerHandler("Keyboard", new KeyboardInterruptHandler());
        registerHandler("Disk", new DiskInterruptHandler());
        registerHandler("Network", new NetworkInterruptHandler());
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
