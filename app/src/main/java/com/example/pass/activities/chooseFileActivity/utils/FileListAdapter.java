package com.example.pass.activities.chooseFileActivity.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.activities.MainActivity;
import com.example.pass.activities.TestShadeActivity;
import com.example.pass.activities.analyseOfficeActivity.view.AnalyseOfficeActivity;
import com.example.pass.util.TimeFormatTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "FileListAdapter";

    private Context context;


    private List<StateContainedFileItem> fileList = new ArrayList<>();

    private View.OnClickListener clickListener;

    private void refresh(){
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.clickListener = listener;
    }

    public FileListAdapter(Context context){
        this.context = context;
    }

    public void setData(List<StateContainedFileItem> list){
        this.fileList.clear();
        this.fileList.add(null);
        this.fileList.addAll(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_choose_file_top,parent,false);
            return new TopHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_choose_file_file,parent,false);
            return new FileHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        if (viewHolder instanceof FileHolder) {
            StateContainedFileItem stateContainedFileItem = fileList.get(position);

            FileHolder holder = (FileHolder) viewHolder;

            //初始化展开
            setShowMore(holder,stateContainedFileItem.isNeedMore(),position);

            File file = stateContainedFileItem.getFile();
            holder.tv_filename.setText(file.getName());
            holder.tv_createtime.setText(TimeFormatTools.timeStamp2Date(file.lastModified()));

            holder.ll_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setShowMore(holder, false,position);
                }
            });

            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setShowMore(holder, true,position);
                }
            });

            holder.btn_slide_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.btn_slide_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, AnalyseOfficeActivity.class);
//                    Intent intent = new Intent(context, MainActivity.class);
                    Intent intent = new Intent(context,TestShadeActivity.class);
                    intent.putExtra("path", file.getAbsolutePath());
                    context.startActivity(intent);
                }
            });
        }else if (viewHolder instanceof TopHolder){
            TopHolder topHolder = (TopHolder) viewHolder;
            if (clickListener != null) {
                topHolder.btn_from_all.setOnClickListener(clickListener);
                topHolder.btn_from_qq.setOnClickListener(clickListener);
                topHolder.btn_from_wx.setOnClickListener(clickListener);
                topHolder.btn_type_all.setOnClickListener(clickListener);
                topHolder.btn_type_docx.setOnClickListener(clickListener);
                topHolder.btn_type_pptx.setOnClickListener(clickListener);
            }
        }
    }



    @Override
    public int getItemCount() {
        return fileList.size();
    }


    //不重写这个方法的话数据会混乱不堪
    //再加以混乱
    public int getItemViewType(int position) {
//        if (position >= fileList.size()-1) return position;
//        File file = fileList.get(position);
//        if (file == null) return position;
//        String timeStamp = String.valueOf(file.lastModified());
//        String dealtTime = position+timeStamp.substring(timeStamp.length()-6);
//        return Integer.parseInt(dealtTime);
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setShowMore(FileHolder fileHolder, boolean isNeedShowMore,int position){
        if (isNeedShowMore){
            fileHolder.ll_slide_buttons.setVisibility(View.VISIBLE);
            fileHolder.btn_more.setVisibility(View.GONE);
            fileList.get(position).setNeedMore(true);
        }else{
            fileHolder.ll_slide_buttons.setVisibility(View.GONE);
            fileHolder.btn_more.setVisibility(View.VISIBLE);
            fileList.get(position).setNeedMore(false);
        }
    }
}
