package com.demo.remoteview;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    String channelId="example";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createChannel();
        createNotification();

    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channel_name="notify";
            String description="a notification for ordinary";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(channelId,channel_name,importance);
            channel.setDescription(description);
            NotificationManager manager= getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void createNotification() {

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId)
                .setContentTitle("this is a notification title!")
                .setContentText("there is the notification's body!it could be no-fit one line!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("there is the notification's body!it could be no-fit one line!"))
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(0,builder.build());
    }
}
