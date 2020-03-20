package com.example.pass.util.spans.movementMethods;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.pass.util.spans.ClickableImageSpan;
import com.example.pass.util.spans.callbacks.ClickMovementMethodCallback;


public class ClickableLinkMovementMethod extends LinkMovementMethod {

    private static final String TAG= "ClickableLinkMovementMethod";
    private ClickableImageSpan mClickableImageSpan;


    private int TOUCH_MAX=50;
    private int mLastMotionX;
    private int mLastMotionY;

    private long lastPressTime = 0;

    private ClickMovementMethodCallback callback;

    public void setCallback(ClickMovementMethodCallback callback){
        this.callback = callback;
    }


    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        super.onTouchEvent(widget,buffer,event);
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:{
//                handler.removeCallbacks(r);
                if (Math.abs(System.currentTimeMillis() - lastPressTime )> TOUCH_MAX){
                    if (mClickableImageSpan!=null){
                        Drawable drawable = mClickableImageSpan.clicked();
                        if (callback!=null){
                            callback.onClicked(drawable);
                        }
                    }
                }
                lastPressTime = 0;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                ClickableImageSpan touchSpan = getPressedSpan(widget,buffer,event);
                if (mClickableImageSpan != null && touchSpan != mClickableImageSpan){
                    mClickableImageSpan = null;
                    break;
                }
                if (Math.abs((int)event.getX() - mLastMotionX)>TOUCH_MAX
                        || Math.abs((int)event.getY() - mLastMotionY) > TOUCH_MAX){
                    mClickableImageSpan = null;
                    break;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN:{
                mLastMotionX = (int)event.getX();
                mLastMotionY = (int)event.getY();
                lastPressTime = System.currentTimeMillis();
                mClickableImageSpan = getPressedSpan(widget, buffer, event);
                break;
            }

//            case MotionEvent.ACTION_DOWN:
//                mClickableImageSpan = getPressedSpan(widget,buffer,event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                ClickableImageSpan touchSpan = getPressedSpan(widget,buffer,event);
//                if (mClickableImageSpan != null && touchSpan != mClickableImageSpan){
//                    mClickableImageSpan = null;
//                    break;
//                }
//
//            default:
//                if (mClickableImageSpan !=null){
//                    if (MotionEvent.ACTION_UP == action){
//                        mClickableImageSpan.onClick(widget);
//                    }
//                }
//                mClickableImageSpan = null;
//                break;

        }
        return true;
    }



    private ClickableImageSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX() - textView.getTotalPaddingLeft() + textView.getScrollX();
        int y = (int) event.getY() - textView.getTotalPaddingTop() + textView.getScrollY();

        Layout layout = textView.getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);

        ClickableImageSpan[] blockImageSpans = spannable.getSpans(position, position, ClickableImageSpan.class);
        ClickableImageSpan touchedSpan = null;
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
