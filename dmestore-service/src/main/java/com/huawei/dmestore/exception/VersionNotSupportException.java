package com.huawei.dmestore.exception;

/**
 * VersionNotSupportException
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class VersionNotSupportException extends IgnorableException {
    private static final String RETURN_CODE = "-90006";

    /**
     * VersionNotSupportException
     *
     * @param currentVersion currentVersion
     */
    public VersionNotSupportException(String currentVersion) {
        super(RETURN_CODE, "Version doesn't support: " + currentVersion);
    }

    public static String getReturnCode() {
        return RETURN_CODE;
    }
}
