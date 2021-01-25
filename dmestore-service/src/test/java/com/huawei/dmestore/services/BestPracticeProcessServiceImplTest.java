package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.BestPracticeCheckDao;
import com.huawei.dmestore.model.BestPracticeBean;
import com.huawei.dmestore.services.bestpractice.*;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.huawei.vmware.VcConnectionHelpers;
import com.huawei.vmware.VmwarePluginConnectionHelpers;
import com.huawei.vmware.mo.*;
import com.huawei.vmware.util.DatastoreVmwareMoFactory;
import com.huawei.vmware.util.HostVmwareFactory;
import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;
import com.google.gson.Gson;
import com.vmware.vim25.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * BestPracticeProcessServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十一月 13, 2020</pre>
 */
public class BestPracticeProcessServiceImplTest {
    private Gson gson = new Gson();
    @Mock
    private VCSDKUtils vcsdkUtils;
    @Mock
    private BestPracticeCheckDao bestPracticeCheckDao;
    @InjectMocks
    private BestPracticeProcessServiceImpl bestPracticeProcessService = new BestPracticeProcessServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        List<BestPracticeService> bestPracticeServices = new ArrayList<>();
        bestPracticeServices.add(spy(Vmfs3EnableBlockDeleteImpl.class));
        bestPracticeServices.add(spy(Vmfs3HardwareAcceleratedLockingImpl.class));
        bestPracticeServices.add(spy(VMFS3UseATSForHBOnVMFS5Impl.class));
        bestPracticeServices.add(spy(DiskSchedQuantumImpl.class));
        bestPracticeServices.add(spy(DiskDiskMaxIOSizeImpl.class));
        bestPracticeServices.add(spy(DataMoverHardwareAcceleratedInitImpl.class));
        bestPracticeServices.add(spy(DataMoverHardwareAcceleratedMoveImpl.class));
        bestPracticeServices.add(spy(LunQueueDepthForEmulexImpl.class));
        bestPracticeServices.add(spy(LunQueueDepthForQlogicImpl.class));
        bestPracticeServices.add(spy(JumboFrameMTUImpl.class));
        bestPracticeServices.add(spy(NumberOfVolumesInDatastoreImpl.class));
        bestPracticeServices.add(spy(Vmfs6AutoReclaimImpl.class));
        bestPracticeServices.add(spy(NMPPathSwitchPolicyImpl.class));
        bestPracticeProcessService.setBestPracticeServices(bestPracticeServices);
    }

    /**
     * Method: getCheckRecord()
     */
    @Test
    public void testGetCheckRecord() throws Exception {
        List<BestPracticeBean> hostBeanList = new ArrayList<>();
        BestPracticeBean bestPracticeBean = new BestPracticeBean();
        hostBeanList.add(bestPracticeBean);
        when(bestPracticeCheckDao.getRecordBeanByHostsetting(anyString())).thenReturn(hostBeanList);
        bestPracticeProcessService.getCheckRecord();
    }

    /**
     * Method: getCheckRecordBy()
     */
    @Test
    public void testGetCheckRecordBy() throws Exception {
        List<BestPracticeBean> list = new ArrayList<>();
        list.add(new BestPracticeBean());
        when(bestPracticeCheckDao.getRecordByPage("test", 0, 100)).thenReturn(list);
        bestPracticeProcessService.getCheckRecordBy("test", 0, 100);
    }

    /**
     * Method: check()
     */
    @Test
    public void testCheck() throws Exception {
        String objectId = "123";
        vCenterInit();

        bestPracticeProcessService.check(objectId);
    }

    private void vCenterInit() throws Exception {
        String objectId = "123";
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("hostId", objectId);
        map.put("objectId", "123");
        map.put("hostName", "testHost");
        lists.add(map);
        List<BestPracticeService> bestPracticeServiceList = bestPracticeProcessService.getBestPracticeServices();
        when(vcsdkUtils.findHostById(objectId)).thenReturn(gson.toJson(lists));
        when(vcsdkUtils.getAllHosts()).thenReturn(gson.toJson(lists));
        ManagedObjectReference mor = mock(ManagedObjectReference.class);
        VcConnectionHelpers vcConnectionHelpers = mock(VmwarePluginConnectionHelpers.class);
        when(vcsdkUtils.getVcConnectionHelper()).thenReturn(vcConnectionHelpers);
        when(vcConnectionHelpers.objectId2Mor(objectId)).thenReturn(mor);
        VmwareContext context = mock(VmwareContext.class);
        when(vcConnectionHelpers.getServerContext(objectId)).thenReturn(context);
        HostMO hostMo = mock(HostMO.class);
        DatastoreMO datastoreMo = mock(DatastoreMO.class);
        HostVmwareFactory hostVmwareFactory = mock(HostVmwareFactory.class);
        DatastoreVmwareMoFactory datastoreVmwareMoFactory = mock(DatastoreVmwareMoFactory.class);
        for (BestPracticeService service : bestPracticeServiceList) {
            when(service.getHostMoFactory()).thenReturn(hostVmwareFactory);
            when(hostVmwareFactory.build(context, mor)).thenReturn(hostMo);

            List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = new ArrayList<>();
            Pair<ManagedObjectReference, String> pair = mock(Pair.class);
            datastoreMountsOnHost.add(pair);
            when(hostMo.getDatastoreMountsOnHost()).thenReturn(datastoreMountsOnHost);
            ManagedObjectReference dsMor = mock(ManagedObjectReference.class);
            when(pair.first()).thenReturn(dsMor);
            when(service.getDatastoreMoFactory()).thenReturn(datastoreVmwareMoFactory);
            when(datastoreVmwareMoFactory.build(context, dsMor)).thenReturn(datastoreMo);
        }
        HostAdvanceOptionMO hostAdvanceOptionMO = mock(HostAdvanceOptionMO.class);
        when(hostMo.getHostAdvanceOptionMo()).thenReturn(hostAdvanceOptionMO);
        List<OptionValue> optionValueList = new ArrayList<>();
        OptionValue optionValue = mock(OptionValue.class);
        optionValueList.add(optionValue);
        when(hostAdvanceOptionMO.queryOptions(anyString())).thenReturn(optionValueList);
        doNothing().when(hostAdvanceOptionMO).updateOptions(anyList());
        when(optionValue.getValue()).thenReturn("--");
        doNothing().when(optionValue).setValue(anyObject());
        HostKernelModuleSystemMO hostKernelModuleSystemMO = mock(HostKernelModuleSystemMO.class);
        when(hostMo.getHostKernelModuleSystemMo()).thenReturn(hostKernelModuleSystemMO);
        when(hostKernelModuleSystemMO.queryConfiguredModuleOptionString(anyString())).thenReturn("aa=5");
        doNothing().when(hostKernelModuleSystemMO).updateModuleOptionString(anyString(), anyString());
        HostStorageSystemMO hostStorageSystemMo = mock(HostStorageSystemMO.class);
        when(hostMo.getHostStorageSystemMo()).thenReturn(hostStorageSystemMo);
        doNothing().when(hostStorageSystemMo).setMultipathLunPolicy(anyString(), anyObject());

        HostStorageDeviceInfo hostStorageDeviceInfo = mock(HostStorageDeviceInfo.class);
        when(hostStorageSystemMo.getStorageDeviceInfo()).thenReturn(hostStorageDeviceInfo);
        HostMultipathInfo hostMultipathInfo = mock(HostMultipathInfo.class);
        when(hostStorageDeviceInfo.getMultipathInfo()).thenReturn(hostMultipathInfo);
        List<HostMultipathInfoLogicalUnit> logicalUnits = new ArrayList<>();
        HostMultipathInfoLogicalUnit unit = mock(HostMultipathInfoLogicalUnit.class);
        logicalUnits.add(unit);
        when(hostMultipathInfo.getLun()).thenReturn(logicalUnits);
        HostMultipathInfoLogicalUnitPolicy policy = mock(HostMultipathInfoLogicalUnitPolicy.class);
        when(unit.getPolicy()).thenReturn(policy);
        when(policy.getPolicy()).thenReturn("cc");

        List<HostVirtualSwitch> virtualSwitches = new ArrayList<>();
        HostVirtualSwitch virtualSwitch = mock(HostVirtualSwitch.class);
        virtualSwitches.add(virtualSwitch);
        HostNetworkInfo hostNetworkInfo = mock(HostNetworkInfo.class);
        when(hostMo.getHostNetworkInfo()).thenReturn(hostNetworkInfo);
        when(hostNetworkInfo.getVswitch()).thenReturn(virtualSwitches);
        HostVirtualSwitchSpec spec = mock(HostVirtualSwitchSpec.class);
        when(virtualSwitch.getSpec()).thenReturn(spec);
        when(spec.getMtu()).thenReturn(10);
        doNothing().when(spec).setMtu(anyInt());

        HostNetworkSystemMO hostNetworkSystemMo = mock(HostNetworkSystemMO.class);
        when(hostMo.getHostNetworkSystemMo()).thenReturn(hostNetworkSystemMo);
        doNothing().when(hostNetworkSystemMo).updateVirtualSwitch(anyString(), anyObject());
        HostNetworkConfig networkConfig = mock(HostNetworkConfig.class);
        when(hostNetworkSystemMo.getNetworkConfig()).thenReturn(networkConfig);
        List<HostVirtualSwitchConfig> hostVirtualSwitchConfigs = new ArrayList<>();
        HostVirtualSwitchConfig config = mock(HostVirtualSwitchConfig.class);
        hostVirtualSwitchConfigs.add(config);
        when(networkConfig.getVswitch()).thenReturn(hostVirtualSwitchConfigs);
        when(config.getSpec()).thenReturn(spec);
        doNothing().when(hostNetworkSystemMo).updateVirtualSwitch(anyString(), anyObject());
        when(hostNetworkSystemMo.updateNetworkConfig(anyObject(), anyString())).thenReturn(null);

        DatastoreSummary summary = mock(DatastoreSummary.class);
        when(datastoreMo.getSummary()).thenReturn(summary);
        when(summary.getType()).thenReturn("VMFS");
        VmfsDatastoreInfo vmfsDatastoreInfo = mock(VmfsDatastoreInfo.class);
        when(datastoreMo.getVmfsDatastoreInfo()).thenReturn(vmfsDatastoreInfo);
        HostVmfsVolume hostVmfsVolume = mock(HostVmfsVolume.class);
        when(vmfsDatastoreInfo.getVmfs()).thenReturn(hostVmfsVolume);
        when(hostVmfsVolume.getVersion()).thenReturn("6.21");
        when(hostVmfsVolume.getUnmapPriority()).thenReturn("aa");
        List<HostScsiDiskPartition> extentList = new ArrayList<>();
        HostScsiDiskPartition diskPartition = mock(HostScsiDiskPartition.class);
        HostScsiDiskPartition diskPartition1 = mock(HostScsiDiskPartition.class);
        extentList.add(diskPartition);
        extentList.add(diskPartition1);
        when(hostVmfsVolume.getExtent()).thenReturn(extentList);
        doNothing().when(hostVmfsVolume).setUnmapPriority(anyString());
    }
}
