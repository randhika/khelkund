package com.appacitive.khelkund.infra;

import android.app.Activity;

import com.nispok.snackbar.Snackbar;

/**
 * Created by sathley on 3/30/2015.
 */
public class SnackBarManager {

    public static void showMessage(String message, Activity activity)
    {
        Snackbar.with(KhelkundApplication.getAppContext()) // context
                .text("No internet connectivity") // text to display
                .show(activity);
    }
}
