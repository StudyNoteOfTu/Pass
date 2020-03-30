package com.example.pass.configs;

public class PathConfig {

    /**
     * 图片存放路径
     */
    //pptx内置图片存放位置
    public static final String PPT_PICS_PATH = "Pass/Pics/pptx_pics";

    //docx内置图片存放位置
    public static final String DOX_PICS_PATH = "Pass/Pics/docx_pics";

    //零散记，图片存放位置
    public static final String SCATTER_PICS_PATH = "Pass/Pics/scatter_pics";

    //下载来的系统文件的图片存放路径
    public static final String SYSTEM_DOWNLOAD_PICS_PATH = "Pass/Pics/system_download";

    //下载下来的用户文件的图片存放路径
    public static final String OTHERS_DOWNLOAD_PICS_PATH = "Pass/Pics/others_download";

    /**
     * 本地文档存放路径
     */
    //确认生成的office文件的xml解析文档
    public static final String FINAL_OFFICE_PATH = "Pass/final_xmls";

    //临时读取，不一定生成最终xml文档的office文件
    public static final String TEMP_OFFICE_PATH = "Pass/read_temp";

    //零散记，每条xml文件存放位置
    public static final String SCATTER_XMLS_PATH = "Pass/scatter_xmls";

    /**
     * 下载文档存放路径,所有xml文件中解析的pic路径全部进行修改
     */
    //下载的官方文件的存放路径
    public static final String SYSTEM_DOWNLOAD_PATH ="Pass/download/system";
    //下载的其他用户的资料的路径
    public static final String OTHERS_DOWNLOAD_PATH = "Pass/download/others";



}
