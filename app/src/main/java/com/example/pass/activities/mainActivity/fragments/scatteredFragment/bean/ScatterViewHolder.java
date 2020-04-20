package com.example.pass.activities.mainActivity.fragments.scatteredFragment.bean;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.view.datacontainedViews.DataContainedLinearLayout;

public class ScatterViewHolder extends RecyclerView.ViewHolder {

    public DataContainedLinearLayout ll_data_contained;

    public TextView tv_date;
    public TextView tv_hasTitle;
    public TextView tv_from;
    public TextView tv_content;

    public ScatterViewHolder(@NonNull View itemView) {
        super(itemView);
        ll_data_contained = itemView.findViewById(R.id.ll_data_contained);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_hasTitle = itemView.findViewById(R.id.tv_hasTitle);
        tv_from = itemView.findViewById(R.id.tv_from);
        tv_content = itemView.findViewById(R.id.tv_content);
    }
}
