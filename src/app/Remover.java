package app;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * License: MIT
 */
public class Remover {
    private SudokuPuzzle puzzle;
    private ArrayList<Square> removedSquares = new ArrayList<>();

    /**
     * Constructor
     *
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
        SudokuPuzzle copy = new SudokuPuzzle(this.puzzle);
        Random r = new Random();
        Square square;

        System.out.println(this.puzzle);

        // Remove a square from the puzzle
        do {
            // Set the original puzzle to be the modified version
            this.puzzle.setPuzzle(copy.getPuzzle());

            // Keep generating a random coordinate until we choose a square that is not in the removedSquares list
            do {
                square = new Square(r.nextInt(9), r.nextInt(9));
            } while (this.removedSquares.indexOf(square) > -1);

            // Empty the square
            copy.setSquare(square, 0);

            // Add coordinates to the removedSquares list
            this.removedSquares.add(square);

        // As long as it can be solved
        } while(copy.solver.solve(this.removedSquares));
    }
}
