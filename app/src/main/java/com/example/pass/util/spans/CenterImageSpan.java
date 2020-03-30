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


    private Context context;

    private Bitmap mBitmap;

    private WeakReference<Drawable> mDrawableRef;

    public CenterImageSpan(Context context, int resourceId) {
        super(context, resourceId, ALIGN_BASELINE);
        this.context = context;
    }

    public CenterImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
        this.context = context;
        mBitmap = ImageFormatTools.scaleBitmapByWidth(bitmap,ScreenConfig.getmImageTargetWidth());
    }

    public CenterImageSpan(Drawable drawable) {
        super(drawable);
    }

    public CenterImageSpan(Context context, Uri uri) {
        super(context, uri);
        this.context = context;
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
        if (mBitmap != null){
            Log.d("CenterImageSpan","x="+x+",y="+y+",top="+top+",bottom="+bottom);
            Log.d("CenterImageSpan","mBitmap is not null");
            Log.d("CenterImageSpan","mBitmap width "+mBitmap.getWidth()+" height = "+mBitmap.getHeight() );

            canvas.save();
            Bitmap scaledBitmap = ImageFormatTools.scaleBitmapByWidth(mBitmap,ScreenConfig.getmImageTargetWidth());
            Log.d("CenterImageSpan","scaledBitmap width "+scaledBitmap.getWidth()+" height = "+scaledBitmap.getHeight() );
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.descent - fmPaint.ascent;
            int centerY = y + fmPaint.descent - fontHeight / 2;
            int transY = centerY - (mBitmap.getHeight()) / 2;
            canvas.translate(x, transY);
            canvas.drawBitmap(scaledBitmap,0,0,new Paint());
            canvas.restore();
            return;
        }
        Drawable drawable = getCachedDrawable();
        Bitmap bitmap = ImageFormatTools.getInstance().drawable2Bitmap(drawable);

        //drawable = ImageFormatTools.getInstance().bitmap2Drawable(bitmap);

//        Bitmap bitmap = ImageFormatTools.getInstance().drawable2Bitmap(drawable);
//        //对bitmap进行缩放（不放只缩）
        Bitmap resultBitmap = ImageFormatTools.scaleBitmapByWidth(bitmap, ScreenConfig.getmImageTargetWidth());
//        drawable = ImageFormatTools.getInstance().bitmap2Drawable(resultBitmap);

        canvas.save();
        Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
        int fontHeight = fmPaint.descent - fmPaint.ascent;
        int centerY = y + fmPaint.descent - fontHeight / 2;
//       int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;
       int transY = centerY - (resultBitmap.getHeight()) / 2;
        canvas.translate(x, transY);
//        drawable.draw(canvas);
        canvas.drawBitmap(resultBitmap,0,0,new Paint());
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;
        if (wr != null) {
            d = wr.get();
        }

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }
}
