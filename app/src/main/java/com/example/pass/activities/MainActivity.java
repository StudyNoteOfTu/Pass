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
import com.example.pass.util.localFileUtils.LocalInfos;
import com.example.pass.util.localFileUtils.LocalOfficeFileUtils;
import com.example.pass.util.officeUtils.DocxUtil;
import com.example.pass.util.officeUtils.MyXmlReader;
import com.example.pass.util.officeUtils.PPTX.PptxUtil;
import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;
import com.example.pass.util.spanUtils.SpanToXmlUtil;
import com.example.pass.util.spanUtils.XmlToSpanUtil;
import com.example.pass.util.spans.callbacks.ClickImageMovementMethodCallback;
import com.example.pass.util.spans.movementMethods.ClickableLinkMovementMethod;
import com.example.pass.view.SwipeItemLayout;


import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    RecyclerView recyclerView;
    LineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testPPT();
//
        new Thread(new Runnable() {
            @Override
            public void run() {
//                LocalOfficeFileUtils.doSearch(LocalInfos.QQFiles);
//                int result = LocalOfficeFileUtils.doSearch(LocalOfficeFileUtils.filePath);
                List<File> totalFileList = LocalOfficeFileUtils.searchOfficeFiles(LocalOfficeFileUtils.filePath);
                List<File> QQFileList = LocalOfficeFileUtils.searchOfficeFiles(LocalInfos.QQFiles);
                List<File> WechatFileList = LocalOfficeFileUtils.searchOfficeFiles(LocalInfos.WeChatFiles);
                //Log.d("LocalOfficeFileUtils","---------------getResult------------------------");
            }
        }).start();


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
        List<DataContainedSpannableStringBuilder> editables = xmlToSpanUtil.xmlToEditable(this, content);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < editables.size(); i++) {
            sb.append(editables.get(i));
        }
//        ((TextView)findViewById(R.id.textview)).setMovementMethod(new ScrollingMovementMethod());
        ClickableLinkMovementMethod clickableLinkMovementMethod = new ClickableLinkMovementMethod();
        clickableLinkMovementMethod.setCallback(new ClickImageMovementMethodCallback() {
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
//        String ppt = sdcard_path + "/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/新版马克思主义基本原理复习提纲20191204(2).pptx";
        String ppt = sdcard_path + "/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/ToPPTX.pptx";

        PptxUtil.readPptx(ppt,"test","test");

        MyXmlReader myXmlReader = new MyXmlReader();
        String content = myXmlReader.fileToString(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/test.xml");

        //解析xml并合成editable呈现在textview上
        XmlToSpanUtil xmlToSpanUtil = new XmlToSpanUtil();

        //返回<p></p>的集合
        List<DataContainedSpannableStringBuilder> editables = xmlToSpanUtil.xmlToEditable(this, content);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < editables.size(); i++) {
            DataContainedSpannableStringBuilder dcSb = editables.get(i);
            Log.d(TAG,"key = "+dcSb.getKey()+" val = "+dcSb.getValue() + " isLineEnd = "+dcSb.isLineEnd());
            Log.d(TAG,"string = "+dcSb);
            sb.append(editables.get(i));
        }

//        ((TextView)findViewById(R.id.textview)).setMovementMethod(new ScrollingMovementMethod());
        ClickableLinkMovementMethod clickableLinkMovementMethod = new ClickableLinkMovementMethod();
        clickableLinkMovementMethod.setCallback(new ClickImageMovementMethodCallback() {
            @Override
            public void onClicked(Drawable drawable) {
                ((SubsamplingScaleImageView) findViewById(R.id.bigview)).setImage(ImageSource.bitmap(FormatTools.getInstance().drawable2Bitmap(drawable)));
            }
        });

        ((TextView) findViewById(R.id.textview)).setMovementMethod(clickableLinkMovementMethod);

        ((TextView) findViewById(R.id.textview)).setText(sb);

        //测试 将Editable转为按<p></p> <p></p> ...为规格的Xml
        List<String> xmlResultList = SpanToXmlUtil.editableToXml(sb);
        StringBuilder finalXmlResult = new StringBuilder();
        finalXmlResult.append(XmlTags.getXmlBegin());
        for (int i = 0 ; i < xmlResultList.size() ; i++){
            finalXmlResult.append(xmlResultList.get(i));
            //加上一个换行只是为了更好打印结果观察
            finalXmlResult.append("\n");
        }
        finalXmlResult.append(XmlTags.getXmlEnd());
        Log.d("XmlResultListTest",finalXmlResult.toString());

        //将xml按<p>拆分成一句一句
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
