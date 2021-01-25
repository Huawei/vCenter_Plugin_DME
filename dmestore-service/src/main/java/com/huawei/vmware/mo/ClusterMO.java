package com.huawei.vmware.mo;

import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.utils.StringUtil;
import com.huawei.vmware.util.HostVmwareFactory;
import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;
import com.huawei.vmware.util.VmwareHelper;
import com.vmware.vim25.ArrayUpdateOperation;
import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ClusterConfigSpecEx;
import com.vmware.vim25.ClusterDasConfigInfo;
import com.vmware.vim25.ClusterDasVmConfigInfo;
import com.vmware.vim25.ClusterDasVmConfigSpec;
import com.vmware.vim25.ClusterDasVmSettings;
import com.vmware.vim25.ClusterDasVmSettingsRestartPriority;
import com.vmware.vim25.DasVmPriority;
import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VirtualMachineConfigOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ClusterMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class ClusterMO extends BaseMO implements VmwareHypervisorHost {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterMO.class);

    private static final String VIRTUAL_MACHINE = "VirtualMachine";

    private static final String HOST_PROPERTY = "host";

    private static final String CLUSTERCOMPUTERESOURCE_TYPE = "ClusterComputeResource";

    private ManagedObjectReference environmentBrowser = null;

    private HostVmwareFactory hostFactory = HostVmwareFactory.getInstance();

    /**
     * ClusterMO
     *
     * @param context    context
     * @param morCluster morCluster
     */
    public ClusterMO(VmwareContext context, ManagedObjectReference morCluster) {
        super(context, morCluster);
    }

    @Override
    public String getHyperHostName() throws Exception {
        return getName();
    }

    @Override
    public ClusterDasConfigInfo getDasConfig() throws Exception {
        ClusterConfigInfoEx configInfo = getClusterConfigInfo();
        if (configInfo != null) {
            return configInfo.getDasConfig();
        }

        return null;
    }

    /**
     * getClusterConfigInfo
     *
     * @return ClusterConfigInfoEx
     * @throws Exception Exception
     */
    public ClusterConfigInfoEx getClusterConfigInfo() throws Exception {
        ClusterConfigInfoEx configInfo = context.getVimClient().getDynamicProperty(mor, "configurationEx");
        return configInfo;
    }

    @Override
    public boolean isHaEnabled() throws Exception {
        ClusterDasConfigInfo dasConfig = getDasConfig();
        if (dasConfig != null && dasConfig.isEnabled() != null && dasConfig.isEnabled()) {
            return true;
        }

        return false;
    }

    private String getRestartPriorityForVm(VirtualMachineMO vmMo) throws Exception {
        if (vmMo == null) {
            return null;
        }

        ManagedObjectReference vmMor = vmMo.getMor();
        if (vmMor == null || !VIRTUAL_MACHINE.equals(vmMor.getType())) {
            return null;
        }

        ClusterConfigInfoEx configInfo = getClusterConfigInfo();
        if (configInfo == null) {
            return null;
        }

        List<ClusterDasVmConfigInfo> dasVmConfig = configInfo.getDasVmConfig();
        for (int dasVmConfigIndex = 0; dasVmConfigIndex < dasVmConfig.size(); dasVmConfigIndex++) {
            ClusterDasVmConfigInfo dasVmConfigInfo = dasVmConfig.get(dasVmConfigIndex);
            if (dasVmConfigInfo != null && dasVmConfigInfo.getKey().getValue().equals(vmMor.getValue())) {
                DasVmPriority dasVmPriority = dasVmConfigInfo.getRestartPriority();
                if (dasVmPriority != null) {
                    return dasVmPriority.value();
                } else {
                    return ClusterDasVmSettingsRestartPriority.CLUSTER_RESTART_PRIORITY.value();
                }
            }
        }

        return null;
    }

    @Override
    public void setRestartPriorityForVm(VirtualMachineMO vmMo, String priority) throws Exception {
        if (vmMo == null || StringUtil.isBlank(priority)) {
            return;
        }

        ManagedObjectReference vmMor = vmMo.getMor();
        if (vmMor == null || !VIRTUAL_MACHINE.equals(vmMor.getType())) {
            LOGGER.debug("Failed to set restart priority for VM: " + vmMo.getName() + ", invalid VM object reference");
            return;
        }

        String currentVmRestartPriority = getRestartPriorityForVm(vmMo);
        if (StringUtil.isNotBlank(currentVmRestartPriority) && currentVmRestartPriority.equalsIgnoreCase(priority)) {
            return;
        }

        ClusterDasVmSettings clusterDasVmSettings = new ClusterDasVmSettings();
        clusterDasVmSettings.setRestartPriority(priority);

        ClusterDasVmConfigInfo clusterDasVmConfigInfo = new ClusterDasVmConfigInfo();
        clusterDasVmConfigInfo.setKey(vmMor);
        clusterDasVmConfigInfo.setDasSettings(clusterDasVmSettings);

        ClusterDasVmConfigSpec clusterDasVmConfigSpec = new ClusterDasVmConfigSpec();
        clusterDasVmConfigSpec.setOperation(
            (StringUtil.isNotBlank(currentVmRestartPriority)) ? ArrayUpdateOperation.EDIT : ArrayUpdateOperation.ADD);
        clusterDasVmConfigSpec.setInfo(clusterDasVmConfigInfo);

        ClusterConfigSpecEx clusterConfigSpecEx = new ClusterConfigSpecEx();
        ClusterDasConfigInfo clusterDasConfigInfo = new ClusterDasConfigInfo();
        clusterConfigSpecEx.setDasConfig(clusterDasConfigInfo);
        clusterConfigSpecEx.getDasVmConfigSpec().add(clusterDasVmConfigSpec);

        ManagedObjectReference morTask = context.getService()
            .reconfigureComputeResourceTask(mor, clusterConfigSpecEx, true);

        boolean result = context.getVimClient().waitForTask(morTask);

        if (result) {
            context.waitForTaskProgressDone(morTask);
        } else {
            LOGGER.error(
                "Set restart priority failed for VM: " + vmMo.getName() + " due to " + TaskMO.getTaskFailureInfo(
                    context, morTask));
        }
    }

    @Override
    public ManagedObjectReference getHyperHostDatacenter() throws Exception {
        Pair<DatacenterMO, String> dcPair = DatacenterMO.getOwnerDatacenter(getContext(), getMor());
        return dcPair.first().getMor();
    }

    @Override
    public ManagedObjectReference getHyperHostOwnerResourcePool() throws Exception {
        return context.getVimClient().getDynamicProperty(getMor(), "resourcePool");
    }

    @Override
    public ManagedObjectReference getHyperHostCluster() {
        return mor;
    }

    @Override
    public synchronized List<VirtualMachineMO> listVmsOnHyperHost(String vmName) throws Exception {
        List<VirtualMachineMO> vms = new ArrayList<>();
        List<ManagedObjectReference> hosts = context.getVimClient().getDynamicProperty(mor, HOST_PROPERTY);
        if (hosts != null && hosts.size() > 0) {
            for (ManagedObjectReference morHost : hosts) {
                HostMO hostMo = hostFactory.build(context, morHost);
                vms.addAll(hostMo.listVmsOnHyperHost(vmName));
            }
        }
        return vms;
    }

    @Override
    public VirtualMachineMO findVmOnHyperHost(String name) throws Exception {
        int key = getCustomFieldKey(VIRTUAL_MACHINE, CustomFieldConstants.CLOUD_VM_INTERNAL_NAME);
        String instanceNameCustomField = "value[" + key + "]";
        ObjectContent[] ocs = getVmPropertiesOnHyperHost(new String[] {"name", instanceNameCustomField});
        return HypervisorHostHelper.findVmFromObjectContent(context, ocs, name, instanceNameCustomField);
    }

    @Override
    public ObjectContent[] getVmPropertiesOnHyperHost(String[] propertyPaths) throws Exception {
        PropertySpec pspec = new PropertySpec();
        pspec.setType(VIRTUAL_MACHINE);
        pspec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec host2VmFolderTraversal = new TraversalSpec();
        host2VmFolderTraversal.setType("HostSystem");
        host2VmFolderTraversal.setPath("vm");
        host2VmFolderTraversal.setName("host2VmFolderTraversal");

        TraversalSpec cluster2HostFolderTraversal = new TraversalSpec();
        cluster2HostFolderTraversal.setType(CLUSTERCOMPUTERESOURCE_TYPE);
        cluster2HostFolderTraversal.setPath(HOST_PROPERTY);
        cluster2HostFolderTraversal.setName("cluster2HostFolderTraversal");
        cluster2HostFolderTraversal.getSelectSet().add(host2VmFolderTraversal);

        ObjectSpec ospec = new ObjectSpec();
        ospec.setObj(getMor());
        ospec.setSkip(true);
        ospec.getSelectSet().add(cluster2HostFolderTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pspec);
        pfSpec.getObjectSet().add(ospec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    @Override
    public ObjectContent[] getDatastorePropertiesOnHyperHost(String[] propertyPaths) throws Exception {
        PropertySpec pspec = new PropertySpec();
        pspec.setType("Datastore");
        pspec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec cluster2DatastoreTraversal = new TraversalSpec();
        cluster2DatastoreTraversal.setType(CLUSTERCOMPUTERESOURCE_TYPE);
        cluster2DatastoreTraversal.setPath("datastore");
        cluster2DatastoreTraversal.setName("cluster2DatastoreTraversal");

        ObjectSpec ospec = new ObjectSpec();
        ospec.setObj(mor);
        ospec.setSkip(Boolean.TRUE);
        ospec.getSelectSet().add(cluster2DatastoreTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pspec);
        pfSpec.getObjectSet().add(ospec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);

        return properties.toArray(new ObjectContent[properties.size()]);
    }

    /**
     * getHostPropertiesOnCluster
     *
     * @param propertyPaths propertyPaths
     * @return ObjectContent[] ObjectContent
     * @throws Exception Exception
     */
    public ObjectContent[] getHostPropertiesOnCluster(String[] propertyPaths) throws Exception {
        PropertySpec pspec = new PropertySpec();
        pspec.setType("HostSystem");
        pspec.getPathSet().addAll(Arrays.asList(propertyPaths));

        TraversalSpec cluster2HostTraversal = new TraversalSpec();
        cluster2HostTraversal.setType(CLUSTERCOMPUTERESOURCE_TYPE);
        cluster2HostTraversal.setPath(HOST_PROPERTY);
        cluster2HostTraversal.setName("cluster2HostTraversal");

        ObjectSpec ospec = new ObjectSpec();
        ospec.setObj(mor);
        ospec.setSkip(Boolean.TRUE);
        ospec.getSelectSet().add(cluster2HostTraversal);

        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pspec);
        pfSpec.getObjectSet().add(ospec);

        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
        pfSpecArr.add(pfSpec);

        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    public void unmountDatastore(ManagedObjectReference datastoremo) throws Exception {
        List<ManagedObjectReference> hosts = context.getVimClient().getDynamicProperty(mor, HOST_PROPERTY);
        if (hosts != null && hosts.size() > 0) {
            for (ManagedObjectReference morHost : hosts) {
                HostMO hostMo = new HostMO(context, morHost);
                hostMo.unmountDatastore(datastoremo);
            }
        }
    }

    @Override
    public VmwareHypervisorHostNetworkSummary getHyperHostNetworkSummary(String esxServiceConsolePort)
        throws Exception {
        List<ManagedObjectReference> hosts = context.getVimClient().getDynamicProperty(mor, HOST_PROPERTY);
        if (hosts != null && hosts.size() > 0) {
            return new HostMO(context, hosts.get(0)).getHyperHostNetworkSummary(esxServiceConsolePort);
        }
        return null;
    }

    /**
     * getClusterHosts
     *
     * @return List
     * @throws Exception Exception
     */
    public List<Pair<ManagedObjectReference, String>> getClusterHosts() throws Exception {
        List<Pair<ManagedObjectReference, String>> hosts = new ArrayList<>();

        ObjectContent[] ocs = getHostPropertiesOnCluster(new String[] {"name"});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                ManagedObjectReference morHost = oc.getObj();
                String name = (String) oc.getPropSet().get(0).getVal();

                hosts.add(new Pair<>(morHost, name));
            }
        }
        return hosts;
    }

    private ManagedObjectReference getEnvironmentBrowser() throws Exception {
        if (environmentBrowser == null) {
            environmentBrowser = context.getVimClient().getMoRefProp(mor, "environmentBrowser");
        }
        return environmentBrowser;
    }

    @Override
    public String getRecommendedDiskController(String guestOsId) throws Exception {
        VirtualMachineConfigOption vmConfigOption = context.getService()
            .queryConfigOption(getEnvironmentBrowser(), null, null);
        GuestOsDescriptor guestOsDescriptor = null;
        String diskController;
        List<GuestOsDescriptor> guestDescriptors = vmConfigOption.getGuestOSDescriptor();
        for (GuestOsDescriptor descriptor : guestDescriptors) {
            if (guestOsId != null && guestOsId.equalsIgnoreCase(descriptor.getId())) {
                guestOsDescriptor = descriptor;
                break;
            }
        }
        if (guestOsDescriptor != null) {
            diskController = VmwareHelper.getRecommendedDiskControllerFromDescriptor(guestOsDescriptor);
            return diskController;
        } else {
            String msg = "Unable to retrieve recommended disk controller for guest OS : " + guestOsId + " in cluster "
                + getHyperHostName();
            LOGGER.error(msg);
            throw new VcenterException(msg);
        }
    }
}
