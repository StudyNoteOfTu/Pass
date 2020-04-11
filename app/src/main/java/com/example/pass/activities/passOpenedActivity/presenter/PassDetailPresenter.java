package com.example.pass.activities.passOpenedActivity.presenter;

import android.text.SpannableStringBuilder;

import com.example.pass.activities.passOpenedActivity.bean.TopNum1.ItemBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.activities.passOpenedActivity.model.PassModel;
import com.example.pass.activities.passOpenedActivity.view.impls.IPassDetailView;
import com.example.pass.base.BasePresenter;

import java.util.List;

public class PassDetailPresenter<T extends IPassDetailView> extends BasePresenter<T> {

    private PassModel passModel;

    public PassDetailPresenter() {
        passModel = new PassModel();
    }

    public void getDetail(String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                passModel.loadFile(path, new PassModel.OnLoadFileListener() {
                    @Override
                    public void onStart() {
                        mViewRef.get().beginLoadFile();
                    }

                    @Override
                    public void onFinish(int top, Object object) {
                        switch (top) {
                            case 0:
                                if (object instanceof SpannableStringBuilder) {
                                    mViewRef.get().getTop0(top, (SpannableStringBuilder) object);
                                }
                                break;
                            case 1:
                                if (object instanceof List) {
                                    mViewRef.get().getTop1(top, (List<ItemBean>) object);
                                }
                                break;
                            case 2:
                                if (object instanceof HBean.H4.H3) {
                                    mViewRef.get().getTop2(top, (HBean.H4.H3) object);
                                }
                                break;
                            case 3:
                                if (object instanceof HBean.H4) {
                                    mViewRef.get().getTop3(top, (HBean.H4) object);
                                }
                                break;
                            case 4:
                                if (object instanceof HBean) {
                                    mViewRef.get().getTop4(top, (HBean) object);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }).start();

    }

}
