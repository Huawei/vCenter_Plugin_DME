package com.huawei.dmestore.exception;

/**
 * DataBaseException
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class DataBaseException extends VcenterRuntimeException {
    /**
     * DataBaseException
     */
    public DataBaseException() {
        super();
    }

    /**
     * DataBaseException
     *
     * @param message message
     */
    public DataBaseException(String message) {
        super(message);
        setCode("-70001");
    }

    /**
     * DataBaseException
     *
     * @param code code
     * @param message message
     */
    public DataBaseException(String code, String message) {
        super(message);
        setCode(code);
    }
}
