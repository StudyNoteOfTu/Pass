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


public class MenuPopWindow extends PopupWindow {

    private float mAlpha = 0.5f;

    private Context mContext;

    private TextView tv_size;
    private TextView tv_mode;
    private TextView tv_edit;
    private TextView tv_copy;

    public MenuPopWindow(Context context) {
        super(context);
        this.mContext = context;
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_pass_open_menu,null,false);
        setContentView(contentView);

        initViews(contentView);
    }

    private void initViews(View contentView) {
        tv_size = contentView.findViewById(R.id.tv_size);
        tv_mode = contentView.findViewById(R.id.tv_mode);
        tv_edit = contentView.findViewById(R.id.tv_edit);
        tv_copy = contentView.findViewById(R.id.tv_copy);
    }

    public void setOnOnClickListener(View.OnClickListener listener){
        if (tv_size != null && listener != null){
            tv_size.setOnClickListener(listener);
            tv_mode.setOnClickListener(listener);
            tv_edit.setOnClickListener(listener);
            tv_copy.setOnClickListener(listener);
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
