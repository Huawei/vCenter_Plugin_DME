package com.huawei.vcenter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * ZipUtils
 *
 * @author andrewliu
 * @since 2020-09-15
 **/
public class ZipUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ZipUtils.class);

    public static String getVersionFromPackage(String file) {
        ZipFile zf = null;
        FileInputStream fin = null;
        InputStream in = null;
        ZipInputStream zin = null;
        BufferedReader br = null;
        String version = null;
        InputStreamReader inr = null;
        try {
            zf = new ZipFile(file);
            fin = new FileInputStream(file);
            in = new BufferedInputStream(fin);
            zin = new ZipInputStream(in);
            while (true) {
                ZipEntry ze;
                do {
                    do {
                        ze = zin.getNextEntry();
                        if (ze == null) {
                            return version;
                        }
                    } while (ze.isDirectory());
                } while (!"plugin-package.xml".equals(ze.getName()));
                inr = new InputStreamReader(zf.getInputStream(ze), "utf-8");
                br = new BufferedReader(inr);
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("<pluginPackage") && line.contains("version=\"")) {
                        line = line.substring(line.indexOf("version=\"") + "version=\"".length());
                        version = line.substring(0, line.indexOf(34));
                    }
                }
            }
        } catch (IOException var11) {
            LOG.error(var11.getMessage());
        } finally {
            if (zin != null) {
                try {
                    zin.closeEntry();
                } catch (IOException var) {
                    LOG.error(var.getMessage());
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var) {
                    LOG.error(var.getMessage());
                }
            }
            closeInputStream(zin);
            closeInputStream(in);
            if (inr != null) {
                try {
                    inr.close();
                } catch (IOException var) {
                    LOG.error(var.getMessage());
                }
            }
            closeInputStream(fin);
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException var) {
                    LOG.error(var.getMessage());
                }
            }
        }
        return version;
    }

    private static void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException var) {
                LOG.error(var.getMessage());
            }
        }
    }
}
