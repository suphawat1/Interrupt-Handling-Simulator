package com.interruptsimulator.core.handler;

public class TimerInterruptHandler extends BaseInterruptHandler {

    @Override
    protected String getName() {
        return "Timer";
    }

    @Override
    protected String getAction() {
        return "processing timer event";
    }
}
