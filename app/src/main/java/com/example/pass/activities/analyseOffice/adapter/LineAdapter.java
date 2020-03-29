package com.example.pass.activities.analyseOffice.adapter;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.activities.analyseOffice.bean.LineHolder;
import com.example.pass.activities.analyseOffice.bean.LineItem;
import com.example.pass.activities.analyseOffice.view.impls.Updatable;
import com.example.pass.util.officeUtils.FileUtil;
import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.view.DataContainedImageView;
import com.example.pass.view.DataContainedLinearLayout;
import com.example.pass.view.OnSwipeItemOpenListener;
import com.example.pass.view.popWindows.TitlePopWindow;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener ,View.OnLongClickListener{

    private final static String TAG = "LineAdapter";


    private Context context;

    private Updatable updatable;

    private List<LineItem> items = new ArrayList<>();

    public void setData(List<LineItem> list){
        this.items.clear();
        this.items.addAll(list);
    }

    public LineAdapter(Context context,Updatable updatable){
        this.context = context;
        this.updatable = updatable;
    }


    public LineAdapter(Context context, List<String> lineData){
        this.context = context;
        LineItem lineItem;
        List<LineItem> items = new ArrayList<>();
        for (String str:lineData){
            lineItem = new LineItem();
            //将str中的信息提取出来，存入lineItem
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
                if (!item.isPic()) {
                    if (item.isSelect()) {
                        item.setSelect(false);
                    } else {
                        item.setSelect(true);
                    }
                    notifyChangedAndUpdate(index);
                }

                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        if (v instanceof DataContainedLinearLayout){
            int index = ((DataContainedLinearLayout) v).getIndexInAdapterList();
            LineItem item = items.get(index);
            //是文字？可点击？是展开？
            if (!item.isPic() && item.isSelect() && !item.isIgnored()){
                View childView = ((DataContainedLinearLayout)v).getChildAt(1);
                if (childView!=null){
                    int[] childLocation = new int[2];
                    childView.getLocationOnScreen(childLocation);
                    Log.d(TAG,"Get Child[3] v.x = "+childLocation[0]+" ,v.y = "+childLocation[1]);
                    showPopupWindow((DataContainedLinearLayout)v,childView);
                }
            }else if (item.isPic()){
                //图片不可作为标题
            }else if (item.isIgnored()){
                //左滑收起来的不可长按
            }
        }
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
        final LineHolder lineHolder = (LineHolder)holder;
        LineItem lineItem = items.get(position);

        //初始化

//        if (!lineHolder.swipeItemLayout.isHasInit()){
//            //设置初始是否张开
//            lineHolder.swipeItemLayout.initOpenState(false);
////            changeStateToOpen(lineHolder,position);
//            lineHolder.swipeItemLayout.setHasInit(true);
//        }

        if (lineItem.isIgnored()){
            changeStateToOpen(lineHolder,position);
        }else{
            changeStateToClose(lineHolder,position);
        }

        if (lineItem.isSelect()){
            //如果选中就亮
            lineHolder.imageSelect.setBackgroundResource(R.drawable.ic_launcher_foreground);

            switch (lineItem.getTitleLevel()){
                case 1:
                    lineHolder.imageTitle.setBackgroundColor(Color.GREEN);
                    break;
                case 2:
                    lineHolder.imageTitle.setBackgroundColor(Color.YELLOW);
                    break;
                case 3:
                    lineHolder.imageTitle.setBackgroundColor(Color.RED);
                    break;
                case 4:
                    lineHolder.imageTitle.setBackgroundColor(Color.BLUE);
                    break;
            }
        }else{
            lineHolder.imageSelect.setBackgroundResource(R.drawable.ic_launcher_background);
            lineHolder.imageTitle.setBackgroundColor(Color.TRANSPARENT);
        }

        lineHolder.imageSelect.setOnClickListener(this);
//        lineHolder.imageSelect.setOnLongClickListener(this);
        lineHolder.linearLayout.setOnLongClickListener(this);

        lineHolder.imageSelect.setIndexInAdapterList(position);
        lineHolder.linearLayout.setIndexInAdapterList(position);

        lineHolder.linearLayout.setOnSwipeItemOpenListener(new OnSwipeItemOpenListener() {
            @Override
            public void onOpenStateChanged(boolean isOpen) {
                if (isOpen){
                    items.get(position).setIgnored(true);

                }else{
                    items.get(position).setIgnored(false);
                }

                notifyChangedAndUpdate(position);
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

    private void changeStateToClose(LineHolder lineHolder, int position) {
        if (lineHolder.tvLineContent!=null){
            lineHolder.tvLineContent.getPaint().setFlags(lineHolder.tvLineContent.getPaintFlags()&~Paint.STRIKE_THRU_TEXT_FLAG);
            lineHolder.tvLineContent.setTextColor(Color.BLACK);
        }
        if (!TextUtils.isEmpty(items.get(position).getPicPath())){
            lineHolder.linearLayout.setShowingPicture(true);
            lineHolder.imagePicture.setImageAlpha(255);

        }
    }

    private void changeStateToOpen(LineHolder lineHolder, int position) {
        if (lineHolder.tvLineContent!=null){
            lineHolder.tvLineContent.getPaint().setFlags(lineHolder.tvLineContent.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            lineHolder.tvLineContent.setTextColor(Color.GRAY);
        }
        if (!TextUtils.isEmpty(items.get(position).getPicPath())){
            lineHolder.linearLayout.setShowingPicture(true);
            lineHolder.imagePicture.setImageAlpha(50);
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public int getItemViewType(int position){
        return position;
    }


    public void showPopupWindow(DataContainedLinearLayout linearLayout,View childView){
        int index = linearLayout.getIndexInAdapterList();

        TitlePopWindow window = new TitlePopWindow(context);
        View contentView = window.getContentView();
        ///需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(window.getWidth()),
                makeDropDownMeasureSpec(window.getHeight()));
        int offsetX = 0;
        int offsetY = -(window.getContentView().getMeasuredHeight()+childView.getHeight())/2;
        PopupWindowCompat.showAsDropDown(window,childView,offsetX,offsetY, Gravity.END);
        //有问题
//        window.showBackgroundAnimator();
        window.setOnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_h1:
                        items.get(index).setTitleLevel(1);
                        break;
                    case R.id.tv_h2:
                        items.get(index).setTitleLevel(2);
                        break;
                    case R.id.tv_h3:
                        items.get(index).setTitleLevel(3);
                        break;
                    case R.id.tv_h4:
                        items.get(index).setTitleLevel(4);
                        break;

                    default:
                        break;
                }
                notifyChangedAndUpdate(index);
            }
        });
    }


    private void notifyChangedAndUpdate(int position){
        notifyDataSetChanged();

        //update item数据到model
        LineItem lineItem = items.get(position);
        if (lineItem.isIgnored()){
            updatable.updateLineInfo(XmlTags.getKey_ignore(),XmlTags.getTitle_level(1),position);
        }else{
            if (lineItem.isSelect()){
                updatable.updateLineInfo(XmlTags.getKey_title(),XmlTags.getTitle_level(lineItem.getTitleLevel()),position);
            }else{
                updatable.updateLineInfo(XmlTags.getNone(),XmlTags.getNone(),position);
            }
        }

    }



    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

}
