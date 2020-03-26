package com.example.pass.activities.chooseFile.utils;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;

public class FileHolder extends RecyclerView.ViewHolder{

    public LinearLayout ll_info;
    public LinearLayout ll_slide_buttons;

    public TextView tv_filename;
    public TextView tv_createtime;
    public Button btn_more;

    public Button btn_slide_choose;
    public Button btn_slide_preview;

    public FileHolder(@NonNull View itemView) {
        super(itemView);
        ll_info = itemView.findViewById(R.id.ll_info);
        tv_filename = itemView.findViewById(R.id.tv_filename);
        tv_createtime = itemView.findViewById(R.id.tv_createtime);
        btn_more = itemView.findViewById(R.id.btn_more);
        btn_slide_choose = itemView.findViewById(R.id.btn_slide_choose);
        btn_slide_preview = itemView.findViewById(R.id.btn_slide_preview);
        ll_slide_buttons = itemView.findViewById(R.id.ll_slide_buttons);
    }
}
