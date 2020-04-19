package com.example.pass.activities.passOpenedActivity.observer;

public class State {
    boolean isShow;
    boolean isEdit;

    public State(boolean isShow, boolean isEdit) {
        this.isShow = isShow;
        this.isEdit = isEdit;
    }

    public State() {
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
}
