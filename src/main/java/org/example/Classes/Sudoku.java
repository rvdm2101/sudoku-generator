package org.example.Classes;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {
    private static int TILES_PER_SUDOKU = 9;
    private static String HORIZONTAL_DIVIDER = "|";
    private static String VERTICAL_DIVIDER = "--------- --------- ---------";
    private static int ROWS_PER_TILE = 3;
    private static int TILES_PER_ROW = 3;

    private List<Tile> tiles = new ArrayList<>();

    public void addTile(Tile newTile) {
        if (tiles.size() == TILES_PER_SUDOKU) {
            throw new RuntimeException("The sudoku is already full");
        }
        tiles.add(newTile);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int rowIndex = 0; rowIndex <= 8; rowIndex++) {
            int tileRow = rowIndex / ROWS_PER_TILE;
            // rowIndex 0,1,2 => tileRow 0
            // rowIndex 3,4,5 => tileRow 1
            // rowIndex 6,7,8 => tileRow 2

            // Get the indexes of the tiles of this row
            // tileRow 0 => tiles 0,1,2
            // tileRow 1 => tiles 3,4,5
            // tileRow 2 => tiles 6,7,8
            int firstTileIndex = tileRow * TILES_PER_ROW;
            int[] indexes = {firstTileIndex, firstTileIndex + 1, firstTileIndex + 2};

            int tileRowNumber = rowIndex % TILES_PER_ROW;
            // If we are about to print the next row of tiles, append the vertival divider
            if (tileRowNumber == 0 && rowIndex != 0) {
                stringBuilder.append(VERTICAL_DIVIDER + "\n");
            }

            // Print all the tiles in this row
            for (int index : indexes) {
                // Add horizontal dividers after each tile
                if (index != indexes[0]) {
                    stringBuilder.append(HORIZONTAL_DIVIDER);
                }

                // Add the tile values
                for (int tileValue : tiles.get(index).getValuesByRow(tileRowNumber)) {
                    stringBuilder.append(String.format(" %d ", tileValue));
                }
            }
            // After the last tile, append a newline
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
