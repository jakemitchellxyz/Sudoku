package app;


import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * License: MIT
 */
public class Remover {
    private SudokuPuzzle puzzle;
    private ArrayList<int[]> removedSquares = new ArrayList<>();


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
        int[] coord;

        // Remove a square from the puzzle
        do {

            // Set the original puzzle to be the modified version
            this.puzzle.setPuzzle(copy.getPuzzle());

            // Keep generating a random coordinate until we choose a square that is not in the removedSquares list
            do {
                coord = new int[]{ r.nextInt(9) + 1, r.nextInt(9) + 1 };
            } while (this.removedSquares.indexOf(coord) > -1);

            // Empty the square
            System.out.println("[ " + coord[0] + ", " + coord[1] + " ]");
            copy.setSquare(coord[0], coord[1], 0);

            // Add coordinates to the removedSquares list
            this.removedSquares.add(coord);

        // As long as it can be solved
        } while(copy.solver.solve());

    }
}
