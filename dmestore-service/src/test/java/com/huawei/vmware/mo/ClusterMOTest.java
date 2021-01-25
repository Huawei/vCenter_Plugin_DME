package com.huawei.vmware.mo;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.huawei.vmware.util.HostVmwareFactory;
import com.huawei.vmware.util.VmwareClient;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ClusterDasConfigInfo;
import com.vmware.vim25.ClusterDasVmConfigInfo;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.DasVmPriority;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ServiceContent;
import com.vmware.vim25.VimPortType;
import com.vmware.vim25.VirtualMachineConfigOption;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangxiangyong
 * @className ClusterMOTest
 * @description TODO
 * @date 2020/11/25 15:28
 */
public class ClusterMOTest {
    @Mock
    private VmwareContext context;

    @Mock
    private HostVmwareFactory hostFactory;

    private VmwareClient vmwareClient;

    private VimPortType service;

    @InjectMocks
    ClusterMO clusterMO;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        vmwareClient = mock(VmwareClient.class);
        when(context.getVimClient()).thenReturn(vmwareClient);

        service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
    }

    @Test
    public void getHyperHostName() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("name"))).thenReturn("sss");
        clusterMO.getHyperHostName();
    }

    @Test
    public void getDasConfig() throws Exception {
        ClusterConfigInfoEx clusterConfigInfoEx = mock(ClusterConfigInfoEx.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configurationEx"))).thenReturn(clusterConfigInfoEx);
        when(clusterConfigInfoEx.getDasConfig()).thenReturn(mock(ClusterDasConfigInfo.class));
        clusterMO.getDasConfig();
    }

    @Test
    public void isHaEnabled() throws Exception {
        ClusterConfigInfoEx clusterConfigInfoEx = mock(ClusterConfigInfoEx.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configurationEx"))).thenReturn(clusterConfigInfoEx);
        ClusterDasConfigInfo clusterDasConfigInfo = mock(ClusterDasConfigInfo.class);
        when(clusterConfigInfoEx.getDasConfig()).thenReturn(clusterDasConfigInfo);
        when(clusterDasConfigInfo.isEnabled()).thenReturn(true);
        clusterMO.isHaEnabled();
    }

    @Test
    public void setRestartPriorityForVm() throws Exception {
        String priority = "high";
        String vmName = "123";
        VirtualMachineMO vmMo = mock(VirtualMachineMO.class);
        ManagedObjectReference vmMor = mock(ManagedObjectReference.class);
        when(vmMo.getMor()).thenReturn(vmMor);
        when(vmMor.getType()).thenReturn("VirtualMachine");
        when(vmMor.getValue()).thenReturn(vmName);
        ClusterConfigInfoEx configInfoEx = mock(ClusterConfigInfoEx.class);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("configurationEx"))).thenReturn(configInfoEx);
        List<ClusterDasVmConfigInfo> dasVmConfigs = new ArrayList<>();
        ClusterDasVmConfigInfo vmConfigInfo = mock(ClusterDasVmConfigInfo.class);
        dasVmConfigs.add(vmConfigInfo);
        when(configInfoEx.getDasVmConfig()).thenReturn(dasVmConfigs);
        ManagedObjectReference vmConfigInfoKey = mock(ManagedObjectReference.class);
        when(vmConfigInfo.getKey()).thenReturn(vmConfigInfoKey);
        when(vmConfigInfoKey.getValue()).thenReturn(vmName);
        DasVmPriority dasVmPriority = DasVmPriority.fromValue("low");
        when(vmConfigInfo.getRestartPriority()).thenReturn(dasVmPriority);

        VimPortType vimPortType = mock(VimPortType.class);
        when(context.getService()).thenReturn(vimPortType);
        ManagedObjectReference morTask = mock(ManagedObjectReference.class);
        when(vimPortType.reconfigureComputeResourceTask(anyObject(), anyObject(), anyBoolean())).thenReturn(morTask);
        when(vmwareClient.waitForTask(morTask)).thenReturn(true);
        doNothing().when(context).waitForTaskProgressDone(morTask);

        clusterMO.setRestartPriorityForVm(vmMo, priority);
    }

    @Test
    public void getHyperHostDatacenter() throws Exception {
        List<ObjectContent> ocs = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        ocs.add(objectContent);
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(objectContent.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getVal()).thenReturn("sa");

        clusterMO.getHyperHostDatacenter();
    }

    @Test
    public void getHyperHostOwnerResourcePool() throws Exception {
        when(vmwareClient.getDynamicProperty(anyObject(), eq("resourcePool"))).thenReturn(
            mock(ManagedObjectReference.class));
        clusterMO.getHyperHostOwnerResourcePool();
    }

    @Test
    public void getHyperHostCluster() throws Exception {
        clusterMO.getHyperHostCluster();
    }

    @Test
    public void listVmsOnHyperHost() throws Exception {
        String vmName = "123";
        List<ManagedObjectReference> hosts = new ArrayList<>();
        ManagedObjectReference host = mock(ManagedObjectReference.class);
        hosts.add(host);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("host"))).thenReturn(hosts);

        HostMO hostMo = mock(HostMO.class);
        when(hostFactory.build(anyObject(), anyString())).thenReturn(hostMo);
        List<VirtualMachineMO> vms = new ArrayList<>();
        VirtualMachineMO vm = mock(VirtualMachineMO.class);
        vms.add(vm);
        when(hostMo.listVmsOnHyperHost(vmName)).thenReturn(vms);
        try {
            clusterMO.listVmsOnHyperHost(vmName);
        } catch (Exception ex) {

        }
    }

    @Test
    public void findVmOnHyperHost() throws Exception {
        String name = "123";
        int key = 11;
        String instanceNameCustomField = "value[" + key + "]";
        ServiceContent content = mock(ServiceContent.class);
        ManagedObjectReference customFieldsManager = mock(ManagedObjectReference.class);
        when(context.getServiceContent()).thenReturn(content);
        when(content.getCustomFieldsManager()).thenReturn(customFieldsManager);

        List<CustomFieldDef> fields = new ArrayList<>();
        CustomFieldDef field = mock(CustomFieldDef.class);
        fields.add(field);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("field"))).thenReturn(fields);
        String fieldName = "cloud.vm.internal.name";
        when(field.getName()).thenReturn(fieldName);
        String vmType = "VirtualMachine";
        when(field.getManagedObjectType()).thenReturn(vmType);
        when(field.getKey()).thenReturn(key);
        List<ObjectContent> ocs = new ArrayList<>();
        ObjectContent oc = mock(ObjectContent.class);
        ocs.add(oc);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);
        clusterMO.findVmOnHyperHost(name);
    }

    @Test
    public void getVmPropertiesOnHyperHost() throws Exception {
        String[] propertyPaths = {"123"};
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        List<ObjectContent> list = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        list.add(objectContent);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(list);
        clusterMO.getVmPropertiesOnHyperHost(propertyPaths);
    }

    @Test
    public void getDatastorePropertiesOnHyperHost() throws Exception {
        String[] propertyPaths = {"123"};
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        List<ObjectContent> list = new ArrayList<>();
        ObjectContent objectContent = mock(ObjectContent.class);
        list.add(objectContent);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(list);
        clusterMO.getDatastorePropertiesOnHyperHost(propertyPaths);
    }

    @Test
    public void mountDatastore() {
        boolean vmfsDatastore = true;
        String poolHostAddress = "";
        int poolHostPort = 26335;
        String poolPath = "/123";
        String poolUuid = "123214";
        try {
            //clusterMO.mountDatastore(vmfsDatastore, poolHostAddress, poolHostPort, poolPath, poolUuid);
        } catch (Exception ex) {

        }
    }

    @Test
    public void unmountDatastore() {
        String poolUuid = "2312";
        try {
            //clusterMO.unmountDatastore(poolUuid);
        } catch (Exception ex) {

        }
    }

    @Test
    public void getHyperHostNetworkSummary() throws Exception {
        String esxServiceConsolePort = "200";
        List<ManagedObjectReference> hosts = new ArrayList<>();
        ManagedObjectReference host = mock(ManagedObjectReference.class);
        hosts.add(host);
        when(vmwareClient.getDynamicProperty(anyObject(), eq("host"))).thenReturn(hosts);
        try {
            clusterMO.getHyperHostNetworkSummary(esxServiceConsolePort);
        } catch (Exception ex) {

        }
    }

    @Test
    public void getRecommendedDiskController() throws Exception {
        String guestOsId = "1232";
        ManagedObjectReference environmentBrowser = mock(ManagedObjectReference.class);
        when(vmwareClient.getMoRefProp(anyObject(), eq("environmentBrowser"))).thenReturn(environmentBrowser);
        VimPortType service = mock(VimPortType.class);
        when(context.getService()).thenReturn(service);
        VirtualMachineConfigOption vmConfigOption = mock(VirtualMachineConfigOption.class);
        when(service.queryConfigOption(environmentBrowser, null, null)).thenReturn(vmConfigOption);
        List<GuestOsDescriptor> guestDescriptors = new ArrayList<>();
        GuestOsDescriptor guestOsDescriptor = mock(GuestOsDescriptor.class);
        guestDescriptors.add(guestOsDescriptor);
        when(vmConfigOption.getGuestOSDescriptor()).thenReturn(guestDescriptors);
        when(guestOsDescriptor.getId()).thenReturn(guestOsId);
        when(guestOsDescriptor.getRecommendedDiskController()).thenReturn("ParaVirtualSCSIController");

        clusterMO.getRecommendedDiskController(guestOsId);
    }

    @Test
    public void getClusterHosts() throws Exception {
        List<ObjectContent> ocs = new ArrayList<>();
        ObjectContent oc = mock(ObjectContent.class);
        ocs.add(oc);
        when(service.retrieveProperties(anyObject(), anyObject())).thenReturn(ocs);
        ManagedObjectReference morHost = mock(ManagedObjectReference.class);
        when(oc.getObj()).thenReturn(morHost);
        List<DynamicProperty> dynamicPropertyList = new ArrayList<>();
        DynamicProperty dynamicProperty = mock(DynamicProperty.class);
        dynamicPropertyList.add(dynamicProperty);
        when(oc.getPropSet()).thenReturn(dynamicPropertyList);
        when(dynamicProperty.getVal()).thenReturn("cc");

        clusterMO.getClusterHosts();
    }
}