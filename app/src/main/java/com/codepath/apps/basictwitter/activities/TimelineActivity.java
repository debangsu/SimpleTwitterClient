package com.codepath.apps.basictwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.adapters.TweetAdapter;
import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codepath.apps.basictwitter.models.Tweet.fromJSONArray;

/**
 * Represents activity for interacting with a user's home timeline.
 * We can populate the timeline with latest tweets, update current logged in
 * user's credentials, launch a ComposeActivity to compose a tweet and insert it
 * to the timeline.
 */
public class TimelineActivity extends Activity {
    static final String CURRENT_USER_KEY = "currentUser";
    static final int COMPOSED_TWEET_RESULT = 50;
    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetAdapter aTweets;
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        populateTimeline();
        // TODO(debangsu): Use ViewHolder pattern.
        lvTweets = (ListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        upsertCurrentUserCredentials();
    }

    /**
     * Populates timeline from Twitter REST API.
     */
    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray json) {
                Log.d("debug", json.toString());
                aTweets.addAll(fromJSONArray(json));
            }

            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Log.d("debug", json.toString());
                User currentUser = User.fromJSON(json);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext());
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

    /**
     * Handles user's compose action. Launches a new ComposeActivity with Intent.
     * Intent used to get posted tweet to insert into home timeline.
     */
    public void onCompose(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        Tweet tweet = new Tweet();
        i.putExtra("tweet", new Tweet());
        startActivityForResult(i, COMPOSED_TWEET_RESULT);
    }

    /**
     * Receives activity result from child activity, and inserts in timeline.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tweet composedTweet = null;
        if (requestCode == COMPOSED_TWEET_RESULT) {
            if (resultCode == RESULT_OK) {
                composedTweet = data.getParcelableExtra("composedTweet");
            }
        }
        if (composedTweet != null) {
            // Inserts at the top of timeline.
            aTweets.insert(composedTweet, 0);
        }
    }
}
