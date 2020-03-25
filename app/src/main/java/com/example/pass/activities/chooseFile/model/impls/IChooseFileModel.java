package com.example.pass.activities.chooseFile.model.impls;

import java.io.File;
import java.util.List;

public interface IChooseFileModel {

    enum LoadType{
        ALL,
        QQ,
        WX
    }

    void loadFiles(LoadType type,OnFilesLoadListener l);
    /**
     * 获取本地所有pptx.docx文件
     */
    void loadAllFiles(OnFilesLoadListener l);

    /**
     * 获取QQ所有pptx.docx文件
     */
    void loadQQFiles(OnFilesLoadListener l);

    /**
     * 获取微信所有pptx.docx文件
     * @param l
     */
    void loadWXFiles(OnFilesLoadListener l);


    interface OnFilesLoadListener {
        void onStart();
        void onComplete(List<File> allFiles);
    }


}
