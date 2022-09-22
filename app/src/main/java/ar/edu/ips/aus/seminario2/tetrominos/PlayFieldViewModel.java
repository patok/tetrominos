package ar.edu.ips.aus.seminario2.tetrominos;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.HashMap;
import java.util.Map;

public class PlayFieldViewModel extends AndroidViewModel {

    public final Map<Tetromino.Shape, Bitmap> shapeResources = new HashMap<>();

    public final PlayField playField;
    public final Tetromino currentBlock;
    public final Game game;

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



}
