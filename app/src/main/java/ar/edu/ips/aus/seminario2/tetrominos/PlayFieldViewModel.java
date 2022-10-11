package ar.edu.ips.aus.seminario2.tetrominos;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

public class PlayFieldViewModel extends AndroidViewModel {

    public final Map<Tetromino.Shape, Bitmap> shapeResources = new HashMap<>();

    public final PlayField playField;
    public final Tetromino currentBlock;
    public final Game game;
    private PlayFieldView gameView;

    public PlayFieldViewModel(@NonNull Application application) {
        super(application);
        game = (Game)application;
        playField = ((Game)application).playField;
        currentBlock = ((Game)application).currentBlock;
        shapeResources.put(Tetromino.Shape.T, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell1));
        shapeResources.put(Tetromino.Shape.J, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell2));
        shapeResources.put(Tetromino.Shape.Z, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell3));
        shapeResources.put(Tetromino.Shape.O, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell4));
        shapeResources.put(Tetromino.Shape.S, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell5));
        shapeResources.put(Tetromino.Shape.L, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell6));
        shapeResources.put(Tetromino.Shape.I, BitmapFactory.decodeResource(application.getResources(), R.drawable.cell7));

    }


    public void setGameView(PlayFieldView playFieldView) {
        this.gameView = playFieldView;
    }

    private MutableLiveData<Tetromino> observableNextBlock = new MutableLiveData<>();
    private MutableLiveData<Integer> observableScore = new MutableLiveData<>();
    private MutableLiveData<Integer> observableLevel = new MutableLiveData<>();

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
}
