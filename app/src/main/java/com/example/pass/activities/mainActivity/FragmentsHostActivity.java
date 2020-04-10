package com.example.pass.activities.mainActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.pass.R;
import com.example.pass.activities.mainActivity.fragments.mineFragment.view.MineFragment;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.view.PassFolderFragment;
import com.example.pass.activities.mainActivity.fragments.scatteredFragment.view.ScatterFragment;
import com.example.pass.base.BaseFragment;

public class FragmentsHostActivity extends AppCompatActivity {

    private View mActionBar;

    private RadioGroup radioGroup;
    private RadioButton mRBScatter;
    private RadioButton mRBPass;
    private RadioButton mRBMine;

    private ScatterFragment mScatterFragment;
    private PassFolderFragment mPassFolderFragment;
    private MineFragment mMineFragment;

    private BaseFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_host);

        //初始化控件
        initViews();
        //初始化fragments
        initFragment();
        //设置切换监听器
        setListener();

    }

    private void setListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rbScatter:
                        switchFragment(mScatterFragment);
                        break;
                    case R.id.rbPass:
                        switchFragment(mPassFolderFragment);
                        break;
                    case R.id.rbMine:
                        switchFragment(mMineFragment);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initFragment(){
        mScatterFragment = new ScatterFragment();
        mScatterFragment.setActionBar(getSupportActionBar());

        mPassFolderFragment = new PassFolderFragment();
        mPassFolderFragment.setActionBar(getSupportActionBar());

        mMineFragment = new MineFragment();
        mMineFragment.setActionBar(getSupportActionBar());

        mFragment = mScatterFragment;
        getSupportFragmentManager().beginTransaction().add(R.id.container,mFragment).commit();
        mFragment.switchTitle("首页");
    }



    private void switchFragment(BaseFragment fragment) {
        if (fragment instanceof ScatterFragment){
            fragment.switchTitle("首页");
        }else if (fragment instanceof PassFolderFragment){
            fragment.switchTitle("记背本");
        }else if (fragment instanceof MineFragment){
            fragment.switchTitle("我的");
        }
        //判断当前显示的Fragment是不是切换的Fragment
        if (mFragment != fragment) {
            //判断切换的Fragment是否已经添加过
            if (!fragment.isAdded()) {
                //如果没有，则先把当前的Fragment隐藏，把切换的Fragment添加上
                getSupportFragmentManager().beginTransaction().hide(mFragment)
                        .add(R.id.container, fragment).commit();
            } else {
                //如果已经添加过，则先把当前的Fragment隐藏，把切换的Fragment显示出来
                getSupportFragmentManager().beginTransaction()
                        .hide(mFragment).show(fragment).commit();
            }
            mFragment = fragment;
        }
    }

    private void initViews() {
        radioGroup = findViewById(R.id.radioGroup);
        mRBScatter = findViewById(R.id.rbScatter);
        mRBPass = findViewById(R.id.rbPass);
        mRBMine = findViewById(R.id.rbMine);

        setStyle(R.drawable.selector_tab_scatter,mRBScatter);
        setStyle(R.drawable.selector_tab_pass,mRBPass);
        setStyle(R.drawable.selector_tab_mine,mRBMine);

    }

    /**
     * 动态设置每个tab的图片宽高以及文字间距
     *
     * @param selector RadioButton的样式选择器
     * @param rb       RadioButton的样式选择器
     */
    private void setStyle(int selector,RadioButton rb){
        Drawable drawable = getResources().getDrawable(selector);
        drawable.setBounds(0,0,80,80);
        rb.setCompoundDrawables(null,drawable,null,null);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
