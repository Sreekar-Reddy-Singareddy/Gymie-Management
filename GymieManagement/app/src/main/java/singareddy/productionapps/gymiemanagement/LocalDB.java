package singareddy.productionapps.gymiemanagement;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import singareddy.productionapps.gymiemanagement.dao.GymDao;
import singareddy.productionapps.gymiemanagement.entities.Gym;

@Database(entities = {Gym.class}, version = 1, exportSchema = false)
public abstract class LocalDB extends RoomDatabase {

    private static LocalDB DB_INSTANCE;

    public static LocalDB getDbInstance(Context context) {
        if (DB_INSTANCE == null) {
            DB_INSTANCE = Room.databaseBuilder(context, LocalDB.class, "GymieDB")
                    .build();
        }
        return DB_INSTANCE;
    }

    public abstract GymDao getGymDao();
}
