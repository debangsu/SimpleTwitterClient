package com.codepath.apps.basictwitter.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.activities.ComposeActivity;
import com.codepath.apps.basictwitter.activities.ProfileActivity;
import com.codepath.apps.basictwitter.adapters.TweetAdapter;
import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import static com.codepath.apps.basictwitter.models.Tweet.fromJSONArray;

/**
 * Abstract class containing common functionality for displaying various timelines (list of tweets).
 */
public abstract class TweetsListFragment extends Fragment {
    public static final int COMPOSED_TWEET_RESULT = 50;
    public static final int PROFILE_RESULT = 51;

    protected ArrayList<Tweet> tweets;
    protected TweetAdapter aTweets;
    protected ListView lvTweets;
    protected ProgressBar pbLoading;
    protected TwitterClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Non-view initialization.
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetAdapter(getActivity(), tweets);
        client = TwitterApplication.getRestClient();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view.
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        // Assign view references.
        // TODO(debangsu): Use ViewHolder pattern.
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.timeline, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        // handle item selection
        switch (mi.getItemId()) {
            case R.id.miCompose:
                onCompose(mi);
                return true;
            case R.id.miProfile:
                onProfileView(mi);
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }

    /**
     * Loads the next set of results from Twitter REST API timeline endpoint.
     *
     * @param maxId        User requests tweet IDs <= maxId i.e. older than it.
     * @param clearAdapter If true, clear the adapter before appending.
     */
    protected void doLoad(String timelineEndpoint, @Nullable Long maxId,
                          @Nullable String screenName, final boolean clearAdapter) {
        pbLoading.setVisibility(ProgressBar.VISIBLE);
        client.getTimeline(timelineEndpoint, EndlessScrollListener.TWEET_COUNT_PER_GET, maxId,
                screenName, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray json) {
                        try {
                            if (clearAdapter) {
                                aTweets.clear();
                            }
                            addAll(fromJSONArray(json));
                        } finally {
                            pbLoading.setVisibility(ProgressBar.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String s) {
                        try {
                            Log.d("ERROR:  ", e.toString());
                            Log.d("ERROR: ", s.toString());
                            Toast.makeText(getActivity(), "Fetch failure.",
                                    Toast.LENGTH_SHORT).show();
                        } finally {
                            pbLoading.setVisibility(ProgressBar.INVISIBLE);
                        }
                    }
                });
    }

    /**
     * Launches activity via intent for viewing a user's own profile.
     */
    public void onProfileView(MenuItem mi) {
        Intent i = new Intent(getActivity(), ProfileActivity.class);
        startActivityForResult(i, TweetsListFragment.PROFILE_RESULT);
    }

    /**
     * Handles user's compose action. Launches a new ComposeActivity with Intent.
     * Intent used to get posted tweet to insert into home timeline.
     */
    public void onCompose(MenuItem mi) {
        Intent i = new Intent(getActivity(), ComposeActivity.class);
        startActivityForResult(i, TweetsListFragment.COMPOSED_TWEET_RESULT);
    }

    /**
     * Receives activity result from child activity, and inserts in timeline.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tweet composedTweet = null;
        if (requestCode == COMPOSED_TWEET_RESULT) {
            // TODO(debangsu): Should we use a different constant here?
            if (resultCode == Activity.RESULT_OK) {
                composedTweet = data.getParcelableExtra("composedTweet");
            }
        }
        if (composedTweet != null) {
            // Inserts at the top of timeline.
            aTweets.insert(composedTweet, 0);
        }
    }

    // Delegate adding to internal adapter.
    public void addAll(ArrayList<Tweet> tweets) {
        aTweets.addAll(tweets);
    }
}
