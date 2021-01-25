package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VmfsDatastoreInfo;

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
 * @ClassName DatastoreMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 14:26:00
 */
public class DatastoreMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private DatastoreMO datastoreMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void getName() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("11");
        datastoreMO.getName();
    }

    @Test
    public void getInfo() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("info"))).thenReturn(mock(DatastoreInfo.class));
        datastoreMO.getInfo();
    }

    @Test
    public void getVmfsDatastoreInfo() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("info"))).thenReturn(mock(VmfsDatastoreInfo.class));
        datastoreMO.getVmfsDatastoreInfo();
    }

    @Test
    public void getSummary() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("summary"))).thenReturn(mock(DatastoreSummary.class));
        datastoreMO.getSummary();
    }

    @Test
    public void getVm() throws Exception {
        List<ManagedObjectReference> list = new ArrayList<>();
        when(vmwareClient.getDynamicProperty(anyObject(), eq("vm"))).thenReturn(list);
        datastoreMO.getVm();
    }

    @Test
    public void getHostMounts() throws Exception {
        List<DatastoreHostMount> list = new ArrayList<>();
        when(vmwareClient.getDynamicProperty(anyObject(), eq("host"))).thenReturn(list);
        datastoreMO.getHostMounts();
    }

    @Test
    public void getOwnerDatacenter() throws Exception {
        List<ObjectContent> ocs = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        ocs.add(objectContent);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getVal()).thenReturn("sa");

        datastoreMO.getOwnerDatacenter();
    }

    @Test
    public void renameDatastore() throws Exception {
        String newDatastoreName = "aa";
        doNothing().when(service).renameDatastore(mor, newDatastoreName);
        datastoreMO.renameDatastore(newDatastoreName);
    }

    @Test
    public void moveDatastoreFile() throws Exception {
        String srcFilePath = "13";
        ManagedObjectReference morSrcDc = mock(ManagedObjectReference.class);
        ManagedObjectReference morDestDs = mock(ManagedObjectReference.class);
        String destFilePath = "11";
        ManagedObjectReference morDestDc = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("11");
        ServiceContent serviceContent = mock(ServiceContent.class);
        when(context.getServiceContent()).thenReturn(serviceContent);
        ManagedObjectReference morFileManager = mock(ManagedObjectReference.class);
        when(serviceContent.getFileManager()).thenReturn(morFileManager);

        ManagedObjectReference morTask = mock(ManagedObjectReference.class);
        String srcFullPath = String.format("[%s] %s", "11", srcFilePath);
        String destFullPath = String.format("[%s] %s", "11", destFilePath);
        when(service.moveDatastoreFileTask(morFileManager, srcFullPath, morSrcDc, destFullPath, morDestDc,
            true)).thenReturn(morTask);
        when(vmwareClient.waitForTask(morTask)).thenReturn(true);
        doNothing().when(context).waitForTaskProgressDone(morTask);
        datastoreMO.moveDatastoreFile(srcFilePath, morSrcDc, morDestDs, destFilePath, morDestDc, true);
    }
    @Test
    public void refreshDatastore() throws Exception {
       doNothing().when(service).refreshDatastore(anyObject());
       datastoreMO.refreshDatastore();
    }
}