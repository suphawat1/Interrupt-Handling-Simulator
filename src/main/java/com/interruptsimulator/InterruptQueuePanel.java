package com.interruptsimulator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InterruptQueuePanel extends JPanel {

    private JPanel cardsPanel;

    private JScrollPane scrollPane;

    /*
     * Dark neutral colors
     */
    private static final Color PANEL_BACKGROUND =
            new Color(32, 32, 32);

    private static final Color CARD_BACKGROUND =
            new Color(45, 45, 45);

    private static final Color BORDER_COLOR =
            new Color(75, 75, 75);

    private static final Color PRIMARY_TEXT =
            new Color(220, 220, 220);

    private static final Color SECONDARY_TEXT =
            new Color(165, 165, 165);

    /*
     * Priority colors
     *
     * Priority 1 = high
     * Priority 2 = medium
     * Priority 3 = low
     */
    private static final Color PRIORITY_HIGH =
            new Color(180, 60, 60);

    private static final Color PRIORITY_MEDIUM =
            new Color(190, 150, 55);

    private static final Color PRIORITY_LOW =
            new Color(90, 120, 140);

    public InterruptQueuePanel() {

        setLayout(
                new BorderLayout()
        );

        setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        "INTERRUPT QUEUE"
                )
        );

        setBackground(
                PANEL_BACKGROUND
        );

        /*
         * --------------------------------------------------------
         * Cards panel
         * --------------------------------------------------------
         */

        cardsPanel =
                new JPanel();

        cardsPanel.setLayout(
                new BoxLayout(
                        cardsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        cardsPanel.setBackground(
                PANEL_BACKGROUND
        );

        /*
         * --------------------------------------------------------
         * Scroll pane
         * --------------------------------------------------------
         */

        scrollPane =
                new JScrollPane(
                        cardsPanel
                );

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.setBackground(
                PANEL_BACKGROUND
        );

        scrollPane.getViewport().setBackground(
                PANEL_BACKGROUND
        );

        /*
         * Always show vertical scrollbar only
         * when the queue becomes too long.
         */
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        setPreferredSize(
                new Dimension(
                        230,
                        250
                )
        );
    }

    public void updateQueue(
            InterruptQueue queue) {

        cardsPanel.removeAll();

        if (queue == null
                || queue.isEmpty()) {

            JLabel emptyLabel =
                    new JLabel(
                            "Queue is empty"
                    );

            emptyLabel.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            13
                    )
            );

            emptyLabel.setForeground(
                    SECONDARY_TEXT
            );

            emptyLabel.setBorder(
                    BorderFactory.createEmptyBorder(
                            8,
                            8,
                            8,
                            8
                    )
            );

            cardsPanel.add(
                    emptyLabel
            );

        } else {

            List<Interrupt> interrupts =
                    new ArrayList<>();

            /*
             * PriorityQueue iterator does not
             * guarantee sorted order.
             *
             * Therefore, copy the interrupts
             * and sort them before displaying.
             */
            for (Interrupt interrupt :
                    queue.getAllInterrupts()) {

                interrupts.add(
                        interrupt
                );
            }

            interrupts.sort(
                    Comparator
                            .comparingInt(
                                    Interrupt::getPriority
                            )
                            .thenComparingInt(
                                    Interrupt::getInterruptId
                            )
            );

            for (Interrupt interrupt :
                    interrupts) {

                cardsPanel.add(
                        createInterruptCard(
                                interrupt
                        )
                );

                cardsPanel.add(
                        Box.createVerticalStrut(
                                6
                        )
                );
            }
        }

        cardsPanel.revalidate();

        cardsPanel.repaint();

        /*
         * Keep the scroll position at the top
         * when the queue is refreshed.
         */
        if (scrollPane != null) {

            scrollPane.getVerticalScrollBar()
                    .setValue(0);
        }
    }

    private JPanel createInterruptCard(
            Interrupt interrupt) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                CARD_BACKGROUND
        );

        /*
         * --------------------------------------------------------
         * Card border
         * --------------------------------------------------------
         */

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER_COLOR
                        ),
                        BorderFactory.createEmptyBorder(
                                6,
                                8,
                                6,
                                8
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        70
                )
        );

        /*
         * --------------------------------------------------------
         * Priority indicator
         * --------------------------------------------------------
         *
         * Only a small strip uses the priority color.
         * The entire card stays neutral.
         */

        JPanel priorityIndicator =
                new JPanel();

        priorityIndicator.setBackground(
                getPriorityColor(
                        interrupt.getPriority()
                )
        );

        priorityIndicator.setPreferredSize(
                new Dimension(
                        5,
                        50
                )
        );

        card.add(
                priorityIndicator,
                BorderLayout.WEST
        );

        /*
         * --------------------------------------------------------
         * Information area
         * --------------------------------------------------------
         */

        JPanel informationPanel =
                new JPanel();

        informationPanel.setLayout(
                new BoxLayout(
                        informationPanel,
                        BoxLayout.Y_AXIS
                )
        );

        informationPanel.setBackground(
                CARD_BACKGROUND
        );

        informationPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        0,
                        8,
                        0,
                        0
                )
        );

        /*
         * Interrupt type
         */

        JLabel typeLabel =
                new JLabel(
                        "INTERRUPT: "
                        + interrupt.getType()
                );

        typeLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        13
                )
        );

        typeLabel.setForeground(
                PRIMARY_TEXT
        );

        /*
         * Priority
         */

        JLabel priorityLabel =
                new JLabel(
                        "Priority: "
                        + interrupt.getPriority()
                );

        priorityLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        priorityLabel.setForeground(
                SECONDARY_TEXT
        );

        /*
         * Interrupt ID
         */

        JLabel idLabel =
                new JLabel(
                        "Interrupt ID: "
                        + interrupt.getInterruptId()
                );

        idLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        12
                )
        );

        idLabel.setForeground(
                SECONDARY_TEXT
        );

        informationPanel.add(
                typeLabel
        );

        informationPanel.add(
                priorityLabel
        );

        informationPanel.add(
                idLabel
        );

        card.add(
                informationPanel,
                BorderLayout.CENTER
        );

        return card;
    }

    private Color getPriorityColor(
            int priority) {

        if (priority == 1) {

            return PRIORITY_HIGH;
        }

        if (priority == 2) {

            return PRIORITY_MEDIUM;
        }

        return PRIORITY_LOW;
    }
}