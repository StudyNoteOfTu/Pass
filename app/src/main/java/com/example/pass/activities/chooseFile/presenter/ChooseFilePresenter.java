package com.example.pass.activities.chooseFile.presenter;

import com.example.pass.activities.chooseFile.model.ChooseFileModelImpl;
import com.example.pass.activities.chooseFile.model.impls.IChooseFileModel;
import com.example.pass.activities.chooseFile.view.impls.IChooseFileView;
import com.example.pass.mvp.base.BasePresenter;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ChooseFilePresenter<T extends IChooseFileView> extends BasePresenter<T> {

    private ChooseFileModelImpl chooseFileModel;
    public ChooseFilePresenter(){
        chooseFileModel = new ChooseFileModelImpl();
    }


    public void fetchAllFiles(){
        fetchFiles(IChooseFileModel.LoadType.ALL);
    }

    public void fetchQQFiles(){
        fetchFiles(IChooseFileModel.LoadType.QQ);
    }

    public void fetchWXFiles(){
        fetchFiles(IChooseFileModel.LoadType.WX);
    }

    private void fetchFiles(IChooseFileModel.LoadType loadType){
        if (isViewAttached()){
            if (chooseFileModel != null){
                chooseFileModel.loadFiles(loadType,new IChooseFileModel.OnFilesLoadListener() {
                    @Override
                    public void onStart() {
                        //提示开始加载
                        mViewRef.get().onLoadingFiles();
                    }

                    @Override
                    public void onComplete(List<File> allFiles) {
                        //做数据处理
                        //排序
                        Collections.sort(allFiles,new FileComparator());
                        mViewRef.get().onShowFiles(allFiles);
                    }
                });
            }
        }
    }


    private class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.lastModified() < o2.lastModified()){
                return 1;
            }else if (o1.lastModified() == o2.lastModified()){
                return 0;
            }else{
                return -1;
            }
        }
    }
}
