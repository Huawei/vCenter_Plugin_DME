package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HostDiskPartitionSpec;
import com.vmware.vim25.HostScsiDisk;
import com.vmware.vim25.HostVmfsSpec;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NasDatastoreInfo;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VmfsDatastoreCreateSpec;
import com.vmware.vim25.VmfsDatastoreExpandSpec;
import com.vmware.vim25.VmfsDatastoreOption;

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
 * @ClassName HostDatastoreSystemMOTest.java
 * @Description TODO
 * @createTime 2020年12月02日 16:01:00
 */
public class HostDatastoreSystemMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ManagedObjectReference mor;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    private HostDatastoreSystemMO hostDatastoreSystemMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    private void findStoreInit() throws Exception {
        String name = "13";
        ServiceContent serviceContent = mock(ServiceContent.class);
        when(context.getServiceContent()).thenReturn(serviceContent);
        ManagedObjectReference getCustomFieldsManager = mock(ManagedObjectReference.class);
        when(serviceContent.getCustomFieldsManager()).thenReturn(getCustomFieldsManager);
        List<CustomFieldDef> customFieldDefList = new ArrayList<>();
        CustomFieldDef customFieldDef = mock(CustomFieldDef.class);
        customFieldDefList.add(customFieldDef);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("field"))).thenReturn(customFieldDefList);
        String fieldName = "cloud.uuid";
        String morType = "Datastore";
        int key = 10;
        when(customFieldDef.getName()).thenReturn(fieldName);
        when(customFieldDef.getManagedObjectType()).thenReturn(morType);
        when(customFieldDef.getKey()).thenReturn(key);

        List<ObjectContent> objectContentList = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        objectContentList.add(objectContent);
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(objectContentList);
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty1 = mock(DynamicProperty.class);
        DynamicProperty dynamicProperty2 = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty1);
        dynamicPropertyList.add(dynamicProperty2);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty1.getVal()).thenReturn(name + "1");

        CustomFieldStringValue customFieldStringValue = mock(CustomFieldStringValue.class);
        when(dynamicProperty2.getVal()).thenReturn(customFieldStringValue);
        when(customFieldStringValue.getValue()).thenReturn(name);
        when(objectContent.getObj()).thenReturn(mock(ManagedObjectReference.class));
    }

    @Test
    public void queryVmfsDatastoreExpandOptions() throws Exception {
        DatastoreMO datastoreMo = mock(DatastoreMO.class);
        List<VmfsDatastoreOption> list = new ArrayList<>();
        when(service.queryVmfsDatastoreExpandOptions(anyObject(), anyObject())).thenReturn(list);
        hostDatastoreSystemMO.queryVmfsDatastoreExpandOptions(datastoreMo);
    }

    @Test
    public void expandVmfsDatastore() throws Exception {
        DatastoreMO datastoreMo = mock(DatastoreMO.class);
        VmfsDatastoreExpandSpec vmfsDatastoreExpandSpec = mock(VmfsDatastoreExpandSpec.class);
        when(service.expandVmfsDatastore(anyObject(), anyObject(), anyObject())).thenReturn(
            mock(ManagedObjectReference.class));
        hostDatastoreSystemMO.expandVmfsDatastore(datastoreMo, vmfsDatastoreExpandSpec);
    }

    @Test
    public void queryAvailableDisksForVmfs() throws Exception {
        List<HostScsiDisk> hostScsiDiskList = new ArrayList<>();
        when(service.queryAvailableDisksForVmfs(anyObject(), anyObject())).thenReturn(hostScsiDiskList);
        hostDatastoreSystemMO.queryAvailableDisksForVmfs();
    }

    @Test
    public void createVmfsDatastore() throws Exception {
        String datastoreName = "11";
        HostScsiDisk hostScsiDisk = mock(HostScsiDisk.class);
        int vmfsMajorVersion = 1;
        int blockSize = 1024;
        long totalSectors = 10240;
        int unmapGranularity = 2;
        String unmapPriority = "aa";
        List<VmfsDatastoreOption> list = new ArrayList<>();
        VmfsDatastoreOption vmfsDatastoreOption = mock(VmfsDatastoreOption.class);
        list.add(vmfsDatastoreOption);
        when(service.queryVmfsDatastoreCreateOptions(mor, hostScsiDisk.getDevicePath(), vmfsMajorVersion)).thenReturn(
            list);

        VmfsDatastoreCreateSpec spec = mock(VmfsDatastoreCreateSpec.class);
        when(vmfsDatastoreOption.getSpec()).thenReturn(spec);
        HostVmfsSpec hostVmfsSpec = mock(HostVmfsSpec.class);
        when(spec.getVmfs()).thenReturn(hostVmfsSpec);

        HostDiskPartitionSpec hostDiskPartitionSpec = mock(HostDiskPartitionSpec.class);
        when(spec.getPartition()).thenReturn(hostDiskPartitionSpec);

        when(service.createVmfsDatastore(mor, spec)).thenReturn(mock(ManagedObjectReference.class));

        hostDatastoreSystemMO.createVmfsDatastore(datastoreName, hostScsiDisk, vmfsMajorVersion, blockSize,
            totalSectors, unmapGranularity, unmapPriority);
    }

    @Test
    public void deleteDatastore() throws Exception {
        findStoreInit();
        String name = "13";
        doNothing().when(service).removeDatastore(anyObject(), anyObject());
        hostDatastoreSystemMO.deleteDatastore(null);
    }

    @Test
    public void createNfsDatastore() throws Exception {
        String host = "192.168.3.115";
        int port = 16;
        String exportPath = "123";
        String uuid = "12";
        String accessMode = "111";
        String type = "122";
        String securityType = "4";
        when(service.createNasDatastore(anyObject(), anyObject())).thenReturn(mock(ManagedObjectReference.class));
        hostDatastoreSystemMO.createNfsDatastore(host, port, exportPath, uuid, accessMode, type, securityType);
    }

    @Test
    public void getDatastores() throws Exception {
        List<ManagedObjectReference> list = new ArrayList<>();
        when(vmwareClient.getDynamicProperty(anyObject(), eq("datastore"))).thenReturn(list);
        hostDatastoreSystemMO.getDatastores();
    }

    @Test
    public void getDatastoreInfo() throws Exception {
        ManagedObjectReference morDatastore = mock(ManagedObjectReference.class);
        DatastoreInfo datastoreInfo = mock(DatastoreInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("info"))).thenReturn(datastoreInfo);
        hostDatastoreSystemMO.getDatastoreInfo(morDatastore);
    }

    @Test
    public void getNasDatastoreInfo() throws Exception {
        ManagedObjectReference morDatastore = mock(ManagedObjectReference.class);
        NasDatastoreInfo datastoreInfo = mock(NasDatastoreInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("info"))).thenReturn(datastoreInfo);
        hostDatastoreSystemMO.getNasDatastoreInfo(morDatastore);
    }

    @Test
    public void getDatastorePropertiesOnHostDatastoreSystem() throws Exception {
        String[] propertyPaths = {"1232"};
        List<ObjectContent> list = new ArrayList<>();
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(list);
        hostDatastoreSystemMO.getDatastorePropertiesOnHostDatastoreSystem(propertyPaths);
    }
}