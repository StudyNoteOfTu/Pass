package com.example.pass.activities.passOpenedActivity.presenter;

import android.text.SpannableStringBuilder;

import com.example.pass.activities.passOpenedActivity.bean.TopNum1.ItemBean;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.activities.passOpenedActivity.model.TextDetailModel;
import com.example.pass.activities.passOpenedActivity.model.PassModel;
import com.example.pass.activities.passOpenedActivity.view.impls.IPassDetailView;
import com.example.pass.base.BasePresenter;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PassDetailPresenter<T extends IPassDetailView> extends BasePresenter<T> {

    private PassModel passModel;

    private TextDetailModel textDetailModel;

    public PassDetailPresenter() {
        passModel = new PassModel();
        textDetailModel = new TextDetailModel();
    }


    //------------------------解析文件，获得数据----------------------------------

    /**
     * 解析文件内容，解析到h1单元
     * @param path 文件相对根路径
     */
    public void getFileDetail(String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                passModel.loadFile(path, new PassModel.OnLoadFileListener() {
                    @Override
                    public void onStart() {
                        mViewRef.get().beginLoadFile();
                    }

                    @Override
                    public void onFinish(int top, Object object) {
                        switch (top) {
                            case 0:
                                if (object instanceof SpannableStringBuilder) {
                                    mViewRef.get().getTop0(top, (SpannableStringBuilder) object);
                                }
                                break;
                            case 1:
                                if (object instanceof List) {
                                    mViewRef.get().getTop1(top, (List<ItemBean>) object);
                                }
                                break;
                            case 2:
                                if (object instanceof HBean.H4.H3) {
                                    mViewRef.get().getTop2(top, (HBean.H4.H3) object);
                                }
                                break;
                            case 3:
                                if (object instanceof HBean.H4) {
                                    mViewRef.get().getTop3(top, (HBean.H4) object);
                                }
                                break;
                            case 4:
                                if (object instanceof HBean) {
                                    mViewRef.get().getTop4(top, (HBean) object);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        }).start();

    }

    /**
     * 将得到的TopNumOver1对象转成可以逐个展现的对象List，应当作引用
     */
    public List<HBean.H4.H3.H2.H1> transformBeanToList(Object obj){
        List<HBean.H4.H3.H2.H1> resultList = new ArrayList<>();
        //进行遍历，拿到所有的h1,注意list是有顺序的
        if (obj instanceof HBean){
            for (HBean.H4 h4 : ((HBean) obj).h4List) {
                for (HBean.H4.H3 h3 : h4.h3List) {
                    for (HBean.H4.H3.H2 h2 : h3.h2List) {
                        resultList.addAll(h2.h1List);
                    }
                }
            }
        }else if (obj instanceof HBean.H4){
            for (HBean.H4.H3 h3 : ((HBean.H4) obj).h3List) {
                for (HBean.H4.H3.H2 h2 : h3.h2List) {
                    resultList.addAll(h2.h1List);
                }
            }

        }else if (obj instanceof HBean.H4.H3){
            for (HBean.H4.H3.H2 h2 : ((HBean.H4.H3) obj).h2List) {
                resultList.addAll(h2.h1List);
            }
        }
        return resultList;
    }


    /**
     * 获得遮罩文件解析结果
     * @param path 文件相对根路径
     * @return
     */
    public List<ShaderBean> getShaderBeans(String path){
        return ShaderXmlTool.analyseXml(path+ File.separator+"shader"+"/shade.shader");
    }


    //-----------------------必须在Activity中 初始化数据Model------------------------------

    /**
     * 设置H1Model必须的一些数据
     * @param path 文件路径
     * @param list H1集合
     */
    public void initTextDetailModel(String path, List<HBean.H4.H3.H2.H1> list){
        textDetailModel.setH1List(list);
        textDetailModel.setPath(path);
    }

    public void initTextDetailModel(String path, SpannableStringBuilder spannableStringBuilder){
        textDetailModel.setSpannableStringBuilder(spannableStringBuilder);
        textDetailModel.setPath(path);
    }


    //----------------------------------用在Fragment中（子View层）-------------------------------------


    /**
     * 存入所有Text、Shader信息
     */
    public void saveShadersAndText(List<ShaderBean> shaderBeans){
        textDetailModel.writeShaders(shaderBeans);
        textDetailModel.writeTextInfo();
    }

    /**
     * 存入所有Text、Shader信息
     */
    public void saveShadersAndText(List<ShaderBean> shaderBeans,SpannableStringBuilder spannable){
        textDetailModel.writeShaders(shaderBeans);
        textDetailModel.writeTextInfo(spannable);
    }

    /**
     * 获得h1List
     */
    public List<HBean.H4.H3.H2.H1> getH1List(){
        return textDetailModel.getH1List();
    }

    /**
     * 获得spannable
     */
    public SpannableStringBuilder getSpannable(){
        return textDetailModel.getSpannableStringBuilder();
    }


}
