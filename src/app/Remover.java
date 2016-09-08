package app;


/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * License: MIT
 */
public class Remover {
    private SudokuPuzzle puzzle;

    /**
     * Constructor
     * @param p Puzzle to remove from
     */
    Remover (SudokuPuzzle p) {
        this.puzzle = p;
    }

    /**
     * Remove Squares until while still solvable
     */
    public void removeSquares () {
        // Copy the puzzle to avoid messing it up
        SudokuPuzzle copy = new SudokuPuzzle(puzzle);

        // As long as the puzzle can be solved,
        while(copy.solver.solve()) {
            // Remove a square from it:
            // TODO: Remove a square from the puzzle
        }
    }
}
