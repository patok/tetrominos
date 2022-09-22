package ar.edu.ips.aus.seminario2.tetrominos;

import android.util.Log;

import androidx.annotation.NonNull;

public class GameThread extends Thread {
    public static final int DELAY_MS = 25;
    private final Game game;
    private PlayFieldView gameView;
    private boolean running;
    private boolean paused;

    public GameThread( Game app){
        super();
        this.running = true;
        this.game = app;
        this.gameView = null;
    }

    @Override
    public void run() {
        final int COUNT_INTERVAL = 60;
        long startWhen = System.nanoTime();
        int intervalCount = 0;

        while (running){
            try {
                if (!paused) {
                    game.update(intervalCount);
                    if (this.gameView != null)
                        this.gameView.postInvalidate();
                }
                Thread.sleep(DELAY_MS);
                intervalCount++;
                if (COUNT_INTERVAL <= intervalCount){
                    long now = System.nanoTime();
                    double framesPerSec = 1000000000.0 / ((now - startWhen) / (intervalCount));
                    Log.d("FPS", String.format("%2.2f", framesPerSec));
                    startWhen = now;
                    intervalCount = 0;
                }
            } catch (Exception e){
                // intentionally left blank
            } finally {
                // intentionally left blank
            }
        }
    }

    public void setGameView(@NonNull PlayFieldView view) {
        this.gameView = view;
    }

    public void finish() {
        this.running = false;
    }

    public void pause() {
        this.paused = true;
    }

    public void rerun() {
        this.paused = false;
    }
}
