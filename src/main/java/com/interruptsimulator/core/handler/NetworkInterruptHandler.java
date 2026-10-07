package com.interruptsimulator.core.handler;

public class NetworkInterruptHandler extends BaseInterruptHandler {

    @Override
    protected String getName() {
        return "Network";
    }

    @Override
    protected String getAction() {
        return "processing network packet";
    }
}
