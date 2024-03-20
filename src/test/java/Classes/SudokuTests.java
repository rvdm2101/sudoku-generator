package Classes;

import java.util.ArrayList;
import java.util.Collections;

import org.example.Classes.Sudoku;
import org.example.Classes.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SudokuTests {
    
    @Test
    public void addTileToSudoku() {
        Sudoku sudoku = new Sudoku();

        ArrayList<Integer> tileValues = new ArrayList<>();
        Collections.addAll(tileValues, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Tile tile = new Tile(tileValues);

        sudoku.addTile(tile);
        ArrayList<Tile> newTiles = new ArrayList<>();
        newTiles.add(tile);

        Assertions.assertEquals(sudoku.getTiles(), newTiles);
    }
}
