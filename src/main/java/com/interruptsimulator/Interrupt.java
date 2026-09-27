package com.interruptsimulator;

public class Interrupt {

    private int interruptId;
    private String type;
    private int priority;

    public Interrupt(int interruptId, String type, int priority) {
        this.interruptId = interruptId;
        this.type = type;
        this.priority = priority;
    }

    public int getInterruptId() {
        return interruptId;
    }

    public String getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Interrupt ID=" + interruptId
                + ", Type=" + type
                + ", Priority=" + priority;
    }
}