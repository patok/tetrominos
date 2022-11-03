package ar.edu.ips.aus.seminario2.tetrominos.adapter;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import ar.edu.ips.aus.seminario2.tetrominos.infra.data.ScoreEntity;
import ar.edu.ips.aus.seminario2.tetrominos.infra.ui.PlayFieldView;
import ar.edu.ips.aus.seminario2.tetrominos.R;
import ar.edu.ips.aus.seminario2.tetrominos.app.Game;
import ar.edu.ips.aus.seminario2.tetrominos.domain.PlayField;
import ar.edu.ips.aus.seminario2.tetrominos.domain.Tetromino;

public class PlayFieldViewModel extends AndroidViewModel
        implements DataRepository.HighScoresThresholdUpdater {

    public final Map<Tetromino.Shape, Bitmap> shapeResources = new HashMap<>();
    public Bitmap phantomResource;

    public final PlayField playField;
    public final Tetromino currentBlock;
    public final Game game;
    private PlayFieldView gameView;

    private final DataRepository dataRepo;

    public PlayFieldViewModel(@NonNull Application application) {
        super(application);
        game = (Game)application;
        playField = ((Game)application).playField;
        currentBlock = ((Game)application).currentBlock;
        setupShapeResources(application);

        dataRepo = DataRepository.getInstance(application);
        dataRepo.getHighScoreThreshold(this);
    }

    private void setupShapeResources(@NonNull Application application) {
        shapeResources.put(Tetromino.Shape.T, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell1));
        shapeResources.put(Tetromino.Shape.J, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell2));
        shapeResources.put(Tetromino.Shape.Z, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell3));
        shapeResources.put(Tetromino.Shape.O, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell4));
        shapeResources.put(Tetromino.Shape.S, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell5));
        shapeResources.put(Tetromino.Shape.L, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell6));
        shapeResources.put(Tetromino.Shape.I, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell7));
        phantomResource = BitmapFactory.decodeResource(application.getResources(), R.drawable.phcell);
    }

    public void setGameView(PlayFieldView playFieldView) {
        this.gameView = playFieldView;
    }

    private MutableLiveData<Tetromino> observableNextBlock = new MutableLiveData<>();
    private MutableLiveData<Integer> observableScore = new MutableLiveData<>();
    private MutableLiveData<Integer> observableLevel = new MutableLiveData<>();
    private MutableLiveData<Integer> highScoreThreshold = new MutableLiveData<>();

    public LiveData<Tetromino> getNextBlock() {
        return observableNextBlock;
    }

    public LiveData<Integer> getScore() {
        return observableScore;
    }

    public LiveData<Integer> getLevel() {
        return observableLevel;
    }

    /**
     * Notify model game data has changed.
     */
    public void update() {
        this.gameView.postInvalidate();
        observableNextBlock.postValue(game.nextBlock);
        observableScore.postValue(game.getScore());
        observableLevel.postValue(game.getGameLevel());
    }

    public LiveData<Integer> getHighScoreThreshold() { return highScoreThreshold; }

    @Override
    public void update(Integer data) {
        highScoreThreshold.postValue(data);
    }
}
