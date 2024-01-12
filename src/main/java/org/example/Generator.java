package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Generator {
    private static String HorizontalDivider = "|";
    private static String VerticalDivider = "--------- --------- ---------";

    private List<List<Number>> sudokuGrid;

    /**
     * Initialze the sudoku grid with a 9 by 9 multidemential array. All array positions are filled with the value 'NULL'
     */
    private void InitialzeSudokuGrid() {
        sudokuGrid = new ArrayList<>();
        for (int rowIndex = 0; rowIndex <= 8; rowIndex++) {
            sudokuGrid.add(new ArrayList<Number>());
            for (int columnIndex = 0; columnIndex <= 8; columnIndex++) {
                sudokuGrid.get(rowIndex).add(null);
            }
        }
    }

    private List<Number> GetAvailableRowColumns(List<Number> row) {
        List<Number> AvailableColumns = new ArrayList<>();

        for (int columnIndex = 0; columnIndex <= 8; columnIndex++) {
            if (row.get(columnIndex) == null) {
                AvailableColumns.add(columnIndex);
            }
        }

        return AvailableColumns;
    }

    private List<Number> Intersection(List<Number> list1, List<Number> list2) {
        return list1.stream().distinct().filter(list2::contains).collect(Collectors.toList());
    }

    public void GenerateSudoku() {
        InitialzeSudokuGrid();

        // 1. Get current number
        // 2. Get available places per row
        Random random = new Random();
        int number = 1;
        List<Number> availableChoices = new LinkedList<Number>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        for (int rowIndex = 0; rowIndex <= 8; rowIndex++) {
            // List<Number> availableColumns = GetAvailableRowColumns(sudokuGrid.get(rowIndex));
            // List<Number> intersectedColumns = Intersection(availableColumns, availableChoices);

            int randomIndex = random.nextInt(availableChoices.size());
            int chosenIndex = availableChoices.get(randomIndex).intValue();

            // Place the number in the sudoku grid, and remove the position from the available list
            sudokuGrid.get(rowIndex).add(chosenIndex, number);
            availableChoices.remove(randomIndex);
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
            for (int columnIndex = 0; columnIndex < sudokuGrid.size(); columnIndex++) {
                builder.append(String.format(" %d ", row.get(columnIndex)));

                // At two points, add a divider
                if (shouldAddDivider(columnIndex)) {
                    builder.append(Generator.HorizontalDivider);
                }
            }
            
            builder.append("\n");
            // At two points, add a divider
            if (shouldAddDivider(rowIndex)) {
                builder.append(Generator.VerticalDivider);
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}
