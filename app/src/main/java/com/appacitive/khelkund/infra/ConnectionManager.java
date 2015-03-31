package com.appacitive.khelkund.infra;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.appacitive.khelkund.R;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

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
            SnackbarManager.show(
                    Snackbar.with(context) // context
                            .type(SnackbarType.MULTI_LINE)
                            .textColor(Color.RED)
                            .text("No internet connectivity")
                            .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                    , context);
    }
}
