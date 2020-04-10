package com.example.pass.activities.mainActivity.fragments.passFoldersFragment.presenter;

import android.util.Log;

import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.bean.PassFolder;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.model.PassFolderModel;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.model.impls.IPassFolderModel;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.view.impls.IPassFolderView;
import com.example.pass.base.BasePresenter;

import java.util.List;

public class PassFolderPresenter<V extends IPassFolderView> extends BasePresenter<V> {

    private PassFolderModel mPassFolderModel;

    public PassFolderPresenter(){
        mPassFolderModel = new PassFolderModel();
    }

    public void loadLocalFolder(){
        if (isViewAttached()){
            if(mPassFolderModel != null){
                mPassFolderModel.loadPassFolder(new IPassFolderModel.OnLoadPassFolderListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish(List<PassFolder> passFolderList) {
                        getView().loadPassFolders(passFolderList);
                    }
                });
            }else{

            }
        }else{

        }
    }
}
