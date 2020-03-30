package com.example.pass.activities.analyseOfficeActivity.view.impls;

import java.util.List;

/**
 * 目录筛选接口
 */
public interface ITitleSelectView {

    void beginLoadXml();


    /**
     * 获取生成的分析文档的集合
     */
    void loadLineList(List<String> lineList);

    /**
     * 修改item状态，交给model进行数据处理
     */
    void changeLineState(String key,String value , int position);

    /**
     * 确定，完成目录标题选择，让model完成后续动作
     */
    void onFinishAndCompileListToXml();



}
