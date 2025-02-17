package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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

    private enum Orientation {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * Create a sudoku tile. All positions are filled with the value 'NULL'.
     * The array represents a sudoku 3x3 tile
     */
    private List<Number> generateSudokTile(Integer... defaultValues) {
        List<Number> tile = new ArrayList<Number>();
        for (int cellIndex = 0; cellIndex <= 8; cellIndex++) {
            tile.add(defaultValues.length > cellIndex ? defaultValues[cellIndex] : null);
        }
        return tile;
    }

    /**
     * Create a sudoku grid with a 9 by 9 multidemential array. All array positions are filled with the value 'NULL'.
     * Each array represents a sudoku 3x3 tile
     */
    private List<List<Number>> generateSudokuGrid(Integer... defaultValues) {
        List<List<Number>> grid = new ArrayList<>();
        for (int tileIndex = 0; tileIndex <= 8; tileIndex++) {
            grid.add(generateSudokTile(defaultValues));
        }
        return grid;
    }

    private Integer getRowIndex(Integer cellIndex) {
        // Determine in which row the cellIndex will be placed
        int rowIndex = (int) Math.floor(cellIndex / TilesPerRow);
        // rowIndex 0 > tiles 0,1,2
        // rowIndex 1 > tiles 3,4,5
        // rowIndex 2 > tiles 6,7,8
        return rowIndex;
    }

    private List<Number> getNumbersFromRowIndex(Integer rowIndex, List<Number> tile) {
        int firstTileIndex = rowIndex * TilesPerRow;
        return Arrays.asList(tile.get(firstTileIndex), tile.get(firstTileIndex + 1), tile.get(firstTileIndex + 2));
    }

    private List<Number> getNumbersFromColumnIndex(Integer columnIndex, List<Number> tile) {
        return Arrays.asList(tile.get(columnIndex), tile.get(columnIndex + TilesPerColumn), tile.get(columnIndex + (TilesPerColumn * 2)));
    }

    private void removeNumbersInARowFromAvailableTileArrangements(
        Integer rowIndex,
        List<Number> numbersToRemove,
        List<List<Number>> AvailableTileArrangements
    ) {
        int firstTileIndex = rowIndex * TilesPerRow;
        int[] indexes = {firstTileIndex, firstTileIndex + 1, firstTileIndex + 2};

        // Get the filled in tiles in that row
        for (int index : indexes) {
            AvailableTileArrangements.get(index).removeAll(numbersToRemove);
        }
    }

    private void removeNumbersInAColumnFromAvailableTileArrangements(
        Integer columnIndex,
        List<Number> numbersToRemove,
        List<List<Number>> AvailableTileArrangements
    ) {
        int[] indexes = {columnIndex, columnIndex + TilesPerColumn, columnIndex + (TilesPerColumn * 2)};

        for (int index : indexes) {
            AvailableTileArrangements.get(index).removeAll(numbersToRemove);
        }
    }

    private List<List<Number>> findAvailableTileArrangements(
        List<List<Number>> horizontalTiles,
        List<List<Number>> verticalTiles
    ) {
        // Generate a prefilled tile
        List<List<Number>> availableTileArrangements = generateSudokuGrid(1,2,3,4,5,6,7,8,9);

        // Remove all the numbers already used in other tiles next to this tile
        for (int index = 0; index < horizontalTiles.size(); index++) {
            for (int rowIndex = 0; rowIndex <= 2; rowIndex++) {
                removeNumbersInARowFromAvailableTileArrangements(
                    rowIndex,
                    getNumbersFromRowIndex(rowIndex, horizontalTiles.get(index)),
                    availableTileArrangements
                );
            }
        }

        // Remove all the numbers already used in other tiles above and below this tile
        for (int index = 0; index < verticalTiles.size(); index++) {
            for (int columnIndex = 0; columnIndex <= 2; columnIndex++) {
                removeNumbersInAColumnFromAvailableTileArrangements(
                    columnIndex,
                    getNumbersFromColumnIndex(columnIndex, verticalTiles.get(index)),
                    availableTileArrangements
                );
            }
        }

        return availableTileArrangements;
    }

    /**
     * Get the existingTiles for a specific row or column
     * 
     * @param currentTileIndex the index of the current tile
     * @param sudokuGrid the current filled in sudokuGrid
     * @param orientation get the tiles for a row or column
     * 
     * @return the filled in tiles in the current row or column
     */
    private List<List<Number>> getExistingTiles(int currentTileIndex, List<List<Number>> sudokuGrid, Orientation orientation) {
        List<List<Number>> existingTiles = new ArrayList<>();
        if (orientation == Orientation.HORIZONTAL) {
            // Determine in which row the currentTileIndex will be placed
            int rowIndex = getRowIndex(currentTileIndex);
            
            // Get the other tiles in that row
            int firstTileIndex = rowIndex * TilesPerRow;
            int[] indexes = {firstTileIndex, firstTileIndex + 1, firstTileIndex + 2};

            // Get the filled in tiles in that row
            for (int index : indexes) {
                if (index == currentTileIndex || index >= sudokuGrid.size()) {
                    continue;
                }
                existingTiles.add(sudokuGrid.get(index));
            }
            return existingTiles;
        }

        // Determine in which column the currentTileIndex will be placed
        int columnIndex = currentTileIndex % TilesPerColumn;
        // columnIndex 0 > tiles 0,3,6
        // columnIndex 1 > tiles 1,4,7
        // columnIndex 2 > tiles 2,5,8

        // Get the other tiles in that column
        int[] indexes = {columnIndex, columnIndex + TilesPerColumn, columnIndex + (TilesPerColumn * 2)};

        // Get the filled in tiles in that row
        for (int index : indexes) {
            if (index == currentTileIndex || index >= sudokuGrid.size()) {
                continue;
            }
            existingTiles.add(sudokuGrid.get(index));
        }

        return existingTiles;
    }

    private Number getLeastRepresentedNumber(Integer currentIndex, List<List<Number>> dataSet) {
        List<Number> representedNumbers = dataSet.get(currentIndex);
        Map<Number, Integer> pairsMap = new LinkedHashMap<>();

        for (int tileIndex = currentIndex; tileIndex < dataSet.size(); tileIndex++) {
            List<Number> data = dataSet.get(tileIndex);
            for (Number representedNumber : representedNumbers) {
                if (data.contains(representedNumber)) {
                    int amount = pairsMap.getOrDefault(representedNumber, 0);
                    pairsMap.put(representedNumber, amount + 1);
                }
            }
        }

        Optional<Map.Entry<Number, Integer>> lowest = pairsMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue((entry1, entry2) -> Integer.compare(entry1, entry2)))
            .findFirst();
        // System.out.println(pairsMap.entrySet());
        Number newNumber = lowest.isPresent() ? lowest.get().getKey() : null;
        // System.out.println(newNumber);
        return newNumber;
    }

    private void removeNumberFromDataSet(Number numberToRemove, List<List<Number>> dataSet) {
        for (List<Number> data : dataSet) {
            data.remove(numberToRemove);
        }
    }

    private List<Number> getNewTileArrangement(List<List<Number>> availableTileArrangements) {
        List<Number> newTileArragement = Arrays.asList(null, null, null, null, null, null, null, null, null);
        System.out.println(availableTileArrangements);

        // Pre-fill all tile postions that only have 1 option
        for (int tileIndex = 0; tileIndex <= 8; tileIndex++) {
            List<Number> availableTileArrangement = availableTileArrangements.get(tileIndex);
            if (availableTileArrangement.size() == 1) {
                newTileArragement.set(tileIndex, availableTileArrangement.get(0));
                removeNumberFromDataSet(availableTileArrangement.get(0), availableTileArrangements);
            }
        }

        for (int tileIndex = 0; tileIndex <= 8; tileIndex++) {
            if (newTileArragement.get(tileIndex) != null) {
                continue;
            }
            Number leastRepresentedNumber = getLeastRepresentedNumber(tileIndex, availableTileArrangements);
            newTileArragement.set(tileIndex, leastRepresentedNumber);
            removeNumberFromDataSet(leastRepresentedNumber, availableTileArrangements);
        }
        System.out.println("");

        return newTileArragement;
    }

    private List<Number> getTileArrangement(int tileIndex, List<List<Number>> sudokuGrid) {
        // Create the first tile of the sudoku grid
        if (tileIndex == 0) {
            List<Number> newTile = new LinkedList<Number>(Arrays.asList(1,2,3,4,5,6,7,8,9));
            // TODO: Remove seed to make it actually random
            Collections.shuffle(newTile, new Random(123));
            return newTile;
        }

        // Get the horizontal and vertical tiles, that are relevant for the current tile index
        // (so get the tiles directly next to the current tileIndex)
        List<List<Number>> horizontalTiles = getExistingTiles(tileIndex, sudokuGrid, Orientation.HORIZONTAL);
        List<List<Number>> verticalTiles = getExistingTiles(tileIndex, sudokuGrid, Orientation.VERTICAL);

        List<List<Number>> availableTileArrangements = findAvailableTileArrangements(horizontalTiles, verticalTiles);
        return getNewTileArrangement(availableTileArrangements);

        // if (availableTileArrangement == null) {
        // }
        // return availableTileArrangement;
        // Something went wrong, return empty sudoku tile
        // return new LinkedList<Number>(Arrays.asList(null,null,null,null,null,null,null,null,null));
    }

    public void generateSudoku() {
        sudokuGrid = new ArrayList<>();

        for (int tileIndex = 0; tileIndex < 9; tileIndex++) {
            List<Number> tileArrangement = getTileArrangement(tileIndex, sudokuGrid);
            sudokuGrid.add(tileIndex, tileArrangement);
        }


        // // 1. Get current number
        // // 2. Get available places per row
        // Random random = new Random();
        // int number = 1;
        // List<Number> availableChoices = new LinkedList<Number>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        // for (int rowIndex = 0; rowIndex <= 8; rowIndex++) {
        //     // List<Number> availableColumns = GetAvailableRowColumns(sudokuGrid.get(rowIndex));
        //     // List<Number> intersectedColumns = Intersection(availableColumns, availableChoices);

        //     int randomIndex = random.nextInt(availableChoices.size());
        //     int chosenIndex = availableChoices.get(randomIndex).intValue();

        //     // Place the number in the sudoku grid, and remove the position from the available list
        //     sudokuGrid.get(rowIndex).add(chosenIndex, number);
        //     availableChoices.remove(randomIndex);
        // }
    }

    private boolean shouldAppendDivider(int index) {
        return index == 2 || index == 5;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<List<Number>> rowsList = generateSudokuGrid();

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

            for (int rowLineIndex = tileRow * TilesPerRow; rowLineIndex < (tileRow + 1) * TilesPerRow; rowLineIndex++) {
                for (int columnLineIndex = tileColumn * TilesPerColumn; columnLineIndex < (tileColumn + 1) * TilesPerColumn; columnLineIndex++) {
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
