package ar.edu.ips.aus.seminario2.tetrominos;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

public class GameThread extends HandlerThread {
    public static final int DELAY_MS = 40;
    private final Game game;
    private PlayFieldViewModel gameViewModel;
    private boolean paused;

    private Handler messageHandler;
    private Handler activityMessageHandler;

    public GameThread(Game app){
        super("Game thread");
        this.paused = true;
        this.game = app;
        this.gameViewModel = null;
        this.activityMessageHandler = null;
    }

    @Override
    protected void onLooperPrepared() {
        messageHandler = new Handler(getLooper()) {
            final int COUNT_INTERVAL = 60;
            long startWhen = System.nanoTime();
            int intervalCount = 0;

            @Override
            public void handleMessage(android.os.Message msg) {
                // process incoming messages here
                // this will run in non-ui/background thread
                synchronized (game.playField) {
                    switch (msg.what) {
                        case Game.UPDATE:
                            if (!paused) {
                                if (!game.update()) {
                                    paused = true;
                                    Message message = activityMessageHandler.obtainMessage(
                                            GameMainActivity.GAME_FINISHED);
                                    activityMessageHandler.sendMessage(
                                            message);
                                }
                                if (gameViewModel != null)
                                    gameViewModel.update();
                            }
                            sendMessageDelayed(obtainMessage(Game.UPDATE), DELAY_MS);
                            intervalCount++;
                            if (COUNT_INTERVAL <= intervalCount) {
                                long now = System.nanoTime();
                                double framesPerSec = 1000000000.0 / ((now - startWhen) / (intervalCount));
                                Log.d("FPS", String.format("%2.2f", framesPerSec));
                                startWhen = now;
                                intervalCount = 0;
                            }
                            break;
                        case Game.MOVE_LEFT:
                            if (!paused)
                                game.moveLeft();
                            break;
                        case Game.MOVE_RIGHT:
                            if (!paused)
                                game.moveRight();
                            break;
                        case Game.MOVE_DOWN:
                            if (!paused)
                                game.moveStep();
                            break;
                        case Game.FALL_DOWN:
                            if (!paused)
                                game.fallDown();
                            break;
                        case Game.ROTATE_CW:
                            if (!paused)
                                game.rotateCW();
                            break;
                        case Game.ROTATE_CCW:
                            if (!paused)
                                game.rotateCCW();
                            break;
                        case Game.PAUSE:
                            paused = true;
                            break;
                        case Game.STOP:
                            getLooper().quit();
                            paused = true;
                            break;
                        case Game.RESUME:
                            paused = false;
                            break;
                        default:
                            break;
                    }
                }
            }
        };
        // begin movement
        messageHandler.sendMessage(messageHandler.obtainMessage(Game.UPDATE));
    }

    public void setGameViewModel(@NonNull PlayFieldViewModel model) {
        this.gameViewModel = model;
    }

    public Handler getMessageHandler() {
        return messageHandler;
    }

    public void setActivityMessageHandler(Handler handler) {
        this.activityMessageHandler = handler;
    }
}
