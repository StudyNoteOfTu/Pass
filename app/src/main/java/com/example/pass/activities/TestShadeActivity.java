package com.example.pass.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.pass.R;
import com.example.pass.recyclerentry.selectTitle.LineAdapter;
import com.example.pass.util.ImageFormatTools;
import com.example.pass.util.officeUtils.DocxUtil;
import com.example.pass.util.officeUtils.FileUtil;
import com.example.pass.util.officeUtils.MyXmlReader;
import com.example.pass.util.officeUtils.PPTX.PptxUtil;
import com.example.pass.util.officeUtils.XmlTags;
import com.example.pass.util.shade.ShadeManager;
import com.example.pass.util.shade.viewAndModels.ShadeRelativeLayout;
import com.example.pass.util.shade.viewAndModels.ShadeView;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;
import com.example.pass.util.spanUtils.SpanToXmlUtil;
import com.example.pass.util.spanUtils.XmlToSpanUtil;
import com.example.pass.util.spans.callbacks.ClickImageMovementMethodCallback;
import com.example.pass.util.spans.movementMethods.ClickableLinkMovementMethod;
import com.example.pass.view.SwipeItemLayout;

import java.util.List;

public class TestShadeActivity extends AppCompatActivity {

    TextView textView;
    ShadeView shadeView;
    ShadeRelativeLayout shadeRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shade);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

        SpannableStringBuilder sb = null;

        if (path.endsWith(".docx")){
            sb =  testDoc(path);
        }else if (path.endsWith(".pptx")){
            sb = testPPT(path);
        }

        initViews(sb);

    }

    private void initViews(SpannableStringBuilder spannableStringBuilder) {
        shadeRelativeLayout = findViewById(R.id.shadeRelativeLayout);
        shadeView = findViewById(R.id.shadeView);
        textView = findViewById(R.id.textView);

        if (spannableStringBuilder != null){
            textView.setText(spannableStringBuilder);
            shadeRelativeLayout.setShadeView(shadeView);
            ShadeManager shadeManager = ShadeManager.getInstance();
            shadeView.init(textView,shadeManager,spannableStringBuilder);
            shadeManager.setHolder(textView);
        }


    }


    private SpannableStringBuilder testDoc(String path) {
        String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();

        String doc_path = path;

        DocxUtil.readDocx(doc_path, "PassTest", FileUtil.getFileName(path));
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PassTest/"+FileUtil.getFileName(path)+".xml";

        MyXmlReader myXmlReader = new MyXmlReader();
        String content = myXmlReader.fileToString(filePath);

        //解析xml并合成editable呈现在textview上
        XmlToSpanUtil xmlToSpanUtil = new XmlToSpanUtil();
        List<DataContainedSpannableStringBuilder> editables = xmlToSpanUtil.xmlToEditable(this, content);
        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < editables.size(); i++) {
            sb.append(editables.get(i));
        }


        return sb;


    }

    private SpannableStringBuilder testPPT(String path) {
        String sdcard_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String ppt = path;

        PptxUtil.readPptx(ppt,"PassTest",FileUtil.getFileName(path));
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PassTest/"+FileUtil.getFileName(path)+".xml";

        MyXmlReader myXmlReader = new MyXmlReader();
        String content = myXmlReader.fileToString(filePath);

        //解析xml并合成editable呈现在textview上
        XmlToSpanUtil xmlToSpanUtil = new XmlToSpanUtil();

        //返回<p></p>的集合
        List<DataContainedSpannableStringBuilder> editables = xmlToSpanUtil.xmlToEditable(this, content);

        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (int i = 0; i < editables.size(); i++) {
            DataContainedSpannableStringBuilder dcSb = editables.get(i);
            sb.append(editables.get(i));
        }


        return sb;

    }
}
