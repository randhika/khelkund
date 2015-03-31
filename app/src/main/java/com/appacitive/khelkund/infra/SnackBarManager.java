package com.appacitive.khelkund.infra;

import android.app.Activity;
import android.graphics.Color;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

/**
 * Created by sathley on 3/30/2015.
 */
public class SnackBarManager {

    public static void showError(String message, Activity activity)
    {
        SnackbarManager.show(
                Snackbar.with(activity) // context
                        .type(SnackbarType.MULTI_LINE)
                        .textColor(Color.WHITE)
                        .color(Color.RED)
                        .text(message)
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                , activity);

    }

    public static void showSuccess(String message, Activity activity)
    {
        SnackbarManager.show(
                Snackbar.with(activity) // context
                        .type(SnackbarType.MULTI_LINE)
                        .textColor(Color.GREEN)
                        .text(message)
                        .duration(Snackbar.SnackbarDuration.LENGTH_LONG)
                , activity);

    }


}
