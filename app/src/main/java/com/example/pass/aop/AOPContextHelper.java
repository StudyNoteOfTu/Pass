package com.example.pass.aop;

import android.app.Activity;

public class AOPContextHelper {

    private Activity mActivity;

    private AOPContextHelper(){}

    private static AOPContextHelper instance;

    public static AOPContextHelper getInstance(){
        if (instance == null){
            synchronized (AOPContextHelper.class){
                if (instance == null){
                    instance = new AOPContextHelper();
                }
            }
        }
        return instance;
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity mActivity) {
        this.mActivity = mActivity;
    }


}
