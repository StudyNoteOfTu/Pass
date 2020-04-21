package com.example.pass.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pass.R;
import com.example.pass.util.spans.customSpans.MyStyleSpan;
import com.example.pass.util.spans.customSpans.MyUnderlineSpan;
import com.example.pass.util.spans.enumtype.CustomTypeEnum;
import com.example.pass.view.psEditText.PSEditText;
import com.example.pass.view.psEditText.model.StyleVm;

public class TestEditTextActivity extends AppCompatActivity {


    String content = "三四集，又是信息量不让人活的节奏，官家各种意义上的长大了，朝堂拉锯战暗潮涌动，感情多线虐恋初见端倪，大宋名臣长卷更是流光溢彩，巨星辈出，只待北辰，共助盛世。\n" +
            "\n" +
            "1.承接上集，“才成绿衣郎，便失赤子心”辛辣直刺，韩琦的“未忘初心”却真挚不疑，君臣相得由此开端。叙述中补齐梁家惨事，官家肉眼可见的“恍惚”-“惊疑”-“自责”，层层情绪转得人心疼，独处练字时的扶腕，比大开大阖的放悲，更摧心肠，又更侧写了官家的体弱，和心情不好就写字的习惯。\n" +
            "\n" +
            "2.说是官家戏韩琦，莫不如说是试韩琦，更兼试朝臣，科举进得少年郎，那何日幼帝可亲征？这两集官家再试三试，步步前逼，温言笑语中，蓄势夺权，实在过瘾。\n" +
            "\n" +
            "3.再试，是辽使位次之争，明面争的是外事细节，暗处争的是决策主导。\n" +
            "\n" +
            "程大人的锱铢必较，颇有现代外交思想，然而下意识直言打脸，即使是士大夫的职责，也掩盖不了对官家本人的不敬，更何况疑似后党身份，更在官家心中减分。\n" +
            "\n" +
            "因此官家一怒起身，虽在拾级而下过程中调整情绪，走到群臣中稳住反击，佯";


    PSEditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_edittext);

        initViews();
        initEvent();
    }



    private void initViews() {
        mEditText = findViewById(R.id.editText);

        SpannableStringBuilder sb =new SpannableStringBuilder(content);
        sb.setSpan(new MyStyleSpan(Typeface.ITALIC),30,50, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new MyStyleSpan(Typeface.BOLD),60,70, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new MyUnderlineSpan(),10,25, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

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
