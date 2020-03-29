package com.example.pass.activities.analyseOffice.view.impls;

public interface Updatable {

    /**
     * 更新adapter中的数据到model中
     * @param key key
     * @param value  value
     * @param position  下标
     */
    void updateLineInfo(String key, String value, int position);
}
