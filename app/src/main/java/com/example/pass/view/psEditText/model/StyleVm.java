package com.example.pass.view.psEditText.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;

import com.example.pass.util.spans.enumtype.CustomTypeEnum;

public class StyleVm {

    /**
     * 具体类型（包含粗体、斜体、下划线、删除线等）
     */
    private @CustomTypeEnum
    int type;

    /**
     * 是否点亮
     */
    private boolean isLight;

    /**
     * 按钮ImageView
     */
    private ImageView ivIcon;

    /**
     * 正常的icon的资源id
     */
    private int iconNormalResId;

    /**
     * 点亮的icon的资源id
     */
    private int iconLightResId;

    /**
     * 被点击的View
     */
    private View clickedView;

    /**
     * 标题文本（比如文字提示）
     */
    private TextView tvTitle;

    /**
     * 标题文本正常的颜色
     */
    private @ColorInt
    int titleNormal;

    /**
     * 标题文本点亮的颜色
     */
    private @ColorInt
    int titleLightColor;


    public StyleVm(Builder builder) {
        this.type = builder.type;
        this.ivIcon = builder.ivIcon;
        this.isLight = false;
        this.iconNormalResId = builder.iconNormalResId;
        this.iconLightResId = builder.iconLightResId;
        this.clickedView = builder.clickedView;
        this.tvTitle = builder.tvTitle;
        this.titleNormal = builder.titleNormal;
        this.titleLightColor = builder.titleLightColor;
    }

    public int getType() {
        return type;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setLight(boolean light) {
        isLight = light;
    }

    public ImageView getIvIcon() {
        return ivIcon;
    }

    public int getIconNormalResId() {
        return iconNormalResId;
    }

    public int getIconLightResId() {
        return iconLightResId;
    }

    public View getClickedView() {
        return clickedView;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public int getTitleNormal() {
        return titleNormal;
    }

    public int getTitleLightColor() {
        return titleLightColor;
    }

    /**
     * 建造者模式
     */
    public static class Builder {
        /**
         * 具体类型（包含粗体、斜体、下划线、删除线等）
         */
        private @CustomTypeEnum
        int type;

        /**
         * 按钮ImageView
         */
        private ImageView ivIcon;

        /**
         * 正常的icon的资源id
         */
        private int iconNormalResId;

        /**
         * 点亮的icon的资源id
         */
        private int iconLightResId;

        /**
         * 被点击的View
         */
        private View clickedView;

        /**
         * 标题文本（比如文字提示）
         */
        private TextView tvTitle;

        /**
         * 标题文本正常的颜色
         */
        private @ColorInt
        int titleNormal;

        /**
         * 标题文本点亮的颜色
         */
        private @ColorInt
        int titleLightColor;

        public Builder setType(int type) {
            this.type = type;
            return this;
        }

        public Builder setIvIcon(ImageView ivIcon) {
            this.ivIcon = ivIcon;
            return this;
        }

        public Builder setIconNormalResId(int iconNormalResId) {
            this.iconNormalResId = iconNormalResId;
            return this;
        }

        public Builder setIconLightResId(int iconLightResId) {
            this.iconLightResId = iconLightResId;
            return this;
        }

        public Builder setClickedView(View clickedView) {
            this.clickedView = clickedView;
            return this;
        }

        public Builder setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
            return this;
        }

        public Builder setTitleNormal(int titleNormal) {
            this.titleNormal = titleNormal;
            return this;
        }

        public Builder setTitleLightColor(int titleLightColor) {
            this.titleLightColor = titleLightColor;
            return this;
        }

        public StyleVm build() {
            return new StyleVm(this);
        }
    }

}
