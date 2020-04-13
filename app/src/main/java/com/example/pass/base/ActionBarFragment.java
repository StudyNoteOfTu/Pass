package com.example.pass.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

public abstract class ActionBarFragment extends NormalFragment {

    protected ActionBar mActionBar;

    public void setActionBar(ActionBar actionBar){
        mActionBar = actionBar;
    }

    public abstract void switchTitle(String title);

    @Override
    protected void refreshState(boolean isEdit, boolean isShow) {

    }
}
