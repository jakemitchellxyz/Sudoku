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
    private static final HashMap<String, Integer> boxes;

    /**
     * Hard-Coded value of the boxes. These can never change, so it's faster and safer to hard-code them in.
     * Format: Coordinate of square => box # it resides in
     */
    static {
        boxes = new HashMap<>();

        boxes.put("00", 0);   boxes.put("30", 1);   boxes.put("60", 2);
        boxes.put("01", 0);   boxes.put("31", 1);   boxes.put("61", 2);
        boxes.put("02", 0);   boxes.put("32", 1);   boxes.put("62", 2);
        boxes.put("10", 0);   boxes.put("40", 1);   boxes.put("70", 2);
        boxes.put("11", 0);   boxes.put("41", 1);   boxes.put("71", 2);
        boxes.put("12", 0);   boxes.put("42", 1);   boxes.put("72", 2);
        boxes.put("20", 0);   boxes.put("50", 1);   boxes.put("80", 2);
        boxes.put("21", 0);   boxes.put("51", 1);   boxes.put("81", 2);
        boxes.put("22", 0);   boxes.put("52", 1);   boxes.put("82", 2);

        boxes.put("03", 3);   boxes.put("33", 4);   boxes.put("63", 5);
        boxes.put("04", 3);   boxes.put("34", 4);   boxes.put("64", 5);
        boxes.put("05", 3);   boxes.put("35", 4);   boxes.put("65", 5);
        boxes.put("13", 3);   boxes.put("43", 4);   boxes.put("73", 5);
        boxes.put("14", 3);   boxes.put("44", 4);   boxes.put("74", 5);
        boxes.put("15", 3);   boxes.put("45", 4);   boxes.put("75", 5);
        boxes.put("23", 3);   boxes.put("53", 4);   boxes.put("83", 5);
        boxes.put("24", 3);   boxes.put("54", 4);   boxes.put("84", 5);
        boxes.put("25", 3);   boxes.put("55", 4);   boxes.put("85", 5);

        boxes.put("06", 6);   boxes.put("36", 7);   boxes.put("66", 8);
        boxes.put("07", 6);   boxes.put("37", 7);   boxes.put("67", 8);
        boxes.put("08", 6);   boxes.put("38", 7);   boxes.put("68", 8);
        boxes.put("16", 6);   boxes.put("46", 7);   boxes.put("76", 8);
        boxes.put("17", 6);   boxes.put("47", 7);   boxes.put("77", 8);
        boxes.put("18", 6);   boxes.put("48", 7);   boxes.put("78", 8);
        boxes.put("26", 6);   boxes.put("56", 7);   boxes.put("86", 8);
        boxes.put("27", 6);   boxes.put("57", 7);   boxes.put("87", 8);
        boxes.put("28", 6);   boxes.put("58", 7);   boxes.put("88", 8);
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
     * @return ArrayList of the coordinates (String)
     */
    private ArrayList<String> getAllInBox(int box) {
        ArrayList<String> ret = new ArrayList<>();

        for(String key : boxes.keySet()) {
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

        for (String square : getAllInBox(boxes.get("" + x + y))) {
            String[] coord = square.split("");
            if (this.puzzle.getSquare(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])) == num) {
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
            return possibleSquares.get(1).iterator().next();
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
