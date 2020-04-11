package com.example.pass.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

public abstract class ActionBarFragment extends Fragment {

    protected ActionBar mActionBar;
    private View mContentView;

    public void setActionBar(ActionBar actionBar){
        mActionBar = actionBar;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == mContentView) {
            mContentView = inflater.inflate(setLayoutId(),container,false);
            initViews(mContentView);
        }
        return mContentView;
    }

    protected abstract void initViews(View mContentView);

    protected abstract @LayoutRes
    int setLayoutId();

    public abstract void switchTitle(String title);

}
