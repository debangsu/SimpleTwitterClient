package com.codepath.apps.basictwitter.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Utilities for interacting with the network.
 */
public class NetworkUtils {

    /**
     * Returns true if network is available. Performs user facing error reporting and logging.
     */
    public static Boolean isNetworkAvailable(Context context) {
        Boolean result = isNetworkAvailableCheck(context);
        if (!result) {
            Toast.makeText(context, "Network unavailable!", Toast.LENGTH_LONG).show();
            Log.e("ERROR", "Network unavailable when trying to conenct.");
        }
        return result;
    }

    /**
     * Actually performs the network availability check. Returns true if network is available.
     */
    static Boolean isNetworkAvailableCheck(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
