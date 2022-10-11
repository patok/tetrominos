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
    private static final int STEP_INTERVAL = 30;

    public PlayField playField;
    public Tetromino currentBlock;
    public Tetromino nextBlock;
    public Point currentPosition;
    private GameThread controlThread;
    private int progress;

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

    private static final int LEVEL_MAX = 10;
    int startingLevel = 1;
    private int gameLevel = startingLevel;
    private int score = 0;

    public Game() {
        super();
        reset();
    }

    // TODO TP1 use a startup activity to select initial game level
    public void reset(int initialLevel) {
        if (initialLevel > 0 && initialLevel < LEVEL_MAX) {
            startingLevel = initialLevel;
        }
        reset();
    }

    public void reset() {
        playField = new PlayField(PlayField.HORIZONTAL_SIZE, PlayField.VERTICAL_SIZE);

        nextBlock = newRandomBlock();
        currentBlock = newRandomBlock();
        currentPosition = new Point(INITIAL_POSITION_X, INITIAL_POSITION_Y);
        status = Status.FREE_RUN;
        gameLevel = this.startingLevel;
        score = 0;
    }

    public @NonNull GameThread initThread() {
        controlThread = new GameThread(this);
        controlThread.start();
        return controlThread;
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
                        int levelsCompleted = playField.removeCompleteLevels();
                        updateScore(levelsCompleted);
                        status = Status.INITIAL;
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

    public int getScore() {
        return this.score;
    }

    public int getGameLevel() {
        return this.gameLevel;
    }
}
