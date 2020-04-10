package com.example.pass.activities.mainActivity.fragments.passFoldersFragment.bean;

public class PassFolder {

    //文件路径
    private String path;

    //文件名
    private String title;

    //type
    private String type;

    private Long createTime;

    public PassFolder(String path, String title,String type,Long createTime) {
        this.path = path;
        this.title = title;
        this.type = type;
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
