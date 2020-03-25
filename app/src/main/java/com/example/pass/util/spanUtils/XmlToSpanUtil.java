package com.example.pass.util.spanUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.pass.util.officeUtils.FileUtil;
import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.util.spans.ClickableImageSpan;
import com.example.pass.util.spans.customSpans.MyForegroundColorSpan;
import com.example.pass.util.spans.customSpans.MyHighLightColoSpan;
import com.example.pass.util.spans.customSpans.MyNormalSpan;
import com.example.pass.util.spans.customSpans.MyStrikethroughSpan;
import com.example.pass.util.spans.customSpans.MyStyleSpan;
import com.example.pass.util.spans.customSpans.MyUnderlineSpan;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class XmlToSpanUtil {

    private static final String TAG = "XmlToSpanUtil";

    //将String类型的Xml解析为Span修饰的Editable
    //返回<p></p>的集合
    public List<DataContainedSpannableStringBuilder> xmlToEditable(Context context, String xml) {

        String p_key = "";
        String p_value ="";
        List<DataContainedSpannableStringBuilder> editables = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        sb.append(XmlTags.getXmlBegin());
        sb.append(xml);
        sb.append(XmlTags.getXmlEnd());
        xml = sb.toString();


        try {
//            byte[] bytes = xml.getBytes("UTF-8");
//            xml = new String(bytes,3,bytes.length-3);,
            xml = xml.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setInput(inputStream, "utf-8");

            //序列 id 颜色 粗体 下划线 斜体 删除线 标题

            boolean isList = false;
            boolean isSize = false;
            boolean isColor = false;
            boolean isBold = false;
            boolean isUnderline = false;
            boolean isItalic = false;
            boolean isTitle = false;
            boolean isLinethrough = false;
            boolean isHighlight = false;
            String valueOfList = "";
            String valueOfListId = "";
            String valueOfTitle = "";
            String valueOfColor = "";
            String valueOfHightlight = "";

            DataContainedSpannableStringBuilder spannableStringBuilder = null;

            int event_type = xmlPullParser.getEventType();
            while (event_type != XmlPullParser.END_DOCUMENT) {
                switch (event_type) {
                    case XmlPullParser.START_TAG:
                        String tagBegin = xmlPullParser.getName();

                        if (tagBegin.equalsIgnoreCase("p")) {
                            p_key = xmlPullParser.getAttributeValue("","key");//key
                            p_value = xmlPullParser.getAttributeValue("","val");//value

                        }

                        if (tagBegin.equalsIgnoreCase("pic")) {
                            String path = xmlPullParser.nextText();
                            Bitmap bitmap = FileUtil.getLocalBitmap(path);
                            if (bitmap != null) {
//                                ImageSpan imageSpan = new ImageSpan(context, bitmap);
                                ClickableImageSpan imageSpan = new ClickableImageSpan(context,bitmap);
                                spannableStringBuilder = new DataContainedSpannableStringBuilder();
                                //将pic的文字部分变为路径，这样就可以提取了
                                spannableStringBuilder.append(path);
                                spannableStringBuilder.setSpan(imageSpan, 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                //设置spannableStringbuilder属性
                                spannableStringBuilder = setSBAttributes(spannableStringBuilder,p_key,p_value,false);

                                editables.add(spannableStringBuilder);
                            }
                        }

                        //所有span片段都被<r></r>包围,先设置文字，再设置span, 在TAG寻找END中 设置span
                        if (tagBegin.equalsIgnoreCase("r")) {
                            spannableStringBuilder = new DataContainedSpannableStringBuilder();
                        }

                        if (tagBegin.equalsIgnoreCase("style")) {
                            String key = xmlPullParser.getAttributeValue(0);
                            String value = xmlPullParser.getAttributeValue(1);
                            if (key.equalsIgnoreCase(XmlTags.getValue_bold())) {//加粗样式
                                isBold = true;
                            } else if (key.equalsIgnoreCase(XmlTags.getValue_italic())) {//斜体样式
                                isItalic = true;
                            } else if (key.equalsIgnoreCase(XmlTags.getValue_underline())) {//下划线样式
                                isUnderline = true;
                            } else if (key.equalsIgnoreCase(XmlTags.getValue_linethrough())) {//删除线样式
                                isLinethrough = true;
                            } else if (key.equalsIgnoreCase(XmlTags.getValue_color())) {//字体颜色样式
                                isColor = true;
                                valueOfColor = value;
                            } else if (key.equalsIgnoreCase(XmlTags.getValue_highlight())) {//字体背景高亮样式
                                isHighlight = true;
                                valueOfHightlight = value;
                            }
                        }

                        if (tagBegin.equalsIgnoreCase("t")) {
                            if (spannableStringBuilder != null) {
//                                Log.d(TAG, "get Text = " + xmlPullParser.getText());
//                                Log.d(TAG, "get Text = " + xmlPullParser.nextText());
//                                if (xmlPullParser.getText()!=null) {
//                                    spannableStringBuilder.append(xmlPullParser.getText());
//
//                                }
                                spannableStringBuilder.append(xmlPullParser.nextText());

                                //全都至少加上一个Normal标识
                                spannableStringBuilder.setSpan(new MyNormalSpan(),0,spannableStringBuilder.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                                if (isBold) {
                                    if (spannableStringBuilder.length() != 0)

                                        Log.d("TestBold","setBoldSpan");
                                        Log.d("TestBold","content is"+spannableStringBuilder.toString());
                                        spannableStringBuilder.setSpan(new MyStyleSpan(Typeface.BOLD), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    isBold = false;
                                }
                                if (isItalic) {
                                    if (spannableStringBuilder.length() != 0)
                                        spannableStringBuilder.setSpan(new MyStyleSpan(Typeface.ITALIC), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    isItalic = false;
                                }
                                if (isUnderline) {
                                    if (spannableStringBuilder.length() != 0)
                                        spannableStringBuilder.setSpan(new MyUnderlineSpan(), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    isUnderline = false;
                                }
                                if (isLinethrough) {
                                    if (spannableStringBuilder.length() != 0)
                                        spannableStringBuilder.setSpan(new MyStrikethroughSpan(), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    isLinethrough = false;
                                }
                                if (isColor) {
                                    if (spannableStringBuilder.length() != 0)
                                        if (!TextUtils.isEmpty(valueOfColor)) {
                                            if (!valueOfColor.equalsIgnoreCase("000000")) {
                                                Log.d("TestForeground", "setForegroundColorSpan");
                                                Log.d("TestForeground", "content is" + spannableStringBuilder.toString());
                                                spannableStringBuilder.setSpan(new MyForegroundColorSpan(Color.parseColor(colorToString(valueOfColor))), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            }
                                        }
                                    isColor = false;
                                }
                                if (isHighlight) {
                                    if (spannableStringBuilder.length() != 0)
                                        if (!TextUtils.isEmpty(valueOfHightlight))
                                            spannableStringBuilder.setSpan(new MyHighLightColoSpan(getFieldValueByFieldName(valueOfHightlight, new Color())), 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    isHighlight = false;
                                }

                                spannableStringBuilder = setSBAttributes(spannableStringBuilder,p_key,p_value,false);
                                editables.add(spannableStringBuilder);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String tagEnd = xmlPullParser.getName();
                        if (tagEnd.equalsIgnoreCase("p")) {
                            DataContainedSpannableStringBuilder dsb = new DataContainedSpannableStringBuilder("\n");
                            dsb = setSBAttributes(dsb,p_key,p_value,true);
                            editables.add(dsb);
                        }
                        break;

                    default:
                        break;

                }
                event_type = xmlPullParser.next();
            }
//            for (int i = 0 ; i < editables.size() ; i++){
//                Log.d(TAG,"editable i = "+i+" content is "+editables.get(i).toString());
//            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return editables;
    }

    private DataContainedSpannableStringBuilder setSBAttributes(DataContainedSpannableStringBuilder spannableStringBuilder, String key, String value, boolean isLineEnd) {
        spannableStringBuilder.setKey(key);
        spannableStringBuilder.setValue(value);
        spannableStringBuilder.setLineEnd(isLineEnd);
        return spannableStringBuilder;
    }

    private int getFieldValueByFieldName(String fieldName, Object object) {
        //注意这里修改为判断fieldName是否为数字开头，如果数字开头说明是srgbClr
        if (isInteger(fieldName)){
            fieldName = "0xFF"+ fieldName.substring(fieldName.length()-6);
            return (int) Integer.parseInt(fieldName);
        }
        //如果是文字 比如YELLOW之类的
        try {
            Field field = object.getClass().getDeclaredField(fieldName.toUpperCase());
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return (int) field.get(object);
        } catch (Exception e) {
            return 0;//0x00000000 透明
        }
    }

    //判断是不是rgb数字
    private boolean isInteger(String fieldName) {
        Pattern pattern = Pattern.compile("[0-9a-fA-F]*");
        return pattern.matcher(fieldName).matches();
    }


    private String colorToString(String color) {
        if (!color.startsWith("#")) {
            return "#FF" + color;
        } else {
            return color;
        }
    }
}
