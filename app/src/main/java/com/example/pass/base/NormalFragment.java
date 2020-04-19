package com.example.pass.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pass.R;

public abstract class NormalFragment extends Fragment {
    public Context myContext;
    public Activity myActivity;
    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("2020413Fragment", "onCreateView1");
        if (null == mContentView) {
            Log.d("2020413Fragment","onCreateView2");
            mContentView = inflater.inflate(setLayoutId(),container,false);

            if (mContentView.findViewById(R.id.tv_title)!=null)Log.d("2020413Fragment","onCreateView3 "+ (mContentView.findViewById(R.id.tv_title).getId()));
            Log.d("2020413Fragment","mContentView Id= "+ mContentView.getId());
            Log.d("2020413Fragment","LinearLayout Id= "+ R.id.ll_linearLayout);
            Log.d("2020413Fragment","layoutId Id= "+ setLayoutId());

            initViews(mContentView);
            Log.d("2020413Fragment","onCreateView4");
        }
//        if (mContentView != null) {
//            ViewGroup parent = (ViewGroup) mContentView.getParent();
//            if (parent != null) {
//                parent.removeView(mContentView);
//            }
//            return mContentView;
//        }
//        Log.d("2020413Fragment", "onCreateView2");
//        mContentView = inflater.inflate(setLayoutId(), container, false);
//        Log.d("2020413Fragment", "onCreateView3" + ((LinearLayout) mContentView).getChildCount());
//        initViews(mContentView);
//        Log.d("2020413Fragment", "onCreateView4");
        return mContentView;
    }

    @Override
    public void onResume() {
        Log.d("2020413Fragment", "onResume");
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("2020413Fragment", "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        Log.d("2020413Fragment","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("2020413Fragment","onDestroy");
        super.onDestroy();
    }

    protected abstract void initViews(View mContentView);

    protected abstract @LayoutRes
    int setLayoutId();

    @Override
    public void onAttach(@NonNull Context context) {
        this.myContext = context;
        super.onAttach(context);
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        this.myActivity = activity;
        super.onAttach(activity);
    }


    protected abstract void refreshState(boolean isEdit, boolean isShow);

}
