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

    public static Paint getPaint(boolean isAlpha255){
        Paint paint = new Paint();
        paint.setColor(0xffffff00);
        paint.setStyle(Paint.Style.FILL);
        if (isAlpha255){
            paint.setAlpha(255);
            return paint;
        }else{
            paint.setAlpha(180);
            return paint;
        }
    }

}
