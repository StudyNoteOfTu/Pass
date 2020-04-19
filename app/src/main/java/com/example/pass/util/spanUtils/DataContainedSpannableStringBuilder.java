package com.example.pass.util.spanUtils;

import android.text.SpannableStringBuilder;

public class DataContainedSpannableStringBuilder extends SpannableStringBuilder {

    public DataContainedSpannableStringBuilder() {
    }

    public DataContainedSpannableStringBuilder(CharSequence text) {
        super(text);
    }

    public DataContainedSpannableStringBuilder(CharSequence text, int start, int end) {
        super(text, start, end);
    }

    String key = "";
    String value = "";
    boolean isLineEnd = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isLineEnd() {
        return isLineEnd;
    }

    public void setLineEnd(boolean lineEnd) {
        isLineEnd = lineEnd;
    }

}
