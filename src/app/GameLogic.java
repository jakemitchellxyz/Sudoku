package app;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
class GameLogic {
    private static boolean pencil;
    private static SudokuPuzzle puzzle;
    private static SudokuPuzzle reset;

    static String getSquare (int x, int y) {
        int square = puzzle.getSquare(x, y);
        return "" + ((square == 0) ? "" : square);
    }

    static boolean isPencil () {
        return pencil;
    }

    static void togglePencil () {
        pencil = !isPencil();
    }

    static void createPuzzle (int difficulty) {
        puzzle = new SudokuPuzzle(difficulty);
        reset = puzzle;
    }

    static void resetPuzzle () {
        puzzle = reset;
    }
}
