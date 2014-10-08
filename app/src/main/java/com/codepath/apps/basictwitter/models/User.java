package com.codepath.apps.basictwitter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A model for representing a user. Implements Parcelable for performance.
 */
public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String description;
    private Long followersCount;
    private Long followingCount;

    public User() {
    }

    public User(Parcel source) {
        readFromParcel(source);
    }

    /**
     * Factory method for converting an individual JSON object to a user.
     */
    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();
        // Extract values from json to populate member variables.
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.description = jsonObject.getString("description");
            user.followersCount = jsonObject.getLong("followers_count");
            user.followingCount = jsonObject.getLong("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Long followerCount) {
        this.followersCount = followerCount;
    }

    public Long getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(Long followingCount) {
        this.followingCount = followingCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(name);
        dest.writeLong(uid);
        dest.writeString(screenName);
        dest.writeString(profileImageUrl);
        dest.writeString(description);
        dest.writeLong(followersCount);
        dest.writeLong(followingCount);
    }

    private void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.uid = source.readLong();
        this.screenName = source.readString();
        this.profileImageUrl = source.readString();
        this.description = source.readString();
        this.followersCount = source.readLong();
        this.followingCount = source.readLong();
    }
}
