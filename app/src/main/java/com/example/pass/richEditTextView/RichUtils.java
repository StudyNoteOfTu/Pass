package com.example.pass.richEditTextView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.example.pass.richEditTextView.utils.SoftKeyboardUtil;
import com.example.pass.util.spans.customSpans.MyImageSpan;

import static com.example.pass.util.BitmapUtil.getFixedBitmap;


public class RichUtils {

    private static final String TAG  = "RichUtils";

    private RichEditText mRichEditText;

    private Activity mActivity;

    public RichUtils(Activity activity,RichEditText richEditText){
        mActivity = activity;
        mRichEditText = richEditText;
        registerEvents();
    }

    private void registerEvents() {
        RichTextWatcher mTextWatcher = new RichTextWatcher(mRichEditText);
        mRichEditText.addTextWatcher( mTextWatcher);

        mRichEditText.setOnSelectionChangedListener(new RichEditText.OnSelectionChangedListener() {
            @Override
            public void onChange(int curPos) {
                Log.d(TAG,"curPos = "+curPos);
                handleSelectionChanged(curPos);
            }
        });

        mRichEditText.setBackspaceListener(new RichInputConnectionWrapper.BackspaceListener() {
            @Override
            public boolean onBackspace() {
                Log.d(TAG,"onBackspace");
                return  handleDeleteKey();
            }
        });

        //为了兼容模拟器
        mRichEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_DEL == event.getKeyCode()
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && !SoftKeyboardUtil.isSoftShowing(mActivity)) {
                    //监听到删除键但是软键盘没弹出，可以基本断定是用模拟器
                    // TODO 也存在模拟器也会弹出软键盘的
                    Log.d(TAG,"模拟器 onBackspace");
                    return RichUtils.this.handleDeleteKey();
                }
                return false;
            }
        });

    }

    private boolean handleDeleteKey() {

        return false;
    }

    private void handleSelectionChanged(int curPos) {
    }

    public void insertPic(String picPath) {

        //handle前后span
        //这里假设handle处理完了，插入pic\
        Bitmap bitmap = getFixedBitmap(picPath);
        MyImageSpan imageSpan = new MyImageSpan(mActivity,bitmap,picPath);
        insertImage(imageSpan,picPath);

    }


    private void insertImage(MyImageSpan imageSpan,String replaceString){

        //前面加个回车
        insertStringIntoEditText("\n",mRichEditText.getSelectionStart());

        //将bitmap插入到EditText中
        SpannableStringBuilder imageSpannableString= new SpannableStringBuilder(replaceString);

        imageSpannableString.setSpan(imageSpan,0,replaceString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        insertStringIntoEditText(imageSpannableString,mRichEditText.getSelectionStart());
//        //在imageSpan后面插入一个空行
        insertStringIntoEditText("\n",mRichEditText.getSelectionStart());
    }

    private void insertStringIntoEditText(CharSequence content, int pos) {
        Editable editable = mRichEditText.getEditableText();//获得文本内容
        if (pos < 0 || pos >= editable.length()) {
            editable.append(content);
            mRichEditText.setSelection(editable.length());
        } else {
            editable.insert(pos, content);
            mRichEditText.setSelection(pos + content.length());
        }
    }


    public void mergeBlockSpanAfterDeleteEnter() {
    }

    public void changeLastBlockOrInlineSpanFlag() {
    }
}
