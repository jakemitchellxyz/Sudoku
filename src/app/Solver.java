package app;

import java.util.*;

/**
 * Created by Jake Mitchell on 8 Sept, 2016.
 * Logical Assistance from Koushul Ramjattun.
 * License: MIT
 */
public class Solver {
    private SudokuPuzzle puzzle;
    private SudokuPuzzle tempPuzzle;
    private int difficulty;

    private HashMap<List<Integer>, ArrayList<Integer>> notes = new HashMap<>();
    private ArrayList<List<Integer>> emptySquares;
    private static final HashMap<List<Integer>, Integer> BOXES;

    /**
     * Hard-Coded value of the BOXES. These can never change, so it's faster and safer to hard-code them in.
     * Format: Coordinate of square => box # it resides in
     */
    static {
        BOXES = new HashMap<>();
        int box;

        // For each column
        for(int x = 0; x < 9; x++) {
            // For each row
            for(int y = 0; y < 9; y++) {
                // Magical function with help from Yunze li
                box = (int) (Math.ceil((x + 1) / 3.0) + 3 * Math.ceil((y + 1) / 3.0) - 4);

                // Insert coordinate and box into HashMap
                BOXES.put(Collections.unmodifiableList(Arrays.asList(x, y)), box);
            }
        }
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
     * Get all coordinates in this box.
     *
     * @param box number of the box we are in
     * @return ArrayList of the coordinates (String)
     */
    private ArrayList<List<Integer>> getAllInBox(int box) {
        ArrayList<List<Integer>> ret = new ArrayList<>();

        for(List<Integer> key : BOXES.keySet()) {
            if (BOXES.get(key) == box) {
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

        // Check if in box
        for (List<Integer> square : getAllInBox(BOXES.get(Arrays.asList(x, y)))) {
            if (this.puzzle.getSquare(square.get(0), square.get(1)) == num) {
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
        List<Integer> square;

        // For each empty square
        Iterator<List<Integer>> emptyIt = this.emptySquares.iterator();
        while(emptyIt.hasNext()) {
            square = emptyIt.next();

            // Get all notes in this square
            theseNotes = this.notes.get(square);

            // If we have already inserted a square with this # of notes
            if (possibleSquares.containsKey(theseNotes.size())) {
                // Add this one to that list
                possibleSquares.get(theseNotes.size()).add(new int[]{ square.get(0), square.get(1), theseNotes.get(0) });
            } else {
                // Otherwise, create a new list
                ArrayList<int[]> list = new ArrayList<int[]>();
                list.add(new int[]{ square.get(0), square.get(1), theseNotes.get(0) });

                possibleSquares.put(theseNotes.size(), list);
            }
        }

        // If we added a square with exactly 1 note
        if(possibleSquares.containsKey(1) && possibleSquares.get(1).size() > 0) {
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
                // TODO: find a way of declaring impossible.
                return new int[]{};
            } else {
                // Otherwise, we have completed the puzzle
                return new int[]{};
            }
        }
    }

    /**
     * Remove a number from the notes in a specific square.
     *
     * @param square coordinates of the square to remove notes from
     * @param value value to remove from the notes
     */
    private void removeNote (List<Integer> square, int value) {
        // If this value is in the notes,
        if(this.notes.get(square).indexOf(value) > -1) {
            // Remove it
            this.notes.get(square).remove(this.notes.get(square).indexOf(value));
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
        List<Integer> coord = Arrays.asList(x, y);
        List<Integer> emptySquare;

        // Remove note from this square
        this.emptySquares.remove(coord);

        // For each empty square
        Iterator<List<Integer>> emptyIt = this.emptySquares.iterator();
        while(emptyIt.hasNext()) {
            emptySquare = emptyIt.next();

            if ((emptySquare.get(0) == x) // If in this column or
                    || (emptySquare.get(1) == y) // If in this row or
                    || (BOXES.get(coord) == BOXES.get(emptySquare))) { // If in this box,

                // Remove it
                this.removeNote(emptySquare, val);
            }
        }
    }

    /**
     * Add all notes to all empty squares.
     */
    private void addAllNotes() {
        List<Integer> square;

        // For each empty square
        Iterator<List<Integer>> emptyIt = this.emptySquares.iterator();
        while(emptyIt.hasNext()) {
            square = emptyIt.next();

            // If square is empty
            if (this.tempPuzzle.getSquare(square.get(0), square.get(1)) == 0) {
                // Add all numbers 1-9 to the notes
                for (int i = 1; i < 10; i++) {
                    if (this.isValid(i, square.get(0), square.get(1))) {
                        // If this square has already been set,
                        if (notes.containsKey(square)) {
                            // Add to it
                            notes.get(square).add(i);
                        } else {
                            // Otherwise, create a new ArrayList for it
                            ArrayList<Integer> list = new ArrayList<>();
                            list.add(i);

                            notes.put(Collections.unmodifiableList(square), list);
                        }
                    }
                }
            }
        }
    }

    /**
     * Inserts numbers, based on the notes, into the puzzle
     */
    private void insertSquares () {
        // Get a square with exactly one note.
        int[] square = this.scanner();

        // If we have one,
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
    public boolean solve (ArrayList<List<Integer>> removedSquares) {
        this.emptySquares = removedSquares;
        this.tempPuzzle = new SudokuPuzzle(this.puzzle);

        // Determine and insert all notes into all empty squares
        this.addAllNotes();

        // Insert the appropriate numbers into the puzzle and update notes until solved
        this.insertSquares();

        // Check if the puzzle has been solved and return that
        return this.isSolved();
    }
}
