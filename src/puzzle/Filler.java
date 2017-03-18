package puzzle;

import exceptions.UnsolvablePuzzleException;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * License: MIT
 */
abstract class Filler {

    /**
     * Fill columns with numbers.
     *
     * @param leftBound left-most column
     * @param rightBound right-most column
     * @param top highest row to start filling numbers in
     * @throws UnsolvablePuzzleException if it cannot place all numbers. Tries again after fail.
     */
    @SuppressWarnings("initialization")
    private static void fillColumns (Puzzle puzzle, int leftBound, int rightBound, int top) throws UnsolvablePuzzleException {
        Square square;

        // For each column:
        for (int x = leftBound; x < rightBound; x++) {
            // Reset pen to contain 1-9 in order
            Pen pen = new Pen();

            // Establish list of empty squares
            LinkedList<Integer> emptySquares = new LinkedList<>();

            // For each row
            for (int y = top; y < 9; y++) {
                square = new Square(x, y);

                /*
                * Remove existing numbers from pen:
                *
                * If we aren't starting at the top,
                * remove the existing items to avoid duplication.
                */
                if(top > 0) {
                    for (int r = 0; r < top; r++) { // iterate through rows above this one
                        pen.remove(puzzle.getSquare(x, r)); // remove it from the pen
                    }
                }

                // Sort the pen lowest-to-highest
                pen.sort();

                // For each number in the pen
                for (int val : pen.iterator()) {

                    // If number is valid
                    if (Solver.isValid(puzzle, val, square)) {
                        // Set the current square to the lowest value that is valid.
                        puzzle.setSquare(square, val);

                        // Remove the number we just used.
                        pen.remove(val);

                        // Remove the current square from the empty list if it exists
                        int index = emptySquares.indexOf(y);
                        if (index > -1) emptySquares.remove(index);

                        // Stop iterating
                        break;
                    } else {
                        // add square to empty list if not present
                        if (emptySquares.indexOf(y) == -1)
                            emptySquares.add(y);
                    }
                }
            }

            /*
            * If we still have items in the pen,
            * try to replace items to put them where they go.
            */
            if(!pen.isEmpty()) {
                // Record changes to avoid looping the same change.
                LinkedList<Integer> changes = new LinkedList<>();

                // Record number of iterations over the column
                int iterations = 0;

                // While we still have items in the pen.
                while (!pen.isEmpty()) {

                    // If we iterate 1000 times, declare the puzzle impossible
                    if (iterations > 1000) {
                        throw new UnsolvablePuzzleException();
                    }

                    // For each square in this column.
                    for (int y = top; y < 9; y++) {
                        square = new Square(x, y);

                        // Skip square if it is empty or if we just replaced it (ignore if over certain iteration count)
                        if ((emptySquares.indexOf(y) > -1 && iterations < 20) || (changes.indexOf(puzzle.getSquare(x, y)) > -1 && iterations < 40)) {
                            continue;
                        }

                        // Reorder pen, smallest to largest.
                        pen.sort();

                        // For each number in the pen:
                        for (int val : pen.iterator()) {
                            // If we can swap this number,
                            if (Solver.isValid(puzzle, val, square)) {
                                // Set oldVal to be what the square was before the swap
                                int oldVal = puzzle.getSquare(x, y);

                                // Swap it
                                puzzle.setSquare(square, val);

                                // Add this to the changes, then remove it from the pen.
                                changes.add(val);
                                pen.remove(val);

                                // Try to insert the old value to an empty square before replacing another.
                                // If empty list is not empty
                                if (emptySquares.size() > 0) {
                                    // Sort the empty list
                                    emptySquares.sort(null);

                                    // For each square in the empty list
                                    ListIterator<Integer> emptyIt = emptySquares.listIterator();
                                    while(emptyIt.hasNext()) {
                                        Square otherSquare = new Square(x, emptyIt.next());

                                        // If adding the old value to the empty otherSquare is valid
                                        if (Solver.isValid(puzzle, oldVal, otherSquare)) {
                                            // Add it
                                            puzzle.setSquare(otherSquare, oldVal);

                                            // Remove otherSquare from empty list
                                            emptyIt.remove();

                                            // Set this to 0 to avoid adding it to the pen.
                                            oldVal = 0;

                                            // Stop iterating
                                            break;
                                        }
                                    }
                                }

                                // If we didn't add the old value to an empty square
                                if (oldVal > 0) {
                                    // Add it to the pen
                                    pen.add(oldVal);
                                }
                            }
                        }
                    }

                    // Increment the iteration
                    iterations++;
                }
            }
        }
    }

    /**
     * Fill box with numbers (Does not check validity).
     *
     * @param topBound top edge of box
     * @param rightBound right edge of box
     * @param bottomBound bottom edge of box
     * @param leftBound left edge of box
     */
    private static void fillBox (Puzzle puzzle, int topBound, int rightBound, int bottomBound, int leftBound) {
        /*
        * All 9 numbers are present in box,
        * therefore, it is only reset once.
        * Pen is in random order.
        */
        Pen pen = new Pen(true);

        // For each column.
        for (int x = leftBound; x < rightBound; x++) {
            // For each row.
            for (int y = topBound; y < bottomBound; y++) {
                // Set the current square to the first number (random) in pen and remove it.
                puzzle.setSquare(new Square(x, y), pen.next());
            }
        }
    }

    /**
     * Generate a completed puzzle.
     */
    static Puzzle filledPuzzle () {
        // Reset puzzle to all 0s
        Puzzle puzzle = new Puzzle();

        // Fill in the first box so we have somewhere to start.
        fillBox(puzzle, 0, 3, 3, 0);

        // Fill in the remaining columns
        // If either function flags impossible,
        try {
            fillColumns(puzzle, 0, 3, 3); // fill left column
            fillColumns(puzzle, 3, 9, 0); // fill right two columns

            return puzzle;
        } catch (UnsolvablePuzzleException e) {
            // Try again.
            return filledPuzzle();
        }
    }
}
