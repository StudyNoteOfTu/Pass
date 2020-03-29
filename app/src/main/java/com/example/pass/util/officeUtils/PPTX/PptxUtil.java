package com.example.pass.util.officeUtils.PPTX;

import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.example.pass.util.officeUtils.FileUtil;
import com.example.pass.util.officeUtils.XmlTags;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
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

            Log.d("test",images.size()+""+images);
            //此时图片已经转为byte[]数组
            //接下来进行解析工作
            ZipFile zipFile = new ZipFile(filePath);
            //创建Xml文件输出流
            FileOutputStream outputStream = new FileOutputStream(xmlFile);
            //写入第一行
            outputStream.write(XmlTags.getXmlBegin().getBytes());
            //逐个写入数据
            for (PptxSlideEntry pptxSlideEntry:pptxSlideEntries){
                writePptxEntryIntoXml(output_name,images,zipFile,outputStream,pptxSlideEntry);
            }
            //全都写完以后
            outputStream.write(XmlTags.getXmlEnd().getBytes());
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在此之前保证图片先全部转存到本地
     * @param outputStream
     * @param pptxSlideEntry
     */
    private static void writePptxEntryIntoXml(String out_name,Map<String,byte[]> images,ZipFile zipFile,FileOutputStream outputStream, PptxSlideEntry pptxSlideEntry) throws Exception{
        if (outputStream ==null){
            return;
        }
        //先把其中的图片取出来（只需要图片名）
        ZipEntry rel = pptxSlideEntry.getRels();
        //返回Map key为rId value为资源名 如image1.png
        Map<String,String> map = resolveRelEntry(zipFile,rel);


        ZipEntry slide = pptxSlideEntry.getSlide();

        //开始写入xml文件，形同docx
        InputStream inputStream = zipFile.getInputStream(slide);

        //输入流测试
        byte[] testBytes = new byte[inputStream.available()];
        int totalNum = inputStream.read(testBytes);
        String byteXmlResult=  new String(testBytes);
        String removeStr = "Evaluation Warning : The document was created with Spire.Presentation for Java";
        String removeStr2 = "Evaluation Warning : The document was created with  Spire.Presentation for Java";
        byteXmlResult = byteXmlResult.replaceAll(removeStr,"");
        byteXmlResult = byteXmlResult.replaceAll(removeStr2,"");

        //得到去掉spire水印的xmlInputStream
        InputStream resultByteArrayInputStream = new ByteArrayInputStream(byteXmlResult.getBytes());

        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(resultByteArrayInputStream,"utf-8");

        boolean isColor = false;//文字颜色
        boolean isBackground = false;//文字背景颜色
        boolean isHighlight = false;//是否高亮
        boolean isItalic = false;//斜体
        boolean isUnderline = false;//下划线
        boolean isBold = false;//加粗
        boolean isList = false;//是否列表
        int listLevel = 0;//列表的层序 0最大
        int listId = 0;//一个文档中可能有多个列表， 在ppt中没有用？
        String color = "000000";
        String highlight = "FFFFFF";

        int event_type = xmlPullParser.getEventType();
        while(event_type != XmlPullParser.END_DOCUMENT){
            switch(event_type){
                case XmlPullParser.START_TAG:
                    String tagBegin = xmlPullParser.getName();
                    if (tagBegin.equalsIgnoreCase("p")){
                        //检测到段落
                        outputStream.write(XmlTags.getLineBegin("","").getBytes());
                        //刷新状态
                        isColor = false;
                        color = "000000";
                        highlight = "FFFFFF";
                        isBackground = false;
                        isHighlight = false;
                        isItalic = false;
                        isUnderline = false;
                        isBold = false;
                        isList = false;
                    }

                    //设置文字颜色
                    if (tagBegin.equalsIgnoreCase("solidFill")){
                        //如果文字有颜色
                        isColor = true;
                    }
                    if (isColor && tagBegin.equalsIgnoreCase("srgbClr")){
                        //设置文字颜色
                        color = xmlPullParser.getAttributeValue("","val");
                        getFieldValueByFieldName(color,new Color());
                    }

                    //设置背景高亮颜色
                    if (tagBegin.equalsIgnoreCase("highlight")){
                        //如果有高亮
                        isHighlight = true;
                    }
                    if (isHighlight && tagBegin.equalsIgnoreCase("srgbClr")){
                        highlight = xmlPullParser.getAttributeValue("","val");
                    }

                    if (tagBegin.equalsIgnoreCase("rPr")){
                        if (xmlPullParser.getAttributeValue("","i")!=null){
                            //如果是斜体
                            isItalic = true;
                        }
                        if (xmlPullParser.getAttributeValue("","u")!=null){
                            //如果是下划线
                            isUnderline = true;
                        }
                        if (xmlPullParser.getAttributeValue("","b")!=null){
                            //如果是加粗
                            isBold = true;
                        }
                        if (xmlPullParser.getAttributeValue("","lvl")!=null){
                            //如果是列表，且为更小一级
                            isList = true;
                            listLevel = Integer.parseInt(xmlPullParser.getAttributeValue("","lvl"));
                        }
                    }

                    if (tagBegin.equalsIgnoreCase("t")){
                        outputStream.write(XmlTags.getBlockBegin().getBytes());//写入文字段开始标签
                        if (isBold) {
                            outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_bold()).getBytes());
                            isBold = false;
                        }
                        if (isUnderline) {
                            outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_underline()).getBytes());
                            isUnderline = false;
                        }
                        if (isItalic) {
                            outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_italic()).getBytes());
                            isItalic = false;
                        }
                        if (isColor){
                            outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_color(),color).getBytes());
                            isColor = false;
                        }
                        if (isHighlight){
                            outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_highlight(),highlight).getBytes());
                            isHighlight = false;
                        }
                        if (isList){
                            outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_list(),listLevel+"").getBytes());
                            isList = false;
                        }

                        String text = xmlPullParser.nextText();
                        outputStream.write(XmlTags.getTextBegin().getBytes());
                        outputStream.write(text.getBytes());//写入文本
                        outputStream.write(XmlTags.getTextEnd().getBytes());

                        outputStream.write(XmlTags.getBlockEnd().getBytes());//写入文字段结束标签

                    }

                    //如果是图片
                    if (map!=null && tagBegin.equalsIgnoreCase("blip")){
                        Log.d("test","find a picture");
                        String rId = xmlPullParser.getAttributeValue(0);
                        Log.d("test","rId is "+rId);
//                        String rId = xmlPullParser.getAttributeValue("r","embed");
//                        Log.d("test","rId is "+rId);
                        String picName = map.get(rId);
                        Log.d("test","picName is "+picName);
                        byte[] pictureBytes = images.get(picName);
                        Log.d("test","byte size is "+pictureBytes.length);
                        writePptxPicture(out_name,outputStream,pictureBytes,picName);
                    }

                    break;

                case XmlPullParser.END_TAG:
                    String tagEnd = xmlPullParser.getName();
                    if (tagEnd.equalsIgnoreCase("p")){
                        //段落结束标签
                        outputStream.write(XmlTags.getLineEnd().getBytes());
                    }
                    break;

                default:
                    break;
            }
            event_type = xmlPullParser.next();
        }


        inputStream.close();
        resultByteArrayInputStream.close();

    }

    /**
     * 返回Map key为rId value为资源名 如image1.png
     */
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
                        Log.d(TAG,"Id = "+Id);

                        if (target.contains("media/image")){
                            map.put(Id,target.substring(target.lastIndexOf("/")+1));
                        }
                    }
                    break;
            }
            event_type = xmlPullParser.next();
        }
        return map;
    }


    private static void writePptxPicture(String name, FileOutputStream output, byte[] pictureBytes,String picName) {
        String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pass/Pics/ppt_pics";
        File dirFile = new File(dir_path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String picturePath = FileUtil.createFile("Pass/Pics/ppt_pics/" + name+"/", picName);
        Log.d("test","copy picture , target path is "+picturePath);
        FileUtil.writePicture(picturePath, pictureBytes);
        String imageString = XmlTags.getPicBegin() + picturePath + XmlTags.getPicEnd();
        Log.d("test","pic tag is "+imageString);
        try {
            output.write(XmlTags.getLineBegin("", "").getBytes());
            output.write(imageString.getBytes());
            output.write(XmlTags.getLineEnd().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private  static String getFieldValueByFieldName(String fieldName, Object object) {
        //注意这里修改为判断fieldName是否为数字开头，如果数字开头说明是srgbClr
        if (isInteger(fieldName)){
            return fieldName;
        }
        //如果是文字 比如YELLOW之类的
        try {
            Field field = object.getClass().getDeclaredField(fieldName.toUpperCase());
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            String color = String.format("%06x",(int)field.get(object));
            color = color.substring(color.length()-6);
            return color;
        } catch (Exception e) {
            return "000000";//0x00000000 透明
        }
    }

    //判断是不是rgb数字
    private static  boolean isInteger(String fieldName) {
        Pattern pattern = Pattern.compile("[0-9a-fA-F]*");
        return pattern.matcher(fieldName).matches();
    }


    private static String colorToString(String color) {
        if (!color.startsWith("#")) {
            return "#FF" + color;
        } else {
            return color;
        }
    }

}
