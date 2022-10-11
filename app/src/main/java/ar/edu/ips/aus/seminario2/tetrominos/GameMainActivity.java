package ar.edu.ips.aus.seminario2.tetrominos;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class GameMainActivity extends AppCompatActivity {

    private static final String TAG = "Game Activity";
    private GameThread gameThread;
    private Handler gameMessageHandler;
    private Handler mainMessageHandler;
    public static final int GAME_FINISHED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);

        Log.i(TAG, "Starting game");
        Game game = (Game) getApplication();
        game.reset();

        PlayFieldViewModel model = initViewModel();
        initControlThread(model);

        Button leftButton = findViewById(R.id.leftButton);
        leftButton.setOnClickListener(v -> gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.MOVE_LEFT)));

        Button rightButton = findViewById(R.id.rightButton);
        rightButton.setOnClickListener(v -> gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.MOVE_RIGHT)));

        Button rotateLeftButton = findViewById(R.id.rotateLeftButton);
        rotateLeftButton.setOnClickListener(v -> gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.ROTATE_CCW)));

        Button rotateRightButton = findViewById(R.id.rotateRightButton);
        rotateRightButton.setOnClickListener(v -> gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.ROTATE_CW)));

        Button downButton = findViewById(R.id.downButton);
        downButton.setOnClickListener(v -> gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.MOVE_DOWN)));
        downButton.setOnLongClickListener(v -> {
            gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.FALL_DOWN));
            return false;
        });
    }

    /**
     * Init ViewModel and asociated views.
     *
     * @return model
     */
    private PlayFieldViewModel initViewModel() {
        PlayFieldViewModel model = new ViewModelProvider(this).get(PlayFieldViewModel.class);

        FrameLayout layoutCanvas = findViewById(R.id.frameLayout);

        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        PlayFieldView playFieldView = new PlayFieldView(this, model);
        playFieldView.setLayoutParams(params);
        playFieldView.setBackgroundColor(Color.parseColor("#954A8781"));
        layoutCanvas.addView(playFieldView);
        model.setGameView(playFieldView);

        FrameLayout nextBlockLayout = findViewById(R.id.nextBlockLayout);
        TetroView nextBlockView = new TetroView(this, model);
        nextBlockView.setLayoutParams(params);
        nextBlockLayout.addView(nextBlockView);
        final Observer<Tetromino> nextBlockObserver = tetromino -> nextBlockView.invalidate();
        model.getNextBlock().observe(this, nextBlockObserver);

        TextView scoreView = findViewById(R.id.scoreValue);
        final Observer<Integer> scoreObserver = newScore -> scoreView.setText(String.format("%05d", newScore));
        model.getScore().observe(this, scoreObserver);

        TextView levelView = findViewById(R.id.levelValue);
        final Observer<Integer> levelObserver = level -> levelView.setText(String.format("%d", level));
        model.getLevel().observe(this, levelObserver);

        return model;
    }

    /**
     * Create and start control thread.
     *
     * @param model
     */
    private void initControlThread(PlayFieldViewModel model) {
        gameThread = model.game.initThread();
        gameThread.setGameViewModel(model);
        mainMessageHandler = new MainMessageHandler(getMainLooper());
        gameThread.setActivityMessageHandler(mainMessageHandler);
    }

    @Override
    public void onResume() {
        super.onResume();
        // obtaining MessageHandler before this point will get null instead
        gameMessageHandler = gameThread.getMessageHandler();
        gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.RESUME));
        Log.i(TAG, "Resuming game");
    }

    @Override
    public void onPause() {
        super.onPause();
        gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.PAUSE));
        Log.i(TAG, "Pausing game");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.STOP));
        Log.i(TAG,"Finish game");
    }

    private class MainMessageHandler extends Handler {

        public MainMessageHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GAME_FINISHED:
                    GameOverDialog gameOverDialog = new GameOverDialog(GameMainActivity.this);
                    gameOverDialog.show(getSupportFragmentManager(), "game_over");
                    break;
                default:
                    break;
            }
        }
    }
}