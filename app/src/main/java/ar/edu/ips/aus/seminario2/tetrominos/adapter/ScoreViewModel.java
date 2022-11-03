package ar.edu.ips.aus.seminario2.tetrominos.adapter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import ar.edu.ips.aus.seminario2.tetrominos.infra.data.ScoreEntity;

public class ScoreViewModel extends AndroidViewModel
        implements DataRepository.HighestScoresUpdater {

    private MutableLiveData<List<ScoreEntity>> highestScores = new MutableLiveData<>();
    private DataRepository dataRepo;

    public ScoreViewModel(@NonNull Application application) {
        super(application);
        dataRepo = DataRepository.getInstance(application);

        dataRepo.getHighScores(this);
    }

    public LiveData<List<ScoreEntity>> getHighestScores() {
        return highestScores;
    }

    @Override
    public void update(List<ScoreEntity> data) {
        highestScores.postValue(data);
    }
}
