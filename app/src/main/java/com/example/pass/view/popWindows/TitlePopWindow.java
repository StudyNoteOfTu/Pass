package com.example.pass.view.popWindows;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.pass.R;

public class TitlePopWindow extends PopupWindow {

    private float mAlpha = 0.5f;

    TextView tv_h1;
    TextView tv_h2;
    TextView tv_h3;
    TextView tv_h4;

    Context mContext;

    public TitlePopWindow(Context context) {
        super(context);
        this.mContext = context;
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_title_select,null,false);
        setContentView(contentView);
        initViews(contentView);
    }



    private void initViews(View view) {
        tv_h1 = view.findViewById(R.id.tv_h1);
        tv_h2 = view.findViewById(R.id.tv_h2);
        tv_h3 = view.findViewById(R.id.tv_h3);
        tv_h4 = view.findViewById(R.id.tv_h4);

    }

    public void setOnOnClickListener(View.OnClickListener listener){

        if (tv_h1 != null && listener != null){
            tv_h1.setOnClickListener(listener);
            tv_h2.setOnClickListener(listener);
            tv_h3.setOnClickListener(listener);
            tv_h4.setOnClickListener(listener);
        }

    }

    /**
     * 控制窗口背景的不透明度
     */
    private void setWindowBackgroundAlpha(float alpha) {
        if (mContext == null) return;
        if (mContext instanceof Activity) {
            Window window = ((Activity) mContext).getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = alpha;
            window.setAttributes(layoutParams);
        }
    }


    /**
     * 窗口显示，窗口背景透明度渐变动画
     */
    public void showBackgroundAnimator() {
        if (mAlpha >= 1f) return;
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mAlpha);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
                setWindowBackgroundAlpha(alpha);
            }
        });
        animator.setDuration(360);
        animator.start();
    }


}
