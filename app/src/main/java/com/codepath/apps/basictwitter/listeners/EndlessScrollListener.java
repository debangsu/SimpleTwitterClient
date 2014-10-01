package com.codepath.apps.basictwitter.listeners;

import android.widget.AbsListView;

import com.codepath.apps.basictwitter.adapters.TweetAdapter;

/**
 * Represents a listener object used to implement "endless" scrolling of image search results.
 * Abstract class. Must instantiate and implement onLoadMore.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
    public static final int TWEET_COUNT_PER_GET = 30;
    // Minimum amount of items below current scroll position before loading more.
    private int visibleThreshold = 5;
    // Id of the oldest loaded tweet.
    private Long idOldestTweet = null;
    // Total items in dataset after the last load.
    private int previousTotalItemCount = 0;
    // True if waiting for the last data set to load.
    private boolean loading = false;

    protected EndlessScrollListener() {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
        TweetAdapter adapter = (TweetAdapter) view.getAdapter();
        // If totalItemCount == 0 and previousTotalItemCount is not, assume list invalidated.
        // Reset to initial state.
        if (totalItemCount < previousTotalItemCount) {
            this.loading = false;
            this.idOldestTweet = adapter.getItem(adapter.getCount() - 1).getUid();
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // If still loading, check for dataset size change. If so, assume loading completed.
        // Update currentPageNumber, totalItemCount;
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            this.idOldestTweet = adapter.getItem(adapter.getCount() - 1).getUid();
        }

        // If not currently loading, check if visibleThreshold breached. If so, load more data
        // using onLoadMore.
        if (!loading
                && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            if (idOldestTweet == null) {
                onLoadMore(null, totalItemCount); // Load the standard count of items.
            } else {
                onLoadMore(idOldestTweet - 1, totalItemCount);
            }
            loading = true;
        }
    }

    // Define how to load more data based on page.
    protected abstract void onLoadMore(Long maxId, int totalItemCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take action on changed.
    }
}
