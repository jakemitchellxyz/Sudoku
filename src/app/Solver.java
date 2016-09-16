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
    private boolean solvable = true;

    private HashMap<Square, ArrayList<Integer>> notes = new HashMap<>();
    private ArrayList<Square> emptySquares;

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
        return this.difficulty;
    }

    /**
     * Boolean of whether a number is valid to insert.
     *
     * @param num value to query
     * @param square Square object of location
     * @return boolean of validity
     */
    public boolean isValid (int num, Square square) {
        // Check if number exists in column.
        for (int i = 0; i < square.getY(); i++) {
            if(puzzle.getSquare(square.getX(), i) == num) {
                return false;
            }
        }

        // Check if number exists in row.
        for (int i = 0; i < square.getX(); i++) {
            if (puzzle.getSquare(i, square.getY()) == num) {
                return false;
            }
        }

        // Check if in box
        for (Square other : square.getAllInBox()) {
            if (this.puzzle.getSquare(other) == num) {
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
    private Square scanner () throws PuzzleUnsolvableException {
        HashMap<Integer, ArrayList<Square>> possibleSquares = new HashMap<Integer, ArrayList<Square>>();

        ArrayList<Integer> theseNotes;
        Square square;

        // For each empty square
        Iterator<Square> emptyIt = this.emptySquares.iterator();
        while(emptyIt.hasNext()) {
            square = emptyIt.next();

            // Get all notes in this square
            theseNotes = this.notes.get(square);

            // If we have already inserted a square with this # of notes
            if (possibleSquares.containsKey(theseNotes.size())) {
                // Add this one to that list
                possibleSquares.get(theseNotes.size()).add(square);
            } else {
                // Otherwise, create a new list
                ArrayList<Square> list = new ArrayList<Square>();
                list.add(square);

                possibleSquares.put(theseNotes.size(), list);
            }
        }

        // If we added a square with exactly 1 note
        if(possibleSquares.containsKey(1) && possibleSquares.get(1).size() > 0) {
            // Return the fuck out of it
            return possibleSquares.get(1).get(0);
        } else {
            // Impossiblity flag
            boolean impossible = false;

            // For each # of notes in a square,
            for (ArrayList<Square> squaresWithNotes : possibleSquares.values()) {
                // For each square,
                for (Square sq : squaresWithNotes) {
                    // If a square exists,
                    if (sq != null) {
                        //
                        impossible = true;
                    }
                }
            }

            // If we have any squares with more than one note,
            if(impossible) {
                // Throw an error to exit back to the solver and exit the solver
                throw new PuzzleUnsolvableException("Puzzle is not solvable.");
            } else {
                // Otherwise, we have completed the puzzle
                return null;
            }
        }
    }

    /**
     * Remove a number from the notes in a specific square.
     *
     * @param note value to remove from the notes
     * @param square coordinates of the square to remove notes from
     */
    private void removeNote (int note, Square square) {
        ArrayList<Integer> theseNotes = this.notes.get(square);

        // If this value is in the notes,
        if(theseNotes.indexOf(note) > -1) {
            // Remove it
            theseNotes.remove(theseNotes.indexOf(note));
        }
    }

    /**
     * Updates the notes when a number has been inserted into the puzzle.
     *
     * @param square square that was inserted
     * @param note value of note to insert
     */
    private void update(Square square, int note) {
        Square emptySquare;

        // Remove note from this square
        this.emptySquares.remove(square);

        // For each empty square
        Iterator<Square> emptyIt = this.emptySquares.iterator();
        while(emptyIt.hasNext()) {
            emptySquare = emptyIt.next();

            // If in this column, row, or box, remove it
            if (emptySquare.affects(square)) {
                this.removeNote(note, emptySquare);
            }
        }
    }

    /**
     * Add all notes to all empty squares.
     */
    private void addAllNotes() {
        Square square;

        // For each empty square
        Iterator<Square> emptyIt = this.emptySquares.iterator();
        while(emptyIt.hasNext()) {
            square = emptyIt.next();

            // If square is empty
            if (this.tempPuzzle.getSquare(square) == 0) {
                // Add all numbers 1-9 to the notes
                for (int note = 1; note < 10; note++) {
                    if (this.isValid(note, square)) {
                        // If this square has already been set,
                        if (notes.containsKey(square)) {
                            // Add to it
                            notes.get(square).add(note);
                        } else {
                            // Otherwise, create a new ArrayList for it
                            ArrayList<Integer> list = new ArrayList<>();
                            list.add(note);

                            notes.put(square, list);
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
        Square square;

        try {
            square = this.scanner();
        } catch (PuzzleUnsolvableException e) {
            // Flag the puzzle impossible
            this.solvable = false;
            return;
        }

        // If we have one,
        if(square != null) {
            // Get the value of the first (only) note in the square
            int firstNote = this.notes.get(square).get(0);

            // Insert the value of the square into the puzzle
            this.tempPuzzle.setSquare(square, firstNote);

            // Update the notes to reflect the change
            this.update(square, firstNote);

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
        // Return false if we've been flagged impossible
        if (!this.solvable)
            return false;

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
    public boolean solve (ArrayList<Square> removedSquares) {
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
