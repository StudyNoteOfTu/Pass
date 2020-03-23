package com.example.pass.util.spanUtils;

import android.text.Editable;
import android.text.ParcelableSpan;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.util.spans.customSpans.CustomSpan;
import com.example.pass.util.spans.customSpans.MyForegroundColorSpan;
import com.example.pass.util.spans.customSpans.MyHighLightColoSpan;
import com.example.pass.util.spans.enumtype.CustomTypeEnum;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class SpanToXmlUtil {

    private static final String TAG = "SpanToXmlUtil";

    //将编辑的Editable转回自定义Xml
    //支持 粗体，下划线，斜体，图片，字体颜色，背景颜色


    /**
     * 作用于将一个小节的内容进行编辑保存
     * 一个小节中可以包含很多行
     *
     * @param editable 这个小节的Editable
     * @return 返回这个小节各行转为Xml语句的集合
     */
    //转回的xml应该是节段形式的，即 <p>aaa</p><p>bbb</p>...
    //deal it line by line
    public static List<String> editableToXml(Editable editable) {


        //获取到的Editable的每一行
        List<SpannableStringBuilder> lines = new ArrayList<>();
        //需要处理的Editable转为SpannableStringBuilder
        SpannableStringBuilder delEditable = new SpannableStringBuilder(editable);
        //将需要处理的Editable按行拆分，并转换类型为SpannableStringBuilder到lines集合中
        int start = 0, end = 0;
        for (int i = 0; i < delEditable.length(); i++) {
            if (delEditable.charAt(i) == '\n') {
                end = i;
                //将字段分出来
                lines.add((SpannableStringBuilder) delEditable.subSequence(start, end));
                start = i + 1;
            }
        }

        //获得一行一行的(SpannableStringBuilder)Editable
        //接下来按行处理,按行处理的结果存入xmlStr集合
        List<String> xmlResultList = new ArrayList<>();
        for (int i = 0 ; i < lines.size() ; i++) {
            SpannableStringBuilder line = lines.get(i);
//            Log.d("TestIndex",i+"");
            String result = handleLineToXmlString(line);
            xmlResultList.add(result);
        }

        return xmlResultList;
    }

    /**
     * 注意，每小节的<p></p>均无属性，不是title也不是ignore啥也不是
     * 将一行的SpannableStringBuilder处理转为以<p></p>为首尾的Xml语句
     *
     * @param line 一行的SpannableStringBuilder
     * @return 返回处理后的到的xml语句
     */
    private static String handleLineToXmlString(SpannableStringBuilder line) {

        StringBuilder sb = new StringBuilder();

        //首先获取所有span位置
        Log.d(TAG, "---------------------------GettingSpan Begin--------------------------------");
        Log.d(TAG, line.toString());
        CustomSpan[] spans = line.getSpans(0, line.length() - 1, CustomSpan.class);

        //每个<r></r> 或者 <pic></pic> 的字段
        List<String> xmlOfEachText = new ArrayList<>();
        boolean isNextLine;
        int startIndex = 0;//上一个文字的开始位置，初始时候为0

        //类似<r></r> 或者 <pic></pic> 的字段
        StringBuilder xmlResult = null;//处理的Xml语句结果
        //记录是那些Span
        boolean isBold = false;
        boolean isItalic = false;
        boolean isUnderline = false;
        boolean isStrikeThrough = false;
        boolean isPic = false;
        String picPath = "";
        boolean isColor = false;
        String color = "000000";
        boolean isHighlight = false;
        String highlight = "ffffff";

        for (int i = 0; i < spans.length; i++) {
            CustomSpan span = spans[i];

            int type = span.getType();
            int spanStart = line.getSpanStart(span);
            int spanEnd = line.getSpanEnd(span);
            Log.d(TAG, "typeId " + span.getType() + " start:" + line.getSpanStart(span));
            Log.d(TAG, "typeId " + span.getType() + " end:" + line.getSpanEnd(span));

            //拿到span，临时变量保存当前span的属性


            //属性保存后，判断当前span是否为最后一个span或者新的span，如果是最后一个span，直接存入

            //当前span的起始位置
            int thisIndex = line.getSpanStart(span);
            //如果起始位置是新的，说明是新起一行
            if (thisIndex > startIndex) {//如果是新的位置，或者当前span为最后一个span
                Log.d("xmlResultTAG", "-----------newLine------------");
                //新起一行
                //处理上一行的数据
                xmlResult = new StringBuilder();

                if (isPic) {
                    Log.d("xmlResultTAG", "isPic!");
                    xmlResult.append(XmlTags.getPicBegin());
                    xmlResult.append(picPath);
                    xmlResult.append(XmlTags.getPicEnd());
                } else {
                    //如果是文字段
                    //添加文字段标签
                    xmlResult.append(XmlTags.getBlockBegin());
                    //添加若干个style标签
                    if (isBold) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_bold()));
                    }
                    if (isItalic) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_italic()));
                    }
                    if (isUnderline) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_underline()));
                    }
                    if (isStrikeThrough) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_linethrough()));
                    }
                    if (isColor) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_color(), color));
                    }
                    if (isHighlight) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_highlight(), highlight));
                    }
                    //设置文字
                    xmlResult.append(XmlTags.getTextBegin());
                    xmlResult.append(line.subSequence(spanStart, spanEnd).toString());
                    xmlResult.append(XmlTags.getTextEnd());
                    //设置文字段结束标签
                    xmlResult.append(XmlTags.getBlockEnd());
                }

                Log.d("xmlResultTAG", xmlResult.toString());
                //把上一行存入数组
                xmlOfEachText.add(xmlResult.toString());
                //将startIndex指向新一句的开始处
                startIndex = thisIndex;

                //刷新数据
                isBold = false;
                isItalic = false;
                isUnderline = false;
                isStrikeThrough = false;
                isPic = false;
                picPath = "";
                isColor = false;
                color = "000000";
                isHighlight = false;
                highlight = "ffffff";

                xmlResult = null;

            } else {
                //如果仍然是当前字段
            }
            //结果示例
//            typeId 1 start:0
//            typeId 1 end:11
            //根据Span编写Xml
            //首先拿到文字部分,注意处理一下没有效果的部分（处理方式：加上一个MyNormalSpan），每段文字都有一个MyNormalSpan

            //获取type，设置属性boolean值
            switch (type) {
                case CustomTypeEnum.NORMAL:
                    break;
                case CustomTypeEnum.BOLD:
                    isBold = true;
                    break;
                case CustomTypeEnum.ITALIC:
                    isItalic = true;
                    break;
                case CustomTypeEnum.STRIKE_THROUGH:
                    isStrikeThrough = true;
                    break;
                case CustomTypeEnum.UNDERLINE:
                    isUnderline = true;
                    break;
                case CustomTypeEnum.IMAGE:
                    isPic = true;
                    picPath = line.subSequence(spanStart, spanEnd).toString();
                    Log.d(TAG, "get pic Path = " + picPath);
                    break;
                case CustomTypeEnum.FOREGROUND_COLOR:
                    isColor = true;
                    color = String.format("%06x", ((MyForegroundColorSpan) span).getForegroundColor());
                    color = color.substring(color.length() - 6);
                    Log.d(TAG, "getForegroundColor " + color);
                    break;
                case CustomTypeEnum.HIGHLIGHT_COLOR:
                    isHighlight = true;
                    highlight = String.format("%06x", ((MyHighLightColoSpan) span).getBackgroundColor());
                    highlight = highlight.substring(highlight.length() - 6);
                    Log.d(TAG, "getHighlightColor " + highlight);
                    break;
                default:
                    break;
            }

            //判断当前span是否为最后一个span
            if (i == spans.length-1) {//如果是新的位置，或者当前span为最后一个span
                Log.d("xmlResultTAG", "-----------lastLine------------");
                //新起一行
                //处理上一行的数据
                xmlResult = new StringBuilder();

                if (isPic) {
                    Log.d("xmlResultTAG", "isPic!");
                    xmlResult.append(XmlTags.getPicBegin());
                    xmlResult.append(picPath);
                    xmlResult.append(XmlTags.getPicEnd());
                } else {
                    //如果是文字段
                    //添加文字段标签
                    xmlResult.append(XmlTags.getBlockBegin());
                    //添加若干个style标签
                    if (isBold) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_bold()));
                    }
                    if (isItalic) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_italic()));
                    }
                    if (isUnderline) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_underline()));
                    }
                    if (isStrikeThrough) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_linethrough()));
                    }
                    if (isColor) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_color(), color));
                    }
                    if (isHighlight) {
                        xmlResult.append(XmlTags.getStyleTag(XmlTags.getValue_highlight(), highlight));
                    }
                    //设置文字
                    xmlResult.append(XmlTags.getTextBegin());
                    xmlResult.append(line.subSequence(spanStart, spanEnd).toString());
                    xmlResult.append(XmlTags.getTextEnd());
                    //设置文字段结束标签
                    xmlResult.append(XmlTags.getBlockEnd());
                }

                Log.d("xmlResultTAG", xmlResult.toString());
                //把上一行存入数组
                xmlOfEachText.add(xmlResult.toString());
                //将startIndex指向新一句的开始处
                startIndex = thisIndex;

                //刷新数据
                isBold = false;
                isItalic = false;
                isUnderline = false;
                isStrikeThrough = false;
                isPic = false;
                picPath = "";
                isColor = false;
                color = "000000";
                isHighlight = false;
                highlight = "ffffff";

                xmlResult = null;

            } else {
                //如果仍然是当前字段
            }


        }
        Log.d(TAG, "---------------------------GettingSpan End--------------------------------");

        sb.append(XmlTags.getLineBegin());

        for (int i = 0 ; i < xmlOfEachText.size() ; i++){
             sb.append(xmlOfEachText.get(i));
        }

        sb.append(XmlTags.getLineEnd());
        return sb.toString();
    }


}
