/**
 * Project name: AndroidSpeeder
 * Package name: me.waye.androidspeeder.util
 * Filename: StringUtil.java
 * Created time: Nov 27, 2013
 * Copyright: Copyright(c) 2013. All Rights Reserved.
 */

package me.waye.androidspeeder.util;

/**
 * @ClassName: StringUtil
 * @Description: 字符串工具类
 * @author tangwei
 * @date Nov 27, 2013 1:27:06 PM
 * 
 */
public class StringUtil {

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     * 
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }
}
