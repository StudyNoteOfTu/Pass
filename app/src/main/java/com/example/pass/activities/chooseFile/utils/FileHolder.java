package com.example.pass.activities.chooseFile.utils;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;

public class FileHolder extends RecyclerView.ViewHolder{

    public TextView tv_filename;
    public TextView tv_createtime;
    public Button btn_choose;


    public FileHolder(@NonNull View itemView) {
        super(itemView);
        tv_filename = itemView.findViewById(R.id.tv_filename);
        tv_createtime = itemView.findViewById(R.id.tv_createtime);
        btn_choose = itemView.findViewById(R.id.btn_choose);
    }
}
