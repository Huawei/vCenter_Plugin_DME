package com.huawei.dmestore.exception;

/**
 * DmeSqlException .
 *
 * @author Administrator .
 * @since 2020-12-08
 */
public class DmeSqlException extends DmeException {
    public DmeSqlException() {
        super();
    }

    public DmeSqlException(String message) {
        super(message);
    }

    /**
     * 构造方法.
     *
     * @param code 错误编码
     * @param message 错误信息
     */
    public DmeSqlException(String code, String message) {
        super(code, message);
    }
}
