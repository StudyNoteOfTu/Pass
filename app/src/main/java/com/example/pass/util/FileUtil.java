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

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param filePath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }

    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

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
     * 获得文件夹名
     * @param pathandname
     * @return
     */
    public static String getFolderName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1 ) {
            return pathandname.substring(start + 1);
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

