package com.example.pass.activities.passOpenedActivity.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.pass.R;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.activities.passOpenedActivity.observer.State;
import com.example.pass.base.NormalFragment;
import com.example.pass.observers.base.Observer;
import com.example.pass.observers.base.Observerable;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.shade.ShadeManager;
import com.example.pass.util.shade.util.ShadePaintManager;
import com.example.pass.util.shade.viewAndModels.ShadeRelativeLayout;
import com.example.pass.util.shade.viewAndModels.ShadeView;
import com.example.pass.util.shade.viewAndModels.Shader;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerItemFragment extends NormalFragment implements Observer<State> {

    public double tag = Math.random();

    TextView tv_title;
    TextView tv_content;

    ShadeView shadeView;
    ShadeRelativeLayout shadeRelativeLayout;

    ShadeManager shadeManager;

    String typeface;


    @Override
    protected void initViews(View mContentView) {
        Log.d("2020418LifeCycle", "initViews");
        tv_title = mContentView.findViewById(R.id.tv_title);
        tv_content = mContentView.findViewById(R.id.tv_content);
        tv_content.setMovementMethod(new ScrollingMovementMethod());
        shadeView = mContentView.findViewById(R.id.shadeView);

        shadeRelativeLayout = mContentView.findViewById(R.id.shadeRelativeLayout);
        shadeRelativeLayout.setShadeView(shadeView);
        shadeRelativeLayout.setSendTouchView(tv_content);


        shadeManager = ShadeManager.newInstance();
        Log.d("CreateShadeView_tag", "fragment tag = " + tag + "createShadeView data" + shadeManager.data);
        shadeManager.setHolder(tv_content);
        shadeManager.setOnLocateCallBack(shadeView);

    }

    @Override
    protected int setLayoutId() {
        Log.d("2020413FragmentLayout", "id = " + R.layout.fragment_pass_viewpager_item);
        return R.layout.fragment_pass_viewpager_item;
    }

    @Override
    protected void refreshState(boolean isEdit, boolean isShow) {
        if (isEdit) {
            shadeView.setEditable(true);
            shadeView.getmDrawRectPaint().setAlpha(180);
        } else {
            shadeView.setEditable(false);
            shadeView.getmDrawRectPaint().setAlpha(255);
        }
        //shadeView是否可见
        if (isShow) {
            shadeView.setVisibility(View.VISIBLE);
        } else {
            shadeView.setVisibility(View.GONE);
        }
    }

    boolean isShow;
    HBean.H4.H3.H2.H1 h1;
    SpannableStringBuilder title;
    SpannableStringBuilder content;
    List<ShaderBean> shaderBeans;

    public void setData(boolean isShow, SpannableStringBuilder title, SpannableStringBuilder content, List<ShaderBean> shaderBeans) {
        this.isShow = isShow;
        this.title = title;
        this.content = content;
        this.shaderBeans = shaderBeans;
    }

    public void setData(boolean isShow, HBean.H4.H3.H2.H1 h1, List<ShaderBean> shaderBeans, Observerable<State> stateObserverable) {
        this.isShow = isShow;
        this.h1 = h1;
        this.title = h1.h1Text;
        this.content = h1.detail;
        this.shaderBeans = shaderBeans;

        //注册监听
        stateObserverable.registerObserver(this);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("2020418LifeCycle", "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        Log.d("2020418ListTest", "onPause， list.size = " + shaderBeans.size());
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("2020418Lifecycle", "onDestroy");
        super.onDestroy();
    }


    @Override
    public void onStart() {
        Log.d("2020418Lifecycle", "onStart");
        super.onStart();
        if (shadeManager != null)
            ShadeManager.setCurrentShadeManager(shadeManager);
    }

    @Override
    public void onResume() {

        Log.d("2020418Lifecycle", "onResume");
        super.onResume();
        Log.d("2020413LifeCycle", "shadeView is null ?" + (shadeView == null));

        if (title == null || content == null) return;

        if (isShow) {
            shadeView.setVisibility(View.VISIBLE);
        } else {
            shadeView.setVisibility(View.GONE);
        }
        tv_title.setText(title);
        tv_content.setText(content);
        //设置字体
        if (!TextUtils.isEmpty(typeface)){
            tv_content.setTypeface(Typeface.createFromFile(typeface));
        }

        List<Shader> shaders = new ArrayList<>();
        Shader shader;
        for (ShaderBean shaderBean : shaderBeans) {
            shader = new Shader(shadeView, 0, 0 - shaderBean.getHeight(), shaderBean.getWidth(), shaderBean.getHeight());
            shader.setLeftPadding(shaderBean.getLeftPadding());
            shader.setTopPadding(shaderBean.getTopPadding());
            shader.setImgUrl(shaderBean.getImage_path());
            shader.setTimeTag(shaderBean.getTime_tag());
            shaders.add(shader);
        }

        Log.d("CreateShadeView_resume", "tag = " + tag + "onResume createShadeView data" + shadeManager.data);
        shadeView.init(shaders, tv_content, shadeManager, content, ShadePaintManager.getPaint(true));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("2020418Lifecycle", "onHiddenChanged " + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d("2020418Lifecycle", "setUserVisibleHint " + isVisibleToUser);
        if (isVisibleToUser) {
            if (shadeManager != null) {
                ShadeManager.setCurrentShadeManager(shadeManager);
                //强制刷新
                tv_content.scrollBy(0,-1);
                tv_content.scrollBy(0,0);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void update(State state) {
        switch (state.getMode()){
            case SHADE:
                isShow = state.isShow();
                //shadeView是否可见
                if (isShow) {
                    shadeView.setVisibility(View.VISIBLE);
                } else {
                    shadeView.setVisibility(View.GONE);
                }
                break;
            case FONT_SIZE:
                if (tv_content != null)tv_content.setTextSize(state.getFontSize());
                break;

            case TYPEFACE:
                typeface = state.getTypeface();
                if (tv_content != null) tv_content.setTypeface(Typeface.createFromFile(state.getTypeface()));
                break;
        }

    }

    public List<Shader> getAllShaderOfThisFragment() {
        return shadeView.getAllShaders();
    }

    public void refreshH1(){
        DataContainedSpannableStringBuilder title = h1.h1Text;
        title.clear();
        title.append(tv_title.getText());
        this.h1.h1Text = title;
        this.h1.detail = new SpannableStringBuilder(tv_content.getText());
    }


}


