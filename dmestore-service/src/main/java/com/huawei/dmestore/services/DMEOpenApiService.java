package com.huawei.dmestore.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMEOpenApiService
 *
 * @author yy
 * @since 2020-09-03
 **/
public class DMEOpenApiService {
    protected static final int FAIL_CODE = -99999;

    protected static final int RESULT_SUCCESS_CODE = 0;

    protected static final double RESULT_SUCCESS_CODE_DOUBLE = 0.0;

    protected static final int RESULT_ERROR_CODE = 10000;

    protected static final String CODE_SUCCESS = String.valueOf(RESULT_SUCCESS_CODE);

    protected static final String CODE_SUCCESS_DOUBLE = String.valueOf(RESULT_SUCCESS_CODE_DOUBLE);

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static boolean isSuccessResponse(Object code) {
        if (code != null) {
            if (code instanceof Integer) {
                return (Integer) code == Integer.parseInt(CODE_SUCCESS);
            } else if (code instanceof Double) {
                return (Double) code == Double.parseDouble(CODE_SUCCESS_DOUBLE);
            } else if (code instanceof String) {
                return CODE_SUCCESS.equals(code.toString()) || CODE_SUCCESS_DOUBLE.equals(code.toString());
            }
        }
        return false;
    }
}
