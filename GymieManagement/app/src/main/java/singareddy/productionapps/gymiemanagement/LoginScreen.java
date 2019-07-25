package singareddy.productionapps.gymiemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class LoginScreen extends AppCompatActivity {
    private static final int GOOGLE_SIGNIN_REQ = 4235;
    String TAG = "LoginScreen";

    CallbackManager callbackManager;
    LoginManager loginManager;
    GoogleSignInClient googleClient;
    ImageView facebookLogin, googleLogin, mobileLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        checkUserToken();
        initialiseUI();
    }

    private void checkUserToken() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        if (token != null && !token.isExpired()) {
            launchHomeScreen();
        }
    }

    private void initialiseUI() {
        facebookLogin = findViewById(R.id.activity_login_bt_facebook);
        facebookLogin.setOnClickListener(this::loginByFacebook);
        googleLogin = findViewById(R.id.activity_login_bt_google);
        googleLogin.setOnClickListener(this::loginByGoogle);
    }

    private void loginByFacebook(View view) {
        loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i(TAG, "onSuccess: **");
                Log.i(TAG, "onSuccess: Token: "+loginResult.getAccessToken().getToken());
                Log.i(TAG, "onSuccess: UID: "+loginResult.getAccessToken().getUserId());
                launchHomeScreen();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        loginManager.logIn(this, Arrays.asList("email", "user_likes"));
    }

    private void loginByGoogle (View view) {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder().requestEmail().requestId().build();
        googleClient = GoogleSignIn.getClient(this, signInOptions);
        Intent googleSigninIntent = googleClient.getSignInIntent();
        startActivityForResult(googleSigninIntent, GOOGLE_SIGNIN_REQ);
    }

    private void launchHomeScreen() {
        Intent homeIntent = new Intent(LoginScreen.this, HomeScreen.class);
        startActivity(homeIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGNIN_REQ && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> signedInAccount = GoogleSignIn.getSignedInAccountFromIntent(data);
            signedInAccount.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                @Override
                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                    Log.i(TAG, "onSuccess: Google signin success!");
                    Log.i(TAG, "onSuccess: Name: "+googleSignInAccount.getDisplayName());
                    Log.i(TAG, "onSuccess: UID: "+googleSignInAccount.getId());
                    launchHomeScreen();
                }
            });
            signedInAccount.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i(TAG, "onFailure: Google signin failed: "+e.getLocalizedMessage());
                }
            });
            
        }
            
    }
}
