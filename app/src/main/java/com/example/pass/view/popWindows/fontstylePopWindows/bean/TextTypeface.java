package com.example.pass.view.popWindows.fontstylePopWindows.bean;

import android.graphics.Typeface;

public class TextTypeface {

    /**
     * 本地ttf文件路径
     */
    private String ttf_path;

    /**
     * 字体样式文字解释
     */
    private String text;


    public TextTypeface(String ttf_path, String text) {
        this.ttf_path = ttf_path;
        this.text = text;
    }

    public String getTtf_path() {
        return ttf_path;
    }

    public void setTtf_path(String ttf_path) {
        this.ttf_path = ttf_path;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}