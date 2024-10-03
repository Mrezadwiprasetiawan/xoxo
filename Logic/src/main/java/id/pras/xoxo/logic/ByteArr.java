package id.pras.xoxo.logic;

/**
 * A class to support the creation of a fixed-size byte array,
 * allowing for easier filling and management of values.
 * 
 * @author [M Reza Dwi Prasetiawan]
 * @version 1.1
 */
public class ByteArr {
    private byte[] values; // Array to store byte values
    private int size;      // Size of the array
    private int index;     // Current index for adding values

    /**
     * Constructor to initialize the array with a specified size.
     *
     * @param size the desired size of the array
     */
    public ByteArr(int size) {
        this.size = size;
        this.index = 0;
        this.values = new byte[size];
    }

    /**
     * Adds a byte value to the array if there is space available.
     *
     * @param value the byte value to be added
     * @return true if the addition was successful, false if there is no space
     */
    public boolean add(byte value) {
        if (index + 1 == size) {
            return false;
        }
        this.values[index] = value;
        index++;
        return true;
    }

    /**
     * Adds all byte values from another array into this array.
     *
     * @param values the byte array to be added
     * @throws IllegalArgumentException if there is not enough space
     */
    public void addAll(byte[] values) {
        if (index == 0 && values.length <= size) {
            for (int i = 0; i < values.length; i++) {
                this.values[i] = values[i];
            }
        } else if (values.length <= (size - index)) {
            for (int i = 0; i < values.length; i++) {
                this.values[index++] = values[i];
            }
        } else {
            throw new IllegalArgumentException(
                "Not enough space! Empty space: " + (size - index) + "\tValues size: " + values.length);
        }
    }

    /**
     * Retrieves the byte value at a specified index.
     *
     * @param index the index of the value to retrieve
     * @return the byte value at the specified index
     * @throws IndexOutOfBoundsException if the index is greater than or equal to the size of the array
     */
    public byte get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(
                "Can't get value at Index >= Size! index: " + index + " size: " + size);
        }
        return this.values[index];
    }

    /**
     * Resets the byte value at the specified index to zero.
     *
     * @param index the index to be reset
     */
    public void reset(int index) {
        this.values[index] = 0;
    }

    /**
     * Resets all values in the array to zero and sets the current index to zero.
     */
    public void resetAllToZero() {
        for (int i = 0; i < size; i++) {
            this.values[i] = 0;
        }
        index = 0;
    }

    /**
     * Retrieves the array of byte values.
     *
     * @return the byte array storing the values
     */
    public byte[] values() {
        return values;
    }

    /**
     * Calculates and returns the sum of all values in the array.
     *
     * @return the total sum of byte values in the array
     */
    public int sum() {
        int result = 0;
        for (int i = 0; i < size; i++) {
            result += this.values[i];
        }
        return result;
    }

    /**
     * Returns the current index where new values can be added.
     *
     * @return the current index
     */
    public int currIndex() {
        return index;
    }
}