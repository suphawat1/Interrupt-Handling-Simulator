package com.interruptsimulator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class StateDiagramPanel extends JPanel {

    /*
     * ============================================================
     * Animation
     * ============================================================
     */

    private static final int TIMER_DELAY = 16;

    /*
     * เพิ่มจาก 900 -> 1400 ms
     *
     * ทำให้การเปลี่ยน State ช้าลง
     * และมองเห็น transition ได้ชัดขึ้น
     */
    private static final double TRANSITION_DURATION = 1400.0;

    /*
     * ลดความเร็ว packet
     */
    private static final double FLOW_SPEED = 65.0;


    /*
     * Process State cards
     */
    private static final int PROCESS_CARD_WIDTH = 150;

    private static final int PROCESS_CARD_HEIGHT = 68;


    /*
     * Interrupt Handling cards
     */
    private static final int INTERRUPT_CARD_WIDTH = 128;

    private static final int INTERRUPT_CARD_HEIGHT = 62;


    /*
     * Layout
     */
    private static final int PROCESS_Y = 65;

    private static final int INTERRUPT_Y = 205;


    /*
     * Background
     */
    private static final Color BG =
            new Color(8, 10, 12);

    private static final Color CARD_BG =
            new Color(12, 15, 17);

    private static final Color ACTIVE_CARD_BG =
            new Color(16, 25, 20);

    private static final Color CARD_BORDER =
            new Color(53, 59, 56);

    private static final Color ACTIVE_BORDER =
            new Color(83, 220, 151);

    private static final Color ACCENT =
            new Color(83, 220, 151);

    private static final Color BLUE =
            new Color(56, 189, 248);

    private static final Color BLUE_LIGHT =
            new Color(125, 211, 252);

    private static final Color TEXT =
            new Color(210, 216, 211);

    private static final Color MUTED =
            new Color(126, 134, 129);

    private static final Color LINE =
            new Color(43, 49, 46);


    private String currentSimulationState =
            "READY";

    private String currentProcessState =
            "READY";

    private String currentInterruptPhase =
            "IDLE";


    private String displayedProcessState =
            "READY";

    private String targetProcessState =
            "READY";

    private String previousProcessState =
            "READY";


    private String displayedInterruptPhase =
            "IDLE";

    private String targetInterruptPhase =
            "IDLE";

    private String previousInterruptPhase =
            "IDLE";


    private Timer animationTimer;


    private double processTransitionProgress =
            1.0;

    private double interruptTransitionProgress =
            1.0;


    private double processFlowOffset =
            0.0;

    private double interruptFlowOffset =
            0.0;


    private double pulse =
            0.0;

    private long lastFrameTime;


    public StateDiagramPanel() {

        setBackground(BG);

        setOpaque(true);

        setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(43, 49, 46),
                                1
                        ),
                        BorderFactory.createEmptyBorder(
                                12,
                                18,
                                12,
                                18
                        )
                )
        );

        setPreferredSize(
                new Dimension(
                        920,
                        270
                )
        );
    }


    public void setState(
            String state) {

        if (state == null
                || state.trim().isEmpty()) {

            return;
        }


        currentSimulationState =
                state;


        String newProcessState =
                getProcessState(state);


        String newInterruptPhase =
                getInterruptPhase(state);


        updateProcessState(
                newProcessState
        );


        updateInterruptPhase(
                newInterruptPhase
        );


        startAnimation();

        repaint();
    }


    private String getProcessState(
            String state) {

        if (state.equals("READY")) {

            return "READY";
        }


        if (state.equals("RUNNING")) {

            return "RUNNING";
        }


        if (state.equals("INTERRUPT_RECEIVED")
                || state.equals("SAVING_CONTEXT")
                || state.equals("INTERRUPTED")
                || state.equals("LOOKUP_HANDLER")
                || state.equals("ISR_EXECUTING")
                || state.equals("RESTORING_CONTEXT")) {

            return "INTERRUPTED";
        }


        if (state.equals("RESUMED")) {

            return "RUNNING";
        }


        return currentProcessState;
    }


    private String getInterruptPhase(
            String state) {

        if (state.equals(
                "INTERRUPT_RECEIVED"
        )) {

            return "RECEIVED";
        }


        if (state.equals(
                "SAVING_CONTEXT"
        )) {

            return "SAVE CONTEXT";
        }


        if (state.equals(
                "LOOKUP_HANDLER"
        )) {

            return "LOOKUP HANDLER";
        }


        if (state.equals(
                "ISR_EXECUTING"
        )) {

            return "ISR";
        }


        if (state.equals(
                "RESTORING_CONTEXT"
        )) {

            return "RESTORE CONTEXT";
        }


        if (state.equals("RUNNING")
                || state.equals("READY")
                || state.equals("RESUMED")) {

            return "IDLE";
        }


        if (state.equals("INTERRUPTED")) {

            return "SAVE CONTEXT";
        }


        return currentInterruptPhase;
    }


    private void updateProcessState(
            String newState) {

        if (newState.equals(
                currentProcessState)) {

            return;
        }


        previousProcessState =
                currentProcessState;


        currentProcessState =
                newState;


        displayedProcessState =
                previousProcessState;


        targetProcessState =
                newState;


        processTransitionProgress =
                0.0;


        processFlowOffset =
                0.0;


        lastFrameTime =
                System.nanoTime();


        startAnimation();
    }


    private void updateInterruptPhase(
            String newPhase) {

        if (newPhase.equals(
                currentInterruptPhase)) {

            return;
        }


        previousInterruptPhase =
                currentInterruptPhase;


        currentInterruptPhase =
                newPhase;


        displayedInterruptPhase =
                previousInterruptPhase;


        targetInterruptPhase =
                newPhase;


        interruptTransitionProgress =
                0.0;


        interruptFlowOffset =
                0.0;


        lastFrameTime =
                System.nanoTime();


        startAnimation();
    }


    private void startAnimation() {

        if (animationTimer == null) {

            animationTimer =
                    new Timer(
                            TIMER_DELAY,
                            e -> updateAnimation()
                    );
        }


        if (!animationTimer.isRunning()) {

            lastFrameTime =
                    System.nanoTime();

            animationTimer.start();
        }
    }


    private void updateAnimation() {

        long now =
                System.nanoTime();


        double delta =
                (now - lastFrameTime)
                        / 1_000_000_000.0;


        lastFrameTime =
                now;


        delta =
                Math.min(
                        delta,
                        0.05
                );


        /*
         * Process State transition
         */
        if (processTransitionProgress < 1.0) {

            processTransitionProgress +=
                    delta
                            * 1000.0
                            / TRANSITION_DURATION;


            if (processTransitionProgress >= 1.0) {

                processTransitionProgress =
                        1.0;

                displayedProcessState =
                        targetProcessState;
            }
        }


        /*
         * Interrupt transition
         */
        if (interruptTransitionProgress < 1.0) {

            interruptTransitionProgress +=
                    delta
                            * 1000.0
                            / TRANSITION_DURATION;


            if (interruptTransitionProgress >= 1.0) {

                interruptTransitionProgress =
                        1.0;

                displayedInterruptPhase =
                        targetInterruptPhase;
            }
        }


        /*
         * Moving packets
         */
        processFlowOffset +=
                FLOW_SPEED * delta;

        interruptFlowOffset +=
                FLOW_SPEED * delta;


        pulse +=
                delta * 4.0;


        repaint();


        if (processTransitionProgress >= 1.0
                && interruptTransitionProgress >= 1.0
                && !isLiveState()) {

            animationTimer.stop();
        }
    }


    private boolean isLiveState() {

        return currentSimulationState.equals(
                "INTERRUPT_RECEIVED"
        )
                || currentSimulationState.equals(
                "SAVING_CONTEXT"
        )
                || currentSimulationState.equals(
                "LOOKUP_HANDLER"
        )
                || currentSimulationState.equals(
                "ISR_EXECUTING"
        )
                || currentSimulationState.equals(
                "RESTORING_CONTEXT"
        );
    }


    @Override
    protected void paintComponent(
            Graphics graphics) {

        super.paintComponent(graphics);


        Graphics2D g =
                (Graphics2D)
                        graphics.create();


        try {

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );


            drawTerminalBackground(g);

            drawHeader(g);

            drawProcessSection(g);

            /*
             * Separator
             *
             * อยู่เหนือ INTERRUPT HANDLING
             * และลากเต็มแนว
             */
            drawMonitorDivider(g);

            drawInterruptSection(g);

            drawFooter(g);

        } finally {

            g.dispose();
        }
    }


    private void drawTerminalBackground(
            Graphics2D g) {

        g.setColor(
                new Color(
                        22,
                        27,
                        24,
                        25
                )
        );


        for (int y = 42;
             y < getHeight();
             y += 4) {

            g.drawLine(
                    0,
                    y,
                    getWidth(),
                    y
            );
        }


        g.setColor(
                new Color(
                        32,
                        39,
                        35,
                        20
                )
        );


        for (int x = 0;
             x < getWidth();
             x += 80) {

            g.drawLine(
                    x,
                    42,
                    x,
                    getHeight()
            );
        }
    }


    private void drawHeader(
            Graphics2D g) {

        g.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        12
                )
        );

        g.setColor(TEXT);


        g.drawString(
                "PROCESS / INTERRUPT MONITOR",
                2,
                17
        );


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        10
                )
        );


        g.setColor(MUTED);


        g.drawString(
                "kernel event pipeline",
                2,
                32
        );


        int indicatorX =
                getWidth() - 78;


        g.setColor(
                new Color(
                        34,
                        197,
                        94,
                        35
                )
        );


        g.fillOval(
                indicatorX - 6,
                9,
                18,
                18
        );


        g.setColor(
                new Color(
                        34,
                        197,
                        94
                )
        );


        g.fillOval(
                indicatorX,
                15,
                6,
                6
        );


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        9
                )
        );


        g.setColor(
                new Color(
                        134,
                        239,
                        172
                )
        );


        g.drawString(
                "Active",
                indicatorX + 12,
                21
        );
    }


    private void drawProcessSection(
            Graphics2D g) {

        drawSectionTitle(
                g,
                "PROCESS STATE",
                44
        );


        int[] positions =
                calculateProcessPositions(
                        getWidth()
                );


        drawProcessConnection(
                g,
                positions[0],
                positions[1],
                "READY",
                "RUNNING"
        );


        drawProcessConnection(
                g,
                positions[1],
                positions[2],
                "RUNNING",
                "INTERRUPTED"
        );


        drawReturnConnection(
                g,
                positions[2],
                positions[1]
        );


        drawProcessCard(
                g,
                "READY",
                "WAITING",
                positions[0]
        );


        drawProcessCard(
                g,
                "RUNNING",
                "CPU EXECUTION",
                positions[1]
        );


        drawProcessCard(
                g,
                "INTERRUPTED",
                "PROCESS PAUSED",
                positions[2]
        );
    }


    private void drawInterruptSection(
            Graphics2D g) {

        drawSectionTitle(
                g,
                "INTERRUPT HANDLING",
                151
        );


        int[] positions =
                calculateInterruptPositions(
                        getWidth()
                );


        drawInterruptConnection(
                g,
                positions[0],
                positions[1],
                "RECEIVED",
                "SAVE CONTEXT"
        );


        drawInterruptConnection(
                g,
                positions[1],
                positions[2],
                "SAVE CONTEXT",
                "LOOKUP HANDLER"
        );


        drawInterruptConnection(
                g,
                positions[2],
                positions[3],
                "LOOKUP HANDLER",
                "ISR"
        );


        drawInterruptConnection(
                g,
                positions[3],
                positions[4],
                "ISR",
                "RESTORE CONTEXT"
        );


        drawInterruptCard(
                g,
                "RECEIVED",
                "INTERRUPT",
                positions[0]
        );


        drawInterruptCard(
                g,
                "SAVE CONTEXT",
                "PCB SNAPSHOT",
                positions[1]
        );


        drawInterruptCard(
                g,
                "LOOKUP HANDLER",
                "VECTOR TABLE",
                positions[2]
        );


        drawInterruptCard(
                g,
                "ISR",
                "HANDLER",
                positions[3]
        );


        drawInterruptCard(
                g,
                "RESTORE CONTEXT",
                "PCB RESTORE",
                positions[4]
        );
    }


    private void drawSectionTitle(
            Graphics2D g,
            String title,
            int y) {

        g.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        9
                )
        );


        g.setColor(MUTED);


        g.drawString(
                title,
                2,
                y
        );


        g.setColor(
                new Color(
                        43,
                        49,
                        46
                )
        );


        g.drawLine(
                2,
                y + 5,
                getWidth() - 2,
                y + 5
        );
    }


    /*
     * ============================================================
     * MAIN SEPARATOR
     * ============================================================
     *
     * อยู่ระหว่าง PROCESS STATE
     * และ INTERRUPT HANDLING
     *
     * ไม่มี center marker
     * ไม่มีเส้นที่เด่นตรงกลาง
     * สีเขียวเท่ากันตลอดแนว
     */
    private void drawMonitorDivider(
            Graphics2D g) {

        int y = 180;


        /*
         * Soft glow บาง ๆ
         */
        g.setColor(
                new Color(
                        83,
                        220,
                        151,
                        18
                )
        );


        g.setStroke(
                new BasicStroke(
                        3.0f
                )
        );


        g.drawLine(
                18,
                y,
                getWidth() - 18,
                y
        );


        /*
         * Main green separator
         */
        g.setColor(
                new Color(
                        83,
                        220,
                        151,
                        115
                )
        );


        g.setStroke(
                new BasicStroke(
                        1.2f
                )
        );


        g.drawLine(
                18,
                y,
                getWidth() - 18,
                y
        );
    }


    private int[] calculateProcessPositions(
            int width) {

        int[] positions =
                new int[3];


        int totalWidth =
                3 * PROCESS_CARD_WIDTH;


        int gap =
                (width - totalWidth - 40) / 2;


        gap =
                Math.max(
                        gap,
                        40
                );


        int total =
                totalWidth
                        + 2 * gap;


        int start =
                Math.max(
                        20,
                        (width - total) / 2
                );


        for (int i = 0;
             i < 3;
             i++) {

            positions[i] =
                    start
                            + i
                            * (
                            PROCESS_CARD_WIDTH
                                    + gap
                    );
        }


        return positions;
    }


    private int[] calculateInterruptPositions(
            int width) {

        int[] positions =
                new int[5];


        int totalWidth =
                5 * INTERRUPT_CARD_WIDTH;


        int gap =
                (width - totalWidth - 30) / 4;


        gap =
                Math.max(
                        gap,
                        8
                );


        int total =
                totalWidth
                        + 4 * gap;


        int start =
                Math.max(
                        10,
                        (width - total) / 2
                );


        for (int i = 0;
             i < 5;
             i++) {

            positions[i] =
                    start
                            + i
                            * (
                            INTERRUPT_CARD_WIDTH
                                    + gap
                    );
        }


        return positions;
    }


    private void drawProcessCard(
            Graphics2D g,
            String state,
            String subtitle,
            int x) {

        boolean active =
                state.equals(
                        displayedProcessState
                )
                        && processTransitionProgress >= 1.0;


        boolean target =
                state.equals(
                        targetProcessState
                )
                        && processTransitionProgress < 1.0;


        int y =
                PROCESS_Y;


        if (active || target) {

            drawGlow(
                    g,
                    x,
                    y,
                    PROCESS_CARD_WIDTH,
                    PROCESS_CARD_HEIGHT,
                    BLUE
            );
        }


        RoundRectangle2D card =
                new RoundRectangle2D.Double(
                        x,
                        y,
                        PROCESS_CARD_WIDTH,
                        PROCESS_CARD_HEIGHT,
                        6,
                        6
                );


        if (active) {

            g.setColor(
                    ACTIVE_CARD_BG
            );

        } else {

            g.setColor(
                    CARD_BG
            );
        }


        g.fill(card);


        g.setStroke(
                new BasicStroke(
                        active
                                ? 1.8f
                                : 1.0f
                )
        );


        g.setColor(
                active
                        ? BLUE
                        : CARD_BORDER
        );


        g.draw(card);


        g.setColor(
                active
                        ? ACCENT
                        : new Color(
                        100,
                        116,
                        139
                )
        );


        g.fillOval(
                x + 14,
                y + 16,
                7,
                7
        );


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        12
                )
        );


        g.setColor(
                active
                        ? Color.WHITE
                        : TEXT
        );


        g.drawString(
                state,
                x + 28,
                y + 23
        );


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        9
                )
        );


        g.setColor(
                active
                        ? new Color(
                        158,
                        205,
                        174
                )
                        : MUTED
        );


        g.drawString(
                subtitle,
                x + 14,
                y + 43
        );


        if (active) {

            g.setFont(
                    new Font(
                            "Monospaced",
                            Font.BOLD,
                            8
                    )
            );


            g.setColor(
                    BLUE_LIGHT
            );


            g.drawString(
                    "ACTIVE",
                    x + 14,
                    y + 58
            );
        }
    }


    private void drawInterruptCard(
            Graphics2D g,
            String phase,
            String subtitle,
            int x) {

        boolean active =
                phase.equals(
                        displayedInterruptPhase
                )
                        && interruptTransitionProgress >= 1.0;


        boolean target =
                phase.equals(
                        targetInterruptPhase
                )
                        && interruptTransitionProgress < 1.0;


        int y =
                INTERRUPT_Y;


        if (active || target) {

            drawGlow(
                    g,
                    x,
                    y,
                    INTERRUPT_CARD_WIDTH,
                    INTERRUPT_CARD_HEIGHT,
                    ACCENT
            );
        }


        RoundRectangle2D card =
                new RoundRectangle2D.Double(
                        x,
                        y,
                        INTERRUPT_CARD_WIDTH,
                        INTERRUPT_CARD_HEIGHT,
                        6,
                        6
                );


        if (active) {

            g.setColor(
                    new Color(
                            15,
                            24,
                            19
                    )
            );

        } else {

            g.setColor(
                    CARD_BG
            );
        }


        g.fill(card);


        g.setStroke(
                new BasicStroke(
                        active
                                ? 1.6f
                                : 1.0f
                )
        );


        g.setColor(
                active
                        ? ACCENT
                        : CARD_BORDER
        );


        g.draw(card);


        g.setColor(
                active
                        ? ACCENT
                        : new Color(
                        100,
                        116,
                        139
                )
        );


        g.fillOval(
                x + 12,
                y + 14,
                7,
                7
        );


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.BOLD,
                        10
                )
        );


        g.setColor(
                active
                        ? Color.WHITE
                        : TEXT
        );


        g.drawString(
                phase,
                x + 25,
                y + 21
        );


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        8
                )
        );


        g.setColor(
                active
                        ? new Color(
                        158,
                        205,
                        174
                )
                        : MUTED
        );


        g.drawString(
                subtitle,
                x + 12,
                y + 40
        );


        if (active) {

            g.setFont(
                    new Font(
                            "Monospaced",
                            Font.BOLD,
                            7
                    )
            );


            g.setColor(
                    BLUE_LIGHT
            );


            g.drawString(
                    "ACTIVE",
                    x + 12,
                    y + 53
            );
        }
    }


    private void drawProcessConnection(
            Graphics2D g,
            int startX,
            int endX,
            String from,
            String to) {

        int y =
                PROCESS_Y
                        + PROCESS_CARD_HEIGHT / 2;


        int lineStart =
                startX
                        + PROCESS_CARD_WIDTH
                        + 4;


        int lineEnd =
                endX - 8;


        boolean active =
                isProcessConnectionActive(
                        from,
                        to
                );


        g.setStroke(
                new BasicStroke(
                        2.0f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );


        g.setColor(
                active
                        ? new Color(
                        56,
                        189,
                        248,
                        120
                )
                        : LINE
        );


        g.drawLine(
                lineStart,
                y,
                lineEnd,
                y
        );


        drawArrow(
                g,
                lineEnd,
                y,
                active
                        ? BLUE_LIGHT
                        : new Color(
                        100,
                        116,
                        139
                )
        );


        if (active) {

            drawFlowPackets(
                    g,
                    lineStart,
                    lineEnd,
                    y,
                    processFlowOffset,
                    BLUE_LIGHT
            );
        }
    }


    /*
     * ============================================================
     * INTERRUPTED -> RUNNING
     * ============================================================
     */
    private void drawReturnConnection(
            Graphics2D g,
            int interruptedX,
            int runningX) {

        int startX =
                interruptedX
                        + PROCESS_CARD_WIDTH / 2;


        int endX =
                runningX
                        + PROCESS_CARD_WIDTH / 2;


        int y =
                PROCESS_Y
                        + PROCESS_CARD_HEIGHT
                        + 12;


        boolean active =
                currentSimulationState.equals(
                        "RESUMED"
                );


        g.setStroke(
                new BasicStroke(
                        active
                                ? 2.0f
                                : 1.5f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );


        g.setColor(
                active
                        ? new Color(
                        83,
                        220,
                        151,
                        150
                )
                        : LINE
        );


        /*
         * ลงจาก INTERRUPTED
         */
        g.drawLine(
                startX,
                PROCESS_Y
                        + PROCESS_CARD_HEIGHT,
                startX,
                y
        );


        /*
         * เส้นกลับจากขวา -> ซ้าย
         */
        g.drawLine(
                startX,
                y,
                endX,
                y
        );


        /*
         * ขึ้นเข้า RUNNING
         */
        g.drawLine(
                endX,
                y,
                endX,
                PROCESS_Y
                        + PROCESS_CARD_HEIGHT
                        + 8
        );


        /*
         * Arrow ชี้เข้า RUNNING
         */
        drawArrowUp(
                g,
                endX,
                PROCESS_Y
                        + PROCESS_CARD_HEIGHT
                        + 8,
                active
                        ? ACCENT
                        : new Color(
                        100,
                        116,
                        139
                )
        );


        if (active) {

            drawReturnFlowPackets(
                    g,
                    startX,
                    endX,
                    y,
                    processFlowOffset,
                    ACCENT
            );
        }
    }


    /*
     * Animated packets สำหรับ
     *
     * INTERRUPTED -> RUNNING
     */
    private void drawReturnFlowPackets(
            Graphics2D g,
            int startX,
            int endX,
            int y,
            double offset,
            Color color) {

        int length =
                Math.abs(
                        endX - startX
                );


        if (length <= 0) {

            return;
        }


        int spacing = 28;


        double cycle =
                length
                        + spacing;


        double animatedOffset =
                offset
                        % cycle;


        for (int i = 0;
             i < 3;
             i++) {

            double progress =
                    (
                            animatedOffset
                                    + i * spacing
                    )
                            / cycle;


            progress =
                    progress % 1.0;


            int x =
                    (int)
                            (
                                    startX
                                            + (
                                            endX - startX
                                    )
                                            * progress
                            );


            drawPacket(
                    g,
                    x,
                    y,
                    color
            );
        }
    }


    private void drawInterruptConnection(
            Graphics2D g,
            int startX,
            int endX,
            String from,
            String to) {

        int y =
                INTERRUPT_Y
                        + INTERRUPT_CARD_HEIGHT / 2;


        int lineStart =
                startX
                        + INTERRUPT_CARD_WIDTH
                        + 3;


        int lineEnd =
                endX - 7;


        boolean active =
                isInterruptConnectionActive(
                        from,
                        to
                );


        g.setStroke(
                new BasicStroke(
                        1.8f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );


        g.setColor(
                active
                        ? new Color(
                        83,
                        220,
                        151,
                        130
                )
                        : LINE
        );


        g.drawLine(
                lineStart,
                y,
                lineEnd,
                y
        );


        drawArrow(
                g,
                lineEnd,
                y,
                active
                        ? ACCENT
                        : new Color(
                        100,
                        116,
                        139
                )
        );


        if (active) {

            drawFlowPackets(
                    g,
                    lineStart,
                    lineEnd,
                    y,
                    interruptFlowOffset,
                    ACCENT
            );
        }
    }


    private boolean isProcessConnectionActive(
            String from,
            String to) {

        if (processTransitionProgress < 1.0) {

            return previousProcessState.equals(from)
                    && targetProcessState.equals(to);
        }


        if (currentSimulationState.equals(
                "INTERRUPT_RECEIVED"
        )
                || currentSimulationState.equals(
                "SAVING_CONTEXT"
        )
                || currentSimulationState.equals(
                "LOOKUP_HANDLER"
        )
                || currentSimulationState.equals(
                "ISR_EXECUTING"
        )
                || currentSimulationState.equals(
                "RESTORING_CONTEXT"
        )) {

            return from.equals("RUNNING")
                    && to.equals("INTERRUPTED");
        }


        return false;
    }


    private boolean isInterruptConnectionActive(
            String from,
            String to) {

        if (interruptTransitionProgress < 1.0) {

            return previousInterruptPhase.equals(
                    from
            )
                    && targetInterruptPhase.equals(
                    to
            );
        }


        if (currentInterruptPhase.equals(
                "RECEIVED"
        )) {

            return from.equals("RECEIVED")
                    && to.equals("SAVE CONTEXT");
        }


        if (currentInterruptPhase.equals(
                "SAVE CONTEXT"
        )) {

            return from.equals("SAVE CONTEXT")
                    && to.equals("LOOKUP HANDLER");
        }


        if (currentInterruptPhase.equals(
                "LOOKUP HANDLER"
        )) {

            return from.equals("LOOKUP HANDLER")
                    && to.equals("ISR");
        }


        if (currentInterruptPhase.equals(
                "ISR"
        )) {

            return from.equals("ISR")
                    && to.equals("RESTORE CONTEXT");
        }


        return false;
    }


    private void drawFlowPackets(
            Graphics2D g,
            int start,
            int end,
            int y,
            double offset,
            Color color) {

        int length =
                Math.max(
                        1,
                        end - start
                );


        int spacing = 28;


        double animatedOffset =
                offset
                        % (
                        length
                                + spacing
                );


        for (int i = 0;
             i < 3;
             i++) {

            int x =
                    start
                            + (int)
                            animatedOffset
                            - i * spacing;


            while (x < start) {

                x +=
                        length
                                + spacing;
            }


            while (x > end) {

                x -=
                        length
                                + spacing;
            }


            drawPacket(
                    g,
                    x,
                    y,
                    color
            );
        }
    }


    private void drawPacket(
            Graphics2D g,
            int x,
            int y,
            Color color) {

        int size = 6;


        Ellipse2D.Double packet =
                new Ellipse2D.Double(
                        x - size / 2.0,
                        y - size / 2.0,
                        size,
                        size
                );


        g.setColor(color);

        g.fill(packet);


        g.setColor(
                new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        60
                )
        );


        g.fillOval(
                x - 7,
                y - 7,
                14,
                14
        );
    }


    private void drawGlow(
            Graphics2D g,
            int x,
            int y,
            int width,
            int height,
            Color color) {

        int alpha =
                18
                        + (int)
                        (
                                10
                                        * (
                                        0.5
                                                + 0.5
                                                * Math.sin(
                                                pulse
                                        )
                                )
                        );


        g.setColor(
                new Color(
                        color.getRed(),
                        color.getGreen(),
                        color.getBlue(),
                        alpha
                )
        );


        g.fillRoundRect(
                x - 6,
                y - 6,
                width + 12,
                height + 12,
                8,
                8
        );
    }


    private void drawArrow(
            Graphics2D g,
            int x,
            int y,
            Color color) {

        g.setColor(color);


        g.setStroke(
                new BasicStroke(
                        2.0f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );


        g.drawLine(
                x - 6,
                y - 5,
                x,
                y
        );


        g.drawLine(
                x,
                y,
                x - 6,
                y + 5
        );
    }


    private void drawArrowUp(
            Graphics2D g,
            int x,
            int y,
            Color color) {

        g.setColor(color);


        g.setStroke(
                new BasicStroke(
                        1.8f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                )
        );


        /*
         * ลูกศรชี้ขึ้นเข้า RUNNING
         */
        g.drawLine(
                x,
                y,
                x,
                y - 8
        );


        g.drawLine(
                x,
                y - 8,
                x - 4,
                y - 3
        );


        g.drawLine(
                x,
                y - 8,
                x + 4,
                y - 3
        );
    }


    private void drawFooter(
            Graphics2D g) {

        String message;


        if (currentSimulationState.equals(
                "ISR_EXECUTING"
        )) {

            message =
                    "[SYSTEM] ISR executing";

        } else if (currentSimulationState.equals(
                "RESTORING_CONTEXT"
        )) {

            message =
                    "[SYSTEM] restoring process context";

        } else if (currentSimulationState.equals(
                "SAVING_CONTEXT"
        )) {

            message =
                    "[SYSTEM] saving CPU context";

        } else if (currentSimulationState.equals(
                "LOOKUP_HANDLER"
        )) {

            message =
                    "[SYSTEM] looking up interrupt vector";

        } else if (currentSimulationState.equals(
                "INTERRUPT_RECEIVED"
        )) {

            message =
                    "[SYSTEM] interrupt received";

        } else {

            message =
                    "[SYSTEM] process="
                            + currentProcessState;
        }


        g.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        9
                )
        );


        g.setColor(MUTED);


        g.drawString(
                message,
                2,
                getHeight() - 8
        );
    }
}