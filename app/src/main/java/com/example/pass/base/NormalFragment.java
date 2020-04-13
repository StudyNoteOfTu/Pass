package com.example.pass.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class NormalFragment extends Fragment {
    public Context myContext;
    public Activity myActivity;
    protected View mContentView;
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

    @Override
    public void onAttach(@NonNull Context context) {
        this.myContext = context;
        super.onAttach(context);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        this.myActivity = activity;
        super.onAttach(activity);
    }



    protected abstract void refreshState(boolean isEdit, boolean isShow);

}
