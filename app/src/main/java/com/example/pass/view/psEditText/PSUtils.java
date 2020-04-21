package com.example.pass.view.psEditText;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pass.util.spanUtils.SpanToXmlUtil;
import com.example.pass.util.spans.customSpans.CustomSpan;
import com.example.pass.util.spans.customSpans.CustomSpanFactory;
import com.example.pass.util.spans.customSpans.MyForegroundColorSpan;
import com.example.pass.util.spans.customSpans.MyHighLightColoSpan;
import com.example.pass.util.spans.customSpans.MyImageSpan;
import com.example.pass.util.spans.customSpans.MyNormalSpan;
import com.example.pass.util.spans.customSpans.MyShadeSpan;
import com.example.pass.util.spans.customSpans.MyStrikethroughSpan;
import com.example.pass.util.spans.customSpans.MyStyleSpan;
import com.example.pass.util.spans.customSpans.MyUnderlineSpan;
import com.example.pass.util.spans.enumtype.CustomTypeEnum;
import com.example.pass.view.psEditText.model.StyleVm;
import com.example.pass.view.psEditText.utils.ClipboardUtil;
import com.example.pass.view.psEditText.utils.SoftKeyboardUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PSUtils {

    private static final String TAG = "PSUtils";

    private PSEditText mPSEditText;

    private Activity mActivity;

    /**
     * 标记支持注册了的样式
     */
    private Map<Integer, StyleVm> mPSTypeToVmMap = new HashMap<>();

    public PSUtils(Activity activity, PSEditText psEditText) {
        mActivity = activity;
        mPSEditText = psEditText;
        registerEvents();
    }

    private void registerEvents() {
        PSTextWatcher mTextWatcher = new PSTextWatcher(mPSEditText);
        mPSEditText.addTextWatcher(mTextWatcher);

        //监听光标位置变化
        mPSEditText.setOnSelectionChangedListener(new PSEditText.OnSelectionChangedListener() {

            @Override
            public void onChange(int curPos) {
                handleSelectionChanged(curPos);
            }
        });

        //监听删除按键
        mPSEditText.setBackspaceListener(new PSInputConnectionWrapper.BackspaceListener() {

            @Override
            public boolean onBackspace() {
                return handleDeleteKey();
            }
        });

        //为了兼容模拟器
        mPSEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_DEL == event.getKeyCode()
                        && event.getAction() == KeyEvent.ACTION_DOWN
                        && !SoftKeyboardUtil.isSoftShowing(mActivity)) {
                    //监听到删除键但是软键盘没弹出，可以基本断定是用模拟器
                    // TODO 也存在模拟器也会弹出软键盘的
                    return PSUtils.this.handleDeleteKey();
                }
                return false;
            }
        });

    }

    private boolean handleDeleteKey() {
        return false;
    }

    public void initStyleButton(StyleVm styleVm) {
        Integer type = styleVm.getType();
        mPSTypeToVmMap.put(type, styleVm);

        View clickedView = styleVm.getClickedView();
        if (clickedView == null) {
            clickedView = styleVm.getIvIcon();
        }

        clickedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPSEditText.isFocused()) {
                    //先重设亮暗
                    styleVm.setLight(!styleVm.isLight());
                    changeStyleBtnImage(styleVm);
                    changeStyleBtnText(styleVm);
                    //然后设置内容
                    toggleStyle(type);
                }
            }
        });
    }

    /**
     * 处理样式修改
     *
     * @param type 样式
     */
    private void toggleStyle(Integer type) {

        //在此之前 ，光标位置变化的时候就已经将 button 的图样刷新了

        //首先判断位置， 分为 单标、框选
        int start = mPSEditText.getSelectionStart();
        int end = mPSEditText.getSelectionEnd();
        //如果是单标，判断光标在哪里
        //如果是单标
        if (start == end) {
            //如果是单标
            //判断所在的地方有哪些Span（中后端）
            Map<Integer, StyleVm> stateMap = new HashMap<>();
            Editable editable = mPSEditText.getEditableText();
            CustomSpan[] customSpans = editable.getSpans(end, end, CustomSpan.class);
            int spanStart;
            int spanEnd;
            int stateType;
            for (CustomSpan customSpan : customSpans) {
                stateType = customSpan.getType();
                spanStart = editable.getSpanStart(customSpan);
                spanEnd = editable.getSpanEnd(customSpan);
                //判断是否在中后端
                if (spanStart < end && end <= spanEnd) {
                    //获得状态
                    if (mPSTypeToVmMap.get(stateType) != null) {
                        //存入这个位置原来的state
                        stateMap.put(stateType, mPSTypeToVmMap.get(type));
                    }
                }
            }
            //此时已经存好了这个位置所有的可编辑span
            //接下来不作操作？
            //TODO: 接下来操作还没写
        } else {
            //如果是框选
            //判断是否为文本
            if (!isText()) {
                return;
            }
            //如果纯文本，没有图片在里面
            //开始修改所有span
            //1. 前后切分
            if (mPSTypeToVmMap.get(type) == null) return;
            handleSelectBoundary(mPSTypeToVmMap.get(type));
            //2. remove掉所有同类span
            //3. 如果点亮，各个节点插入此类Span，如果不点亮，不插入
            //注意，只删除spanStart在选框中的同类Span
            Editable editable = mPSEditText.getEditableText();
            CustomSpan[] customSpans = editable.getSpans(start, end, CustomSpan.class);
            int spanStart;
            int spanEnd;
            //拿到所有节点的下标，用不重复的HashMap存储
            Map<Integer, Integer> innerIndexMap = new HashMap<>();

            //先预处理下标位置
            for (CustomSpan customSpan : customSpans) {
                spanStart = editable.getSpanStart(customSpan);
                spanEnd = editable.getSpanEnd(customSpan);
                //只要在内部的：
                if (start < spanStart && spanStart < end){
                    innerIndexMap.put(spanStart,0);
                }
                if (start < spanEnd && spanEnd < end){
                    innerIndexMap.put(spanEnd,0);
                }
            }

            //开始处理插入span的设置
            List<Integer> innerIndexList = new ArrayList<>(innerIndexMap.keySet());
            //自然升序
            Collections.sort(innerIndexList);

            //获取到所有内部下标后，开始真正处理
            //如果点亮， 全删除同类，而后全添加同类
            //如果置灰， 有就删除同类，没有就没有同类
            //判断是点亮还是置灰
            if (mPSTypeToVmMap.get(type).isLight()) {
                //如果是点亮
                for (CustomSpan customSpan : customSpans) {
                    //同类全删除
                    if (customSpan.getType()== type){
                        //同类，删除
                        editable.removeSpan(customSpan);
                    }
                }
                //全部删除过后，全添加
                int beginIndex = start;
                CustomSpan customSpan;
                for (Integer integer : innerIndexList) {
                    //可以自己根据StyleVm来new一个出来
                    customSpan = CustomSpanFactory.getCustomSpanInstance(getSpanClassFromType(type));
                    editable.setSpan(customSpan,beginIndex,integer,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                    beginIndex = beginIndex;
                }
                customSpan = CustomSpanFactory.getCustomSpanInstance(getSpanClassFromType(type));
                editable.setSpan(customSpan,beginIndex,end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                //同类全添加，完成
            }else{
                //如果置灰，全部删除即可
                for (CustomSpan customSpan : customSpans) {
                    //同类全删除
                    if (customSpan.getType()== type){
                        //同类，删除
                        editable.removeSpan(customSpan);
                    }
                }
            }



        }


    }

    /**
     * 处理选框便捷
     *
     * @param styleVm
     */
    private void handleSelectBoundary(StyleVm styleVm) {
        Editable editable = mPSEditText.getEditableText();
        int start = mPSEditText.getSelectionStart();
        int end = mPSEditText.getSelectionEnd();
        CustomSpan[] customSpans = editable.getSpans(start, end, CustomSpan.class);
        if (customSpans.length <= 0) return;
        //判断谁的start在前外面，谁的end在后外面
        int spanStart;
        int spanEnd;
        for (CustomSpan customSpan : customSpans) {
            spanStart = editable.getSpanStart(customSpan);
            spanEnd = editable.getSpanEnd(customSpan);
            //判断开始是否在前面
            if (spanStart < start) {
                //删去
                editable.removeSpan(customSpan);
                //插入Span
                editable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), spanStart, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                editable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), start, spanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            if (end < spanEnd) {
                //删去
                editable.removeSpan(customSpan);
                //插入
                editable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), spanStart, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                editable.setSpan(CustomSpanFactory.getCustomSpanInstance(customSpan), end, spanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }
        //此时已经前后切分
    }

    private CustomSpan getSpanClassFromType(Integer type) {
        switch (type) {
            case CustomTypeEnum.BOLD:
                return new MyStyleSpan(Typeface.BOLD);
            case CustomTypeEnum.ITALIC:
                return new MyStyleSpan(Typeface.ITALIC);
            case CustomTypeEnum.NORMAL:
                return new MyNormalSpan();
            case CustomTypeEnum.STRIKE_THROUGH:
                return new MyStrikethroughSpan();
            case CustomTypeEnum.UNDERLINE:
                return new MyUnderlineSpan();
            case CustomTypeEnum.SHADE_MODE:
                return new MyShadeSpan();
        }
        return null;
    }

    private boolean isText() {
        int curPosStart = mPSEditText.getSelectionStart();
        int curPosEnd = mPSEditText.getSelectionEnd();
        if (curPosStart == curPosEnd && curPosStart == 0) {
            return true;
        }
        Editable editable = mPSEditText.getEditableText();
        //如果有ImageSpan，则return false
        MyImageSpan[] imageSpans = editable.getSpans(curPosStart - 1, curPosEnd, MyImageSpan.class);

        return imageSpans.length <= 0;
    }


    /**
     * 处理光标的位置变化
     *
     * @param curPos 当前光标的位置
     */
    private void handleSelectionChanged(int curPos) {

        Log.d(TAG, "curPos = " + curPos);
        Editable editable = mPSEditText.getEditableText();
        if (editable.length() <= 0 && curPos <= 0) {
            //位置不对
            mPSEditText.requestFocus();
            mPSEditText.setSelection(0);
        }

        //修改各个按钮的状态
        handleStyleButtonStatus();

    }

    private void handleStyleButtonStatus() {
        //先将所有的按钮置灰
        clearStyleButtonStatus();

        //根据光标位置点亮对应的图标
        //如果当前是单标，判断在哪个Span的中间或者末端
        //首先获取到所有Span
        int start = mPSEditText.getSelectionStart();
        int end = mPSEditText.getSelectionEnd();

        //如果是单标
        if (start == end) {
            Editable editable = mPSEditText.getEditableText();
            //如果空文本，或者选在最前面
            if (editable.length() <= 0 || start <= 0) {
                mPSEditText.setSelection(0);
                //最前面不作任何效果变化
            }
            //如果不是空文本，判断在哪些Span的中间或者末端
            //先拿到它所处的Span,注意，我们只要它在谁的中间或者末端，头部不算
            CustomSpan[] customSpans = editable.getSpans(end, end, CustomSpan.class);
            int spanStart;
            int spanEnd;
            for (CustomSpan customSpan : customSpans) {
                Log.d(TAG, "curPos = " + end + " type = " + customSpan.getType() + " ,start = " + editable.getSpanStart(customSpan) + " ,end = " + editable.getSpanEnd(customSpan));
                spanStart = editable.getSpanStart(customSpan);
                spanEnd = editable.getSpanEnd(customSpan);
                //判断是否在中末端,如果在，则点亮对应type的图标
                if (spanStart < end && end <= spanEnd) {
                    //获取type,根据type点亮StyleVm
                    lightButton(customSpan.getType());
                }
            }

        }


        //如果当前是选择，不作效果变化


    }

    private void lightButton(int type) {
        Log.d(TAG, "light button type = " + type);

        StyleVm styleVm = mPSTypeToVmMap.get(type);
        if (styleVm != null) {
            //点亮
            styleVm.setLight(true);
            changeStyleBtnImage(styleVm);
            changeStyleBtnText(styleVm);

        } else {
            Log.d(TAG, "type = " + type + " ,vm is null");
        }

    }

    private void clearStyleButtonStatus() {
        for (StyleVm value : mPSTypeToVmMap.values()) {
            value.setLight(false);
            changeStyleBtnImage(value);
            changeStyleBtnText(value);
        }
    }

    private void changeStyleBtnText(StyleVm styleVm) {
        TextView tvTitle = styleVm.getTvTitle();
        if (tvTitle == null) {
            return;
        }

        tvTitle.setTextColor(
                styleVm.isLight() ? styleVm.getTitleLightColor() : styleVm.getTitleNormalColor()
        );
    }

    private void changeStyleBtnImage(StyleVm styleVm) {
        ImageView ivIcon = styleVm.getIvIcon();
        if (ivIcon == null) {
            return;
        }
        ivIcon.setImageResource(styleVm.isLight() ? styleVm.getIconLightResId() : styleVm.getIconNormalResId());
    }

    /**
     * 插入文本
     *
     * @param instance
     * @param selectionStart
     */
    public void insertStringIntoEditText(ClipboardUtil instance, int selectionStart) {


    }
}
