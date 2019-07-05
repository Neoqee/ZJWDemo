package com.demo.remoteview;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG="mainActivity";

    String channelId="example";
    private LinearLayout mRemoteViewsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        createChannel();
//        createNotification();
        initView();

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mRemoteViewsReceiver);
        super.onDestroy();
    }

    private void initView() {
        mRemoteViewsContent = findViewById(R.id.linear_layout);
        IntentFilter filter=new IntentFilter(MyData.REMOTE_ACTION);
        registerReceiver(mRemoteViewsReceiver,filter);
        TextView textView=findViewById(R.id.text_view);
        textView.setOnClickListener(this);
    }

    private BroadcastReceiver mRemoteViewsReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews=intent.getParcelableExtra(MyData.EXTRA_REMOTEVIEWS);
            if(remoteViews!=null){
                updateUI(remoteViews);
            }
        }
    };

    private void updateUI(RemoteViews remoteViews) {

        int layoutId=getResources().getIdentifier("notification_remoteviews","layout",getPackageName());
        View view=getLayoutInflater().inflate(layoutId,mRemoteViewsContent,false);

        /**
         * apply和reapply的区别：
         * apply 加载布局并更新界面
         * reapply 只更新界面
         */
//        View view=remoteViews.apply(this,mRemoteViewsContent);
        remoteViews.reapply(this,view);
        mRemoteViewsContent.removeAllViews();
        mRemoteViewsContent.addView(view);
    }


    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channel_name="notify";
            String description="a notification for ordinary";
            int importance=NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(channelId,channel_name,importance);
            channel.setDescription(description);
            NotificationManager manager= getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }
    }

    private void createNotification() {

        Intent intent=new Intent(this,Main2Activity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews remoteViews=new RemoteViews(this.getPackageName(),R.layout.notification_remoteviews);
        remoteViews.setImageViewResource(R.id.image_remote,R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.title_remote,"Notification title!");
        remoteViews.setTextViewText(R.id.content_remote,"There is the content !");
//        remoteViews.setOnClickPendingIntent(R.id.open_remote,pendingIntent);

        Bitmap largeIcon= BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,channelId)
                .setContentTitle("this is a notification title!")
                .setContentText("there is the notification's body!it could be no-fit one line!")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(largeIcon)
//                .setShowWhen(true)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                .bigText("there is the notification's body!it could be no-fit one line!there is the notification's body!it could be no-fit one line!there is the notification's body!it could be no-fit one line!"))
                .setAutoCancel(true)
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
//                .setContent(remoteViews)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat=NotificationManagerCompat.from(this);
        managerCompat.notify(0,builder.build());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.text_view) {
            Intent intent=new Intent(this,Main2Activity.class);
            startActivity(intent);
        }
    }
}
