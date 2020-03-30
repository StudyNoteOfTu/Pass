package com.example.pass.activities.mainActivity.fragments.scatteredFragment.model.impls;

import com.example.pass.callbacks.LoadListCallback;

import java.io.File;
import java.util.List;

public interface IScatterModel {

    //获取本地零散记文件(只需要xml）
    void loadScatterFiles(OnLoadScatterListener<File> listener);

    //对获得到的Files进行排序，如置顶等
    void sortFiles();



    public interface OnLoadScatterListener<T> extends LoadListCallback<T> {
        void onStart();
        void onFinish(List<T> list);
        void onError(String error);
    }

}
