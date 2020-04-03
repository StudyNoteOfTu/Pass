package com.example.pass.test.shade;

import android.text.Layout;
import android.text.Spannable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class ShadeManager implements OnLocateListener{
    private static final String TAG ="ShadeManager";
    private WeakReference<TextView> holder;

    public interface OnLocateCallBack{
        /**
         *
         * @param url  图片标识
         * @param x  图片在屏幕的left
         * @param y 图片在屏幕的top
         */
        void onLocate(String url, int x, int y, int originX, int originY);
    }

    private OnLocateCallBack onLocateCallBack;



    @Override
    public void onLocate(String url,int x, int y,int originX, int originY) {
        if (getHolder() == null) return;
//        getHolder().getLocationOnScreen(ints);
        int top = y  - getHolder().getScrollY();
        int left = x + getHolder().getScrollX();
        //这个图片出现在了屏幕上并且开始绘制，此时要同时把方片给绘制上去
        Log.d(TAG,String.format("lcoate on Screen = (x,y) = (%d,%d)",left,top));
//        Log.d(TAG,String.format("bounds.top = %s, bounds.left = %s", y, x));

        if (onLocateCallBack != null){
            onLocateCallBack.onLocate(url,left,top,originX,originY);
        }
    }

    public TextView getHolder() {
        if (holder == null) return null;
        return holder.get();
    }

    public void setHolder(TextView holder) {
        this.holder = new WeakReference<>(holder);
    }


    private ShadeManager(){}

    private static ShadeManager instance = new ShadeManager();

    public static ShadeManager getInstance(){
        return instance;
    }

    public OnLocateCallBack getOnLocateCallBack() {
        return onLocateCallBack;
    }

    public void setOnLocateCallBack(OnLocateCallBack onLocateCallBack) {
        this.onLocateCallBack = onLocateCallBack;
    }

    //获取触摸位置的ImageSpan
    public CenterImageSpan getPressedImageSpan(Spannable spannable,MotionEvent event){

        int x = (int) event.getX() - getHolder().getTotalPaddingLeft() + getHolder().getScrollX();
        int y = (int) event.getY() - getHolder().getTotalPaddingTop() + getHolder().getScrollY();

        Layout layout = getHolder().getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);


        CenterImageSpan[] blockImageSpans = spannable.getSpans(position, position, CenterImageSpan.class);
        CenterImageSpan touchedSpan = null;
        if (blockImageSpans.length > 0 && positionWithinTag(position, spannable, blockImageSpans[0])
                && blockImageSpans[0].clicked(x, y)) {
            touchedSpan = blockImageSpans[0];
        }

        return touchedSpan;
    }

    public List<CenterImageSpan> getAllCenterImageSpan(Spannable spannable){
        return Arrays.asList(spannable.getSpans(0, spannable.length(), CenterImageSpan.class));
    }

    private boolean positionWithinTag(int position, Spannable spannable, Object tag) {
        return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag);
    }
}
