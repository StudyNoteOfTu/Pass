package com.example.pass.base;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends AppCompatActivity {

    //表示层的引用
    public T mPresenter;

    protected  abstract @LayoutRes int setLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());
        initViews();
        mPresenter = createPresenter();
        mPresenter.attachView((V)this);
    }

    protected abstract void initViews();

    protected abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

}
