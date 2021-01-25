package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VimPortType;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName HostKernelModuleSystemMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 17:53:00
 */
public class HostKernelModuleSystemMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private HostKernelModuleSystemMO hostKernelModuleSystemMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void queryConfiguredModuleOptionString() throws Exception {
        String name = "312";
        when(service.queryConfiguredModuleOptionString(anyObject(), anyObject())).thenReturn("ss");
        hostKernelModuleSystemMO.queryConfiguredModuleOptionString(name);
    }

    @Test
    public void updateModuleOptionString() throws Exception {
        String name = "test";
        String options = "ss";
        doNothing().when(service).updateModuleOptionString(anyObject(), anyObject(), anyObject());
        hostKernelModuleSystemMO.updateModuleOptionString(name, options);
    }
}