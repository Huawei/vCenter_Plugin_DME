package com.huawei.dmestore.utils;

/**
 * VcClientUtil
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public class VcClientUtil {
    private static final String FLASH_CLIENT = "vsphere-client";

    private static final String HTML5_CLIENT = "vsphere-ui";

    private static final String WEB_CLIENT = System.getProperty("user.name");

    private VcClientUtil() {
    }

    public static boolean isFlashClient() {
        return FLASH_CLIENT.equalsIgnoreCase(getWebClient());
    }

    public static boolean isHtml5Client() {
        return HTML5_CLIENT.equalsIgnoreCase(getWebClient());
    }

    public static String getWebClient() {
        return WEB_CLIENT;
    }
}
