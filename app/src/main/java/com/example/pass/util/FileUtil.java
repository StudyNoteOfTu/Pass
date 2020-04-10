package com.example.pass.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {

    private final static String TAG = "FileUtil";

    /*
     * 创建文件
     */
    public static String createFile(String url){
        File target_file = new File(url);
        if (!target_file.getParentFile().exists()){
            target_file.getParentFile().mkdirs();
        }
        return url;
    }

    /**
     * 获取文件名，不带后缀
     * @param pathandname 文件路径
     * @return
     */
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return "";
        }
    }

    /**
     * 获取带随机数的文件名，不带后缀
     * @param pathandname 文件路径
     * @return
     */
    public static String createSingleFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end)+"_"+System.currentTimeMillis();
        } else {
            return "";
        }
    }

    /**
     * 获取文件名，带后缀
     * @param pathandname 文件路径
     * @return
     */
    public static String getCompleteFileName(String pathandname){
        int start = pathandname.lastIndexOf("/");
        if (start != -1){
            return pathandname.substring(start+1);
        }
        else{
            return "";
        }
    }

    /**
     * 创建文件
     * @param dir_name
     * @param file_name
     * @return
     */
    public static String createFile(String dir_name, String file_name) {
        String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dir_path = String.format("%s/%s", sdcard_path, dir_name);
        String file_path = String.format("%s/%s", dir_path, file_name);
//        String dir_path = dir_name;
//        String file_path = file_name;
        try {
            File dirFile = new File(dir_path);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            File myFile = new File(file_path);
            myFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_path;
    }


    /**
     * 从压缩文件中读取图片
     * @param zipFile  压缩文件
     * @param pic_entry 对应图片压缩Entry
     * @return
     */
    public static byte[] getPictureBytes(ZipFile zipFile, ZipEntry pic_entry) {
        byte[] pictureBytes = null;
        try {
            InputStream pictIS = zipFile.getInputStream(pic_entry);
            ByteArrayOutputStream pOut = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            int len = 0;
            while ((len = pictIS.read(b)) != -1) {
                pOut.write(b, 0, len);
            }
            pictIS.close();
            pOut.close();
            pictureBytes = pOut.toByteArray();
            System.out.println("pictureBytes.length=" + pictureBytes.length);
            if (pictIS != null) {
                pictIS.close();
            }
            if (pOut != null) {
                pOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictureBytes;

    }

    /**
     * 将图片写入文件
     * @param pic_path 目标文件路径
     * @param pictureBytes 图片文件
     */
    public static void writePicture(String pic_path, byte[] pictureBytes) {
        File myPicture = new File(pic_path);
        try {
            FileOutputStream outputPicture = new FileOutputStream(myPicture);
            outputPicture.write(pictureBytes);
            outputPicture.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String getParentPath(String path){
        int lastSeperator = path.lastIndexOf("/");
        return path.substring(0,lastSeperator);
    }

}

