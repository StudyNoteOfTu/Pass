package com.example.pass.activities.chooseFileActivity.model;

import android.os.Environment;
import android.util.Log;

import com.example.pass.activities.chooseFileActivity.model.impls.IChooseFileModel;

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
    public static String mWeChatFilesPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg/Download";


    /**
     * QQ文件存储路径
     */
    public static String mQQFilesPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv";

    /**
     * QQ浏览器存储路径
     */
    public static String mQQBrowserPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/QQBroswer";

    /**
     * wps云
     */
    public static String mWPSCloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/cn.wps.moffice_eng/.Cloud";

    /**
     * wps缓存
     */
    public static String mWPSCachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/cn.wps.moffice_eng/.cache/KingsoftOffice/file/download";


    /**
     * UC浏览器
     */
    public static String mUCPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/UCDownloads";

    /**
     * 搜狗浏览器
     */
    public static String mSougouPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/SougouExplorer/download";

    /**
     * 360浏览器
     */
    public static String m360Path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/360Browser/download";

    private List<File> allFiles;
    private List<File> qqFiles;
    private List<File> wxFiles;

    @Override
    public void loadFiles(LoadType loadType, FileType fileType, OnFilesLoadListener<File> l) {
        switch (loadType) {
            case ALL:
                loadAllFiles(fileType, l);
                break;
            case QQ:
                loadQQFiles(fileType, l);
                break;
            case WX:
                loadWXFiles(fileType, l);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadFiles(LoadType type, OnFilesLoadListener<File> l) {
        switch (type) {
            case ALL:
                loadAllFiles(FileType.ALL, l);
                break;
            case QQ:
                loadQQFiles(FileType.ALL, l);
                break;
            case WX:
                loadWXFiles(FileType.ALL, l);
                break;
            default:
                break;
        }
    }


    @Override
    public void loadAllFiles(FileType fileType, OnFilesLoadListener<File> l) {
        if (l == null) return;
        //开始搜索
        l.onStart();

        if (allFiles == null) {
            List<String> paths = new ArrayList<>();
            //添加微信路径
            paths.add(mWeChatFilesPath);
            //添加QQ路径
            paths.add(mQQFilesPath);
            //添加QQ浏览器路径
            paths.add(mQQBrowserPath);
            //添加wps两个路径
            paths.add(mWPSCloadPath);
            paths.add(mWPSCachePath);
            //360浏览器路径
            paths.add(m360Path);
            //搜狗浏览器路径
            paths.add(mSougouPath);
            //UC浏览器路径
            paths.add(mUCPath);

            loadFile(fileType,paths, l, new OnCompleteListener() {
                @Override
                public void onComplete(List<File> fileList) {
                    allFiles = fileList;
                }
            });
        } else {
            List<File> typeFileList = getTypeFileList(fileType.name(),allFiles);
            l.onFinish(typeFileList);
        }
    }

    @Override
    public void loadQQFiles(FileType fileType, OnFilesLoadListener<File> l) {
        if (l == null) return;
        l.onStart();

        if (qqFiles == null) {
            loadFile(fileType,mQQFilesPath, l, new OnCompleteListener() {
                @Override
                public void onComplete(List<File> fileList) {
                    qqFiles = fileList;
                }
            });
        } else {
            List<File> typeFileList = getTypeFileList(fileType.name(),qqFiles);
            l.onFinish(typeFileList);
        }

    }

    @Override
    public void loadWXFiles(FileType fileType, OnFilesLoadListener<File> l) {
        if (l == null) return;
        l.onStart();

        if (wxFiles == null) {
            loadFile(fileType,mWeChatFilesPath, l, new OnCompleteListener() {
                @Override
                public void onComplete(List<File> fileList) {
                    wxFiles = fileList;
                }
            });
        } else {
            List<File> typeFileList = getTypeFileList(fileType.name(),wxFiles);
            l.onFinish(typeFileList);
        }

    }

    private interface OnCompleteListener {
        void onComplete(List<File> fileList);
    }

    private void loadFile(FileType fileType,String path, OnFilesLoadListener<File> l, OnCompleteListener innerListener) {
        List<File> fileList = new ArrayList<>();
        new Thread(() -> {
            fileList.addAll(searchOfficeFiles(path));
            innerListener.onComplete(fileList);
            //筛选type后的
            List<File> typeFileList = getTypeFileList(fileType.name(),fileList);
            l.onFinish(typeFileList);

        }).start();

    }

    private void loadFile(FileType fileType,List<String> paths, OnFilesLoadListener<File> l, OnCompleteListener innerListener) {
        List<File> fileList = new ArrayList<>();
        new Thread(() -> {
            for (String path : paths) {
                fileList.addAll(searchOfficeFiles(path));
            }
            innerListener.onComplete(fileList);
            //筛选type后的
            List<File> typeFileList = getTypeFileList(fileType.name(),fileList);
            l.onFinish(typeFileList);

        }).start();
    }


    private List<File> searchOfficeFiles(String filePath) {
        List<File> fileList = new ArrayList<>();
        int result = doSearch(fileList, filePath);
        Log.d(TAG, fileList.toString());
        return fileList;
    }

    private int doSearch(List<File> fileList, String path) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isDirectory()) {
                File[] fileArray = file.listFiles();
                for (File file1 : fileArray) {
                    if (file1.isDirectory()) {
                        doSearch(fileList, file1.getAbsolutePath());
                    } else {
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

    private List<File> getTypeFileList(String end, List<File> fileList) {
        List<File> typeFileList = new ArrayList<>();
        //如果不做筛选
        if (end.equalsIgnoreCase("all")){
            typeFileList.addAll(fileList);
            return typeFileList;
        }
        //如果做筛选
        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            if (file.getAbsolutePath().endsWith(end.toUpperCase()) ||
                    file.getAbsolutePath().endsWith(end.toLowerCase())) {
                typeFileList.add(file);
            }
        }
        return typeFileList;
    }
}
