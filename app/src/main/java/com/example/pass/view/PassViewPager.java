package com.example.pass.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class PassViewPager extends ViewPager {

    boolean isCanScroll = true;

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    public PassViewPager(@NonNull Context context) {
        super(context);
    }

    public PassViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("PassViewPager","dispatchTouchEvent event = "+ev.getAction());
        //硬性传下去
        onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("PassViewPager","onTouchEvent event = "+ev.getAction());
        return isCanScroll && super.onTouchEvent(ev);
    }
}
