package ar.edu.ips.aus.seminario2.tetrominos.infra.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import ar.edu.ips.aus.seminario2.tetrominos.domain.Score;

@Entity(tableName = "score")
public class ScoreEntity implements Score {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "player_name")
    private String playerName;

    @ColumnInfo(name = "points")
    private int points;

    public ScoreEntity() {}

    @Ignore
    public ScoreEntity(int id, String playerName, int points) {
        this.id = id;
        this.playerName = playerName;
        this.points = points;
    }

    @Ignore
    public ScoreEntity(String playerName, int points) {
        this.playerName = playerName;
        this.points = points;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
