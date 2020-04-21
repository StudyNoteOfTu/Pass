package com.example.pass.util.spans.customSpans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyNormalSpan extends CharacterStyle implements CustomSpan {

    private int mWidth=-1;

    public MyNormalSpan(){

    }

    @Override
    public void updateDrawState(TextPaint tp) {

    }


    @Override
    public int getType() {
        return CustomTypeEnum.NORMAL;
    }


}
