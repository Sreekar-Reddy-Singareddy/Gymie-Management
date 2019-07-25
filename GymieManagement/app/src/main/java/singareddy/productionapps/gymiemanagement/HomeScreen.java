package singareddy.productionapps.gymiemanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = "HomeScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            finish();
        }
        // Logout Google
        else if (googleAccount != null && !googleAccount.isExpired()) {
            GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            finish();
        }
        // Logout mobile
        else if (mobileAccount != null) {
            AccountKit.logOut();
            finish();
        }
    }
}
