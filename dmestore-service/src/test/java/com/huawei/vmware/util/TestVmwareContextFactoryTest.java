package com.huawei.vmware.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className TestVmwareContextFactoryTest
 * @description TODO
 * @date 2020/12/2 16:52
 */
public class TestVmwareContextFactoryTest {

    @InjectMocks
    TestVmwareContextFactory testVmwareContextFactory;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void create() {
    }

    @Test
    public void getContext() throws Exception {
        //String vCenterAddress, int vCenterPort, String vCenterUserName, String vCenterPassword
        testVmwareContextFactory.getContext("321", 321, "321", "321");
    }
}