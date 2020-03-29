package com.example.pass.util.officeUtils;


import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 用于提取Docx文档内容，生成自定义xml文件
 */
public class DocxUtil {
    private static int presentPicture;

//    public DocxUtil(String docPath, String output_dir, String output_name) {
//        presentPicture = 0;
//        readDocx(docPath, output_dir, output_name);
//    }

    //解析docx文件，提取内容写入自己的xml文件中
    public static void readDocx(String docPath, String output_dir, String output_name) {
        presentPicture = 0;
        try {
            //创建一个outputXmlFile
            String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
            File dirFile = new File(sdcard_path + "/" + output_dir);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File xmlFile = new File(sdcard_path + "/" + output_dir + "/" + output_name + ".xml");
            if (xmlFile.exists()) {
                if (!xmlFile.delete()) return;
            }
            if (!xmlFile.createNewFile()) {
                return;
            }

            FileOutputStream outputStream = new FileOutputStream(xmlFile);


            ZipFile docxFile = new ZipFile(new File(docPath));
            ZipEntry sharedStringXML = docxFile.getEntry("word/document.xml");
            InputStream inputStream = docxFile.getInputStream(sharedStringXML);
            XmlPullParser xmlParser = Xml.newPullParser();
            xmlParser.setInput(inputStream, "utf-8");

            boolean isTable = false; // 表格
            boolean isSize = false; // 文字大小
            boolean isColor = false; // 文字颜色
            boolean isBackground = false;//文字背景颜色
            boolean isHighlight = false;
            boolean isCenter = false; // 居中对齐
            boolean isRight = false; // 靠右对齐
            boolean isItalic = false; // 斜体
            boolean isUnderline = false; // 下划线
            boolean isBold = false; // 加粗
            boolean isRegion = false; // 在那个区域中
            boolean isList = false;//是否列表
            int listLevel = 0;//列表的层序 0 最大
            int listId = 0;//一个文档中可能有多个列表
            String color = "000000";
            String highlight = "yellow";
            int pic_ndex = 1; // docx中的图片名从image1开始，所以索引从1开始
            int event_type = xmlParser.getEventType();

            outputStream.write(XmlTags.getXmlBegin().getBytes());

            while (event_type != XmlPullParser.END_DOCUMENT) {
                switch (event_type) {
                    case XmlPullParser.START_TAG:
                        String tagBegin = xmlParser.getName();
                        if (tagBegin.equalsIgnoreCase("pic")) {
                            //断头
                            outputStream.write(XmlTags.getLineEnd().getBytes());
                            //起头
                            outputStream.write(XmlTags.getLineBegin("","").getBytes());
                            ZipEntry pic_entry = getWordPicEntry(docxFile, pic_ndex);
                            if (pic_entry != null) {
                                byte[] pictureBytes = FileUtil.getPictureBytes(docxFile, pic_entry);
                                writeDocumentPicture(output_name, outputStream, pictureBytes);
                            }
                            //必须要断尾
                            outputStream.write(XmlTags.getLineEnd().getBytes());
                            //补上下一段起头（就算自带结尾也可以，就是一个段空的p
                            outputStream.write(XmlTags.getLineBegin("","").getBytes());
                            pic_ndex++;
                        }
                        if (tagBegin.equalsIgnoreCase("p")) {//检测到段落
                            outputStream.write(XmlTags.getLineBegin("", "").getBytes());
                        }
                        if (tagBegin.equalsIgnoreCase("b")) {//检测到加粗
                            isBold = true;
                        }
                        if (tagBegin.equalsIgnoreCase("u")) {//检测到下划线
                            isUnderline = true;
                        }
                        if (tagBegin.equalsIgnoreCase("i")) {//检测到斜体
                            isItalic = true;
                        }
                        if (tagBegin.equalsIgnoreCase("color")) {
                            isColor = true;
                            color = xmlParser.getAttributeValue(0);
                        }
                        if (tagBegin.equalsIgnoreCase("highlight")) {
                            isHighlight = true;
                            highlight = xmlParser.getAttributeValue(0);
                        }
                        if (tagBegin.equalsIgnoreCase("ilvl")) {
                            isList = true;
                            listLevel = Integer.parseInt(xmlParser.getAttributeValue(0));
                        }
                        if (tagBegin.equalsIgnoreCase("numId")) {
                            listId = Integer.parseInt(xmlParser.getAttributeValue(0));
                        }

                        //检测到文本
                        if (tagBegin.equalsIgnoreCase("t")) {
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
                            if (isColor) {
                                outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_color(), color).getBytes());
                                Log.d("DocxUtil", color);
                                isColor = false;
                            }
                            if (isHighlight) {
                                outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_highlight(), highlight).getBytes());
                                isHighlight = false;
                            }
                            if (isList) {
                                outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_list(), listLevel + "").getBytes());
                                outputStream.write(XmlTags.getStyleTag(XmlTags.getValue_listId(), listId + "").getBytes());
                                isList = false;
                            }

                            String text = xmlParser.nextText();
                            outputStream.write(XmlTags.getTextBegin().getBytes());
                            outputStream.write(text.getBytes());//写入文本
                            outputStream.write(XmlTags.getTextEnd().getBytes());

                            outputStream.write(XmlTags.getBlockEnd().getBytes());//写入文字段结束标签
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String tagEnd = xmlParser.getName();
                        if (tagEnd.equalsIgnoreCase("p")) {//段落结束标签
                            outputStream.write(XmlTags.getLineEnd().getBytes());
                        }
                        break;

                    default:
                        break;
                }
                event_type = xmlParser.next();
            }

            outputStream.write(XmlTags.getXmlEnd().getBytes());

            outputStream.close();

        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }

    }

    public static void writeDocumentPicture(String name, FileOutputStream output, byte[] pictureBytes) {
        String dir_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pass/Pics/docx_pics";
        File dirFile = new File(dir_path);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        String picturePath = FileUtil.createFile("Pass/Pics/docx_pics/" + name+"/", presentPicture + ".jpg");
        FileUtil.writePicture(picturePath, pictureBytes);
        presentPicture++;
        String imageString = XmlTags.getPicBegin() + picturePath + XmlTags.getPicEnd();
        try {
            output.write(imageString.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 获得word文档中的图片Entry
     * @param docxFile
     * @param pic_index
     * @return
     */
    public static ZipEntry getWordPicEntry(ZipFile docxFile, int pic_index) {
        String entry_jpg = "word/media/image" + pic_index + ".jpeg";
        String entry_png = "word/media/image" + pic_index + ".png";
        String entry_gif = "word/media/image" + pic_index + ".gif";
        String entry_wmf = "word/media/image" + pic_index + ".wmf";
        ZipEntry pic_entry = null;
        pic_entry = docxFile.getEntry(entry_jpg);
        // 以下为读取docx的图片 转化为流数组
        if (pic_entry == null) {
            pic_entry = docxFile.getEntry(entry_png);
        }
        if (pic_entry == null) {
            pic_entry = docxFile.getEntry(entry_gif);
        }
        if (pic_entry == null) {
            pic_entry = docxFile.getEntry(entry_wmf);
        }
        return pic_entry;
    }



}

