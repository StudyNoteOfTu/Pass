package com.example.pass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pass.R;

import com.example.pass.activities.analyseOfficeActivity.model.impls.IOfficeModel;
import com.example.pass.util.officeUtils.DocxUtil;
import com.example.pass.util.FileUtil;
import com.example.pass.util.officeUtils.MyXmlReader;
import com.example.pass.util.officeUtils.MyXmlWriter;
import com.example.pass.util.officeUtils.PPTX.PptxUtil;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderBean;
import com.example.pass.util.officeUtils.shadeInfoUtils.ShaderXmlTool;
import com.example.pass.util.shade.ShadeManager;
import com.example.pass.util.shade.viewAndModels.ShadeRelativeLayout;
import com.example.pass.util.shade.viewAndModels.ShadeView;
import com.example.pass.util.shade.viewAndModels.Shader;
import com.example.pass.util.spanUtils.DataContainedSpannableStringBuilder;
import com.example.pass.util.spanUtils.XmlToSpanUtil;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestShadeActivity extends AppCompatActivity {

    TextView textView;
    ShadeView shadeView;
    ShadeRelativeLayout shadeRelativeLayout;

    Button btn_finish;
    String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shade);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        SpannableStringBuilder sb = null;

        if (path.endsWith(".docx")){
            sb =  testDoc(path);
        }else if (path.endsWith(".pptx")){
            sb = testPPT(path);
        }

        initViews(sb);

    }

    private void initViews(SpannableStringBuilder spannableStringBuilder) {
        btn_finish = findViewById(R.id.btn_finish);
        shadeRelativeLayout = findViewById(R.id.shadeRelativeLayout);
        shadeView = findViewById(R.id.shadeView);
        textView = findViewById(R.id.textView);

        if (spannableStringBuilder != null){
            textView.setText(spannableStringBuilder);
            shadeRelativeLayout.setShadeView(shadeView);
            ShadeManager shadeManager = ShadeManager.getInstance();
            List<ShaderBean> shaderBeans= ShaderXmlTool.analyseXml(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator+"Pass/Test/test.shader");
            List<Shader> shaders = new ArrayList<>();
            Shader shader;
            for (ShaderBean shaderBean : shaderBeans) {
                shader = new Shader(shadeView,0,0-shaderBean.getHeight()-100,shaderBean.getWidth(),shaderBean.getHeight());
                shader.setLeftPadding(shaderBean.getLeftPadding());
                shader.setTopPadding(shaderBean.getTopPadding());
                shader.setImgUrl(shaderBean.getImage_path());
                shader.setTimeTag(shaderBean.getTime_tag());
                shaders.add(shader);
            }


            textView.post(new Runnable() {
                @Override
                public void run() {
                    //holder设置必须在init之前
                    shadeManager.setHolder(textView);
                    shadeManager.setOnLocateCallBack(shadeView);
                    shadeView.init(shaders,textView,shadeManager,spannableStringBuilder);
                }
            });


            btn_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<ShaderBean> shaderBeans = new ArrayList<>();
                    List<Shader> shaders = shadeView.getAllShaders();
                    ShaderBean bean;
                    for (Shader shader : shaders) {
                        bean = new ShaderBean(shader.getImgUrl(),shader.getTimeTag(),
                                shader.getLeftPadding(),shader.getTopPadding(),
                                shader.getWidth(),shader.getHeight());
                        shaderBeans.add(bean);
                    }

                    ShaderXmlTool.createXml(shaderBeans,"Pass/Test","test");

                    String xml = shadeView.getXmlString();
                    //存入文件
                    //覆盖
                    MyXmlWriter.compileLinesToXml(xml,"Pass/Test/Shade", "shadetest", new IOfficeModel.OnLoadProgressListener<String>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFinish(String result) {
                            Log.d("OnFinishTest",result);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });


                    //同时保存xml编辑
                    //往path里面更新


//                    String result = ShaderXmlTool.analyseXml(Environment.getExternalStorageDirectory().getAbsolutePath()
//                    + File.separator+"Pass/Test/test.shader").toString();
//                    Log.d("XmlTest",result);
                }
            });
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
