package com.example.pass.activities.mainActivity.fragments.passFoldersFragment.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import com.example.pass.R;
import com.example.pass.activities.chooseFileActivity.view.ChooseFileActivity;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.adapter.FolderGridAdapter;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.bean.PassFolder;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.presenter.PassFolderPresenter;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.view.impls.IPassFolderView;
import com.example.pass.base.BaseFragment;

import java.util.List;

public class PassFolderFragment extends BaseFragment<IPassFolderView, PassFolderPresenter<IPassFolderView>> implements IPassFolderView{

    private GridView mGridView;

    private FolderGridAdapter mAdapter;

    @Override
    protected PassFolderPresenter<IPassFolderView> createPresenter() {
        return new PassFolderPresenter<>();
    }

    @Override
    protected void initViews(View mContentView) {
        mGridView = mContentView.findViewById(R.id.gridView);
        mAdapter = new FolderGridAdapter(getContext());
        mGridView.setAdapter(mAdapter);
        initData();
    }



    private void initData(){
        Log.d("2020410","begin init data");
        if (mPresenter != null)mPresenter.loadLocalFolder();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_pass_folder;
    }

    @Override
    public void switchTitle(String title) {
        initData();
        if (mActionBar != null) {
            Log.d("2020410","setTitle = "+title);
            mActionBar.setCustomView(R.layout.actionbar_pass_folder);
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View customView= mActionBar.getCustomView();
            ((TextView)customView.findViewById(R.id.tv_title)).setText(title);
            customView.findViewById(R.id.img_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ChooseFileActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void loadPassFolders(List<PassFolder> passFolderList) {
        mAdapter.setData(passFolderList);
        mAdapter.notifyDataSetChanged();
    }


}
