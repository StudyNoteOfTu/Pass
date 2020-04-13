package com.example.pass.activities.passOpenedActivity.fragments;

import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pass.R;
import com.example.pass.base.ActionBarFragment;
import com.example.pass.callbacks.LoadObjectCallback;
import com.example.pass.util.FileUtil;
import com.example.pass.util.officeUtils.MyXmlWriter;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;
import com.example.pass.util.shade.ShadeManager;
import com.example.pass.util.shade.util.ShadePaintManager;
import com.example.pass.util.shade.viewAndModels.ShadeRelativeLayout;
import com.example.pass.util.shade.viewAndModels.ShadeView;
import com.example.pass.util.shade.viewAndModels.Shader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextViewFragment extends ActionBarFragment {

    TextView textView;
    ShadeView shadeView;

    Button img_edit_shade;
    Button img_see;
    TextView tv_finish;

    String path;
    SpannableStringBuilder sb;


    ShadeManager shadeManager;

    ShadeRelativeLayout shadeRelativeLayout;


    //初始状态：不可编辑，可视
    boolean isEdit;
    boolean isShow;


    @Override
    protected void initViews(View mContentView) {
        textView = mContentView.findViewById(R.id.textView);
        shadeView = mContentView.findViewById(R.id.shadeView);
        shadeRelativeLayout = mContentView.findViewById(R.id.shadeRelativeLayout);
        tv_finish = mContentView.findViewById(R.id.tv_finish);
        textView.setMovementMethod(new LinkMovementMethod());
        img_edit_shade = mContentView.findViewById(R.id.btn_edit_shade);
        img_edit_shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditState();
            }
        });
        img_see = mContentView.findViewById(R.id.btn_see);
        img_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSeeState();
            }
        });

        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //完成编辑
                isEdit = false;
                isShow = true;
                //保存shade
                saveShaders();
                refreshState();
            }
        });

    }

    //存入方片信息
    private void saveShaders() {
        List<ShaderBean> shaderBeans = new ArrayList<>();
        List<Shader> shaders = shadeView.getAllShaders();
        ShaderBean bean;
        for (Shader shader : shaders) {
            bean = new ShaderBean(shader.getImgUrl(),shader.getTimeTag(),
                    shader.getLeftPadding(),shader.getTopPadding(),
                    shader.getWidth(),shader.getHeight());
            shaderBeans.add(bean);
        }
        //存入方片
        ShaderXmlTool.createXml(shaderBeans,path+File.separator+"shader","shade");
        //存入当前页面文字信息
        String xml = shadeView.getXmlString();
        //存入文件
        //覆盖
        MyXmlWriter.compileLinesToXml(xml,path+"/final","final" , new LoadObjectCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(String result) {
                Log.d("OnFinishTest",result);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void changeSeeState() {
        //设置shadeView不可编辑
        isEdit = false;
        //是否可见可以修改一下
        isShow = !isShow;
        refreshState();
    }

    private void changeEditState() {
        //设置shadeView为可见
        isShow = true;
        //设置是否可编辑
        isEdit = !isEdit;
        refreshState();
    }

    private void refreshState() {
        if (isEdit){
            tv_finish.setVisibility(View.VISIBLE);
            img_edit_shade.setVisibility(View.GONE);
            img_see.setVisibility(View.GONE);
            shadeView.setEditable(true);
            shadeView.getmDrawRectPaint().setAlpha(180);
        }else{
            tv_finish.setVisibility(View.GONE);
            img_edit_shade.setVisibility(View.VISIBLE);
            img_see.setVisibility(View.VISIBLE);
            shadeView.setEditable(false);
            shadeView.getmDrawRectPaint().setAlpha(255);
        }
        //shadeView是否可见
        if (isShow){
            shadeView.setVisibility(View.VISIBLE);
        }else{
            shadeView.setVisibility(View.GONE);
        }

    }
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_pass_text_view;
    }

    @Override
    public void switchTitle(String title) {

    }

    public void setData(String path,SpannableStringBuilder spannableStringBuilder){
        this.path = path;
        this.sb = spannableStringBuilder;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(textView==null){
                    Log.d("2020411LOOP","loop");
                }
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(spannableStringBuilder);
                        shadeRelativeLayout.setShadeView(shadeView);
                        shadeRelativeLayout.setSendTouchView(textView);
                        shadeManager = ShadeManager.newInstance();
                        ShadeManager.currentShadeManager = shadeManager;//设置shademanager为当前shademanager
                        List<ShaderBean> shaderBeans= ShaderXmlTool.analyseXml(path+ File.separator+"shader"+"/shade.shader");
                        List<Shader> shaders = new ArrayList<>();
                        Shader shader;
                        for (ShaderBean shaderBean : shaderBeans) {
                            shader = new Shader(shadeView,0,0-shaderBean.getHeight()-100,shaderBean.getWidth(),shaderBean.getHeight());
                            shader.setLeftPadding(shaderBean.getLeftPadding());
                            shader.setTopPadding(shaderBean.getTopPadding());
                            shader.setImgUrl(shaderBean.getImage_path());
                            shader.setTimeTag(shaderBean.getTime_tag());
                            shaders.add(shader);
                        }

                        shadeManager.setHolder(textView);
                        shadeManager.setOnLocateCallBack(shadeView);
                        shadeView.init(shaders,textView,shadeManager,spannableStringBuilder,ShadePaintManager.getPaint(true));

                        isEdit = false;
                        isShow = true;
                        refreshState();
                    }
                });
            }
        }).start();

    }
}
