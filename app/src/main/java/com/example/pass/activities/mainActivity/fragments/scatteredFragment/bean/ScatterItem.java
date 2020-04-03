package com.example.pass.activities.mainActivity.fragments.scatteredFragment.bean;

import android.text.SpannableStringBuilder;

public class ScatterItem {

    /**
     * 文件路径
     */
    String filePath;

    /**
     * 预览文字
     */
    SpannableStringBuilder previewText;

    /**
     * 时间
     */
    String date;

    /**
     * 标题标签
     */
    String title;

    /**
     * 来源标签
     */
    String from;

    public ScatterItem(String filePath, SpannableStringBuilder previewText, String date) {
        this.filePath = filePath;
        this.previewText = previewText;
        this.date = date;
    }

    public ScatterItem(String filePath, SpannableStringBuilder previewText, String date, String title, String from) {
        this.filePath = filePath;
        this.previewText = previewText;
        this.date = date;
        this.title = title;
        this.from = from;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public SpannableStringBuilder getPreviewText() {
        return previewText;
    }

    public void setPreviewText(SpannableStringBuilder previewText) {
        this.previewText = previewText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
