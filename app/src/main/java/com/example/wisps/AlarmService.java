package com.example.wisps;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

public class AlarmService extends Service {

    private static final String TAG = AlarmService.class.getSimpleName();

    private Vibrator vibrator;

    private MediaPlayer mediaPlayer;

    private VibratorBinder mBinder = new VibratorBinder();

    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);
        startForeground(1, new Notification());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 用于震动的Binder
     */
    public class VibratorBinder extends Binder {
        public void start() {
            vibrator.vibrate(new long[] { 1000, 1000, 2000, 20 }, 0);
            mediaPlayer.start();
        }

        public void stop() {
            vibrator.cancel();
            mediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}