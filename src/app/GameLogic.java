package app;

import puzzle.SudokuPuzzle;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
@SuppressWarnings("initialization")
class GameLogic {
    private static boolean pencil;
    private static SudokuPuzzle puzzle;
    private static SudokuPuzzle reset;

    static String getSquare (int x, int y) {
        int square = puzzle.getSquare(x, y);
        return "" + ((square == 0) ? "" : square);
    }

    static void togglePencil () {
        pencil = !pencil;
    }

    static void createPuzzle (int difficulty) {
        puzzle = new SudokuPuzzle(difficulty);
        reset = puzzle; // store untouched puzzle to allow a reset method
    }

    static void resetPuzzle () {
        puzzle = reset;
    }
}
