package com.example.pass.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.pass.R;
import com.example.pass.recyclerentry.selectTitle.LineAdapter;
import com.example.pass.util.FormatTools;
import com.example.pass.util.officeUtils.DocxUtil;
import com.example.pass.util.officeUtils.MyXmlReader;
import com.example.pass.util.officeUtils.PPTX.PptxSlideEntry;
import com.example.pass.util.officeUtils.PPTX.PptxSlideEntryUtil;
import com.example.pass.util.officeUtils.PPTX.PptxUtil;
import com.example.pass.util.spanUtils.SpanToXmlUtil;
import com.example.pass.util.spanUtils.XmlToSpanUtil;
import com.example.pass.util.spans.callbacks.ClickMovementMethodCallback;
import com.example.pass.util.spans.movementMethods.ClickableLinkMovementMethod;
import com.example.pass.view.SwipeItemLayout;


import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testPPT();

    }


    private void testDoc() {
        String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();


//        String ppt = sdcard_path+"/tencent/QQfile_recv/新版马克思主义基本原理复习提纲20191204.pptx";


        String doc_path = sdcard_path + "/tencent/QQfile_recv/2019新闻二学位硬件基础复习(1).docx";

        DocxUtil docxUtil = new DocxUtil(doc_path, "SpanEditTextDemoDocxUtil", "test");

        MyXmlReader myXmlReader = new MyXmlReader();
        String content = myXmlReader.fileToString(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SpanEditTextDemoDocxUtil/test.xml");

        //解析xml并合成editable呈现在textview上
        XmlToSpanUtil xmlToSpanUtil = new XmlToSpanUtil();
        List<SpannableStringBuilder> editables = xmlToSpanUtil.xmlToEditable(this, content);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < editables.size(); i++) {
            sb.append(editables.get(i));
        }
//        ((TextView)findViewById(R.id.textview)).setMovementMethod(new ScrollingMovementMethod());
        ClickableLinkMovementMethod clickableLinkMovementMethod = new ClickableLinkMovementMethod();
        clickableLinkMovementMethod.setCallback(new ClickMovementMethodCallback() {
            @Override
            public void onClicked(Drawable drawable) {
                ((SubsamplingScaleImageView) findViewById(R.id.bigview)).setImage(ImageSource.bitmap(FormatTools.getInstance().drawable2Bitmap(drawable)));
            }
        });

        ((TextView) findViewById(R.id.textview)).setMovementMethod(clickableLinkMovementMethod);
        ((TextView) findViewById(R.id.textview)).setText(sb);

        List<String> list = myXmlReader.getLineList(content);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new LineAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));    //子项监听器
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setLineData(list);
        adapter.notifyDataSetChanged();

    }

    private void testPPT() {
        String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String ppt = sdcard_path + "/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/新版马克思主义基本原理复习提纲20191204(2).pptx";

        PptxUtil.readPptx(ppt,"test","test");

        MyXmlReader myXmlReader = new MyXmlReader();
        String content = myXmlReader.fileToString(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/test.xml");

        //解析xml并合成editable呈现在textview上
        XmlToSpanUtil xmlToSpanUtil = new XmlToSpanUtil();
        List<SpannableStringBuilder> editables = xmlToSpanUtil.xmlToEditable(this, content);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < editables.size(); i++) {
            sb.append(editables.get(i));
        }
//        ((TextView)findViewById(R.id.textview)).setMovementMethod(new ScrollingMovementMethod());
        ClickableLinkMovementMethod clickableLinkMovementMethod = new ClickableLinkMovementMethod();
        clickableLinkMovementMethod.setCallback(new ClickMovementMethodCallback() {
            @Override
            public void onClicked(Drawable drawable) {
                ((SubsamplingScaleImageView) findViewById(R.id.bigview)).setImage(ImageSource.bitmap(FormatTools.getInstance().drawable2Bitmap(drawable)));
            }
        });

        ((TextView) findViewById(R.id.textview)).setMovementMethod(clickableLinkMovementMethod);

        ((TextView) findViewById(R.id.textview)).setText(sb);

        //测试sb
        SpanToXmlUtil.editableToXml(sb);

        List<String> list = myXmlReader.getLineList(content);

        Log.d("test","list.size() = "+list.size());

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new LineAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(this));    //子项监听器
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setLineData(list);
        adapter.notifyDataSetChanged();
    }
}
