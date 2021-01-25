package com.huawei.dmestore.exception;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VersionNotSupportExceptionTest
 * @description TODO
 * @date 2020/12/3 10:27
 */
public class VersionNotSupportExceptionTest {

    @InjectMocks
    VersionNotSupportException versionNotSupportException = new VersionNotSupportException("321");

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        versionNotSupportException = new VersionNotSupportException("321");
    }

    @Test
    public void getReturnCode() {
        VersionNotSupportException.getReturnCode();
    }
}