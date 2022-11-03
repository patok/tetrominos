package ar.edu.ips.aus.seminario2.tetrominos.app;

import android.app.Application;
import android.graphics.Point;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Random;

import ar.edu.ips.aus.seminario2.tetrominos.domain.Block;
import ar.edu.ips.aus.seminario2.tetrominos.domain.PlayField;
import ar.edu.ips.aus.seminario2.tetrominos.domain.Tetromino;

public class Game extends Application {

    public static final int INITIAL_POSITION_X = 3;
    public static final int INITIAL_POSITION_Y = 0;
    public static final String TAG = "Tetro Game";
    private static final int STEP_INTERVAL = 30;
    private static final int MAX_GAME_LEVEL = 10;
    public PlayField playField;
    public Tetromino currentBlock;
    public Tetromino nextBlock;
    public Point currentPosition;
    private int progress;
    public Point phantomPosition;
    public boolean showPhantomBlock;

    private enum Status {
        INITIAL,
        FREE_RUN,
        STOPPED
    }
    private Status status = Status.INITIAL;

    public static final int UPDATE = 0;
    public static final int MOVE_LEFT = 1;
    public static final int MOVE_RIGHT = 2;
    public static final int MOVE_DOWN = 3;
    public static final int ROTATE_CW = 4;
    public static final int ROTATE_CCW = 5;
    public static final int FALL_DOWN = 6;
    public static final int RESUME = 7;
    public static final int PAUSE = 8;
    public static final int STOP = 9;

    private int gameLevel = 1;
    private int score = 0;

    public Game() {
        super();
        reset();
    }

    public Game(int initialLevel) {
        this();
        if (initialLevel > 0 && initialLevel < MAX_GAME_LEVEL) {
            gameLevel = initialLevel;
        }
    }

    public void reset() {
        playField = new PlayField(PlayField.HORIZONTAL_SIZE, PlayField.VERTICAL_SIZE);

        nextBlock = newRandomBlock();
        currentBlock = newRandomBlock();
        currentPosition = new Point(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        status = Status.FREE_RUN;
        gameLevel = 1;
        score = 0;
    }

    /**
     * We update game status every predefined progress steps to differentiate
     * in-game object movement from UI refresh, then movement velocity
     * can be managed within the Game object independently
     *
     * @return boolean whether can further proceed or not
     */
    public boolean update() {
        // 1. generate random block
        // 2. while new block can be positioned/moved
        // 3.   move/rotate block
        // 4. otherwise
        // 5.   place block
        // 6.   remove block levels
        // 7. loop #1
        boolean proceed = true;
        switch (status) {
            case INITIAL:
                if (generateNewBlock()) {
                    status = Status.FREE_RUN;
                } else {
                    status = Status.STOPPED;
                }
                break;
            case FREE_RUN:
                if (progress % (STEP_INTERVAL/gameLevel) == 0) {
                    if (moveStep()) {
                        // future extra stuff
                    } else {
                        playField.place(currentBlock, currentPosition);
                        int levelsCompleted = playField.removeCompletedLevels();
                        updateScore(levelsCompleted);
                        status = Status.INITIAL;
                        this.showPhantomBlock = false;
                    }
                    progress = 1;
                } else
                    progress++;
                break;
            case STOPPED:
                proceed = false;
                Log.i(TAG, "Game is finished!");
                break;
        }
        return proceed;
    }

    private void updateScore(int levelsCompleted) {
        switch (levelsCompleted) {
            case 1:
                score += 100 * gameLevel;
                break;
            case 2:
                score += 200 * gameLevel;
                break;
            case 3:
                score += 400 * gameLevel;
                break;
            case 4:
                score += 800 * gameLevel;
                break;
            default:
                break;
        }
        gameLevel += score / (1000 * gameLevel * gameLevel);
    }

    private boolean generateNewBlock() {
        if (status != Status.INITIAL)
            return false;

        if (nextBlock == null)
            nextBlock = newRandomBlock();

        Point newPosition = new Point(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        if (playField.canBePositioned(nextBlock, newPosition)) {
            currentBlock = nextBlock;
            currentPosition = newPosition;
            nextBlock = newRandomBlock();
            Log.i(TAG, "New block generated");
            computePhantomBlock();
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
            Log.i(TAG, "Moved free block down");
            computePhantomBlock();
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
            Log.i(TAG, "Moved free block right");
            computePhantomBlock();
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
            Log.i(TAG, "Moved free block left");
            computePhantomBlock();
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
            Log.i(TAG, "Rotated free block CW");
            computePhantomBlock();
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
            Log.i(TAG, "Rotated free block CCW");
            computePhantomBlock();
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
        Log.i(TAG, "Free block fall down");
        return false;
    }

    public boolean computePhantomBlock() {
        if (status != Status.FREE_RUN)
            return false;

        int dx = 0, dy = 1;
        Point nextPosition = new Point(currentPosition.x, currentPosition.y);
        nextPosition.offset(dx, dy);
        while (playField.canBePositioned(currentBlock, nextPosition)) {
            nextPosition.offset(dx, dy);
        }
        nextPosition.offset(dx, -dy);
        phantomPosition = nextPosition;
        this.showPhantomBlock = true;
        return false;
    }

    public int getScore() {
        return this.score;
    }

    public int getGameLevel() {
        return this.gameLevel;
    }
}
