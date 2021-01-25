package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.HostFileSystemVolumeInfo;
import com.vmware.vim25.HostInternetScsiHbaSendTarget;
import com.vmware.vim25.HostMultipathInfoLogicalUnitPolicy;
import com.vmware.vim25.HostStorageDeviceInfo;
import com.vmware.vim25.ManagedObjectReference;
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
 * @ClassName HostStorageSystemMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 19:04:00
 */
public class HostStorageSystemMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private HostStorageSystemMO hostStorageSystemMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void getStorageDeviceInfo() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("storageDeviceInfo"))).thenReturn(
            mock(HostStorageDeviceInfo.class));
        hostStorageSystemMO.getStorageDeviceInfo();
    }

    @Test
    public void getHostFileSystemVolumeInfo() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("fileSystemVolumeInfo"))).thenReturn(
            mock(HostFileSystemVolumeInfo.class));
        hostStorageSystemMO.getHostFileSystemVolumeInfo();
    }

    @Test
    public void rescanHba() throws Exception {
        String iScsiHbaDevice = "aa";
        doNothing().when(service).rescanHba(anyObject(), anyString());
        hostStorageSystemMO.rescanHba(iScsiHbaDevice);
    }

    @Test
    public void rescanVmfs() throws Exception {
        doNothing().when(service).rescanVmfs(anyObject());
        hostStorageSystemMO.rescanVmfs();
    }

    @Test
    public void refreshStorageSystem() throws Exception {
        doNothing().when(service).refreshStorageSystem(anyObject());
        hostStorageSystemMO.refreshStorageSystem();
    }

    @Test
    public void mountVmfsVolume() throws Exception {
        String datastoreUuid = "123";
        doNothing().when(service).mountVmfsVolume(anyObject(), anyString());
        hostStorageSystemMO.mountVmfsVolume(datastoreUuid);
    }

    @Test
    public void unmountVmfsVolume() throws Exception {
        String datastoreUuid = "123";
        doNothing().when(service).unmountVmfsVolume(anyObject(), anyString());
        hostStorageSystemMO.unmountVmfsVolume(datastoreUuid);
    }

    @Test
    public void setMultipathLunPolicy() throws Exception {
        String lunId = "11";
        HostMultipathInfoLogicalUnitPolicy unitPolicy = mock(HostMultipathInfoLogicalUnitPolicy.class);
        doNothing().when(service).setMultipathLunPolicy(anyObject(), anyString(), anyObject());
        hostStorageSystemMO.setMultipathLunPolicy(lunId, unitPolicy);
    }

    @Test
    public void addInternetScsiSendTargets() throws Exception {
        String iScsiHbaDevice = "132";
        List<HostInternetScsiHbaSendTarget> targets = new ArrayList<>();
        doNothing().when(service).addInternetScsiSendTargets(anyObject(), anyString(), anyObject());
        hostStorageSystemMO.addInternetScsiSendTargets(iScsiHbaDevice, targets);
    }
}