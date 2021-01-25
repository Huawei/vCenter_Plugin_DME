package com.huawei.vmware.mo;

import com.huawei.vmware.util.ClusterVmwareMoFactory;
import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.AlreadyExistsFaultMsg;
import com.vmware.vim25.ClusterDasConfigInfo;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.HostConfigManager;
import com.vmware.vim25.HostNetworkInfo;
import com.vmware.vim25.HostVirtualNic;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NasDatastoreInfo;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.TraversalSpec;
import com.vmware.vim25.VirtualNicManagerNetConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HostMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class HostMO extends BaseMO implements VmwareHypervisorHost {
    private static final Logger logger = LoggerFactory.getLogger(HostMO.class);

    private static final String CLUSTER_COMPUTE_RESOURCE = "ClusterComputeResource";

    private final String strHostSystem = "HostSystem";

    private final String strName = "name";

    private Map<String, VirtualMachineMO> vmCache = new HashMap<>();

    private ClusterVmwareMoFactory clusterVmwareMoFactory = ClusterVmwareMoFactory.getInstance();

    /**
     * HostMO
     *
     * @param context context
     * @param morHost morHost
     */
    public HostMO(VmwareContext context, ManagedObjectReference morHost) {
        super(context, morHost);
    }

    /**
     * HostMO
     *
     * @param context context
     * @param morType morType
     * @param morValue morValue
     */
    public HostMO(VmwareContext context, String morType, String morValue) {
        super(context, morType, morValue);
    }

    /**
     * HostMO
     *
     * @param context context
     * @param hostName hostName
     * @throws Exception Exception
     */
    public HostMO(VmwareContext context, String hostName) throws Exception {
        super(context, null);
        mor = this.context.getVimClient().getDecendentMoRef(this.context.getRootFolder(), strHostSystem, hostName);
        if (mor == null) {
            logger.error("Unable to locate host " + hostName);
        }
    }

    /**
     * getHostConfigManager
     *
     * @return HostConfigManager
     * @throws Exception Exception
     */
    public HostConfigManager getHostConfigManager() throws Exception {
        return (HostConfigManager) context.getVimClient().getDynamicProperty(mor, "configManager");
    }

    /**
     * getHostVirtualNicManagerNetConfig
     *
     * @return List
     * @throws Exception Exception
     */
    public List<VirtualNicManagerNetConfig> getHostVirtualNicManagerNetConfig() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "config.virtualNicManagerInfo.netConfig");
    }

    /**
     * getHostNetworkInfo
     *
     * @return HostNetworkInfo
     * @throws Exception Exception
     */
    public HostNetworkInfo getHostNetworkInfo() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "config.network");
    }

    @Override
    public String getHyperHostName() throws Exception {
        return getName();
    }

    @Override
    public ClusterDasConfigInfo getDasConfig() throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = clusterVmwareMoFactory.build(context, morParent);
            return clusterMo.getDasConfig();
        }
        return null;
    }

    @Override
    public boolean isHaEnabled() throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = clusterVmwareMoFactory.build(context, morParent);
            return clusterMo.isHaEnabled();
        }
        return false;
    }

    @Override
    public void setRestartPriorityForVm(VirtualMachineMO vmMo, String priority) throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = new ClusterMO(context, morParent);
            clusterMo.setRestartPriorityForVm(vmMo, priority);
        }
    }

    /**
     * getHostStorageSystemMo
     *
     * @return HostStorageSystemMO
     * @throws Exception Exception
     */
    public HostStorageSystemMO getHostStorageSystemMo() throws Exception {
        return new HostStorageSystemMO(context,
            context.getVimClient().getDynamicProperty(mor, "configManager.storageSystem"));
    }

    /**
     * getHostDatastoreSystemMo
     *
     * @return HostDatastoreSystemMO
     * @throws Exception Exception
     */
    public HostDatastoreSystemMO getHostDatastoreSystemMo() throws Exception {
        return new HostDatastoreSystemMO(context,
            context.getVimClient().getDynamicProperty(mor, "configManager.datastoreSystem"));
    }

    /**
     * HostAdvanceOptionMO
     *
     * @return HostAdvanceOptionMO
     * @throws Exception Exception
     */
    public HostAdvanceOptionMO getHostAdvanceOptionMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new HostAdvanceOptionMO(context, configMgr.getAdvancedOption());
    }

    /**
     * getHostKernelModuleSystemMo
     *
     * @return HostKernelModuleSystemMO
     * @throws Exception
     */
    public HostKernelModuleSystemMO getHostKernelModuleSystemMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new HostKernelModuleSystemMO(context, configMgr.getKernelModuleSystem());
    }

    /**
     * getIscsiManagerMo
     *
     * @return IscsiManagerMO
     * @throws Exception Exception
     */
    public IscsiManagerMO getIscsiManagerMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new IscsiManagerMO(context, configMgr.getIscsiManager());
    }

    /**
     * getHostNetworkSystemMo
     *
     * @return HostNetworkSystemMO
     * @throws Exception Exception
     */
    public HostNetworkSystemMO getHostNetworkSystemMo() throws Exception {
        HostConfigManager configMgr = getHostConfigManager();
        return new HostNetworkSystemMO(context, configMgr.getNetworkSystem());
    }

    @Override
    public ManagedObjectReference getHyperHostDatacenter() throws Exception {
        Pair<DatacenterMO, String> dcPair = DatacenterMO.getOwnerDatacenter(getContext(), getMor());
        assert dcPair != null;
        return dcPair.first().getMor();
    }

    @Override
    public ManagedObjectReference getHyperHostOwnerResourcePool() throws Exception {
        ManagedObjectReference morComputerResource = context.getVimClient().getDynamicProperty(mor, "parent");
        return context.getVimClient().getDynamicProperty(morComputerResource, "resourcePool");
    }

    @Override
    public ManagedObjectReference getHyperHostCluster() throws Exception {
        ManagedObjectReference morParent = context.getVimClient().getDynamicProperty(mor, "parent");
        if (CLUSTER_COMPUTE_RESOURCE.equalsIgnoreCase(morParent.getType())) {
            return morParent;
        }
        throw new Exception("Standalone host is not supported");
    }

    /**
     * getHostAboutInfo
     *
     * @return AboutInfo
     * @throws Exception Exception
     */
    public AboutInfo getHostAboutInfo() throws Exception {
        return (AboutInfo) context.getVimClient().getDynamicProperty(mor, "config.product");
    }

    /**
     * getHostType
     *
     * @return VmwareHostType
     * @throws Exception Exception
     */
    public VmwareHostType getHostType() throws Exception {
        AboutInfo aboutInfo = getHostAboutInfo();
        if ("VMware ESXi".equals(aboutInfo.getName())) {
            return VmwareHostType.ESXi;
        } else if ("VMware ESX".equals(aboutInfo.getName())) {
            return VmwareHostType.ESX;
        }
        throw new Exception("Unrecognized VMware host type " + aboutInfo.getName());
    }

    /**
     * getHostName
     *
     * @return String
     * @throws Exception Exception
     */
    public String getHostName() throws Exception {
        return (String) context.getVimClient().getDynamicProperty(mor, strName);
    }

    @Override
    public synchronized List<VirtualMachineMO> listVmsOnHyperHost(String vmName) throws Exception {
        List<VirtualMachineMO> vms = new ArrayList<>();
        if (vmName != null && !vmName.isEmpty()) {
            vms.add(findVmOnHyperHost(vmName));
        } else {
            loadVmCache();
            vms.addAll(vmCache.values());
        }
        return vms;
    }

    @Override
    public synchronized VirtualMachineMO findVmOnHyperHost(String vmName) throws Exception {
        VirtualMachineMO vmMo = vmCache.get(vmName);
        if (vmMo != null) {
            return vmMo;
        }
        loadVmCache();
        return vmCache.get(vmName);
    }

    private boolean isUserVmInternalCsName(String vmInternalCsName) {
        String pattern = "^[i][-][0-9]+[-][0-9]+[-]";
        Pattern pattern1 = Pattern.compile(pattern);
        java.util.regex.Matcher matcher = pattern1.matcher(vmInternalCsName);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    private void loadVmCache() throws Exception {
        vmCache.clear();
        int key = getCustomFieldKey("VirtualMachine", CustomFieldConstants.CLOUD_VM_INTERNAL_NAME);
        if (key == 0) {
            logger.warn("Custom field {} is not registered!", CustomFieldConstants.CLOUD_VM_INTERNAL_NAME);
        }
        ObjectContent[] ocs = getVmPropertiesOnHyperHost(new String[] {strName, "value[" + key + "]"});
        if (ocs != null && ocs.length > 0) {
            for (ObjectContent oc : ocs) {
                List<DynamicProperty> props = oc.getPropSet();
                if (props == null) {
                    continue;
                }
                String vmVcenterName = null;
                String vmInternalCsName = null;
                for (DynamicProperty prop : props) {
                    if (strName.equals(prop.getName())) {
                        vmVcenterName = prop.getVal().toString();
                    } else if (prop.getName().startsWith("value[")) {
                        if (prop.getVal() != null) {
                            vmInternalCsName = ((CustomFieldStringValue) prop.getVal()).getValue();
                        }
                    }
                }
                String vmName;
                if (vmInternalCsName != null && isUserVmInternalCsName(vmInternalCsName)) {
                    vmName = vmInternalCsName;
                } else {
                    vmName = vmVcenterName;
                }
                vmCache.put(vmName, new VirtualMachineMO(context, oc.getObj()));
            }
        }
    }

    @Override
    public ObjectContent[] getVmPropertiesOnHyperHost(String[] propertyPaths) throws Exception {
        PropertySpec pspec = new PropertySpec();
        pspec.setType("VirtualMachine");
        pspec.getPathSet().addAll(Arrays.asList(propertyPaths));
        TraversalSpec host2VmTraversal = new TraversalSpec();
        host2VmTraversal.setType(strHostSystem);
        host2VmTraversal.setPath("vm");
        host2VmTraversal.setName("host2VmTraversal");
        ObjectSpec ospec = new ObjectSpec();
        ospec.setObj(mor);
        ospec.setSkip(Boolean.TRUE);
        ospec.getSelectSet().add(host2VmTraversal);
        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pspec);
        pfSpec.getObjectSet().add(ospec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<>();
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
        TraversalSpec host2DatastoreTraversal = new TraversalSpec();
        host2DatastoreTraversal.setType(strHostSystem);
        host2DatastoreTraversal.setPath("datastore");
        host2DatastoreTraversal.setName("host2DatastoreTraversal");
        ObjectSpec ospec = new ObjectSpec();
        ospec.setObj(mor);
        ospec.setSkip(Boolean.TRUE);
        ospec.getSelectSet().add(host2DatastoreTraversal);
        PropertyFilterSpec pfSpec = new PropertyFilterSpec();
        pfSpec.getPropSet().add(pspec);
        pfSpec.getObjectSet().add(ospec);
        List<PropertyFilterSpec> pfSpecArr = new ArrayList<PropertyFilterSpec>();
        pfSpecArr.add(pfSpec);
        List<ObjectContent> properties = context.getService()
            .retrieveProperties(context.getPropertyCollector(), pfSpecArr);
        return properties.toArray(new ObjectContent[properties.size()]);
    }

    /**
     * getDatastoreMountsOnHost
     *
     * @return List
     * @throws Exception Exception
     */
    public List<Pair<ManagedObjectReference, String>> getDatastoreMountsOnHost() throws Exception {
        List<Pair<ManagedObjectReference, String>> mounts = new ArrayList<>();
        ObjectContent[] ocs = getDatastorePropertiesOnHyperHost(
            new String[] {String.format("host[\"%s\"].mountInfo.path", mor.getValue())});
        if (ocs != null) {
            for (ObjectContent oc : ocs) {
                Pair<ManagedObjectReference, String> mount = new Pair<>(oc.getObj(),
                    oc.getPropSet().get(0).getVal().toString());
                mounts.add(mount);
            }
        }
        return mounts;
    }

    /**
     * getExistingDataStoreOnHost
     *
     * @param hostAddress hostAddress
     * @param path path
     * @param hostDatastoreSystemMo hostDatastoreSystemMo
     * @return ManagedObjectReference ManagedObjectReference
     */
    public ManagedObjectReference getExistingDataStoreOnHost(String hostAddress, String path,
        HostDatastoreSystemMO hostDatastoreSystemMo) {
        List<ManagedObjectReference> morArray;
        try {
            morArray = hostDatastoreSystemMo.getDatastores();
        } catch (Exception e) {
            return null;
        }
        if (morArray.size() > 0) {
            for (int i = 0; i < morArray.size(); i++) {
                NasDatastoreInfo nasDadastore;
                try {
                    nasDadastore = hostDatastoreSystemMo.getNasDatastoreInfo(morArray.get(i));
                    if (nasDadastore != null) {
                        if (nasDadastore.getNas().getRemoteHost().equalsIgnoreCase(hostAddress) && nasDadastore.getNas()
                            .getRemotePath()
                            .equalsIgnoreCase(path)) {
                            return morArray.get(i);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    public void unmountDatastore(ManagedObjectReference mor) throws Exception {
        HostDatastoreSystemMO hostDatastoreSystemMo = getHostDatastoreSystemMo();
        if (!hostDatastoreSystemMo.deleteDatastore(mor)) {
            throw new Exception("Unable to unmount datastore. uuid: " + mor.getValue());
        }
    }

    @Override
    public VmwareHypervisorHostNetworkSummary getHyperHostNetworkSummary(String managementPortGroup) throws Exception {
        VmwareHypervisorHostNetworkSummary summary = new VmwareHypervisorHostNetworkSummary();
        VmwareHostType hostType = getHostType();
        if (hostType == VmwareHostType.ESXi) {
            List<VirtualNicManagerNetConfig> netConfigs = context.getVimClient()
                .getDynamicProperty(mor, "config.virtualNicManagerInfo.netConfig");
            assert netConfigs != null;

            String dvPortGroupKey;
            String portGroup;
            for (VirtualNicManagerNetConfig netConfig : netConfigs) {
                if ("management".equals(netConfig.getNicType())) {
                    for (HostVirtualNic nic : netConfig.getCandidateVnic()) {
                        portGroup = nic.getPortgroup();
                        if (portGroup == null || portGroup.isEmpty()) {
                            dvPortGroupKey = nic.getSpec().getDistributedVirtualPort().getPortgroupKey();
                            portGroup = getNetworkName(dvPortGroupKey);
                        }
                        if (portGroup.equalsIgnoreCase(managementPortGroup)) {
                            summary.setHostIp(nic.getSpec().getIp().getIpAddress());
                            summary.setHostNetmask(nic.getSpec().getIp().getSubnetMask());
                            summary.setHostMacAddress(nic.getSpec().getMac());
                            return summary;
                        }
                    }
                }
            }
        } else {
            List<HostVirtualNic> hostNics = context.getVimClient()
                .getDynamicProperty(mor, "config.network.consoleVnic");
            if (hostNics != null) {
                for (HostVirtualNic vnic : hostNics) {
                    if (vnic.getPortgroup().equals(managementPortGroup)) {
                        summary.setHostIp(vnic.getSpec().getIp().getIpAddress());
                        summary.setHostNetmask(vnic.getSpec().getIp().getSubnetMask());
                        summary.setHostMacAddress(vnic.getSpec().getMac());
                        return summary;
                    }
                }
            }
        }
        throw new Exception("Unable to find management port group " + managementPortGroup);
    }

    @Override
    public String getRecommendedDiskController(String guestOsId) throws Exception {
        ManagedObjectReference morParent = getParentMor();
        if (CLUSTER_COMPUTE_RESOURCE.equals(morParent.getType())) {
            ClusterMO clusterMo = clusterVmwareMoFactory.build(context, morParent);
            return clusterMo.getRecommendedDiskController(guestOsId);
        }
        return null;
    }

    /**
     * getHostNetworks
     *
     * @return List
     * @throws Exception Exception
     */
    public List<ManagedObjectReference> getHostNetworks() throws Exception {
        return context.getVimClient().getDynamicProperty(mor, "network");
    }

    /**
     * getNetworkName
     *
     * @param netMorVal netMorVal
     * @return String
     * @throws Exception Exception
     */
    public String getNetworkName(String netMorVal) throws Exception {
        String networkName = "";
        List<ManagedObjectReference> hostNetworks = getHostNetworks();
        for (ManagedObjectReference hostNetwork : hostNetworks) {
            if (hostNetwork.getValue().equals(netMorVal)) {
                networkName = context.getVimClient().getDynamicProperty(hostNetwork, strName);
                break;
            }
        }
        return networkName;
    }
}
