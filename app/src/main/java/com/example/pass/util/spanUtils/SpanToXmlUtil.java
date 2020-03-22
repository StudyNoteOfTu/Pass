package com.example.pass.util.spanUtils;

import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SpanToXmlUtil {

    private static final String TAG = "SpanToXmlUtil";

    //将编辑的Editable转回自定义Xml
    //支持 粗体，下划线，斜体，图片，字体颜色，背景颜色


    //转回的xml应该是节段形式的，即 <p>aaa</p><p>aaa</p>
    //deal it line by line
    public static void editableToXml(Editable editable){
        List<SpannableStringBuilder> lines = new ArrayList<>();
        SpannableStringBuilder delEditable = new SpannableStringBuilder(editable);
        int start = 0,end=0;
        for (int i = 0 ; i < delEditable.length() ; i ++){
            if (delEditable.charAt(i)=='\n'){
                end = i;
                //将字段分出来
                lines.add((SpannableStringBuilder)delEditable.subSequence(start,end));
                start = i+1;
            }
        }
        //获得一行一行的Editable
        //接下来按行处理,按行处理的结果存入xmlStr集合
        List<String> xmlStr = new ArrayList<>();
        for (SpannableStringBuilder line : lines) {
            String result = handleLineToXmlString(line);
        }


    }

    private static String handleLineToXmlString(SpannableStringBuilder line) {
        //首先获取所有span位置
        Log.d(TAG,"---------------------------GettingSpan Begin--------------------------------");
        Log.d(TAG,line.toString());
        ParcelableSpan[] spans = line.getSpans(0,line.length()-1,ParcelableSpan.class);
        for (ParcelableSpan span:spans){
            Log.d(TAG,"typeId "+span.getSpanTypeId()+" start:" + line.getSpanStart(span));
            Log.d(TAG,"typeId "+span.getSpanTypeId()+" end:" + line.getSpanEnd(span));
        }
        Log.d(TAG,"---------------------------GettingSpan End--------------------------------");

        return null;
    }


}
