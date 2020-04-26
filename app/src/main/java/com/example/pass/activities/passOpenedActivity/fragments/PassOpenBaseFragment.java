package com.example.pass.activities.passOpenedActivity.fragments;

import android.view.View;

import com.example.pass.base.ActionBarFragment;

public abstract class PassOpenBaseFragment extends ActionBarFragment {

    //返回键监听

    protected OnBackPressedListener mOnBackPressedListener;

    public interface OnBackPressedListener{
        void onBackPressed(ActionBarFragment fromFragment);
    }

    public void setOnBackPressedListener(OnBackPressedListener listener){
        mOnBackPressedListener = listener;
    }


}
