package com.example.pass.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment {

    //View
    private View mContentView;

    //表示层的引用
    public T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view接口与presenter牵手
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
    }

    //表示层的选择
    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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

    public View getmContentView() {
        return mContentView;
    }

    protected abstract void initViews(View mContentView);

    protected abstract @LayoutRes int setLayoutId();
}
