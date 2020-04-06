package com.example.pass.util.spans.utils;

import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;

import com.example.pass.util.spans.customSpans.CustomSpan;
import com.example.pass.util.spans.customSpans.CustomSpanFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpanTool {
    //进行样式处理

    //插入样式
    public static Spannable handleInsert(Spannable spannable, int start, int end, CustomSpan insertSpan){

        long start_time = System.currentTimeMillis();
        //首先获得spannable中所有的span的起始与终点坐标
        CustomSpan[] customSpans = spannable.getSpans(0,spannable.length(),CustomSpan.class);
        //判断start和end在谁的中间
        List<CustomSpan> outterSpans = new ArrayList<>();
        //判断start穿插在谁中间
        List<CustomSpan> previousSpans = new ArrayList<>();
        //判断end穿插在谁中间
        List<CustomSpan> latterSpans= new ArrayList<>();
        //在其中间的span起始或者终点的位置,不重复，用hashMap
        Map<Integer,Integer> innerIndexMap =new HashMap<>();
        //开始判断
        int outerStart;
        int outerEnd;
        for (CustomSpan customSpan : customSpans) {
            outerStart = spannable.getSpanStart(customSpan);
            outerEnd = spannable.getSpanEnd(customSpan);
            if(outerEnd <= start || outerStart >= end){//排除这个就必有交壤

            }else if (outerStart < start && outerEnd <end){
                //中间有个span_end
                innerIndexMap.put(outerEnd,0);
                previousSpans.add(customSpan);
            }else if(start < outerStart && end < outerEnd){
                //中间有个span_start
                innerIndexMap.put(outerStart,0);
                latterSpans.add(customSpan);
            }else if( start <outerStart && outerEnd < end){
                //中间有 span_Start, span_end两个
                innerIndexMap.put(outerStart,0);
                innerIndexMap.put(outerEnd,0);
            }else if(outerStart == start && outerEnd < end){
                //中间有span_end
                innerIndexMap.put(outerEnd,0);
                outterSpans.add(customSpan);
            }else if (outerStart < start && outerEnd == end){
                //中间有span_start
                innerIndexMap.put(outerStart,0);
                outterSpans.add(customSpan);
            }
        }

        //开始处理
        //首先处理outterSpan
        int span_start = -1;
        int span_end = -1;
        for (CustomSpan customSpan : outterSpans) {
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            //先remove掉原来的
            spannable.removeSpan(customSpan);
            //添加
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),span_start,start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),end,span_end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        //接着处理previousSpan
        for (CustomSpan customSpan : previousSpans){
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            //首先处理原来的span
            spannable.removeSpan(customSpan);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),span_start,start,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),start,span_end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        for (CustomSpan customSpan : latterSpans) {
            span_start = spannable.getSpanStart(customSpans);
            span_end = spannable.getSpanEnd(customSpans);
            //首先处理原来的span
            spannable.removeSpan(customSpans);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),span_start,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan),end,span_end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //开始处理插入span的设置
        List<Integer> innerIndexList = new ArrayList<>(innerIndexMap.keySet());
        //自然升序
        Collections.sort(innerIndexList);
        int beginIndex = start;
        for (Integer integer : innerIndexList) {
            spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(insertSpan),beginIndex,integer,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            beginIndex = integer;
        }
        spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(insertSpan),beginIndex, end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Log.d("TimeConsumedTAG","use "+(System.currentTimeMillis() - start_time)+" millis");
        return spannable;
    }
}
