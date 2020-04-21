package com.example.pass.view.psEditText;

import android.content.Context;
import android.text.Editable;
import android.text.Spanned;
import android.util.Log;

import com.example.pass.util.spans.customSpans.CustomSpan;
import com.example.pass.util.spans.customSpans.MyNormalSpan;
import com.hanks.lineheightedittext.TextWatcher;

public class PSTextWatcher implements TextWatcher {

    private static final String TAG = "PSTextWatcher";

    private Context mContext;

    private PSEditText mPSEditText;

    //上次的输入框内容
    private String lastEditTextContent = "";

    //是否删除了回车符
    private boolean isDeleteEnterStr;

    public PSTextWatcher(PSEditText psEditText) {
        mPSEditText = psEditText;
        mContext = psEditText.getContext();
    }

    /**
     * 删除字符的时候，要删除当前位置start和end相等的span
     */
    private void handleDelete(){
        Editable editable = mPSEditText.getEditableText();
        int cursorPos = mPSEditText.getSelectionStart();

        CustomSpan[] customSpans = editable.getSpans(cursorPos,cursorPos,CustomSpan.class);
        for (CustomSpan customSpan : customSpans) {
            if (editable.getSpanStart(cursorPos) == editable.getSpanEnd(customSpan)){
                editable.removeSpan(customSpan);
            }
        }

        if (isDeleteEnterStr){
            //删除了回车符
        }

    }


    int needStart = -1;
    int needAfter = 0;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //判断加入的文字前后是否有span包围，如果没有，就补一手normal
        Log.d(TAG,"start = "+start+" ,count = "+count+" ,after = "+after);
//        int curPos = mPSEditText.getSelectionStart();
//        CustomSpan[] customSpans = mPSEditText.getEditableText().getSpans(curPos+after,curPos+after,CustomSpan.class);
//        Log.d(TAG,"list size = "+customSpans.length);
//        int size = 0;
//        for (CustomSpan customSpan : customSpans) {
//            if (mPSEditText.getEditableText().getSpanEnd(customSpan)==curPos+after-1){
//                size++;
//            }
//        }
//        Log.d(TAG,"curPos = "+curPos+" ,size = "+size);
//        if (size == 0){
//            //需要至少添加一个normalSpan
//            needStart = start;
//            needAfter = after;
//        }

        needStart = start;
        needAfter = after;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {


        int cursorPos = mPSEditText.getSelectionStart();
        String editContent = s.toString();
        if (cursorPos > 0 && editContent.charAt(cursorPos - 1) == '\n' && !editContent.equalsIgnoreCase(lastEditTextContent)){
            lastEditTextContent = s.toString();
            changeLastLine();
        }

        //判断插入的文字是否有span
        int size = 0;
        CustomSpan[] customSpans = s.getSpans(needStart,needStart,CustomSpan.class);
        for (CustomSpan customSpan : customSpans) {
            if (s.getSpanStart(customSpan) == needStart){
                size ++;
            }
        }
        if (size==0){
            //添加normalSpan
            s.setSpan(new MyNormalSpan(),needStart,needStart+needAfter,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            needStart = -1;
            needAfter = 0;
        }

        lastEditTextContent = s.toString();
    }

    private void changeLastLine() {
        mPSEditText.getPSUtils().changeLastLineSpanFlag();
    }
}
