package com.interruptsimulator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InterruptQueuePanel extends JPanel {

    private JPanel cardsPanel;

    public InterruptQueuePanel() {

        setLayout(
                new BoxLayout(
                        this,
                        BoxLayout.Y_AXIS
                )
        );

        setBorder(
                BorderFactory.createTitledBorder(
                        "INTERRUPT QUEUE"
                )
        );

        setBackground(Color.WHITE);

        cardsPanel =
                new JPanel();

        cardsPanel.setLayout(
                new BoxLayout(
                        cardsPanel,
                        BoxLayout.Y_AXIS
                )
        );

        cardsPanel.setBackground(
                Color.WHITE
        );

        add(cardsPanel);

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
                            Font.ITALIC,
                            13
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

                interrupts.add(interrupt);
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
            }
        }

        cardsPanel.revalidate();

        cardsPanel.repaint();
    }

    private JPanel createInterruptCard(
            Interrupt interrupt) {

        JPanel card =
                new JPanel();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.GRAY
                        ),
                        BorderFactory.createEmptyBorder(
                                6,
                                8,
                                6,
                                8
                        )
                )
        );

        card.setBackground(
                getPriorityColor(
                        interrupt.getPriority()
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        75
                )
        );

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

        JLabel priorityLabel =
                new JLabel(
                        "Priority: "
                        + interrupt.getPriority()
                );

        JLabel idLabel =
                new JLabel(
                        "Interrupt ID: "
                        + interrupt.getInterruptId()
                );

        card.add(typeLabel);
        card.add(priorityLabel);
        card.add(idLabel);

        return card;
    }

    private Color getPriorityColor(
            int priority) {

        if (priority == 1) {

            return new Color(
                    255,
                    220,
                    220
            );
        }

        if (priority == 2) {

            return new Color(
                    255,
                    245,
                    200
            );
        }

        return new Color(
                220,
                235,
                255
        );
    }
}