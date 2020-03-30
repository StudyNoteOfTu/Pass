package com.example.pass.activities.mainActivity.fragments.scatteredFragment.presenter;

import com.example.pass.activities.mainActivity.fragments.scatteredFragment.model.ScatterModelImpl;
import com.example.pass.activities.mainActivity.fragments.scatteredFragment.model.impls.IScatterModel;
import com.example.pass.activities.mainActivity.fragments.scatteredFragment.view.impls.IScatterView;
import com.example.pass.base.BasePresenter;

import java.io.File;
import java.util.List;

public class ScatterPresenter<V extends IScatterView> extends BasePresenter<V> {

    IScatterModel scatterModel = new ScatterModelImpl();

    public ScatterPresenter(){

    }


    public void getScatterFileList(){
        if (isViewAttached()){
            scatterModel.loadScatterFiles(new IScatterModel.OnLoadScatterListener<File>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish(List<File> list) {
                    getView().loadScatterFiles(list);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

}
