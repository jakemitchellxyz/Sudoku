package app;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
class SudokuPuzzle {
    public int[][] solution;
    private int[][] puzzle;
    public Solver solver;

    /**
     * Constructor to create a new puzzle.
     *
     * @param difficulty 0 = easy, 1 = medium, 2 = hard
     */
    SudokuPuzzle (int difficulty) {
        this.solver = new Solver(this, difficulty);
        this.generate();
    }

    /**
     * Constructor to duplicate an old puzzle.
     *
     * @param copyFrom SudokuPuzzle object to copy from
     */
    SudokuPuzzle (SudokuPuzzle copyFrom) {
        this.solver = new Solver(this, copyFrom.solver.getDifficulty());
        this.puzzle = copyFrom.getPuzzle();
    }

    /**
     * Retrieve an individual square via coordinates.
     *
     * @param x column of square - 1
     * @param y row of square - 1
     * @return int value of the square
     */
    public int getSquare (int x, int y) {
        return this.puzzle[y][x];
    }

    /**
     * Retrieve an individual square view Square object.
     *
     * @param square square object of location
     * @return int value of the square
     */
    public int getSquare (Square square) {
        return this.getSquare(square.getX(), square.getY());
    }

    /**
     * Set the value of a single square.
     *
     * @param square Square object of coordinates
     * @param val value to set the square to
     */
    public void setSquare (Square square, int val) {
        this.puzzle[square.getY()][square.getX()] = val;
    }

    /**
     * Return the entire puzzle.
     *
     * @return returns entire puzzle numbers
     */
    public int[][] getPuzzle() {
        return this.puzzle;
    }

    /**
     * Set all puzzle values at once
     *
     * @param vals all puzzle values
     */
    protected void setPuzzle(int[][] vals) {
        this.puzzle = vals;
    }

    /**
     * Reset the puzzle to all 0s
     */
    protected void resetPuzzle() {
        this.setPuzzle(new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        });
    }

    /**
     * Generate a new puzzle.
     */
    private void generate() {
        // Create the filler and remover
        Filler filler = new Filler(this);
        Remover remover = new Remover(this);

        // generate a new, completed puzzle
        filler.fillPuzzle();

        // Set the solution before we start removing values
        this.solution = this.puzzle;

        // Remove squares from the puzzle so the user can fill them in
        remover.removeSquares();
    }

    @Override
    public String toString() {
        String ret = "\nSUDOKU PUZZLE:\n-------------------------\n";
        String rightBorder;
        String bottomBorder;
        int[] row;
        int col;

        // For each row
        for (int r = 0; r < this.getPuzzle().length; r++) {
            row = this.getPuzzle()[r];

            ret += "| ";

            // For each column
            for (int c = 0; c < row.length; c++) {
                col = row[c];

                // Calculate if there should be a border
                rightBorder = ((c + 1) % 3) == 0 ? " | " : " ";

                ret += col + rightBorder;
            }

            // Calculate if there should be a border
            bottomBorder = ((r + 1) % 3) == 0 ? "\n-------------------------" : "";

            ret += bottomBorder + "\n";
        }

        return ret;
    }
}
