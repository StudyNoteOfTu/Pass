package com.example.pass.util.shade.viewAndModels;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TextShader {
     int top;
     int left;
     int right;
     int bottom;


    public TextShader(int top, int left, int right, int bottom) {
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;

    }


    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public void draw(Canvas canvas,Paint paint,int scrollX,int scrollY){
        canvas.drawRect(left+scrollX,top-scrollY
                ,right+scrollX,bottom-scrollY
                ,paint);
    }
}
