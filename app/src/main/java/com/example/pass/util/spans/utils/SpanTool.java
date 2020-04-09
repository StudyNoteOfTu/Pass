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
    public static Spannable handleInsert(Spannable spannable, int insertStart, int insertEnd, CustomSpan insertSpan) {

        long start_time = System.currentTimeMillis();
        //首先获得spannable中所有的span的起始与终点坐标
        CustomSpan[] customSpans = spannable.getSpans(0, spannable.length(), CustomSpan.class);

        //判断start和end在谁的中间
        List<CustomSpan> outterSpans = new ArrayList<>();
        //判断start穿插在谁中间
        List<CustomSpan> previousSpans = new ArrayList<>();
        //判断end穿插在谁中间
        List<CustomSpan> latterSpans = new ArrayList<>();
        //在其中间的span起始或者终点的位置,不重复，用hashMap
        Map<Integer, Integer> innerIndexMap = new HashMap<>();

        Map<String, Integer> shadeSpanNotHere = new HashMap<>();

        Map<String,MyShadeSpan> shadeSpanInsertedArea = new HashMap<>();
        //开始判断
        int originStart;
        int originEnd;
        for (CustomSpan customSpan : customSpans) {
            originStart = spannable.getSpanStart(customSpan);
            originEnd = spannable.getSpanEnd(customSpan);
            //在判断的时候去空,不浪费时间和资源
            if (originStart == originEnd) {
                spannable.removeSpan(customSpan);
                continue;
            }
            if (originStart < insertStart && originEnd <= insertStart) {
                //完全不接壤
            } else if (originStart >= insertEnd && originEnd > insertEnd) {
                //完全不接壤
            } else if (originStart <= insertStart && originEnd > insertStart && originEnd < insertEnd) {
                //前入
                if (customSpan instanceof MyShadeSpan) {
                    shadeSpanNotHere.put("<" + insertStart + "," + originEnd + ">", 0);
                }
                innerIndexMap.put(originEnd, 0);
                previousSpans.add(customSpan);
            } else if (originEnd >= insertEnd && originStart > insertStart && originStart < insertEnd) {
                //后进
                if (customSpan instanceof MyShadeSpan) {
                    shadeSpanNotHere.put("<" + originStart + "," + insertEnd + ">", 0);
                }
                innerIndexMap.put(originStart, 0);
                latterSpans.add(customSpan);
            } else if (originStart >= insertStart && originEnd <= insertEnd) {
                //全内
                if (customSpan instanceof MyShadeSpan) {
                    shadeSpanNotHere.put("<" + originStart + "," + originEnd + ">", 0);
                }
                innerIndexMap.put(originStart, 0);
                innerIndexMap.put(originEnd, 0);
            } else if (originStart <= insertStart && originEnd >= insertEnd) {
                //全外
                if (customSpan instanceof MyShadeSpan) {
                    shadeSpanNotHere.put("<" + insertStart + "," + insertEnd + ">", 0);
                }
                outterSpans.add(customSpan);
            } else {
            }
        }

        Log.d("NoHereTest", new ArrayList<>(shadeSpanNotHere.keySet()).toString());

        //开始处理
        //首先处理outterSpan
        //判断原来是否是shade，如果是，重叠部分应当不予处理
        int span_start = -1;
        int span_end = -1;
        for (CustomSpan customSpan : outterSpans) {
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            //先remove掉原来的
            spannable.removeSpan(customSpan);
            //添加
            //判断原来是否是shade
            if (customSpan instanceof MyShadeSpan) {
                //如果原来是shade，则反转处理
                //重叠部分取消span
                MyShadeSpan shadeSpan1 =(MyShadeSpan) CustomSpanFactory.getCustomSpanInstance(customSpan);
                MyShadeSpan shadeSpan2 =(MyShadeSpan) CustomSpanFactory.getCustomSpanInstance(customSpan);
                spannable.setSpan(shadeSpan1, span_start, insertStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(shadeSpan2, insertEnd, span_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                shadeSpanInsertedArea.put("<"+span_start+","+insertStart+">",shadeSpan1);
                shadeSpanInsertedArea.put("<"+insertEnd+","+span_end+">",shadeSpan2);

            } else {
                spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), span_start, insertStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), insertEnd, span_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), insertStart, insertEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }


        //接着处理previousSpan
        for (CustomSpan customSpan : previousSpans) {
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            //首先处理原来的span
            spannable.removeSpan(customSpan);
            if (customSpan instanceof MyShadeSpan) {
                //重叠部分作翻转处理
                MyShadeSpan shadeSpan1 =(MyShadeSpan) CustomSpanFactory.getCustomSpanInstance(customSpan);
                spannable.setSpan(shadeSpan1, span_start, insertStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                shadeSpanInsertedArea.put("<"+span_start+","+insertStart+">",shadeSpan1);
            } else {
                spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), span_start, insertStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), insertStart, span_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        //处理latterSpan
        for (CustomSpan customSpan : latterSpans) {
            span_start = spannable.getSpanStart(customSpan);
            span_end = spannable.getSpanEnd(customSpan);
            //首先处理原来的span
            spannable.removeSpan(customSpan);
            if (customSpan instanceof MyShadeSpan) {
                MyShadeSpan shadeSpan1 =(MyShadeSpan) CustomSpanFactory.getCustomSpanInstance(customSpan);
                spannable.setSpan(shadeSpan1, insertEnd, span_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                shadeSpanInsertedArea.put("<"+insertEnd+","+span_end+">",shadeSpan1);
            } else {
                spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), span_start, insertEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), insertEnd, span_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        //开始处理插入span的设置
        List<Integer> innerIndexList = new ArrayList<>(innerIndexMap.keySet());
        //自然升序
        Collections.sort(innerIndexList);
        //如果在不允许的范围内就不允许插入
        int beginIndex = insertStart;
        MyShadeSpan insertShadeSpan;
        for (Integer integer : innerIndexList) {
            insertShadeSpan =(MyShadeSpan) CustomSpanFactory.getCustomSpanInstance(insertSpan);
            spannable.setSpan(insertShadeSpan, beginIndex, integer, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            shadeSpanInsertedArea.put("<"+beginIndex+","+integer+">",insertShadeSpan);
            beginIndex = integer;
        }
        insertShadeSpan =(MyShadeSpan) CustomSpanFactory.getCustomSpanInstance(insertSpan);
        spannable.setSpan(insertShadeSpan, beginIndex, insertEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        shadeSpanInsertedArea.put("<"+beginIndex+","+insertEnd+">",insertShadeSpan);

        //先全给加进去，然后remove掉
//        for (String s : shadeSpanNotHere.keySet()) {
//            Log.d("NoHereTest",s);
//            if (shadeSpanInsertedArea.get(s)!= null){
//                spannable.removeSpan(shadeSpanInsertedArea.get(s));
//            }
//        }

        MyShadeSpan[] shadeSpans = spannable.getSpans(0,spannable.length(),MyShadeSpan.class);
        for (MyShadeSpan shadeSpan : shadeSpans) {
            int shade_start = spannable.getSpanStart(shadeSpan);
            int shade_end = spannable.getSpanEnd(shadeSpan);
            if (shadeSpanNotHere.get(("<"+shade_start+","+shade_end+">"))!=null){
                spannable.removeSpan(shadeSpan);
            }
        }

        return spannable;
    }

}
