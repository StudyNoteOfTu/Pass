package com.example.pass.activities.mainActivity.fragments.passFoldersFragment.model;

import android.util.Log;

import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.bean.PassFolder;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.model.impls.IPassFolderModel;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.view.impls.IPassFolderView;
import com.example.pass.configs.PathConfig;
import com.example.pass.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PassFolderModel implements IPassFolderModel {

    private static final String TAG = "PassFolderModel";

    @Override
    public void loadPassFolder(OnLoadPassFolderListener listener) {
        listener.onStart();
        List<PassFolder> passFolderList = new ArrayList<>();
        //获取数据
        String path = PathConfig.EnvironmentPath + File.separator + PathConfig.ROOT_PATH;
        Log.d(TAG,"path = "+path);
        //遍历获得文件
        File parentFolder = new File(path);
        File[] folders = parentFolder.listFiles();
        String type;
        if (folders != null) {
            for (File folder : folders) {
                if (folder.isDirectory()) {
                    //进入该分类
                    //首先判断是什么类型，比如local_files
                    if (folder.getName().equalsIgnoreCase(PathConfig.LOCAL_PATH_TAG)) {
                        type = "文件上传";
                    } else {
                        type = "系统自带";
                    }

                    //开始获取该文件夹下的所有文件
                    File[] local_files = folder.listFiles();
                    String file_name;
                    Long createTime;
                    if (local_files != null) {
                        for (File local_file : local_files) {
                            file_name = local_file.getName();
                            createTime = Long.parseLong(file_name.substring(file_name.lastIndexOf("_") + 1));
                            file_name = file_name.substring(0, file_name.lastIndexOf("_"));
                            Log.e(TAG, " create time = " + createTime + " result file_name = " + file_name);
                            PassFolder passFolder = new PassFolder(local_file.getAbsolutePath(), file_name, type, createTime);
                            passFolderList.add(passFolder);
                        }
                    }
                }
            }
        }
        listener.onFinish(passFolderList);
    }

}
