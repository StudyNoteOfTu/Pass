package com.example.pass.util.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.WindowManager;

import com.example.pass.configs.ScreenConfig;
import com.example.pass.util.ImageFormatTools;

import java.lang.ref.WeakReference;

public class CenterImageSpan extends ImageSpan {


    private WeakReference<Drawable> mDrawableRef;

    public CenterImageSpan(Context context, int resourceId) {
        super(context, resourceId, ALIGN_BASELINE);

    }

    public CenterImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);

    }

    public CenterImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CenterImageSpan(Context context, Uri uri) {
        super(context, uri);

    }

    @Override public int getSize(Paint paint, CharSequence text, int start, int end,
                                 Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int drHeight = rect.bottom - rect.top;
            int centerY = fmPaint.ascent + fontHeight / 2;

            fontMetricsInt.ascent = centerY - drHeight / 2;
            fontMetricsInt.top = fontMetricsInt.ascent;
            fontMetricsInt.bottom = centerY + drHeight / 2;
            fontMetricsInt.descent = fontMetricsInt.bottom;
        }
        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
                     int bottom, Paint paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
    }
}
