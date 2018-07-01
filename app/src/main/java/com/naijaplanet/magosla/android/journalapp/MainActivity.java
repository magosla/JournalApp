package com.naijaplanet.magosla.android.journalapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naijaplanet.magosla.android.journalapp.models.User;
import com.naijaplanet.magosla.android.journalapp.utilities.Values;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";


    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeAuthentication();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Values.REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // IdpResponse response = IdpResponse.fromResultIntent(data);
                Toast.makeText(this, "Welcome to your journal", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in was canceled!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseAuth != null) {
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    /**
     * Initializes the components needed to Authenticate the use and try to authenticate the user
     */
    private void initializeAuthentication() {
        //
        User user = Values.getUserFromPreference(this);
        if (user != null) {
            Log.d(TAG, "there is a user preference " + user.getName());
            goToJournalsActivity();
            return;
        }
        if (hasInternetConnection()) {
            tryAuthenticate();
        } else {
            noInternetView();
        }

    }

    /**
     * Let the user know an internet is required
     */
    private void noInternetView() {
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryLogin();
            }
        });
    }

    private void tryAuthenticate() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignedInInitialize(user);
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            Values.REQUEST_CODE_SIGN_IN);
                }
            }
        };
    }


    /**
     * Action to perform when the user is signed-in to the app
     *
     * @param u the FirebaseUser object
     */
    private void onSignedInInitialize(FirebaseUser u) {
        String photoUrl = u.getPhotoUrl() != null ? u.getPhotoUrl().toString() : "";
        User user = new User();
        user.setId(u.getUid());
        user.setEmail(u.getEmail());
        user.setName(u.getDisplayName());
        user.setPhotoUrl(photoUrl);
        Values.saveUserInPreference(this, user);

        // go to the Journals activity
        goToJournalsActivity();

    }

    /**
     * Go to the Avtivity that list the journals
     */
    private void goToJournalsActivity() {
        Intent intent = new Intent(this, JournalsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    /**
     * Checks is the device has an internet connection
     *
     * @return the connection status
     */
    private boolean hasInternetConnection() {

        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectionManager != null ?
                connectionManager.getActiveNetworkInfo() : null;

        return (networkInfo != null && networkInfo.isConnected());
    }

    private void retryLogin() {
        if (hasInternetConnection()) {
            tryAuthenticate();
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        } else {
            Toast.makeText(this, R.string.msg_cannot_connect, Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
