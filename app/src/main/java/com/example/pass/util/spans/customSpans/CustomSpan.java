package com.example.pass.util.spans.customSpans;

import android.text.ParcelableSpan;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public interface CustomSpan  {

    @CustomTypeEnum
    public int getType();
}
