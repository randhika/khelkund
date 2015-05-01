package com.appacitive.khelkund.infra.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.appacitive.khelkund.R;
import com.appacitive.khelkund.activities.fantasy.ViewTeamActivity;
import com.appacitive.khelkund.activities.misc.HomeActivity;
import com.appacitive.khelkund.activities.pick5.Pick5ListActivity;
import com.appacitive.khelkund.activities.privateleague.PrivateLeagueHomeActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;


public class GcmIntentService extends IntentService {
    public int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    String TAG = "GCM";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                sendNotification(extras);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(final Bundle bundle) {
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = null;
        String game = bundle.getString("game");
        if (TextUtils.isEmpty(game) == false) {
            if (game.contains("fantasy"))
                contentIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, ViewTeamActivity.class), 0);
            else if (game.contains("pick"))
                contentIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, Pick5ListActivity.class), 0);
            else if (game.contains("private"))
                contentIntent = PendingIntent.getActivity(this, 0,
                        new Intent(this, PrivateLeagueHomeActivity.class), 0);

        } else contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, HomeActivity.class), 0);


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSmallIcon(R.drawable.allrounder)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.launcher))
                        .setDefaults(Notification.DEFAULT_SOUND);

        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(bundle.getString("title"));
        bigTextStyle.bigText(bundle.getString("alert"));
        mBuilder.setStyle(bigTextStyle);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}