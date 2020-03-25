package com.example.pass.activities.chooseFile.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.activities.MainActivity;
import com.example.pass.util.TimeFormatTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<FileHolder> {
    private Context context;

    private List<File> fileList = new ArrayList<>();

    public FileListAdapter(Context context){
        this.context = context;
    }

    public void setData(List<File> list){
        this.fileList.clear();
        this.fileList.addAll(list);
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_choosefile_file,parent,false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, int position) {
        File file= fileList.get(position);
        holder.tv_filename.setText(file.getName());
        holder.tv_createtime.setText(TimeFormatTools.timeStamp2Date(file.lastModified()));

        holder.btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("path",file.getAbsolutePath());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }


    //不重写这个方法的话数据会混乱不堪
    public int getItemViewType(int position) {
        return position;
    }
}
