package com.example.pass.util.spans.customSpans;

import android.text.style.StrikethroughSpan;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyStrikethroughSpan extends StrikethroughSpan implements CustomSpan {
    @Override
    public int getType() {
        return CustomTypeEnum.STRIKE_THROUGH;
    }
}
