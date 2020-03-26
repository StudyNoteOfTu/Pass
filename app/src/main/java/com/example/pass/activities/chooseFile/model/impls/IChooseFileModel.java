package com.example.pass.activities.chooseFile.model.impls;

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

    void loadFiles(LoadType loadType, FileType fileType, OnFilesLoadListener l);

    void loadFiles(LoadType loadType,OnFilesLoadListener l);
    /**
     * 获取本地所有pptx.docx文件
     */
    void loadAllFiles(FileType fileType,OnFilesLoadListener l);

    /**
     * 获取QQ所有pptx.docx文件
     */
    void loadQQFiles(FileType fileType,OnFilesLoadListener l);

    /**
     * 获取微信所有pptx.docx文件
     * @param l
     */
    void loadWXFiles(FileType fileType,OnFilesLoadListener l);


    interface OnFilesLoadListener {
        void onStart();
        void onComplete(List<File> allFiles);
    }


}
