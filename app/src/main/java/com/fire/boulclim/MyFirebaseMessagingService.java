package com.fire.boulclim;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.fire.boulclim.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    ///

    ///



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        Log.d("MSG",remoteMessage.getNotification().getBody());
        shownotification(remoteMessage.getNotification());



    }






    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN",s);
    }


    public void shownotification(RemoteMessage.Notification message){


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.fire.boulclim"; //your app package name




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound);
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setSound(soundUri, Notification.AUDIO_ATTRIBUTES_DEFAULT);
            notificationChannel.setDescription("z Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 200, 100,
                     200, 100,
                     200, 100,
                     500, 100,

                     100, 200,
                     100, 500,
                     200, 500,

                     200, 200,
                     500, 500,
                     500, 500,

                     200, 200,
                     500, 500,
                     500, 200,
                     500, 0, 500
            });
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Uri soundUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sound);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(message.getTitle())
                .setContentText(message.getBody())
                .setVibrate(new long[]{0, 200, 100,
                         200, 100,
                         200, 100,
                         500, 100,

                         100, 200,
                         100, 500,
                         200, 500,

                         200, 200,
                         500, 500,
                         500, 500,

                         200, 200,
                         500, 500,
                         500, 200,
                         500, 0, 500
                })
                .setSound(soundUri)
                .setContentInfo("Info");


        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());

    }

}