package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.IscsiPortInfo;
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
 * @ClassName IscsiManagerMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 19:48:00
 */
public class IscsiManagerMOTest {
    @Mock
    private VmwareContext context;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private IscsiManagerMO iscsiManagerMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void bindVnic() throws Exception {
        String iScsiHbaName = "112";
        String vnicDevice = "454";
        doNothing().when(service).bindVnic(anyObject(), anyString(), anyString());
        iscsiManagerMO.bindVnic(iScsiHbaName, vnicDevice);
    }

    @Test
    public void queryBoundVnics() throws Exception {
        String iScsiHbaName = "123";
        List<IscsiPortInfo> list = new ArrayList<>();
        when(service.queryBoundVnics(anyObject(), anyString())).thenReturn(list);
        iscsiManagerMO.queryBoundVnics(iScsiHbaName);
    }
}