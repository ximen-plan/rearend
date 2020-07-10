package com.ximen.plan.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.URLEncoder;

public class FileUtils {
    /**
     * 下载文件时，针对不同浏览器，进行附件名的编码
     *
     * @param filename 下载文件名
     * @param agent    客户端浏览器
     * @return 编码后的下载附件名
     * @throws IOException
     */
    public static String encodeDownloadFilename(String filename, String agent) throws IOException {
        if (agent.contains("Firefox")) { // 火狐浏览器
            filename = "=?UTF-8?B?" + new BASE64Encoder().encode(filename.getBytes("utf-8")) + "?=";
            filename = filename.replaceAll("\r\n", "");
        } else { // IE及其他浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        return filename;
    }

    /**
     * 对上传的 .word .excel .ppt .pdf进行分类
     *
     * @param extendName
     * @return
     */
    public static String getFileType(String extendName) {
        if (StringUtils.isBlank(extendName)) {
            return "";
        }
        extendName = extendName.substring(1);
        if ("pdf".equals(extendName)) {
            return "pdfs";
        }
        if ("ppt".equals(extendName) || "pptx".equals(extendName)) {
            return "ppts";
        }
        if ("doc".equals(extendName) || "docx".equals(extendName)) {
            return "words";
        }
        if ("xls".equals(extendName) || "xlsx".equals(extendName) || "csv".equals(extendName)) {
            return "excels";
        }
        if ("png".equals(extendName) || "jpg".equals(extendName) || "jpeg".equals(extendName)) {
            return "images";
        }
        return "others";
    }
}
