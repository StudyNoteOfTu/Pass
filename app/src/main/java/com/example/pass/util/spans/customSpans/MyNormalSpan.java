package com.example.pass.util.spans.customSpans;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyNormalSpan extends CharacterStyle implements CustomSpan {


    public MyNormalSpan(){}

    @Override
    public void updateDrawState(TextPaint tp) {}

    @Override
    public int getType() {
        return CustomTypeEnum.NORMAL;
    }

}
