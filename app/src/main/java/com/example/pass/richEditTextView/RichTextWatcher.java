package com.example.pass.richEditTextView;

import android.content.Context;
import android.text.Editable;
import android.text.ParcelableSpan;
import android.util.Log;

import com.example.pass.util.spans.customSpans.MyImageSpan;
import com.hanks.lineheightedittext.TextWatcher;

public class RichTextWatcher implements TextWatcher {

    private static final String TAG = "RichTextWatcher";
    private Context mContext;
    private RichEditText mRichEditText;

    // 在修改前的输入框的文本长度
    private int beforeEditContentLen = 0;

    //是否需要插入回车符的位置，场景：在imageSpan后面输入文字的时候需要换行
    private int needInsertBreakLinePosAfterImage= -1;

    //是否需要再ImageSpan之前插入换行，场景：在imageSpan前面输入文字的时候需要换行
    private boolean isNeedInsertBreakLineBeforeImage;

    // 上次的输入框内容
    private String lastEditTextContent = "";

    //是否删除了回车符
    private boolean isDeleteEnterStr;

    public RichTextWatcher(RichEditText richEditText) {
        mRichEditText = richEditText;
        mContext = mRichEditText.getContext();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //count为文字变化开始位置往前删除多少字，after为文字变化开始位置往后增加多少字
        Log.d(TAG,"beforeTextChanged, start = "+start +" ,count = "+count+" ,after = "+after);
        isDeleteEnterStr = after==0 && s.length() >0 && s.charAt(start)=='\n';
        Log.d("SpanTest","isDeleteEnterStr"+isDeleteEnterStr);
        beforeEditContentLen = s.length();
        Editable editable = mRichEditText.getEditableText();
        int curPos = mRichEditText.getSelectionStart();

        //判断是否在图片之前 、 之后 删除
        if (curPos >0 && after == 0){
            MyImageSpan[] imageSpans = editable.getSpans(curPos,curPos+1,MyImageSpan.class);
            if (imageSpans.length > 0 ){
                //说明在图片之前删除，更改selection
                int span_start = editable.getSpanStart(imageSpans[0]);
                Log.d("SpanTest","char "+s.charAt(span_start-1)+"[]");
                mRichEditText.setSelection(span_start-1);
                editable.insert(mRichEditText.getSelectionStart(),"\n");
            }

            //测试出来的结果，直接把del转到图片背后
            if (curPos >2) {
                imageSpans = editable.getSpans(curPos - 2, curPos, MyImageSpan.class);
                if (imageSpans.length >0){
                    int span_end = editable.getSpanEnd(imageSpans[0]);
                    Log.d("SpanTest","char "+s.charAt(span_end)+"[]");
                    //说明在图片之后删除，直接删除imageSpan
                    mRichEditText.setSelection(span_end);
                    editable.insert(span_end,"\n");
                }
            }
        }


        //判断是否在图片后面输入
        if (curPos == 0){
            needInsertBreakLinePosAfterImage = -1;
        }else{
            //判断是否在图片后面输入
            MyImageSpan[] imageSpansAfter = editable.getSpans(curPos - 1, curPos, MyImageSpan.class);
            if (imageSpansAfter.length >0){
                //说明当前光标处于imageSpan的后面，如果在当前位置输入文字，需要另起一行
                needInsertBreakLinePosAfterImage = curPos;
            }else{
                needInsertBreakLinePosAfterImage = -1;
            }
        }

        //判断是否在图片的前面输入
        MyImageSpan[] imageSpansBefore = editable.getSpans(curPos,curPos+1,MyImageSpan.class);
        //说明当前光标在ImageSpan的前面
        isNeedInsertBreakLineBeforeImage = imageSpansBefore.length>0;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG,"onTextChanged, start = "+start +" ,count = "+count+" ,before = "+before);
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d(TAG,"afterTextChanged ");
        if (s.toString().length() < beforeEditContentLen) {
            // 说明删除了字符
            if (s.length() > 0) {
                handleDelete();
            }
            lastEditTextContent = s.toString();
            return;
        }

        int cursorPos = mRichEditText.getSelectionStart();
        String editContent = s.toString();
        //处理图片前后换行问题
        if (needInsertBreakLinePosAfterImage != -1 &&
                cursorPos > 0 && editContent.charAt(cursorPos - 1) != '\n') {
            //在imageSpan后面输入了文字（除了'\n'），则需要换行
            s.insert(needInsertBreakLinePosAfterImage, "\n");
        }
        if (isNeedInsertBreakLineBeforeImage && cursorPos >= 0) {
            // 在ImageSpan前输入回车, 则需要将光标移动到上一个行
            // 在ImageSpan前输入文字（除了'\n'），则需要先换行，在将光标移动到上一行
            if (editContent.charAt(cursorPos - 1) != '\n') {
                s.insert(cursorPos, "\n");
            }
            mRichEditText.setSelection(cursorPos);
        }

        if (cursorPos > 0 && editContent.charAt(cursorPos - 1) == '\n' && !editContent.equals(lastEditTextContent)) {
            // 输入了回车, 需要断开上一行的样式（包括inline和block）
            lastEditTextContent = s.toString();
            changeLastBlockOrInlineSpanFlag();
        }

        lastEditTextContent = s.toString();
    }

    private void changeLastBlockOrInlineSpanFlag() {
        mRichEditText.getmRichUtils().changeLastBlockOrInlineSpanFlag();
    }

    /**
     * 删除字符的时候，要删除当前位置start和end相等的span
     */
    private void handleDelete() {
        Editable editable = mRichEditText.getEditableText();
        int cursorPos = mRichEditText.getSelectionStart();

        ParcelableSpan[] parcelableSpans = editable.getSpans(cursorPos, cursorPos, ParcelableSpan.class);

        for (ParcelableSpan span : parcelableSpans) {
            if (editable.getSpanStart(span) == editable.getSpanEnd(span)) {
                editable.removeSpan(span);
            }
        }

        if (isDeleteEnterStr) {
            // 删除了回车符，如果回车前后两行只要有一行是block样式，就要合并
            mRichEditText.getmRichUtils().mergeBlockSpanAfterDeleteEnter();
        }
    }
}
