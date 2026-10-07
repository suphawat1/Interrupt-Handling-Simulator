package com.interruptsimulator.core.handler;

public class DiskInterruptHandler extends BaseInterruptHandler {

    @Override
    protected String getName() {
        return "Disk";
    }

    @Override
    protected String getAction() {
        return "processing disk I/O";
    }
}
