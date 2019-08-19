package singareddy.productionapps.gymiemanagement.requests;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import singareddy.productionapps.gymiemanagement.R;


/**
 * This activity shows the pending approval
 * requests raised by other roles as follows
 * For SUPER ADMIN - Admin requests alone
 * For ADMIN - Trainer, Receptionist, and other gym staff only
 */
public class RequestsScreen extends AppCompatActivity {

    RecyclerView requestsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        initialiseUI();
    }

    private void initialiseUI() {
        requestsList = findViewById(R.id.activity_screen_rv_requests);
        RequestsAdapter adapter = new RequestsAdapter(this, R.layout.list_item_request);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        requestsList.setAdapter(adapter);
        requestsList.setLayoutManager(manager);
    }

}
