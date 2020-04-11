package com.example.pass.activities.passOpenedActivity.bean.TopNumOver1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.baozi.treerecyclerview.item.TreeItemGroup;
import com.example.pass.R;

import java.util.List;

public class ItemH2 extends TreeItemGroup<HBean.H4.H3.H2> {
    @Override
    public int getLayoutId() {
        return R.layout.item_expandable_h2;
    }

    @Nullable
    @Override
    protected List<TreeItem> initChild(HBean.H4.H3.H2 data) {
        return ItemHelperFactory.createItems(data.h1List,this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_title,getData().h2Text.toString());
    }
}
