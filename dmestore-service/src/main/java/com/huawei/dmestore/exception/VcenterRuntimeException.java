package com.huawei.dmestore.exception;

/**
 * VcenterRuntimeException
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class VcenterRuntimeException extends RuntimeException {
    private String code;
    private String message;

    /**
     * VcenterRuntimeException
     */
    public VcenterRuntimeException() {
        super();
    }

    /**
     * VcenterRuntimeException
     *
     * @param message message
     */
    public VcenterRuntimeException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * VcenterRuntimeException
     *
     * @param code code
     * @param message message
     */
    public VcenterRuntimeException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
