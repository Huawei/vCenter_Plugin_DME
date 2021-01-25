package com.huawei.dmestore.exception;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VcenterRuntimeExceptionTest
 * @description TODO
 * @date 2020/12/3 10:25
 */
public class VcenterRuntimeExceptionTest {

    @InjectMocks
    VcenterRuntimeException vcenterRuntimeException = new VcenterRuntimeException();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vcenterRuntimeException = new VcenterRuntimeException("321");
        vcenterRuntimeException = new VcenterRuntimeException("321", "321");
    }

    @Test
    public void getCode() {
        vcenterRuntimeException.getCode();
    }

    @Test
    public void setCode() {
        vcenterRuntimeException.setCode("321");

    }

    @Test
    public void getMessage() {
        vcenterRuntimeException.getMessage();
    }

    @Test
    public void setMessage() {
        vcenterRuntimeException.setMessage("");
    }
}
