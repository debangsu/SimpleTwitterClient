package com.codepath.apps.basictwitter.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * Container for displaying the home timeline.
 */
public class HomeTimelineFragment extends TweetsListFragment {
    public static final String CURRENT_USER_KEY = "currentUser";
    private boolean shouldUpdateUserCredentials = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (shouldUpdateUserCredentials) {
            upsertCurrentUserCredentials();
            shouldUpdateUserCredentials = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            protected void onLoadMore(@Nullable Long maxId, int totalItemCount) {
                // Triggered only when new data needs to be appended to adapterView.
                if (firstTime) {
                    doLoad(TwitterClient.HOME_TIMELINE_ENDPOINT, maxId, null, true); // Clear adapter.
                    firstTime = false;
                } else {
                    doLoad(TwitterClient.HOME_TIMELINE_ENDPOINT, maxId, null, false);
                    // Do not clear adapter. Just append.
                }
            }
        });
        return v;
    }

    /**
     * Upserts the current logged in user's credentials. Saves to SharedPreferences.
     */
    private void upsertCurrentUserCredentials() {
        // JSON request in AsyncTask for verifyCreds.
        // store username, screen_name, profileurl in sharedpref
        // store user profile bitmap in file.
        client.getLoggedInUserCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                User currentUser = User.fromJSON(json);
                // TODO(debangsu): can we avoid using getActivity()?
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity()
                        .getApplicationContext());
                SharedPreferences.Editor prefsEditor = prefs.edit();
                Gson gson = new Gson();
                String loggedinUserJson = gson.toJson(currentUser);
                prefsEditor.putString(CURRENT_USER_KEY, loggedinUserJson);
                prefsEditor.commit();
            }

            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });
    }
}
