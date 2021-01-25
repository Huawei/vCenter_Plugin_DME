package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.HostNetworkConfig;
import com.vmware.vim25.HostNetworkConfigResult;
import com.vmware.vim25.HostVirtualSwitchSpec;
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
 * @ClassName HostNetworkSystemMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 17:59:00
 */
public class HostNetworkSystemMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private HostNetworkSystemMO hostNetworkSystemMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void getNetworkConfig() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("networkConfig"))).thenReturn(
            mock(HostNetworkConfig.class));
        hostNetworkSystemMO.getNetworkConfig();
    }

    @Test
    public void updateVirtualSwitch() throws Exception {
        String vSwitchName = "111";
        HostVirtualSwitchSpec spec = mock(HostVirtualSwitchSpec.class);
        doNothing().when(service).updateVirtualSwitch(anyObject(), anyString(), anyObject());
        hostNetworkSystemMO.updateVirtualSwitch(vSwitchName, spec);
    }

    @Test
    public void updateNetworkConfig() throws Exception {
        HostNetworkConfig config = mock(HostNetworkConfig.class);
        String changeMode = "123";
        HostNetworkConfigResult hostNetworkConfigResult = mock(HostNetworkConfigResult.class);
        when(service.updateNetworkConfig(anyObject(), anyObject(), anyString())).thenReturn(hostNetworkConfigResult);
        hostNetworkSystemMO.updateNetworkConfig(config, changeMode);
    }
}