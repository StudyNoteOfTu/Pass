package com.example.pass.util.spans.customSpans;

import android.os.Parcel;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.NonNull;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyForegroundColorSpan extends ForegroundColorSpan implements CustomSpan {

    public MyForegroundColorSpan(int color) {
        super(color);
    }

    public MyForegroundColorSpan(@NonNull Parcel src) {
        super(src);
    }

    @Override
    public int getType() {
        return CustomTypeEnum.FOREGROUND_COLOR;
    }
}
