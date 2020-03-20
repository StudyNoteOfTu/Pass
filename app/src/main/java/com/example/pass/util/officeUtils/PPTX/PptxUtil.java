package com.example.pass.util.officeUtils.PPTX;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.example.pass.util.officeUtils.FileUtil;
import com.example.pass.util.officeUtils.XmlTags;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class PptxUtil {

    private static final String TAG = "PptxUtil";

    /**
     * 解析pptx文件，提取内容写入自己的xml文件中
     * @param filePath 文件路径
     * @param output_dir 输出父路径
     * @param output_name 输出文件名 不加后缀
     */
    public static void readPptx(String filePath, String output_dir,String output_name) {
        //逐个解析逐个写入整体之中
        try {
            //创建目标xml文件
            String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            String path = sdcard_path + "/" + output_dir + "/" + output_name;
            path = FileUtil.createFile(path);

            File xmlFile = new File(path + ".xml");
            if (xmlFile.exists()) {
                if (!xmlFile.delete()) return;
            }
            if (!xmlFile.createNewFile()) {
                return;
            }

            //文件创建完成

            //获取所有图片资源,存放在Map中
            Map<String,byte[]> images = new HashMap<>();
            //注意images不能为空
            List<PptxSlideEntry> pptxSlideEntries = PptxSlideEntryUtil.getPptSlideEntries(images,filePath);
            //此时图片已经转为byte[]数组
            //接下来进行解析工作
            ZipFile zipFile = new ZipFile(filePath);
            //创建Xml文件输出流
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            //写入第一行
            outputStream.write(XmlTags.getXmlBegin().getBytes());
            //逐个写入数据
            for (PptxSlideEntry pptxSlideEntry:pptxSlideEntries){
                writePptxEntryIntoXml(images,zipFile,outputStream,pptxSlideEntry);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在此之前保证图片先全部转存到本地
     * @param outputStream
     * @param pptxSlideEntry
     */
    private static void writePptxEntryIntoXml(Map<String,byte[]> images,ZipFile zipFile,FileOutputStream outputStream, PptxSlideEntry pptxSlideEntry) throws Exception{
        if (outputStream ==null){
            return;
        }
        //先把其中的图片取出来（只需要图片名）
        ZipEntry rel = pptxSlideEntry.getRels();
        resolveRelEntry(zipFile,rel);

        ZipEntry slide = pptxSlideEntry.getSlide();

    }

    private static Map<String,String> resolveRelEntry(ZipFile zipFile,ZipEntry rel) throws Exception {
        Map<String,String> map = new HashMap<>();
        InputStream inputStream = zipFile.getInputStream(rel);
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream,"utf-8");
        int event_type = xmlPullParser.getEventType();

        while(event_type != XmlPullParser.END_DOCUMENT){
            switch(event_type){
                case XmlPullParser.START_TAG:
                    String tagBegin = xmlPullParser.getName();
                    if (tagBegin.equalsIgnoreCase("Relationship")){
                        String target = xmlPullParser.getAttributeValue("","Target");
                        Log.d(TAG,"target = "+target);
                        String Id =xmlPullParser.getAttributeValue("","Id");
                        Log.d(TAG,"Id = "+target);

                        if (target.contains("media/image")){
                            map.put(Id,target);
                        }
                    }
                    break;
            }
            event_type = xmlPullParser.next();
        }
        return null;
    }




}
