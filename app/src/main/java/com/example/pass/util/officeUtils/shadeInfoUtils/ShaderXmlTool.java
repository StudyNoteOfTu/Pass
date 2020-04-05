package com.example.pass.util.officeUtils.shadeInfoUtils;

import android.os.Environment;
import android.util.Xml;

import com.example.pass.util.officeUtils.FileUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShaderXmlTool {
    //    image_path, time_tag, leftPadding, \ topPadding, width,  height
    private static final String ROOT_TAG = "shaders";
    private static final String ITEM_TAG = "pic_shader";
    private static final String IMAGE_PATH = "image_path";
    private static final String TIME_TAG= "time_tag";
    private static final String LEFT_PADDING = "left_padding";
    private static final String TOP_PADDING = "top_padding";
    private static final String WIDTH= "width";
    private static final String HEIGHT= "height";

    /**
     * 解析xml
     */
    public static List<ShaderBean> analyseXml(String xmlPath) {
        List<ShaderBean> shaderBeans = new ArrayList<>();
        File file = new File(xmlPath);
        try {
            FileInputStream fis = new FileInputStream(file);
            //获取xlm的pull解析器
            XmlPullParser xmlPullParser = Xml.newPullParser();

            //置顶是用什么编码去解析xml文件
            xmlPullParser.setInput(fis, "utf-8");

            //开始解析
            //需要判断当前解析到什么标签，因为解析不到的不同标签需要做不同的操作
            //通过pa获取当前标签的事件类型
            int type = xmlPullParser.getEventType();

            ShaderBean bean = null;
//            String image_path, String time_tag, int leftPadding, int topPadding, int width, int height
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (ITEM_TAG.equalsIgnoreCase(xmlPullParser.getName())) {
                            bean = new ShaderBean();
                        } else if (IMAGE_PATH.equalsIgnoreCase(xmlPullParser.getName())) {
                            //获取当前标签的名字
                            String image_path = xmlPullParser.nextText();
                            if (bean != null) bean.setImage_path(image_path);
                        } else if (TIME_TAG.equalsIgnoreCase(xmlPullParser.getName())) {
                            String time_tag = xmlPullParser.nextText();
                            if (bean != null) bean.setTime_tag(time_tag);
                        } else if (LEFT_PADDING.equalsIgnoreCase(xmlPullParser.getName())) {
                            String leftPadding = xmlPullParser.nextText();
                            if (bean != null) bean.setLeftPadding(Integer.parseInt(leftPadding));
                        } else if (TOP_PADDING.equalsIgnoreCase(xmlPullParser.getName())) {
                            String topPadding = xmlPullParser.nextText();
                            if (bean != null) bean.setTopPadding(Integer.parseInt(topPadding));
                        } else if (WIDTH.equalsIgnoreCase(xmlPullParser.getName())) {
                            String width = xmlPullParser.nextText();
                            if (bean != null) bean.setWidth(Integer.parseInt(width));
                        } else if (HEIGHT.equalsIgnoreCase(xmlPullParser.getName())) {
                            String height = xmlPullParser.nextText();
                            if (bean != null) bean.setHeight(Integer.parseInt(height));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (ITEM_TAG.equalsIgnoreCase(xmlPullParser.getName())) {
                            shaderBeans.add(bean);
                        }
                        break;
                }
                //指针移动到下一个节点，并且获取事件类型
                type = xmlPullParser.next();

            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return shaderBeans;

    }

    //生成xml文件的方法
    public static void createXml(List<ShaderBean> shaderBeans,String dir,String name){
        String root_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //得到xml序列化器对象
        XmlSerializer xmlSerializer = Xml.newSerializer();
        //给序列化器设置输出流
        //首先创建文件
        String path = FileUtil.createFile(root_path+ File.separator+dir+File.separator+name+".shader");
        File file = new File(path);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //给序列化器制定好输出流
            xmlSerializer.setOutput(fos,"utf-8");//这个utf-8的作用，是指定xml文件用什么编码生成
            xmlSerializer.startDocument("utf-8", true);//这个utf-8的作用,指定的是头结点中encoding这个属性的值

            xmlSerializer.startTag(null,ROOT_TAG);

            for (ShaderBean shaderBean : shaderBeans) {

                //            String image_path, String time_tag, int leftPadding, int topPadding, int width, int height
                xmlSerializer.startTag(null,ITEM_TAG);

                //image_path
                xmlSerializer.startTag(null,IMAGE_PATH);
                xmlSerializer.text(shaderBean.getImage_path());
                xmlSerializer.endTag(null,IMAGE_PATH);

                //time_tag
                xmlSerializer.startTag(null,TIME_TAG);
                xmlSerializer.text(shaderBean.getTime_tag());
                xmlSerializer.endTag(null,TIME_TAG);

                //left_padding
                xmlSerializer.startTag(null,LEFT_PADDING);
                xmlSerializer.text(String.valueOf(shaderBean.getLeftPadding()));
                xmlSerializer.endTag(null,LEFT_PADDING);

                //top_padding
                xmlSerializer.startTag(null,TOP_PADDING);
                xmlSerializer.text(String.valueOf(shaderBean.getTopPadding()));
                xmlSerializer.endTag(null,TOP_PADDING);

                //width
                xmlSerializer.startTag(null,WIDTH);
                xmlSerializer.text(String.valueOf(shaderBean.getWidth()));
                xmlSerializer.endTag(null,WIDTH);

                //height
                xmlSerializer.startTag(null,HEIGHT);
                xmlSerializer.text(String.valueOf(shaderBean.getHeight()));
                xmlSerializer.endTag(null,HEIGHT);

                xmlSerializer.endTag(null,ITEM_TAG);
            }

            xmlSerializer.endTag(null,ROOT_TAG);

            xmlSerializer.endDocument();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
