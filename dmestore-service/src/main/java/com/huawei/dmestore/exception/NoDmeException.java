package com.huawei.dmestore.exception;

/**
 * NoDmeException
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class NoDmeException extends IgnorableException {
    /**
     * NoDmeException
     */
    public NoDmeException() {
        super("-90002", "no esight in DB");
    }
}
