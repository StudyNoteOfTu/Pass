package com.example.pass.activities.passOpenedActivity.fragments;

import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.widget.PopupWindowCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.pass.R;
import com.example.pass.activities.passOpenedActivity.adapters.ViewPagerItemAdapter;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.ParentInterface;
import com.example.pass.activities.passOpenedActivity.observer.PagerStateObserverable;
import com.example.pass.activities.passOpenedActivity.observer.State;
import com.example.pass.base.ActionBarFragment;
import com.example.pass.callbacks.LoadObjectCallback;
import com.example.pass.util.officeUtils.MyXmlWriter;
import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;
import com.example.pass.util.shade.viewAndModels.Shader;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;
import com.example.pass.util.spanUtils.SpanToXmlUtil;
import com.example.pass.view.PassViewPager;
import com.example.pass.view.popWindows.FontSizePopWindow;
import com.example.pass.view.popWindows.MenuPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends ActionBarFragment {


    private PassViewPager mViewPager;

    private RelativeLayout tabBar;

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

    private int currentFontSize = 19;//默认

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_pass_viewpager;
    }

    @Override
    protected void initViews(View mContentView) {
        //初始化控件
        mViewPager = mContentView.findViewById(R.id.viewPager);

        tabBar = mContentView.findViewById(R.id.tabbar);

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
                //设置标题
                switchTitle(h1List.get(position).parent.h2Text.toString());
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

    /**
     * 保存所有文件和方片
     */
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

            SpannableStringBuilder xmlStringBuilder = new SpannableStringBuilder();

            //首先写入文件头
            xmlStringBuilder.append(XmlTags.getXmlBegin());

            //接下来写入各行字段

            DataContainedSpannableStringBuilder sb;
            List<String> xmlList;
            //获取rootparent，开始遍历
            if (rootParent instanceof HBean){
                for (HBean.H4 h4 : ((HBean) rootParent).h4List) {
                    //写入标题字段
                    sb = h4.h4Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    for (HBean.H4.H3 h3 : h4.h3List) {
                        sb = h3.h3Text;
                        createXmlStringLine(xmlStringBuilder, sb);
                        for (HBean.H4.H3.H2 h2 : h3.h2List) {
                            sb = h2.h2Text;
                            createXmlStringLine(xmlStringBuilder, sb);
                            for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                                //写入标题数据
                                sb = h11.h1Text;
                                createXmlStringLine(xmlStringBuilder, sb);
                                //写入内容
                                Log.d("2020419SpanToXmlUtil",""+h11.h1Text);
                                if (h11.detail != null) {
                                    Log.d("2020419SpanToXmlUtil",""+h11.detail.toString());
                                    xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                                    for (String s : xmlList) {
                                        xmlStringBuilder.append(s);
                                    }
                                }
                            }
                        }
                    }
                }
            }else if (rootParent instanceof HBean.H4){
                for (HBean.H4.H3 h3 : ((HBean.H4) rootParent).h3List) {
                    sb = h3.h3Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    for (HBean.H4.H3.H2 h2 : h3.h2List) {
                        sb = h2.h2Text;
                        createXmlStringLine(xmlStringBuilder, sb);
                        for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                            //写入标题数据
                            sb = h11.h1Text;
                            createXmlStringLine(xmlStringBuilder, sb);
                            //写入内容
                            Log.d("2020419SpanToXmlUtil",""+h11.h1Text);
                            if (h11.detail != null) {
                                Log.d("2020419SpanToXmlUtil",""+h11.detail.toString());
                                xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                                for (String s : xmlList) {
                                    xmlStringBuilder.append(s);
                                }
                            }
                        }
                    }
                }
            }else if (rootParent instanceof HBean.H4.H3){
                for (HBean.H4.H3.H2 h2 : ((HBean.H4.H3) rootParent).h2List) {
                    sb = h2.h2Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                        //写入标题数据
                        sb = h11.h1Text;
                        createXmlStringLine(xmlStringBuilder, sb);
                        //写入内容
                        Log.d("2020419SpanToXmlUtil",""+h11.h1Text);
                        if (h11.detail != null) {
                            Log.d("2020419SpanToXmlUtil",""+h11.detail.toString());
                            xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                            for (String s : xmlList) {
                                xmlStringBuilder.append(s);
                            }
                        }
                    }
                }
            }else if (rootParent instanceof HBean.H4.H3.H2){
                for (HBean.H4.H3.H2.H1 h11 : ((HBean.H4.H3.H2) rootParent).h1List) {
                    //写入标题数据
                    sb = h11.h1Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    //写入内容
                    Log.d("2020419SpanToXmlUtil",""+h11.h1Text);
                    if (h11.detail != null) {
                        Log.d("2020419SpanToXmlUtil",""+h11.detail.toString());
                        xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                        for (String s : xmlList) {
                            xmlStringBuilder.append(s);
                        }
                    }
                }
            }
            xmlStringBuilder.append(XmlTags.getXmlEnd());

            //写入文件
            MyXmlWriter.compileLinesToXml(xmlStringBuilder.toString(), path + "/final", "final", new LoadObjectCallback<String>() {
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
    }

    /**
     * 创建单行xmlString
     */
    private void createXmlStringLine(SpannableStringBuilder xmlStringBuilder, DataContainedSpannableStringBuilder sb) {
        xmlStringBuilder.append(XmlTags.getLineBegin(sb.getKey(), sb.getValue()));
        xmlStringBuilder.append(XmlTags.getBlockBegin());
        xmlStringBuilder.append(XmlTags.getTextBegin());
        xmlStringBuilder.append(sb.toString());
        xmlStringBuilder.append(XmlTags.getTextEnd());
        xmlStringBuilder.append(XmlTags.getBlockEnd());
        xmlStringBuilder.append(XmlTags.getLineEnd());
    }


    /**
     * 更新shadeview的可视与可编辑状态
     */
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

    /**
     * 初始化数据
     * @param path
     * @param h1List
     * @param selectIndex
     */
    public void initData(String path, List<HBean.H4.H3.H2.H1> h1List, int selectIndex) {
        this.h1List = h1List;
        this.path = path;
        this.selectedIndex = selectIndex;
        Log.d("2020419RootParentTest","h1 list size = "+h1List.size());

    }


    /**
     * 更新状态变化，通知所有ItemFragment进行更新
     */
    private void notifyStateChanged() {
        State state = new State(State.MODE.SHADE);
        state.setShow(isShow);
        state.setEdit(isEdit);
        pagerStateObserverable.notifyObserver(state);
    }

    /**
     * 按钮效果，更改可视效果
     */
    private void changeSeeState() {
        //设置shadeView不可编辑
        isEdit = false;
        //是否可见可以修改一下
        isShow = !isShow;
        refresh();

    }

    /**
     * 按钮效果，更改可编辑效果
     */
    private void changeEditState() {
        //设置shadeView为可见
        isShow = true;
        //设置是否可编辑
        isEdit = !isEdit;
        refresh();

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
                State state = new State(State.MODE.FONT_SIZE);
                state.setFontSize(currentFontSize);
                pagerStateObserverable.notifyObserver(state);
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
//        int offsetY = -(window.getContentView().getMeasuredHeight()+childView.getHeight())/2;
        int offsetY = -(window.getContentView().getMeasuredHeight()+childView.getHeight());
        PopupWindowCompat.showAsDropDown(window,childView,offsetX,offsetY, Gravity.START);
        //有问题
//        window.showBackgroundAnimator();
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
