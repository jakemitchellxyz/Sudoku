package app;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * Logical Assistance from Koushul Ramjattun.
 * License: MIT
 */
public class Solver {
    private SudokuPuzzle puzzle;
    private int difficulty;
    private int[][][] notes;
    private int[][] emptySquares;

    /**
     * Constructor
     *
     * @param p Puzzle to solve
     * @param diff Difficulty to make the puzzle
     */
    Solver (SudokuPuzzle p, int diff) {
        this.puzzle = p;
        this.difficulty = diff;
    }

    /**
     * Returns the difficulty of this puzzle.
     *
     * @return int of difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Boolean of whether a number is valid to insert.
     *
     * @param num value to query
     * @param x column of square - 1
     * @param y row of square - 1
     * @return boolean of validity
     */
    public boolean isValid (int num, int x, int y) {
        // Check if number exists in column.
        for (int i = 0; i < y; i++) {
            if(puzzle.getSquare(x, i) == num) {
                return false;
            }
        }

        // Check if number exists in row.
        for (int i = 0; i < x; i++) {
            if (puzzle.getSquare(i, y) == num) {
                return false;
            }
        }

        // Establish bounds of existing 3x3 box.
        int minX, maxX, minY, maxY;
        if (x < 3) { // left column
            if (y < 3) { // top row
                minX = 0;
                minY = 0;
                maxX = Math.max(2, x);
                maxY = Math.max(2, y);
            } else if (y < 6) { // middle row
                minX = 0;
                minY = 3;
                maxX = Math.max(2, x);
                maxY = Math.max(5, y);
            } else { // bottom row
                minX = 0;
                minY = 6;
                maxX = Math.max(2, x);
                maxY = Math.max(8, y);
            }
        } else if (x < 6) { // middle column
            if (y < 3) { // top row
                minX = 3;
                minY = 0;
                maxX = Math.max(5, x);
                maxY = Math.max(2, y);
            } else if (y < 6) { // middle row
                minX = 3;
                minY = 3;
                maxX = Math.max(5, x);
                maxY = Math.max(5, y);
            } else { // bottom row
                minX = 3;
                minY = 6;
                maxX = Math.max(5, x);
                maxY = Math.max(8, y);
            }
        } else { // right column
            if (y < 3) { // top row
                minX = 6;
                minY = 0;
                maxX = Math.max(8, x);
                maxY = Math.max(2, y);
            } else if (y < 6) { // middle row
                minX = 6;
                minY = 3;
                maxX = Math.max(8, x);
                maxY = Math.max(5, y);
            } else { // bottom row
                minX = 6;
                minY = 6;
                maxX = Math.max(8, x);
                maxY = Math.max(8, y);
            }
        }

        // Check if number exists in the 3x3 box.
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                if (puzzle.getSquare(i, j) == num) {
                    return false;
                }
            }
        }

        // If the number was not found, it is valid.
        return true;
    }

    /**
     * Check if the puzzle has been solved.
     *
     * @return boolean of whether puzzle is solved
     */
    private boolean isSolved () {
        int total = 0;

        // For each column
        for (int x = 0; x < 9; x++) {
            // For each row
            for (int y = 0; y < 9; y++) {
                // Add the value of the square to the total
                total += puzzle.getSquare(x, y);
            }
        }

        // All numbers added together should be 405
        return (total == 405);
    }

    public boolean solve () {

        // TODO: Add notes to every empty square
//        // For each column
//        for (int x = 0; x < 9; x++) {
//            // For each row
//            for (int y = 0; y < 9; y++) {
//                // If square is empty
//                if (puzzle.getSquare(x, y) == 0) {
//                    // Add all numbers 1-9 to the notes
//                    for (int i = 1; i < 10; i++) {
//                        if(this.isValid(i, x, y)) {
//                            //notes[y][x][] = i;
//                        }
//                    }
//                }
//            }
//        }

        // TODO: Insert the appropriate numbers into the puzzle and update notes until solved

        return this.isSolved();
    }
}
