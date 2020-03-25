package com.example.pass.activities.chooseFile.model;

import android.os.Environment;
import android.util.Log;

import com.example.pass.activities.chooseFile.model.impls.IChooseFileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseFileModelImpl implements IChooseFileModel {

    private static final String TAG = "ChooseFIleModelImpl";

    /**
     * 本地文件存储路径
     */
    public static String mRootPath = Environment.getExternalStorageDirectory().toString() + File.separator;

    /**
     * 微信文件存储路径
     */
    public static String mWeChatFilesPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/tencent/MicroMsg/Download";


    /**
     * QQ文件存储路径
     */
    public static String mQQFilesPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv";


    private List<File> allFiles;
    private List<File> qqFiles;
    private List<File> wxFiles;

    @Override
    public void loadFiles(LoadType type, OnFilesLoadListener l) {
        switch (type){
            case ALL:
                loadAllFiles(l);
                break;
            case QQ:
                loadQQFiles(l);
                break;
            case WX:
                loadWXFiles(l);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadAllFiles(OnFilesLoadListener l) {
        if (l == null) return;
        //开始搜索
        l.onStart();

        if (allFiles == null){
            loadFile(mRootPath, l, new OnCompleteListener() {
                @Override
                public void onComplete(List<File> fileList) {
                    allFiles = fileList;
                }
            });
        }else{
            l.onComplete(allFiles);
        }
    }

    @Override
    public void loadQQFiles(OnFilesLoadListener l) {
        if (l == null) return;
        l.onStart();

        if (qqFiles == null){
            loadFile(mQQFilesPath, l, new OnCompleteListener() {
                @Override
                public void onComplete(List<File> fileList) {
                    qqFiles = fileList;
                }
            });
        }else{
            l.onComplete(qqFiles);
        }

    }

    @Override
    public void loadWXFiles(OnFilesLoadListener l) {
        if (l == null) return;
        l.onStart();

        if (wxFiles == null){
            loadFile(mWeChatFilesPath, l, new OnCompleteListener() {
                @Override
                public void onComplete(List<File> fileList) {
                    wxFiles = fileList;
                }
            });
        }else{
            l.onComplete(wxFiles);
        }

    }

    private interface OnCompleteListener{
        void onComplete(List<File> fileList);
    }

    private void loadFile(String path,OnFilesLoadListener l,OnCompleteListener innerListener){
        List<File> fileList = new ArrayList<>();
        new Thread(() -> {
            fileList.addAll(searchOfficeFiles(path));
            l.onComplete(fileList);
            innerListener.onComplete(fileList);
        }).start();

    }


    private List<File> searchOfficeFiles(String filePath){
        List<File> fileList = new ArrayList<>();
        int result = doSearch(fileList,filePath);
        Log.d(TAG,fileList.toString());
        return  fileList;
    }

    private int doSearch(List<File> fileList, String path) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File file1 : fileArray) {
                    if (file1.isDirectory()){
                        doSearch(fileList,file1.getAbsolutePath());
                    }else {
                        if (file1.getName().endsWith(".pptx") || file1.getName().endsWith(".docx")) {
                            Log.d(TAG, "文件名称" + file1.getName());
                            Log.d(TAG, "文件的绝对路径" + file1.getAbsolutePath());
                            fileList.add(file1);
                        }

                    }

                }
            }
        }
        return 1;
    }
}
