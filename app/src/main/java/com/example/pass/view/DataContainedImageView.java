package com.example.pass.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class DataContainedImageView extends ImageView {

    private int indexInAdapterList = -1;


    public DataContainedImageView(Context context) {
        super(context);
    }

    public DataContainedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DataContainedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DataContainedImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public int getIndexInAdapterList() {
        return indexInAdapterList;
    }

    public void setIndexInAdapterList(int indexInAdapterList) {
        this.indexInAdapterList = indexInAdapterList;
    }

}
