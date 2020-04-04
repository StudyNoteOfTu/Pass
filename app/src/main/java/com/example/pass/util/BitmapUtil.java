package com.example.pass.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BitmapUtil {

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 判断拍照 图片是否旋转
     *
     * @param degree 图片的角度
     * @param filePath 文件路径
     */
    public static Bitmap rotateBitmap(int degree, String filePath) {
        if (degree > 0) {
            // 针对相片有旋转问题的处理方式
            try {
                //获取缩略图显示到屏幕上
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, opts);
                return rotateBitmapInternal(degree, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 旋转图片
     */
    private static Bitmap rotateBitmapInternal(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    //压缩图片，质量压缩
    /**
     * 压缩图片
     *
     * @param bitmap
     *          被压缩的图片
     * @param sizeLimit
     *          大小限制 单位M
     * @return
     *          压缩后的图片
     */
    public static Bitmap compressBitmap(Bitmap bitmap, long sizeLimit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 80;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        Bitmap newBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);


//        int bytes = bitmap.getByteCount();
//        ByteBuffer buf = ByteBuffer.allocate(bytes);
//        bitmap.copyPixelsToBuffer(buf);
//        byte[] byteArray = buf.array();
//
////        Bitmap newBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
//        Bitmap newBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length,options);
//
//        while()

        return newBitmap;
    }


    /**
     * 获取转为Bitmap格式的本地图片
     * @param url
     * @return
     */
    public static Bitmap getLocalBitmap(String url){
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public  static Bitmap getFixedBitmap(String picPath){

        Bitmap bitmap;

        int rotate = BitmapUtil.readPictureDegree(picPath);

        if (rotate >0){
            //若图片角度大于0 则需要旋转角度
            bitmap = BitmapUtil.rotateBitmap(rotate,picPath);
        }else{
            bitmap = getLocalBitmap(picPath);
        }

        bitmap = ImageFormatTools.scaleBitmapByWidth(bitmap,800);

        if (bitmap != null) bitmap = BitmapUtil.compressBitmap(bitmap,1);

        return bitmap;
    }

}
