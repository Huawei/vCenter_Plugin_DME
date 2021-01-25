package com.huawei.vcenter.utils;

import com.vmware.automatic.plugin.registration.PluginRegistrationMain;

import java.util.HashMap;
import java.util.Map;

/**
 * VcenterRegisterRunner
 *
 * @author andrewliu
 * @since 2020-09-15
 **/
public class VcenterRegisterRunner {
    private static final int DEFAULT_LEN = 16;

    private static final String ACTION = "-action";

    private static final String USERNAME = "-username";

    private static final String PASSWORD = "-password";

    private static final String KEY = "-key";

    private static final String URL = "-url";

    private static final String HTTPS_PRE = "https://";

    private static final String COLON = ":";

    private static final String SDK_PATH = "/sdk";

    private static final int DIGIT_2 = 2;

    private VcenterRegisterRunner() {
    }

    public static void run(String version, String packageUrl, String serverThumbprint, String vcenterIp,
        String vcenterPort, String username, String password, String key) {
        Map<String, String> unRegParamMap = new HashMap(DEFAULT_LEN);
        unRegParamMap.put(ACTION, "unregisterPlugin");
        unRegParamMap.put(USERNAME, username);
        unRegParamMap.put(PASSWORD, password);
        unRegParamMap.put(KEY, key);
        unRegParamMap.put(URL, HTTPS_PRE + vcenterIp + COLON + vcenterPort + SDK_PATH);

        PluginRegistrationMain.main(convertMapToArray(unRegParamMap));

        Map<String, String> regParamMap = new HashMap(DEFAULT_LEN);
        regParamMap.put(ACTION, "registerPlugin");
        regParamMap.put(USERNAME, username);
        regParamMap.put(PASSWORD, password);
        regParamMap.put(KEY, key);
        regParamMap.put("-version", version);
        regParamMap.put("-pluginUrl", packageUrl);
        regParamMap.put("-company", "DME");
        regParamMap.put("--serverThumbprint", serverThumbprint);
        regParamMap.put(URL, HTTPS_PRE + vcenterIp + COLON + vcenterPort + SDK_PATH);

        PluginRegistrationMain.main(convertMapToArray(regParamMap));
    }

    public static void unRegister(String vcenterIp, String vcenterPort, String username, String password, String key) {
        Map<String, String> unRegParamMap = new HashMap(DEFAULT_LEN);
        unRegParamMap.put(ACTION, "unregisterPlugin");
        unRegParamMap.put(USERNAME, username);
        unRegParamMap.put(PASSWORD, password);
        unRegParamMap.put(KEY, key);
        unRegParamMap.put(URL, HTTPS_PRE + vcenterIp + COLON + vcenterPort + SDK_PATH);

        PluginRegistrationMain.main(convertMapToArray(unRegParamMap));
    }

    private static String[] convertMapToArray(Map<String, String> map) {
        if ((map == null) || (map.size() == 0)) {
            return new String[0];
        }
        String[] args = new String[map.size() * DIGIT_2];
        int index = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            args[index++] = entry.getKey().trim();
            args[index++] = entry.getValue().trim();
        }
        return args;
    }
}
