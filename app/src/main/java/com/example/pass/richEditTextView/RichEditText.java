package com.example.pass.richEditTextView;

import android.app.Activity;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;

import com.hanks.lineheightedittext.LineHeightEditText;

public class RichEditText extends LineHeightEditText {

    private static final String TAG = "RichEditText";
    private int imageSpanPaddingTop;
    private int imageSpanPaddingBottom;
    private int imageSpanPaddingLeft;
    private int imageSpanPaddingRight;

    private RichInputConnectionWrapper mRichInputConnection;

    private Context mContext;

    private RichUtils mRichUtils;

    public interface OnSelectionChangedListener {
        /**
         * 光标位置改变回调
         *
         * @param curPos 新的光标位置
         */
        void onChange(int curPos);
    }

    /**
     * EditText监听复制、粘贴、剪切事件回调的接口
     */
    public interface IClipCallback {
        /**
         * 剪切回调
         */
        void onCut();

        /**
         * 复制回调
         */
        void onCopy();

        /**
         * 粘贴回调
         */
        void onPaste();
    }

    /**
     * 光标位置变化监听器
     */
    private OnSelectionChangedListener mOnSelectionChangedListener;


    public RichEditText(Context context) {
        super(context);
        init(context,null);
    }

    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        if (attrs != null) {
//            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RichEditText);
//
//            ta.recycle();
//        }

        mContext = context;
        mRichInputConnection = new RichInputConnectionWrapper(null, true);

        setMovementMethod(new ScrollingMovementMethod());

        requestFocus();

        setSelection(0);

        if (!(mContext instanceof Activity)) {
            Log.e(TAG, "context is not activity context!");
            return;
        }

        mRichUtils = new RichUtils((Activity) context, this);

    }

    /**
     * 注册光标位置监听器
     *
     * @param listener 光标位置变化监听器
     */
    protected void setOnSelectionChangedListener(OnSelectionChangedListener listener) {
        this.mOnSelectionChangedListener = listener;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        if (mOnSelectionChangedListener != null) {
            mOnSelectionChangedListener.onChange(selEnd);
        }
    }

    /**
     * 设置软键盘删除按键监听器
     *
     * @param backspaceListener 软键盘删除按键监听器
     */
    protected void setBackspaceListener(RichInputConnectionWrapper.BackspaceListener backspaceListener) {
        mRichInputConnection.setBackspaceListener(backspaceListener);
    }

    public RichUtils getmRichUtils() {
        return mRichUtils;
    }

    public void setmRichUtils(RichUtils mRichUtils) {
        this.mRichUtils = mRichUtils;
    }
}
