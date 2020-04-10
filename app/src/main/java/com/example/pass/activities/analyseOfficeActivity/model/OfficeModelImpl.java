package com.example.pass.activities.analyseOfficeActivity.model;

import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.pass.activities.analyseOfficeActivity.model.impls.IOfficeModel;
import com.example.pass.util.officeUtils.DocxUtil;
import com.example.pass.util.officeUtils.MyXmlReader;
import com.example.pass.util.officeUtils.MyXmlWriter;
import com.example.pass.util.officeUtils.PPTX.PptxUtil;
import com.example.pass.util.officeUtils.XmlTags;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OfficeModelImpl implements IOfficeModel {

    MyXmlReader myXmlReader;
    private final String DOCX = ".docx";
    private final String PPTX = ".pptx";
    private final String XML = ".xml";

    //获取到的xml按行集合
    List<String> xmlLineList = new ArrayList<>();

    public OfficeModelImpl() {
        myXmlReader = new MyXmlReader();
    }


    @Override
    public boolean readOffice(String path, String dir, String name, @NonNull OnLoadProgressListener<String> listener) {
        listener.onStart();
        //判断是哪种Office文件
        if (path.toLowerCase().endsWith(DOCX)) {
            //如果是docx文件
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String out_path = readDocx(path, dir, name);
                    listener.onFinish(out_path);
                }
            }).start();
            return true;
        } else if (path.toLowerCase().endsWith(PPTX)) {
            //如果是pptx文件
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String out_path = readPptx(path, dir, name);
                    listener.onFinish(out_path);
                }
            }).start();
            return true;
        } else if (path.toLowerCase().endsWith(XML)){
            //如果是xml，不作处理直接返回
            listener.onFinish(path);
        }
        listener.onError("selected file is not .docx or .pptx file, so we can't analyse it");
        return false;
    }

    private String readPptx(String path, String dir, String name) {
        PptxUtil.readPptx(path, dir, name);
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dir + File.separator + name + ".xml";
    }

    private String readDocx(String path, String dir, String name) {
        DocxUtil.readDocx(path, dir, name);
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dir + File.separator + name + ".xml";
    }

    @Override
    public String xmlToString(String path) {
        return xmlToString(path, null);
    }

    @Override
    public String xmlToString(String path, OnLoadProgressListener listener) {
        if (listener != null) listener.onStart();
        if (myXmlReader != null) {
            String result = myXmlReader.fileToString(path);
            if (listener != null) listener.onFinish(result);
            return result;
        }
        //如果解析器为空
        if (listener != null) listener.onError("MyXmlReader is null");
        return "";
    }

    @Override
    public List<String> getXmlLines(String content) {
        if (myXmlReader != null) {
            xmlLineList.clear();
            xmlLineList.addAll(myXmlReader.getLineList(content));
            return xmlLineList;
        }
        return null;
    }

    @Override
    public boolean changeLineState(String key, String value, int position) {
        if (position < 0 || position > xmlLineList.size()) return false;
        //如果position确实在list中，接下来就进行修改
        String result = xmlLineList.get(position);
        String oldBeginTag = getTargetSubString(result);
        if (oldBeginTag == null) return false;
        //如果subString不为空，则进行替换
        String newBeginTag = XmlTags.getLineBegin(key, value);
        result = result.replace(oldBeginTag, newBeginTag);
        //或者xmlLineList.get(position) = result; 暂时不知道哪个合适
        xmlLineList.set(position, result);
        return true;
    }

    private String getTargetSubString(String resource) {
        String regex = "<pass:p key=\"(.*?)\" val=\"(.*?)\">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(resource);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    @Override
    public boolean recompileListToXml(String dir, String name, OnLoadProgressListener<String> listener) {
        List<String> fixedXmlLines = fixXmlLines(xmlLineList);
        return MyXmlWriter.compileLinesToXml(fixedXmlLines, dir, name, listener);
    }

    /**
     * 自动填补完善用户编辑好的列表
     * //注意这里有bug， 什么标题都没设置的时候是不应该有填充的
     *
     * @param xmlLineList 用户编辑好的列表
     * @return 自动填补后的列表
     */
    private List<String> fixXmlLines(List<String> xmlLineList){
        List<String> fixedLines = new ArrayList<>(xmlLineList);

        int top = 0;
        int lastNum = 1;
        int i = 0 ;
        int curNum = 0;
        while((i<fixedLines.size() - 1)){
            curNum = getLevelAttribute(fixedLines.get(i));
            //遍历的同时获取当前集合最大的level
            if (curNum > top){
                //寻找最大数字
                top = curNum;
            }
            //处理文段
            if (curNum >= lastNum - 1){
                //如果比上一个大，或者比上一个小1，则不作处理
                lastNum = curNum;
            }else{
                //如果相差超过1
                fixedLines.add(i,createEmptyLineString(lastNum - 1));
                lastNum = lastNum -1;
            }
            i++;
        }

        if (fixedLines.size() > 0 ){
            int delta = top - getLevelAttribute(fixedLines.get(0));
            for (int j = 0 ; j < delta ; j++){
                fixedLines.add(j,createEmptyLineString(top-j));
            }
        }
        return fixedLines;
    }

    /**
     * 创建一个<p></p>空白内容，自动填补层级
     * @param level
     * @return
     */
    private String createEmptyLineString(int level){
        StringBuilder sb= new StringBuilder();
        sb.append(XmlTags.getLineBegin(XmlTags.getKey_title(),XmlTags.getTitle_level(level)));
        sb.append(XmlTags.getBlockBegin());
        sb.append(XmlTags.getTextBegin());
        sb.append("未命名H").append(level).append("层级(自动填补)");
        sb.append(XmlTags.getTextEnd());
        sb.append(XmlTags.getBlockEnd());
        sb.append(XmlTags.getLineEnd());
        return sb.toString();
    }

    /**
     * 获得当前行（<p></p>）的层级
     * @param line 当前行字符串
     * @return 获取到的层级，如果为""返回0
     */
    private int getLevelAttribute(String line){

        String key = null;
        String value = null;
        String regex = "<pass:p key=\"(.*?)\" val=\"(.*?)\">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        //如果有匹配的，只需要最前面一段
        if (matcher.find()) {
            key = matcher.group(1);
            value = matcher.group(2);
        }

        if (TextUtils.isEmpty(value)){
            value = String.valueOf(0);
        }

        return  Integer.parseInt(value);

    }
}
