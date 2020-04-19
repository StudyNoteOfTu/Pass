package com.example.pass.activities.passOpenedActivity.fragments;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.example.pass.R;
import com.example.pass.activities.passOpenedActivity.adapters.ViewPagerItemAdapter;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.ParentInterface;
import com.example.pass.activities.passOpenedActivity.observer.PagerStateObserverable;
import com.example.pass.activities.passOpenedActivity.observer.State;
import com.example.pass.base.ActionBarFragment;
import com.example.pass.util.FileUtil;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;
import com.example.pass.util.shade.viewAndModels.Shader;
import com.example.pass.view.PassViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends ActionBarFragment {


    private PassViewPager mViewPager;


    private Button btn_edit_shade;
    private Button btn_see;
    private TextView tv_finish;

    private ViewPagerItemFragment currentFragment;

    private boolean isEdit = false;
    private boolean isShow = false;


    private String path;
    private int selectedIndex;

    private List<HBean.H4.H3.H2.H1> h1List;

    private List<ViewPagerItemFragment> viewPagerItemFragmentList;

    private PagerStateObserverable pagerStateObserverable = new PagerStateObserverable();

    @Override
    protected void initViews(View mContentView) {
        //初始化控件
        mViewPager = mContentView.findViewById(R.id.viewPager);

        btn_edit_shade = mContentView.findViewById(R.id.btn_edit_shade);
        btn_see = mContentView.findViewById(R.id.btn_see);
        tv_finish = mContentView.findViewById(R.id.tv_finish);

        btn_edit_shade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditState();
            }
        });

        btn_see.setOnClickListener(new View.OnClickListener() {
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
                refresh();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        List<ShaderBean> shaderBeans = ShaderXmlTool.analyseXml(path + File.separator + "shader" + "/shade.shader");
        viewPagerItemFragmentList = new ArrayList<>();
        ViewPagerItemFragment fragment;
        for (int i = 0; i < h1List.size(); i++) {
            fragment = new ViewPagerItemFragment();
//            fragment.setData(isShow, h1List.get(i).h1Text, h1List.get(i).detail, shaderBeans);
            fragment.setData(isShow, h1List.get(i), shaderBeans, pagerStateObserverable);
            viewPagerItemFragmentList.add(fragment);
        }

        mViewPager.setAdapter(new ViewPagerItemAdapter(getChildFragmentManager(), viewPagerItemFragmentList, h1List.size()));

        mViewPager.setOffscreenPageLimit(viewPagerItemFragmentList.size());
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("2020413F", "onScrolling");
                //注意，如果正在编辑，则不允许滑动
            }

            @Override
            public void onPageSelected(int position) {
                //设置currentItem
                currentFragment = viewPagerItemFragmentList.get(position);
                Log.d("2020419ADDR", "currentFragment =" + currentFragment.tag);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("20204013G", "state = " + state);
            }
        };
        mViewPager.setOnPageChangeListener(pageChangeListener);
        mViewPager.setCurrentItem(selectedIndex);
        pageChangeListener.onPageSelected(selectedIndex);
        currentFragment = viewPagerItemFragmentList.get(selectedIndex);
    }

    //保存所有文件和方片
    private void saveShaders() {
        Log.d("2020418SaveShaders", "save shaders path = " + path);
//        ShaderXmlTool.createXml(shaderBeans, FileUtil.getParentPath(FileUtil.getParentPath(path))+File.separator+"shader","shade");
        List<ShaderBean> shaderBeans = new ArrayList<>();
        ShaderBean bean;
        for (ViewPagerItemFragment fragment : viewPagerItemFragmentList) {
            //循环获得所有fragment的所有shader，并且转为ShaderBean存入数据
            for (Shader shader : fragment.getAllShaderOfThisFragment()) {
                bean = new ShaderBean(shader.getImgUrl(), shader.getTimeTag(),
                        shader.getLeftPadding(), shader.getTopPadding(),
                        shader.getWidth(), shader.getHeight());
                shaderBeans.add(bean);
            }
        }
        //获取到所有shader之后，存入数据
        ShaderXmlTool.createXml(shaderBeans, path + File.separator + "shader", "shade");

        //存储所有文字信息
        //拿到所有viewpagerItemFragment
        for (ViewPagerItemFragment fragment : viewPagerItemFragmentList) {
            //更新h1信息
            fragment.refreshH1();
        }
        //将bean重新整理
        //尝试获取顶层，而后获取底层
        if (h1List.size() != 0) {
            HBean.H4.H3.H2.H1 h1 = h1List.get(0);
            ParentInterface rootParent = h1.parent;
            while (rootParent != null) {
                if (rootParent.getParent() != null) {
                    rootParent = (ParentInterface) rootParent.getParent();
                }else{
                    break;
                }
            }
            //获取rootparent，开始遍历
            if (rootParent instanceof HBean){
                for (HBean.H4 h4 : ((HBean) rootParent).h4List) {
                    Log.d("2020419RootParentTest",h4.h4Text.toString());
                    for (HBean.H4.H3 h3 : h4.h3List) {
                        Log.d("2020419RootParentTest",h3.h3Text.toString());
                        for (HBean.H4.H3.H2 h2 : h3.h2List) {
                            Log.d("2020419RootParentTest",h2.h2Text.toString()+"h1 list size = "+h2.h1List.size());
                            for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                                Log.d("2020419RootParentTest",h11.h1Text.toString()+" "+Math.random());
                            }
                        }
                    }
                }
            }else if (rootParent instanceof HBean.H4){
                for (HBean.H4.H3 h3 : ((HBean.H4) rootParent).h3List) {
                    Log.d("2020419RootParentTest",h3.h3Text.toString());
                    for (HBean.H4.H3.H2 h2 : h3.h2List) {
                        Log.d("2020419RootParentTest",h2.h2Text.toString()+"h1 list size = "+h2.h1List.size());
                        for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                            Log.d("2020419RootParentTest",h11.h1Text.toString()+" "+Math.random());
                        }
                    }
                }
            }else if (rootParent instanceof HBean.H4.H3){
                for (HBean.H4.H3.H2 h2 : ((HBean.H4.H3) rootParent).h2List) {
                    Log.d("2020419RootParentTest",h2.h2Text.toString());
                    for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                        Log.d("2020419RootParentTest",h11.h1Text.toString()+" "+Math.random());
                    }
                }
            }else if (rootParent instanceof HBean.H4.H3.H2){
                for (HBean.H4.H3.H2.H1 h11 : ((HBean.H4.H3.H2) rootParent).h1List) {
                    Log.d("2020419RootParentTest",h11.h1Text.toString()+" "+Math.random());
                }
            }
        }


    }

    private void refresh() {
        if (currentFragment != null) currentFragment.refreshState(isEdit, isShow);
        if (isEdit) {
            mViewPager.setCanScroll(false);
            tv_finish.setVisibility(View.VISIBLE);
            btn_edit_shade.setVisibility(View.GONE);
            btn_see.setVisibility(View.GONE);
        } else {
            mViewPager.setCanScroll(true);
            tv_finish.setVisibility(View.GONE);
            btn_edit_shade.setVisibility(View.VISIBLE);
            btn_see.setVisibility(View.VISIBLE);
        }

        //通知所有修改，isEdit只对currentFragment生效，所以state不包含这个
        notifyStateChanged();
    }


    public void initData(String path, List<HBean.H4.H3.H2.H1> h1List, int selectIndex) {
        this.h1List = h1List;
        this.path = path;
        this.selectedIndex = selectIndex;
        Log.d("2020419RootParentTest","h1 list size = "+h1List.size());

    }


    private void notifyStateChanged() {
        State state = new State();
        state.setShow(isShow);
        state.setEdit(isEdit);
        pagerStateObserverable.notifyObserver(state);
    }

    private void changeSeeState() {
        //设置shadeView不可编辑
        isEdit = false;
        //是否可见可以修改一下
        isShow = !isShow;
        refresh();

    }

    private void changeEditState() {
        //设置shadeView为可见
        isShow = true;
        //设置是否可编辑
        isEdit = !isEdit;
        refresh();

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_pass_viewpager;
    }

    @Override
    public void switchTitle(String title) {

    }

}
