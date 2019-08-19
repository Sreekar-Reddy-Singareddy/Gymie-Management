package singareddy.productionapps.gymiemanagement.gyms;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;

import singareddy.productionapps.gymiemanagement.R;
import singareddy.productionapps.gymiemanagement.entities.Gym;

public class AddGymScreen extends AppCompatActivity {

    private static final int PICK_PLACE_REQ = 11;
    private static final String TAG = "AddGymScreen";
    private ConstraintLayout addressContainer;
    private LinearLayout pickedAddressContainer;
    private TextView pickedAddress;
    private EditText gymName, gymOwner, gymOwnerContact, gymAddress, gymPincode;
    private Integer pickedAddressPincode;
    private boolean isThisAddressPicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gym);
        Places.initialize(this,getString(R.string.google_places_api_key));
        initialiseUI();
    }

    private void initialiseUI() {
        gymName = findViewById(R.id.activity_add_gym_et_gym_name);
        gymOwner = findViewById(R.id.activity_add_gym_et_owner_name);
        gymOwnerContact = findViewById(R.id.activity_add_gym_et_owner_contact);
        gymAddress = findViewById(R.id.activity_add_gym_tv_gym_address);
        gymPincode = findViewById(R.id.activity_add_gym_tv_pincode);
        addressContainer = findViewById(R.id.activity_add_gym_cl_address_container);
        pickedAddressContainer = findViewById(R.id.activity_add_gym_ll_picked_address_container);
        pickedAddress = findViewById(R.id.activity_add_gym_tv_picked_address);
    }

    /**
     * Clicking this button validates all the UI data,
     * if its present or absent.
     * @param view
     */
    public void registerGym (View view) {
        if (gymName.getText() == null || gymOwner.getText() == null
        || gymOwnerContact.getText() == null || gymName.getText().toString().isEmpty()
        || gymOwner.getText().toString().isEmpty() || gymOwnerContact.getText().toString().isEmpty()) {
            //Cannot register due to incomplete data
            Log.i(TAG, "registerGym: CANNOT REGISTER");
            return;
        }
        /**
         * There are two cases in the address
         * 1. Pick address
         * 2. Enter manual address
         */
        if (isThisAddressPicked) {
            if ((pickedAddress.getText() == null || pickedAddress.getText().toString().isEmpty())) {
                // Cannot register due to absence of picked address
                Log.i(TAG, "registerGym: CANNOT REGISTER");
                return;
            }
            // Can register now
            registerNewGym();
        }
        else {
            if (gymAddress.getText() == null || gymAddress.getText().toString().isEmpty()
            || gymPincode.getText() == null || gymPincode.getText().toString().isEmpty()) {
                // Cannot register due to absence of picked address
                Log.i(TAG, "registerGym: CANNOT REGISTER");
                return;
            }
            // Can register now
            registerNewGym();
        }
    }


    /**
     * This method creates an instance of the new gym
     * and send it to the backend for storing purpose
     */
    private void registerNewGym() {
        Gym gym = new Gym(gymName.getText().toString(),null,null,null,
                gymOwner.getText().toString(),Long.parseLong(gymOwnerContact.getText().toString()));
        if (isThisAddressPicked) {
            gym.setAddress(pickedAddress.getText().toString());
            gym.setPincode(pickedAddressPincode);
        }
        else {
            gym.setAddress(gymAddress.getText().toString());
            gym.setPincode(Integer.parseInt(gymPincode.getText().toString()));
        }
        // *** HTTP POST REQUEST ***
    }

    public void enterAddress (View view) {
        isThisAddressPicked = false;
        addressContainer.setVisibility(View.VISIBLE);
        pickedAddressContainer.setVisibility(View.GONE);
    }

    /**
     * Pick or enter the address from Google Places Interface.
     * @param view
     */
    public void pickUpAddress (View view) {
        isThisAddressPicked = true;
        addressContainer.setVisibility(View.GONE);
        pickedAddressContainer.setVisibility(View.GONE);
        Intent pickPlaceIntent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
                Arrays.asList(Place.Field.ADDRESS, Place.Field.NAME, Place.Field.PLUS_CODE))
                .build(this);
        startActivityForResult(pickPlaceIntent, PICK_PLACE_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Once the place is picked, the result of the place instane
        // is returned back to the calling activity in the form of
        // an intent. Entire data about the place can be found here.
        if (requestCode == PICK_PLACE_REQ && resultCode == RESULT_OK) {
            Place pickedPlace = Autocomplete.getPlaceFromIntent(data);
            pickedAddressContainer.setVisibility(View.VISIBLE);
            pickedAddress.setText(pickedPlace.getAddress());
            String [] addressComps = pickedPlace.getAddress().split(",");
            String pincodeComponent = addressComps[addressComps.length-2];
            pickedAddressPincode = Integer.parseInt(pincodeComponent.substring(pincodeComponent.length()-6));
            Log.i(TAG, "onActivityResult: Name: "+pickedPlace.getName());
            Log.i(TAG, "onActivityResult: Address: "+pickedPlace.getAddress());
            Log.i(TAG, "onActivityResult: Plus Code: "+pickedPlace.getPlusCode());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
