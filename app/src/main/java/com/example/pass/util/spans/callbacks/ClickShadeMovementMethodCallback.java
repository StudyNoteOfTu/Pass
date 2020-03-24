package com.example.pass.util.spans.callbacks;

import com.example.pass.util.spans.customSpans.CustomSpan;
import com.example.pass.util.spans.customSpans.MyShadeSpan;

public interface ClickShadeMovementMethodCallback extends ClickCallBack {
    void onClicked(MyShadeSpan shadeSpan);
}
