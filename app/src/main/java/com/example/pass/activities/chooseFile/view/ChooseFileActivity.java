package com.example.pass.activities.chooseFile.view;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.activities.chooseFile.presenter.ChooseFilePresenter;
import com.example.pass.activities.chooseFile.utils.FileListAdapter;
import com.example.pass.activities.chooseFile.view.impls.IChooseFileView;
import com.example.pass.mvp.base.BaseActivity;

import java.io.File;
import java.util.List;


public class ChooseFileActivity extends BaseActivity<IChooseFileView, ChooseFilePresenter<IChooseFileView>> implements IChooseFileView {

    RecyclerView recyclerView;
    FileListAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_choosefile;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.fetchAllFiles();
    }

    @Override
    protected void initViews() {
        recyclerView = findViewById(R.id.recyclerView);

        adapter = new FileListAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);


    }

    @Override
    public void onLoadingFiles() {
        runOnUiThread(()->{
            Toast.makeText(this, "正在加载", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public void onShowFiles(List<File> fileList) {
        runOnUiThread(()->{
            Toast.makeText(this, "加载完毕", Toast.LENGTH_SHORT).show();
            //交给adapter
            adapter.setData(fileList);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected ChooseFilePresenter<IChooseFileView> createPresenter() {
        return new ChooseFilePresenter<>();
    }
}
