package com.huawei.vmware;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VmwarePluginConnectionHelperTest
 * @description TODO
 * @date 2020/12/3 15:40
 */
public class VmwarePluginConnectionHelperTest {

    @InjectMocks
    VmwarePluginConnectionHelpers vmwarePluginConnectionHelper = new VmwarePluginConnectionHelpers();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getServerContext() throws Exception {
        vmwarePluginConnectionHelper.getServerContext("321");
    }

    @Test
    public void getAllContext() throws Exception {
        vmwarePluginConnectionHelper.getAllContext();
    }
}