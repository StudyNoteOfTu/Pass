package com.example.pass.activities.passOpenedActivity.bean.TopNumOver1;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeItemGroup;
import com.example.pass.R;

import java.util.List;

public class ItemH3 extends TreeItemGroup<HBean.H4.H3> {

    @Override
    public int getLayoutId() {
        return R.layout.item_expandable_h3;
    }

    @Nullable
    @Override
    protected List<TreeItem> initChild(HBean.H4.H3 data) {
        return ItemHelperFactory.createItems(data.h2List,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_title,getData().h3Text.toString());
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, RecyclerView.LayoutParams layoutParams, int position) {
        super.getItemOffsets(outRect, layoutParams, position);
        outRect.left = getData().marginLeftLevel*100+20;
    }

}
