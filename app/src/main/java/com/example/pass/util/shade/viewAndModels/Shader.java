package com.example.pass.util.shade.viewAndModels;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.example.pass.util.shade.impls.Touchable;
import com.example.pass.util.shade.util.LongClickTimer;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class Shader implements Touchable {


    //与图片进行数据绑定
    private String imgUrl;

    private String timeTag;

    //在图片中的位置偏移量
    private int leftPadding;

    //在图片中的位置偏移量
    private int topPadding;

    //图片的底下位置(屏幕显示的位置）
    private int mImageBottom;

    //图片顶上位置
    private int mImageTop;

    //顶层优先显示优先触摸的Shader
    public static Shader currentTouchShader;

    private static final int OVER_DISTANCE = 50;
    private int mLocationX;
    private int mLocationY;

    private int mWidth;
    private int mHeight;

    private final int OUTER_RADIUS = 80;
    private final int INNER_RADIUS= 50;


    public static final int MIN_WIDTH = 100;
    public static final int MIN_HEIGHT = 50;

    private Paint mRectPaint;

    private Paint mCirclePaint;

    private Paint mCircleInnerPaint;

    private boolean isEditable;

    private LongClickTimer shaderLongClickTimer;

    private LongClickTimer cancelLongClickTimer;

    private ShadeView mShadeView;

    public Shader(ShadeView shadeView,int locationX, int locationY,int width, int height) {
        mShadeView = shadeView;
        mLocationX = locationX;
        mLocationY = locationY;
        mWidth = width;
        mHeight = height;

        mRectPaint = new Paint();
//        mRectPaint.setColor(Color.WHITE);
        mRectPaint.setColor(Color.YELLOW);
        mRectPaint.setStyle(Paint.Style.FILL);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.GREEN);
        mCirclePaint.setStyle(Paint.Style.FILL);

        mCircleInnerPaint = new Paint();
        mCircleInnerPaint.setColor(Color.BLUE);
        mCircleInnerPaint.setStyle(Paint.Style.FILL);

        isEditable = true;

    }



    int lastX = -1;
    int lastY= -1;

    @Override
    public boolean onTouch(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        //判断触摸的点是否在区域内
        //如果在区域内，则锁定当前操作的Shader为该Shader


        if (isPointInner(x,y)){
            //是否可编辑
            if (isEditable){
                //开始操作，获取down事件
                if (event.getAction() == ACTION_DOWN){
                    currentTouchShader = this;
                }

                //判断是不是矩形
                if(pointInCircle(mLocationX+mWidth,mLocationY, OUTER_RADIUS,x,y)){
                    Log.d("ShaderTAG","pointInCircle");
                    //叉叉
                    //点击事件
                    switch (event.getAction()){
                        case ACTION_DOWN:
                            lastX = (int)event.getX();
                            lastY = (int)event.getY();
                            cancelLongClickTimer = new LongClickTimer(500);
                            break;
                        case ACTION_UP:
                            int thisX = (int) event.getX();
                            int thisY = (int) event.getY();
                            //判断移动范围
                            if (!isOverDistance(lastX,lastY,thisX,thisY)) {
                                if (cancelLongClickTimer != null && cancelLongClickTimer.timer != null){
                                    //短按
                                    //状态翻转
                                    cancelLongClickTimer.timer.cancel();
                                    cancelLongClickTimer = null;

                                    endLife();
                                }else{
                                    //长按
                                }
                            }
                            break;
                    }

                }else if(pointInCircle(mLocationX+mWidth,mLocationY+mHeight, OUTER_RADIUS,x,y)){
                    Log.d("ShaderTAG","pointInCircle");
                    //缩放
                    switch (event.getAction()){
                        case ACTION_DOWN:
                            lastX = x;
                            lastY = y;
                            break;
                        case ACTION_MOVE:
                            if (x == -1 || y == -1){
                                lastX = x;
                                lastY = y;
                            }else {

                                mWidth += x - lastX;
                                mHeight += y - lastY;

                                //不允许锁到负向

                                if (mWidth < MIN_WIDTH){
//                                    mWidth -= x- lastX;
                                    //改为重置状态
                                    mWidth = MIN_WIDTH;
                                }

                                //高度太小或者太高超出底下边框
                                if (mHeight <= MIN_HEIGHT){
//                                    mHeight -= y- lastY;
                                    //改为重置状态
                                    mHeight = MIN_HEIGHT;
                                }else if(mLocationY+mHeight > mImageBottom){
                                    mHeight = mImageBottom - mLocationY;
                                }



                                lastX = x;
                                lastY = y;
                            }
                            break;
                        case ACTION_UP:
                            break;
                    }
                }else if (pointInRect(x,y)){
                    Log.d("ShaderTAG","pointInRect");
                    switch (event.getAction()){
                        case ACTION_DOWN:
                            lastX = x;
                            lastY = y;
                            break;
                        case ACTION_MOVE:
                            if (x == -1 || y == -1){
                                lastX = x;
                                lastY = y;
                            }else {
                                leftPadding += x - lastX;
                                topPadding += y - lastY;
                                //刷新上次触摸位置

                                if (leftPadding <0 ){
//                                    leftPadding -= x- lastX;
                                    //改为重置状态
                                    leftPadding = 0;
                                }

                                if (topPadding <0){
//                                    Log.d("ShaderTestHeight","mLocationY+mHeight = "+(mLocationY+mHeight)+" mImageBottom = "+mImageBottom);
//                                    //撤销状态
//                                    topPadding -= y - lastY;
                                    //不应撤销状态，而改为重置状态
                                    topPadding = 0;
                                }else if(mLocationY+mHeight > mImageBottom){
                                    //画图看
                                    topPadding = mImageBottom - mImageTop - mHeight;
                                }

                                lastX = x;
                                lastY = y;
                            }
                            break;
                        case ACTION_UP:
                            break;
                    }
                }


            }else{
                Log.d("ShaderTAG","not editable touch");
                //若不可编辑，判断是否点击事件，点击事件就变为可编辑
                switch (event.getAction()){
                    case ACTION_DOWN:
                        lastX = (int)event.getX();
                        lastY = (int)event.getY();
                        shaderLongClickTimer = new LongClickTimer(500);
                        break;
                    case ACTION_UP:
                        int thisX = (int) event.getX();
                        int thisY = (int) event.getY();
                        //判断移动范围
                        if (!isOverDistance(lastX,lastY,thisX,thisY)) {
                            if (shaderLongClickTimer != null && shaderLongClickTimer.timer != null){
                                //短按
                                //状态翻转
                                shaderLongClickTimer.timer.cancel();
                                shaderLongClickTimer = null;
                                isEditable = !isEditable;

                                currentTouchShader = this;

                            }else{
                                //长按
                            }
                        }
                        break;
                }
            }

            return true;
        }
        return false;
    }

    private void endLife(){
        if (currentTouchShader == this){
            currentTouchShader = null;
        }
//        mShadeView.delShader(this);
        mShadeView.delShader(imgUrl,timeTag);
    }

    private boolean isOverDistance(int lastX, int lastY, int thisX, int thisY) {
        int distanceX = Math.abs(lastX - thisX);
        int distanceY = Math.abs(lastY - thisY);
        int distance = (int) Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));
        return distance >= OVER_DISTANCE;
    }

    /**
     * 判断点击区域是否在区域内
     * @param x
     * @param y
     * @return
     */
    public boolean isPointInner(int x, int y) {
        //是否编辑状态
        if (isEditable){
            //矩形和圆
            if (pointInRect(x,y)||pointInCircle(mLocationX+mWidth,mLocationY, OUTER_RADIUS,x,y)||pointInCircle(mLocationX+mWidth,mLocationY+mHeight, OUTER_RADIUS,x,y)){
                Log.d("PointerTAG","pointInner, isEditable");
                return true;
            }else{
                return false;
            }
        }else{
            //点击进入可编辑状态
            //矩形
            if (pointInRect(x,y)){
                Log.d("PointerTAG","pointInner, not Editable");
                return true;
            }else{
                return false;
            }
        }
    }

    private boolean pointInCircle(int mLocationX, int mLocationY, int radius, int x, int y) {
        int distanceX = Math.abs(mLocationX - x);
        int distanceY = Math.abs(mLocationY - y);
        int distance = (int) Math.sqrt(Math.pow(distanceX,2)+Math.pow(distanceY,2));
        if (distance > radius){
            return false;
        }else{
            Log.d("ShaderTAG","point in circle");
            return true;
        }
    }

    private boolean pointInRect(int x,int y){
        Rect rect = new Rect(mLocationX,mLocationY,mLocationX+mWidth,mLocationY+mHeight);
        return rect.contains(x,y);
    }


    public void draw(int picLeft, int picTop, int bottom, int scrollX, int scrollY, Canvas canvas){
        mImageBottom = bottom - scrollY;
        mImageTop = picTop - scrollY;
        mLocationX = picLeft + scrollX +leftPadding;
        mLocationY = picTop- scrollY + topPadding;
        Log.d("FastTest1","mLocationX = "+mLocationX +" ,mLocationY = "+mLocationY);
        if (isEditable){
            //是否可编辑
            //方片和编辑圆 都画
            drawRectAndCircle(picLeft- scrollX,picTop - scrollY,canvas);
        }else{
            //只画方片
            drawRect(picLeft- scrollX,picTop - scrollY,canvas);
        }
    }

    private void drawRectAndCircle(int positionX, int positionY, Canvas canvas){

//        canvas.drawRect(mLocationX,mLocationY,mLocationX+mWidth,mLocationY+mHeight, mRectPaint);
//
//        canvas.drawCircle(mLocationX+mWidth,mLocationY, OUTER_RADIUS,mCirclePaint);
//        canvas.drawCircle(mLocationX+mWidth,mLocationY, INNER_RADIUS,mCircleInnerPaint);
//
//        canvas.drawCircle(mLocationX+mWidth,mLocationY+mHeight, OUTER_RADIUS,mCirclePaint);
//        canvas.drawCircle(mLocationX+mWidth,mLocationY+mHeight, INNER_RADIUS,mCircleInnerPaint);

        canvas.drawRect(positionX+leftPadding,positionY+topPadding,positionX+leftPadding+mWidth,positionY+topPadding+mHeight,mRectPaint);

        canvas.drawCircle(positionX+leftPadding+mWidth,positionY+topPadding,OUTER_RADIUS,mCirclePaint);
        canvas.drawCircle(positionX+leftPadding+mWidth,positionY+topPadding,INNER_RADIUS,mCircleInnerPaint);

        canvas.drawCircle(positionX+leftPadding+mWidth,positionY+topPadding+mHeight,OUTER_RADIUS,mCirclePaint);
        canvas.drawCircle(positionX+leftPadding+mWidth,positionY+topPadding+mHeight,INNER_RADIUS,mCircleInnerPaint);

    }

    private void drawRect(int positionX, int positionY,Canvas canvas){
//        canvas.drawRect(mLocationX,mLocationY,mLocationX+mWidth,mLocationY+mHeight, mRectPaint);
        canvas.drawRect(positionX+leftPadding,positionY+topPadding,positionX+leftPadding+mWidth,positionY+topPadding+mHeight,mRectPaint);

    }


    public int getLocationX() {
        return mLocationX;
    }

    public void setLocationX(int mLocationX) {
        this.mLocationX = mLocationX;
    }

    public int getLocationY() {
        return mLocationY;
    }

    public void setLocationY(int mLocationY) {
        this.mLocationY = mLocationY;
    }


    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public Paint getRectPaint() {
        return mRectPaint;
    }

    public void setRectPaint(Paint mRectPaint) {
        this.mRectPaint = mRectPaint;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public Paint getCirclePaint() {
        return mCirclePaint;
    }

    public void setCirclePaint(Paint mCirclePaint) {
        this.mCirclePaint = mCirclePaint;
    }

    public Paint getmCircleInnerPaint() {
        return mCircleInnerPaint;
    }

    public void setmCircleInnerPaint(Paint mCircleInnerPaint) {
        this.mCircleInnerPaint = mCircleInnerPaint;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public String getTimeTag() {
        return timeTag;
    }

    public void setTimeTag(String timeTag) {
        this.timeTag = timeTag;
    }
}
