package com.example.pass.activities.passOpenedActivity.view.impls;

import android.text.SpannableStringBuilder;

import com.example.pass.activities.passOpenedActivity.bean.TopNum1.ItemBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;

import java.util.List;

public interface IPassDetailView {

    void beginLoadFile();

    void getTop0(int top, SpannableStringBuilder spannableStringBuilder);

    void getTop1(int top, List<ItemBean> object);

    void getTop2(int top, HBean.H4.H3 object);

    void getTop3(int top, HBean.H4 object);

    void getTop4(int top, HBean object);
}
