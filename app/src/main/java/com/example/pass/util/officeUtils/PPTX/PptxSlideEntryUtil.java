package com.example.pass.util.officeUtils.PPTX;

import android.util.Log;

import com.example.pass.util.officeUtils.FileUtil;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 用于生成或者解析PptxSlideEntry的工具
 */
public class PptxSlideEntryUtil {

    private static final String TAG = "PptxSlideEntryUtil";
    /**
     * 遍历压缩文件，是否存入图片，并且生成PptxSlideEntry集合
     */
    public static List<PptxSlideEntry> getPptSlideEntries(Map<String,byte[]> images,String path) throws IOException {


        Map<String,ZipEntry> slides = new HashMap<>();
        Map<String,ZipEntry> rels = new HashMap<>();
        //一个一个PptxSlideEntryEntry载入
        ZipFile zipFile = new ZipFile(path);
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(path)));
        ZipEntry zipEntry;
        //获得所有的slide文件和rels文件
        String zipEntryName;
        while((zipEntry = zin.getNextEntry())!=null){
            zipEntryName = zipEntry.getName();
            //如果是数据资源
            if (zipEntryName.startsWith("ppt/slides/")){
                if (zipEntryName.contains("_rels")){
                    rels.put(getFileCenterName(zipEntryName),zipEntry);
                    continue;
                }
                slides.put(getFileCenterName(zipEntryName),zipEntry);
            }
            //如果是图片资源，解压存入本地，返回路径
            if (images!=null && zipEntryName.startsWith("ppt/media/")){
                if (zipEntryName.endsWith(".jpeg")||zipEntryName.endsWith(".jpg")||zipEntryName.endsWith(".png")){
                    //如果是图片,将图片写入文件
                    Log.d(TAG,"写入图片 get byte[]");
                    String key = FileUtil.getCompleteFileName(zipEntryName);
                    byte[] pic = FileUtil.getPictureBytes(zipFile,zipEntry);
                    images.put(key,pic);
                }
            }
        }

        //遍历过后，整合slide和rels文件
        List<PptxSlideEntry> entryList = new ArrayList<>();
        int count = Math.max(slides.size(),rels.size());
        PptxSlideEntry pptxSlideEntry;
        for (int i = 1 ; i < count+1 ; i ++){
            ZipEntry slide = slides.get("slide"+i);
            ZipEntry rel = rels.get("slide"+i);
            Log.d(TAG,"getPptSlideEntries --- new PptxSlideEntry : slide's name is "+slide.getName()+" , rel's name is"+rel.getName());
            pptxSlideEntry = new PptxSlideEntry(slide,rel);
            entryList.add(pptxSlideEntry);
        }
        return entryList;
    }

    /**
     * 获得无后缀文件名
     * @param absoluteName
     * @return
     */
    private static String getFileCenterName(String absoluteName){
        int beginIndex = absoluteName.lastIndexOf("/");
        int endIndex = absoluteName.indexOf(".");
        String name = absoluteName.substring(beginIndex+1,endIndex);
        Log.d(TAG,"getFileCenterName --- name = "+name);
        return name;
    }


}
