package com.codepath.apps.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A model for representing a tweet. Implements Parcelable for performance.
 */
public class Tweet implements Parcelable {
    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        @Override
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        @Override
        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };

    private String body;
    private long uid;
    private String createdAt;
    private User user;

    public Tweet() {
    }

    public Tweet(Parcel source) {
        readFromParcel(source);
    }

    /**
     * Factory method for converting an individual JSON object to a tweet.
     */
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        // Extract values from json to populate member variables.
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    /**
     * Factory method to convert a JSONArray of tweets to an ArrayList of Tweet objects.
     */
    public static ArrayList<Tweet> fromJSONArray(JSONArray json) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(json.length());

        for (int i = 0; i < json.length(); ++i) {
            JSONObject tweetJson = null;

            try {
                tweetJson = json.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
            Tweet tweet = fromJSON(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (body != null) {
            builder.append(body);
        }
        if (user != null) {
            if (user.getScreenName() != null) {

                builder.append(" - ").append(user.getScreenName());
            }
        }
        return builder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(body);
        dest.writeLong(uid);
        dest.writeString(createdAt);
        dest.writeParcelable(user, i);
    }

    private void readFromParcel(Parcel source) {
        this.body = source.readString();
        this.uid = source.readLong();
        this.createdAt = source.readString();
        this.user = source.readParcelable(User.class.getClassLoader());
    }
}
