package com.example.wisps.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.wisps.MyApplication;
import com.example.wisps.R;
import com.example.wisps.ui.MainActivity;

/**
 * 发布通知的工具类
 * @author David
 * @date 2021/4/13
 */
public class NotificationUtil {

    private static final String TAG = NotificationUtil.class.getSimpleName();

    private static NotificationManager notificationManager;

    private static int id = 0;

    /**
     * 发送警报
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    public static void notifyNotification() {

        init(MyApplication.getAppContext());

        Intent intent = new Intent(MyApplication.getAppContext(), MainActivity.class);
        RemoteInput remoteInput = new RemoteInput.Builder("test").build();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyApplication.getAppContext(), 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(MyApplication.getAppContext(), "WISPS notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // TODO: change the icon
                .setVibrate(new long[] {1000, 1000, 2000, 20})
                .setContentTitle("警报")
                .setContentText("有人闯进你的家中")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())     // 设置发布通知的时间
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        notificationManager.notify(id, notification);
        Log.i(TAG, "notifyNotification: id: " + id);
        ++id;
    }


    /**
     * 初始化通知工具类，在初始化得到NotificationManager并创建新的通知渠道
     * @param context 上下文，用于得到通知服务
     */
    private static void init(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelID = "WISPS notification";
            String channelName = "报警通知";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelID, channelName, importance);
        }
    }


    /**
     * 工具类中的创建新的通知渠道
     * @param channelID 通知渠道的ID
     * @param channelName 通知渠道的名字
     * @param importance 这个通知渠道的重要性
     */
    private static void createNotificationChannel(String channelID, String channelName, int importance) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelID, channelName, importance);
            channel.setDescription("通过MQTT，实现报警通知");
            notificationManager.createNotificationChannel(channel);
        }
    }
}
