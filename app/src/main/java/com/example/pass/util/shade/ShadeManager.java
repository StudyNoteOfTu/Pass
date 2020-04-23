package com.example.pass.util.shade;

import android.graphics.Rect;
import android.text.Layout;
import android.text.Spannable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.pass.util.shade.viewAndModels.TextShader;
import com.example.pass.util.spans.customSpans.MyImageSpan;
import com.example.pass.util.spans.customSpans.MyShadeSpan;
import com.example.pass.util.spans.utils.SpanTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShadeManager {

    public long data = System.currentTimeMillis();

    private static ShadeManager currentShadeManager;


    private TextView holder;

    public interface OnLocateCallBack {
        /**
         * @param url 图片路径标识
         */
        void onLocate(String url);
    }

    private OnLocateCallBack onLocateCallBack;

    public TextView getHolder() {
        return holder;
    }

    public void setHolder(TextView holder) {
        this.holder = holder;
    }


    private ShadeManager() {
    }

    public static ShadeManager getInstance() {
        return currentShadeManager;
    }

    public static ShadeManager newInstance() {
        return new ShadeManager();
    }


    //获取触摸位置的ShadeSpan
//    public MyShadeSpan getPressedShadeSpan(Spannable spannable,int eventX, int eventY){
//
//    }

    //获取触摸位置的ImageSpan
    public MyImageSpan getPressedImageSpan(Spannable spannable, MotionEvent event) {

        int x = (int) event.getX() - getHolder().getTotalPaddingLeft() + getHolder().getScrollX();
        int y = (int) event.getY() - getHolder().getTotalPaddingTop() + getHolder().getScrollY();

        Layout layout = getHolder().getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);


        MyImageSpan[] blockImageSpans = spannable.getSpans(position, position, MyImageSpan.class);
        MyImageSpan touchedSpan = null;
        if (blockImageSpans.length > 0 && positionWithinTag(position, spannable, blockImageSpans[0])
                && blockImageSpans[0].clickInside(x, y)) {
            touchedSpan = blockImageSpans[0];
        }

        return touchedSpan;
    }

    public MyImageSpan getPressedImageSpan(Spannable spannable, int downX, int downY) {

        int x = (int) downX - getHolder().getTotalPaddingLeft() + getHolder().getScrollX();
        int y = (int) downY - getHolder().getTotalPaddingTop() + getHolder().getScrollY();

        Layout layout = getHolder().getLayout();
        int position = layout.getOffsetForHorizontal(layout.getLineForVertical(y), x);


        MyImageSpan[] blockImageSpans = spannable.getSpans(position, position, MyImageSpan.class);
        MyImageSpan touchedSpan = null;
        if (blockImageSpans.length > 0 && positionWithinTag(position, spannable, blockImageSpans[0])
                && blockImageSpans[0].clickInside(x, y)) {
            touchedSpan = blockImageSpans[0];
        }

        return touchedSpan;
    }

    public List<MyImageSpan> getAllCenterImageSpan(Spannable spannable) {
        return Arrays.asList(spannable.getSpans(0, spannable.length(), MyImageSpan.class));
    }

    //获得所有shadeSpan
    public List<TextShader> getAllShadeSpan(Spannable spannable) {
        return transformAllShadeSpanToTextShaderList(spannable, Arrays.asList(spannable.getSpans(0, spannable.length(), MyShadeSpan.class)));
    }

    public List<TextShader> transformAllShadeSpanToTextShaderList(Spannable spannable, List<MyShadeSpan> shadeSpans) {
        List<TextShader> shaders = new ArrayList<>();
        TextShader textShader;
        for (MyShadeSpan shadeSpan : shadeSpans) {
            textShader = transformShadeSpanToTextShader(spannable, shadeSpan);
            if (textShader != null) shaders.add(textShader);
        }
        return shaders;
    }

    //获取单个shadeSpan的TextShader
    public TextShader transformShadeSpanToTextShader(Spannable spannable, MyShadeSpan shadeSpan) {

        Layout layout = getHolder().getLayout();
        Log.d("2020411F", "text is null? " + (getHolder() == null));
        if (layout == null) {
            Log.d("2020411F", "layout is null");
            return null;
        }
        int span_start = spannable.getSpanStart(shadeSpan);
        int span_end = spannable.getSpanEnd(shadeSpan);

        Rect bound = new Rect();
        int line = layout.getLineForOffset(span_start);
        layout.getLineBounds(line, bound);
        int top = bound.top;
        int bottom = bound.bottom;
        int left = (int) layout.getPrimaryHorizontal(span_start);
        int right = (int) layout.getSecondaryHorizontal(span_end);

        return new TextShader(top, left, right, bottom);

    }

    public int getPressTextInLine(int downY, int nowY) {
        Layout layout = getHolder().getLayout();
        int downLine = layout.getLineForVertical(downY);
        int nowLine = layout.getLineForVertical(nowY);
        int midLine = layout.getLineForVertical((downY + nowY) / 2);
        Log.d("PressTextInLineTest", "downLine = " + downLine + " ,nowLine = " + nowLine + " ,midLine = " + midLine);
        if (Math.abs(downLine - nowLine) > 2) {
            return -1;
        }
        if (downLine == midLine) {
            return downLine;
        } else if (nowLine == midLine) {
            return midLine;
        } else {
            return midLine;
        }
    }

    public Spannable insertTextShadeSpan(Spannable spannable, int line, int startX, int endX) {
        Layout layout = getHolder().getLayout();
        int startPosition = layout.getOffsetForHorizontal(line, startX);
        int endPosition = layout.getOffsetForHorizontal(line, endX);

        MyShadeSpan shadeSpan = new MyShadeSpan();
        Log.d("TextShadeTAG2", "before size = " + spannable.getSpans(0, spannable.length(), MyShadeSpan.class).length);
        Spannable resultSpannable = SpanTool.handleInsert(spannable, startPosition, endPosition, shadeSpan);
        Log.d("TextShadeTAG2", "size = " + resultSpannable.getSpans(0, spannable.length(), MyShadeSpan.class).length);

        //设置最新span，并通知更新
        getHolder().setText(resultSpannable);
        return resultSpannable;
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

    public void imageAppear(String url) {
        if (onLocateCallBack != null) {
            onLocateCallBack.onLocate(url);
        }
    }

    public static ShadeManager getCurrentShadeManager() {
        return currentShadeManager;
    }

    public static void setCurrentShadeManager(ShadeManager currentShadeManager) {
        Log.d("2020419CurrentShadeManager","setCurrentShadeManager");
        ShadeManager.currentShadeManager = currentShadeManager;
    }
}
