package com.codepath.apps.basictwitter.clients;

import android.content.Context;
import android.support.annotation.Nullable;

import com.codepath.apps.basictwitter.helpers.NetworkUtils;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/**
 * TwitterClient is responsible for communicating with the Twitter REST API.
 * Full list of supported API classes:
 * https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api *
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "kQFTyRfpdip8IQsCHZ0syy5Bb";
    public static final String REST_CONSUMER_SECRET =
            "t4lC4piULmLxOKJZ6r4p8u2mhUqaHbb7XKyvIZ50FE4uecbCGK";
    public static final String REST_CALLBACK_URL =
            "oauth://cpbasictweets"; // (here and in manifest)
    public static final String HOME_TIMELINE_ENDPOINT = "statuses/home_timeline.json";
    public static final String VERIFY_CREDENTIALS_ENDPOINT = "account/verify_credentials.json";
    private static final String POST_TWEET_ENDPOINT = "statuses/update.json";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET,
                REST_CALLBACK_URL);
    }

    /**
     * Performs GET request to retrieve user's home timeline.
     */
    public void getHomeTimeline(Integer count, @Nullable Long maxId, AsyncHttpResponseHandler handler) {
        if (!NetworkUtils.isNetworkAvailable(this.context)) {
            return;
        }
        String apiUrl = getApiUrl(HOME_TIMELINE_ENDPOINT);
        RequestParams params = new RequestParams();
        params.put("count", String.valueOf(count));
        if (maxId != null) {
            params.put("max_id", String.valueOf(maxId));
        }
        client.get(apiUrl, params, handler);
    }

    /**
     * Performs GET request to obtain current logged in user's credentials.
     */
    public void getLoggedInUserCredentials(AsyncHttpResponseHandler handler) {
        if (!NetworkUtils.isNetworkAvailable(this.context)) {
            return;
        }
        String apiUrl = getApiUrl(VERIFY_CREDENTIALS_ENDPOINT);
        RequestParams params = new RequestParams();
        client.get(apiUrl, null, handler);
    }

    /**
     * Performs POST request to post a tweet.
     */
    public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
        if (!NetworkUtils.isNetworkAvailable(this.context)) {
            return;
        }
        String apiUrl = getApiUrl(POST_TWEET_ENDPOINT);
        RequestParams params = new RequestParams();
        params.put("status", tweet);
        client.post(apiUrl, params, handler);
    }
}