package utils;

import java.awt.Point;
import java.util.function.BiConsumer;

/**
 * A bi-dimensional array implemented linearly with a single array.
 */
public class Array2D<T>{
    private final int cols;
    private final Object[] array;

    /**
     * Creates a new rows by cols matrix.
     * 
     * @param rows
     * @param cols
     */
    public Array2D(int rows, int cols) {
        this.cols = cols;
        this.array = new Object[rows*cols];
    }

    private int getIndex(int row, int col) { return row*cols + col; }

    /**
     * Gets the value at (row, col).
     * 
     * @param row
     * @param col
     * @return
     */
    @SuppressWarnings("unchecked")
    public T get(int row, int col) {
        return (T)array[getIndex(row, col)];
    }

    /**
     * Sets the value at (row, col).
     * 
     * @param row
     * @param col
     * @param value
     */
    public void set(int row, int col, T value) {
        array[getIndex(row, col)] = value;
    }

    /**
     * Loops over the whole matrix, from (0, 0) to (n, m).
     * 
     * @param consumer
     */
    @SuppressWarnings("unchecked")
    public void foreach(BiConsumer<T, Point> consumer) {
        for (int i = 0; i < array.length; i++) {
            consumer.accept(
                (T)array[i], 
                new Point(i/cols, i%cols)
            );
        }
    }
}
