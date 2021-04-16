package com.example.wisps.ui;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wisps.AlarmService;
import com.example.wisps.OneNetMqttService;
import com.example.wisps.R;
import com.example.wisps.util.NotificationUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private OneNetMqttService.MqttBinder mMqttBinder;

    private AlarmService.VibratorBinder mVibratorBinder;

    // 关于服务的意图
    Intent mqttServiceIntent;
    Intent alarmServiceItent;

    // MqttService的连接类
    private ServiceConnection mqttServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMqttBinder = (OneNetMqttService.MqttBinder) service;
            // 注册从OneNet平台接收警报信息的监听
            mMqttBinder.getService().setOnAlarmListener(new OneNetMqttService.OnAlarmListener() {
                @Override
                public void onAlarm() {
                    Log.i(TAG, "onAlarm");
                    // 执行跳转到alarm_fragment的全局操作
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.action_global_nav_alarm);
                    NotificationUtil.notifyNotification();
                    mVibratorBinder.start();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    // AlarmService的连接类
    private ServiceConnection alarmServiceConnectionn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mVibratorBinder = (AlarmService.VibratorBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // toolbar = (Toolbar) findViewById(R.id.toolbar);

        // 配置NavigationUI
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);

        mqttServiceIntent = new Intent(this, OneNetMqttService.class);
        alarmServiceItent = new Intent(this, AlarmService.class);

        initService();
    }

    /**
     * 开始多个服务并绑定
     */
    private void initService() {
        if (Build.VERSION.SDK_INT > 26) {
            startForegroundService(mqttServiceIntent);
            startForegroundService(alarmServiceItent);
        } else {
            startService(mqttServiceIntent);
            startService(alarmServiceItent);
        }
        bindService(mqttServiceIntent, mqttServiceConnection, BIND_AUTO_CREATE);
        bindService(alarmServiceItent, alarmServiceConnectionn, BIND_AUTO_CREATE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
            || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mqttServiceIntent);
        stopService(alarmServiceItent);
        unbindService(mqttServiceConnection);
        unbindService(alarmServiceConnectionn);
    }

    public AlarmService.VibratorBinder getVibratorBinder() {
        return mVibratorBinder;
    }
}