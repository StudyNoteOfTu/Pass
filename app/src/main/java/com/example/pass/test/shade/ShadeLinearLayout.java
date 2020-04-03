package com.example.pass.test.shade;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ShadeLinearLayout extends LinearLayout {

    ShadeView shadeView;

    public ShadeView getShadeView() {
        return shadeView;
    }

    public void setShadeView(ShadeView shadeView) {
        this.shadeView = shadeView;
    }

    public ShadeLinearLayout(Context context) {
        super(context);
    }

    public ShadeLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadeLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShadeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (shadeView != null) {
            shadeView.onTouchEvent(event);
        }
        return true;
    }
}
