package com.codepath.apps.basictwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.basictwitter.clients.TwitterClient;
import com.codepath.apps.basictwitter.listeners.EndlessScrollListener;

/**
 * Created by debangsu.
 */
public class MentionsTimelineFragment extends TweetsListFragment {

//    private TwitterClient client;
    // May need to move it up to parent.
//    public static final String CURRENT_USER_KEY = "currentUser";
//    static final int COMPOSED_TWEET_RESULT = 50;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        client = TwitterApplication.getRestClient();

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
                    doLoad(TwitterClient.MENTIONS_TIMELINE_ENDPOINT, maxId, null, true); // Clear adapter.
                    firstTime = false;
                } else {
                    doLoad(TwitterClient.MENTIONS_TIMELINE_ENDPOINT, maxId, null, false);
                    // Do not clear adapter. Just append.
                }
            }
        });
        return v;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.timeline, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.timeline, menu);
//        return true;
//    }

    /**
     * Loads the next set of results from Twitter REST API home timeline endpoint.
     * @param maxId User requests tweet IDs <= maxId i.e. older than it.
     * @param clearAdapter If true, clear the adapter before appending.
     */
//    @Override
//    protected void doLoad(@Nullable Long maxId, final boolean clearAdapter) {
//        // TODO(debangsu): fetch from mentions timeline.
//        client.getMentionsTimeline(EndlessScrollListener.TWEET_COUNT_PER_GET, maxId,
//                new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(JSONArray json) {
//                        if (clearAdapter) {
//                            aTweets.clear();
//                        }
//                        addAll(fromJSONArray(json));
//                    }
//
//                    @Override
//                    public void onFailure(Throwable e, String s) {
//                        Log.d("ERROR:  ", e.toString());
//                        Log.d("ERROR: ", s.toString());
//                        // TODO(debangsu): can we avoid getActivity()?
//                        Toast.makeText(getActivity(), "Fetch failure.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
