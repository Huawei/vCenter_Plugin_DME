package com.huawei.dmestore.services;

import org.junit.Test;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName DMEOpenApiServiceTest.java
 * @Description TODO
 * @createTime 2020年12月03日 18:23:00
 */
public class DMEOpenApiServiceTest {

    @Test
    public void isSuccessResponse() {
        Object[] objects = {0, 0.0, "0"};
        for(Object object : objects){
            DMEOpenApiService.isSuccessResponse(object);
        }
    }
}