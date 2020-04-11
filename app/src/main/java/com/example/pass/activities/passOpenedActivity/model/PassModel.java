package com.example.pass.activities.passOpenedActivity.model;

import android.text.SpannableStringBuilder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pass.activities.MyApplication;
import com.example.pass.activities.passOpenedActivity.bean.TopNum1.ItemBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.util.officeUtils.MyXmlReader;
import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;
import com.example.pass.util.spanUtils.XmlToSpanUtil;

import java.util.ArrayList;
import java.util.List;

public class PassModel {

    List<SpannableStringBuilder> stringBuilders;

    String shader_xml_path;

    public PassModel() {
        stringBuilders = new ArrayList<>();
    }

    public interface OnLoadFileListener {
        void onStart();

        void onFinish(int top, Object object);
    }

    /**
     * 加载本地文件，吧数据转入list<HBean>中
     *
     * @param parentPath 文件夹路径
     */
    public void loadFile(String parentPath, @NonNull OnLoadFileListener listener) {

        listener.onStart();

        List<String> xmlString = new ArrayList<>();
        List<DataContainedSpannableStringBuilder> dataContainedSpannableStringBuilders = readXml(parentPath + "/final/final.xml");
        //首先找DataContainedSpannableStringBuilders中最大的title
        int top = 0;
        int level;
        for (DataContainedSpannableStringBuilder dataContainedSpannableStringBuilder : dataContainedSpannableStringBuilders) {
            if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_title())) {
                //获取value
                level = Integer.parseInt(dataContainedSpannableStringBuilder.getValue());
                Log.d("2020411","get level = "+level);
                if (level > top) top = level;
            }
        }
        //接下来开始存入数据

        //如果top= 0
        //直接返回全部文本
        if (top == 0) {
            SpannableStringBuilder sb = new SpannableStringBuilder();
            DataContainedSpannableStringBuilder dcSb;
            for (int i = 0; i < dataContainedSpannableStringBuilders.size(); i++) {
                dcSb = dataContainedSpannableStringBuilders.get(i);
                if (dcSb.getKey().equalsIgnoreCase(XmlTags.getKey_ignore())) {

                } else {
                    sb.append(dcSb);
                }
            }
            listener.onFinish(0, sb);
        } else if (top == 1) {
            //如果top = 1
            //返回带title的bean
            //从头开始扫描
            //遇到1，新建一个ItemBean
            List<ItemBean> itemBeans = new ArrayList<>();
            ItemBean nowItem = null;
            ItemBean lastInsertItem = null;
            for (DataContainedSpannableStringBuilder dataContainedSpannableStringBuilder : dataContainedSpannableStringBuilders) {
                if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_ignore())) {

                } else {
                    //如果不是ignore
                    //开始扫描
                    if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_title())) {
                        if (nowItem == null) {
                            //第一个
                            nowItem = new ItemBean(dataContainedSpannableStringBuilder);
                        } else {
                            //后面个
                            itemBeans.add(nowItem);
                            lastInsertItem = nowItem;
                            //创建新的item， 获取标题
                            nowItem = new ItemBean(dataContainedSpannableStringBuilder);
                        }
                    } else {
                        //没设置标题，说明是0
                        //将内容插入nowItem中
                        if (nowItem != null) {
                            nowItem.detail.append(dataContainedSpannableStringBuilder);
                        }
                    }
                }
            }
            //判断最后一个是否处理完毕
            if (lastInsertItem != nowItem) {
                //说明要么last = null， nowItem != null 要么 last = lastItem, nowItem = newItem;
                //插入
                itemBeans.add(nowItem);
            }

            listener.onFinish(1, itemBeans);

        } else {
            //如果top > 1
            //返回TreeItem
            //判断top是多少 4 3 2
            if (top == 4) {
                HBean hBean = new HBean();
                hBean.h4List = new ArrayList<>();
                HBean.H4 tempH4 = null;
                HBean.H4.H3 tempH3 = null;
                HBean.H4.H3.H2 tempH2 = null;
                HBean.H4.H3.H2.H1 tempH1 = null;
                int nowLevel;
                for (DataContainedSpannableStringBuilder dataContainedSpannableStringBuilder : dataContainedSpannableStringBuilders) {
                    Log.d("2020411C",dataContainedSpannableStringBuilder.toString());
                    if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_ignore())) {
                        //如果忽略就不处理
                    } else {
                        //如果正常，获取title
                        if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_title())) {
                            //获取level
                            nowLevel = Integer.parseInt(dataContainedSpannableStringBuilder.getValue());
                            switch (nowLevel) {
                                case 4:
                                    //初始化
                                    tempH4 = new HBean.H4();
                                    tempH4.marginLeftLevel = 0;
                                    tempH4.h3List = new ArrayList<>();
                                    //加入
                                    hBean.h4List.add(tempH4);
                                    //传入标题
                                    tempH4.h4Text = dataContainedSpannableStringBuilder;
                                    break;
                                case 3:
                                    //初始化H3
                                    tempH3 = new HBean.H4.H3();
                                    tempH3.marginLeftLevel = 1;
                                    tempH3.h2List = new ArrayList<>();
                                    //加入h4
                                    tempH4.h3List.add(tempH3);
                                    //将标题信息传给H3
                                    tempH3.h3Text = dataContainedSpannableStringBuilder;
//                                        lastLevel = nowLevel;
                                    break;
                                case 2:
                                    //初始化h2
                                    tempH2 = new HBean.H4.H3.H2();
                                    tempH2.marginLeftLevel = 2;
                                    tempH2.h1List = new ArrayList<>();
                                    //加入h3
                                    tempH3.h2List.add(tempH2);
                                    //将标题信息传给H2
                                    tempH2.h2Text = dataContainedSpannableStringBuilder;
//                                        lastLevel = nowLevel;
                                    break;
                                case 1:
                                    //初始化h1
                                    tempH1 = new HBean.H4.H3.H2.H1();
                                    tempH1.marginLeftLevel = 3;
                                    //加入h2
                                    tempH2.h1List.add(tempH1);
                                    //将标题信息传给H1
                                    tempH1.h1Text = dataContainedSpannableStringBuilder;
//                                        lastLevel = nowLevel;
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            //没有title，就是具体内容
                            tempH1.detail.append(dataContainedSpannableStringBuilder);
                        }
                    }
                }
                listener.onFinish(4,hBean);
            } else if (top == 3) {
                HBean.H4 h4 = new HBean.H4();
                h4.h3List = new ArrayList<>();
                HBean.H4.H3 tempH3 = null;
                HBean.H4.H3.H2 tempH2 = null;
                HBean.H4.H3.H2.H1 tempH1 = null;
                int nowLevel;
                for (DataContainedSpannableStringBuilder dataContainedSpannableStringBuilder : dataContainedSpannableStringBuilders) {
                    Log.d("2020411C",dataContainedSpannableStringBuilder.toString());
                    if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_ignore())) {
                        //如果忽略就不处理
                    } else {
                        //如果不忽略就获取title
                        if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_title())) {
                            //获取level
                            nowLevel = Integer.parseInt(dataContainedSpannableStringBuilder.getValue());
                            switch (nowLevel) {
                                case 3:
                                    //初始化
                                    tempH3 = new HBean.H4.H3();
                                    tempH3.marginLeftLevel = 0;
                                    tempH3.h2List = new ArrayList<>();
                                    //insert
                                    h4.h3List.add(tempH3);
                                    //set title
                                    tempH3.h3Text = dataContainedSpannableStringBuilder;
                                    break;
                                case 2:
                                    //initial
                                    tempH2 = new HBean.H4.H3.H2();
                                    tempH2.marginLeftLevel = 1;
                                    tempH2.h1List = new ArrayList<>();
                                    //insert
                                    tempH3.h2List.add(tempH2);
                                    //set title
                                    tempH2.h2Text = dataContainedSpannableStringBuilder;
                                    break;
                                case 1:
                                    //initial
                                    tempH1 = new HBean.H4.H3.H2.H1();
                                    tempH1.marginLeftLevel = 2;
                                    //insert
                                    tempH2.h1List.add(tempH1);
                                    //set title
                                    tempH1.h1Text = dataContainedSpannableStringBuilder;
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            //no title ,means the concrete detail
                            tempH1.detail.append(dataContainedSpannableStringBuilder);
                        }
                    }
                }
                listener.onFinish(3,h4);
            } else if (top == 2) {
                HBean.H4.H3 h3 = new HBean.H4.H3();
                h3.h2List = new ArrayList<>();
                HBean.H4.H3.H2 tempH2 = null;
                HBean.H4.H3.H2.H1 tempH1 = null;
                int nowLevel;
                for (DataContainedSpannableStringBuilder dataContainedSpannableStringBuilder : dataContainedSpannableStringBuilders) {
                    Log.d("2020411C",dataContainedSpannableStringBuilder.toString());
                    if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_ignore())) {
                        //如果忽略就不做处理
                    } else {
                        //如果不忽略，正常处理
                        if (dataContainedSpannableStringBuilder.getKey().equalsIgnoreCase(XmlTags.getKey_title())) {
                            nowLevel = Integer.parseInt(dataContainedSpannableStringBuilder.getValue());
                            switch (nowLevel) {
                                case 2:
                                    //初始化
                                    tempH2 = new HBean.H4.H3.H2();
                                    tempH2.marginLeftLevel = 0;
                                    tempH2.h1List = new ArrayList<>();
                                    //insert
                                    h3.h2List.add(tempH2);
                                    //set title
                                    tempH2.h2Text = dataContainedSpannableStringBuilder;
                                    break;
                                case 1:
                                    //initial
                                    tempH1 = new HBean.H4.H3.H2.H1();
                                    tempH1.marginLeftLevel = 1;
                                    //insert
                                    tempH2.h1List.add(tempH1);
                                    //set detail
                                    tempH1.detail.append(dataContainedSpannableStringBuilder);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
                listener.onFinish(2,h3);
            }

        }


    }

    private List<DataContainedSpannableStringBuilder> readXml(String path) {
        MyXmlReader myXmlReader = new MyXmlReader();
        String content = myXmlReader.fileToString(path);

        //解析xml并合成editable呈现在textview上
        XmlToSpanUtil xmlToSpanUtil = new XmlToSpanUtil();

        //返回<p></p>的集合
        return xmlToSpanUtil.xmlToEditable(MyApplication.getContextInstance(), content);
    }
}
