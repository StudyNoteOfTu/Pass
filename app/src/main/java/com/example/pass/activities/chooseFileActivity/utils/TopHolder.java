package com.example.pass.activities.chooseFileActivity.utils;

import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;

public class TopHolder extends RecyclerView.ViewHolder {
    Button btn_from_all;
    Button btn_from_wx;
    Button btn_from_qq;

    Button btn_type_all;
    Button btn_type_docx;
    Button btn_type_pptx;
    public TopHolder(@NonNull View itemView) {
        super(itemView);



        btn_from_all = itemView.findViewById(R.id.btn_from_all);


        btn_from_qq = itemView.findViewById(R.id.btn_from_qq);


        btn_from_wx = itemView.findViewById(R.id.btn_from_wx);

        btn_type_all = itemView.findViewById(R.id.btn_type_all);


        btn_type_docx = itemView.findViewById(R.id.btn_type_docx);


        btn_type_pptx = itemView.findViewById(R.id.btn_type_pptx);

    }
}
