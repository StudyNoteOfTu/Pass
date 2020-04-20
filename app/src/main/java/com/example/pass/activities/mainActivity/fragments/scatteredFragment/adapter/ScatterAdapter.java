package com.example.pass.activities.mainActivity.fragments.scatteredFragment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.activities.mainActivity.fragments.scatteredFragment.bean.ScatterItem;
import com.example.pass.activities.mainActivity.fragments.scatteredFragment.bean.ScatterViewHolder;
import com.example.pass.view.datacontainedViews.DataContainedLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ScatterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<ScatterItem> items = new ArrayList<>();

    public void setData(List<ScatterItem> list){
        this.items.clear();
        this.items.addAll(list);
    }

    public ScatterAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_scatter_item,null,false);
        RecyclerView.ViewHolder holder = new ScatterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScatterViewHolder scatterViewHolder = (ScatterViewHolder) holder;
        ScatterItem scatterItem = items.get(position);

        scatterViewHolder.tv_date.setText(scatterItem.getDate());
        scatterViewHolder.tv_hasTitle.setText(scatterItem.getTitle());
        scatterViewHolder.tv_from.setText(scatterItem.getFrom());
        scatterViewHolder.ll_data_contained.setIndexInAdapterList(position);
        scatterViewHolder.tv_content.setText(scatterItem.getPreviewText());

        scatterViewHolder.ll_data_contained.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getItemViewType(int position){
        return position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_data_contained:
                int position = ((DataContainedLinearLayout)v).getIndexInAdapterList();
                String filePath = items.get(position).getFilePath();
                break;

            default:
                break;
        }
    }
}
