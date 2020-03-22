package com.example.pass.util.spans.customSpans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.example.pass.util.spans.ClickableImageSpan;
import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class MyImageSpan extends ClickableImageSpan implements CustomSpan {

    public MyImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public MyImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    public MyImageSpan(Drawable drawable) {
        super(drawable);
    }

    public MyImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    @Override
    public int getType() {
        return CustomTypeEnum.IMAGE;
    }
}
