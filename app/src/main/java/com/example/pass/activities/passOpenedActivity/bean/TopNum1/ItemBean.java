package com.example.pass.activities.passOpenedActivity.bean.TopNum1;

import android.text.Editable;
import android.text.SpannableStringBuilder;

import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;

public class ItemBean {

    DataContainedSpannableStringBuilder title;

    public SpannableStringBuilder detail ;

    public ItemBean(DataContainedSpannableStringBuilder title) {
        this.title = title;
        detail = new SpannableStringBuilder();
    }

    public DataContainedSpannableStringBuilder getTitle() {
        return title;
    }

    public void setTitle(DataContainedSpannableStringBuilder title) {
        this.title = title;
    }

}
