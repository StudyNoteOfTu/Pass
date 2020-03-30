package com.example.pass.activities.mainActivity.fragments.scatteredFragment.view;

import android.view.View;

import com.example.pass.activities.mainActivity.fragments.scatteredFragment.presenter.ScatterPresenter;
import com.example.pass.activities.mainActivity.fragments.scatteredFragment.view.impls.IScatterView;
import com.example.pass.base.BaseFragment;
import com.example.pass.observers.base.Observer;

import java.io.File;
import java.util.List;

public class ScatterFragment extends BaseFragment<IScatterView, ScatterPresenter<IScatterView>> implements IScatterView, Observer<String> {



    @Override
    public void loadScatterFiles(List<File> list) {

    }

    @Override
    protected ScatterPresenter<IScatterView> createPresenter() {
        return null;
    }

    @Override
    protected void initViews(View mContentView) {

    }

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    public void update(String obj) {

    }
}
