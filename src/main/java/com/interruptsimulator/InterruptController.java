package com.interruptsimulator;

public class InterruptController {

    private InterruptQueue interruptQueue;

    private InterruptVectorTable vectorTable;

    public InterruptController() {

        interruptQueue =
                new InterruptQueue();

        vectorTable =
                new InterruptVectorTable();
    }

    public void receiveInterrupt(
            Interrupt interrupt) {

        interruptQueue.addInterrupt(
                interrupt
        );
    }

    public Interrupt getNextInterrupt() {

        return interruptQueue
                .getNextInterrupt();
    }

    public Interrupt peekNextInterrupt() {

        return interruptQueue
                .peekNextInterrupt();
    }

    public boolean hasInterrupt() {

        return !interruptQueue.isEmpty();
    }

    public int getQueueSize() {

        return interruptQueue.size();
    }

    public InterruptQueue getInterruptQueue() {

        return interruptQueue;
    }

    /*
     * Find ISR from Interrupt Vector Table.
     */
    public String handleInterrupt(
            Interrupt interrupt) {

        if (interrupt == null) {

            return "No interrupt";
        }

        InterruptHandler handler =
                vectorTable.getHandler(
                        interrupt.getType()
                );

        if (handler == null) {

            return "No handler registered for "
                    + interrupt.getType();
        }

        return handler.handle(
                interrupt
        );
    }

    public InterruptVectorTable
            getVectorTable() {

        return vectorTable;
    }
}