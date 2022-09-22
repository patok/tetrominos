package ar.edu.ips.aus.seminario2.tetrominos;

import android.app.Application;
import android.graphics.Point;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Random;

public class Game extends Application {

    public static final int INITIAL_POSITION_X = 3;
    public static final int INITIAL_POSITION_Y = 0;
    public static final String TAG = "Tetro Game";
    public final PlayField playField;
    public Tetromino currentBlock;
    public Point currentPosition;
    private GameThread controlThread;
    private static final int STEP_INTERVAL = 30;

    private enum Status {
        INITIAL,
        FREE_RUN,
        FINISHED
    }
    private Status status = Status.INITIAL;

    public Game() {
        super();
        playField = new PlayField(PlayField.HORIZONTAL_SIZE, PlayField.VERTICAL_SIZE);

        currentBlock = newRandomBlock();
        currentPosition = new Point(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        status = Status.FREE_RUN;
    }

    public @NonNull GameThread initThread() {
        controlThread = new GameThread(this);
        controlThread.start();
        return controlThread;
    }

    public void update(int progress) {
        // 1. generate random block
        // 2. while new block can be positioned/moved
        // 3.   move/rotate block
        // 4. otherwise
        // 5.   place block
        // 6.   remove block levels
        // 7. loop #1
        switch (status) {
            case INITIAL:
                if (generateNewBlock()) {
                    status = Status.FREE_RUN;
                } else {
                    status = Status.FINISHED;
                }
                break;
            case FREE_RUN:
                if (progress % STEP_INTERVAL == 0) {
                    if (moveStep()) {
                        // future extra stuff
                    } else {
                        playField.place(currentBlock, currentPosition);
                        playField.removeCompleteLevels();
                        status = Status.INITIAL;
                    }
                }
                break;
            case FINISHED:
                Log.i(TAG, "Game is finished!");
                break;
        }

    }

    private boolean generateNewBlock() {
        if (status != Status.INITIAL)
            return false;

        Tetromino newBlock = newRandomBlock();
        Point newPosition = new Point(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        if (playField.canBePositioned(newBlock, newPosition)) {
            currentBlock = newBlock;
            currentPosition = newPosition;
            return true;
        }
        return false;
    }

    @NonNull
    private Tetromino newRandomBlock() {
        return new Tetromino(Tetromino.Shape.values()[new Random().nextInt(Tetromino.Shape.values().length)]);
    }

    public boolean moveStep() {
        return moveDown(1);
    }

    public boolean moveDown(int step) {
        if (status != Status.FREE_RUN)
            return false;

        int dx = 0, dy = step;
        Point nextPosition = new Point(currentPosition.x + dx, currentPosition.y + dy);
        if (playField.canBePositioned(currentBlock, nextPosition)) {
            currentPosition.offset(dx, dy);
            return true;
        }
        else
            return false;
    }

    public boolean moveRight() {
        if (status != Status.FREE_RUN)
            return false;

        int dx = 1, dy = 0;
        Point nextPosition = new Point(currentPosition.x + dx, currentPosition.y + dy);
        if (playField.canBePositioned(currentBlock, nextPosition)) {
            currentPosition.offset(dx, dy);
            return true;
        } else
            return false;
    }

    public boolean moveLeft() {
        if (status != Status.FREE_RUN)
            return false;

        int dx = -1, dy = 0;
        Point nextPosition = new Point(currentPosition.x + dx, currentPosition.y + dy);
        if (playField.canBePositioned(currentBlock, nextPosition)) {
            currentPosition.offset(dx, dy);
            return true;
        } else
            return false;
    }

    public boolean rotateCW() {
        if (status != Status.FREE_RUN)
            return false;

        Block newBlock = new Block(currentBlock.rotateCells(true));
        if (playField.canBePositioned(newBlock, currentPosition)) {
            currentBlock.rotateClockwise();
            return true;
        } else
            return false;
    }

    public boolean rotateCCW() {
        if (status != Status.FREE_RUN)
            return false;

        Block newBlock = new Block(currentBlock.rotateCells(false));
        if (playField.canBePositioned(newBlock, currentPosition)) {
            currentBlock.rotateCounterClockwise();
            return true;
        } else
            return false;
    }

    public boolean fallDown() {
        if (status != Status.FREE_RUN)
            return false;

        int dx = 0, dy = 1;
        Point nextPosition = new Point(currentPosition.x, currentPosition.y);
        nextPosition.offset(dx, dy);
        while (playField.canBePositioned(currentBlock, nextPosition)) {
            nextPosition.offset(dx, dy);
        }
        nextPosition.offset(dx, -dy);
        currentPosition = nextPosition;
        return false;
    }

}
