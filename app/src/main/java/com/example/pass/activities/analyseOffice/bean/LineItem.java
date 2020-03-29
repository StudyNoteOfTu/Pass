package com.example.pass.activities.analyseOffice.bean;


import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineItem {
    private final static String TAG = "LineItem";

    private boolean isSelect = false;

    private int titleLevel = 1;//1为最低 2 3 4 逐次上升

    private boolean isIgnored = false;

    private String text = "";

    private String picPath = "";

    /**
     * 如果picPath不为空 返回true
     * @return
     */
    public boolean isPic(){
        return !TextUtils.isEmpty(picPath);
    }

    /**
     * 判断是否为标题
     * @return
     */
    public boolean isTitle(){
        if (titleLevel !=0){
            return true;
        }
        return false;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(int titleLevel) {
        this.titleLevel = titleLevel;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void  setDataWithXml(String xml) {
        //正则提取该<p>的状态信息
        String key = null;
        String value = null;
        String regex = "<pass:p key=\"(.*?)\" val=\"(.*?)\">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(xml);
        //如果有匹配的，只需要最前面一段
        if (matcher.find()) {
            key = matcher.group(1);
            value = matcher.group(2);
            Log.d(TAG, "getKey = " + key);
            Log.d(TAG,"getValue ="+value);
        }


        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            //如果均不为空
            //进行判断
            //如果ignore
            if (key.equalsIgnoreCase("ignore")){
                this.isIgnored = false;
            }
            if (key.equalsIgnoreCase("title")){
                //如果是标题，获取其等级
                this.titleLevel= Integer.parseInt(value);
            }
        }

        //先判断是否包含图片
        String picBegin = "<pass:pic>";
        String picEnd = "</pass:pic>";
        //判断是否包含图片
        if (xml.contains(picBegin)) {
            this.picPath = xml.substring(xml.indexOf(picBegin) + picBegin.length(), xml.indexOf(picEnd));
            Log.d(TAG, "pic path is " + picPath);
            return;
        }
        //找到所有的<pass:t></pass:t>取出其中内容
        StringBuilder sb = new StringBuilder();
        String tagBegin = "<pass:t>";
        String tagEnd = "</pass:t>";
        int tagBeginIndex;
        int tagEndIndex;
        int tagBeginlength = tagBegin.length();
        int tagEndlength = tagEnd.length();
        String subStr = "";
        while (xml.contains(tagBegin)) {
            tagBeginIndex = xml.indexOf(tagBegin);
            tagEndIndex = xml.indexOf(tagEnd);
            subStr = xml.substring(tagBeginIndex + tagBeginlength, tagEndIndex);
            sb.append(subStr);
            xml = xml.substring(tagEndIndex + tagEndlength);
        }
        this.text = trimString(sb.toString());
    }

    public String trimString(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            //全角空格 半角空格
            if (str.charAt(i) != '　' && str.charAt(i) != '\t' && str.charAt(i) != (char) 12288) {
                Log.d(TAG, "char is" + (int) str.charAt(i));
                sb.append(str.substring(i));
                break;
            }
        }
        return sb.toString();
    }

    public boolean isIgnored() {
        return isIgnored;
    }

    public void setIgnored(boolean ignored) {
        if (ignored) refresh();
        isIgnored = ignored;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public void refresh() {
        isSelect = false;
        titleLevel = 1;
    }
}
