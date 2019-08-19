package singareddy.productionapps.gymiemanagement.login;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import singareddy.productionapps.gymiemanagement.R;
import singareddy.productionapps.gymiemanagement.common.ui.AllGymsFragment;
import singareddy.productionapps.gymiemanagement.entities.Gym;
import singareddy.productionapps.gymiemanagement.listeners.GymClickListener;

// For everyone else other than Super Admin
// LoginScreen -> GymSelectScreen

public class GymSelectScreen extends AppCompatActivity implements GymClickListener {
    // Constants
    private static final String TAG = "GymSelectScreen";
    public static final String INTENT_EXTRA_LOGGED_USER_ID = "loggedUserId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_select);
        initialiseUI();
    }

    private void initialiseUI() {
        Fragment allGymsFragment = new AllGymsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_gym_select_fl_container, allGymsFragment)
                .addToBackStack("AllGymsFragment")
                .commit();
    }

    @Override
    public void onGymSelected(Gym gym) {
        Log.i(TAG, "onGymSelected: Selected Gym: "+gym.getName());
        Intent roleSelectIntent = new Intent(this, RoleSelectScreen.class);
        roleSelectIntent.putExtra(RoleSelectScreen.INTENT_EXTRA_SELECTED_GYM_NAME, gym.getName());
        roleSelectIntent.putExtra(RoleSelectScreen.INTENT_EXTRA_SELECTED_GYM_ADDRESS, gym.getAddress());
        startActivity(roleSelectIntent);
    }
}
