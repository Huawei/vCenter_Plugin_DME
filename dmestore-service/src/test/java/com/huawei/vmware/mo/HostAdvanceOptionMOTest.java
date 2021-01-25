package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.OptionValue;
import com.vmware.vim25.VimPortType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName HostAdvanceOptionMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 15:47:00
 */
public class HostAdvanceOptionMOTest {
    @Mock
    private VmwareContext context;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private HostAdvanceOptionMO hostAdvanceOptionMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void queryOptions() throws Exception{
        String name = "123";
        List<OptionValue> list = new ArrayList<>();
        when(service.queryOptions(anyObject(), eq(name))).thenReturn(list);
        hostAdvanceOptionMO.queryOptions(name);
    }

    @Test
    public void updateOptions() throws Exception{
        List<OptionValue> optionValues = new ArrayList<>();
        doNothing().when(service).updateOptions(anyObject(), anyObject());
        hostAdvanceOptionMO.updateOptions(optionValues);
    }
}