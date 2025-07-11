package com.dev.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CalcPanel extends JPanel implements ActionListener, PropertyChangeListener {
    private static final long serialVersionUID = 1L;
    private final CalcModel model;
    private final JLabel display = new JLabel("0", SwingConstants.RIGHT);

    public CalcPanel(CalcModel model) {
        super(new BorderLayout());
        this.model = model;
        model.addPropertyChangeListener(this);
        init();
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.addActionListener(this);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        return button;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        panel.add(createButton("7"));
        panel.add(createButton("8"));
        panel.add(createButton("9"));
        panel.add(createButton("/"));

        panel.add(createButton("4"));
        panel.add(createButton("5"));
        panel.add(createButton("6"));
        panel.add(createButton("*"));

        panel.add(createButton("1"));
        panel.add(createButton("2"));
        panel.add(createButton("3"));
        panel.add(createButton("-"));

        panel.add(createButton("C"));
        panel.add(createButton("0"));
        panel.add(createButton("="));
        panel.add(createButton("+"));

        return panel;
    }

    private void init() {
        display.setFont(new Font("Arial", Font.BOLD, 24));
        display.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        display.setOpaque(true);
        display.setBackground(Color.WHITE);

        this.add(display, BorderLayout.NORTH);
        this.add(createButtonPanel(), BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        String command = evt.getActionCommand();
        if (command.isEmpty()) return;

        try {
            switch (command.charAt(0)) {
                case '+':
                    model.setOperation(CalcModel.Operation.ADD);
                    break;
                case '-':
                    model.setOperation(CalcModel.Operation.SUB);
                    break;
                case '*':
                    model.setOperation(CalcModel.Operation.MUL);
                    break;
                case '/':
                    model.setOperation(CalcModel.Operation.DIV);
                    break;
                case '=':
                    model.calculate();
                    break;
                case 'C':
                    model.clear();
                    break;
                default:
                    model.addDigit(command);
                    break;
            }
        } catch (NumberFormatException ex) {
            display.setText("Error");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("value".equals(evt.getPropertyName())) {
            display.setText(model.getValue());
        }
    }
}