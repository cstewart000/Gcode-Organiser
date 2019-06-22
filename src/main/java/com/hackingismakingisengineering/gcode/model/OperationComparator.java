package com.hackingismakingisengineering.gcode.model;

import java.util.Comparator;

/**
 * Compares operations based on tool number
 */
public class OperationComparator implements Comparator<Operation> {
    @Override
    public int compare(Operation operation1, Operation operation2) {
        return operation1.getTool().getTool_code() - operation2.getTool().getTool_code();
}
}