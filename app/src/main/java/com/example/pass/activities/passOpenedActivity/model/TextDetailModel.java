package com.example.pass.activities.passOpenedActivity.model;

import android.text.SpannableStringBuilder;
import android.util.Log;

import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.ParentInterface;
import com.example.pass.callbacks.LoadObjectCallback;
import com.example.pass.util.officeUtils.MyXmlWriter;
import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;
import com.example.pass.util.spanUtils.SpanToXmlUtil;

import java.io.File;
import java.util.List;

public class TextDetailModel {

    //当前文件在的路径,必须提前设置
    private String path;

    //所有需要展示的H1，必须提前设置
    private List<HBean.H4.H3.H2.H1> h1List;

    //spannable文字
    private SpannableStringBuilder spannableStringBuilder;

    //当前编辑的H1，作为传递参数用
    private HBean.H4.H3.H2.H1 currentH1;

    public TextDetailModel(){}

    public SpannableStringBuilder getSpannableStringBuilder() {
        return spannableStringBuilder;
    }

    public void setSpannableStringBuilder(SpannableStringBuilder spannableStringBuilder) {
        this.spannableStringBuilder = spannableStringBuilder;
    }

    public void setH1List(List<HBean.H4.H3.H2.H1> h1List){
        this.h1List = h1List;//传引用
    }

    public List<HBean.H4.H3.H2.H1> getH1List(){
        return h1List;
    }

    public void setCurrentH1(HBean.H4.H3.H2.H1 h1){
        currentH1 = h1;
    }

    public HBean.H4.H3.H2.H1 getCurrentH1(){
        return currentH1;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    //写入遮罩信息
    public void writeShaders(List<ShaderBean> shaderBeans){
        ShaderXmlTool.createXml(shaderBeans, path + File.separator + "shader", "shade");
    }

    public void writeTextInfo(SpannableStringBuilder spannableStringBuilder){
        String xml = SpanToXmlUtil.transformAllSpanToXmlFile(spannableStringBuilder);
        //覆盖
        MyXmlWriter.compileLinesToXml(xml,path+"/final","final" , new LoadObjectCallback<String>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish(String result) {
                Log.d("OnFinishTest",result);
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    //写入文本信息(List)
    public void writeTextInfo(){

        if (h1List.size() != 0) {
            HBean.H4.H3.H2.H1 h1 = h1List.get(0);
            ParentInterface rootParent = h1.parent;
            while (rootParent != null) {
                if (rootParent.getParent() != null) {
                    rootParent = (ParentInterface) rootParent.getParent();
                } else {
                    break;
                }
            }

            SpannableStringBuilder xmlStringBuilder = new SpannableStringBuilder();

            //首先写入文件头
            xmlStringBuilder.append(XmlTags.getXmlBegin());

            //接下来写入各行字段

            DataContainedSpannableStringBuilder sb;
            List<String> xmlList;
            //获取rootparent，开始遍历
            if (rootParent instanceof HBean) {
                for (HBean.H4 h4 : ((HBean) rootParent).h4List) {
                    //写入标题字段
                    sb = h4.h4Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    for (HBean.H4.H3 h3 : h4.h3List) {
                        sb = h3.h3Text;
                        createXmlStringLine(xmlStringBuilder, sb);
                        for (HBean.H4.H3.H2 h2 : h3.h2List) {
                            sb = h2.h2Text;
                            createXmlStringLine(xmlStringBuilder, sb);
                            for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                                //写入标题数据
                                sb = h11.h1Text;
                                createXmlStringLine(xmlStringBuilder, sb);
                                //写入内容
                                if (h11.detail != null) {
                                    xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                                    for (String s : xmlList) {
                                        xmlStringBuilder.append(s);
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (rootParent instanceof HBean.H4) {
                for (HBean.H4.H3 h3 : ((HBean.H4) rootParent).h3List) {
                    sb = h3.h3Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    for (HBean.H4.H3.H2 h2 : h3.h2List) {
                        sb = h2.h2Text;
                        createXmlStringLine(xmlStringBuilder, sb);
                        for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                            //写入标题数据
                            sb = h11.h1Text;
                            createXmlStringLine(xmlStringBuilder, sb);
                            //写入内容
                            if (h11.detail != null) {
                                xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                                for (String s : xmlList) {
                                    xmlStringBuilder.append(s);
                                }
                            }
                        }
                    }
                }
            } else if (rootParent instanceof HBean.H4.H3) {
                for (HBean.H4.H3.H2 h2 : ((HBean.H4.H3) rootParent).h2List) {
                    sb = h2.h2Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    for (HBean.H4.H3.H2.H1 h11 : h2.h1List) {
                        //写入标题数据
                        sb = h11.h1Text;
                        createXmlStringLine(xmlStringBuilder, sb);
                        //写入内容
                        if (h11.detail != null) {
                            xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                            for (String s : xmlList) {
                                xmlStringBuilder.append(s);
                            }
                        }
                    }
                }
            } else if (rootParent instanceof HBean.H4.H3.H2) {
                for (HBean.H4.H3.H2.H1 h11 : ((HBean.H4.H3.H2) rootParent).h1List) {
                    //写入标题数据
                    sb = h11.h1Text;
                    createXmlStringLine(xmlStringBuilder, sb);
                    //写入内容
                    if (h11.detail != null) {
                        xmlList = SpanToXmlUtil.editableToXml(h11.detail);
                        for (String s : xmlList) {
                            xmlStringBuilder.append(s);
                        }
                    }
                }
            }
            xmlStringBuilder.append(XmlTags.getXmlEnd());

            //写入文件
            MyXmlWriter.compileLinesToXml(xmlStringBuilder.toString(), path + "/final", "final", new LoadObjectCallback<String>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onFinish(String result) {
                    Log.d("OnFinishTest", result);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }


    /**
     * 创建单行xmlString
     */
    private void createXmlStringLine(SpannableStringBuilder xmlStringBuilder, DataContainedSpannableStringBuilder sb) {
        xmlStringBuilder.append(XmlTags.getLineBegin(sb.getKey(), sb.getValue()));
        xmlStringBuilder.append(XmlTags.getBlockBegin());
        xmlStringBuilder.append(XmlTags.getTextBegin());
        xmlStringBuilder.append(sb.toString());
        xmlStringBuilder.append(XmlTags.getTextEnd());
        xmlStringBuilder.append(XmlTags.getBlockEnd());
        xmlStringBuilder.append(XmlTags.getLineEnd());
    }


}
