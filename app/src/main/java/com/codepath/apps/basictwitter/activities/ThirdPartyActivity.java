package com.codepath.apps.basictwitter.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.fragments.ThirdPartyTimelineFragment;
import com.codepath.apps.basictwitter.helpers.UserUtils;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.codepath.apps.basictwitter.models.Tweet.fromJSONArray;

/**
 * Represents and activity for viewing another user's profile and timeline.
 */
public class ThirdPartyActivity extends ActionBarActivity {
    private Bitmap profileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party);
        Intent i = getIntent();
        String screenName = i.getStringExtra("screenName");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ThirdPartyTimelineFragment thirdPartyFragment = ThirdPartyTimelineFragment.newInstance(
                screenName);
        ft.replace(R.id.thirdPartyFragmentHolder, thirdPartyFragment);
        ft.commit();
        getUserProfile(screenName);
    }

    private void getUserProfile(String screenName) {
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pbLoading);
        if (pb != null) {
            pb.setVisibility(ProgressBar.VISIBLE);
        }
        TwitterApplication.getRestClient()
                .getTimeline(TwitterClient.USER_TIMELINE_ENDPOINT,
                        EndlessScrollListener.TWEET_COUNT_PER_GET, null, screenName,
                        new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(JSONArray json) {
                                try {
                                    ArrayList<Tweet> tweets = fromJSONArray(json);
                                    User user = tweets.get(0).getUser();
                                    getActionBar().setTitle("@" + user.getScreenName());
                                    UserUtils.populateProfileHeader(ThirdPartyActivity.this,
                                            user, profileImage);
                                } catch (Exception e) {
                                    Log.d("ERROR:  ", e.toString());
                                    Toast.makeText(ThirdPartyActivity.this, "Fetch failure.",
                                            Toast.LENGTH_SHORT).show();
                                } finally {
                                    if (pb != null) {
                                        pb.setVisibility(ProgressBar.INVISIBLE);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Throwable e, String s) {
                                try {
                                    Log.d("ERROR:  ", e.toString());
                                    Log.d("ERROR: ", s.toString());
                                    Toast.makeText(ThirdPartyActivity.this, "Fetch failure.",
                                            Toast.LENGTH_SHORT).show();
                                } finally {
                                    if (pb != null) {
                                        pb.setVisibility(ProgressBar.INVISIBLE);
                                    }
                                }
                            }
                        });
    }
}
