package com.example.pass.view.psEditText;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

import com.example.pass.view.psEditText.model.StyleVm;
import com.example.pass.view.psEditText.utils.SoftKeyboardUtil;

import java.util.HashMap;
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
        mPSEditText.setOnSelectionChangedListener(new PSEditText.OnSelectionChangedListener(){

            @Override
            public void onChange(int curPos) {
                handleSelectionChanged(curPos);
            }
        });

        //监听删除按键
        mPSEditText.setBackspaceListener(new PSInputConnectionWrapper.BackspaceListener(){

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

    public void initStyleButton(StyleVm styleVm){
        Integer type = styleVm.getType();
        mPSTypeToVmMap.put(type,styleVm);

        View clickedView = styleVm.getClickedView();
        if (clickedView == null){
            clickedView = styleVm.getIvIcon();
        }

        clickedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPSEditText.isFocused()){
                    toggleStyle(type);
                }
            }
        });
    }

    private void toggleStyle(Integer type) {

    }

    private void handleSelectionChanged(int curPos) {

    }
}
