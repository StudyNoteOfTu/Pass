package com.example.pass.activities.mainActivity.fragments.passFoldersFragment.model;

import android.util.Log;

import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.bean.PassFolder;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.model.impls.IPassFolderModel;
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
                    Log.d("2020411F","folder isdirectory "+folder.getName());
                    //进入该分类
                    //首先判断是什么类型，比如local_files
                    if (folder.getName().equalsIgnoreCase(PathConfig.LOCAL_PATH_TAG)) {
                        type = "文件上传";//自己或者用户
                    } else {
                        type = "系统自带";
                    }
                    //开始获取该文件夹下的所有文件
                    File[] local_files = folder.listFiles();
                    if (local_files != null)Log.d("2020411F","size = "+local_files.length);
                    String file_name;
                    Long createTime;
                    if (local_files != null) {
                        List<File> fileToDelete = new ArrayList<>();
                        for (File local_file : local_files) {
                            //判断是否有finish文件夹，如果没有，则删除，如果有，则解析并获取
                            if(!canUseThisFile(local_file)){
                                //如果不可使用，则删除该文件
                                fileToDelete.add(local_file);

                            }else {
                                file_name = local_file.getName();
                                Log.d("2020411F", file_name);
                                createTime = Long.parseLong(file_name.substring(file_name.lastIndexOf("_") + 1));
                                file_name = file_name.substring(0, file_name.lastIndexOf("_"));
                                Log.e(TAG, " create time = " + createTime + " result file_name = " + file_name);
                                PassFolder passFolder = new PassFolder(local_file.getAbsolutePath(), file_name, type, createTime);
                                passFolderList.add(passFolder);
                            }
                        }
                        for (int i = 0; i < fileToDelete.size(); i++) {
                            boolean deleted = FileUtil.deleteFolder(fileToDelete.get(i).getAbsolutePath());
                            Log.d("2020428","deleted ? "+deleted);
                        }
                    }
                }
            }
        }
        listener.onFinish(passFolderList);
    }

    /**
     * 判断文件是否完整，如果不完整，则删除不添加
     * @param file 文件
     * @return 该文件是否可用
     */
    private boolean canUseThisFile(File file) {
        if (file == null) return false;
        File[] files = file.listFiles();
        if (files == null) return false;
        //判断是否有finish文件
        boolean hasFinish = false;
        File[] innerCheckFiles;
        //是否finish文件夹下有文件
        boolean hasFinishDetail = false;
        for (File file1 : files) {
            //判断是否有finish文件夹
            if (file1.getName().endsWith("final")){
                hasFinish = true;
                innerCheckFiles = file1.listFiles();
                hasFinishDetail = innerCheckFiles!=null && innerCheckFiles.length>0;
            }
        }

        return hasFinish && hasFinishDetail;
    }

}
