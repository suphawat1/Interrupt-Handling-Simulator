package com.interruptsimulator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

/**
 * Interrupt Illustration
 *
 * แสดง Interrupt Handling ในรูปแบบ Timeline / Waveform
 *
 * แนวคิด:
 *
 * CPU
 * ────────────────┐    ┌───────────────
 *                 │    │
 *                 └────┘
 *
 * I/O DEVICE
 * ────────┐          ┌───────────────
 *         │          │
 *         └──────────┘
 *
 * มีเส้น Event Marker และ Packet
 * ที่เคลื่อนที่ตาม SimulationState จริง
 */
public class InterruptIllustrationPanel extends JPanel {

    /*
     * ============================================================
     * ANIMATION
     * ============================================================
     */

    private static final int TIMER_DELAY = 16;

    /*
     * ความเร็วของ Marker
     *
     * ยิ่งน้อย = เคลื่อนที่ช้าลง
     */
    private static final double MARKER_SPEED = 0.0018;


    /*
     * ============================================================
     * COLORS
     * ============================================================
     */

    private static final Color BACKGROUND =
            new Color(10, 12, 14);

    private static final Color PANEL_BACKGROUND =
            new Color(14, 17, 19);

    private static final Color GRID_COLOR =
            new Color(35, 40, 42);

    private static final Color LINE_COLOR =
            new Color(105, 112, 115);

    private static final Color CPU_LINE =
            new Color(190, 195, 195);

    private static final Color IO_LINE =
            new Color(145, 155, 158);

    private static final Color ACTIVE_LINE =
            new Color(83, 220, 151);

    private static final Color INTERRUPT_LINE =
            new Color(230, 180, 80);

    private static final Color EVENT_LINE =
            new Color(120, 150, 160);

    private static final Color TEXT =
            new Color(220, 224, 224);

    private static final Color MUTED_TEXT =
            new Color(130, 138, 140);

    private static final Color RED =
            new Color(190, 75, 75);


    /*
     * ============================================================
     * CURRENT SIMULATION STATE
     * ============================================================
     */

    private String simulationState =
            "READY";

    private Interrupt currentInterrupt;


    /*
     * ============================================================
     * TIMELINE
     * ============================================================
     *
     * 0.00 = เริ่ม
     * 1.00 = จบ Interrupt cycle
     */

    private double targetTimeline =
            0.0;

    private double currentTimeline =
            0.0;


    /*
     * Marker สำหรับเส้นที่กำลังวิ่ง
     */

    private double markerPosition =
            0.0;


    /*
     * Pulse สำหรับจุด Active
     */

    private double pulse =
            0.0;


    private Timer animationTimer;


    /*
     * ============================================================
     * CONSTRUCTOR
     * ============================================================
     */

    public InterruptIllustrationPanel() {

        setBackground(
                BACKGROUND
        );

        setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(55, 60, 62)
                        ),
                        "INTERRUPT ILLUSTRATION"
                )
        );

        setPreferredSize(
                new Dimension(
                        800,
                        360
                )
        );


        startAnimation();
    }


    /*
     * ============================================================
     * START ANIMATION
     * ============================================================
     */

    private void startAnimation() {

        animationTimer =
                new Timer(
                        TIMER_DELAY,
                        e -> updateAnimation()
                );

        animationTimer.start();
    }


    /*
     * ============================================================
     * SET SIMULATION STATE
     * ============================================================
     *
     * SimulatorGUI จะเรียก method นี้
     * ทุกครั้งที่ SimulationState เปลี่ยน
     */

    public void setSimulationState(
            String state) {

        if (state == null) {
            return;
        }

        simulationState =
                state;

        targetTimeline =
                getTimelinePosition(
                        state
                );

        repaint();
    }


    /*
     * ============================================================
     * SET CURRENT INTERRUPT
     * ============================================================
     */

    public void setInterrupt(
            Interrupt interrupt) {

        currentInterrupt =
                interrupt;

        repaint();
    }


    /*
     * ============================================================
     * RESET
     * ============================================================
     */

    public void resetIllustration() {

        simulationState =
                "READY";

        currentInterrupt =
                null;

        targetTimeline =
                0.0;

        currentTimeline =
                0.0;

        markerPosition =
                0.0;

        pulse =
                0.0;

        repaint();
    }


    /*
     * ============================================================
     * UPDATE ANIMATION
     * ============================================================
     */

    private void updateAnimation() {

        /*
         * --------------------------------------------------------
         * Timeline ค่อย ๆ วิ่งไปยัง State จริง
         * --------------------------------------------------------
         */

        double difference =
                targetTimeline
                        - currentTimeline;


        if (Math.abs(difference) > 0.001) {

            currentTimeline +=
                    difference * 0.035;

        } else {

            currentTimeline =
                    targetTimeline;
        }


        /*
         * --------------------------------------------------------
         * Marker
         * --------------------------------------------------------
         *
         * Marker จะวิ่งเฉพาะตอนมี Interrupt
         */

        if (isInterruptActive()) {

            markerPosition +=
                    MARKER_SPEED;

            if (markerPosition > 1.0) {

                markerPosition =
                        0.0;
            }

        } else {

            markerPosition =
                    currentTimeline;
        }


        /*
         * --------------------------------------------------------
         * Pulse
         * --------------------------------------------------------
         */

        pulse += 0.08;

        if (pulse > Math.PI * 2) {

            pulse = 0.0;
        }


        repaint();
    }


    /*
     * ============================================================
     * MAP SIMULATION STATE -> TIMELINE
     * ============================================================
     *
     * ไม่สร้าง State ใหม่
     *
     * ใช้ SimulationState ของระบบจริงทั้งหมด
     */

    private double getTimelinePosition(
            String state) {

        switch (state) {

            case "READY":
                return 0.00;

            case "RUNNING":
                return 0.10;

            case "INTERRUPT_RECEIVED":
                return 0.25;

            case "SAVING_CONTEXT":
                return 0.38;

            case "INTERRUPTED":
                return 0.48;

            case "LOOKUP_HANDLER":
                return 0.60;

            case "ISR_EXECUTING":
                return 0.72;

            case "RESTORING_CONTEXT":
                return 0.84;

            case "RESUMED":
                return 0.94;

            default:
                return 0.00;
        }
    }


    /*
     * ============================================================
     * CHECK INTERRUPT ACTIVE
     * ============================================================
     */

    private boolean isInterruptActive() {

        switch (simulationState) {

            case "INTERRUPT_RECEIVED":
            case "SAVING_CONTEXT":
            case "INTERRUPTED":
            case "LOOKUP_HANDLER":
            case "ISR_EXECUTING":
            case "RESTORING_CONTEXT":
            case "RESUMED":

                return true;

            default:

                return false;
        }
    }


    /*
     * ============================================================
     * PAINT
     * ============================================================
     */

    @Override
    protected void paintComponent(
            Graphics graphics) {

        super.paintComponent(
                graphics
        );

        Graphics2D g =
                (Graphics2D) graphics;


        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );


        int width =
                getWidth();

        int height =
                getHeight();


        /*
         * ========================================================
         * BACKGROUND
         * ========================================================
         */

        g.setPaint(
                new GradientPaint(
                        0,
                        0,
                        BACKGROUND,
                        0,
                        height,
                        PANEL_BACKGROUND
                )
        );

        g.fillRect(
                0,
                0,
                width,
                height
        );


        /*
         * ========================================================
         * HEADER
         * ========================================================
         */

        drawHeader(
                g,
                width
        );


        /*
         * ========================================================
         * TIMELINE GRID
         * ========================================================
         */

        int left =
                110;

        int right =
                width - 40;

        int top =
                85;

        int bottom =
                height - 45;


        drawGrid(
                g,
                left,
                right,
                top,
                bottom
        );


        /*
         * ========================================================
         * CPU WAVEFORM
         * ========================================================
         */

        int cpuY =
                135;

        drawCPUWaveform(
                g,
                left,
                right,
                cpuY
        );


        /*
         * ========================================================
         * I/O DEVICE WAVEFORM
         * ========================================================
         */

        int ioY =
                235;

        drawIOWaveform(
                g,
                left,
                right,
                ioY
        );


        /*
         * ========================================================
         * EVENT MARKER
         * ========================================================
         */

        drawEventMarker(
                g,
                left,
                right,
                top,
                bottom
        );


        /*
         * ========================================================
         * MOVING INTERRUPT SIGNAL
         * ========================================================
         */

        drawMovingSignal(
                g,
                left,
                right,
                cpuY,
                ioY
        );


        /*
         * ========================================================
         * CURRENT STATE
         * ========================================================
         */

        drawCurrentState(
                g,
                width,
                height
        );


        /*
         * ========================================================
         * INTERRUPT INFORMATION
         * ========================================================
         */

        drawInterruptInfo(
                g,
                width,
                height
        );
    }


    /*
     * ============================================================
     * HEADER
     * ============================================================
     */

    private void drawHeader(
            Graphics2D g,
            int width) {

        g.setColor(
                TEXT
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        g.drawString(
                "Interrupt Illustration",
                25,
                30
        );


        g.setColor(
                MUTED_TEXT
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        g.drawString(
                "CPU / I/O interrupt timeline",
                25,
                48
        );


        /*
         * Current state indicator
         */

        String stateText =
                "STATE: "
                + simulationState;


        int textWidth =
                g.getFontMetrics()
                        .stringWidth(
                                stateText
                        );


        g.setColor(
                getStateColor()
        );

        g.fillOval(
                width - textWidth - 55,
                20,
                9,
                9
        );


        g.setColor(
                TEXT
        );

        g.drawString(
                stateText,
                width - textWidth - 40,
                29
        );
    }


    /*
     * ============================================================
     * GRID
     * ============================================================
     */

    private void drawGrid(
            Graphics2D g,
            int left,
            int right,
            int top,
            int bottom) {

        g.setStroke(
                new BasicStroke(
                        1f
                )
        );

        g.setColor(
                GRID_COLOR
        );


        /*
         * Horizontal lines
         */

        g.drawLine(
                left,
                110,
                right,
                110
        );

        g.drawLine(
                left,
                160,
                right,
                160
        );

        g.drawLine(
                left,
                210,
                right,
                210
        );

        g.drawLine(
                left,
                260,
                right,
                260
        );


        /*
         * Vertical timeline markers
         */

        int divisions =
                8;

        for (int i = 0;
             i <= divisions;
             i++) {

            double ratio =
                    (double) i
                            / divisions;

            int x =
                    left
                    + (int)
                    ((right - left)
                            * ratio);

            g.drawLine(
                    x,
                    top,
                    x,
                    bottom
            );
        }
    }


    /*
     * ============================================================
     * CPU WAVEFORM
     * ============================================================
     */

    private void drawCPUWaveform(
            Graphics2D g,
            int left,
            int right,
            int y) {

        g.setColor(
                MUTED_TEXT
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        g.drawString(
                "CPU",
                25,
                y + 5
        );


        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        10
                )
        );

        g.setColor(
                MUTED_TEXT
        );

        g.drawString(
                "user process",
                25,
                y + 22
        );


        /*
         * Base line
         */

        g.setStroke(
                new BasicStroke(
                        2f
                )
        );

        g.setColor(
                CPU_LINE
        );


        int interruptStart =
                left
                + (int)
                ((right - left)
                        * 0.25);

        int interruptEnd =
                left
                + (int)
                ((right - left)
                        * 0.48);


        /*
         * CPU RUNNING
         */

        g.drawLine(
                left,
                y,
                interruptStart,
                y
        );


        /*
         * CPU interrupted
         *
         * ลงต่ำ
         */

        Path2D cpuInterrupted =
                new Path2D.Double();


        cpuInterrupted.moveTo(
                interruptStart,
                y
        );

        cpuInterrupted.lineTo(
                interruptStart,
                y + 35
        );

        cpuInterrupted.lineTo(
                interruptEnd,
                y + 35
        );

        cpuInterrupted.lineTo(
                interruptEnd,
                y
        );


        g.setColor(
                getCPUActiveColor()
        );

        g.draw(
                cpuInterrupted
        );


        /*
         * CPU resumes
         */

        g.setColor(
                CPU_LINE
        );

        g.drawLine(
                interruptEnd,
                y,
                right,
                y
        );


        /*
         * Labels
         */

        g.setColor(
                MUTED_TEXT
        );

        g.drawString(
                "RUNNING",
                left + 15,
                y - 10
        );

        g.drawString(
                "INTERRUPT SERVICE",
                interruptStart + 15,
                y + 55
        );

        g.drawString(
                "RUNNING",
                interruptEnd + 15,
                y - 10
        );
    }


    /*
     * ============================================================
     * I/O WAVEFORM
     * ============================================================
     */

    private void drawIOWaveform(
            Graphics2D g,
            int left,
            int right,
            int y) {

        g.setColor(
                MUTED_TEXT
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        g.drawString(
                "I/O DEVICE",
                25,
                y + 5
        );


        /*
         * Device idle
         */

        int requestX =
                left
                + (int)
                ((right - left)
                        * 0.08);

        int transferStart =
                left
                + (int)
                ((right - left)
                        * 0.28);

        int transferEnd =
                left
                + (int)
                ((right - left)
                        * 0.52);


        /*
         * Idle line
         */

        g.setStroke(
                new BasicStroke(
                        2f
                )
        );

        g.setColor(
                IO_LINE
        );

        g.drawLine(
                left,
                y,
                requestX,
                y
        );


        /*
         * Transfer begins
         */

        Path2D transfer =
                new Path2D.Double();


        transfer.moveTo(
                requestX,
                y
        );

        transfer.lineTo(
                requestX,
                y + 35
        );

        transfer.lineTo(
                transferEnd,
                y + 35
        );

        transfer.lineTo(
                transferEnd,
                y
        );


        g.setColor(
                INTERRUPT_LINE
        );

        g.draw(
                transfer
        );


        /*
         * Continue idle
         */

        g.setColor(
                IO_LINE
        );

        g.drawLine(
                transferEnd,
                y,
                right,
                y
        );


        /*
         * Labels
         */

        g.setColor(
                MUTED_TEXT
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        10
                )
        );

        g.drawString(
                "IDLE",
                left + 15,
                y - 10
        );

        g.drawString(
                "TRANSFERRING",
                requestX + 20,
                y + 55
        );

        g.drawString(
                "IDLE",
                transferEnd + 15,
                y - 10
        );
    }


    /*
     * ============================================================
     * EVENT MARKER
     * ============================================================
     */

    private void drawEventMarker(
            Graphics2D g,
            int left,
            int right,
            int top,
            int bottom) {

        int x =
                left
                + (int)
                ((right - left)
                        * currentTimeline);


        /*
         * เส้น Event
         */

        g.setStroke(
                new BasicStroke(
                        1.5f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_BEVEL,
                        0,
                        new float[]{5, 5},
                        0
                )
        );


        g.setColor(
                EVENT_LINE
        );

        g.drawLine(
                x,
                top,
                x,
                bottom
        );


        /*
         * จุดด้านบน
         */

        double glow =
                3
                + Math.sin(pulse)
                * 2;


        g.setColor(
                getStateColor()
        );

        g.fill(
                new Ellipse2D.Double(
                        x - glow,
                        top - glow,
                        glow * 2,
                        glow * 2
                )
        );


        /*
         * Timeline label
         */

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        10
                )
        );

        g.setColor(
                TEXT
        );

        String label =
                getTimelineLabel();


        int labelWidth =
                g.getFontMetrics()
                        .stringWidth(
                                label
                        );


        g.drawString(
                label,
                x - labelWidth / 2,
                bottom + 18
        );
    }


    /*
     * ============================================================
     * MOVING SIGNAL
     * ============================================================
     */

    private void drawMovingSignal(
            Graphics2D g,
            int left,
            int right,
            int cpuY,
            int ioY) {

        if (!isInterruptActive()) {

            return;
        }


        int x =
                left
                + (int)
                ((right - left)
                        * markerPosition);


        /*
         * Signal line
         */

        g.setStroke(
                new BasicStroke(
                        2.5f
                )
        );

        g.setColor(
                ACTIVE_LINE
        );


        /*
         * วิ่งผ่าน CPU
         */

        g.drawLine(
                x - 18,
                cpuY,
                x,
                cpuY
        );


        /*
         * จุดสัญญาณ
         */

        g.fill(
                new Ellipse2D.Double(
                        x - 5,
                        cpuY - 5,
                        10,
                        10
                )
        );


        /*
         * ถ้าอยู่ช่วง ISR
         * ให้เส้นลงมาหา I/O
         */

        if (simulationState.equals(
                "ISR_EXECUTING")) {

            g.setColor(
                    INTERRUPT_LINE
            );

            g.drawLine(
                    x,
                    cpuY,
                    x,
                    ioY
            );


            g.fill(
                    new Ellipse2D.Double(
                            x - 5,
                            ioY - 5,
                            10,
                            10
                    )
            );
        }
    }


    /*
     * ============================================================
     * CURRENT STATE
     * ============================================================
     */

    private void drawCurrentState(
            Graphics2D g,
            int width,
            int height) {

        g.setColor(
                MUTED_TEXT
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        11
                )
        );

        g.drawString(
                "Current simulation state",
                25,
                height - 22
        );


        g.setColor(
                getStateColor()
        );

        g.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        12
                )
        );

        g.drawString(
                simulationState,
                185,
                height - 22
        );
    }


    /*
     * ============================================================
     * INTERRUPT INFORMATION
     * ============================================================
     */

    private void drawInterruptInfo(
            Graphics2D g,
            int width,
            int height) {

        if (currentInterrupt == null) {

            return;
        }


        String text =
                currentInterrupt.getType()
                + "  |  Priority "
                + currentInterrupt.getPriority()
                + "  |  ID "
                + currentInterrupt.getInterruptId();


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        11
                )
        );


        int textWidth =
                g.getFontMetrics()
                        .stringWidth(
                                text
                        );


        g.setColor(
                INTERRUPT_LINE
        );


        g.drawString(
                text,
                width - textWidth - 25,
                height - 22
        );
    }


    /*
     * ============================================================
     * STATE COLOR
     * ============================================================
     */

    private Color getStateColor() {

        switch (simulationState) {

            case "INTERRUPT_RECEIVED":
            case "SAVING_CONTEXT":

                return INTERRUPT_LINE;

            case "INTERRUPTED":
            case "LOOKUP_HANDLER":

                return new Color(
                        210,
                        150,
                        65
                );

            case "ISR_EXECUTING":

                return ACTIVE_LINE;

            case "RESTORING_CONTEXT":
            case "RESUMED":

                return new Color(
                        80,
                        170,
                        220
                );

            case "RUNNING":

                return ACTIVE_LINE;

            default:

                return LINE_COLOR;
        }
    }


    /*
     * ============================================================
     * CPU ACTIVE COLOR
     * ============================================================
     */

    private Color getCPUActiveColor() {

        if (simulationState.equals(
                "INTERRUPTED")
                || simulationState.equals(
                "LOOKUP_HANDLER")
                || simulationState.equals(
                "ISR_EXECUTING")
                || simulationState.equals(
                "RESTORING_CONTEXT")) {

            return RED;
        }

        return CPU_LINE;
    }


    /*
     * ============================================================
     * TIMELINE LABEL
     * ============================================================
     */

    private String getTimelineLabel() {

        switch (simulationState) {

            case "READY":
                return "READY";

            case "RUNNING":
                return "CPU EXECUTION";

            case "INTERRUPT_RECEIVED":
                return "INTERRUPT REQUEST";

            case "SAVING_CONTEXT":
                return "SAVE CONTEXT";

            case "INTERRUPTED":
                return "CPU INTERRUPTED";

            case "LOOKUP_HANDLER":
                return "VECTOR LOOKUP";

            case "ISR_EXECUTING":
                return "ISR";

            case "RESTORING_CONTEXT":
                return "RESTORE";

            case "RESUMED":
                return "PROCESS RESUMED";

            default:
                return simulationState;
        }
    }
}