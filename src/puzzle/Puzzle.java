package puzzle;

/**
 * Created by Jake Mitchell on 17 Mar, 2017.
 * License: MIT
 */
class Puzzle {
    private int[][] puzzle;

    /**
     * General constructor: create empty puzzle.
     */
    Puzzle() {
        puzzle = new int[9][9];
    }

    /**
     * Constructor to duplicate an old puzzle.
     *
     * @param old Puzzle object to copy from
     */
    Puzzle(Puzzle old) {
        puzzle = old.getPuzzle();
    }

    /**
     * Return the entire puzzle. Used for copying puzzles.
     *
     * @return returns entire puzzle numbers
     */
    private int[][] getPuzzle() {
        return this.puzzle;
    }

    /**
     *
     * @param otherPuzzle puzzle to copy from
     */
    void setPuzzle(Puzzle otherPuzzle) {
        this.puzzle = otherPuzzle.getPuzzle();
    }

    /**
     * Retrieve an individual square via coordinates.
     *
     * @param x column of square - 1
     * @param y row of square - 1
     * @return int value of the square
     */
    int getSquare (int x, int y) {
        return this.puzzle[y][x];
    }

    /**
     * Retrieve an individual square.
     *
     * @param square coordinates of square to look at
     * @return int value of the square
     */
    int getSquare (Square square) {
        return this.puzzle[square.getY()][square.getX()];
    }

    /**
     * Set the value of a single square.
     *
     * @param square Square object of coordinates
     * @param val value to set the square to
     */
    void setSquare (Square square, int val) {
        this.puzzle[square.getY()][square.getX()] = val;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("\nSUDOKU PUZZLE:\n-------------------------\n");

        // For each row
        for (int y = 0; y < 9; y++) {
            ret.append("| ");

            // For each column
            for (int x = 0; x < 9; x++) {
                int val = getSquare(x, y);

                // Calculate if there should be a border
                String rightBorder = ((x + 1) % 3) == 0 ? " | " : " ";

                ret.append(val).append(rightBorder);
            }

            // Calculate if there should be a border
            String bottomBorder = ((y + 1) % 3) == 0 ? "\n-------------------------" : "";

            ret.append(bottomBorder).append("\n");
        }

        return ret.toString();
    }
}
