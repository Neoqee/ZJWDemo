package com.demo.firebase.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.demo.firebase.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG="xiaohai";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: --->"+s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived: "+remoteMessage.getFrom());
        if(!remoteMessage.getData().isEmpty()){
            Log.d(TAG, "data: "+remoteMessage.getData().toString());
        }
        if(remoteMessage.getNotification()!=null){
            Log.d(TAG, "notification: "+remoteMessage.getNotification().getBody());
        }
        showNotification(remoteMessage.getNotification().getBody());

    }

    private void showNotification(String message){
        Log.d(TAG, "showNotification: !!!");

        String channel_id="firebase";

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            CharSequence channel_name="neoqee";
            String description="neoqee's channel!";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel=new NotificationChannel(channel_id,channel_name,importance);
            channel.setDescription(description);
            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channel_id)
                .setContentTitle("neoqee")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(0,builder.build());

    }

}
