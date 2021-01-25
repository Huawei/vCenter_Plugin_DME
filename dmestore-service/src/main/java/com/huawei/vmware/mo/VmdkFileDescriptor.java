package com.huawei.vmware.mo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * VmdkFileDescriptor
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class VmdkFileDescriptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(VmdkFileDescriptor.class);

    private Properties properties = new Properties();
    private String baseFileName;
    private final int arrLength = 2;

    /**
     * VmdkFileDescriptor
     */
    public VmdkFileDescriptor() {
    }

    /**
     * parse
     *
     * @param vmdkFileContent vmdkFileContent
     * @throws IOException IOException
     */
    public void parse(byte[] vmdkFileContent) throws IOException {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(vmdkFileContent), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }

                String[] tokens = line.split("=");
                if (tokens.length == arrLength) {
                    String name = tokens[0].trim();
                    String value = tokens[1].trim();
                    if (value.charAt(0) == '\"') {
                        value = value.substring(1, value.length() - 1);
                    }

                    properties.put(name, value);
                } else {
                    if (line.startsWith("RW")) {
                        int startPos = line.indexOf('\"');
                        int endPos = line.lastIndexOf('\"');
                        assert startPos > 0;
                        assert endPos > 0;

                        baseFileName = line.substring(startPos + 1, endPos);
                    } else {
                        LOGGER.warn("Unrecognized vmdk line content:{} ", line);
                    }
                }
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    public String getBaseFileName() {
        return baseFileName;
    }

    public String getParentFileName() {
        return properties.getProperty("parentFileNameHint");
    }

    public Properties getProperties() {
        return properties;
    }
}
