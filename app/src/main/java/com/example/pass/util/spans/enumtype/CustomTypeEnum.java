package com.example.pass.util.spans.enumtype;


import androidx.annotation.IntDef;

import com.example.pass.util.spans.customSpans.CustomSpan;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({CustomTypeEnum.NORMAL,CustomTypeEnum.BOLD,CustomTypeEnum.ITALIC,CustomTypeEnum.STRIKE_THROUGH,CustomTypeEnum.UNDERLINE,
        CustomTypeEnum.IMAGE,CustomTypeEnum.FOREGROUND_COLOR,CustomTypeEnum.HIGHLIGHT_COLOR, CustomTypeEnum.SHADE_MODE})
public @interface CustomTypeEnum {
    /**
     * 普通无效果
     */
    int NORMAL = 0;

    /**
     * 加粗
     */
    int BOLD = 1;

    /**
     * 斜体
     */
    int ITALIC = 2;

    /**
     * 删除线
     */
    int STRIKE_THROUGH = 3;

    /**
     * 下划线
     */
    int UNDERLINE = 4;

    /**
     * 图片
     */
    int IMAGE = 5;

    /**
     * 字体颜色
     */
    int FOREGROUND_COLOR = 6;

    /**
     * 高亮颜色
     */
    int HIGHLIGHT_COLOR = 7;


    /**
     * 遮罩效果
     */
    int SHADE_MODE = 8;


}
