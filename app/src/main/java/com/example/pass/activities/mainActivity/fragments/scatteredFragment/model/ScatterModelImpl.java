package com.example.pass.activities.mainActivity.fragments.scatteredFragment.model;

import com.example.pass.activities.mainActivity.fragments.scatteredFragment.model.impls.IScatterModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScatterModelImpl implements IScatterModel {

    private List<File> fileList = new ArrayList<>();

    @Override
    public void loadScatterFiles(OnLoadScatterListener<File> listener) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //开始获取文件

                //通知listener

                //调用sortFiles进行排序
            }
        }).start();


    }

    @Override
    public void sortFiles() {

    }

    //增删改查，只需要删
    public void deleteScatter(int position) {
        if (position < 0 || position > fileList.size()-1)return;
        fileList.remove(position);
    }

    /**
     * 添加到记背本
     */
    public void addScatterToPass(){

    }



}
