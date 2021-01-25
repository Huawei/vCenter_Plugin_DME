package com.huawei.dmestore.exception;

/**
 * IgnorableException
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class IgnorableException extends VcenterRuntimeException {
    /**
     * IgnorableException
     */
    public IgnorableException() {
        super();
    }

    /**
     * IgnorableException
     *
     * @param message message
     */
    public IgnorableException(String message) {
        super(message);
    }

    /**
     * IgnorableException
     *
     * @param code
     * @param message message
     */
    public IgnorableException(String code, String message) {
        super(code, message);
    }
}
