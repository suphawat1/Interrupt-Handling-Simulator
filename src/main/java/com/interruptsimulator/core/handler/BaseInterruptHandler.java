package com.interruptsimulator.core.handler;

import com.interruptsimulator.core.InterruptHandler;
import com.interruptsimulator.model.Interrupt;

/**
 * โครงของ ISR: ข้อความเริ่มต้น -> งานเฉพาะชนิด -> ข้อความจบ
 * คลาสลูกกำหนดแค่ชื่อและงานที่ทำ
 */
public abstract class BaseInterruptHandler implements InterruptHandler {

    /** ชื่อใช้ใน log เช่น "Timer" */
    protected abstract String getName();

    /** งานที่ ISR ทำ เช่น "processing timer event" */
    protected abstract String getAction();

    @Override
    public String handle(Interrupt interrupt) {

        return "ISR started: "
                + interrupt.getType()
                + " Interrupt\n"
                + getName()
                + " ISR: "
                + getAction()
                + "\n"
                + "ISR completed";
    }
}
