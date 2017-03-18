package puzzle;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by Jake Mitchell on 15 Sept, 2016.
 * License: MIT
 */
class Square {
    private int x;
    private int y;
    private int box;

    // Global BOXES and corresponding points
    private static final HashMap<Square, Integer> BOXES = new HashMap<>();
    static {
        int box;

        // For each column
        for(int x = 0; x < 9; x++) {
            // For each row
            for(int y = 0; y < 9; y++) {
                // Magical function with help from Yunze li
                box = (int) Math.round(Math.ceil((x + 1) / 3.0) + 3 * Math.ceil((y + 1) / 3.0) - 4);

                // Insert coordinate and box into HashMap
                BOXES.put(new Square(x, y), box);
            }
        }
    }

    /**
     * Constructor.
     *
     * @param x x value of square
     * @param y y value of square
     */
    Square(int x, int y) {
        this.x = x;
        this.y = y;

        // Magical method with help from Yunze Li
        this.box = (int) (Math.ceil((x + 1) / 3.0) + 3 * Math.ceil((y + 1) / 3.0) - 4);
    }

    /**
     * Get all squares in this box.
     *
     * @return ArrayList of the points
     */
    LinkedList<Square> getAllInBox() {
        LinkedList<Square> pointsInBox = new LinkedList<>();

        for(Square square : BOXES.keySet()) {
            if (BOXES.get(square) == this.box) {
                pointsInBox.add(square);
            }
        }

        return pointsInBox;
    }

    /**
     * Determines if the queried square affects this one.
     *
     * @param other square to query
     * @return whether the square affects this one
     */
    boolean affects (Square other) {
        return other.getX() == this.getX() // If in this column or
                || other.getY() == this.getY() // If in this row or
                || BOXES.get(other) == this.box; // If in this box
    }

    /**
     * Returns the x value.
     *
     * @return x value of square
     */
    int getX() {
        return this.x;
    }

    /**
     * Returns the y value.
     *
     * @return y value of square
     */
    int getY() {
        return this.y;
    }

    /**
     * Customize toString method.
     *
     * @return "Square[ x, y ]"
     */
    @Override
    public String toString() {
        return "Square[ " + this.getX() + ", " + this.getY() + " ]";
    }

    /**
     * Custom equality comparison.
     *
     * @param other square to query
     * @return whether the two are equivalent
     */
    @Override
    public boolean equals(Object other) {
        // Ensure other is another Square
        if (!(other instanceof Square)) return false;

        // Set the type to a square
        Square otherSquare = (Square) other;

        // Return their equivalency
        return (this.getX() == otherSquare.getX()) && (this.getY() == otherSquare.getY());
    }
}
