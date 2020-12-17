package com.jacd.smscommunity.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jacd.smscommunity.R;
import com.jacd.smscommunity.ui.MainActivity;

import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;

public abstract class NotificationsTask {

    private static Context mContext;
    private static final String CHANNEL_ID = "com.jacd.smscommunity.ANDROID";
    public static final String ANDROID_CHANNEL_ID = "com.jacd.smscommunity.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "notifications_smscommunity_android";

    public abstract class Tasks {
        public static final int SECOND = 1000;
        public static final int TIME_SLEEP_NOTIFICATIONS = 30 * SECOND;
        public static final int ICONS_SMALL = R.drawable.ic_baseline_send_24;
    }

    /*
    TODO: eliminar notificaciones
    NotificationManager notificationManager = ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE));
        notificationManager.cancelAll();
    */

    public static void createNotificacions(Context context, String title, String content, String contendInfo, int id) {
        NotificationCompat.Builder mBuilder;

        int icono = Tasks.ICONS_SMALL;
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 250, 100, 500})
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setSound(sonido)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 250, 100, 500})
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setSound(sonido)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ANDROID_CHANNEL_NAME;
            String description = ANDROID_CHANNEL_NAME;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager mNotifyMgr = context.getSystemService(NotificationManager.class);
            mNotifyMgr.createNotificationChannel(channel);
            mNotifyMgr.notify(id, mBuilder.build());
        } else {
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(id, mBuilder.build());
        }

    }

    public static void createNotificacionsSilent(Context context, String title, String content, String contendInfo, int id) {
        NotificationCompat.Builder mBuilder;

        int icono = Tasks.ICONS_SMALL;
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentIntent(pendingIntent)
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ANDROID_CHANNEL_NAME;
            String description = ANDROID_CHANNEL_NAME;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager mNotifyMgr = context.getSystemService(NotificationManager.class);
            mNotifyMgr.createNotificationChannel(channel);
            mNotifyMgr.notify(id, mBuilder.build());
        } else {
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(id, mBuilder.build());
        }

    }

    public static void createNotificacionsSilent(Context context, String title, String content, String contendInfo, boolean showIntent, int id) {
        NotificationCompat.Builder mBuilder;

        int icono = Tasks.ICONS_SMALL;
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentTitle(title)
                    .setContentText(content)
                    //.setContentIntent(pendingIntent)
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    //.setContentIntent(pendingIntent)
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        if (showIntent){
            mBuilder.setContentIntent(pendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ANDROID_CHANNEL_NAME;
            String description = ANDROID_CHANNEL_NAME;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager mNotifyMgr = context.getSystemService(NotificationManager.class);
            mNotifyMgr.createNotificationChannel(channel);
            mNotifyMgr.notify(id, mBuilder.build());
        } else {
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(id, mBuilder.build());
        }

    }

    public static void createNotificacionsSilent(Context context, int icono, String title, String content, String contendInfo, boolean showIntent, int id) {
        NotificationCompat.Builder mBuilder;

        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentTitle(title)
                    .setContentText(content)
                    //.setContentIntent(pendingIntent)
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    //.setContentIntent(pendingIntent)
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(content))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        if (showIntent){
            mBuilder.setContentIntent(pendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ANDROID_CHANNEL_NAME;
            String description = ANDROID_CHANNEL_NAME;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager mNotifyMgr = context.getSystemService(NotificationManager.class);
            mNotifyMgr.createNotificationChannel(channel);
            mNotifyMgr.notify(id, mBuilder.build());
        } else {
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(id, mBuilder.build());
        }

    }


    public static void createNotificacions(Context context, String title, String content, Bitmap urlImage, String contendInfo, int id) {
        NotificationCompat.Builder mBuilder;

        int icono = Tasks.ICONS_SMALL;
        Intent i = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        Uri sonido = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 250, 100, 500})
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setSound(sonido)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(urlImage))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icono)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{100, 250, 100, 500})
                    .setTicker("tickers")
                    .setContentInfo(contendInfo)
                    .setSound(sonido)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(urlImage))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ANDROID_CHANNEL_NAME;
            String description = ANDROID_CHANNEL_NAME;
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager mNotifyMgr = context.getSystemService(NotificationManager.class);
            mNotifyMgr.createNotificationChannel(channel);
            mNotifyMgr.notify(id, mBuilder.build());
        } else {
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(id, mBuilder.build());
        }

    }
}
