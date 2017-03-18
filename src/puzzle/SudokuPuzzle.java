package puzzle;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
public class SudokuPuzzle {
    private Puzzle puzzle;
    private Puzzle solution;

    /**
     * Constructor to create a new puzzle.
     *
     * @param difficulty 0 = easy, 1 = medium, 2 = hard
     */
    public SudokuPuzzle (int difficulty) {
        solution = Filler.filledPuzzle();
        puzzle = solution;
//        puzzle = Remover.removeSquares(solution, difficulty);
    }

    /**
     * Access to puzzle info to allow printing the puzzle.
     *
     * @param x x-coord
     * @param y y-coord
     * @return value
     */
    public int getSquare(int x, int y) {
        return puzzle.getSquare(x, y);
    }

    @Override
    public String toString() {
        return solution.toString(); // can be changed to puzzle or solution
    }
}
