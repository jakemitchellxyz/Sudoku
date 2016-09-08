package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * License: MIT
 */
public class Filler {
    private SudokuPuzzle puzzle;
    private ArrayList<Integer> pen = new ArrayList<>();

    /**
     * Constructor
     *
     * @param p Puzzle to fill
     */
    Filler (SudokuPuzzle p) {
        this.puzzle = p;
    }

    /**
     * Reset the pen to contain 1-9
     */
    private void resetPen () {
        // empty the array
        pen.clear();

        // Add all numbers 1-9 to the pen
        for (int i = 0; i < 9; i++) {
            pen.add(i, i + 1);
        }
    }

    /**
     * Fill columns with numbers.
     *
     * @param leftBound left-most column
     * @param rightBound right-most column
     * @param top highest row to start filling numbers in
     * @return boolean of whether puzzle is impossible
     */
    private boolean fillColumns (int leftBound, int rightBound, int top) {
        // Flag for whether puzzle is impossible
        boolean impossible = false;

        // For each column:
        for (int x = leftBound; x < rightBound; x++) {
            // Reset pen to contain 1-9
            this.resetPen();

            // Establish list of empty squares
            ArrayList<Integer> emptySquares = new ArrayList<>();

            // For each row
            for (int y = top; y < 9; y++) {
                /*
                * Remove existing numbers from pen:
                *
                * If we aren't starting at the top,
                * remove the existing items to avoid duplication.
                */
                if(top > 0) {
                    for (int i = 0; i < top; i++) {
                        int index = pen.indexOf(puzzle.getSquare(x, i));
                        if (index != -1) {
                            pen.remove(index);
                        }
                    }
                }

                // Sort the pen lowest-to-highest
                Collections.sort(pen);

                // For each number in the pen
                Iterator<Integer> penIt = pen.iterator();
                while(penIt.hasNext()) {
                    Integer val = penIt.next();

                    // If number is valid
                    if (puzzle.solver.isValid(val, x, y)) {
                        // Set the current square to the lowest value that is valid.
                        puzzle.setSquare(x, y, val);

                        // Remove the number we just used.
                        penIt.remove();

                        // Remove the current square from the empty list if it is in the empty list
                        int index = emptySquares.indexOf(y);
                        if (index > -1) {
                            emptySquares.remove(index);
                        }

                        // Stop iterating
                        break;
                    } else {
                        // add square to empty list if not present
                        if (emptySquares.indexOf(y) == -1) {
                            emptySquares.add(y);
                        }
                    }
                }
            }

            /*
            * If we still have items in the pen,
            * try to replace items to put them where they go.
            */
            if(pen.size() > 0) {
                // Record changes to avoid looping the same change.
                ArrayList<Integer> changes = new ArrayList<>();

                // Record number of iterations over the column
                int iterations = 0;

                // While we still have items in the pen.
                while (pen.size() > 0) {

                    // If we iterate 1000 times, declare the puzzle impossible
                    if (iterations > 1000) {
                        impossible = true;
                        break;
                    }

                    // For each square in this column.
                    for (int y = top; y < 9; y++) {
                        // Skip square if it is empty or if we just replaced it (ignore if over certain iteration count)
                        if ((emptySquares.indexOf(y) > -1 && iterations < 20) || (changes.indexOf(puzzle.getSquare(x, y)) > -1 && iterations < 40)) {
                            continue;
                        }

                        // Reorder pen, smallest to largest.
                        Collections.sort(pen);

                        // For each number in the pen:
                        ListIterator<Integer> penIt = pen.listIterator();
                        while (penIt.hasNext()) {
                            Integer val = penIt.next();

                            // If we can swap this number,
                            if (puzzle.solver.isValid(val, x, y)) {
                                // Set oldVal to be what the square was before the swap
                                int oldVal = puzzle.getSquare(x, y);

                                // Swap it
                                puzzle.setSquare(x, y, val);

                                // Add this to the changes, then remove it from the pen.
                                changes.add(val);
                                penIt.remove();

                                // Try to insert the old value to an empty square before replacing another.
                                // If empty list is not empty
                                if (emptySquares.size() > 0) {
                                    // Sort the empty list
                                    Collections.sort(emptySquares);

                                    // For each square in the empty list
                                    Iterator<Integer> emptyIt = emptySquares.iterator();
                                    while(emptyIt.hasNext()) {
                                        Integer square = emptyIt.next();

                                        // If adding the old value to the empty square is valid
                                        if (puzzle.solver.isValid(oldVal, x, square)) {
                                            // Add it
                                            puzzle.setSquare(x, square, oldVal);

                                            // Remove square from empty list
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
                                    penIt.add(oldVal);
                                }
                            }
                        }
                    }

                    // Increment the iteration
                    iterations++;
                }
            }
        }

        // Return the possibility flag
        return impossible;
    }

    /**
     * Fill box with numbers (Does not check validity).
     *
     * @param topBound top edge of box
     * @param rightBound right edge of box
     * @param bottomBound bottom edge of box
     * @param leftBound left edge of box
     */
    private void fillBox (int topBound, int rightBound, int bottomBound, int leftBound) {
        /*
        * All 9 numbers are present in box,
        * therefore, it is only reset once.
        */
        this.resetPen();

        // For each column.
        for (int x = leftBound; x < rightBound; x++) {
            // For each row.
            for (int y = topBound; y < bottomBound; y++) {
                // Shuffle pen to randomize order.
                Collections.shuffle(pen);

                // Set the current square to the first number (random) in pen.
                puzzle.setSquare(x, y, pen.get(0));

                // Prevent repeat numbers by removing the one we just used.
                pen.remove(0);
            }
        }
    }

    /**
     * Generate a completed puzzle.
     */
    public void fillPuzzle () {
        // Reset puzzle to all 0s
        puzzle.resetPuzzle();

        // Fill in the first box so we have somewhere to start.
        this.fillBox(0, 3, 3, 0);

        // Fill in the remaining columns
        // If either function flags impossible,
        if(this.fillColumns(0, 3, 3) || this.fillColumns(3, 9, 0)) {
            // Try again.
            this.fillPuzzle();
        }
    }
}
