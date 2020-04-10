package com.example.pass.activities.mainActivity.fragments.mineFragment.view;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.example.pass.R;
import com.example.pass.activities.mainActivity.fragments.mineFragment.presenter.MinePresenter;
import com.example.pass.activities.mainActivity.fragments.mineFragment.view.impls.IMineView;
import com.example.pass.base.BaseFragment;

public class MineFragment extends BaseFragment<IMineView, MinePresenter<IMineView>> implements IMineView {

    @Override
    protected MinePresenter<IMineView> createPresenter() {
        return new MinePresenter<>();
    }

    @Override
    protected void initViews(View mContentView) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void switchTitle(String title) {
        if (mActionBar != null) {
            Log.d("2020410", "setTitle = " + title);
            mActionBar.setCustomView(R.layout.actionbar_mine);
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View customView = mActionBar.getCustomView();
            ((TextView) customView.findViewById(R.id.tv_title)).setText(title);
        }
    }
}
