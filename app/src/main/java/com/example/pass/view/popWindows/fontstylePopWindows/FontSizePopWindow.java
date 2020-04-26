package com.example.pass.view.popWindows.fontstylePopWindows;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pass.R;
import com.example.pass.view.popWindows.fontstylePopWindows.bean.TextTypeface;
import com.example.pass.view.popWindows.fontstylePopWindows.impls.OnTypefaceChangeListener;

import java.util.List;

public class FontSizePopWindow extends PopupWindow {

    private float mAlpha = 0.5f;

    private Context mContext;

    private SeekBar mSeekBar;

    private GridView mGridView;

    /**
     * 点击事件，打开GridView
     */
    private LinearLayout mLinearLayout;

    /**
     * 显示当前的字体
     */
    private TextView mTvCurrentTypeface;

    /**
     * 包含GridView的LinearLayout
     */
    private LinearLayout mLtContainedGridView;

    /**
     * 控制 gridView收起的ImageView
     */
    private ImageView mImgDown;

    /**
     * 外部View
     */
    private View outsideView1;

    /**
     * 外部View
     */
    private View outsideView2;


    private TtfGridAdapter mAdapter;

    public void changeCurrentTypeface(String currentTypeface){
        mTvCurrentTypeface.setText(currentTypeface);
    }

    public void setTypefaceData(List<TextTypeface> list, OnTypefaceChangeListener listener){
        mAdapter.setData(list,listener);
    }

    public FontSizePopWindow(Context context,int currentFontSize,SeekBar.OnSeekBarChangeListener listener) {
        super(context);
        this.mContext = context;
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_seekbar_fontsize,null,false);
        setContentView(contentView);

        initViews(contentView);

        initSeekBar(contentView,currentFontSize,listener);

        initGridView(contentView);
    }

    private void initViews(View contentView) {

        outsideView1 = contentView.findViewById(R.id.view_outside1);
        outsideView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        outsideView2 = contentView.findViewById(R.id.view_outside2);
        outsideView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mLinearLayout = contentView.findViewById(R.id.ll_choose_ttf);

        mLtContainedGridView = contentView.findViewById(R.id.ll_grid_container);


        mTvCurrentTypeface = contentView.findViewById(R.id.tv_ttf);

        mImgDown = contentView.findViewById(R.id.img_down);

        mImgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLtContainedGridView.setVisibility(View.GONE);
            }
        });

        mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLtContainedGridView.setVisibility(View.VISIBLE);
            }
        });


    }

    private void initGridView(View contentView) {
        mGridView = contentView.findViewById(R.id.gridView);
        mAdapter = new TtfGridAdapter(mContext);
        mGridView.setAdapter(mAdapter);
    }

    private void initSeekBar(View contentView, int currentFontSize, SeekBar.OnSeekBarChangeListener listener) {
        mSeekBar = contentView.findViewById(R.id.seekbar);
        mSeekBar.setProgress(currentFontSize);
        mSeekBar.setOnSeekBarChangeListener(listener);
    }
}
