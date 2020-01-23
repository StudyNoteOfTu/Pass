package com.example.pass.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class PassSpanTextView extends TextView {
    public PassSpanTextView(Context context) {
        super(context);
    }

    public PassSpanTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PassSpanTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PassSpanTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
