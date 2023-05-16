package com.example.smd_project;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.window.SplashScreen;

import androidx.core.content.res.ResourcesCompat;

public class PillAlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "Channel2";
    private static int NOTIFICATION_ID = 101;
    private Context c;
    String pillId = "";
    String pill_name = "";
    String alarmTimes = "";
    String true_date = "";
    String ChannelId = "Channel" ;

    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        pillId = intent.getStringExtra("AlarmId");
        pill_name = intent.getStringExtra("NameOfPill");
        alarmTimes = intent.getStringExtra("SetDate");
        true_date = intent.getStringExtra("TrueDate");



        NotificationGeneration();

        

        if(alarmTimes != null)
        {
            long interval = 10 * 1000L;
            long totalTime = Long.parseLong(alarmTimes) + interval;

            //ScheduleNext();
        }


    }

    private void ScheduleNext()
    {
        long AlarmTimings = System.currentTimeMillis();
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(c,PillAlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(c,Integer.parseInt(pillId),intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, AlarmTimings + (500L),intervalMillis, pendingIntent);
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,AlarmTimings,pendingIntent);
        }
        else {

            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,AlarmTimings + (500L),intervalMillis,pendingIntent);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,AlarmTimings,pendingIntent);
        }
    }

    private void NotificationGeneration()
    {
        Drawable drawable = ResourcesCompat.getDrawable( c.getResources(),R.drawable.pill,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
        assert bitmapDrawable != null;
        Bitmap largeIcon = bitmapDrawable.getBitmap();

        long timeMillis = Long.parseLong(alarmTimes);

        String current_date = TImeUtilityClass.currentDateToString();
        String current_time = TImeUtilityClass.currentTimeToString();
        Notification notification;
        NotificationManager nm = (NotificationManager)c.getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            notification = new Notification.Builder(c)
                    .setSmallIcon(R.drawable.pill)
                    .setLargeIcon(largeIcon)
                    .setChannelId(CHANNEL_ID)
                    .setContentText("Its pill time " + pill_name + " " + current_time)
                    .setSubText("Pill Time  " + current_date)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"Channel 2",NotificationManager.IMPORTANCE_DEFAULT));
        }
        else
        {
            notification = new Notification.Builder(c)
                    .setSmallIcon(R.drawable.pill)
                    .setLargeIcon(largeIcon)
                    .setContentText("Its pill time " + pill_name + " " + current_time)
                    .setSubText("Pill Time  " + current_date)
                    .build();
        }

        NOTIFICATION_ID = (int) System.currentTimeMillis();
        nm.notify(Integer.parseInt(pillId),notification);
    }
}
