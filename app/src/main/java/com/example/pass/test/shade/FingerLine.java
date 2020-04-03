package com.example.pass.test.shade;

public class FingerLine {

    private boolean isCalculating = false;

    private int downX;
    private int downY;
    private int nowX;
    private int nowY;

    private boolean isWorking = false;

    public boolean isInDangerousDirection(){
        int delX = getNowX() - getDownX();
        int delY = getNowY() - getDownY();
        if (delY > 0 && delX > 0 && delY <= 2 * delX) {
            return true;
        }
        return false;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }

    public int getDownX() {
        return downX;
    }

    public void setDownX(int downX) {
        this.downX = downX;
    }

    public int getDownY() {
        return downY;
    }

    public void setDownY(int downY) {
        this.downY = downY;
    }

    public int getNowX() {
        return nowX;
    }

    public void setNowX(int nowX) {
        this.nowX = nowX;
    }

    public int getNowY() {
        return nowY;
    }

    public void setNowY(int nowY) {
        this.nowY = nowY;
    }

    public boolean isCalculating() {
        return isCalculating;
    }

    public void setCalculating(boolean calculating) {
        isCalculating = calculating;
    }
}
