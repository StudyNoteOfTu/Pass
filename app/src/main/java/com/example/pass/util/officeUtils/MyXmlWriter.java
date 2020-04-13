package com.example.pass.util.officeUtils;

import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pass.callbacks.LoadObjectCallback;
import com.example.pass.util.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MyXmlWriter {

    public static boolean compileLinesToXml(List<String> xmlLines, String dir, String name, @NonNull LoadObjectCallback<String> listener){
        try {
             listener.onStart();
            String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            String path = sdcard_path + File.separator + dir + File.separator + name;
            path = FileUtil.createFile(path);
            File xmlFile = new File(path + ".xml");
            if (xmlFile.exists()) {
                if (!xmlFile.delete()) {
                    listener.onError("delete old file failed");
                    return false;
                }
            }
            if (!xmlFile.createNewFile()) {
                listener.onError("create new file failed");
                return false;
            }
            //文件创建完成
            //创建文件输出流
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            //写入第一行
            outputStream.write(XmlTags.getXmlBegin().getBytes());
            //逐个写入数据
            for (String xmlLine : xmlLines) {
                Log.d("2020411B",xmlLine);
                outputStream.write(xmlLine.getBytes());
            }
            //全部写完以后
            outputStream.write(XmlTags.getXmlEnd().getBytes());
            //关闭输出流
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        String out_path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+dir+File.separator+name+".xml";
        listener.onFinish(out_path);

        return true;
    }


    public static boolean compileLinesToXml(String xmlFileString, String dir, String name, @NonNull LoadObjectCallback<String> listener){
        try {
            listener.onStart();
            String root_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            Log.d("2020411F","root path = "+ root_path+" ,dir = "+dir);
            if (dir.startsWith(root_path)){
                root_path = dir;
            }else{
                root_path = root_path + File.separator+dir;
            }
            String path = root_path +File.separator + name;
            path = FileUtil.createFile(path);
            File xmlFile = new File(path + ".xml");
            if (xmlFile.exists()) {
                if (!xmlFile.delete()) {
                    listener.onError("delete old file failed");
                    return false;
                }
            }
            if (!xmlFile.createNewFile()) {
                listener.onError("create new file failed");
                return false;
            }
            //文件创建完成
            //创建文件输出流
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            //写入文件
            outputStream.write(xmlFileString.getBytes());
            //关闭输出流
            outputStream.close();

            String out_path = path+".xml";
            listener.onFinish(out_path);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return true;
    }
}
