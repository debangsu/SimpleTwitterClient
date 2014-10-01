package com.codepath.apps.basictwitter.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONObject;

/**
 * Represents an activity for composing and posting a tweet.
 * We asynchronously a) load user profile info and b) post tweet.
 * On posting tweet, flow passes back to TimelineActivity.
 */
public class ComposeActivity extends Activity {
    private Bitmap profileImage = null;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        populateCurrentUser();
        client = TwitterApplication.getRestClient();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
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
     * Asynchronously loads current user profile information from SharedPreferences.
     */
    private void populateCurrentUser() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        Gson gson = new Gson();
        String currentUserJson = prefs.getString(TimelineActivity.CURRENT_USER_KEY, "defaultUser");
        User currentUser = gson.fromJson(currentUserJson, User.class);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(currentUser.getName());
        TextView tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
        tvUserScreenName.setText(currentUser.getScreenName());
        if (profileImage != null) {
            ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
            ivProfileImage.setImageBitmap(profileImage);
        } else {
            new ImageLoaderTask().execute(currentUser.getProfileImageUrl());
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
                Log.d("debug", json.toString());
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
            }
        });
    }

    private class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
        protected void onPreExecute() {
            // TODO(debangsu): add a progress bar for practice.
            // Not needed for small icon images, but useful for a detailed view.
            // progressBar.setVisibility(ProgressBar.VISIBLE);
        }

        protected Bitmap doInBackground(String... urls) {
            return downloadImageFromUrl(urls[0]);
        }

        private Bitmap downloadImageFromUrl(String imageUri) {
            return ImageLoader.getInstance().loadImageSync(imageUri);
        }

        protected void onPostExecute(Bitmap result) {
            // This method is executed in the UIThread
            // with access to the result of the long running task.
            ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
            ivProfileImage.setImageBitmap(result);
        }
    }
}
