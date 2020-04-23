package com.example.pass.view.psEditText;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import com.example.pass.R;
import com.example.pass.view.psEditText.model.StyleVm;
import com.example.pass.view.psEditText.utils.ClipboardUtil;
import com.example.pass.view.psEditText.utils.WindowUtil;
import com.hanks.lineheightedittext.LineHeightEditText;

public class PSEditText extends LineHeightEditText {

    private static final String TAG = "RichEditText";

    private int screenWidth;


    private PSInputConnectionWrapper mPSInputConnection;

    private Context mContext;

    private PSUtils mPSUtils;

    public void setBackspaceListener(PSInputConnectionWrapper.BackspaceListener backspaceListener) {
        mPSInputConnection.setBackspaceListener(backspaceListener);
    }


    //光标监听器
    public interface OnSelectionChangedListener{
        /**
         * 光标位置改变回调
         * @param curPos 新的光标的位置
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



    public PSEditText(Context context) {
        super(context);
        init(context,null);
    }

    public PSEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public PSEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    /**
     * 初始化
     * @param context 上下文
     * @param attrs 属性
     */
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
//            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PSEditText);
//            ta.recycle();
        }

        mContext = context;

        mPSInputConnection = new PSInputConnectionWrapper(null,true);

        setMovementMethod(new MyLinkMovementMethod());

        requestFocus();

        setSelection(0);

        if (!(mContext instanceof Activity)) {
            Log.e(TAG, "context is not activity context!");
            return;
        }

        mPSUtils = new PSUtils((Activity)context,this);

        screenWidth = WindowUtil.getScreenSize(mContext)[0];

    }


    public void setOnSelectionChangedListener(OnSelectionChangedListener onSelectionChangedListener) {
        this.mOnSelectionChangedListener = onSelectionChangedListener;
    }

    public void initStyleButton(StyleVm styleVm){
        mPSUtils.initStyleButton(styleVm);
    }

    /**
     * 插入一些东西之前，先删除被光标选中的区域
     */
    private void removeSelectedContent(){
        Editable editable = getEditableText();
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();

        if (selectionStart >= selectionEnd){
            return;
        }

        editable.delete(selectionStart,selectionEnd);
    }


    public PSUtils getPSUtils(){
        return mPSUtils;
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        if (mOnSelectionChangedListener != null){
            mOnSelectionChangedListener.onChange(selEnd);
        }
        super.onSelectionChanged(selStart, selEnd);

    }

    /**
     * 处理黏贴
     */
    private void handlePaste(){
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        Editable editable = getEditableText();
        editable.delete(selectionStart, selectionEnd);
        selectionStart = getSelectionStart();
        mPSUtils.insertStringIntoEditText(ClipboardUtil.getInstance(mContext),selectionStart);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        Log.d("EditTextMenuItem","id = "+id);
        switch (id) {
            case android.R.id.cut:
                Log.d(TAG,"cut ，getSelectionStart: " + getSelectionStart() + ", getSelectionEnd: " + getSelectionEnd());

                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onCut();
                }
                break;
            case android.R.id.copy:
                Log.d(TAG, "copy getSelectionStart: " + getSelectionStart() + ", getSelectionEnd: " + getSelectionEnd());

                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onCopy();
                }
                break;
            case android.R.id.paste:
                Log.d(TAG, "paste getSelectionStart: " + getSelectionStart() + ", getSelectionEnd: " + getSelectionEnd());

                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onPaste();
                }
                handlePaste();
                return true;
            default:
                break;
        }

        return super.onTextContextMenuItem(id);
    }

    /**
     * 当输入法和EditText建立连接的时候会通过这个方法返回一个InputConnection。
     * 我们需要代理这个方法的父类方法生成的InputConnection并返回我们自己的代理类。
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        mPSInputConnection.setTarget(super.onCreateInputConnection(outAttrs));
        return mPSInputConnection;
    }


}
