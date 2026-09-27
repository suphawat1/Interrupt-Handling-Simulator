package com.interruptsimulator;

public class InterruptHandler {

    public String handle(Interrupt interrupt) {

        StringBuilder log =
                new StringBuilder();

        log.append(
                "ISR started: "
        );

        log.append(
                interrupt.getType()
        );

        log.append(
                " Interrupt\n"
        );

        switch (interrupt.getType()) {

            case "Timer":

                log.append(
                        "Timer ISR: "
                        + "processing timer event\n"
                );

                break;

            case "Keyboard":

                log.append(
                        "Keyboard ISR: "
                        + "processing keyboard input\n"
                );

                break;

            case "Disk":

                log.append(
                        "Disk ISR: "
                        + "processing disk I/O\n"
                );

                break;

            case "Network":

                log.append(
                        "Network ISR: "
                        + "processing network packet\n"
                );

                break;

            default:

                log.append(
                        "Unknown ISR\n"
                );

                break;
        }

        log.append(
                "ISR completed"
        );

        return log.toString();
    }
}