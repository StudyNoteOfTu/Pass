package com.example.pass.util.officeUtils.shadeInfoUtils;

import androidx.annotation.NonNull;

public class ShaderBean {

    //追随的image
    private String image_path;

    //时间Tag
    private String time_tag;

    //图片中的位置偏移量
    //横向偏移量
    private int leftPadding;
    //竖向偏移量
    private int topPadding;

    //宽度
    private int width;

    //高度
    private int height;

    public ShaderBean() {

    }

    public ShaderBean(String image_path, String time_tag, int leftPadding, int topPadding, int width, int height) {
        this.image_path = image_path;
        this.time_tag = time_tag;
        this.leftPadding = leftPadding;
        this.topPadding = topPadding;
        this.width = width;
        this.height = height;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getTime_tag() {
        return time_tag;
    }

    public void setTime_tag(String time_tag) {
        this.time_tag = time_tag;
    }

    public int getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        this.leftPadding = leftPadding;
    }

    public int getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(int topPadding) {
        this.topPadding = topPadding;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @NonNull
    @Override
    public String toString() {
        return "[image_path:"+image_path+"," +
                "time_tag:" +time_tag+","+
                "left_padding:"+leftPadding+","+
                "top_padding:"+topPadding+","+
                "width:"+width+","+
                "height:"+height+
                "]";
    }
}
