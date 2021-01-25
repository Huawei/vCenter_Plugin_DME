package com.huawei.dmestore.utils;

import java.nio.charset.Charset;


public class StringUtil {
    private static Charset preferredACSCharset;
    private static final String UTF8 = "UTF-8";

    static {
        if (isUtf8Supported()) {
            preferredACSCharset = Charset.forName(UTF8);
        } else {
            preferredACSCharset = Charset.defaultCharset();
        }
    }


    public static boolean isUtf8Supported() {
        return Charset.isSupported(UTF8);
    }

    public static Charset getPreferredCharset() {
        return preferredACSCharset;
    }

    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
