package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * Logical Assistance from Koushul Ramjattun.
 * License: MIT
 */
public class Solver {
    private SudokuPuzzle puzzle;
    private SudokuPuzzle tempPuzzle;
    private int difficulty;
    private HashMap<int[], ArrayList<Integer>> notes = new HashMap<>();
    private ArrayList<int[]> emptySquares;
    private static final HashMap<int[], Integer> boxes;

    /**
     * Hard-Coded value of the boxes. These can never change, so it's faster and safer to hard-code them in.
     * Format: Coordinate of square => box # it resides in
     */
    static {
        boxes = new HashMap<>();

        boxes.put(new int[]{0,0}, 0);   boxes.put(new int[]{3,0}, 1);   boxes.put(new int[]{6,0}, 2);
        boxes.put(new int[]{0,1}, 0);   boxes.put(new int[]{3,1}, 1);   boxes.put(new int[]{6,1}, 2);
        boxes.put(new int[]{0,2}, 0);   boxes.put(new int[]{3,2}, 1);   boxes.put(new int[]{6,2}, 2);
        boxes.put(new int[]{1,0}, 0);   boxes.put(new int[]{4,0}, 1);   boxes.put(new int[]{7,0}, 2);
        boxes.put(new int[]{1,1}, 0);   boxes.put(new int[]{4,1}, 1);   boxes.put(new int[]{7,1}, 2);
        boxes.put(new int[]{1,2}, 0);   boxes.put(new int[]{4,2}, 1);   boxes.put(new int[]{7,2}, 2);
        boxes.put(new int[]{2,0}, 0);   boxes.put(new int[]{5,0}, 1);   boxes.put(new int[]{8,0}, 2);
        boxes.put(new int[]{2,1}, 0);   boxes.put(new int[]{5,1}, 1);   boxes.put(new int[]{8,1}, 2);
        boxes.put(new int[]{2,2}, 0);   boxes.put(new int[]{5,2}, 1);   boxes.put(new int[]{8,2}, 2);

        boxes.put(new int[]{0,3}, 3);   boxes.put(new int[]{3,3}, 4);   boxes.put(new int[]{6,3}, 5);
        boxes.put(new int[]{0,4}, 3);   boxes.put(new int[]{3,4}, 4);   boxes.put(new int[]{6,4}, 5);
        boxes.put(new int[]{0,5}, 3);   boxes.put(new int[]{3,5}, 4);   boxes.put(new int[]{6,5}, 5);
        boxes.put(new int[]{1,3}, 3);   boxes.put(new int[]{4,3}, 4);   boxes.put(new int[]{7,3}, 5);
        boxes.put(new int[]{1,4}, 3);   boxes.put(new int[]{4,4}, 4);   boxes.put(new int[]{7,4}, 5);
        boxes.put(new int[]{1,5}, 3);   boxes.put(new int[]{4,5}, 4);   boxes.put(new int[]{7,5}, 5);
        boxes.put(new int[]{2,3}, 3);   boxes.put(new int[]{5,3}, 4);   boxes.put(new int[]{8,3}, 5);
        boxes.put(new int[]{2,4}, 3);   boxes.put(new int[]{5,4}, 4);   boxes.put(new int[]{8,4}, 5);
        boxes.put(new int[]{2,5}, 3);   boxes.put(new int[]{5,5}, 4);   boxes.put(new int[]{8,5}, 5);

        boxes.put(new int[]{0,6}, 6);   boxes.put(new int[]{3,6}, 7);   boxes.put(new int[]{6,6}, 8);
        boxes.put(new int[]{0,7}, 6);   boxes.put(new int[]{3,7}, 7);   boxes.put(new int[]{6,7}, 8);
        boxes.put(new int[]{0,8}, 6);   boxes.put(new int[]{3,8}, 7);   boxes.put(new int[]{6,8}, 8);
        boxes.put(new int[]{1,6}, 6);   boxes.put(new int[]{4,6}, 7);   boxes.put(new int[]{7,6}, 8);
        boxes.put(new int[]{1,7}, 6);   boxes.put(new int[]{4,7}, 7);   boxes.put(new int[]{7,7}, 8);
        boxes.put(new int[]{1,8}, 6);   boxes.put(new int[]{4,8}, 7);   boxes.put(new int[]{7,8}, 8);
        boxes.put(new int[]{2,6}, 6);   boxes.put(new int[]{5,6}, 7);   boxes.put(new int[]{8,6}, 8);
        boxes.put(new int[]{2,7}, 6);   boxes.put(new int[]{5,7}, 7);   boxes.put(new int[]{8,7}, 8);
        boxes.put(new int[]{2,8}, 6);   boxes.put(new int[]{5,8}, 7);   boxes.put(new int[]{8,8}, 8);
    }

    /**
     * Constructor
     *
     * @param p Puzzle to solve
     * @param diff Difficulty to make the puzzle
     */
    Solver (SudokuPuzzle p, int diff) {
        this.puzzle = p;
        this.difficulty = diff;
    }

    /**
     * Returns the difficulty of this puzzle.
     *
     * @return int of difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Get all coordinates in this box
     * @param box number of the box we are in
     * @return ArrayList of the coordinates
     */
    private ArrayList<int[]> getAllInBox(int box) {
        ArrayList<int[]> ret = new ArrayList<>();

        for(int[] key : boxes.keySet()) {
            if (boxes.get(key) == box) {
                ret.add(key);
            }
        }

        return ret;
    }

    /**
     * Boolean of whether a number is valid to insert.
     *
     * @param num value to query
     * @param x column of square - 1
     * @param y row of square - 1
     * @return boolean of validity
     */
    public boolean isValid (int num, int x, int y) {
        // Check if number exists in column.
        for (int i = 0; i < y; i++) {
            if(puzzle.getSquare(x, i) == num) {
                return false;
            }
        }

        // Check if number exists in row.
        for (int i = 0; i < x; i++) {
            if (puzzle.getSquare(i, y) == num) {
                return false;
            }
        }

        // Check if number exists in the 3x3 box.
        System.out.println("[ " + x + ", " + y + " ]");

        for (int[] coord : boxes.keySet()) {
            System.out.println("[ " + coord[0] + ", " + coord[1] + " ]");
        }

        System.out.println(boxes.get(new int[]{ x, y }));

        for (int[] square : getAllInBox(boxes.get(new int[]{ x, y }))) {
            if (this.puzzle.getSquare(square[0], square[1]) == num) {
                return false;
            }
        }

        // If the number was not found, it is valid.
        return true;
    }

    /**
     * Search through the notes to find squares with exactly one note.
     *
     * @return [ x, y, val ]
     */
    private int[] scanner () {
        HashMap<Integer, ArrayList<int[]>> possibleSquares = new HashMap<Integer, ArrayList<int[]>>();

        ArrayList<Integer> theseNotes;
        int[] square;
        Iterator<int[]> emptyIt = this.emptySquares.iterator();

        // For each empty square
        while(emptyIt.hasNext()) {
            square = emptyIt.next();

            // Get all notes in this square
            theseNotes = this.notes.get(square);

            // If we have already inserted a square with this # of notes
            if (possibleSquares.containsKey(theseNotes.size())) {
                // Add this one to that list
                possibleSquares.get(theseNotes.size()).add(new int[]{ square[0], square[1], theseNotes.get(0) });
            } else {
                // Otherwise, create a new list
                ArrayList<int[]> list = new ArrayList<int[]>();
                list.add(new int[]{ square[0], square[1], theseNotes.get(0) });

                possibleSquares.put(theseNotes.size(), list);
            }
        }

        // If we added a square with exactly 1 note
        if(possibleSquares.get(1).size() > 0) {
            // Return the fuck out of it
            return possibleSquares.get(1).get(0);
        } else {
            /*
             * Can this next bit be an anonymous function somehow? Or even a Lambda expression?
             */
            boolean ret = false;
            for (int size : possibleSquares.keySet()) {
                if (possibleSquares.get(size).size() > 0) {
                    ret = true;
                }
            }
            /* End Imaginary anonymous function */

            // If we have any squares with more than one note,
            if(ret) {
                // TODO: flag impossible
                return new int[]{};
            } else {
                // Otherwise, we have completed the puzzle
                return new int[]{};
            }
        }
    }

    /**
     * Updates the notes when a number has been inserted into the puzzle.
     *
     * @param x column of the inserted number
     * @param y row of inserted number
     * @param val value of inserted number
     */
    private void update(int x, int y, int val) {
        int[] coord = new int[]{ x, y };
        int[] emptyCoord;

        // Remove note from this square
        notes.get(coord).remove(notes.get(coord).indexOf(val));

        Iterator<int[]> emptyIt = this.emptySquares.iterator();

        // For each empty square
        while(emptyIt.hasNext()) {
            int[] emptySquare = emptyIt.next();

            // If it is in this column
            if(emptySquare[0] == x) {
                emptyCoord = new int[]{ x, emptySquare[1] };

                // If this value is in the notes,
                if(notes.get(emptyCoord).indexOf(val) > -1) {
                    // Remove it
                    notes.get(emptyCoord).remove(notes.get(emptyCoord).indexOf(val));
                }

            // Else, if it is in this row
            } else if (emptySquare[1] == y) {
                emptyCoord = new int[]{ emptySquare[0], y };

                // If this value is in the notes,
                if(notes.get(emptyCoord).indexOf(val) > -1) {
                    // Remove it
                    notes.get(emptyCoord).remove(notes.get(emptyCoord).indexOf(val));
                }
            }

            if (boxes.get(coord) == boxes.get(emptySquare)) {
                // If this value is in the notes,
                if(notes.get(emptySquare).indexOf(val) > -1) {
                    // Remove it
                    notes.get(emptySquare).remove(notes.get(emptySquare).indexOf(val));
                }
            }
        }
    }

    /**
     * Inserts numbers, based on the notes, into the puzzle
     */
    private void insertSquares () {
        int[] square = this.scanner();

        if(square.length > 0) {
            // Insert the value of the square into the puzzle
            this.tempPuzzle.setSquare(square[0], square[1], square[2]);

            // Update the notes to reflect the change
            this.update(square[0], square[1], square[2]);

            // Since we made a change, repeat this function
            this.insertSquares();
        }
    }

    /**
     * Check if the puzzle has been solved.
     *
     * @return boolean of whether puzzle is solved
     */
    private boolean isSolved () {
        int total = 0;

        // For each column
        for (int x = 0; x < 9; x++) {
            // For each row
            for (int y = 0; y < 9; y++) {
                // Add the value of the square to the total
                total += puzzle.getSquare(x, y);
            }
        }

        // All numbers added together should be 405
        return (total == 405);
    }

    /**
     * Attempt to solve the puzzle. Return whether or not it can be solved.
     *
     * @param removedSquares list of squares that have been removed
     * @return whether or not the puzzle has been solved
     */
    public boolean solve (ArrayList<int[]> removedSquares) {
        this.emptySquares = removedSquares;
        this.tempPuzzle = new SudokuPuzzle(this.puzzle);

        Iterator<int[]> emptyIt = this.emptySquares.iterator();

        // For each empty square
        while(emptyIt.hasNext()) {
            int[] square = emptyIt.next();

            // If square is empty
            if (this.tempPuzzle.getSquare(square[0], square[1]) == 0) {
                // Add all numbers 1-9 to the notes
                for (int i = 1; i < 10; i++) {
                    if(this.isValid(i, square[0], square[1])) {
                        int[] coord = new int[]{ square[0], square[1] };

                        // If this square has already been set,
                        if (notes.containsKey(coord)) {
                            // Add to it
                            notes.get(coord).add(i);
                        } else {
                            // Otherwise, create a new ArrayList for it
                            ArrayList<Integer> list = new ArrayList<>();
                            list.add(i);

                            notes.put(coord, list);
                        }
                    }
                }
            }
        }

        // Insert the appropriate numbers into the puzzle and update notes until solved
        this.insertSquares();

        // Check if the puzzle has been solved and return that
        return this.isSolved();
    }
}
