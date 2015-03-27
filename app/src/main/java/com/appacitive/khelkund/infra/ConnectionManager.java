package com.appacitive.khelkund.infra;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nispok.snackbar.Snackbar;

/**
 * Created by sathley on 3/24/2015.
 */
public class ConnectionManager {

    public static void checkNetworkConnectivity(Activity context) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected == false)
            Snackbar.with(context.getApplicationContext()) // context
                    .text("No internet connectivity") // text to display
                    .show(context);
    }
}
