package com.example.pass.view.psEditText;

import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

public class MyLinkMovementMethod extends LinkMovementMethod {


    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        return true;
    }
}
