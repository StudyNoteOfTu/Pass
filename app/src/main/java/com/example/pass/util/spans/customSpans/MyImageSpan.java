package com.example.pass.util.spans.customSpans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.example.pass.configs.ScreenConfig;
import com.example.pass.util.ImageFormatTools;
import com.example.pass.util.shade.ShadeManager;
import com.example.pass.util.spans.ClickableImageSpan;
import com.example.pass.util.spans.enumtype.CustomTypeEnum;
import com.example.pass.util.spans.impls.TouchableSpan;

import java.lang.ref.WeakReference;

public class MyImageSpan extends ClickableImageSpan implements CustomSpan, TouchableSpan {

    public float x;
    public float y;//canvas上真实top
    public int bottom;//canvas上真实bottom

    //显示图片的路径标识，应该作为相对父路径下的子路径
    private String imgUrl;

    private WeakReference<Drawable> mDrawableRef;

    //TextView用这个
    public MyImageSpan(Context context, Bitmap bitmap, String imgUrl) {
        this(context, bitmap,imgUrl,ScreenConfig.getmImageTargetWidth());
        this.imgUrl = imgUrl;
    }

    //EditText用这个
    public MyImageSpan(Context context, Bitmap bitmap, String imgUrl,int width) {
        super(context, ImageFormatTools.scaleBitmapByWidth(bitmap, width));
        this.imgUrl = imgUrl;
    }

    @Override
    public int getType() {
        return CustomTypeEnum.IMAGE;
    }


    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end,
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
        Log.d("2020419SpanDraw","draw url = "+imgUrl);
        if (ShadeManager.getInstance() != null) {
            ShadeManager.getInstance().imageAppear(imgUrl);
        }else{
            Log.d("2020413Image","shadeManager is null");
        }
        this.x = x;
        this.y = top;

        Drawable drawable = getCachedDrawable();

        //如果要吧LineExtra去掉，就要利用drawable的真实高度来计算
        this.bottom = top+drawable.getBounds().height();

        canvas.save();
        Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
        int fontHeight = fmPaint.descent - fmPaint.ascent;
        int centerY = y + fmPaint.descent - fontHeight / 2;
        int transY = centerY - (drawable.getBounds().bottom - drawable.getBounds().top) / 2;

        canvas.translate(x, transY);

        drawable.draw(canvas);
        canvas.restore();
    }

    public Drawable getCachedDrawable() {
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


    public boolean clickInside(int touchX, int touchY) {
//        Log.d(TAG,"clickInside, touchX = "+touchX + " ,touch Y = "+touchY);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Rect rect = drawable.getBounds();
            return touchX <= rect.right + x && touchX >= rect.left + x
                    && touchY <= rect.bottom + y && touchY >= rect.top + y;
        }
        return false;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
