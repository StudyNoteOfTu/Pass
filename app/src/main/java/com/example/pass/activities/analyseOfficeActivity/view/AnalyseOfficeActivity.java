package com.example.pass.activities.analyseOfficeActivity.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.activities.TestShadeActivity;
import com.example.pass.activities.analyseOfficeActivity.adapter.LineAdapter;
import com.example.pass.activities.analyseOfficeActivity.model.impls.IOfficeModel;
import com.example.pass.activities.analyseOfficeActivity.presenter.AnalyseOfficePresenter;
import com.example.pass.activities.analyseOfficeActivity.view.impls.ITitleSelectView;
import com.example.pass.activities.analyseOfficeActivity.view.impls.Updatable;
import com.example.pass.configs.PathConfig;
import com.example.pass.base.BaseActivity;
import com.example.pass.util.FileUtil;
import com.example.pass.view.SwipeItemLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AnalyseOfficeActivity extends BaseActivity<ITitleSelectView, AnalyseOfficePresenter<ITitleSelectView>> implements ITitleSelectView, Updatable {
    private  static final String TAG = "AnalyseOfficeActivity";

    /**
     * 显示集合
     */
    RecyclerView recyclerView;
    /**
     * 适配器
     */
    LineAdapter lineAdapter;
    /**
     * 完成按钮
     */
    Button btn_finish;

    /**
     * 数据集合
     */
    List<String> list = new ArrayList<>();

    /**
     * 传来的文件路径
     */
    private String path;


    /**
     * 创建的文件的唯一名字
     */
    String singleName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        //可能是传入xml 也可能传入 .pptx .docx文件
        path = intent.getStringExtra("path");
        //处理这个path
        if (path != null) {
            singleName = FileUtil.createSingleFileName(path);
            mPresenter.readFileAndGetLineList(path, PathConfig.LOCAL_PATH+File.separator+singleName,singleName );
        }else{
            //不允许继续下去
        }
    }

    @Override
    public void beginLoadXml() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AnalyseOfficeActivity.this, "开始加载Xml", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void loadLineList(List<String> lineList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AnalyseOfficeActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                lineAdapter.setLineData(lineList);
                lineAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void changeLineState(String key, String value, int position) {
        mPresenter.updateLine(key,value,position);
    }

    @Override
    public void onFinishAndCompileListToXml() {
        if (path == null) return;
        mPresenter.compileLinesToXml(PathConfig.LOCAL_PATH+File.separator+singleName, singleName+"_final", new IOfficeModel.OnLoadProgressListener<String>() {
            @Override
            public void onStart() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"onFinishAndCompileListToXml, 开始合并");
                    }
                });
            }

            @Override
            public void onFinish(String result) {
                Log.d(TAG,"onFinishAndCompileListToXml, 合并完成");
                //跳转过去
                //直接进入final
                doIntent(result);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG,"onFinishAndCompileListToXml, 合并出错： error = "+error);

            }
        });
    }

    private void doIntent(String result) {
        //开始跳转，判断是否有设置小标题，如果有，如果没有

        //这里不做判断，直接跳入
        Intent intent = new Intent(this, TestShadeActivity.class);
        intent.putExtra("path",result);
        startActivity(intent);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_analyse_office;
    }

    @Override
    protected void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        lineAdapter= new LineAdapter(this,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(lineAdapter);
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));//子项监听器
        recyclerView.setLayoutManager(linearLayoutManager);

        btn_finish = findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFinishAndCompileListToXml();
            }
        });
    }

    @Override
    protected AnalyseOfficePresenter<ITitleSelectView> createPresenter() {
        return new AnalyseOfficePresenter<>();
    }

    @Override
    public void updateLineInfo(String key, String value, int position) {
        changeLineState(key,value,position);
    }
}
