package com.example.pass.view.popWindows.fontstylePopWindows;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.pass.R;
import com.example.pass.view.popWindows.fontstylePopWindows.bean.TextTypeface;
import com.example.pass.view.popWindows.fontstylePopWindows.impls.OnTypefaceChangeListener;

import java.util.ArrayList;
import java.util.List;

public class TtfGridAdapter extends BaseAdapter {

    private Context mContext;

    private List<TextTypeface> mTextTypefaceList = new ArrayList<>();


    private OnTypefaceChangeListener mOnTypefaceChangeListener;

    public void setData(List<TextTypeface> textTypefaceList, OnTypefaceChangeListener listener) {
        mTextTypefaceList.clear();
        mTextTypefaceList.addAll(textTypefaceList);
        mOnTypefaceChangeListener = listener;
        notifyDataSetChanged();

    }

    public TtfGridAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mTextTypefaceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTextTypefaceList.get(position);
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_popwindow_text_choose, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        //显示数据
        viewHolder.tv_text.setText(mTextTypefaceList.get(position).getText());
        if (!TextUtils.isEmpty(mTextTypefaceList.get(position).getTtf_path())) {
            viewHolder.tv_text.setTypeface(Typeface.createFromFile(mTextTypefaceList.get(position).getTtf_path()));
        }
        viewHolder.item_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnTypefaceChangeListener != null) {
                    mOnTypefaceChangeListener.onTypefaceChange(mTextTypefaceList.get(position).getTtf_path());
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        CardView item_container;
        TextView tv_text;

        ViewHolder(View itemView) {
            this.item_container = itemView.findViewById(R.id.item_container);
            this.tv_text = itemView.findViewById(R.id.tv_text);
        }
    }
}
