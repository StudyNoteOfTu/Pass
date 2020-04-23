package com.example.pass.view.psEditText;

import android.content.Context;
import android.media.Image;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.example.pass.util.spans.customSpans.CustomSpan;
import com.example.pass.util.spans.customSpans.MyImageSpan;
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


    /**
     * start: 字符串中即将发生修改的位置。
     * count: 字符串中即将被修改的文字的长度。如果是新增的话则为0。
     * after: 被修改的文字修改之后的长度。如果是删除的话则为0。
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //判断加入的文字前后是否有span包围，如果没有，就补一手normal
        Log.d(TAG,"------------------------------Text changed  BEGIN---------------------------------------------");
        Log.d(TAG,"start = "+start+" ,count = "+count+" ,after = "+after);

        SpannableStringBuilder sb = new SpannableStringBuilder(s);
        CustomSpan[] customSpans = sb.getSpans(0,sb.length(),CustomSpan.class);
        for (CustomSpan customSpan : customSpans) {
            Log.d(TAG,"type = "+customSpan.getType()+" start = "+sb.getSpanStart(customSpan)+" ,end = "+sb.getSpanEnd(customSpan));
        }

        Log.d(TAG,"------------------------------before Text changed END---------------------------------------------");

        needStart = start;
        needAfter = after;
    }

    /**
     * start: 有变动的字符串的序号
     * before: 被改变的字符串长度，如果是新增则为0。
     * count: 添加的字符串长度，如果是删除则为0。
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d(TAG,"onTextChanged start = "+start+" ,before = "+before+" ,count = "+count);
        Log.d(TAG,"------------------------------On Text changed  END---------------------------------------------");

    }

    @Override
    public void afterTextChanged(Editable s) {
        //删去所有不属于CustomSpan的东西
        CharacterStyle[] allSpans = s.getSpans(0,s.length(),CharacterStyle.class);
        for (CharacterStyle span : allSpans) {
            if (!(span instanceof CustomSpan)){
                Log.d(TAG,"removed one that is not instance of CustomSpan"+" , spanStart = "+s.getSpanStart(span)+" ,spanEnd = "+s.getSpanEnd(span));
                s.removeSpan(span);
            }else{
                //如果有无内容CustomSpan，则删掉
                Log.d(TAG,"remove CustomSpan whose start equals to it's end");

                if (s.getSpanStart(span)==s.getSpanEnd(span)){
                    s.removeSpan(span);
                }
            }
        }



        //原则思考：
        //之前会有增减，比如剪切黏贴啊之类的各种情况，但是不管如何，它都可以在这里进行处理
        //一方面处理删除

        //一方面处理添加 -》 单标添加、 框选修改（并非 删除+单标添加）
        //定规则：所有添加都算作普通文本输入，样式随EditText中原有Span走


        //↓↓↓↓↓↓↓↓↓↓↓↓处理删除↓↓↓↓↓↓↓↓↓↓↓↓

        //暂时发现的删除情况都自动完成需求，暂时无需处理


        //↓↓↓↓↓↓↓↓↓↓↓↓处理添加↓↓↓↓↓↓↓↓↓↓↓↓

        //处理插入文字，（应该在换行处理之前进行处理，因为可能插入动作就是换行
        //所有插入文字要删去本身Span，并根据所在位置设置Span（由前面延长，或者不设置Span（因为包括！）
        //首先获取该段文本
        if (needStart != -1 && needAfter != 0){
            //如若有插入文本
            //如果插入的是全部文本，则不作处理（初始插入）
            if (needStart == 0 && needStart+needAfter == s.length()){
                ////如果插入的是全部文本，则不作处理（初始插入）
            }else {
                //否则
                Log.d(TAG,"-----------------文字插入-----------------------");
                //拿到插入的文字段
                Editable subEditable = (Editable) s.subSequence(needStart, needStart + needAfter);
                //如果有图片，取消插入！！！！
                //插入的文字：
                Log.d(TAG,"插入文本: "+subEditable);

                CustomSpan[] customSpans = s.getSpans(0, s.length(), CustomSpan.class);
                //删去所有自身Span,什么是自身？ 自身就是所有Span的开头和末尾都在自身首尾内，会有冲突，但后续代码增加Normal的方法解决了这个问题？
                boolean hasImage = false;
                int spanStart;
                int spanEnd;
                for (CustomSpan customSpan : customSpans) {
                    //判断是否有图片Span
//                    if (customSpan instanceof MyImageSpan){
//                        hasImage = true;
//                        break;
//                    }
                    spanStart = s.getSpanStart(customSpan);
                    spanEnd = s.getSpanEnd(customSpan);
                    //判断customSpan是否在自身首尾内
                    //判断Span是否自带的，如果是，则删去自带的Span
                    if (needStart <= spanStart && spanEnd <= needStart+needAfter){
                        //判断是否有图片，做标记
                        if (customSpan instanceof MyImageSpan){
                            hasImage = true;
                        }
                        //只要在其中的Span全都删掉
                        s.removeSpan(customSpan);
                        Log.d(TAG,"删去文本自带的Span， 其type 为"+customSpan.getType());
                    }
                }

                if (hasImage){
                    //取消插入
                    Log.d(TAG,"存在图片，取消操作");
                    s.delete(needStart,needAfter);
                }else{
                    //没有图片
                    //删除所有Span
                }
            }
        }

        //换行处理
        int cursorPos = mPSEditText.getSelectionStart();
        String editContent = s.toString();
        if (cursorPos > 0 && editContent.charAt(cursorPos - 1) == '\n' && !editContent.equalsIgnoreCase(lastEditTextContent)){
            lastEditTextContent = s.toString();
            changeLastLine();
        }

        //为了修复在SEI 的E处插入文本的无Span问题
        //判断插入的文字是否有span
        int size = 0;
        CustomSpan[] customSpans = s.getSpans(needStart,needStart,CustomSpan.class);
        for (CustomSpan customSpan : customSpans) {
            //判断标准：
            //1、向外看customSpan包含了它
            if (s.getSpanStart(customSpan)< needStart && needStart+needAfter <= s.getSpanEnd(customSpan) ){
                //Start 接壤不算，不可等于（因为SEI，你在E的位置，没有Span拓展到本身,   End可以接壤， SEI在I的位置，有Span拓展到后续
                size++;
            }
            //2、向自己前面是否有Span起头,如果有，那么它 （一定？）不存在无Span情况
            if (s.getSpanStart(customSpan) == needStart){
                size ++;
            }

        }
        if (size==0){
            Log.d(TAG,"前后无样式，添加NormalSpan");
            //添加normalSpan
            s.setSpan(new MyNormalSpan(),needStart,needStart+needAfter,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            needStart = -1;
            needAfter = 0;
        }

        lastEditTextContent = s.toString();


        Log.d(TAG,"------------------------------Text changed  END---------------------------------------------");

    }

    private void changeLastLine() {
        mPSEditText.getPSUtils().changeLastLineSpanFlag();
    }
}
