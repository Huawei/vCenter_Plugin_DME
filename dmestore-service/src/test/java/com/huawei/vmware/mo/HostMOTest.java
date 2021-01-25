package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.ClusterVmwareMoFactory;
import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.ClusterDasConfigInfo;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HostConfigManager;
import com.vmware.vim25.HostIpConfig;
import com.vmware.vim25.HostNasVolume;
import com.vmware.vim25.HostNetworkInfo;
import com.vmware.vim25.HostVirtualNic;
import com.vmware.vim25.HostVirtualNicSpec;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NasDatastoreInfo;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VirtualNicManagerNetConfig;

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
 * @ClassName HostMOTest.java
 * @Description TODO
 * @createTime 2020年12月01日 10:36:00
 */
public class HostMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private ClusterVmwareMoFactory clusterVmwareMoFactory;

    private VimPortType service;

    @InjectMocks
    private HostMO hostMo;

    private VmwareClient vmwareClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);
        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);

    }

    @Test
    public void getHostConfigManager() throws Exception {
        HostConfigManager configManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(configManager);
        hostMo.getHostConfigManager();
    }

    @Test
    public void getHostVirtualNicManagerNetConfig() throws Exception {
        List<VirtualNicManagerNetConfig> list = new ArrayList<>();
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.virtualNicManagerInfo.netConfig"))).thenReturn(
            list);
        hostMo.getHostVirtualNicManagerNetConfig();
    }

    @Test
    public void getHostNetworkInfo() throws Exception {
        HostNetworkInfo hostNetworkInfo = mock(HostNetworkInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.network"))).thenReturn(hostNetworkInfo);
        hostMo.getHostNetworkInfo();
    }

    @Test
    public void getHyperHostName() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("aa");
        hostMo.getHyperHostName();
    }

    @Test
    public void getDasConfig() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterVmwareMoFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.getDasConfig()).thenReturn(mock(ClusterDasConfigInfo.class));
        hostMo.getDasConfig();
    }

    @Test
    public void isHaEnabled() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterVmwareMoFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.isHaEnabled()).thenReturn(true);
        hostMo.isHaEnabled();
    }

    @Test
    public void setRestartPriorityForVm() throws Exception {
        String priority = "cca";
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterVmwareMoFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.isHaEnabled()).thenReturn(true);
        VirtualMachineMO vmMo = mock(VirtualMachineMO.class);
        doNothing().when(clusterMO).setRestartPriorityForVm(anyObject(), eq(priority));
        hostMo.setRestartPriorityForVm(vmMo, priority);
    }

    @Test
    public void getHostStorageSystemMo() throws Exception {
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager.storageSystem"))).thenReturn(
            managedObjectReference);
        hostMo.getHostStorageSystemMo();
    }

    @Test
    public void getHostDatastoreSystemMo() throws Exception {
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager.datastoreSystem"))).thenReturn(
            managedObjectReference);
        hostMo.getHostDatastoreSystemMo();
    }

    @Test
    public void getHostAdvanceOptionMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getAdvancedOption()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHostAdvanceOptionMo();
    }

    @Test
    public void getHostKernelModuleSystemMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getKernelModuleSystem()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHostKernelModuleSystemMo();
    }

    @Test
    public void getIscsiManagerMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getIscsiManager()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getIscsiManagerMo();
    }

    @Test
    public void getHostNetworkSystemMo() throws Exception {
        HostConfigManager hostConfigManager = mock(HostConfigManager.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager"))).thenReturn(hostConfigManager);
        when(hostConfigManager.getNetworkSystem()).thenReturn(mock(ManagedObjectReference.class));
        hostMo.getHostNetworkSystemMo();
    }

    @Test
    public void getHyperHostDatacenter() throws Exception {
        List<ObjectContent> objectContentList = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        objectContentList.add(objectContent);
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(objectContentList);
        when(objectContent.getObj()).thenReturn(mock(ManagedObjectReference.class));
        List<DynamicProperty> dynamicPropertieList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertieList.add(dynamicProperty);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertieList);
        when(dynamicProperty.getVal()).thenReturn("cc");
        hostMo.getHyperHostDatacenter();
    }

    @Test
    public void getHyperHostOwnerResourcePool() throws Exception {
        ManagedObjectReference morComputerResource = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(morComputerResource);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("resourcePool"))).thenReturn(
            mock(ManagedObjectReference.class));
        hostMo.getHyperHostOwnerResourcePool();
    }

    @Test
    public void getHyperHostCluster() throws Exception {
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        hostMo.getHyperHostCluster();
    }

    @Test
    public void getHostAboutInfo() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.product"))).thenReturn(mock(AboutInfo.class));
        hostMo.getHostAboutInfo();
    }

    @Test
    public void getHostType() throws Exception {
        AboutInfo aboutInfo = mock(AboutInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.product"))).thenReturn(aboutInfo);
        when(aboutInfo.getName()).thenReturn("VMware ESX");
        hostMo.getHostType();
    }

    @Test
    public void getHostType2() throws Exception {
        AboutInfo aboutInfo = mock(AboutInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.product"))).thenReturn(aboutInfo);
        when(aboutInfo.getName()).thenReturn("VMware ESXi");
        hostMo.getHostType();
    }

    @Test
    public void getHostName() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("cc");
        hostMo.getHostName();
    }

    @Test
    public void listVmsOnHyperHost() throws Exception {
        String vmName = "12";
        ServiceContent serviceContent = mock(ServiceContent.class);
        when(context.getServiceContent()).thenReturn(serviceContent);
        when(serviceContent.getCustomFieldsManager()).thenReturn(mock(ManagedObjectReference.class));
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = mock(ObjectContent.class);
        properties.add(content);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);
        List<DynamicProperty> props = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        props.add(dynamicProperty);
        when(content.getPropSet()).thenReturn(props);
        when(dynamicProperty.getName()).thenReturn("name");
        when(dynamicProperty.getVal()).thenReturn(vmName);
        hostMo.listVmsOnHyperHost(vmName);
    }

    @Test
    public void listVmsOnHyperHost2() throws Exception {
        String vmName = "12";
        ServiceContent serviceContent = mock(ServiceContent.class);
        when(context.getServiceContent()).thenReturn(serviceContent);
        when(serviceContent.getCustomFieldsManager()).thenReturn(mock(ManagedObjectReference.class));
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = mock(ObjectContent.class);
        properties.add(content);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);
        List<DynamicProperty> props = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        props.add(dynamicProperty);
        when(content.getPropSet()).thenReturn(props);
        when(dynamicProperty.getName()).thenReturn("value[11]");
        CustomFieldStringValue fieldStringValue = mock(CustomFieldStringValue.class);
        when(dynamicProperty.getVal()).thenReturn(fieldStringValue);
        String vmInternalCsName = "i-9-9-";
        when(fieldStringValue.getValue()).thenReturn(vmInternalCsName);
        hostMo.listVmsOnHyperHost(null);
    }

    @Test
    public void findVmOnHyperHost() throws Exception {
        String vmName = "12";
        try {
            hostMo.findVmOnHyperHost(vmName);
        } catch (Exception ex) {

        }
    }

    @Test
    public void getVmPropertiesOnHyperHost() throws Exception {
        String[] propertyPaths = {"11", "22"};
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = mock(ObjectContent.class);
        properties.add(content);
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);
        hostMo.getVmPropertiesOnHyperHost(propertyPaths);
    }

    @Test
    public void getDatastorePropertiesOnHyperHost() throws Exception {
        String[] propertyPaths = {"33", "44"};
        List<ObjectContent> properties = new ArrayList();
        ObjectContent content = mock(ObjectContent.class);
        properties.add(content);
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(properties);
        hostMo.getDatastorePropertiesOnHyperHost(propertyPaths);
    }

    @Test
    public void getDatastoreMountsOnHost() throws Exception {
        List<ObjectContent> ocs = new ArrayList();
        ObjectContent oc = mock(ObjectContent.class);
        ocs.add(oc);
        ManagedObjectReference ocObj = mock(ManagedObjectReference.class);
        when(oc.getObj()).thenReturn(ocObj);
        List<DynamicProperty> list = new ArrayList<>();
        DynamicProperty dynamicProperty = spy(DynamicProperty.class);
        list.add(dynamicProperty);
        when(dynamicProperty.getVal()).thenReturn("kk");
        when(oc.getPropSet()).thenReturn(list);
        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        when(vimPortType.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);

        hostMo.getDatastoreMountsOnHost();
    }

    @Test
    public void getExistingDataStoreOnHost() throws Exception {
        String hostAddress = "122";
        String path = "12";
        HostDatastoreSystemMO hostDatastoreSystemMo = mock(HostDatastoreSystemMO.class);
        List<ManagedObjectReference> morArray = new ArrayList<>();
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        morArray.add(managedObjectReference);
        when(hostDatastoreSystemMo.getDatastores()).thenReturn(morArray);
        NasDatastoreInfo nasDatastoreInfo = mock(NasDatastoreInfo.class);
        when(hostDatastoreSystemMo.getNasDatastoreInfo(anyObject())).thenReturn(nasDatastoreInfo);
        HostNasVolume hostNasVolume = mock(HostNasVolume.class);
        when(nasDatastoreInfo.getNas()).thenReturn(hostNasVolume);
        when(hostNasVolume.getRemoteHost()).thenReturn(hostAddress);
        when(hostNasVolume.getRemotePath()).thenReturn(path);

        hostMo.getExistingDataStoreOnHost(hostAddress, path, hostDatastoreSystemMo);
    }


    @Test
    public void unmountDatastore() throws Exception {
        String uuid = "123";
        ManagedObjectReference datastoreSystemMo = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configManager.datastoreSystem"))).thenReturn(
            datastoreSystemMo);
        ServiceContent serviceContent = mock(ServiceContent.class);
        when(context.getServiceContent()).thenReturn(serviceContent);
        ManagedObjectReference customFieldsManager = mock(ManagedObjectReference.class);
        when(serviceContent.getCustomFieldsManager()).thenReturn(customFieldsManager);

        List<CustomFieldDef> fields = new ArrayList<>();
        CustomFieldDef field = mock(CustomFieldDef.class);
        fields.add(field);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("field"))).thenReturn(fields);
        when(field.getName()).thenReturn("cloud.uuid");
        when(field.getManagedObjectType()).thenReturn("Datastore");
        when(field.getKey()).thenReturn(10);

        List<ObjectContent> ocs = new ArrayList<>();
        ObjectContent oc = mock(ObjectContent.class);
        ocs.add(oc);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(oc.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getVal()).thenReturn(uuid);
        ManagedObjectReference morDatastore = mock(ManagedObjectReference.class);
        when(oc.getObj()).thenReturn(morDatastore);
        doNothing().when(service).removeDatastore(anyObject(), anyObject());
        hostMo.unmountDatastore(morDatastore);

    }

    @Test
    public void getHyperHostNetworkSummary() throws Exception {
        String managementPortGroup = "123";
        AboutInfo aboutInfo = mock(AboutInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.product"))).thenReturn(aboutInfo);
        when(aboutInfo.getName()).thenReturn("VMware ESXi");
        List<VirtualNicManagerNetConfig> netConfigList = new ArrayList<>();
        VirtualNicManagerNetConfig netConfig = mock(VirtualNicManagerNetConfig.class);
        netConfigList.add(netConfig);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.virtualNicManagerInfo.netConfig"))).thenReturn(
            netConfigList);
        when(netConfig.getNicType()).thenReturn("management");
        List<HostVirtualNic> nicList = new ArrayList<>();
        HostVirtualNic nic = spy(HostVirtualNic.class);
        nicList.add(nic);
        when(netConfig.getCandidateVnic()).thenReturn(nicList);
        when(nic.getPortgroup()).thenReturn(managementPortGroup);
        HostVirtualNicSpec spec = mock(HostVirtualNicSpec.class);
        when(nic.getSpec()).thenReturn(spec);
        HostIpConfig hostIpConfig = mock(HostIpConfig.class);
        when(spec.getIp()).thenReturn(hostIpConfig);
        when(hostIpConfig.getIpAddress()).thenReturn("192.168.1.5");
        when(hostIpConfig.getSubnetMask()).thenReturn("192.168.1.1");
        when(spec.getMac()).thenReturn("192:168:1:1");
        hostMo.getHyperHostNetworkSummary(managementPortGroup);
    }

    @Test
    public void getHyperHostNetworkSummary1() throws Exception {
        String managementPortGroup = "123";
        AboutInfo aboutInfo = mock(AboutInfo.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.product"))).thenReturn(aboutInfo);
        when(aboutInfo.getName()).thenReturn("VMware ESX");
        List<HostVirtualNic> hostNics = new ArrayList<>();
        HostVirtualNic vnic = mock(HostVirtualNic.class);
        hostNics.add(vnic);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("config.network.consoleVnic"))).thenReturn(hostNics);
        when(vnic.getPortgroup()).thenReturn(managementPortGroup);
        HostVirtualNicSpec nicSpec =  mock(HostVirtualNicSpec.class);
        when(vnic.getSpec()).thenReturn(nicSpec);
        HostIpConfig hostIpConfig = mock(HostIpConfig.class);
        when(nicSpec.getIp()).thenReturn(hostIpConfig);
        when(hostIpConfig.getIpAddress()).thenReturn("192.168.3.112");
        when(hostIpConfig.getSubnetMask()).thenReturn("11:112:33");
        when(nicSpec.getMac()).thenReturn("s");

        hostMo.getHyperHostNetworkSummary(managementPortGroup);
    }

    @Test
    public void getRecommendedDiskController() throws Exception {
        String guestOsId = "11";
        ManagedObjectReference parent = mock(ManagedObjectReference.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("parent"))).thenReturn(parent);
        when(parent.getType()).thenReturn("ClusterComputeResource");
        ClusterMO clusterMO = mock(ClusterMO.class);
        when(clusterVmwareMoFactory.build(context, parent)).thenReturn(clusterMO);
        when(clusterMO.getRecommendedDiskController(guestOsId)).thenReturn("aa");
        try {
            hostMo.getRecommendedDiskController(guestOsId);
        } catch (Exception ex) {
        }
    }

    @Test
    public void getHostNetworks() throws Exception {
        List<ManagedObjectReference> list = new ArrayList<>();
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        list.add(managedObjectReference);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("network"))).thenReturn(list);

        hostMo.getHostNetworks();
    }

    @Test
    public void getNetworkName() throws Exception {
        String netMorVal = "123";
        List<ManagedObjectReference> list = new ArrayList<>();
        ManagedObjectReference managedObjectReference = mock(ManagedObjectReference.class);
        list.add(managedObjectReference);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("network"))).thenReturn(list);
        when(managedObjectReference.getValue()).thenReturn(netMorVal);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("sss");
        hostMo.getNetworkName(netMorVal);
    }
}