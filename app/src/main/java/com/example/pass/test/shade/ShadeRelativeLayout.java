package com.example.pass.test.shade;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class ShadeRelativeLayout extends RelativeLayout {

    ShadeView shadeView;

    public ShadeView getShadeView() {
        return shadeView;
    }

    public void setShadeView(ShadeView shadeView) {
        this.shadeView = shadeView;
    }


    public ShadeRelativeLayout(Context context) {
        super(context);
    }

    public ShadeRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ShadeRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
