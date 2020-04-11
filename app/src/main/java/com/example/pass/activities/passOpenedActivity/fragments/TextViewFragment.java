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
import com.example.pass.util.FileUtil;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;
import com.example.pass.util.shade.ShadeManager;
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

    ShadeRelativeLayout shadeRelativeLayout;



    @Override
    protected void initViews(View mContentView) {
        textView = mContentView.findViewById(R.id.textView);
        shadeView = mContentView.findViewById(R.id.shadeView);
        shadeRelativeLayout = mContentView.findViewById(R.id.shadeRelativeLayout);
        tv_finish = mContentView.findViewById(R.id.tv_finish);
        textView.setMovementMethod(new LinkMovementMethod());
        img_edit_shade = mContentView.findViewById(R.id.btn_edit_shade);
        img_edit_shade.setSelected(false);
        img_edit_shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_edit_shade.isSelected()){
                    //选中
                    setState(1);
                    img_edit_shade.setSelected(false);
                }else{
                    //位被选中
                    setState(2);
                    img_edit_shade.setSelected(true);
                }
            }
        });
        img_see = mContentView.findViewById(R.id.btn_see);
        img_see.setSelected(false);
        img_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_see.isSelected()){
                    setState(3);
                    img_see.setSelected(false);
                }else{
                    setState(4);
                    img_see.setSelected(true);
                }
            }
        });

        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setState(5);
            }
        });

    }

    private void setState(int type){
        switch(type){
            case 1:
                //普通,所有置空
                tv_finish.setVisibility(View.GONE);
                img_edit_shade.setVisibility(View.VISIBLE);
                img_see.setVisibility(View.VISIBLE);
                //不显示shadeView，不可编辑shadeView，
                shadeView.setVisibility(View.GONE);
                shadeView.setEditable(false);
                break;
            case 2:
                //开启shadeView
                img_edit_shade.setVisibility(View.GONE);
                img_see.setVisibility(View.GONE);
                tv_finish.setVisibility(View.VISIBLE);
                //显示shadeView，可编辑shadeView
                shadeView.setVisibility(View.VISIBLE);
                shadeView.setEditable(true);
                break;
            case 3:
                tv_finish.setVisibility(View.GONE);
                img_edit_shade.setVisibility(View.VISIBLE);
                img_see.setVisibility(View.VISIBLE);
                //不显示shadeView
                shadeView.setVisibility(View.GONE);
                break;
            case 4:
            case 5:
                tv_finish.setVisibility(View.GONE);
                img_edit_shade.setVisibility(View.VISIBLE);
                img_see.setVisibility(View.VISIBLE);
                //显示shadeView，不可编辑
                shadeView.setVisibility(View.VISIBLE);
                shadeView.setEditable(false);
                break;
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(textView==null){
                    Log.d("2020411LOOP","loop");
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(spannableStringBuilder);
                        shadeRelativeLayout.setShadeView(shadeView);
                        shadeRelativeLayout.setSendTouchView(textView);
                        ShadeManager shadeManager = ShadeManager.getInstance();
                        List<ShaderBean> shaderBeans= ShaderXmlTool.analyseXml(FileUtil.getParentPath(FileUtil.getParentPath(path))+ File.separator+"shader"+"/shade.shader");
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
                        shadeView.init(shaders,textView,shadeManager,spannableStringBuilder);
                    }
                });
            }
        }).start();

    }
}
