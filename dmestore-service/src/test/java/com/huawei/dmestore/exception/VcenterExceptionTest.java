package com.huawei.dmestore.exception;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VcenterExceptionTest
 * @description TODO
 * @date 2020/12/3 10:23
 */
public class VcenterExceptionTest {

    @InjectMocks
    VcenterException vcenterException = new VcenterException();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vcenterException = new VcenterException("321");
        vcenterException = new VcenterException("321", "321");
    }

    @Test
    public void getCode() {
        vcenterException.getCode();

    }

    @Test
    public void setCode() {
        vcenterException.setCode("321");
    }

    @Test
    public void getMessage() {
        vcenterException.getMessage();
    }

    @Test
    public void setMessage() {
        vcenterException.setMessage("321");
    }
}