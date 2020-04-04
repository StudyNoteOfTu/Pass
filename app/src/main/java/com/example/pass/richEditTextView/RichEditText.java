package com.example.pass.richEditTextView;

import android.content.Context;
import android.util.AttributeSet;

import com.hanks.lineheightedittext.LineHeightEditText;

public class RichEditText extends LineHeightEditText {

    // 宽度撑满编辑区的ImageSpan需要减去的一个值，为了防止ImageSpan碰到边界导致的重复绘制的问题
    private static final int IMAGE_SPAN_MINUS_VALUE = 6;

    private int imageSpanPaddingTop;
    private int imageSpanPaddingBottom;
    private int imageSpanPaddingLeft;
    private int imageSpanPaddingRight;

    /**
     * EditText的宽度
     */
    public static int gRichEditTextWidthWithoutPadding;

    //输入法关键工具
    private RichInputConnectionWrapper mRichInputConnection;

    private Context mContext;


    public RichEditText(Context context) {
        super(context);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
