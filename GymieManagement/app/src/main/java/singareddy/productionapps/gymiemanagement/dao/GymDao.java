package singareddy.productionapps.gymiemanagement.dao;



import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import singareddy.productionapps.gymiemanagement.entities.Gym;

@Dao
public interface GymDao {
    @Query("SELECT * FROM Gym")
    public List<Gym> getAllGyms ();

    @Insert
    public long insertGym(Gym g);
}
