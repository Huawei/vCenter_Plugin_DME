package com.huawei.dmestore.exception;

/**
 * VcenterException
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class VcenterException extends DmeException {
    private String code;

    private String message;

    public VcenterException() {
        super();
    }

    public VcenterException(String message) {
        super(message);
        this.message = message;
    }

    public VcenterException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
