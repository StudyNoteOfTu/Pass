package com.example.pass.util.spans.customSpans;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

import androidx.annotation.NonNull;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;
import com.example.pass.util.spans.impls.TouchableSpan;

public class MyShadeSpan extends UnderlineSpan implements CustomSpan, TouchableSpan {

    private boolean isShow = false;

    public MyShadeSpan(boolean isShow){
        setShow(isShow);
    }

    public MyShadeSpan(){
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
//        if (!isShow){
//            //背景黑色，字体透明
//            ds.setColor(0x00000000);
//            ds.bgColor = 0xff000000;
//        }else{
//            //字体黑色，背景透明
//            ds.setColor(0xff000000);
//            ds.bgColor = 0x00000000;
//        }
        super.updateDrawState(ds);
    }

    @Override
    public int getType() {
        return CustomTypeEnum.SHADE_MODE;
    }
}
