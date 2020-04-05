package com.example.pass.util.shade;

import android.text.Layout;
import android.text.Spannable;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.pass.util.spans.customSpans.MyImageSpan;
import com.example.pass.util.spans.customSpans.MyShadeSpan;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

public class ShadeManager  {


    private WeakReference<TextView> holder;

    public interface OnLocateCallBack{
        /**
         *
         * @param url  图片路径标识
         */
        void onLocate(String url);
    }
    private OnLocateCallBack onLocateCallBack;

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


    //获取触摸位置的ShadeSpan
//    public MyShadeSpan getPressedShadeSpan(Spannable spannable,int eventX, int eventY){
//
//    }

    //获取触摸位置的ImageSpan
    public MyImageSpan getPressedImageSpan(Spannable spannable, MotionEvent event){

        int x = (int) event.getX() - getHolder().getTotalPaddingLeft() + getHolder().getScrollX();
        int y = (int) event.getY() - getHolder().getTotalPaddingTop() + getHolder().getScrollY();

        Layout layout = getHolder().getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);


        MyImageSpan[] blockImageSpans = spannable.getSpans(position, position, MyImageSpan.class);
        MyImageSpan touchedSpan = null;
        if (blockImageSpans.length > 0 && positionWithinTag(position, spannable, blockImageSpans[0])
                && blockImageSpans[0].clicked(x, y)) {
            touchedSpan = blockImageSpans[0];
        }

        return touchedSpan;
    }

    public List<MyImageSpan> getAllCenterImageSpan(Spannable spannable){
        return Arrays.asList(spannable.getSpans(0, spannable.length(), MyImageSpan.class));
    }

    private boolean positionWithinTag(int position, Spannable spannable, Object tag) {
        return position >= spannable.getSpanStart(tag) && position <= spannable.getSpanEnd(tag);
    }

    public OnLocateCallBack getOnLocateCallBack() {
        return onLocateCallBack;
    }

    public void setOnLocateCallBack(OnLocateCallBack onLocateCallBack) {
        this.onLocateCallBack = onLocateCallBack;
    }

    public void imageAppear(String url){
        if (onLocateCallBack != null){
            onLocateCallBack.onLocate(url);
        }
    }
}
