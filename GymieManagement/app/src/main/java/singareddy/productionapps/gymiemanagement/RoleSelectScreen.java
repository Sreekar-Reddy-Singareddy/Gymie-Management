package singareddy.productionapps.gymiemanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class RoleSelectScreen extends AppCompatActivity {
    // Constants
    private static final String TAG = "RoleSelectScreen";
    public static final String INTENT_EXTRA_SELECTED_GYM_NAME = "selectedGymName";
    public static final String INTENT_EXTRA_SELECTED_GYM_ADDRESS = "selectedGymAddress";

    private TextView gymName, gymAddress;
    private CardView ownerCard, recepCard, trainCard, cleanCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_select);

        initialiseUI();
    }

    private void initialiseUI() {
        gymAddress = findViewById(R.id.activity_role_select_tv_address);
        gymName = findViewById(R.id.activity_role_select_tv_name);
        ownerCard = findViewById(R.id.activity_role_select_cv_owner);
        recepCard = findViewById(R.id.activity_role_select_cv_reception);
        trainCard = findViewById(R.id.activity_role_select_cv_trainer);
        cleanCard = findViewById(R.id.activity_role_select_cv_cleaner);
        ownerCard.setOnClickListener(this::roleSelected);
        recepCard.setOnClickListener(this::roleSelected);
        trainCard.setOnClickListener(this::roleSelected);
        cleanCard.setOnClickListener(this::roleSelected);
        ownerCard.setOnTouchListener(this::cardTouched);
        recepCard.setOnTouchListener(this::cardTouched);
        trainCard.setOnTouchListener(this::cardTouched);
        cleanCard.setOnTouchListener(this::cardTouched);

        // Get the selected gym data
        String name = getIntent().getExtras().getString(INTENT_EXTRA_SELECTED_GYM_NAME);
        String address = getIntent().getExtras().getString(INTENT_EXTRA_SELECTED_GYM_ADDRESS);
        gymName.setText(name);
        gymAddress.setText(address);
    }

    private boolean cardTouched(View view, MotionEvent event) {
        CardView touchedCard = (CardView) view;
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            touchedCard.setCardBackgroundColor(getColor(R.color.selectedCardBackground));
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            touchedCard.setCardBackgroundColor(getColor(android.R.color.white));
        }
        return false;
    }

    private void roleSelected(View view) {
        Intent homeScreenIntent = new Intent(this, HomeScreen.class);
        startActivity(homeScreenIntent);
    }
}
