package com.example.pass.activities.passOpenedActivity.fragments;

import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.pass.R;
import com.example.pass.activities.MyApplication;
import com.example.pass.activities.passOpenedActivity.adapters.ViewPagerItemAdapter;
import com.example.pass.base.ActionBarFragment;
import com.example.pass.base.NormalFragment;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;
import com.example.pass.util.shade.ShadeManager;
import com.example.pass.util.shade.util.ShadePaintManager;
import com.example.pass.util.shade.viewAndModels.ShadeRelativeLayout;
import com.example.pass.util.shade.viewAndModels.ShadeView;
import com.example.pass.util.shade.viewAndModels.Shader;
import com.example.pass.view.PassViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends ActionBarFragment {


    PassViewPager mViewPager;


    Button btn_edit_shade;
    Button btn_see;
    TextView tv_finish;

    ViewPagerItemFragment fragmentA;
    ViewPagerItemFragment fragmentB;
    ViewPagerItemFragment fragmentC;
    ViewPagerItemFragment fragmentD;


    ViewPagerItemFragment currentFragment;


    List<ViewPagerItemFragment> list;


    private SpannableStringBuilder test = new SpannableStringBuilder("ekflhajkfh\n\nkldasf\nkl;asjfkl;asjklfa\nfjkljakl;flk;afdl7nfjk\nlasd\njfla7\njflkffdsafdsaf\ndsa\nhfkdslahfi;kad\nshfkl\nadsnjk;fh\nasjk;dfnjkas\nnvj\nksn\nfvklnvkl\nanlfnl.aaflkaj\njklfja\nfjklajlkaf\njfkldaj\nn\nfdasfdasf\nasdfas\njk\nlafjlak;s\n");

    boolean isEdit = false;
    boolean isShow = false;

    List<ShaderBean> shaderBeans;

    @Override
    protected void initViews(View mContentView) {
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
//                saveShaders();
                refresh();
            }
        });
    }

    private void refresh() {
        if (currentFragment != null)currentFragment.refreshState(isEdit,isShow);
        if (isEdit){
            mViewPager.setCanScroll(false);
            tv_finish.setVisibility(View.VISIBLE);
            btn_edit_shade.setVisibility(View.GONE);
            btn_see.setVisibility(View.GONE);
        }else{
            mViewPager.setCanScroll(true);
            tv_finish.setVisibility(View.GONE);
            btn_edit_shade.setVisibility(View.VISIBLE);
            btn_see.setVisibility(View.VISIBLE);
        }

    }

    public void initData(FragmentManager fragmentManager,String path) {

//        List<ShaderBean> shaderBeans= ShaderXmlTool.analyseXml(path+ File.separator+"shader"+"/shade.shader");
        List<ShaderBean> shaderBeans= new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mViewPager == null){}
                myActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        list = new ArrayList<>();

                        fragmentA = new ViewPagerItemFragment();
                        fragmentB = new ViewPagerItemFragment();
                        fragmentC = new ViewPagerItemFragment();
                        fragmentD = new ViewPagerItemFragment();

                        list.add(fragmentA);
                        list.add(fragmentB);
                        list.add(fragmentC);
                        list.add(fragmentD);

                        List<SpannableStringBuilder> spanlist = new ArrayList<>();
                        for (int i = 0; i < 10; i++) {
                            spanlist.add(new SpannableStringBuilder(test));
                        }


                        mViewPager.setAdapter(new ViewPagerItemAdapter(fragmentManager,list,10));
                        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener(){

                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                Log.d("2020413F","onScrolling");
                                //注意，如果正在编辑，则不允许滑动
                            }

                            @Override
                            public void onPageSelected(int position) {
                                Log.d("2020413H","position = "+position);
                                int realPosition = position;
                                //预处理
                                //如果有上一个
                                if (realPosition > 0){
                                    //如果有前一个
                                    //预设前一个
                                    Log.d("20204113O","set previous");
                                    list.get((realPosition-1)%list.size()).setData(isShow,new SpannableStringBuilder("aaa"+realPosition),spanlist.get(realPosition-1),shaderBeans);
                                }
                                //如果有下一个
                                if (realPosition < 10-1){
                                    Log.d("20204113O","set later");
                                    list.get((realPosition+1)%list.size()).setData(isShow,new SpannableStringBuilder("aaa"+realPosition),spanlist.get(realPosition+1),shaderBeans);
                                }

                                //处理position。让position落在[0,fragmentList.size)中，防止数组越界
                                position = position % list.size();
                                ViewPagerItemFragment fragment= list.get(position); //获得此时选中的fragment


                                //前后fragment也要预设
                                currentFragment = fragment;
                                fragment.setData(isShow,new SpannableStringBuilder("aaa"+realPosition),spanlist.get(realPosition) ,shaderBeans); //翻页的时候每个页面需要改变的参数使用这个方法来实现，这个方法在ReadF
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                Log.d("20204013G","state = "+state);
                            }
                        };
                        mViewPager.setOnPageChangeListener(pageChangeListener);



                        mViewPager.setCurrentItem(0);
                        pageChangeListener.onPageSelected(0);
                    }
                });
            }
        }).start();

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
