package org.example;

import java.util.List;

public class Generator {
    private static List<List<Number>> sudoku;

    public static void GenerateSudoku() {
        for (int number = 1; number <= 9; number++) {
            System.out.print(number);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
