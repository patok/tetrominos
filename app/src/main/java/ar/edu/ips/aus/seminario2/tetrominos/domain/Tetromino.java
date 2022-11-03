package ar.edu.ips.aus.seminario2.tetrominos.domain;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Tetromino extends Block {

    private final Shape shape;

    public enum Shape {
        T {
            @Override
            boolean[][] getCells() {
                return new boolean[][]{
                        {false, false, false},
                        {true, true, true},
                        {false, true, false}
                };
            }
        },
        J {
            @Override
            boolean[][] getCells() {
                return new boolean[][]{
                        {false, false, false},
                        {true, true, true},
                        {false, false, true}
                };
            }
        },
        Z {
            @Override
            boolean[][] getCells() {
                return new boolean[][]{
                        {false, false, false},
                        {true, true, false},
                        {false, true, true}
                };
            }
        },
        O {
            @Override
            boolean[][] getCells() {
                return new boolean[][]{
                        {true, true},
                        {true, true}
                };
            }
        },
        S {
            @Override
            boolean[][] getCells() {
                return new boolean[][]{
                        {false, false, false},
                        {false, true, true},
                        {true, true, false}
                };
            }
        },
        L {
            @Override
            boolean[][] getCells() {
                return new boolean[][]{
                        {false, false, false},
                        {true, true, true},
                        {true, false, false}
                };
            }
        },
        I {
            @Override
            boolean[][] getCells() {
                return new boolean[][]{
                        {false, false, false, false},
                        {true, true, true, true},
                        {false, false, false, false},
                        {false, false, false, false}
                };
            }
        };

        abstract boolean[][] getCells();
    }

    public Tetromino(Shape shape) {
        super(shape.getCells());
        this.shape = shape;
    }

    public Tetromino(Tetromino aBlock) {
        super(aBlock);
        this.shape = aBlock.shape;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tetromino)) return false;
        if (!super.equals(o)) return false;
        Tetromino tetromino = (Tetromino) o;
        return shape == tetromino.shape;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), shape);
    }

    @Override
    public void rotateClockwise() {
        this.cells = rotateCells(true);
    }

    @NonNull
    public boolean[][] rotateCells(boolean clockwise) {
        // rotation relies on a perfect square block
        boolean[][] newCells = new boolean[this.width][this.height];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (clockwise)
                    newCells[j][this.height-1-i] =  this.cells[i][j];
                else
                    newCells[this.width-1-j][i] =  this.cells[i][j];
            }
        }
        return newCells;
    }

    @Override
    public void rotateCounterClockwise() {
        this.cells = rotateCells(false);
    }

}
