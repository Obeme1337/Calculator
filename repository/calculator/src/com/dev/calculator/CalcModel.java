package com.dev.calculator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CalcModel {
    public enum Operation {
        NONE, ADD, SUB, MUL, DIV
    }
    
    private Operation pendingOp = Operation.NONE;
    private double previousValue = 0;
    private double currentValue = 0;
    private String displayValue = "0";
    
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    
    public String getValue() {
        return displayValue;
    }
    
    public void addDigit(String s) {
        char c = s.charAt(0);
        addDigit(c);
    }
    
    public void addDigit(char c) {
        if (displayValue.equals("0") || !isNumeric(displayValue.replace(".", ""))) {
            displayValue = String.valueOf(c);
        } else {
            displayValue += c;
        }
        currentValue = Double.parseDouble(displayValue);
        notifyDisplayChange();
    }
    
    public void setOperation(Operation op) {
        if (pendingOp != Operation.NONE) {
            calculate();
        }
        previousValue = currentValue;
        this.pendingOp = op;
        displayValue = "0";
        currentValue = 0;
    }
    
    public void setDisplay(String value) {
        String oldValue = this.displayValue;
        this.displayValue = value;
        propertyChangeSupport.firePropertyChange("value", oldValue, this.displayValue);
    }
    
    public void clear() {
        this.pendingOp = Operation.NONE;
        previousValue = 0;
        currentValue = 0;
        setDisplay("0");
    }
    
    public void calculate() {
        switch (pendingOp) {
            case ADD:
                setDisplay(removeTrailingZeros(previousValue + currentValue));
                break;
            case SUB:
                setDisplay(removeTrailingZeros(previousValue - currentValue));
                break;
            case MUL:
                setDisplay(removeTrailingZeros(previousValue * currentValue));
                break;
            case DIV:
                if (currentValue == 0) {
                    setDisplay("Error");
                } else {
                    setDisplay(removeTrailingZeros(previousValue / currentValue));
                }
                break;
            case NONE:
                break;
        }
        pendingOp = Operation.NONE;
        previousValue = Double.parseDouble(displayValue.equals("Error") ? "0" : displayValue);
        currentValue = 0;
    }
    
    private String removeTrailingZeros(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        }
        return String.format("%s", value).replaceAll("0*$", "").replaceAll("\\.$", "");
    }
    
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private void notifyDisplayChange() {
        propertyChangeSupport.firePropertyChange("value", null, displayValue);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}