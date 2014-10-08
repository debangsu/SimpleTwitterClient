package com.codepath.apps.basictwitter.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.helpers.UserUtils;
import com.codepath.apps.basictwitter.models.User;

/**
 * Represents an activity for displaying user profile stats and timeline.
 */
public class ProfileActivity extends ActionBarActivity {
    private Bitmap profileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        User currentUser = UserUtils.getCurrentUserFromSharedPrefs(this);
        // Set User screen name on Action Bar
        getSupportActionBar().setTitle("@" + currentUser.getScreenName());
        UserUtils.populateProfileHeader(this, currentUser, profileImage);
    }
}
