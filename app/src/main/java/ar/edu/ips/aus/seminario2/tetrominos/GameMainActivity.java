package ar.edu.ips.aus.seminario2.tetrominos;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class GameMainActivity extends AppCompatActivity {

    private static final String TAG = "Tetro Game";
    private GameThread gameThread;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_board);

        PlayFieldViewModel model = new ViewModelProvider(this).get(PlayFieldViewModel.class);
        initGame(model);
        Log.i(TAG, "Init game");

        Button leftButton = findViewById(R.id.leftButton);
        leftButton.setOnClickListener(v -> game.moveLeft());

        Button rightButton = findViewById(R.id.rightButton);
        rightButton.setOnClickListener(v -> game.moveRight());

        Button rotateLeftButton = findViewById(R.id.rotateLeftButton);
        rotateLeftButton.setOnClickListener(v -> game.rotateCCW());

        Button rotateRightButton = findViewById(R.id.rotateRightButton);
        rotateRightButton.setOnClickListener(v -> game.rotateCW());

        Button downButton = findViewById(R.id.downButton);
        downButton.setOnClickListener(v -> game.moveStep());
        downButton.setOnLongClickListener(v -> {
            game.fallDown();
            return false;
        }
        );
    }

    private void initGame(PlayFieldViewModel model) {
        FrameLayout layoutCanvas = findViewById(R.id.frameLayout);

        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        PlayFieldView playFieldView = new PlayFieldView(this, model);
        playFieldView.setLayoutParams(params);
        playFieldView.setBackgroundColor(Color.parseColor("#954A8781"));
        layoutCanvas.addView(playFieldView);

        game = (Game) getApplication();
        gameThread = game.initThread();
        gameThread.setGameView(playFieldView);
    }

    @Override
    public void onResume() {
        super.onResume();
        gameThread.rerun();
        Log.i(TAG, "Resuming game");
    }

    @Override
    public void onPause() {
        super.onPause();
        gameThread.pause();
        Log.i(TAG, "Pausing game");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gameThread.finish();
        Log.i(TAG,"Finish game");
    }
}