package singareddy.productionapps.gymiemanagement.gyms;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import singareddy.productionapps.gymiemanagement.R;
import singareddy.productionapps.gymiemanagement.common.ui.AllGymsFragment;
import singareddy.productionapps.gymiemanagement.entities.Gym;
import singareddy.productionapps.gymiemanagement.listeners.GymClickListener;

public class ManageGymsScreen extends AppCompatActivity implements GymClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_gyms);
        initialiseUI();
    }

    private void initialiseUI() {
        Fragment allGymsFragment = new AllGymsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frag_all_gyms_fl_container, allGymsFragment)
                .addToBackStack("AllGymsFragment")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.manage_gyms_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.manage_gyms_add_gym_item:
                // UI flow for adding a new gym here
                Intent addNewGymIntent = new Intent(this, AddGymScreen.class);
                startActivity(addNewGymIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGymSelected(Gym gym) {
        AlertDialog gymDetailedInfoDialog = new AlertDialog.Builder(this)
                .setTitle(gym.getName())
                .setMessage(gym.getAddress())
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the owner of the gym
                        Intent callOnwerIntent = new Intent(Intent.ACTION_DIAL,
                                Uri.parse("tel:"+gym.getOwnerMobile().toString()));
                        startActivity(callOnwerIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog box
                        dialog.dismiss();
                    }
                }).create();
        gymDetailedInfoDialog.show();
    }
}
