package com.example.pass.base;

import java.lang.ref.WeakReference;

public class BasePresenter<T> {

    protected WeakReference<T> mViewRef;

    /**
     * 绑定View，一般在初始化中调用该方法
     * @param view
     */
    public void attachView(T view){
        mViewRef = new WeakReference<>(view);
    }

    /**
     * 解除绑定View，一般在onDestroy中调用
     */
    public void detachView(){
        if (mViewRef !=null){
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * View是否绑定
     * @return
     */
    public boolean isViewAttached(){
        return mViewRef!=null && mViewRef.get()!=null;
    }


    public T getView(){
        return (T)mViewRef.get();
    }



}
