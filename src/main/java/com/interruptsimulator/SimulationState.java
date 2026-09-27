package com.interruptsimulator;

public enum SimulationState {

    READY,
    RUNNING,
    INTERRUPT_RECEIVED,
    SAVING_CONTEXT,
    INTERRUPTED,
    LOOKUP_HANDLER,
    ISR_EXECUTING,
    RESTORING_CONTEXT,
    RESUMED
}