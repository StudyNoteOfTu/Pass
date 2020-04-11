package com.example.pass.activities.passOpenedActivity.fragments;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baozi.treerecyclerview.adpater.TreeRecyclerAdapter;
import com.baozi.treerecyclerview.factory.ItemHelperFactory;
import com.baozi.treerecyclerview.item.TreeItem;
import com.example.pass.R;
import com.example.pass.activities.passOpenedActivity.bean.TopNumOver1.HBean;
import com.example.pass.base.ActionBarFragment;

import java.util.List;

public class FolderListFragment extends ActionBarFragment {

    RecyclerView recyclerView;
    TreeRecyclerAdapter adapter = new TreeRecyclerAdapter();

    @Override
    protected void initViews(View mContentView) {
        recyclerView = mContentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void setData(int top,Object object){
        List<TreeItem> items = null;
        switch(top){
            case 4:
                if (object instanceof HBean){
                    items = ItemHelperFactory.createItems(((HBean)object).h4List);
                }
                break;
            case 3:
                if (object instanceof HBean.H4){
                    items = ItemHelperFactory.createItems(((HBean.H4) object).h3List);
                }
                break;
            case 2:
                if (object instanceof HBean.H4.H3){
                    items = ItemHelperFactory.createItems(((HBean.H4.H3) object).h2List);
                }
                break;
        }

        if (items != null)adapter.getItemManager().replaceAllItem(items);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_pass_folder_list;
    }

    @Override
    public void switchTitle(String title) {

    }
}