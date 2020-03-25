package com.example.pass.util.spans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;


import com.example.pass.util.spans.callbacks.OnImageClickListener;
import com.example.pass.util.spans.impls.LongClickableSpan;

public class ClickableImageSpan extends CenterImageSpan implements LongClickableSpan {

    private static final String TAG = "MyImageSpan";

    private float x;
    private float y;

    private OnImageClickListener mOnImageClickListener;



    public ClickableImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }

    public ClickableImageSpan(Context context, Bitmap bitmap) {
        super(context, bitmap);
    }

    public ClickableImageSpan(Drawable drawable) {
        super(drawable);
    }

    public ClickableImageSpan(Context context, Uri uri) {
        super(context, uri);
    }

    private void setOnImageClickListener(OnImageClickListener onImageClickListener){
        this.mOnImageClickListener = onImageClickListener;
    }

    @Override
    public void onClick(View view) {
        if (mOnImageClickListener != null){
            mOnImageClickListener.onClick(this);
        }
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        super.draw(canvas, text, start, end, x, top, y, bottom, paint);
        this.x = x;
        this.y = top;
    }

    public Drawable clicked(){
        Drawable drawable = getDrawable();
        Log.d(TAG,"clicked, get drawable"+drawable.toString());
        return drawable;
    }

    public boolean clicked(int touchX, int touchY){
//        Log.d(TAG,"clicked, touchX = "+touchX + " ,touch Y = "+touchY);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Rect rect = drawable.getBounds();
            return touchX <= rect.right + x && touchX >= rect.left + x
                    && touchY <= rect.bottom + y && touchY >= rect.top + y;
        }
        return false;
    }

    @Override
    public Drawable getDrawable() {
        Drawable drawable = super.getDrawable();
        return drawable;
    }


}
