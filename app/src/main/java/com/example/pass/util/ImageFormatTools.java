package com.example.pass.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Bitmap与DrawAble与byte[]与InputStream之间的转换工具类
 *
 * @author Administrator
 */
public class ImageFormatTools {
    private static ImageFormatTools tools = new ImageFormatTools();

    public static ImageFormatTools getInstance() {
        if (tools == null) {
            tools = new ImageFormatTools();
            return tools;
        }
        return tools;
    }

    // 将byte[]转换成InputStream
    public InputStream Byte2InputStream(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        return bais;
    }

    // 将InputStream转换成byte[]
    public byte[] InputStream2Bytes(InputStream is) {
        String str = "";
        byte[] readByte = new byte[1024];
        int readCount = -1;
        try {
            while ((readCount = is.read(readByte, 0, 1024)) != -1) {
                str += new String(readByte).trim();
            }
            return str.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将Bitmap转换成InputStream
    public InputStream Bitmap2InputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将Bitmap转换成InputStream
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }

    // 将InputStream转换成Bitmap
    public Bitmap InputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);
    }

    // Drawable转换成InputStream
    public InputStream Drawable2InputStream(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2InputStream(bitmap);
    }

    // InputStream转换成Drawable
    public Drawable InputStream2Drawable(InputStream is) {
        Bitmap bitmap = this.InputStream2Bitmap(is);
        return this.bitmap2Drawable(bitmap);
    }

    // Drawable转换成byte[]
    public byte[] Drawable2Bytes(Drawable d) {
        Bitmap bitmap = this.drawable2Bitmap(d);
        return this.Bitmap2Bytes(bitmap);
    }

    // byte[]转换成Drawable
    public Drawable Bytes2Drawable(byte[] b) {
        Bitmap bitmap = this.Bytes2Bitmap(b);
        return this.bitmap2Drawable(bitmap);
    }

    // Bitmap转换成byte[]
    public byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    // byte[]转换成Bitmap
    public Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    // Drawable转换成Bitmap
    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // Bitmap转换成Drawable
    public Drawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        Drawable d = (Drawable) bd;
        return d;
    }

    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) return null;
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBitmap;
    }

    public static Bitmap scaleBitmapByWidth(Bitmap origin, int newWidth) {
        if (origin == null) return null;
        int height = origin.getHeight();
        int width = origin.getWidth();
        Log.d("CenterImageSpan", "origin width " + origin.getWidth() + " height = " + origin.getHeight());
        //判断宽度是否超出目标宽度
        //如果宽度没有超出目标宽度，直接不用处理把它本身返回回去
        //只缩不放（放会出bug，高度固定，而后才绘制图片，会覆盖到下面的文本）
//        if (width <= newWidth) {
//            Log.d("CenterImageSpan", "return origin");
//
//
//            return origin;
//        }
        //如果宽度超出范围


        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = scaleWidth;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
//        if (!origin.isRecycled()) {
//            origin.recycle();
//        }
        Log.d("CenterImageSpan", "newBitmap width " + newBitmap.getWidth() + " height = " + newBitmap.getHeight());
        return newBitmap;
    }


}
