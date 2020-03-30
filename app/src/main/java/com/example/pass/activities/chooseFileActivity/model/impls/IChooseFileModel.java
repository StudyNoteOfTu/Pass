package com.example.pass.activities.chooseFileActivity.model.impls;

import com.example.pass.callbacks.LoadListCallback;

import java.io.File;
import java.util.List;

public interface IChooseFileModel {

    enum LoadType{
        ALL,
        QQ,
        WX
    }

    enum FileType{
        ALL,
        PPTX,
        DOCX
    }

    void loadFiles(LoadType loadType, FileType fileType, OnFilesLoadListener<File> l);

    void loadFiles(LoadType loadType,OnFilesLoadListener<File> l);
    /**
     * 获取本地所有pptx.docx文件
     */
    void loadAllFiles(FileType fileType,OnFilesLoadListener<File> l);

    /**
     * 获取QQ所有pptx.docx文件
     */
    void loadQQFiles(FileType fileType,OnFilesLoadListener<File> l);

    /**
     * 获取微信所有pptx.docx文件
     * @param l
     */
    void loadWXFiles(FileType fileType,OnFilesLoadListener<File> l);


    interface OnFilesLoadListener<T> extends LoadListCallback<T> {
        void onStart();
        void onFinish(List<T> list);
        void onError(String error);
    }


}
