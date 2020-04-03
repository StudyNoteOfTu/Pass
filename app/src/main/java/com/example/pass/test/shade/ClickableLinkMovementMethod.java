package com.example.pass.test.shade;


import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;


public class ClickableLinkMovementMethod extends LinkMovementMethod {

    private static final String TAG= "ClickableLinkMovementMethod";




    private int TOUCH_MAX=50;
    private int mLastMotionX;
    private int mLastMotionY;

    private long lastPressTime = 0;


    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        super.onTouchEvent(widget,buffer,event);

        return true;
    }



    private CenterImageSpan getPressedImageSpan(TextView textView, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX() - textView.getTotalPaddingLeft() + textView.getScrollX();
        int y = (int) event.getY() - textView.getTotalPaddingTop() + textView.getScrollY();

        Layout layout = textView.getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);


        CenterImageSpan[] blockImageSpans = spannable.getSpans(position, position, CenterImageSpan.class);
        CenterImageSpan touchedSpan = null;
        if (blockImageSpans.length > 0 && positionWithinTag(position, spannable, blockImageSpans[0])
                && blockImageSpans[0].clicked(x, y)) {
            touchedSpan = blockImageSpans[0];
        }

        return touchedSpan;
    }





    private boolean positionWithinTag(int position, Spannable spannable, Object tag) {
        return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag);
    }
}