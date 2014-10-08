package com.codepath.apps.basictwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;

/**
 * Container for displaying third party (another user's) profile and timeline.
 */
public class ThirdPartyTimelineFragment extends TweetsListFragment {
    private String screenName = null;

    /**
     * Singleton used for inter fragment communication.
     */
    public static ThirdPartyTimelineFragment newInstance(String screenName) {
        ThirdPartyTimelineFragment fragmentInstance = new ThirdPartyTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        fragmentInstance.setArguments(args);
        return fragmentInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenName = getArguments().getString("screenName", "");
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
                    doLoad(TwitterClient.USER_TIMELINE_ENDPOINT, maxId, screenName, true); // Clear adapter.
                    firstTime = false;
                } else {
                    doLoad(TwitterClient.USER_TIMELINE_ENDPOINT, maxId, screenName, false);
                    // Do not clear adapter. Just append.
                }
            }
        });
        return v;
    }
}
