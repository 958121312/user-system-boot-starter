package com.zjdx.user.security.utils;

/**
 * 标志位工具类
 * @author Yuntian
 * @since 2022-02-21
 */
public class FlagUtil {

    public static final String DEFAULT_FLAG = "0000000000000000000000";

    public static String flagIndexOf(String flag,int index) {
        if(index < 0 || index >= flag.length()) {
            return null;
        }
        return String.valueOf(flag.charAt(index));
    }

    /**
     * 变更flag指定位置值
     * @return
     */
    public static String intoFlag(String flag, int index, char value) {
        if(index < 0 || index >= flag.length()) {
            return null;
        }
        String prefix = flag.substring(0, index);
        String suffix = flag.substring(index + 1);
        return prefix + value + suffix;
    }

}
