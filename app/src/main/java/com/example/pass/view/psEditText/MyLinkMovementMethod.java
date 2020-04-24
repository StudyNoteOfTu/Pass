package com.example.pass.view.psEditText;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.PopupWindowCompat;

import com.example.pass.R;
import com.example.pass.util.spans.customSpans.MyImageSpan;
import com.example.pass.view.popWindows.MenuPopWindow;
import com.example.pass.view.psEditText.utils.SoftKeyboardUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MyLinkMovementMethod extends LinkMovementMethod {

    private static final String TAG = "MyLinkMovementMethod";

    private MyImageSpan mPressedSpan;

    private Context mContext;

    //长按事件
    //上一次的触摸位置
    private int mLastMotionX, mLastMotionY;
    //是否移动了
    private boolean isMoved;
    //移动的阈值
    private static final int TOUCH_SLOP = 10;

    private LongClickTimer longClickTimer;
    //popWindow需要的当前View
    private View popParent;
    //pop需要的点击位置
    private int nowX;
    private int nowY;

    public MyLinkMovementMethod(Context context) {
        mContext = context;
    }


    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        if (popParent == null) {
            popParent = widget;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        nowX = x;
        nowY = y;

        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mLastMotionX = x;
                mLastMotionY = y;
                Log.d(TAG,"lastX = "+x+" lastY = "+y);
                longClickTimer = new LongClickTimer(ViewConfiguration.getLongPressTimeout());
                mPressedSpan = getPressedSpan(widget, buffer, event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"nowX = "+x+" nowY = "+y);
                if (Math.abs(mLastMotionX - x) > TOUCH_SLOP
                        || Math.abs(mLastMotionY - y) > TOUCH_SLOP) {
                    if (longClickTimer != null && longClickTimer.timer != null) {
                        longClickTimer.timer.cancel();
                        longClickTimer = null;
                    }
                }
                MyImageSpan touchedSpan = getPressedSpan(widget, buffer, event);
                if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                    mPressedSpan = null;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (longClickTimer != null && longClickTimer.timer != null) {
                    longClickTimer.timer.cancel();
                    longClickTimer = null;
                }
                break;
        }
        return true;
    }

    /**
     * 长按事件
     */
    private void performLongClick(View view, int x, int y) {

        if (mPressedSpan != null) {
            //mPressedSpan.onClick(widget);
            String path = mPressedSpan.getImgUrl();
            SoftKeyboardUtil.hideInput((Activity) mContext);
            showMenuPopWindow(view, x, y);
            Log.d(TAG, "action up, image path = " + path);
        }
        mPressedSpan = null;
    }


    public void showMenuPopWindow(View view, int x, int y) {
        MenuPopWindow window = new MenuPopWindow(mContext);
        View contentView = window.getContentView();
        ///需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(window.getWidth()),
                makeDropDownMeasureSpec(window.getHeight()));

        int offsetX = x;
//        int offsetY = -(window.getContentView().getMeasuredHeight()+childView.getHeight())/2;
        int offsetY = y;
        window.showAtLocation(view, Gravity.START | Gravity.TOP, offsetX, offsetY);

    }

    private MyImageSpan getPressedSpan(TextView widget, Spannable buffer, MotionEvent event) {
        int x = (int) event.getX() - widget.getTotalPaddingLeft() + widget.getScrollX();
        int y = (int) event.getY() - widget.getTotalPaddingTop() + widget.getScrollY();

        Layout layout = widget.getLayout();

        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);

        MyImageSpan[] imageSpans = buffer.getSpans(position, position, MyImageSpan.class);
        MyImageSpan touchedSpan = null;
        if (imageSpans.length > 0 && positionWithinTag(position, buffer, imageSpans[0])
                && imageSpans[0].clickInside(x, y)) {
            touchedSpan = imageSpans[0];
        }

        return touchedSpan;
    }

    private boolean positionWithinTag(int position, Spannable spannable, Object tag) {
        return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag);
    }

    private void safeRemoveSelection(Spannable spannable) {
        if (!spannable.toString().isEmpty()) {
            Selection.removeSelection(spannable);
        }
    }

    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }


    class LongClickTimer {
        public Timer timer;

        public LongClickTimer(int millis) {
            timer = new Timer();
            timer.schedule(new LongClickTask(), millis);
        }

        class LongClickTask extends TimerTask {
            public void run() {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            performLongClick(popParent, nowX, nowY);
                        }
                    });
                }
                timer.cancel();
            }
        }
    }
}
