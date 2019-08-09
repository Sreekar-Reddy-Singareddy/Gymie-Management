package singareddy.productionapps.gymiemanagement;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import singareddy.productionapps.gymiemanagement.entities.Gym;

public class DataRepository {
    private static String TAG = "DataRepository";

    private static DataRepository DATA_REPO;
    private LocalDB localDB;
    private ExecutorService executorService;

    private DataRepository(Context context) {
        localDB = LocalDB.getDbInstance(context);
        executorService = Executors.newSingleThreadExecutor();
    }

    public static DataRepository getDataRepo(Context context) {
        if (DATA_REPO == null) DATA_REPO = new DataRepository(context);
        return DATA_REPO;
    }

    public void dummy_insertGym() {
        Gym g1 = new Gym("Super Fit","Ameerpet, Telangana",500011,"superfit@gmail.com","Ramesh",9177170709l);
        Gym g2 = new Gym("Size Zero","Nizampet, Telangana",500123,"sizezero@gmail.com","Anushka Shetty",9123470709l);
        Gym g3 = new Gym("Love Fitness","Banjara Hills, Telangana",512011,"lovefit@gmail.com","Surya",9177170709l);
        executorService.submit(()->{
            Log.i(TAG, "dummy_insertGym: *");
            // Insert this gym into Room DB
            localDB.getGymDao().insertGym(g1);
            localDB.getGymDao().insertGym(g2);
            localDB.getGymDao().insertGym(g3);
        });
    }

    public List<Gym> dummy_getGyms() {
//        dummy_insertGym();
        try {
            return executorService.submit(()->localDB.getGymDao().getAllGyms()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
