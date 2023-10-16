package ar.edu.ips.aus.seminario2.tetrominos.domain;

import android.graphics.Point;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents area where blocks are stacked together.
 */
public class PlayField {

    public static final int HORIZONTAL_SIZE = 10;
    public static final int VERTICAL_SIZE = 22;
    private final int width;
    private final int height;

    private boolean[][] cells;
    private Tetromino.Shape[][] shapes;

    public PlayField(int width, int height) {
        this.width = width;
        this.height = height;
        cells = new boolean[height][width];
        shapes = new Tetromino.Shape[height][width];
    }

    /**
     * Extract equivalent block from field content, determined by
     * required position and dimensions.
     *
     * @param position block coordinates
     * @param deltaX offset in x
     * @param deltaY offset in y
     * @return equivalent Block
     */
    public Block getBlock(Point position, int deltaX, int deltaY) {
        boolean[][] newCells = new boolean[deltaY][deltaX];
        for (int x = 0; x < deltaX; x++) {
            for (int y = 0; y < deltaY; y++ ) {
                newCells[y][x] = true;
            }
        }
        for (int x = position.x; x < position.x + deltaX; x++) {
            for (int y = position.y; y < position.y + deltaY; y++) {
                if ( y >= 0 && y < cells.length  &&
                     x >= 0 && x < cells[0].length)
                    newCells[y-position.y][x-position.x] = cells[y][x];
            }
        }
        return new Block(newCells);
    }

    /**
     * Place the block in requested position within the field content.
     *
     * @param newBlock block to place
     * @param position coordinates to be placed in
     * @return true is possible to do, otherwise false
     */
    public boolean place(Block newBlock, Point position) {
        if (canBePositioned(newBlock, position)) {
            fill(newBlock, position);
            return true;
        }
        return false;
    }

    public boolean place(Tetromino tetro, Point position) {
        if (this.place((Block)tetro, position)) {
            fill(tetro, position);
            return true;
        }
        return false;
    }

    /**
     * Fills non empty cells from block in place.
     *
     * @param newBlock block to copy
     * @param position coordinates to copy to
     */
    private void fill(@NonNull Block newBlock, Point position) {
        // fill block into field cells
        for (int j = 0; j < newBlock.getHeight(); j++) {
            for (int i = 0; i < newBlock.getWidth(); i++) {
                if (newBlock.isFilled(i, j))
                    cells[position.y + j][position.x + i] = true;
            }
        }
    }

    private void fill(@NonNull Tetromino tetro, Point position) {
        for (int j = 0; j < tetro.getHeight(); j++) {
            for (int i = 0; i < tetro.getWidth(); i++) {
                if (tetro.isFilled(i, j))
                    shapes[position.y + j][position.x + i] = tetro.getShape();
            }
        }
    }

    /**
     * Check whether block can be positioned as required.
     *
     * @param aBlock block to check
     * @param position coordinates to check against
     * @return true if can be positioned, false otherwise
     */
    public boolean canBePositioned(@NonNull Block aBlock, Point position) {
        return getBlock(position, aBlock.getWidth(), aBlock.getHeight()).canMerge(aBlock);
    }

    /**
     * Remove all completed levels, starting from biggest level.
     * @return number of removed levels
     */
    public int removeCompletedLevels() {
        List<Integer> levelsToRemove = checkLevelCompletion();

        boolean[][] targetCells = new boolean[height][width];
        Tetromino.Shape[][] targetShapes = new Tetromino.Shape[height][width];

        int targetLevel = this.height-1;
        for (int sourceLevel = this.height-1; sourceLevel >= 0; sourceLevel--) {
            if (!levelsToRemove.contains(sourceLevel)) {
                for (int i = 0; i < this.width; i++) {
                    if (cells[sourceLevel][i]) {
                        targetCells[targetLevel][i] = true;
                        targetShapes[targetLevel][i] = shapes[sourceLevel][i];
                    }
                }
                targetLevel--;
            }
        }
        this.cells = targetCells;
        this.shapes = targetShapes;

        return levelsToRemove.size();
    }

    /**
     * Discriminate complete vs incomplete levels.
     *
     * @return list of complete levels
     */
    private @NonNull List<Integer> checkLevelCompletion() {
        List<Integer> complete = new ArrayList<>();
        for (int level = this.height-1; level >= 0; level--) {
            if (isLevelComplete(level))
                complete.add(level);
        }
        return complete;
    }

    private boolean isLevelComplete(int level) {
        for (int i = 0; i < width; i++) {
            if (!cells[level][i])
                return false;
        }
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        throw new UnsupportedOperationException();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public boolean[][] getCells() {
        return cells;
    }

    public Tetromino.Shape[][] getShapes() {
        return shapes;
    }

}
