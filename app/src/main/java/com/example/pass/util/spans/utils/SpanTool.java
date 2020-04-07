package com.example.pass.util.spans.utils;

import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;

import com.example.pass.util.spans.customSpans.CustomSpan;
import com.example.pass.util.spans.customSpans.CustomSpanFactory;
import com.example.pass.util.spans.customSpans.MyShadeSpan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpanTool {
    //进行样式处理

    //插入样式
    public static Spannable handleInsert(Spannable spannable, int insertStart, int insertEnd, CustomSpan insertSpan){

        long start_time = System.currentTimeMillis();
        //首先获得spannable中所有的span的起始与终点坐标
        CustomSpan[] customSpans = spannable.getSpans(0,spannable.length(),CustomSpan.class);

        Log.d("LatterSpanTesst","begin------------");
        Log.d("LatterSpanTesst","insertStart = "+insertStart+" ,insertEnd = "+insertEnd);

        //判断start和end在谁的中间
        List<CustomSpan> outterSpans = new ArrayList<>();
        //判断start穿插在谁中间
        List<CustomSpan> previousSpans = new ArrayList<>();
        //判断end穿插在谁中间
        List<CustomSpan> latterSpans= new ArrayList<>();
        //在其中间的span起始或者终点的位置,不重复，用hashMap
        Map<Integer,Integer> innerIndexMap =new HashMap<>();
        //开始判断
        int originStart;
        int originEnd;
        for (CustomSpan customSpan : customSpans) {
            originStart = spannable.getSpanStart(customSpan);
            originEnd = spannable.getSpanEnd(customSpan);
            //在判断的时候去空,不浪费时间和资源
            if (originStart == originEnd){
                spannable.removeSpan(customSpan);
                continue;
            }
            Log.d("TextShadeTAG1","get tag , origin start = "+originStart+" , origin end = "+originEnd +" insert start = "+insertStart+" ,end = "+insertEnd+"type = "+customSpan.getType());
            if (originStart < insertStart && originEnd <= insertStart){
                Log.d("TextShadeTAG","get before");
                //完全不接壤
            }else if (originStart >= insertEnd && originEnd > insertEnd){
                Log.d("TextShadeTAG","get after");
                //完全不接壤
            }else if(originStart<= insertStart && originEnd> insertStart && originEnd < insertEnd){
                //前入
                Log.d("TextShadeTAG","get previous , origin start = "+originStart+" , origin end = "+originEnd +" insert start = "+insertStart+" ,end = "+insertEnd+"type = "+customSpan.getType());
                innerIndexMap.put(originEnd,0);
                previousSpans.add(customSpan);
            }else if (originEnd >= insertEnd && originStart > insertStart && originStart < insertEnd){
                //后进
                Log.d("TextShadeTAG","get latter , origin start = "+originStart+" , origin end = "+originEnd +" insert start = "+insertStart+" ,end = "+insertEnd+"type = "+customSpan.getType());
                innerIndexMap.put(originStart,0);
                latterSpans.add(customSpan);
            }else if(originStart>=insertStart && originEnd <= insertEnd){
                //全内
                Log.d("TextShadeTAG","all inner");
                innerIndexMap.put(originStart,0);
                innerIndexMap.put(originEnd,0);
            }else if(originStart <= insertStart && originEnd >= insertEnd){
                //全外
                Log.d("TextShadeTAG","get outer , origin start = "+originStart+" , origin end = "+originEnd +" insert start = "+insertStart+" ,end = "+insertEnd+"type = "+customSpan.getType());
                outterSpans.add(customSpan);
            }else{
                Log.d("TextShadeTAG","else ...");
            }
        }
        Log.d("LatterSpanTesst","for end");



        //开始处理
        //首先处理outterSpan
        int span_start = -1;
        int span_end = -1;
        for (CustomSpan customSpan : outterSpans) {
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            Log.d("OuterSpanTAG","get OuterSpan , span_start = "+span_start+" ,span_end = "+span_end+" insert start = "+insertStart+" end = "+insertEnd+" type = "+customSpan.getType());
            //先remove掉原来的
            spannable.removeSpan(customSpan);
            //添加
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),span_start,insertStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),insertEnd,span_end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        //接着处理previousSpan
        for (CustomSpan customSpan : previousSpans){
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            Log.d("PreviousSpanTAG","get PreviousSpan , span_start = "+span_start+" ,span_end = "+span_end+" insert start = "+insertStart+" end = "+insertEnd+" type = "+customSpan.getType());

            //首先处理原来的span
            spannable.removeSpan(customSpan);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),span_start,insertStart,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),insertStart,span_end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //处理latterSpan
        for (CustomSpan customSpan : latterSpans) {
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            Log.d("LatterSpanTAG","get latterSpan , span_start = "+span_start+" ,span_end = "+span_end+" insert start = "+insertStart+" end = "+insertEnd+" type = "+customSpan.getType());

            //首先处理原来的span
            spannable.removeSpan(customSpan);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),span_start,insertEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),insertEnd,span_end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //开始处理插入span的设置
        List<Integer> innerIndexList = new ArrayList<>(innerIndexMap.keySet());
        //自然升序
        Collections.sort(innerIndexList);
        int beginIndex = insertStart;
        Log.d("TimeConsumedTAG","start = "+insertStart+" list = "+innerIndexList.toString());
        for (Integer integer : innerIndexList) {
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(insertSpan),beginIndex,integer,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            beginIndex = integer;
        }
        spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(insertSpan),beginIndex, insertEnd,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Log.d("TimeConsumedTAG","use "+(System.currentTimeMillis() - start_time)+" millis");
        return spannable;
    }
}
