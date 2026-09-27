package com.interruptsimulator;

import java.util.Comparator;
import java.util.PriorityQueue;

public class InterruptQueue {

    private PriorityQueue<Interrupt> queue;

    public InterruptQueue() {

        queue = new PriorityQueue<>(
                Comparator
                        .comparingInt(Interrupt::getPriority)
                        .thenComparingInt(Interrupt::getInterruptId)
        );
    }

    public void addInterrupt(Interrupt interrupt) {
        queue.offer(interrupt);
    }

    public Interrupt getNextInterrupt() {
        return queue.poll();
    }

    public Interrupt peekNextInterrupt() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public String getQueueDescription() {

        if (queue.isEmpty()) {
            return "Empty";
        }

        StringBuilder result =
                new StringBuilder();

        for (Interrupt interrupt : queue) {

            result.append(
                    interrupt.getType()
            );

            result.append(
                    "(P"
            );

            result.append(
                    interrupt.getPriority()
            );

            result.append("), ");
        }

        return result.substring(
                0,
                result.length() - 2
        );
    }
    public java.util.List<Interrupt> getAllInterrupts() {

    return new java.util.ArrayList<>(
            queue
    );
}
}