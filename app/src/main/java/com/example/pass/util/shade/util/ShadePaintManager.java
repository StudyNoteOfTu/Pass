package com.example.pass.util.shade.util;

import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.ColorInt;

public class ShadePaintManager {

    private static Paint mPaint;

    private static ShadePaintManager instance= new ShadePaintManager();

    private ShadePaintManager(){
        mPaint = new Paint();
    }

    public static ShadePaintManager getInstance(){
        return instance;
    }

    public static Paint getPaint(){
        return mPaint;
    }

}
