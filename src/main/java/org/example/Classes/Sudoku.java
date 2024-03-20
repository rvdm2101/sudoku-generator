package org.example.Classes;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {
    private static int TILES_PER_SUDOKU = 9;

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
}
