package com.example.pass.util.spans.customSpans;

import android.os.Parcel;
import android.text.style.StyleSpan;

import androidx.annotation.NonNull;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyStyleSpan extends StyleSpan implements CustomSpan {

    public MyStyleSpan(int style) {
        super(style);
    }

    public MyStyleSpan(@NonNull Parcel src) {
        super(src);
    }

    @Override
    public int getType() {
        return CustomTypeEnum.BOLD;
    }
}
