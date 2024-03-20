package Classes;

import java.util.ArrayList;
import java.util.Collections;

import org.example.Classes.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TileTests {
    
    @Test
    public void createTileWithTooLittleValues() {
        ArrayList<Integer> tileValues = new ArrayList<>();
        Collections.addAll(tileValues, 1, 2, 3);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Tile(tileValues));
    }
    
    @Test
    public void createTileWithTooManyValues() {
        ArrayList<Integer> tileValues = new ArrayList<>();
        Collections.addAll(tileValues, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Tile(tileValues));
    }
    
    @Test
    public void createTileWithInvalidValues() {
        ArrayList<Integer> tileValues = new ArrayList<>();
        Collections.addAll(tileValues, 14, 2, 3, 54, 5, 6, 27, 8, 79);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Tile(tileValues));
    }
    
    @Test
    public void createTileWithValidValues() {
        ArrayList<Integer> tileValues = new ArrayList<>();
        Collections.addAll(tileValues, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Tile tile = new Tile(tileValues);

        Assertions.assertEquals(tileValues, tile.getValues());
    }
}