package singareddy.productionapps.gymiemanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
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
    private static final int GOOGLE_SIGNIN_REQ = 11;
    private static final int PHONE_LOGIN_REQ = 12;
    private static final int EMAIL_LOGIN_REQ = 13;
    String TAG = "LoginScreen";

    CallbackManager callbackManager;
    LoginManager loginManager;
    GoogleSignInClient googleClient;
    ImageView facebookLogin, googleLogin, mobileLogin, emailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        checkUserToken();
        initialiseUI();
    }

    private void checkUserToken() {
        AccessToken facebookAccount = AccessToken.getCurrentAccessToken(); // Facebook account
        GoogleSignInAccount googleAccount = GoogleSignIn.getLastSignedInAccount(this); // Google account
        com.facebook.accountkit.AccessToken accountKitMobileOrEmail = AccountKit.getCurrentAccessToken(); // Mobile account

        // Log statements for testing purpose
        Log.i(TAG, "checkUserToken: F Account: "+facebookAccount);
        Log.i(TAG, "checkUserToken: G Account: "+googleAccount);
        Log.i(TAG, "checkUserToken: M Account: "+accountKitMobileOrEmail);

        if (facebookAccount != null && !facebookAccount.isExpired()) {
            launchHomeScreen();
        }
        else if (googleAccount != null) {
            launchHomeScreen();
        }
        else if (accountKitMobileOrEmail != null) {
            launchHomeScreen();
        }
    }

    private void initialiseUI() {
        facebookLogin = findViewById(R.id.activity_login_bt_facebook);
        facebookLogin.setOnClickListener(this::loginByFacebook);
        googleLogin = findViewById(R.id.activity_login_bt_google);
        googleLogin.setOnClickListener(this::loginByGoogle);
        mobileLogin = findViewById(R.id.activity_login_bt_mobile);
        mobileLogin.setOnClickListener(this::loginByMobile);
        emailLogin = findViewById(R.id.activity_login_bt_email);
        emailLogin.setOnClickListener(this::loginByEmail);
    }

    private void loginByGoogle (View view) {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder().requestEmail().requestId().build();
        googleClient = GoogleSignIn.getClient(this, signInOptions);
        Intent googleSigninIntent = googleClient.getSignInIntent();
        startActivityForResult(googleSigninIntent, GOOGLE_SIGNIN_REQ);
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

    private void loginByMobile (View view) {
        UIManager uiManager = new SkinManager(SkinManager.Skin.CONTEMPORARY,getColor(R.color.themeColor));
        AccountKitConfiguration.AccountKitConfigurationBuilder loginBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE, AccountKitActivity.ResponseType.TOKEN);
        loginBuilder.setUIManager(uiManager);
        Intent phoneLoginIntent = new Intent(this, AccountKitActivity.class);
        phoneLoginIntent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                loginBuilder.build());
        startActivityForResult(phoneLoginIntent, PHONE_LOGIN_REQ);
    }

    private void loginByEmail (View view) {
        UIManager uiManager = new SkinManager(SkinManager.Skin.CONTEMPORARY, getColor(R.color.themeColor));
        AccountKitConfiguration.AccountKitConfigurationBuilder loginBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.EMAIL, AccountKitActivity.ResponseType.TOKEN);
        loginBuilder.setUIManager(uiManager);
        Intent intent = new Intent(this, AccountKitActivity.class);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                loginBuilder.build());
        startActivityForResult(intent, EMAIL_LOGIN_REQ);
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
        else if (requestCode == PHONE_LOGIN_REQ && resultCode == RESULT_OK) {
            // Activity result by the Account Kit for Phone login
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getAccessToken() != null) {
                launchHomeScreen();
            }
            Log.i(TAG, "onActivityResult: User Token: "+loginResult.getAccessToken().getToken());
            Log.i(TAG, "onActivityResult: Account ID"+loginResult.getAccessToken().getAccountId());
        }
        else if (requestCode == EMAIL_LOGIN_REQ && resultCode == RESULT_OK) {
            AccountKitLoginResult accountKitResult =
                    data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            com.facebook.accountkit.AccessToken emailToken =
                    accountKitResult.getAccessToken();
            if (emailToken != null) {
                launchHomeScreen();
            }
            Log.i(TAG, "onActivityResult: Email Account ID: "+emailToken.getAccountId());
        }
    }
}
