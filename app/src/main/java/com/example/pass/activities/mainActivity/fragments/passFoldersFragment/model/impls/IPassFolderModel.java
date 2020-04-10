package com.example.pass.activities.mainActivity.fragments.passFoldersFragment.model.impls;

import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.bean.PassFolder;

import java.util.List;

public interface IPassFolderModel {

    //获取文件
    void loadPassFolder(OnLoadPassFolderListener listener);

    interface OnLoadPassFolderListener{
        void onStart();
        void onFinish(List<PassFolder> passFolderList);
    }
}
