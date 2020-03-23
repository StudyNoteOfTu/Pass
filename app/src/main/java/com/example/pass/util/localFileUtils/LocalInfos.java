package com.example.pass.util.localFileUtils;


import android.os.Environment;

public class LocalInfos {

    /**
     * 微信文件存储路径
     */
    public static String WeChatFiles = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tencent/MicroMsg/Download";


    /**
     * QQ文件存储路径
     */
    public static String QQFiles = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv";

}
