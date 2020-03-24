package com.example.pass.util.localFileUtils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class LocalOfficeFileUtils {

    private static final String TAG = "LocalOfficeFileUtils";
    public static String filePath = Environment.getExternalStorageDirectory().toString() + File.separator;


    public static List<File> searchOfficeFiles(String filePath){
        List<File> fileList = new ArrayList<>();
        int result = doSearch(fileList,filePath);
        Log.d(TAG,fileList.toString());
        return  fileList;
    }

    public static int doSearch(List<File> fileList, String path) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File file1 : fileArray) {
                    if (file1.isDirectory()){
                        doSearch(fileList,file1.getAbsolutePath());
                    }else {
                        if (file1.getName().endsWith(".pptx") || file1.getName().endsWith(".docx")) {
                            Log.d(TAG, "文件名称" + file1.getName());
                            Log.d(TAG, "文件的绝对路径" + file1.getAbsolutePath());
                            fileList.add(file1);
                        }

                    }

                }
            }
        }
        return 1;
    }

}
