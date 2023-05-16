package com.example.smd_project;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.provider.Settings;

import androidx.core.content.res.ResourcesCompat;

public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    private Context c;
    private static final String CHANNEL_ID = "MyChannel";
    private static final int NOTIFICATION_ID = 100;


    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        NotificationGeneration();
    }

    private void NotificationGeneration()
    {
        Drawable drawable = ResourcesCompat.getDrawable( c.getResources(),R.drawable.drop,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        assert bitmapDrawable != null;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        Notification notification;

        String current_date = TImeUtilityClass.currentDateToString();
        String current_Time = TImeUtilityClass.currentTimeToString();
        NotificationManager nm = (NotificationManager)c.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            notification = new Notification.Builder(c)
                    .setSmallIcon(R.drawable.drop)
                    .setLargeIcon(largeIcon)
                    .setChannelId(CHANNEL_ID)
                    .setContentText("Your water time : " + current_date)
                    .setSubText("Water Time " + current_Time)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"Channel 1",NotificationManager.IMPORTANCE_HIGH));
        }
        else
        {
            notification = new Notification.Builder(c)
                    .setSmallIcon(R.drawable.drop)
                    .setLargeIcon(largeIcon)
                    .setContentText("Your water time : " + current_date)
                    .setSubText("Water Time " + current_Time)
                    .build();
        }


        nm.notify(NOTIFICATION_ID,notification);
    }
}
