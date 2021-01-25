package com.huawei.dmestore.exception;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className ServiceExceptionTest
 * @description TODO
 * @date 2020/12/3 10:20
 */
public class ServiceExceptionTest {

    @InjectMocks
    ServiceException serviceException = new ServiceException();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        serviceException = new ServiceException("321");
        serviceException = new ServiceException("321","321");
    }

    @Test
    public void getCode() {
        serviceException.getCode();
    }

    @Test
    public void setCode() {
        serviceException.setCode("321");

    }

    @Test
    public void getMessage() {
        serviceException.getMessage();

    }

    @Test
    public void setMessage() {
        serviceException.setMessage("321");

    }
}