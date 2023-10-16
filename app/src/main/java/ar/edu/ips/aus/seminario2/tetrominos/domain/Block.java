package ar.edu.ips.aus.seminario2.tetrominos.domain;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * Stackable element containing specific shape
 */
public class Block {

    int width;
    int height;
    boolean[][] cells;

    public Block(){
        this(1,1);
    }

    public Block(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new boolean[this.height][this.width];
    }

    public Block(@NonNull boolean[][] blockCells) {
        this.width = blockCells[0].length;
        this.height = blockCells.length;
        cells = blockCells;
    }

    /**
     * Copy constructor. To be used instead dreadful clone implementation.
     * @param aBlock block to copy
     */
    public Block(@NonNull Block aBlock) {
        this.width = aBlock.width;
        this.height = aBlock.height;
        this.cells = java.util.Arrays.stream(aBlock.cells).map(el -> el.clone()).toArray($ -> aBlock.cells.clone());
    }

    public boolean[][] getCells() {
        return this.cells;
    }

    public boolean isFilled(int deltaX, int deltaY) {
        return cells[deltaY][deltaX];
    }

    public void fill(int deltaX, int deltaY) {
        cells[deltaY][deltaX] = true;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    /**
     * Check if can be merged with another block.
     *
     * @param aBlock block to check
     * @return true if mergeable, false otherwise
     */
    public boolean canMerge(@NonNull Block aBlock) {
        if (aBlock.getWidth() == this.getWidth() &&
            aBlock.getHeight() == this.getHeight()) {
            for (int x = 0; x < getWidth(); x++) {
                for (int y = 0; y < getHeight(); y++) {
                    if (cells[y][x] & aBlock.cells[y][x]) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /**
     * Rotate block clockwise.
     */
    public void rotateClockwise() {
        throw new UnsupportedOperationException("Rotation unsupported.");
    }

    /**
     * Rotate block counter-clockwise.
     */
    public void rotateCounterClockwise() {
        throw new UnsupportedOperationException("Rotation unsupported.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Block)) return false;
        Block block = (Block) o;
        if (getWidth() != block.getWidth() || getHeight() != block.getHeight())
            return false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (cells[i][j] != block.cells[i][j])
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getWidth(), getHeight());
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

}
