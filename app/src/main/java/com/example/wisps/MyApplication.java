package com.example.wisps;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application  {

    private static final String TAG = MyApplication.class.getSimpleName();

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
