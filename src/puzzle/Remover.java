package puzzle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * License: MIT
 */
abstract class Remover {
    /**
     * Remove Squares until while still solvable
     */
    Puzzle removeSquares (Puzzle puzzle, int difficulty) {
        // Copy the puzzle to avoid messing it up
        Puzzle copy = new Puzzle(puzzle);
        Solver solver = new Solver(copy, difficulty);
        Random r = new Random();
        Square square;

        ArrayList<Square> removedSquares = new ArrayList<>();

//        System.out.println(this.puzzle);

        // Remove a square from the puzzle
        do {
            // Set the original puzzle to be the modified version
            puzzle.setPuzzle(copy);

            // Keep generating a random coordinate until we choose a square that is not in the removedSquares list
            do {
                square = new Square(r.nextInt(9), r.nextInt(9));
            } while (removedSquares.indexOf(square) > -1);

            // Empty the square
            copy.setSquare(square, 0);

            // Add coordinates to the removedSquares list
            removedSquares.add(square);

        // As long as it can be solved
        } while(solver.solve(removedSquares));

        return puzzle;
    }
}








































