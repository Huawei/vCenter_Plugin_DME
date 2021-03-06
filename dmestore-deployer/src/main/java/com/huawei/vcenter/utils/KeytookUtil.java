package com.huawei.vcenter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * KeytookUtil
 *
 * @author andrewliu
 * @since 2020-09-15
 **/
public class KeytookUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(KeytookUtil.class);

    private static final String KEY = "tomcat.keystore";

    private static final String SHA = "(SHA1):";

    private KeytookUtil() {
    }

    public static void genKey() throws IOException {
        LOGGER.info("Checking keystore file...");
        File file = new File(KEY);
        if (!file.exists()) {
            LOGGER.info("Generating keystore file...");
            String cmd
                = "keytool -genkeypair -alias tomcat -keyalg RSA  -keystore ./tomcat.keystore  -keypass changeit -storepass changeit -dname CN=localhost,OU=cn,O=cn,L=cn,ST=cn,C=cn";
            Runtime.getRuntime().exec(cmd);
        } else {
            LOGGER.info("Keystore file exists");
        }
    }

    public static String getKeystoreServerThumbprint() throws IOException {
        BufferedReader input = null;
        InputStreamReader inr = null;
        try {
            String cmd = "keytool -list -keypass changeit -storepass changeit -keystore ./tomcat.keystore";
            Process process = Runtime.getRuntime().exec(cmd);
            inr = new InputStreamReader(process.getInputStream(), "utf-8");
            input = new BufferedReader(inr);
            String line = "";
            while ((line = input.readLine()) != null) {
                if (line.contains(SHA)) {
                    return line.substring(line.indexOf(SHA) + SHA.length() + 1);
                }
            }
        } finally {
            if (input != null) {
                input.close();
            }

            if (inr != null) {
                inr.close();
            }
        }
        return null;
    }
}
