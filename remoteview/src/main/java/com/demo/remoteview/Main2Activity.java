package com.demo.remoteview;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RemoteViews;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.notification_remoteviews);
        remoteViews.setTextViewText(R.id.title_remote,"The remoteviews title!");
        remoteViews.setTextViewText(R.id.content_remote,"The content!!!");
        remoteViews.setImageViewResource(R.id.image_remote,R.mipmap.ic_launcher_round);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,
                new Intent(this,MainActivity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2=PendingIntent.getActivity(this,0,
                new Intent(this,Main2Activity.class),PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.image_remote,pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.open_remote,pendingIntent2);

        Intent intent=new Intent(MyData.REMOTE_ACTION);
        intent.putExtra(MyData.EXTRA_REMOTEVIEWS,remoteViews);
        sendBroadcast(intent);

    }
}
