package com.example.pass.recyclerentry.selectTitle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pass.R;
import com.example.pass.util.FileUtil;
import com.example.pass.recyclerentry.selectTitle.bean.LineHolder;
import com.example.pass.recyclerentry.selectTitle.bean.LineItem;
import com.example.pass.view.datacontainedViews.DataContainedImageView;
import com.example.pass.view.datacontainedViews.DataContainedLinearLayout;
import com.example.pass.view.swipeViews.OnSwipeItemOpenListener;
import com.example.pass.view.popWindows.TitlePopWindow;

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
        int[] location = new int[2];
        v.getLocationOnScreen(location);

        Log.d(TAG,"v.x = "+location[0]+" ,v.y = "+location[1]+" ,v.width = "+v.getMeasuredWidth()+" ,v.height = "+v.getMeasuredHeight());
        if (v instanceof DataContainedLinearLayout){
            View childView = ((DataContainedLinearLayout)v).getChildAt(1);
            if (childView!=null){
                int[] childLocation = new int[2];
                childView.getLocationOnScreen(childLocation);
                Log.d(TAG,"Get Child[3] v.x = "+childLocation[0]+" ,v.y = "+childLocation[1]);
                showPopupWindow((DataContainedLinearLayout)v,childView);
            }


        }
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
        final LineHolder lineHolder = (LineHolder)holder;
        LineItem lineItem = items.get(position);

        //初始化

        if (!lineHolder.swipeItemLayout.isHasInit()){
            //设置初始是否张开
            lineHolder.swipeItemLayout.initOpenState(false);
//            changeStateToOpen(lineHolder,position);
            lineHolder.swipeItemLayout.setHasInit(true);
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
                    Log.d("DataContainedLinearLayout","isOpen");
                    changeStateToOpen(lineHolder, position);
                }else{
                    items.get(position).setIgnored(false);
                    changeStateToClose(lineHolder, position);
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
        TitlePopWindow window = new TitlePopWindow(context);
        View contentView = window.getContentView();
        ///需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(makeDropDownMeasureSpec(window.getWidth()),
                makeDropDownMeasureSpec(window.getHeight()));
        int offsetX = 0;
        int offsetY = -(window.getContentView().getMeasuredHeight()+childView.getHeight())/2;
        PopupWindowCompat.showAsDropDown(window,childView,offsetX,offsetY,Gravity.END);
        //有问题
//        window.showBackgroundAnimator();
        window.setOnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.tv_h1:
                        Toast.makeText(context, "h1", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
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
