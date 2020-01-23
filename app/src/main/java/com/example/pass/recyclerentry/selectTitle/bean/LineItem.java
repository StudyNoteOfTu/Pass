package com.example.pass.recyclerentry.selectTitle.bean;

import android.util.Log;

public class LineItem {
    private final static String TAG = "LineItem";

    private boolean isSelect = false;

    private int titleLevel = 0;//0为没有 1 2 3 逐次上升

    private boolean isIgnored = false;

    private String text = "";

    private String picPath = "";

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

    public void setDataWithXml(String xml) {
        //先判断是不是图片
        String picBegin = "<pass:pic>";
        String picEnd ="</pass:pic>";
        if (xml.contains(picBegin)){
            this.picPath = xml.substring(xml.indexOf(picBegin)+picBegin.length(),xml.indexOf(picEnd));
            Log.d(TAG,"pic path is "+picPath);
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

    public String trimString(String str){
        StringBuilder sb = new StringBuilder();
        for (int i = 0 ; i< str.length() ;i++){
            //全角空格 半角空格
            if (str.charAt(i)!='　'&&str.charAt(i)!='\t'&&str.charAt(i)!=(char)12288){
                Log.d(TAG,"char is"+(int)str.charAt(i));
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
        isIgnored = ignored;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
