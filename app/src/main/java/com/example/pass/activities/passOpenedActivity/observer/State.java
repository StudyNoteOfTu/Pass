package com.example.pass.activities.passOpenedActivity.observer;

public class State {

    public static enum MODE{
        SHADE,
        FONT_SIZE,
        TYPEFACE
    }

    MODE mode;

    //mode SHADE
    boolean isShow;
    boolean isEdit;

    //mode FONT_SIZE
    int fontSize;

    String typeface;



    public State(MODE mode) {
        this.mode = mode;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public String getTypeface() {
        return typeface;
    }

    public void setTypeface(String typeface) {
        this.typeface = typeface;
    }
}
