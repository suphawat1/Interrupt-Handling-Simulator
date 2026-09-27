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

    public class StateDiagramPanel extends JPanel {

        private String currentState = "READY";
        private String displayedState = "READY";
        private String targetState = "READY";

        private Timer animationTimer;

        private double animationProgress = 1.0;

        // ใช้สำหรับทำให้ > เคลื่อนที่ตลอดเวลา
        private double chevronOffset = 0.0;

        private static final double ANIMATION_SPEED = 0.015;
        private static final int ANIMATION_DELAY = 25;

        public StateDiagramPanel() {

            setBorder(
                    BorderFactory.createTitledBorder(
                            "PROCESS STATE DIAGRAM"
                    )
            );

            setBackground(Color.WHITE);

            setPreferredSize(
                    new Dimension(800, 180)
            );
        }

        /**
         * เปลี่ยนสถานะของ Process
         */
        public void setState(String state) {

            if (state == null) {
                return;
            }

            currentState = state;

            String newTargetState =
                    getVisualState(state);

            /*
            * ถ้า State ที่แสดงอยู่ยังเป็น State เดิม
            * ไม่ต้องเริ่ม animation ใหม่
            */
            if (newTargetState.equals(targetState)) {
                repaint();
                return;
            }

            /*
            * หยุด animation เดิม
            */
            if (animationTimer != null
                    && animationTimer.isRunning()) {

                animationTimer.stop();
                animationTimer = null;
            }

            targetState = newTargetState;

            /*
            * เริ่ม transition ใหม่
            */
            animationProgress = 0.0;

            /*
            * เริ่มตำแหน่ง > ใหม่
            */
            chevronOffset = 0.0;

            startAnimation();
        }

        /**
         * เริ่ม Animation
         */
        private void startAnimation() {

            animationTimer =
                    new Timer(
                            ANIMATION_DELAY,
                            e -> updateAnimation()
                    );

            animationTimer.start();
        }

        /**
         * อัปเดต Animation
         */
        private void updateAnimation() {

            /*
            * ค่อย ๆ เดินจาก State เก่า
            * ไป State ใหม่
            */
            if (animationProgress < 1.0) {

                animationProgress +=
                        ANIMATION_SPEED;

                if (animationProgress >= 1.0) {

                    animationProgress = 1.0;

                    displayedState =
                            targetState;
                }
            }

            /*
            * ------------------------------------------------
            * ทำให้เครื่องหมาย > เคลื่อนที่ตลอดเวลา
            * ------------------------------------------------
            *
            * ไม่ผูกกับ animationProgress
            *
            * ดังนั้นแม้ transition จะไปถึง 100%
            * แล้ว > ก็ยังวิ่งต่อ
            */
            chevronOffset += 2.0;

            /*
            * ถ้าวิ่งถึงปลายแล้ว
            * ให้กลับไปเริ่มต้นใหม่
            */
            int distance = 150;

            if (chevronOffset >= distance) {
                chevronOffset = 0.0;
            }

            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {

            super.paintComponent(graphics);

            Graphics2D g =
                    (Graphics2D) graphics;

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            /*
            * ------------------------------------------------
            * ตำแหน่ง State
            * ------------------------------------------------
            */

            int y = 75;

            int readyX = 70;
            int runningX = 230;
            int interruptedX = 420;
            int isrX = 610;
            int resumedX = 780;

            /*
            * ------------------------------------------------
            * วาด State
            * ------------------------------------------------
            */

            drawState(
                    g,
                    "READY",
                    readyX,
                    y
            );

            drawState(
                    g,
                    "RUNNING",
                    runningX,
                    y
            );

            drawState(
                    g,
                    "INTERRUPTED",
                    interruptedX,
                    y
            );

            drawState(
                    g,
                    "ISR",
                    isrX,
                    y
            );

            drawState(
                    g,
                    "RESUMED",
                    resumedX,
                    y
            );

            /*
            * ------------------------------------------------
            * วาด Transition
            * ------------------------------------------------
            */

            // READY → RUNNING
            drawTransition(
                    g,
                    readyX + 110,
                    runningX,
                    y + 25,
                    "READY",
                    "RUNNING"
            );

            // RUNNING → INTERRUPTED
            drawTransition(
                    g,
                    runningX + 110,
                    interruptedX,
                    y + 25,
                    "RUNNING",
                    "INTERRUPTED"
            );

            // INTERRUPTED → ISR
            drawTransition(
                    g,
                    interruptedX + 110,
                    isrX,
                    y + 25,
                    "INTERRUPTED",
                    "ISR"
            );

            // ISR → RESUMED
            drawTransition(
                    g,
                    isrX + 110,
                    resumedX,
                    y + 25,
                    "ISR",
                    "RESUMED"
            );

            /*
            * ------------------------------------------------
            * คำอธิบายด้านล่าง
            * ------------------------------------------------
            */

            g.setColor(Color.DARK_GRAY);

            g.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            11
                    )
            );

            g.drawString(
                    "CPU",
                    runningX + 40,
                    125
            );

            g.drawString(
                    "Interrupt",
                    interruptedX + 20,
                    125
            );

            g.drawString(
                    "Handler",
                    isrX + 20,
                    125
            );

            g.drawString(
                    "Return",
                    resumedX + 20,
                    125
            );
        }

        /**
         * วาด State แต่ละกล่อง
         */
        private void drawState(
                Graphics2D g,
                String state,
                int x,
                int y) {

            boolean active =
                    state.equals(displayedState)
                            && animationProgress >= 1.0;

            /*
            * ISR ต้องเป็นสีฟ้า
            * ระหว่าง ISR_EXECUTING
            * และ RESTORING_CONTEXT
            */
            if (state.equals("ISR")
                    && (currentState.equals(
                            "ISR_EXECUTING")
                    || currentState.equals(
                            "RESTORING_CONTEXT"))) {

                active = true;
            }

            /*
            * RESUMED ต้องเป็นสีฟ้า
            * ตอน Process กำลัง Resume
            */
            if (state.equals("RESUMED")
                    && currentState.equals(
                            "RESUMED")) {

                active = true;
            }

            /*
            * สีของ State
            */
            if (active) {

                g.setColor(
                        new Color(
                                80,
                                160,
                                220
                        )
                );

            } else {

                g.setColor(
                        new Color(
                                220,
                                220,
                                220
                        )
                );
            }

            /*
            * กล่อง State
            */
            g.fillRoundRect(
                    x,
                    y,
                    110,
                    50,
                    15,
                    15
            );

            /*
            * ขอบกล่อง
            */
            g.setColor(Color.DARK_GRAY);

            g.setStroke(
                    new BasicStroke(2)
            );

            g.drawRoundRect(
                    x,
                    y,
                    110,
                    50,
                    15,
                    15
            );

            /*
            * ข้อความ State
            */
            g.setColor(Color.BLACK);

            g.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            12
                    )
            );

            int textWidth =
                    g.getFontMetrics()
                            .stringWidth(state);

            g.drawString(
                    state,
                    x + (110 - textWidth) / 2,
                    y + 30
            );
        }

        /**
         * วาดเส้น Transition
         */
        private void drawTransition(
                Graphics2D g,
                int startX,
                int endX,
                int y,
                String from,
                String to) {

            /*
            * Transition นี้เป็น Transition
            * ที่กำลังทำงานอยู่หรือไม่
            */
            boolean active;

if ((from.equals("READY") && to.equals("RUNNING"))
        || (from.equals("RUNNING") && to.equals("INTERRUPTED"))) {

    active = targetState.equals(to)
            && animationProgress < 1.0;

} else {
    active = targetState.equals(to);
}

            /*
            * ------------------------------------------------
            * เส้นพื้นฐานสีเทา
            * ------------------------------------------------
            */

            g.setColor(
                    new Color(
                            220,
                            220,
                            220
                    )
            );

            g.setStroke(
                    new BasicStroke(
                            5,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            g.drawLine(
                    startX + 5,
                    y,
                    endX - 5,
                    y
            );

            /*
            * ------------------------------------------------
            * Transition ที่กำลังทำงาน
            * ------------------------------------------------
            */

            if (active) {

                /*
                * เส้นสีน้ำเงิน
                */
                g.setColor(
                        new Color(
                                80,
                                160,
                                220
                        )
                );

                g.setStroke(
                        new BasicStroke(
                                5,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        )
                );

                g.drawLine(
                        startX + 5,
                        y,
                        endX - 5,
                        y
                );

                /*
                * > > > >
                *
                * เคลื่อนที่ตลอดเวลา
                */
                drawMovingChevrons(
                        g,
                        startX,
                        endX,
                        y
                );
            }
        }

        /**
         * วาดเครื่องหมาย > ที่เคลื่อนที่
         */
        private void drawMovingChevrons(
                Graphics2D g,
                int startX,
                int endX,
                int y) {

            int distance =
                    endX - startX;

            int spacing = 20;

            int chevronCount = 4;

            /*
            * วาด > หลายตัว
            */
            for (int i = 0;
                i < chevronCount;
                i++) {

                int x =
                        startX
                        + (int) chevronOffset
                        - (i * spacing);

                /*
                * ถ้าออกทางซ้าย
                * ให้กลับไปทางขวา
                */
                while (x < startX) {

                    x += distance;
                }

                /*
                * ถ้าออกทางขวา
                * ให้กลับไปทางซ้าย
                */
                while (x > endX) {

                    x -= distance;
                }

                drawChevron(
                        g,
                        x,
                        y
                );
            }
        }

        /**
         * วาดเครื่องหมาย >
         */
        private void drawChevron(
                Graphics2D g,
                int x,
                int y) {

            int size = 7;

            g.setColor(
                    new Color(
                            50,
                            120,
                            200
                    )
            );

            g.setStroke(
                    new BasicStroke(
                            3,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );

            /*
            * เส้นบนของ >
            */
            g.drawLine(
                    x,
                    y - size,
                    x + size,
                    y
            );

            /*
            * เส้นล่างของ >
            */
            g.drawLine(
                    x + size,
                    y,
                    x,
                    y + size
            );
        }

        /**
         * แปลง SimulationState
         * ให้เป็น State ที่แสดงใน Diagram
         */
        private String getVisualState(
                String state) {

            if (state.equals(
                    "SAVING_CONTEXT")) {

                return "INTERRUPTED";
            }

            if (state.equals(
                    "LOOKUP_HANDLER")) {

                return "INTERRUPTED";
            }

            if (state.equals(
                    "INTERRUPT_RECEIVED")) {

                return "INTERRUPTED";
            }

            if (state.equals(
                    "ISR_EXECUTING")) {

                return "ISR";
            }

            /*
            * สำคัญ:
            * RESTORING_CONTEXT
            * กำลังเปลี่ยนจาก ISR → RESUMED
            */
            if (state.equals(
                    "RESTORING_CONTEXT")) {

                return "RESUMED";
            }

            if (state.equals(
                    "RUNNING")) {

                return "RUNNING";
            }

            if (state.equals(
                    "RESUMED")) {

                return "RESUMED";
            }

            if (state.equals(
                    "INTERRUPTED")) {

                return "INTERRUPTED";
            }

            if (state.equals(
                    "READY")) {

                return "READY";
            }

            return state;
        }
    }