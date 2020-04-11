package com.example.pass.activities;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.example.pass.configs.ScreenConfig;

public class MyApplication extends Application {

    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initScreenParams();

    }

    public static Context getContextInstance(){
        return mContext;
    }

    private void initScreenParams() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        ScreenConfig.setmScreenWidth(metrics.widthPixels);
        ScreenConfig.setmImageTargetWidth((int)(metrics.widthPixels * 0.9));
        ScreenConfig.setmScreenHeight(metrics.heightPixels);
    }
}
