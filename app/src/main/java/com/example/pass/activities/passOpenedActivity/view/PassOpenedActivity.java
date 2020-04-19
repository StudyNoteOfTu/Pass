package com.example.pass.activities.passOpenedActivity.view;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.pass.R;
import com.example.pass.activities.passOpenedActivity.bean.TopNum1.ItemBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.ItemH1;
import com.example.pass.activities.passOpenedActivity.fragments.FolderListFragment;
import com.example.pass.activities.passOpenedActivity.fragments.ScatterLikeListFragment;
import com.example.pass.activities.passOpenedActivity.fragments.TextViewFragment;
import com.example.pass.activities.passOpenedActivity.fragments.ViewPagerFragment;
import com.example.pass.activities.passOpenedActivity.fragments.ViewPagerItemFragment;
import com.example.pass.activities.passOpenedActivity.presenter.PassDetailPresenter;
import com.example.pass.activities.passOpenedActivity.view.impls.IPassDetailView;
import com.example.pass.base.ActionBarFragment;
import com.example.pass.base.BaseActivity;

import java.util.List;

public class PassOpenedActivity extends BaseActivity<IPassDetailView, PassDetailPresenter<IPassDetailView>> implements IPassDetailView {


    private static final String TAG = "PassOpenedActivity";
    //topOver1 、 top1
    private FolderListFragment mFolderListFragment;//type = 0
    private ScatterLikeListFragment mScatterLikeListFragment;//type = 1
    private ViewPagerFragment mViewPagerFragment;//type = 2

    //top = 0
    private TextViewFragment mTextViewFragment;//type = 3

    private ActionBarFragment mFragment;

    //路径
    String path;

    //所有需要展示的H1
    List<HBean.H4.H3.H2.H1> h1List;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pass_opened;
    }

    @Override
    protected void initViews() {
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        initFragments();

        mPresenter.getDetail(path);


    }

    private void initFragments() {
        mFolderListFragment = new FolderListFragment();
        mFolderListFragment.setActionBar(getSupportActionBar());

        mScatterLikeListFragment = new ScatterLikeListFragment();
        mScatterLikeListFragment.setActionBar(getSupportActionBar());

        mViewPagerFragment = new ViewPagerFragment();
        mViewPagerFragment.setActionBar(getSupportActionBar());

        mTextViewFragment = new TextViewFragment();
        mTextViewFragment.setActionBar(getSupportActionBar());
    }


    @Override
    protected PassDetailPresenter<IPassDetailView> createPresenter() {
        return new PassDetailPresenter<>();
    }


    @Override
    public void beginLoadFile() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PassOpenedActivity.this, "正在加载中", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void getTop0(int top, SpannableStringBuilder spannableStringBuilder) {
        Log.d(TAG, "getTop0");
        mFragment = mTextViewFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.container, mFragment).commit();
        mTextViewFragment.setData(path, spannableStringBuilder);
    }

    @Override
    public void getTop1(int top, List<ItemBean> object) {
        Log.d(TAG, "getTop1");

    }

    @Override
    public void getTop2(int top, HBean.H4.H3 object) {
        h1List = mPresenter.transformBeanToList(object);
        Log.d(TAG, "getTop2");
        mFragment = mFolderListFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.container, mFragment).commit();
        mFolderListFragment.setData(top, object, new FolderListFragment.ItemClickCallback() {
            @Override
            public void onClick(ItemH1 item) {
                clickItem(item);
            }
        });
    }

    @Override
    public void getTop3(int top, HBean.H4 object) {
        h1List = mPresenter.transformBeanToList(object);
        Log.d(TAG, "getTop3");
        mFragment = mFolderListFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.container, mFragment).commit();
        mFolderListFragment.setData(top, object, new FolderListFragment.ItemClickCallback() {
            @Override
            public void onClick(ItemH1 item) {
                clickItem(item);
            }
        });
    }

    @Override
    public void getTop4(int top, HBean object) {
        h1List = mPresenter.transformBeanToList(object);
        Log.d(TAG, "getTop4");
        mFragment = mFolderListFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.container, mFragment).commit();
        mFolderListFragment.setData(top, object, new FolderListFragment.ItemClickCallback() {
            @Override
            public void onClick(ItemH1 item) {
                clickItem(item);
            }
        });
    }

    private void clickItem(ItemH1 item) {
        //获取click的item在arraylist中是什么
        if (h1List != null) {
            for (int i = 0; i < h1List.size(); i++) {
                //判断item在哪一个
                Log.d("2020413DATA2", "index = " + i);
                if (item.getData() == h1List.get(i)) {
                    Log.d("2020413DATA", "same！！！！ ,index = " + i);
                    //开始跳转
                    jumpToViewPagerFragment(i);
                    break;
                }
            }
        }
    }

    private void jumpToViewPagerFragment(int selectIndex) {
        if (mFragment != null) {
            mViewPagerFragment = new ViewPagerFragment();
            mViewPagerFragment.initData(path, h1List, selectIndex);
            mViewPagerFragment.setActionBar(getSupportActionBar());
            getSupportFragmentManager().beginTransaction().hide(mFragment).add(R.id.container, mViewPagerFragment).commit();
            mFragment = mViewPagerFragment;

        }

    }
}
