package com.huawei.dmestore.exception;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className DMEExceptionTest
 * @description TODO
 * @date 2020/12/3 10:12
 */
public class DmeExceptionTest {

    @InjectMocks
    DmeException dmeException = new DmeException();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        dmeException = new DmeException("321", "321");
    }

    @Test
    public void getMessage() {
        dmeException.getMessage();
    }

    @Test
    public void getCode() {
        dmeException.getCode();

    }

    @Test
    public void setCode() {
        dmeException.setCode("321");

    }

    @Test
    public void setMessage() {
        dmeException = new DmeException("321");
        dmeException.setMessage("321");

    }
}