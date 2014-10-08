package com.codepath.apps.basictwitter.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.models.User;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Utility methods for loading user information from shared preferences and network.
 */
public class UserUtils {
    /**
     * Utility method for getting current user from shared preferences.
     *
     * @return
     */
    public static User getCurrentUserFromSharedPrefs(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                context);
        Gson gson = new Gson();
        String currentUserJson = prefs.getString(HomeTimelineFragment.CURRENT_USER_KEY,
                "defaultUser");
        return gson.fromJson(currentUserJson, User.class);
    }

    /**
     * Populates profile header from User model.
     */
    public static void populateProfileHeader(Activity activity, User user, Bitmap profileImage) {
        TextView tvUserName = (TextView) activity.findViewById(R.id.tvUserName);
        tvUserName.setText(user.getName());
        TextView tvUserScreenName = (TextView) activity.findViewById(R.id.tvUserScreenName);
        if (tvUserScreenName != null) {
            tvUserScreenName.setText(user.getScreenName());
        }
        TextView tvUserDescription = (TextView) activity.findViewById(R.id.tvUserDescription);
        if (tvUserDescription != null) {
            tvUserDescription.setText(user.getDescription());
        }
        TextView tvTweetsCount = (TextView) activity.findViewById(R.id.tvTweetsCount);
        if (tvTweetsCount != null) {
            tvTweetsCount.setText(Html.fromHtml("<b>" + String.valueOf(user.getTweetsCount())
                    + "</b> <br> TWEETS"));
        }
        TextView tvFollowers = (TextView) activity.findViewById(R.id.tvFollowers);
        if (tvFollowers != null) {
            tvFollowers.setText(Html.fromHtml("<b>" + String.valueOf(user.getFollowersCount())
                    + "</b> <br> FOLLOWERS"));
        }
        TextView tvFollowing = (TextView) activity.findViewById(R.id.tvFollowing);
        if (tvFollowing != null) {
            tvFollowing.setText(Html.fromHtml("<b>" + String.valueOf(user.getFollowingCount())
                    + "</b> <br> FOLLOWING"));
        }

        if (profileImage == null) {
            new UserUtils.ImageLoaderTask(activity, profileImage).execute(
                    user.getProfileImageUrl());
        }
        ImageView ivProfileImage = (ImageView) activity.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageBitmap(profileImage);
    }

    public static class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
        private Activity activity = null;
        private Bitmap imageBitmap = null;

        public ImageLoaderTask(Activity activity, Bitmap imageBitmap) {
            this.activity = activity;
            this.imageBitmap = imageBitmap;
        }

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
            imageBitmap = result;
            ImageView ivProfileImage = (ImageView) activity.findViewById(R.id.ivProfileImage);
            ivProfileImage.setImageBitmap(result);
        }
    }
}
