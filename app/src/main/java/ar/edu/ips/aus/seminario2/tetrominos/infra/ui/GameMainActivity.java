package ar.edu.ips.aus.seminario2.tetrominos.infra.ui;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import ar.edu.ips.aus.seminario2.tetrominos.R;
import ar.edu.ips.aus.seminario2.tetrominos.adapter.DataRepository;
import ar.edu.ips.aus.seminario2.tetrominos.adapter.HighScoreVO;
import ar.edu.ips.aus.seminario2.tetrominos.adapter.PlayFieldViewModel;
import ar.edu.ips.aus.seminario2.tetrominos.app.Game;
import ar.edu.ips.aus.seminario2.tetrominos.controller.GameThread;
import ar.edu.ips.aus.seminario2.tetrominos.domain.Tetromino;

public class GameMainActivity extends AppCompatActivity {

    private static final String TAG = "Game Activity";
    private GameThread gameThread;
    private Handler gameMessageHandler;
    private Handler mainMessageHandler;
    public static final int GAME_FINISHED = 0;
    private DataRepository dataRepository;
    private GestureDetector gestureDetector;
    private Integer scoreThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);

        Game game = (Game) getApplication();
        // TODO TP1 seleccionar el nivel de dificultad inicial
        game.reset();
        Log.i(TAG, "Init game");

        PlayFieldViewModel model = initViewModel();
        initControlThread(game, model);

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

        initRepository(game);

        initGestureDetector();
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

        final Observer<Integer> scoreThresholdObserver = new Observer<>() {
            @Override
            public void onChanged(Integer threshold) {
                GameMainActivity.this.scoreThreshold = threshold;
            }
        };
        model.getHighScoreThreshold().observe(this, scoreThresholdObserver);

        return model;
    }

    /**
     * Create and start control thread.
     *
     * @param game
     * @param model
     */
    private void initControlThread(Game game, PlayFieldViewModel model) {
        gameThread = GameThread.initThread(game);
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
                    HighScoreVO helper = new HighScoreVO();
                    helper.message = msg.arg1 > scoreThreshold ? "Felicitaciones, nuevo record!" : "";
                    helper.newHighScore = msg.arg1 > scoreThreshold ? true : false;
                    helper.repo = dataRepository;
                    helper.playerScore = msg.arg1;
                    // TODO TP1 recuperar el nombre del jugador
                    //  de las SharedPreferences, puede ser conveniente en este punto
                    DialogFragment gameOverDialog =
                            new GameOverDialog(GameMainActivity.this, helper);
                    gameOverDialog.show(getSupportFragmentManager(), "High score");
                    break;
                default:
                    break;
            }
        }
    }


    private void initRepository(Game game) {
        this.dataRepository = DataRepository.getInstance(game);
    }

    private void initGestureDetector() {
        gestureDetector = new GestureDetector(this, new GameGestureDetector());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }

    private class GameGestureDetector implements GestureDetector.OnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        private static final String MOTION_TAG = "MOTION";

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // intentionally left blank
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.v(MOTION_TAG, "single tap up detected!");
            // TODO TP1 implemente movimiento a la derecha e izquierda
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // intentionally left blank
        }

        @Override
        public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velX, float velY) {
            boolean eventConsumed = false;
            float diffY = moveEvent.getY() - downEvent.getY();
            float diffX = moveEvent.getX() - downEvent.getX();

            if (Math.abs(diffX) > Math.abs(diffY)){
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight(diffX);
                    } else {
                        onSwipeLeft(diffX);
                    }
                    eventConsumed = true;
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velY) > SWIPE_VELOCITY_THRESHOLD){
                    if (diffY > 0) {
                        onSwipeBottom(diffY);
                    } else {
                        onSwipeTop(diffY);
                    }
                    eventConsumed = true;
                }
            }
            return eventConsumed;

        }

        private void onSwipeTop(float diffY) {
            // intentionally left blank
        }

        private void onSwipeBottom(float diffY) {
            gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.FALL_DOWN));
        }

        private void onSwipeLeft(float diffX) {
            gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.ROTATE_CCW));
        }

        private void onSwipeRight(float diffX) {
            gameMessageHandler.sendMessage(gameMessageHandler.obtainMessage(Game.ROTATE_CW));
        }

    }
}