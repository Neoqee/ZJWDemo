package com.demo.remoteview;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

public class MyAppwidgetProvider extends AppWidgetProvider {

    private static final String TAG="widgetProvider";
    private static final String CLICK_ACTION="com.demo.remoteview.action.CLICK";

    public MyAppwidgetProvider(){
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: action="+intent.getAction());
        if(intent.getAction().equals(CLICK_ACTION)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher_round);
                    AppWidgetManager manager=AppWidgetManager.getInstance(context);
                    for (int i=0;i<37;i++){
                        float degree=(i*10)%360;
                        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget);
                        remoteViews.setImageViewBitmap(R.id.widget_img,rotateBitmap(context,bitmap,degree));
                        Intent intentClick=new Intent();
                        intentClick.setAction(CLICK_ACTION);
                        PendingIntent pending=PendingIntent.getBroadcast(context,0,intentClick,0);
                        remoteViews.setOnClickPendingIntent(R.id.widget_img,pending);
                        manager.updateAppWidget(new ComponentName(context,MyAppwidgetProvider.class),remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int count=appWidgetIds.length;
        for (int i=0;i<count;i++){
            int appWidgetId=appWidgetIds[i];
            onWidgetUpdate(context,appWidgetManager,appWidgetId);
        }
    }

    private void onWidgetUpdate(Context context,AppWidgetManager manager,int appWidgetId){
        RemoteViews remoteViews=new RemoteViews(context.getPackageName(),R.layout.widget);
        Intent intentClick=new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pending=PendingIntent.getBroadcast(context,0,intentClick,0);
        remoteViews.setOnClickPendingIntent(R.id.widget_img,pending);
        manager.updateAppWidget(appWidgetId,remoteViews);
    }

    private Bitmap rotateBitmap(Context context,Bitmap bitmap,float degree){
        Matrix matrix=new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tmpBitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return tmpBitmap;
    }

}
