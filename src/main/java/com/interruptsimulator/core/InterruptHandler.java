package com.interruptsimulator.core;

import com.interruptsimulator.model.Interrupt;

/**
 * Interrupt Service Routine (ISR) แต่ละชนิดต้อง implement interface นี้
 */
public interface InterruptHandler {

    /** ทำงานของ ISR แล้วคืนข้อความ log */
    String handle(Interrupt interrupt);
}
