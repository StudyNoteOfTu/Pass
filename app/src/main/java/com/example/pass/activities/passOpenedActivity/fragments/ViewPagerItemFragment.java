package com.example.pass.activities.passOpenedActivity.fragments;

import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pass.R;
import com.example.pass.base.NormalFragment;
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

public class ViewPagerItemFragment extends NormalFragment {

    TextView tv_title;
    TextView tv_content;

    ShadeView shadeView;
    ShadeRelativeLayout shadeRelativeLayout;

    ShadeManager shadeManager;


    @Override
    protected void initViews(View mContentView) {

        tv_title = mContentView.findViewById(R.id.tv_title);
        tv_content = mContentView.findViewById(R.id.tv_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod());
        shadeView = mContentView.findViewById(R.id.shadeView);

        shadeRelativeLayout = mContentView.findViewById(R.id.shadeRelativeLayout);
        shadeRelativeLayout.setShadeView(shadeView);
        shadeRelativeLayout.setSendTouchView(tv_content);

        shadeManager = ShadeManager.newInstance();
        shadeManager.setHolder(tv_content);
        shadeManager.setOnLocateCallBack(shadeView);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_pass_viewpager_item;
    }

    @Override
    protected void refreshState(boolean isEdit, boolean isShow) {
        if (isEdit){
            shadeView.setEditable(true);
            shadeView.getmDrawRectPaint().setAlpha(180);
        }else{
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

    public void setData(boolean isShow,SpannableStringBuilder title, SpannableStringBuilder content,List<ShaderBean> shaderBeans){
        //设置内容给textView
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (tv_title == null){
                    Log.d("2020413A","is null");
                }
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isShow){
                            shadeView.setVisibility(View.VISIBLE);
                        }else{
                            shadeView.setVisibility(View.GONE);
                        }
                        tv_title.setText(title);
                        tv_content.setText(content);

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
                        shadeView.init(shaders,tv_content,shadeManager,content,ShadePaintManager.getPaint(true));
                    }
                });
            }
        }).start();
    }

    public TextView getTv_title() {
        return tv_title;
    }

    public void setTv_title(TextView tv_title) {
        this.tv_title = tv_title;
    }

    public TextView getTv_content() {
        return tv_content;
    }

    public void setTv_content(TextView tv_content) {
        this.tv_content = tv_content;
    }


}
