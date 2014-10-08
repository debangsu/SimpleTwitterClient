package com.codepath.apps.basictwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.helpers.UserUtils;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;
import com.codepath.apps.basictwitter.models.User;

/**
 * Container for displaying a user's own profile and timeline.
 */
public class UserTimelineFragment extends TweetsListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        final User user = UserUtils.getCurrentUserFromSharedPrefs(getActivity());
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            protected void onLoadMore(@Nullable Long maxId, int totalItemCount) {
                // Triggered only when new data needs to be appended to adapterView.
                if (firstTime) {
                    doLoad(TwitterClient.USER_TIMELINE_ENDPOINT, maxId, user.getScreenName(),
                            true); // Clear adapter.
                    firstTime = false;
                } else {
                    doLoad(TwitterClient.USER_TIMELINE_ENDPOINT, maxId, user.getScreenName(),
                            false);
                    // Do not clear adapter. Just append.
                }
            }
        });
        return v;
    }
}
