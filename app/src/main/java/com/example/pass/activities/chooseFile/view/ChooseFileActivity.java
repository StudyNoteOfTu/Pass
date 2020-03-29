package com.example.pass.activities.chooseFile.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.activities.chooseFile.model.impls.IChooseFileModel;
import com.example.pass.activities.chooseFile.presenter.ChooseFilePresenter;
import com.example.pass.activities.chooseFile.utils.FileListAdapter;
import com.example.pass.activities.chooseFile.utils.StateContainedFileItem;
import com.example.pass.activities.chooseFile.view.impls.IChooseFileView;
import com.example.pass.mvp.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChooseFileActivity extends BaseActivity<IChooseFileView, ChooseFilePresenter<IChooseFileView>> implements IChooseFileView, View.OnClickListener {


    RecyclerView recyclerView;
    FileListAdapter adapter;

    IChooseFileModel.LoadType mLoadType = IChooseFileModel.LoadType.ALL;
    IChooseFileModel.FileType mFileType = IChooseFileModel.FileType.ALL;



    @Override
    protected int setLayoutId() {
        return R.layout.activity_choose_file;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.fetchFiles(mLoadType, mFileType);

    }

    @Override
    protected void initViews() {
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new FileListAdapter(this);
        adapter.setOnClickListener(this);
        adapter.setHasStableIds(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);



        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void onLoadingFiles() {
        runOnUiThread(() -> {
            Toast.makeText(this, "正在加载", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onShowFiles(List<File> fileList) {
        runOnUiThread(() -> {
            Toast.makeText(this, "加载完毕", Toast.LENGTH_SHORT).show();
            //交给adapter
            List<StateContainedFileItem> stateContainedFileItems = new ArrayList<>();
            for (File file : fileList) {
                stateContainedFileItems.add(new StateContainedFileItem(false,file));
            }
            adapter.setData(stateContainedFileItems);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected ChooseFilePresenter<IChooseFileView> createPresenter() {
        return new ChooseFilePresenter<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_from_all:
                changeLoadType(IChooseFileModel.LoadType.ALL);
                break;
            case R.id.btn_from_qq:
                changeLoadType(IChooseFileModel.LoadType.QQ);
                break;
            case R.id.btn_from_wx:
                changeLoadType(IChooseFileModel.LoadType.WX);
                break;
            case R.id.btn_type_all:
                changeFileType(IChooseFileModel.FileType.ALL);
                break;
            case R.id.btn_type_pptx:
                changeFileType(IChooseFileModel.FileType.PPTX);
                break;
            case R.id.btn_type_docx:
                changeFileType(IChooseFileModel.FileType.DOCX);
                break;
            default:
                break;
        }
    }


    private void changeLoadType(IChooseFileModel.LoadType loadType) {
        this.mLoadType = loadType;
        mPresenter.fetchFiles(mLoadType, mFileType);
    }

    private void changeFileType(IChooseFileModel.FileType fileType) {
        this.mFileType = fileType;
        mPresenter.fetchFiles(mLoadType, mFileType);
    }
}
