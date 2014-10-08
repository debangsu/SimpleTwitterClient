package com.codepath.apps.basictwitter.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.helpers.UserUtils;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

/**
 * Represents an activity for composing and posting a tweet.
 * We asynchronously a) load user profile info and b) post tweet.
 * On posting tweet, flow passes back to TimelineActivity.
 */
public class ComposeActivity extends ActionBarActivity {
    private Bitmap profileImage = null;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();
        UserUtils.populateProfileHeader(this, UserUtils.getCurrentUserFromSharedPrefs(this),
                profileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        // Item selection.
        switch (mi.getItemId()) {
            case R.id.miTweet:
                onComposeTweet(mi);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }

    /**
     * Handles user's request to post the tweet. Passes control back to TimelineActivity.
     */
    public void onComposeTweet(MenuItem mi) {
        EditText etBody = (EditText) findViewById(R.id.etBody);
        String tweet = etBody.getText().toString();
        etBody.setText("");
        client.postTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                Tweet composedTweet = Tweet.fromJSON(json);
                Intent i = new Intent();
                i.putExtra("composedTweet", composedTweet);
                // Package with result.
                setResult(RESULT_OK, i); // e.g. http 200. intent is set similar to post data.
                // Dismiss the activity.
                finish();
            }

            @Override
            public void onFailure(Throwable e, String s) {
                Log.d("debug", e.toString());
                Log.d("debug", s.toString());
                Toast.makeText(ComposeActivity.this, "Error posting tweet",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
