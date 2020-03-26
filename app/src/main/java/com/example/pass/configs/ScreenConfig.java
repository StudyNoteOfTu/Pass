package com.example.pass.configs;

import android.util.Log;

public class ScreenConfig {

    private static final String TAG ="ScreenConfig";

    private static int mScreenWidth;

    private static int mScreenHeight;

    public static int getmImageTargetWidth() {
        return mImageTargetWidth;
    }

    public static void setmImageTargetWidth(int mImageTargetWidth) {
        ScreenConfig.mImageTargetWidth = mImageTargetWidth;
    }

    private static int mImageTargetWidth;

    public static int getmScreenWidth() {
        return mScreenWidth;
    }

    public static void setmScreenWidth(int mScreenWidth) {
        ScreenConfig.mScreenWidth = mScreenWidth;
        Log.d(TAG,"setScreenWidth"+mScreenWidth);
    }

    public static int getmScreenHeight() {
        return mScreenHeight;
    }

    public static void setmScreenHeight(int mScreenHeight) {
        ScreenConfig.mScreenHeight = mScreenHeight;
        Log.d(TAG,"setScreenHeight"+mScreenHeight);
    }
}
