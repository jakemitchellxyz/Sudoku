package app;

/**
 * Created by Jake Mitchell on 23 Jul, 2016.
 * License: MIT
 */
class SudokuPuzzle {
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
     * Retrieve an individual square.
     *
     * @param x column of square - 1
     * @param y row of square - 1
     * @return int value of the square
     */
    public int getSquare (int x, int y) {
        return puzzle[y][x];
    }

    /**
     * Set the value of a single square.
     *
     * @param x column of square - 1
     * @param y row of square - 1
     * @param val value to set the square to
     */
    public void setSquare (int x, int y, int val) {
        puzzle[y][x] = val;
    }

    /**
     * Return the entire puzzle.
     *
     * @return returns entire puzzle numbers
     */
    public int[][] getPuzzle() {
        return puzzle;
    }

    /**
     * Reset the puzzle to all 0s
     */
    protected void resetPuzzle() {
        this.puzzle = new int[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
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

        // Remove squares from the puzzle so the user can fill them in
        remover.removeSquares();
    }
}
