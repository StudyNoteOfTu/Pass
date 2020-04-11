package com.example.pass.activities.mainActivity.fragments.passFoldersFragment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.pass.R;
import com.example.pass.activities.TestShadeActivity;
import com.example.pass.activities.mainActivity.fragments.passFoldersFragment.bean.PassFolder;
import com.example.pass.activities.passOpenedActivity.view.PassOpenedActivity;

import java.util.ArrayList;
import java.util.List;

public class FolderGridAdapter extends BaseAdapter {

    private Context mContext;

    private List<PassFolder> mPassFolderList = new ArrayList<>();

    public void setData(List<PassFolder> passFolders){
        mPassFolderList.clear();
        mPassFolderList.addAll(passFolders);
    }

    public FolderGridAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return mPassFolderList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPassFolderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_grid_pass_folder,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //显示数据
        viewHolder.tv_title.setText(mPassFolderList.get(position).getTitle());
        viewHolder.tv_type.setText(mPassFolderList.get(position).getType());
        viewHolder.item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, TestShadeActivity.class);
//                intent.putExtra("path",mPassFolderList.get(position).getPath()+"/final/final.xml");
                Intent intent = new Intent(mContext, PassOpenedActivity.class);
                intent.putExtra("path",mPassFolderList.get(position).getPath());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        CardView item_container;
        TextView tv_title;
        TextView tv_type;

        ViewHolder(View itemView){
            this.item_container = itemView.findViewById(R.id.item_container);
            this.tv_title = itemView.findViewById(R.id.tv_title);
            this.tv_type = itemView.findViewById(R.id.tv_type);
        }
    }
}
