package puzzle;

import java.util.Collections;
import java.util.LinkedList;

class Pen {
    private LinkedList<Integer> pen;

    /**
     * Constructor for an ordered Pen
     */
    Pen() {
        pen = new LinkedList<>();

        // empty the array
        pen.clear();

        // Add all numbers 1-9 to the pen
        for (int i = 1; i <= 9; i++) {
            pen.add(i);
        }
    }

    /**
     * Constructor for randomizing pen.
     *
     * @param randomize boolean if pen should be random
     */
    Pen(boolean randomize) {
        this();

        // Randomize pen if necessary
        if (randomize)
            Collections.shuffle(pen);
    }

    /**
     * Get the next value of the pen.
     */
    int next() {
        return pen.pop();
    }

    /**
     * Add item to the pen.
     *
     * @param item to add
     */
    void add(int item) {
        pen.addLast(item);
    }

    /**
     * Remove specific item from the pen.
     *
     * @param item value to remove
     */
    void remove(int item) {
        int index = pen.indexOf(item);
        if (index > -1) pen.remove(index);
    }

    /**
     * Ensure the pen is sorted.
     */
    @SuppressWarnings("initialization")
    void sort() {
        pen.sort(null);
    }

    /**
     * Generate an array of the items in the pen to iterate over.
     *
     * @return list of items in the pen
     */
    int[] iterator() {
        int[] result = new int[10];
        int i = 0;
        for (Integer aPen : pen) {
            result[i++] = aPen;
        }
        return result;
    }

    /**
     * Check if pen is empty.
     *
     * @return boolean if empty
     */
    boolean isEmpty() {
        return pen.isEmpty();
    }
}
