package com.example.pass.recyclerentry.selectTitle.bean;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.view.DataContainedImageView;
import com.example.pass.view.DataContainedLinearLayout;

public class LineHolder extends RecyclerView.ViewHolder {

    public DataContainedImageView imageSelect;
    public ImageView imageTitle;
    public ImageView imagePicture;
    public TextView tvLineContent;
    public DataContainedLinearLayout linearLayout;

    public LineHolder(View itemView) {
        super(itemView);
        imageSelect = itemView.findViewById(R.id.image_select);
        imageTitle = itemView.findViewById(R.id.image_title);
        tvLineContent = itemView.findViewById(R.id.tv_line_content);
        imagePicture = itemView.findViewById(R.id.image_picture);

        linearLayout = itemView.findViewById(R.id.dataContainedLinearLayout);
        linearLayout.setContainedTextView(tvLineContent);
        linearLayout.setContainedImageView(imagePicture);
    }
}
