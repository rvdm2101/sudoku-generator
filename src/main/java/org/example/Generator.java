package org.example;

import java.util.ArrayList;
import java.util.List;

public class Generator {
    private List<List<Number>> sudokuGrid;

    public void GenerateSudoku() {
        sudokuGrid = new ArrayList<>();

        for (int number = 0; number <= 8; number++) {
            sudokuGrid.add(new ArrayList<Number>());
            for (int numberx = 1; numberx <= 9; numberx++) {
                sudokuGrid.get(number).add(numberx);
            }
        }
    }

    private boolean shouldAddDivider(int index) {
        return index == 2 || index == 5;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        // Loop over the different sudoku rows
        for (int rowIndex = 0; rowIndex < sudokuGrid.size(); rowIndex++) {
            List<Number> row = sudokuGrid.get(rowIndex);

            // Add the different values of the current row to the string builder
            for (int cellIndex = 0; cellIndex < sudokuGrid.size(); cellIndex++) {
                builder.append(String.format(" %d ", row.get(cellIndex)));

                // At two points, add a divider
                if (shouldAddDivider(cellIndex)) {
                    builder.append("|");
                }
            }
            
            builder.append("\n");
            // At two points, add a divider
            if (shouldAddDivider(rowIndex)) {
                builder.append("--------- --------- ---------");
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}
