package com.example.pass.recyclerentry.selectTitle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.util.officeUtils.FileUtil;
import com.example.pass.recyclerentry.selectTitle.bean.LineHolder;
import com.example.pass.recyclerentry.selectTitle.bean.LineItem;
import com.example.pass.view.DataContainedImageView;
import com.example.pass.view.OnSwipeItemOpenListener;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener ,View.OnLongClickListener{

    private final static String TAG = "LineAdapter";

    private Context context;

    private List<LineItem> items = new ArrayList<>();

    public void setData(List<LineItem> list){
        this.items.clear();
        this.items.addAll(list);
    }

    public LineAdapter(Context context){
        this.context = context;
    }


    public LineAdapter(Context context, List<String> lineData){
        this.context = context;
        LineItem lineItem;
        List<LineItem> items = new ArrayList<>();
        for (String str:lineData){
            lineItem = new LineItem();
            lineItem.setDataWithXml(str);
            items.add(lineItem);
        }
        setData(items);
    }

    public void setLineData(List<String> lineData){
        LineItem lineItem;
        List<LineItem> items = new ArrayList<>();
        for (String str:lineData){
            lineItem = new LineItem();
            Log.d(TAG,"line data is" + str);
            lineItem.setDataWithXml(str);
            items.add(lineItem);
        }
        setData(items);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_select:
                int index = ((DataContainedImageView)v).getIndexInAdapterList();
                LineItem item = items.get(index);
                if (item.isSelect()){
                    item.setSelect(false);
                    item.setTitleLevel(0);
                    v.setBackgroundResource(R.drawable.ic_launcher_background);
                }else{
                    item.setSelect(true);
                    item.setTitleLevel(1);
                    v.setBackgroundResource(R.drawable.ic_launcher_foreground);
                }
                Toast.makeText(context, "短按"+index, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(context, "长按", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_line,null,false);
        RecyclerView.ViewHolder holder = new LineHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((LineHolder)holder).imageSelect.setOnClickListener(this);
        ((LineHolder)holder).imageSelect.setOnLongClickListener(this);
        ((LineHolder)holder).imageSelect.setIndexInAdapterList(position);
        ((LineHolder)holder).linearLayout.setIndexInAdapterList(position);
        ((LineHolder)holder).linearLayout.setOnSwipeItemOpenListener(new OnSwipeItemOpenListener() {
            @Override
            public void onOpenStateChanged(boolean isOpen) {
                if (isOpen){
                    items.get(position).setIgnored(true);
                    Log.d("DataContainedLinearLayout","isOpen");
                    if (((LineHolder)holder).tvLineContent!=null){
                        ((LineHolder)holder).tvLineContent.getPaint().setFlags(((LineHolder)holder).tvLineContent.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                        ((LineHolder)holder).tvLineContent.setTextColor(Color.GRAY);
                    }
                    if (!TextUtils.isEmpty(items.get(position).getPicPath())){
                        ((LineHolder)holder).linearLayout.setShowingPicture(true);
                        ((LineHolder)holder).imagePicture.setImageAlpha(50);
                    }
                }else{
                    items.get(position).setIgnored(false);
                    if (((LineHolder)holder).tvLineContent!=null){
                        ((LineHolder)holder).tvLineContent.getPaint().setFlags(((LineHolder)holder).tvLineContent.getPaintFlags()&~Paint.STRIKE_THRU_TEXT_FLAG);
                        ((LineHolder)holder).tvLineContent.setTextColor(Color.BLACK);
                    }
                    if (!TextUtils.isEmpty(items.get(position).getPicPath())){
                        ((LineHolder)holder).linearLayout.setShowingPicture(true);
                        ((LineHolder)holder).imagePicture.setImageAlpha(255);

                    }
                }

            }
        });


        if (!TextUtils.isEmpty(items.get(position).getPicPath())){
            Log.d(TAG,"picture is not empty "+items.get(position).getPicPath());
            ((LineHolder)holder).imagePicture.setImageBitmap(FileUtil.getLocalBitmap(items.get(position).getPicPath()));
            ((LineHolder)holder).linearLayout.setShowingPicture(true);
        }else{
            Log.d(TAG,"picture is empty "+items.get(position).getPicPath());
            ((LineHolder)holder).imagePicture.setVisibility(View.GONE);
            ((LineHolder)holder).tvLineContent.setText(items.get(position).getText());
            ((LineHolder)holder).linearLayout.setShowingPicture(false);
        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getItemViewType(int position){
        return position;
    }


}
