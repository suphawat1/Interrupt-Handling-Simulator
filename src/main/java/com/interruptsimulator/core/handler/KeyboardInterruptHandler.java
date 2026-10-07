package com.interruptsimulator.core.handler;

public class KeyboardInterruptHandler extends BaseInterruptHandler {

    @Override
    protected String getName() {
        return "Keyboard";
    }

    @Override
    protected String getAction() {
        return "processing keyboard input";
    }
}
