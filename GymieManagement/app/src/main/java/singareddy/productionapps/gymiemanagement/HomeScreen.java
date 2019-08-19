package singareddy.productionapps.gymiemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import singareddy.productionapps.gymiemanagement.attendance.ManageAttendanceScreen;
import singareddy.productionapps.gymiemanagement.attendance.ScanAttendanceScreen;
import singareddy.productionapps.gymiemanagement.gyms.ManageGymsScreen;
import singareddy.productionapps.gymiemanagement.login.LoginScreen;
import singareddy.productionapps.gymiemanagement.requests.RequestsScreen;

/**
 * Once the person has logged in, we must check it is.
 * We query the STAFF table and know the role of the logged user.
 * If the user is an admin, then how many gyms is he an admin for?
 * If he is admin of only 1 gym, by default show something.
 * If he is admin of multiple gyms, then let him choose which gym
 * he wants to manage.
 * In the navigation view header, the user can choose and change
 * the gym they want to manage. Clicking the header view displays a
 * dialog box where they can choose a gym.
 */
public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "HomeScreen";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initialiseUI();
    }

    private void initialiseUI() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_drawer);
        drawerLayout = findViewById(R.id.activity_home_dl_drawer_layout);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                Log.i(TAG, "onDrawerSlide: Slider: "+slideOffset);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.go_back);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_drawer);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        navigationView = findViewById(R.id.activity_home_nv_drawer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "onOptionsItemSelected: ***");;
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.closeDrawer(Gravity.START);
            else drawerLayout.openDrawer(Gravity.START);
        }
        return super.onOptionsItemSelected(item);
    }

    public void navMenuItemClicked (MenuItem menuItem) {
        Log.i(TAG, "navMenuItemClicked: **");
        switch (menuItem.getItemId()) {
            case R.id.home_nav_menu_item_gyms:
                Intent manageGymsIntent = new Intent(this, ManageGymsScreen.class);
                startActivity(manageGymsIntent);
                break;
            case R.id.home_nav_menu_item_attendance:
                Intent manageAttendanceIntent = new Intent(this, ManageAttendanceScreen.class);
                startActivity(manageAttendanceIntent);
                break;
            case R.id.home_nav_menu_item_requests:
                Intent manageRequestsIntent = new Intent(this, RequestsScreen.class);
                startActivity(manageRequestsIntent);
                break;
        }
    }

    public void logout(View v){
        AccessToken facebookAccount = AccessToken.getCurrentAccessToken(); // Facebook account
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this); // Google account
        com.facebook.accountkit.AccessToken mobileAccount = AccountKit.getCurrentAccessToken(); // Mobile account

        // Log statements for testing purpose
        Log.i(TAG, "logout: F Account: "+facebookAccount);
        Log.i(TAG, "logout: G Account: "+googleAccount);
        Log.i(TAG, "logout: M Account: "+mobileAccount);

        // Logout Facebook
        if (facebookAccount != null && !facebookAccount.isExpired()) {
            LoginManager.getInstance().logOut();
            launchLoginScreen();
        }
        // Logout Google
        else if (googleAccount != null && !googleAccount.isExpired()) {
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            launchLoginScreen();
        }
        // Logout mobile
        else if (mobileAccount != null) {
            AccountKit.logOut();
            launchLoginScreen();
        }
    }

    /**
     * When user chooses to logout, they are taken
     * back to the login screen, where they must login again.
     */
    private void launchLoginScreen() {
        Intent loginScreenIntent = new Intent(this, LoginScreen.class);
        startActivity(loginScreenIntent);
        finish();
    }
}
