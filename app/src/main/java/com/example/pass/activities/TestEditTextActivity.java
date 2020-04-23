package com.example.pass.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pass.R;
import com.example.pass.configs.RequestCodeConfig;
import com.example.pass.util.PhotoUtil;
import com.example.pass.util.spanUtils.SpanToXmlUtil;
import com.example.pass.util.spans.customSpans.MyHighLightColoSpan;
import com.example.pass.util.spans.customSpans.MyNormalSpan;
import com.example.pass.util.spans.customSpans.MyStyleSpan;
import com.example.pass.util.spans.customSpans.MyUnderlineSpan;
import com.example.pass.util.spans.enumtype.CustomTypeEnum;
import com.example.pass.view.psEditText.PSEditText;
import com.example.pass.view.psEditText.model.StyleVm;

import java.util.List;

public class TestEditTextActivity extends AppCompatActivity {

    private static final String TAG = "TestEditTextActivity";

    String content = "三四集，又是信息量不让人活的节奏，官家各种意义上的长大了，朝堂拉锯战暗潮涌动，感情多线虐恋初见端倪，大宋名臣长卷更是流光溢彩，巨星辈出，只待北辰，共助盛世。";


    PSEditText mEditText;

    Button btn_test_xml;

    Button btn_insert_pic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_edittext);

        initViews();
        initEvent();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RequestCodeConfig.PICTURE_GALLERY && resultCode == RESULT_OK){
            String path = PhotoUtil.getRealPathWithResultIntent(this,data);
            Log.d(TAG,"onActivityResult, path = "+path);
            if (!TextUtils.isEmpty(path)){
                handleReturnedPicPath(path);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void handleReturnedPicPath(String path) {
        mEditText.getPSUtils().insertImage("aaaa",path);
    }

    private void initViews() {
        mEditText = findViewById(R.id.editText);

        btn_insert_pic = findViewById(R.id.btn_insert_pic);
        btn_insert_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoUtil.getGalleryPictureWithResultByIntent(TestEditTextActivity.this);
            }
        });

        btn_test_xml = findViewById(R.id.btn_test_xml);
        btn_test_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> xml =  SpanToXmlUtil.editableToXml(mEditText.getEditableText());
                for (String s : xml) {
                    Log.d(TAG,s);
                }
            }
        });

        SpannableStringBuilder sb =new SpannableStringBuilder(content);
        sb.setSpan(new MyNormalSpan(),0,2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.setSpan(new MyNormalSpan(),2,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.setSpan(new MyNormalSpan(),5,sb.length(),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        sb.setSpan(new MyStyleSpan(Typeface.BOLD),0,2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.setSpan(new MyHighLightColoSpan(Color.BLUE),2,5,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        mEditText.setText(sb);
    }


    private void initEvent() {
        initBoldEvent();

        initItalicEvent();

        initUnderLineEvent();
    }

    private void initUnderLineEvent() {
        StyleVm styleVm = new StyleVm.Builder()
                .setType(CustomTypeEnum.UNDERLINE)
                .setIvIcon(findViewById(R.id.img_underline))
                .setIconLightResId(R.drawable.ic_launcher_foreground)
                .setIconNormalResId(R.drawable.ic_launcher_background)
                .build();
        mEditText.initStyleButton(styleVm);
    }

    private void initItalicEvent() {
        StyleVm styleVm = new StyleVm.Builder()
                .setType(CustomTypeEnum.ITALIC)
                .setIvIcon(findViewById(R.id.img_italic))
                .setIconLightResId(R.drawable.ic_launcher_foreground)
                .setIconNormalResId(R.drawable.ic_launcher_background)
                .build();
        mEditText.initStyleButton(styleVm);
    }

    private void initBoldEvent() {
        StyleVm styleVm = new StyleVm.Builder()
                .setType(CustomTypeEnum.BOLD)
                .setIvIcon(findViewById(R.id.img_bold))
                .setIconLightResId(R.drawable.ic_launcher_foreground)
                .setIconNormalResId(R.drawable.ic_launcher_background)
                .build();
        mEditText.initStyleButton(styleVm);
    }
}
