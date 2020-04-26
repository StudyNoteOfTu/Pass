package com.example.pass.util.typefaceUtils;

import com.example.pass.configs.TypefaceConfig;
import com.example.pass.util.FileUtil;
import com.example.pass.view.popWindows.fontstylePopWindows.bean.TextTypeface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TypefaceUtil {
    /**
     * 可在全局提前加载
     * 获取本地typeface
     * IOC 判断权限！
     */
    public static List<TextTypeface> getLocalTextTypeface(){
        List<TextTypeface> typefaceList = new ArrayList<>();
        String parentPath = TypefaceConfig.ttfFolderPath;
        File file = new File(parentPath);
        if (file.exists() && file.isDirectory()){
            //如果file存在并且是文件夹，获取其中所有文件（都是ttf）
            File[] childrenFiles= file.listFiles();
            if (childrenFiles != null) {
                TextTypeface typeface;
                for (File childrenFile : childrenFiles) {
                    if (childrenFile.getName().endsWith(".ttf")) {
                        //如果是ttf文件，那么加入
                        String path = childrenFile.getAbsolutePath();
                        typeface = new TextTypeface(path, FileUtil.getFileName(path));
                        typefaceList.add(typeface);
                    }
                }
            }
        }
        return typefaceList;
    }
}
