package org.example.Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tile {
    private static int VALUES_PER_TILE = 9;
    private static Integer[] ALLOWED_TILE_NUMBERS = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    private List<Integer> values;
    
    public Tile(ArrayList<Integer> tileValues) {
        if (tileValues.size() != VALUES_PER_TILE) {
            throw new IllegalArgumentException("The tile should have precisely 9 values");
        }

        List<Integer> tileValuesCopy = tileValues;
        List<Integer> allowedValues = Arrays.asList(ALLOWED_TILE_NUMBERS);
        Collections.sort(tileValuesCopy);
        if (!tileValuesCopy.equals(allowedValues)) {
            throw new IllegalArgumentException("The new tile values contain a illegal number");
        }

        values = tileValues;
    }

    public List<Integer> getValues() {
        return values;
    }
}
