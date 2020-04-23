package com.example.pass.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.pass.configs.RequestCodeConfig;

import java.io.File;

public class PhotoUtil {

    /**
     * 调起相机拍照
     * @param context
     * @param photoFile
     * @param requestCode
     */
    public static void takePicture(Activity context,File photoFile,int requestCode){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager())!=null){
            if (photoFile != null){
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            }
        }
        context.startActivityForResult(takePictureIntent,requestCode);
    }

    /**
     * 调起获取相册获取本地图片
     * @param context 上下文
     */
    public static void getGalleryPictureWithResultByIntent(Activity context){
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        context.startActivityForResult(intent, RequestCodeConfig.PICTURE_GALLERY);
    }

    /**
     * 根据onActivityResult的Intent拿到图片路径
     * @param context 上下文
     * @param data Intent
     * @return
     */
    public static String getRealPathWithResultIntent(Context context,Intent data){
        if (data != null) {
            Uri uri = data.getData();
            return getRealPathFromURI(context, uri);
        }else{
            return "";
        }
    }

    /**
     * 根据uri，拿到文件真实路径
     * @param context 上下文
     * @param contentUri uri
     * @return
     */
    public static String getRealPathFromURI(Context context,Uri contentUri){
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            cursor.moveToFirst();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_index);
        }finally {
            if (cursor != null){
                cursor.close();;
            }
        }
    }

}
