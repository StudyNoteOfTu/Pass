package com.example.pass.util.officeUtils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyXmlReader {
    public final static String TAG = "MyXmlReader";

    public String fileToString(String filePath){
        StringBuilder sb = new StringBuilder();
        try{
            FileReader fr = new FileReader(filePath);
            BufferedReader bf= new BufferedReader(fr);
            String str;
            //按行读取字符串
            while((str = bf.readLine())!=null){
                sb.append(str);
                Log.d(TAG,"readline: "+str);
            }
            bf.close();
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    //切割，拿到<pass:p>到</pass:p>的所有内容，存入list
    public List<String> getLineList(String content){
        List<String> list = new ArrayList<>();
        String tagBegin = "<pass:p";//注意这里不要后面的>
        String tagEnd = "</pass:p>";
        String subStr = "";
        int tagEndLength = tagEnd.length();

        //循环拿到子串
        while(content.contains(tagBegin)){
            //几个index
            int tagBeginIndex = content.indexOf(tagBegin);
            int tagEndIndex = content.indexOf(tagEnd);
            //如果出错，只找到一半tag
            if (tagBeginIndex == -1 || tagEndIndex == -1){
                break;
            }
            subStr = content.substring(tagBeginIndex,tagEndIndex+tagEndLength);
            //如果subStr没有内容就不要了
            if (hasContent(subStr)) {
                list.add(subStr);
                Log.d(TAG,"get subString :"+subStr);
            }
            //丢掉这一段tag
            content = content.substring(tagEndIndex+tagEndLength);
        }
        return list;
    }

    private boolean hasContent(String subString){
        //通过正则表达式格式判断
        String pattern = "<pass:p key=\"\\w*\" val=\"\\w*\"></pass:p>";
        return !subString.matches(pattern);

    }

}
