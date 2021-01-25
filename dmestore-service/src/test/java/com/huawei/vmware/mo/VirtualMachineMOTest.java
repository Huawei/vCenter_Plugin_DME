package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDiskMode;
import com.vmware.vim25.VirtualDiskType;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualSCSIController;

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
 * @ClassName VirtualMachineMOTest.java
 * @Description TODO
 * @createTime 2020年12月03日 10:14:00
 */
public class VirtualMachineMOTest {

    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private VirtualMachineMO virtualMachineMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void getOwnerDatacenter() throws Exception {
        List<ObjectContent> ocs = new ArrayList<>();
        ObjectContent oc = mock(ObjectContent.class);
        ocs.add(oc);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);
        ManagedObjectReference dataCenterObject = mock(ManagedObjectReference.class);
        when(oc.getObj()).thenReturn(dataCenterObject);
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(oc.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getVal()).thenReturn("aa");
        virtualMachineMO.getOwnerDatacenter();
    }

    @Test
    public void getRunningHost() throws Exception {
        VirtualMachineRuntimeInfo runtimeInfo = mock(VirtualMachineRuntimeInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("runtime"))).thenReturn(runtimeInfo);
        ManagedObjectReference host = mock(ManagedObjectReference.class);
        when(runtimeInfo.getHost()).thenReturn(host);
        virtualMachineMO.getRunningHost();
    }

    @Test
    public void getRuntimeInfo() throws Exception {
        VirtualMachineRuntimeInfo runtimeInfo = mock(VirtualMachineRuntimeInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("runtime"))).thenReturn(runtimeInfo);
        virtualMachineMO.getRuntimeInfo();
    }

    @Test
    public void getFileInfo() throws Exception {
        VirtualMachineFileInfo fileInfo = mock(VirtualMachineFileInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.files"))).thenReturn(fileInfo);
        virtualMachineMO.getFileInfo();
    }

    @Test
    public void getParentMor() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        virtualMachineMO.getParentMor();
    }

    @Test
    public void createDisk() throws Exception {
        String vmdkDatastorePath = "123";
        VirtualDiskType diskType = VirtualDiskType.RDM;
        VirtualDiskMode diskMode = VirtualDiskMode.PERSISTENT;
        String rdmDeviceName = "testa";
        long sizeInMb = 1024;
        ManagedObjectReference morDs = mock(ManagedObjectReference.class);
        int controllerKey = -1;
        List<VirtualDevice> devices = new ArrayList<>();
        VirtualLsiLogicController device = mock(VirtualLsiLogicController.class);
        devices.add(device);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.hardware.device"))).thenReturn(devices);
        when(device.getKey()).thenReturn(201);

        ManagedObjectReference morTask = mock(ManagedObjectReference.class);
        when(service.reconfigVMTask(anyObject(), anyObject())).thenReturn(morTask);
        when(vmwareClient.waitForTask(morTask)).thenReturn(true);
        doNothing().when(context).waitForTaskProgressDone(morTask);
        virtualMachineMO.createDisk(vmdkDatastorePath, diskType, diskMode, rdmDeviceName, sizeInMb, morDs,
            controllerKey);

        virtualMachineMO.createDisk(vmdkDatastorePath, VirtualDiskType.THIN, diskMode, rdmDeviceName, sizeInMb, morDs,
            controllerKey);
    }

    @Test
    public void getVmdkFileInfo() throws Exception {
        String vmdkDatastorePath = "2131";
        List<ObjectContent> ocs = new ArrayList<>();
        ObjectContent oc = mock(ObjectContent.class);
        ocs.add(oc);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);
        ManagedObjectReference dataCenterObject = mock(ManagedObjectReference.class);
        when(oc.getObj()).thenReturn(dataCenterObject);
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(oc.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getVal()).thenReturn("aa");

        String url = "12321";
        when(context.composeDatastoreBrowseUrl(anyObject(), anyString())).thenReturn(url);
        byte[] content = "aa=123".getBytes();
        when(context.getResourceContent(url)).thenReturn(content);
        virtualMachineMO.getVmdkFileInfo(vmdkDatastorePath);
    }

    @Test
    public void getScsiDeviceControllerKeyNoException() throws Exception {
        List<VirtualDevice> devices = new ArrayList<>();
        VirtualSCSIController device = mock(VirtualSCSIController.class);
        devices.add(device);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.hardware.device"))).thenReturn(devices);
        when(device.getKey()).thenReturn(201);

        virtualMachineMO.getScsiDeviceControllerKeyNoException();
    }
}