package com.example.pass.activities.analyseOfficeActivity.bean;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.view.datacontainedViews.DataContainedImageView;
import com.example.pass.view.datacontainedViews.DataContainedLinearLayout;
import com.example.pass.view.swipeViews.SwipeItemLayout;

public class LineHolder extends RecyclerView.ViewHolder {

    public DataContainedImageView imageSelect;
    public ImageView imageTitle;
    public ImageView imagePicture;
    public TextView tvLineContent;
    public DataContainedLinearLayout linearLayout;
    public SwipeItemLayout swipeItemLayout;

    public LineHolder(View itemView) {
        super(itemView);
        swipeItemLayout = itemView.findViewById(R.id.swipeItemLayout);
        imageSelect = itemView.findViewById(R.id.image_select);
        imageTitle = itemView.findViewById(R.id.image_title);
        tvLineContent = itemView.findViewById(R.id.tv_line_content);
        imagePicture = itemView.findViewById(R.id.image_picture);

        linearLayout = itemView.findViewById(R.id.dataContainedLinearLayout);
        linearLayout.setContainedTextView(tvLineContent);
        linearLayout.setContainedImageView(imagePicture);
    }
}
