package com.example.pass.util.spans.customSpans;

import android.text.style.UnderlineSpan;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyUnderlineSpan extends UnderlineSpan implements CustomSpan {
    @Override
    public int getType() {
        return CustomTypeEnum.UNDERLINE;
    }
}
