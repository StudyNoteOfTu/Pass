package com.example.pass.test.shade;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShadeView extends View implements ShadeManager.OnLocateCallBack {

    //所有shader
    List<Shader> allShaders = new ArrayList<>();

    //需要显示的shader
    List<Shader> shaders = new ArrayList<>();


    Map<String,Shader> shaderMap= new HashMap<>();

    //获取所有的CenterImageSpan
    List<CenterImageSpan> centerImageSpans = new ArrayList<>();

    private TextView mTextView;

    private FingerLine fingerPathLine;

    private Paint fingerLinePaint;

    private ShadeManager mShadeManager;

    private Spannable mSpannable;

    public ShadeView(Context context) {
        super(context);
    }

    public ShadeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public ShadeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Spannable getmSpannable() {
        return mSpannable;
    }

    public void setmSpannable(Spannable mSpannable) {
        this.mSpannable = mSpannable;
    }

    public void init(TextView textView, ShadeManager shadeManager, Spannable spannable) {
        mSpannable = spannable;
        mShadeManager = shadeManager;
        mTextView = textView;
        textView.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                onScrollChanged(scrollX,scrollY);
            }
        });
        fingerPathLine = new FingerLine();
        fingerLinePaint = new Paint();
        fingerLinePaint.setColor(Color.RED);
        fingerLinePaint.setStrokeWidth(10f);
        fingerLinePaint.setStyle(Paint.Style.FILL);

        //获取所有centerImageSpan
        centerImageSpans.addAll(mShadeManager.getAllCenterImageSpan(spannable));
    }

    private void onScrollChanged(int scrollX, int scrollY) {
        //管理shader

    }

//    private void addShader(int locationX, int locationY, int width, int height) {
//        Shader shader = new Shader(this,locationX, locationY, width, height);
//        shaders.add(shader);
//    }
//
//    private void addShader(Shader shader){
//        if (shader != null)shaders.add(shader);
//    }


    //    public void delShader(Shader shaderToDelete){
//        shaders.remove(shaderToDelete);
//    }

    private void addShaderToMap(String url,Shader shader){
        if (shader != null) shaderMap.put(url,shader);
    }

//
    public void delShader(String url){
        shaderMap.remove(url);
    }


    int downX;
    int downY;

    int lastX;
    int lastY;

    boolean isDealingShader = false;


    CenterImageSpan touchedSpan;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        //触摸必须在图片上，且不能超出去

        //触摸，如果在图片内，则进行接下来的方片操作
        //如果超出图片范围则取消接下来的操作


        //判断一开始触摸的是在谁里面
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //判断点的在不在优先Shader中,如果不在就把它取消掉
            if (Shader.currentTouchShader != null && !Shader.currentTouchShader.isPointInner(x,y)){
                Shader.currentTouchShader = null;
            }
            //判断在不在shaders中
//            for (Shader shader : shaders) {
//                if (shader.isPointInner(x, y)) {
//                    isDealingShader = true;
//                } else {
//                    //如果不在，就设置Editable = false
//                    shader.setEditable(false);
//                }
//            }

            for (Shader shader : shaderMap.values()){
                if (shader.isPointInner(x, y)) {
                    isDealingShader = true;
                } else {
                    //如果不在，就设置Editable = false
                    shader.setEditable(false);
                }
            }
        }


        //判断按下的位置在哪里。如果不在shader里面，那么就不传给shader
        if (!isDealingShader) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    lastX = (int) event.getX();
                    lastY = (int) event.getY();

                    touchedSpan = mShadeManager.getPressedImageSpan(mSpannable,event);

                    Log.d("ShadeView", "down");
                    downX = (int) event.getX();
                    downY = (int) event.getY();
                    fingerPathLine.setDownX(downX);
                    fingerPathLine.setDownY(downY);
                    break;
                case MotionEvent.ACTION_MOVE:

                    Log.d("ShadeView", "move");
                    fingerPathLine.setCalculating(true);
                    //获取末端位置，绘制线条
                    fingerPathLine.setNowX((int) event.getX());
                    fingerPathLine.setNowY((int) event.getY());
                    fingerPathLine.setWorking(true);

                    //判断fingerPath是否在重要方向区域内
                    //如果不是，则控制TextView

                    if (!fingerPathLine.isInDangerousDirection()){
                        int delY = (int) event.getY() - lastY;
                        mTextView.scrollBy(0,-delY);
                    }

                    lastX = (int) event.getX();
                    lastY = (int) event.getY();


                    break;
                case MotionEvent.ACTION_UP:

                    if (fingerPathLine.isCalculating()) {
                        fingerPathLine.setWorking(false);
                        CenterImageSpan imageSpan = mShadeManager.getPressedImageSpan(mSpannable,event);
                        if (touchedSpan == null ||imageSpan == null|| imageSpan != touchedSpan){
                            //如果不在同个图片上，或者down的时候不在图片上，或者up时候不在图片内
                            //则不可绘制shader
                        }else {
                            dealFingerPathLine(touchedSpan);
                        }

                        fingerPathLine.setCalculating(false);
                    }

                    //重置touchedSpan
                    touchedSpan = null;
                    break;

            }
        } else {
            //如果在方块里面
            //交给最后添加的shader
            //优先交给顶层优先Shader
            if (Shader.currentTouchShader != null){
                Shader.currentTouchShader.onTouch(event);
                isDealingShader = true;
            }else {
//                for (int i = shaders.size() - 1; i >= 0; i--) {
//                    if (shaders.get(i).onTouch(event)) {
//                        //如果处理了，拦截
//                        isDealingShader = true;
//                        break;
//                    }
//                }
                for (Shader shader: shaderMap.values()){
                    if (shader.onTouch(event)){
                        //如果处理了，拦截
                        isDealingShader = true;
                        break;
                    }
                }
            }

        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            isDealingShader = false;
        }
        return false;
    }

    private void dealFingerPathLine(CenterImageSpan touchedSpan) {
        if (fingerPathLine != null) {
            //判断方位，添加Shader
            int delX = fingerPathLine.getNowX() - fingerPathLine.getDownX();
            int delY = fingerPathLine.getNowY() - fingerPathLine.getDownY();
            if (delY > Shader.MIN_HIGHT && delX > Shader.MIN_WIDTH && delY <= 2 * delX) {
//                addShader(fingerPathLine.getDownX(), fingerPathLine.getDownY(), delX, delY);
                Shader shader = new Shader(this,fingerPathLine.getDownX(),fingerPathLine.getDownY(),delX,delY);
                //设置对应的图片
                shader.setImgUrl(touchedSpan.getImgUrl());
                //设置相对位置
                //当前图片在TextView显示的位置
                int top = (int)touchedSpan.y - mTextView.getScrollY();
                int left = (int) touchedSpan.x + mTextView.getScrollX();

                shader.setLeftPadding(fingerPathLine.getDownX() - left);
                shader.setTopPadding(fingerPathLine.getDownY() - top);

                //addShader(shader);

                addShaderToMap(touchedSpan.getImgUrl(),shader);



            }

        }
    }

    List<CenterImageSpan> centerImageSpansShown = new ArrayList<>();

    int y_top;
    int y_bottom;
    @Override
    protected void onDraw(Canvas canvas) {

        //判断哪些CenterImageSpan在屏幕上
        centerImageSpansShown.clear();
        for (CenterImageSpan centerImageSpan : centerImageSpans) {
            y_top = (int)(centerImageSpan.y - mTextView.getScrollY());
            y_bottom = (int)(centerImageSpan.bottom - mTextView.getScrollY());
            if (y_bottom<0 || y_top > mTextView.getMeasuredHeight()){
                //底部已经在上面或者顶部在下面
            }else{
                //在绘制
                centerImageSpansShown.add(centerImageSpan);
            }
        }

        //根据url获取需要绘制的shader
        for (CenterImageSpan centerImageSpan : centerImageSpansShown) {
            Shader shader = shaderMap.get(centerImageSpan.getImgUrl());
            if (shader != null) shader.draw((int)centerImageSpan.x,(int)centerImageSpan.y,mTextView.getScrollX(),mTextView.getScrollY(),canvas);
        }

//        for (Shader shader : shaders) {
//            if (shader != Shader.currentTouchShader) {
//                shader.draw(canvas);
//            }
//        }

//        //如果是当前Touch的顶层绘制
//        if (Shader.currentTouchShader != null) {
//            Shader.currentTouchShader.draw(canvas);
//        }

        //finger线是最顶层绘制的
        if (fingerPathLine != null && fingerPathLine.isWorking() && fingerLinePaint != null) {
            Log.d("ShadeView", "draw line");
            canvas.drawLine(fingerPathLine.getDownX(), fingerPathLine.getDownY(),
                    fingerPathLine.getNowX(), fingerPathLine.getNowY(), fingerLinePaint);
        }

        super.onDraw(canvas);
        invalidate();
    }


    public TextView getmTextView() {
        return mTextView;
    }

    public void setmTextView(TextView mTextView) {
        this.mTextView = mTextView;
    }

    @Override
    public void onLocate(String url, int x, int y,int originX, int originY) {
        Log.d("ShadeViewTAG","onLocate url = "+url+" ,x= "+x+" ,y =  "+y);
        //在所有的shader中重写添加需要显示的shader


    }
}
