package com.ti.utils;

public class StringUtil {

    /**
     * 字符串首字母大写
     *
     * @param str
     * @return
     */
    public static String toUpperFirstChar(String str) {
        StringBuffer sb = new StringBuffer(str);
        String firstChar = sb.substring(0, 1);
        String upper = firstChar.toUpperCase();
        sb.replace(0, 1, upper);
        return sb.toString();
    }
}
