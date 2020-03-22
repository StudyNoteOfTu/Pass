package com.example.pass.util.spans.customSpans;

import android.os.Parcel;
import android.text.style.BackgroundColorSpan;

import androidx.annotation.NonNull;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyHighLightColoSpan extends BackgroundColorSpan implements CustomSpan {
    public MyHighLightColoSpan(int color) {
        super(color);
    }

    public MyHighLightColoSpan(@NonNull Parcel src) {
        super(src);
    }

    @Override
    public int getType() {
        return CustomTypeEnum.HIGHLIGHT_COLOR;
    }
}
