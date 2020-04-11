package com.example.pass.activities.passOpenedActivity.bean.TopNumOver1;

import androidx.annotation.NonNull;

import com.baozi.treerecyclerview.base.ViewHolder;
import com.baozi.treerecyclerview.item.TreeItem;
import com.example.pass.R;

public class ItemH1 extends TreeItem<HBean.H4.H3.H2.H1> {


    @Override
    public int getLayoutId() {
        return R.layout.item_expandable_h1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder) {
        viewHolder.setText(R.id.tv_title,getData().h1Text.toString());
    }


}
