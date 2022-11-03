package ar.edu.ips.aus.seminario2.tetrominos.infra.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ScoreEntity.class}, version = 1, exportSchema = false)
public abstract class ScoreDatabase extends RoomDatabase {

    public abstract ScoreDao scoreDAO();

    private static volatile ScoreDatabase instance;

    public static ScoreDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (ScoreDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            ScoreDatabase.class,
                            "score_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    db.execSQL("INSERT INTO score ('player_name', 'points' ) values ('Player Unknown', 0)");
                                }
                            })
                            .build();
                }
            }
        }
        return instance;
    }
}
