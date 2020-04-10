package com.example.pass.activities.mainActivity.fragments.scatteredFragment.view;


import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pass.R;
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
        return new ScatterPresenter<>();
    }

    @Override
    protected void initViews(View mContentView) {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_scatter;
    }

    @Override
    public void switchTitle(String title) {
        if (mActionBar != null) {
            Log.d("2020410","setTitle = "+title);
            mActionBar.setShowHideAnimationEnabled(false);
            mActionBar.show();
            mActionBar.setCustomView(R.layout.actionbar_scatter);
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            View customView= mActionBar.getCustomView();
            ((TextView)customView.findViewById(R.id.tv_title)).setText(title);
        }
    }

    @Override
    public void update(String obj) {

    }


}
