package com.example.pass.view.popWindows;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.example.pass.R;

public class FontSizePopWindow extends PopupWindow {

    private float mAlpha = 0.5f;

    private Context mContext;

    private SeekBar seekBar;

    public FontSizePopWindow(Context context,int currentFontSize,SeekBar.OnSeekBarChangeListener listener) {
        super(context);
        this.mContext = context;
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_seekbar_fontsize,null,false);
        setContentView(contentView);

        initSeekBar(contentView,currentFontSize,listener);
    }

    private void initSeekBar(View contentView, int currentFontSize, SeekBar.OnSeekBarChangeListener listener) {
        seekBar = contentView.findViewById(R.id.seekbar);
        seekBar.setProgress(currentFontSize);
        seekBar.setOnSeekBarChangeListener(listener);
    }
}
