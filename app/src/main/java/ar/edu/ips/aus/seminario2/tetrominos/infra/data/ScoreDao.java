package ar.edu.ips.aus.seminario2.tetrominos.infra.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ar.edu.ips.aus.seminario2.tetrominos.domain.Score;

@Dao
public interface ScoreDao {

    @Query("SELECT * FROM score ORDER BY points DESC LIMIT 10")
    List<ScoreEntity> getHighestScores();

    @Query("SELECT min(points) FROM (SELECT * FROM score ORDER BY points DESC LIMIT 10)")
    int getHighestScoresThreshold();

    @Insert
    void insert(ScoreEntity score);

    @Update
    void update(ScoreEntity score);

    @Delete
    void delete(ScoreEntity score);

}
