package singareddy.productionapps.gymiemanagement.login;

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

import singareddy.productionapps.gymiemanagement.HomeScreen;
import singareddy.productionapps.gymiemanagement.R;

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
            launchNextScreen(facebookAccount.getUserId());
        }
        else if (googleAccount != null) {
            launchNextScreen(googleAccount.getId());
        }
        else if (accountKitMobileOrEmail != null) {
            launchNextScreen(accountKitMobileOrEmail.getAccountId());
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
                launchNextScreen(loginResult.getAccessToken().getUserId());
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

    private void launchNextScreen(String userId) {
        Log.i(TAG, "launchNextScreen: Logged In User ID: "+userId);

        /**
         * DATA: This data originally comes from app server through
         * HTTP response in the form of JSON string. For now it is
         * DUMMY DATA.
         */
        boolean isItSuperAdmin = false;

        // If the user is the super admin, take them directly to
        // the home screen instead of GymSelectScreen.
        if (isItSuperAdmin) {
            Intent homeScreenIntent = new Intent(this, HomeScreen.class);
            startActivity(homeScreenIntent);
            finish();
            return;
        }

        // If the user is not the super admin, then take them to
        // GymSelectScreen so that they can register under a gym.
        Intent gymSelectScreenIntent = new Intent(LoginScreen.this, GymSelectScreen.class);
        gymSelectScreenIntent.putExtra(GymSelectScreen.INTENT_EXTRA_LOGGED_USER_ID, userId);
        startActivity(gymSelectScreenIntent);
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
                    launchNextScreen(googleSignInAccount.getId());
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
                launchNextScreen(loginResult.getAccessToken().getAccountId());
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
                launchNextScreen(emailToken.getAccountId());
            }
            Log.i(TAG, "onActivityResult: Email Account ID: "+emailToken.getAccountId());
        }
    }
}
