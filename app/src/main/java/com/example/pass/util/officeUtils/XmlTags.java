package com.example.pass.util.officeUtils;


public class XmlTags {

    private final static String passTag = "pass:";

    private static String xmlBegin = "<?xml version=\"1.0\" encoding=\"utf-8\"?><resource xmlns:pass=\"http://www.fengyitu.com\">";

    private static String xmlEnd = "</resource>";

    /**
     * 譬如
     * 左滑忽略：<pass:p key="ignore" val="1"></pass:p>
     * 目录登记：<pass:p key="title" val="1"></pass:p>
     */
    private static String lineBegin = "<"+passTag+"p key=\"%s\" val=\"%s\">",lineEnd ="</"+passTag+"p>";

    private static  String styleTag = "<"+passTag+"style key=\"%s\" val=\"%s\"/>";

    private static  String textBegin = "<"+passTag+"t>", textEnd = "</"+passTag+"t>";

    private static  String blockBegin= "<"+passTag+"r>", blockEnd = "</"+passTag+"r>";

    private static  String picBegin="<"+passTag+"pic>", picEnd = "</"+passTag+"pic>";

    private static String titleBegin = "<"+passTag+"title val=\"%d\">",titleEnd="</"+passTag+"title>";


    //列表序列
    private static String value_list = "list";

    //列表id
    private static String value_listId = "listId";

    //颜色
    private static String value_color = "color";

    //背景高亮
    private static String value_highlight = "highlight";

    //粗体
    private static  String value_bold = "bold";

    //下划线
    private static  String value_underline = "underline";

    //斜体
    private static  String value_italic ="italic";

    //删除线
    private static  String value_linethrough = "linethrough";

    //标题
    private static  String value_title = "title";

    //字体大小
    private static  String value_fontsize = "fontsize";

    public static String getLineBegin(String key,String value) {
        return String.format(lineBegin,key,value);
    }

    public static String getLineEnd() {
        return lineEnd;
    }

    public static String getStyleTag(String s,String value) {
        return String.format(styleTag,s,value);
    }
    public static String getStyleTag(String s) {
        return String.format(styleTag,s,"");
    }
    public static String getTextBegin() {
        return textBegin;
    }

    public static String getTextEnd() {
        return textEnd;
    }

    public static String getBlockBegin() {
        return blockBegin;
    }

    public static String getBlockEnd() {
        return blockEnd;
    }

    public static String getPicBegin() {
        return picBegin;
    }

    public static String getPicEnd() {
        return picEnd;
    }

    public static String getValue_bold() {
        return value_bold;
    }

    public static String getValue_underline() {
        return value_underline;
    }

    public static String getValue_italic() {
        return value_italic;
    }

    public static String getValue_linethrough() {
        return value_linethrough;
    }

    public static String getValue_title() {
        return value_title;
    }

    public static String getValue_fontsize() {
        return value_fontsize;
    }

    public static String getTitleBegin() {
        return titleBegin;
    }

    public static String getTitleEnd() {
        return titleEnd;
    }

    public static String getXmlBegin() {
        return xmlBegin;
    }

    public static String getXmlEnd() {
        return xmlEnd;
    }

    public static String getValue_list() {
        return value_list;
    }

    public static String getPassTag() {
        return passTag;
    }

    public static String getLineBegin() {
        return lineBegin;
    }

    public static String getStyleTag() {
        return styleTag;
    }

    public static String getValue_listId() {
        return value_listId;
    }

    public static String getValue_color() {
        return value_color;
    }

    public static String getValue_highlight() {
        return value_highlight;
    }
}

