package app;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
class GameLogic {
    private static boolean pencil;
    private static SudokuPuzzle puzzle;

    static String getSquare (int x, int y) {
        return "" + puzzle.getSquare(x,y);
    }

    static boolean isPencil() {
        return pencil;
    }

    static void togglePencil() {
        pencil = !isPencil();
    }

    static void createPuzzle(int difficulty) {
        puzzle = new SudokuPuzzle(difficulty);
    }
}
