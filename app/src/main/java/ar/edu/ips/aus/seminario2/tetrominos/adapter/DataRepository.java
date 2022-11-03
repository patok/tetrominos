package ar.edu.ips.aus.seminario2.tetrominos.adapter;

import android.app.Application;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ar.edu.ips.aus.seminario2.tetrominos.domain.Score;
import ar.edu.ips.aus.seminario2.tetrominos.infra.data.ScoreDao;
import ar.edu.ips.aus.seminario2.tetrominos.infra.data.ScoreDatabase;
import ar.edu.ips.aus.seminario2.tetrominos.infra.data.ScoreEntity;

/**
 * Intermediary object to manage data retrieval from diff datasources.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private ScoreDao dao;
    private Executor executor;

    private DataRepository(Application application) {
        ScoreDatabase db = ScoreDatabase.getDatabase(application.getApplicationContext());
        this.dao = db.scoreDAO();
        this.executor = Executors.newSingleThreadExecutor();
    }


    public static DataRepository getInstance(final Application application) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(application);
                }
            }
        }
        return sInstance;
    }

    public interface HighestScoresUpdater {
        void update(List<ScoreEntity> data);
    }

    @NonNull
    public void getHighScores(HighestScoresUpdater livedata) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                List<ScoreEntity> highestScores = dao.getHighestScores();
                livedata.update(highestScores);
            }
        });
    }

    public interface HighScoresThresholdUpdater {
        void update(Integer data);
    }

    public void getHighScoreThreshold(HighScoresThresholdUpdater livedata) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                int threshold = dao.getHighestScoresThreshold();
                livedata.update(threshold);
            }
        });
    }

    public void setPlayerScore(String playerName, int score) {
        this.executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(new ScoreEntity(playerName, score));
            }
        });
    }
}
