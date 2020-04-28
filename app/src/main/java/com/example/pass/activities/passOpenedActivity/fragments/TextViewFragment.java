package com.example.pass.activities.passOpenedActivity.fragments;

import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.core.widget.PopupWindowCompat;

import com.example.pass.R;
import com.example.pass.activities.passOpenedActivity.presenter.PassDetailPresenter;
import com.example.pass.activities.passOpenedActivity.view.impls.IPassDetailView;
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
import com.example.pass.view.popWindows.fontstylePopWindows.FontSizePopWindow;
import com.example.pass.view.popWindows.MenuPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextViewFragment extends PassOpenBaseFragment {

    private TextView textView;
    private ShadeView shadeView;

    private Button img_edit_shade;
    private Button img_see;
    private TextView tv_finish;

    private RelativeLayout tabBar;

    private String path;


    private ShadeManager shadeManager;

    private ShadeRelativeLayout shadeRelativeLayout;

    //Presenter引用
    private PassDetailPresenter<IPassDetailView> passDetailPresenter;


    //初始状态：不可编辑，可视
    private boolean isEdit;
    private boolean isShow;

    private int currentFontSize = 19;

    @Override
    protected void initViews(View mContentView) {

        String folderName= FileUtil.getFolderName(path);
        folderName =  folderName.substring(0,folderName.lastIndexOf("_"));
        switchTitle(folderName);

        tabBar = mContentView.findViewById(R.id.tabbar);
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
        //存入当前页面Shader和文字信息
        SpannableStringBuilder spannableStringBuilder = shadeView.getSpannable();
        passDetailPresenter.saveShadersAndText(shaderBeans,spannableStringBuilder);
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


    public void setData(String path, PassDetailPresenter<IPassDetailView> passDetailPresenter){
        this.path = path;
        this.passDetailPresenter = passDetailPresenter;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(textView==null){
                    Log.d("2020411LOOP","loop");
                }
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SpannableStringBuilder sb = passDetailPresenter.getSpannable();
                        textView.setText(sb);
                        shadeRelativeLayout.setShadeView(shadeView);
                        shadeRelativeLayout.setSendTouchView(textView);
                        shadeManager = ShadeManager.newInstance();
                        ShadeManager.setCurrentShadeManager(shadeManager);//设置shademanager为当前shademanager
                        List<ShaderBean> shaderBeans = passDetailPresenter.getShaderBeans(path);
                        List<Shader> shaders = new ArrayList<>();
                        Shader shader;
                        for (ShaderBean shaderBean : shaderBeans) {
                            shader = new Shader(shadeView,0,0-shaderBean.getHeight(),shaderBean.getWidth(),shaderBean.getHeight());
                            shader.setLeftPadding(shaderBean.getLeftPadding());
                            shader.setTopPadding(shaderBean.getTopPadding());
                            shader.setImgUrl(shaderBean.getImage_path());
                            shader.setTimeTag(shaderBean.getTime_tag());
                            shaders.add(shader);
                        }

                        shadeManager.setHolder(textView);
                        shadeManager.setOnLocateCallBack(shadeView);
                        shadeView.init(shaders,textView,shadeManager,sb,ShadePaintManager.getPaint(true));

                        isEdit = false;
                        isShow = true;
                        refreshState();
                    }
                });
            }
        }).start();

    }


    @Override
    public void switchTitle(String title) {
        if (mActionBar != null) {
            mActionBar.setCustomView(R.layout.actionbar_pass_open_viewpager);
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View customView= mActionBar.getCustomView();
            ((TextView)customView.findViewById(R.id.tv_title)).setText(title);

            (customView.findViewById(R.id.img_menu)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showMenuPopWindow(v);
                }
            });

            //返回键
            (customView.findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnBackPressedListener != null){
                        mOnBackPressedListener.onBackPressed(TextViewFragment.this);
                    }
                }
            });

        }
    }


    public void showMenuPopWindow(View childView){
        MenuPopWindow window = new MenuPopWindow(myContext);
        View contentView = window.getContentView();
        ///需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(window.getWidth()),
                makeDropDownMeasureSpec(window.getHeight()));
        int offsetX = 0;
//        int offsetY = -(window.getContentView().getMeasuredHeight()+childView.getHeight())/2;
        int offsetY = 0;
        PopupWindowCompat.showAsDropDown(window,childView,offsetX,offsetY, Gravity.BOTTOM);
        //有问题
//        window.showBackgroundAnimator();
        window.setOnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_size:
                        setFontSize();
                        window.dismiss();
                        break;
                    case R.id.tv_mode:
                        break;
                    case R.id.tv_edit:
                        break;
                    case R.id.tv_copy:
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void setFontSize() {
        showFontSizePopWindow(tabBar);
    }

    private void showFontSizePopWindow(View childView){
        //最小字体16sp，我们用百分比来做，最大是36sp 那么 100份，每份是0.2sp
        FontSizePopWindow window = new FontSizePopWindow(myContext, ((currentFontSize-16)*5), new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentFontSize = 16+(int)(0.2*progress);
                textView.setTextSize(currentFontSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        View contentView = window.getContentView();
        ///需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(window.getWidth()),
                makeDropDownMeasureSpec(window.getHeight()));
        int offsetX = Math.abs((window.getContentView().getMeasuredWidth()-childView.getWidth())/2);
        int offsetY = -(window.getContentView().getMeasuredHeight()+childView.getHeight());
        PopupWindowCompat.showAsDropDown(window,childView,offsetX,offsetY, Gravity.START);
    }

    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }
}
