package com.example.pass.util.spans.movementMethods;

import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.pass.util.spans.ClickableImageSpan;
import com.example.pass.util.spans.callbacks.ClickCallBack;
import com.example.pass.util.spans.callbacks.ClickShadeMovementMethodCallback;
import com.example.pass.util.spans.callbacks.ClickImageMovementMethodCallback;
import com.example.pass.util.spans.customSpans.MyImageSpan;
import com.example.pass.util.spans.customSpans.MyShadeSpan;
import com.example.pass.util.spans.impls.TouchableSpan;

/**
 * 触摸事件
 */
public class ClickableLinkMovementMethod extends LinkMovementMethod {

    private static final String TAG= "ClickableLinkMovementMethod";

    private TouchableSpan mTouchableSpan;


    private int TOUCH_MAX=50;
    private int mLastMotionX;
    private int mLastMotionY;

    private long lastPressTime = 0;

    private ClickCallBack callback;

    public void setCallback(ClickCallBack callback){
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
                    if (mTouchableSpan != null){
                        //如果触摸的事MyImageSpan
                        if (mTouchableSpan instanceof ClickableImageSpan){
                            Drawable drawable = ((ClickableImageSpan) mTouchableSpan).clickGetDrawable();
                            if (callback!=null && callback instanceof ClickImageMovementMethodCallback){
                                ((ClickImageMovementMethodCallback)callback).onClicked(drawable);
                            }
                        }
                        //如果触摸的是MyShadeSpan
                        if (mTouchableSpan instanceof MyShadeSpan){
                            if (callback != null && callback instanceof ClickShadeMovementMethodCallback){
                                ((ClickShadeMovementMethodCallback) callback).onClicked((MyShadeSpan)mTouchableSpan);
                            }
                        }
                    }
//                    if (mClickableImageSpan!=null){
//                        Drawable drawable = mClickableImageSpan.clickInside();
//                        if (callback!=null && callback instanceof ClickImageMovementMethodCallback){
//                            ((ClickImageMovementMethodCallback)callback).onClicked(drawable);
//                        }
//                    }
//                    if (mMyShadeSpan != null){
//                        if (callback != null && callback instanceof ClickShadeMovementMethodCallback){
//                            ((ClickShadeMovementMethodCallback) callback).onClicked(mMyShadeSpan);
//                        }
//                    }
                }
                lastPressTime = 0;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                TouchableSpan touchableSpan = getPressedSpan(widget, buffer, event);
                if (mTouchableSpan != null && touchableSpan != mTouchableSpan){
                    mTouchableSpan = null;
                    break;
                }
                if (Math.abs((int) event.getX() - mLastMotionX) > TOUCH_MAX
                        || Math.abs((int) event.getY() - mLastMotionY) > TOUCH_MAX) {
                    mTouchableSpan = null;
                    break;
                }
//                ClickableImageSpan touchSpan = getPressedImageSpan(widget,buffer,event);
//                if (mClickableImageSpan != null && touchSpan != mClickableImageSpan){
//                    mClickableImageSpan = null;
//                    break;
//                }
//                if (Math.abs((int)event.getX() - mLastMotionX)>TOUCH_MAX
//                        || Math.abs((int)event.getY() - mLastMotionY) > TOUCH_MAX){
//                    mClickableImageSpan = null;
//                    break;
//                }
//
//                MyShadeSpan shadeSpan = getPressedShadeSpan(widget, buffer, event);
//                if (mMyShadeSpan !=null  && shadeSpan != mMyShadeSpan){
//                    mMyShadeSpan = null;
//                    break;
//                }
//                if (Math.abs((int)event.getX() - mLastMotionX)>TOUCH_MAX
//                        || Math.abs((int)event.getY() - mLastMotionY) > TOUCH_MAX){
//                    mMyShadeSpan = null;
//                    break;
//                }
                break;
            }

            case MotionEvent.ACTION_DOWN:{
                mLastMotionX = (int)event.getX();
                mLastMotionY = (int)event.getY();
                lastPressTime = System.currentTimeMillis();
                mTouchableSpan = getPressedSpan(widget, buffer, event);
                break;
            }

//            case MotionEvent.ACTION_DOWN:
//                mClickableImageSpan = getPressedImageSpan(widget,buffer,event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                MyImageSpan touchSpan = getPressedImageSpan(widget,buffer,event);
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


    private TouchableSpan getPressedSpan(TextView textView,Spannable spannable,MotionEvent event){
        TouchableSpan pressedSpan = getPressedImageSpan(textView, spannable, event);
        if (pressedSpan != null) return pressedSpan;
        pressedSpan = getPressedShadeSpan(textView, spannable, event);
        if (pressedSpan != null) return pressedSpan;

        return null;
    }


    private MyImageSpan getPressedImageSpan(TextView textView, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX() - textView.getTotalPaddingLeft() + textView.getScrollX();
        int y = (int) event.getY() - textView.getTotalPaddingTop() + textView.getScrollY();

        Layout layout = textView.getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);


        MyImageSpan[] blockImageSpans = spannable.getSpans(position, position, MyImageSpan.class);
        MyImageSpan touchedSpan = null;
        if (blockImageSpans.length > 0 && positionWithinTag(position, spannable, blockImageSpans[0])
                && blockImageSpans[0].clickInside(x, y)) {
            touchedSpan = blockImageSpans[0];
        }

        return touchedSpan;
    }




    private MyShadeSpan getPressedShadeSpan(TextView textView, Spannable spannable, MotionEvent event) {
        int x = (int) event.getX() - textView.getTotalPaddingLeft() + textView.getScrollX();
        int y = (int) event.getY() - textView.getTotalPaddingTop() + textView.getScrollY();

        Layout layout = textView.getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);

        MyShadeSpan[] clickableTextSpans = spannable.getSpans(0,spannable.length(),MyShadeSpan.class);
        MyShadeSpan touchedSpan = null;
        for (int i = 0 ; i < clickableTextSpans.length;i++){
            if (positionWithinTag(position,spannable,clickableTextSpans[i])){
                Log.d("SpanClickTest","点击命中第"+i+"个");
                touchedSpan = clickableTextSpans[i];
            }
        }

        return touchedSpan;
    }

    private boolean positionWithinTag(int position, Spannable spannable, Object tag) {
        return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag);
    }
}
