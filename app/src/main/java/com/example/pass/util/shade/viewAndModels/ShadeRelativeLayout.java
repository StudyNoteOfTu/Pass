package com.example.pass.util.shade.viewAndModels;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShadeRelativeLayout extends RelativeLayout {

    ShadeView shadeView;
    View sendTouchView;

    public ShadeView getShadeView() {
        return shadeView;
    }


    public void setShadeView(ShadeView shadeView) {
        this.shadeView = shadeView;
    }

    public View getSendTouchView() {
        return sendTouchView;
    }

    public void setSendTouchView(View sendTouchView) {
        this.sendTouchView = sendTouchView;
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
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);

        Log.d("2020411D","visibility"+shadeView.getVisibility());
        if (shadeView != null && shadeView.getVisibility()== VISIBLE) {
            shadeView.onTouchEvent(event);
        }else{
            sendTouchView.onTouchEvent(event);
        }
        return true;
    }
}
