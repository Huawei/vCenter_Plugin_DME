package com.huawei.vmware.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.huawei.vmware.autosdk.SessionHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className TaggingWorkflowFactoryTest
 * @description TODO
 * @date 2020/12/1 20:36
 */
public class TaggingWorkflowFactoryTest {

    @InjectMocks
    TaggingWorkflowFactory factory;

    SessionHelper sessionHelper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        sessionHelper = mock(SessionHelper.class);
    }

    @Test
    public void getInstance() {
        TaggingWorkflowFactory.getInstance();
    }

    @Test
    public void build() throws Exception {
        factory.build(sessionHelper);
    }
}