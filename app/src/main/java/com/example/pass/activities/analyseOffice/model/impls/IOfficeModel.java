package com.example.pass.activities.analyseOffice.model.impls;

import com.example.pass.callbacks.ProgressCallback;

import java.util.List;

public interface IOfficeModel {


    /**
     *  解析文档生成xml文件
     * @param path office文件路径
     * @param dir 生成新文件的父目录
     * @param name 生成新文件的文件名（不带后缀）
     * @param listener 进度监听器
     * @return 是否成功
     */
    boolean readOffice(String path, String dir, String name, OnProgressListener listener);

    /**
     * 将xml文档转为String类型数据
     * @param path xml文档路径
     * @return 转出的String类型数据
     */
    String xmlToString(String path);

    /**
     * 将xml文档转为String类型数据
     * @param path xml文档路径
     * @param listener 进度监听器
     * @return 转出的String类型数据
     */
    String xmlToString(String path, OnProgressListener listener);

    /**
     * 将xml数据按行存入集合并返回
     * @param content xml转为String后的数据
     * @return 返回按行存入的集合
     */
    List<String> getXmlLines(String content);

    /**
     * 修改xml按行数据集合的对应位置标题数据
     * @param key key
     * @param value value
     * @param position  集合中的下标
     * @return 是否修改成功
     */
    boolean changeLineState(String key,String value,int position);

    /**
     * 将xml按行集合（可能更新过）重新整合
     * @param dir 新文件的存放父路径
     * @param name 新文件的文件名（不带后缀）
     * @return 文件路径
     */
    boolean recompileListToXml(String dir,String name,OnProgressListener listener);


    /**
     * 进度监听器，多适用
     */
    interface OnProgressListener extends ProgressCallback {
        void onStart();
        void onFinish(String result);
        void onError(String error);
    }



}
