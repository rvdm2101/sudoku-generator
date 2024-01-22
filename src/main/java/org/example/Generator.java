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
    private static int TilesPerRow = 3;
    private static int TilesPerColumn = TilesPerRow;

    /**
     * An array of 9 items. Each item represents 1 3x3 sudoku tile.
     * [
     *   [
     *     1,2,3,
     *     4,5,6,
     *     7,8,9
     *   ],
     * ]
     */
    private List<List<Number>> sudokuGrid;

    private List<List<Number>> GenerateSudokuGrid() {
        List<List<Number>> grid = new ArrayList<>();
        for (int rowIndex = 0; rowIndex <= 8; rowIndex++) {
            grid.add(new ArrayList<Number>());
            for (int columnIndex = 0; columnIndex <= 8; columnIndex++) {
                grid.get(rowIndex).add(null);
            }
        }
        return grid;
    }

    /**
     * Initialze the sudoku grid with a 9 by 9 multidemential array. All array positions are filled with the value 'NULL'
     */
    private void InitialzeSudokuGrid() {
        sudokuGrid = GenerateSudokuGrid();
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

    private boolean shouldAppendDivider(int index) {
        return index == 2 || index == 5;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<List<Number>> rowsList = GenerateSudokuGrid();

        for (int tileIndex = 0; tileIndex < sudokuGrid.size(); tileIndex++) {
            List<Number> tile = sudokuGrid.get(tileIndex);
            int index = 0;
            int tileRow = tileIndex / TilesPerRow;
            // tileIndex 0,1,2 => tileRow 0
            // tileIndex 3,4,5 => tileRow 1
            // tileIndex 6,7,8 => tileRow 2
            int tileColumn = tileIndex % TilesPerColumn;
            // tileIndex 0,3,6 => tileColumn 0
            // tileIndex 1,4,7 => tileColumn 1
            // tileIndex 2,5,8 => tileColumn 2

            // tile 0 => array 0,1,2. Positions 0,1,2.
            // tile 1 => array 0,1,2. Positions 3,4,5.
            // tile 2 => array 0,1,2. Positions 6,7,8.
            // tile 3 => array 3,4,5. Positions 0,1,2.

            for (int rowLineIndex = tileRow * TilesPerRow; rowLineIndex < (tileRow + 1) * TilesPerRow; rowLineIndex++) {

                System.out.println(rowLineIndex);
                System.out.println(rowLineIndex * TilesPerRow);
                for (int columnLineIndex = tileColumn * TilesPerColumn; columnLineIndex < (tileColumn + 1) * TilesPerColumn; columnLineIndex++) {
                    // rowsList.get(rowLineIndex).add(columnLineIndex, tile.get((rowLineIndex * TilesPerRow) + columnLineIndex));
                    rowsList.get(rowLineIndex).add(columnLineIndex, tile.get(index));
                    index++;
                }
            }
        }

        // Loop over the different sudoku rows
        for (int rowIndex = 0; rowIndex < rowsList.size(); rowIndex++) {
            // Add the different values of the current row to the string builder
            for (int columnIndex = 0; columnIndex < rowsList.size(); columnIndex++) {
                builder.append(String.format(" %d ", rowsList.get(rowIndex).get(columnIndex)));

                // At two points, add a divider
                if (shouldAppendDivider(columnIndex)) {
                    builder.append(Generator.HorizontalDivider);
                }
            }
            
            builder.append("\n");
            // At two points, add a divider
            if (shouldAppendDivider(rowIndex)) {
                builder.append(Generator.VerticalDivider + "\n");
            }
        }

        return builder.toString();
    }
}
