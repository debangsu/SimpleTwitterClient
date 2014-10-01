package com.codepath.apps.basictwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.helpers.NetworkUtils;
import com.codepath.oauth.OAuthLoginActivity;

/**
 * Represents activity for performing an OAuthLogin to Twitter.
 */
public class LoginActivity extends OAuthLoginActivity<TwitterClient> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Inflate the menu; this adds items to the action bar if it is present.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    /**
     * OAuth authenticated successfully, launch primary authenticated activity i.e Display
     * application "homepage"
     */
    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(LoginActivity.this, TimelineActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    /**
     * OAuth authentication flow failed, handle the error i.e Display an error dialog or toast
     */
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
        Log.e("ERROR", "Login failure!");
        Toast.makeText(this, "Login failure!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Click handler method for the button used to start OAuth flow. Uses the client to initiate
     * OAuth authorization. This should be tied to a button used to login
     */
    public void loginToRest(View view) {
        if (NetworkUtils.isNetworkAvailable(this)) {
            getClient().connect();
        } else {
            Toast.makeText(this, "Network unavailable!", Toast.LENGTH_LONG).show();
            Log.e("ERROR", "Network unavailable when trying to conenct.");
        }
    }
}
