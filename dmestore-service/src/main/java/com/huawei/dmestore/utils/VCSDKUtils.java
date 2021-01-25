package com.huawei.dmestore.utils;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.vmware.VcConnectionHelpers;
import com.huawei.vmware.autosdk.SessionHelper;
import com.huawei.vmware.autosdk.TaggingWorkflow;
import com.huawei.vmware.mo.ClusterMO;
import com.huawei.vmware.mo.DatastoreMO;
import com.huawei.vmware.mo.HostDatastoreSystemMO;
import com.huawei.vmware.mo.HostMO;
import com.huawei.vmware.mo.HostStorageSystemMO;
import com.huawei.vmware.mo.RootFsMO;
import com.huawei.vmware.mo.VirtualMachineMO;
import com.huawei.vmware.util.ClusterVmwareMoFactory;
import com.huawei.vmware.util.DatastoreVmwareMoFactory;
import com.huawei.vmware.util.HostVmwareFactory;
import com.huawei.vmware.util.Pair;
import com.huawei.vmware.util.PbmUtil;
import com.huawei.vmware.util.RootVmwareMoFactory;
import com.huawei.vmware.util.SessionHelperFactory;
import com.huawei.vmware.util.TaggingWorkflowFactory;
import com.huawei.vmware.util.VirtualMachineMoFactorys;
import com.huawei.vmware.util.VmwareContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vmware.cis.tagging.CategoryModel;
import com.vmware.cis.tagging.CategoryTypes;
import com.vmware.cis.tagging.TagModel;
import com.vmware.pbm.PbmCapabilityConstraintInstance;
import com.vmware.pbm.PbmCapabilityDescription;
import com.vmware.pbm.PbmCapabilityDiscreteSet;
import com.vmware.pbm.PbmCapabilityInstance;
import com.vmware.pbm.PbmCapabilityMetadata;
import com.vmware.pbm.PbmCapabilityMetadataPerCategory;
import com.vmware.pbm.PbmCapabilityProfileCreateSpec;
import com.vmware.pbm.PbmCapabilityPropertyInstance;
import com.vmware.pbm.PbmCapabilityPropertyMetadata;
import com.vmware.pbm.PbmCapabilitySubProfile;
import com.vmware.pbm.PbmCapabilitySubProfileConstraints;
import com.vmware.pbm.PbmDuplicateNameFaultMsg;
import com.vmware.pbm.PbmFaultProfileStorageFaultFaultMsg;
import com.vmware.pbm.PbmProfile;
import com.vmware.pbm.PbmProfileId;
import com.vmware.pbm.PbmProfileResourceType;
import com.vmware.pbm.PbmServiceInstanceContent;
import com.vmware.pbm.RuntimeFaultFaultMsg;
import com.vmware.vapi.std.DynamicID;
import com.vmware.vim.binding.vim.HostSystem;
import com.vmware.vim.binding.vim.ServiceInstance;
import com.vmware.vim.binding.vim.ServiceInstanceContent;
import com.vmware.vim.binding.vim.SessionManager;
import com.vmware.vim.binding.vim.version.version10;
import com.vmware.vim.binding.vmodl.reflect.ManagedMethodExecuter;
import com.vmware.vim.vmomi.client.Client;
import com.vmware.vim.vmomi.client.http.HttpClientConfiguration;
import com.vmware.vim.vmomi.client.http.HttpConfiguration;
import com.vmware.vim.vmomi.client.http.impl.AllowAllThumbprintVerifier;
import com.vmware.vim.vmomi.client.http.impl.HttpConfigurationImpl;
import com.vmware.vim.vmomi.core.types.VmodlContext;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.HostFibreChannelHba;
import com.vmware.vim25.HostFileSystemMountInfo;
import com.vmware.vim25.HostHostBusAdapter;
import com.vmware.vim25.HostInternetScsiHba;
import com.vmware.vim25.HostInternetScsiHbaSendTarget;
import com.vmware.vim25.HostScsiDisk;
import com.vmware.vim25.HostScsiDiskPartition;
import com.vmware.vim25.HostVirtualNic;
import com.vmware.vim25.HostVmfsVolume;
import com.vmware.vim25.InvalidArgumentFaultMsg;
import com.vmware.vim25.IscsiPortInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NasDatastoreInfo;
import com.vmware.vim25.VirtualDiskMode;
import com.vmware.vim25.VirtualDiskType;
import com.vmware.vim25.VirtualNicManagerNetConfig;
import com.vmware.vim25.VmfsDatastoreExpandSpec;
import com.vmware.vim25.VmfsDatastoreInfo;
import com.vmware.vim25.VmfsDatastoreOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * VCSDKUtils
 *
 * @author andrew.liu
 * @since 2020-09-15
 **/
public class VCSDKUtils {
    private static final Logger logger = LoggerFactory.getLogger(VCSDKUtils.class);

    private static final Class<?> VERSION = version10.class;

    private static final String POLICY_DESC = "policy created by dme";

    private static final String CONNECT_STATUS = "connectStatus";

    private static final String ISCSI_TYPE = "ISCSI";

    private static final String FC_TYPE = "FC";

    private static final String DEVICE_FIELD = "device";

    private static final String ID = "id";

    private static final String NAME = "name";

    private static final String OBJECT_ID = "objectId";

    private static final String STATUS = "status";

    private static final String TYPE = "type";

    private static final String CAPACITY = "capacity";

    private static final String FREE_SPACE = "freeSpace";

    private static final String CLUSTER_ID = "clusterId";

    private static final String CLUSTER_NAME = "clusterName";

    private static final String HOST_ID = "hostId";

    private static final String SUCCESS_RESULT = "success";

    private static final String FAILED_RESULT = "failed";

    private static final String HOST_FIELD = "host";

    private static final String HOST_SCSI_DISK = "hostScsiDisk";

    private static final String HOST_NAME = "hostName";

    public static final String CATEGORY_NAME = "DME Service Level";

    private static final int TEST_CONNECTIVITY_TIMEOUT = 3000;

    private static final int THREAD_SLEEP_2_SECENDS = 2000;

    private static final int THREAD_POOL_SIZE = 5;

    private static final int DIGIT_1024 = 1024;

    private static final int DEFAULT_CONTROLLER_KEY = -1;

    private static VmodlContext context;

    private VcConnectionHelpers vcConnectionHelpers;

    private RootVmwareMoFactory rootVmwareMoFactory = RootVmwareMoFactory.getInstance();

    private DatastoreVmwareMoFactory datastoreVmwareMoFactory = DatastoreVmwareMoFactory.getInstance();

    private HostVmwareFactory hostVmwareFactory = HostVmwareFactory.getInstance();

    private ClusterVmwareMoFactory clusterVmwareMoFactory = ClusterVmwareMoFactory.getInstance();

    private SessionHelperFactory sessionHelperFactory = SessionHelperFactory.getInstance();

    private TaggingWorkflowFactory taggingWorkflowFactory = TaggingWorkflowFactory.getInstance();

    private VirtualMachineMoFactorys virtualMachineMoFactorys = VirtualMachineMoFactorys.getInstance();

    private Gson gson = new Gson();

    public VcConnectionHelpers getVcConnectionHelper() {
        return vcConnectionHelpers;
    }

    public void setVcConnectionHelper(VcConnectionHelpers vcConnectionHelper) {
        this.vcConnectionHelpers = vcConnectionHelper;
    }

    public String getAllVmfsDataStoreInfos(String storeType) throws VcenterException {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
                if (dss != null && dss.size() > 0) {
                    List<Map<String, Object>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> ds : dss) {
                        DatastoreMO ds1 = datastoreVmwareMoFactory.build(vmwareContext, ds.first());
                        Map<String, Object> dsmap = gson.fromJson(gson.toJson(ds1.getSummary()),
                            new TypeToken<Map<String, Object>>() { }.getType());
                        String objectid = vcConnectionHelpers.mor2ObjectId(ds1.getMor(),
                            vmwareContext.getServerAddress());
                        dsmap.put(OBJECT_ID, objectid);
                        if (storeType.equalsIgnoreCase(ToolUtils.STORE_TYPE_NFS) && ds1.getSummary()
                            .getType()
                            .equalsIgnoreCase(ToolUtils.STORE_TYPE_NFS)) {
                            NasDatastoreInfo nasinfo = (NasDatastoreInfo) ds1.getInfo();

                            dsmap.put("remoteHost", nasinfo.getNas().getRemoteHost());
                            dsmap.put("remotePath", nasinfo.getNas().getRemotePath());
                            dsmap.put("nfsStorageId", ds1.getMor().getValue());
                        } else if (storeType.equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS) && ds1.getSummary()
                            .getType()
                            .equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
                            VmfsDatastoreInfo vmfsDatastoreInfo = ds1.getVmfsDatastoreInfo();
                            List<HostScsiDiskPartition> extent = vmfsDatastoreInfo.getVmfs().getExtent();
                            List<String> wwnList = new ArrayList<>();
                            if (extent != null) {
                                for (HostScsiDiskPartition hostScsiDiskPartition : extent) {
                                    String wwn = hostScsiDiskPartition.getDiskName().replace("naa.", "");
                                    wwnList.add(wwn);
                                }
                                dsmap.put("vmfsWwnList", wwnList);
                            }
                        }
                        if (StringUtils.isEmpty(storeType)) {
                            lists.add(dsmap);
                        } else if (ds1.getSummary().getType().equals(storeType)) {
                            lists.add(dsmap);
                        }
                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getAllVmfsDataStoreInfos error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    /**
     * 得到指定objectid存储的info
     *
     * @param objectid objectid
     * @return String
     * @throws VcenterException VcenterException
     */
    public Map<String, Object> getDataStoreSummaryByObjectId(String objectid) throws VcenterException {
        Map<String, Object> dsmap;
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(objectid);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference datastoremor = vcConnectionHelpers.objectId2Mor(objectid);
            DatastoreMO datastoreMo = datastoreVmwareMoFactory.build(vmwareContext, datastoremor);
            dsmap = gson.fromJson(gson.toJson(datastoreMo.getSummary()),
                new TypeToken<Map<String, Object>>() { }.getType());
        } catch (Exception e) {
            logger.error("getDataStoreSummaryByObjectId error!{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return dsmap;
    }

    /**
     * 得到所有主机的ID与name
     *
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getAllHosts() throws VcenterException {
        logger.info("get all hosts start");
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                logger.info("rootFsMo:{}", rootFsMo);
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                logger.info("hosts:{}", hosts);
                if (hosts != null && hosts.size() > 0) {
                    List<Map<String, String>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                        Map<String, String> map = new HashMap<>();
                        String objectId = vcConnectionHelpers.mor2ObjectId(host1.getMor(),
                            vmwareContext.getServerAddress());
                        map.put(HOST_ID, objectId);
                        map.put(OBJECT_ID, objectId);
                        map.put(HOST_NAME, host1.getName());
                        lists.add(map);
                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getAllHosts error:{}", e.getMessage());
            throw new VcenterException(e.getLocalizedMessage());
        }
        logger.info("get all hosts end");
        return listStr;
    }

    public String findHostById(String objectId) throws VcenterException {
        String hostlist = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            List<Map<String, String>> lists = new ArrayList<>();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                HostMO hostMo = rootFsMo.findHostById(objectId);
                Map<String, String> map = new HashMap<>();
                map.put(HOST_ID, objectId);
                map.put(OBJECT_ID, objectId);
                map.put(HOST_NAME, hostMo.getName());
                lists.add(map);
            }
            hostlist = gson.toJson(lists);
        } catch (Exception e) {
            throw new VcenterException(e.getMessage());
        }

        return hostlist;
    }

    public String getAllClusters() throws VcenterException {
        String listStr = "";
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> cls = rootFsMo.getAllClusterOnRootFs();
                if (cls != null && cls.size() > 0) {
                    List<Map<String, String>> lists = new ArrayList<>();
                    for (Pair<ManagedObjectReference, String> cl : cls) {
                        ClusterMO cl1 = clusterVmwareMoFactory.build(vmwareContext, cl.first());
                        Map<String, String> map = new HashMap<>();
                        String objectId = vcConnectionHelpers.mor2ObjectId(cl1.getMor(),
                            vmwareContext.getServerAddress());
                        map.put(CLUSTER_ID, objectId);
                        map.put(CLUSTER_NAME, cl1.getName());
                        lists.add(map);
                    }
                    if (lists.size() > 0) {
                        listStr = gson.toJson(lists);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("getAllClusters error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    /**
     * 得到所有主机的ID与name 除去已经挂载了当前存储的主机  20200918objectId
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getHostsByDsObjectId(String dataStoreObjectId) throws VcenterException {
        return getHostsByDsObjectId(dataStoreObjectId, false);
    }

    /**
     * 得到所有主机的ID与name, boolean mount 是否已经挂载了当前存储的主机
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @param mount mount
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getHostsByDsObjectId(String dataStoreObjectId, boolean mount) throws VcenterException {
        String listStr = "";
        try {
            // 得到当前的context
            String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);

            // 取得该存储下所有已经挂载的主机ID
            List<String> mounthostids = new ArrayList<>();
            DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, dsmor);
            if (dsmo != null) {
                List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                if (dhms != null && dhms.size() > 0) {
                    for (DatastoreHostMount dhm : dhms) {
                        if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted()) {
                            mounthostids.add(dhm.getKey().getValue());
                        }
                    }
                }
            }

            // 取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
            List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
            if (hosts != null && hosts.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                    if (mount) {
                        if (mounthostids.contains(host1.getMor().getValue())) {
                            Map<String, String> map = new HashMap<>();
                            String objectId = vcConnectionHelpers.mor2ObjectId(host1.getMor(),
                                vmwareContext.getServerAddress());
                            map.put(HOST_ID, objectId);
                            map.put(HOST_NAME, host1.getName());
                            lists.add(map);
                        }
                    } else {
                        if (!mounthostids.contains(host1.getMor().getValue())) {
                            Map<String, String> map = new HashMap<>();
                            String objectId = vcConnectionHelpers.mor2ObjectId(host1.getMor(),
                                vmwareContext.getServerAddress());
                            map.put(HOST_ID, objectId);
                            map.put(HOST_NAME, host1.getName());
                            lists.add(map);
                        }
                    }
                }
                if (lists.size() > 0) {
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception e) {
            logger.error("vmware error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    /**
     * 得到所有主机的ID与name 除去没有挂载了当前存储的主机
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getMountHostsByDsObjectId(String dataStoreObjectId) throws VcenterException {
        return getHostsByDsObjectId(dataStoreObjectId, true);
    }

    /**
     * 得到所有集群的ID与name 除去已经挂载了当前存储的集群  扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，
     * 只有集群下所有主机都挂载了该存储就不显示
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getClustersByDsObjectId(String dataStoreObjectId) throws VcenterException {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());

            // 取得该存储下所有已经挂载的主机ID
            List<String> mounthostids = new ArrayList<>();
            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
            DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, dsmor);
            if (dsmo != null) {
                List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                if (dhms != null && dhms.size() > 0) {
                    for (DatastoreHostMount dhm : dhms) {
                        if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted()) {
                            mounthostids.add(dhm.getKey().getValue());
                        }
                    }
                }
            }

            /**
             * 取得所有集群，并通过mounthostids进行过滤，过滤掉已经挂载的主机
             * 扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
             */
            List<Pair<ManagedObjectReference, String>> cls = rootFsMo.getAllClusterOnRootFs();
            if (cls != null && cls.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> cl : cls) {
                    boolean isMount = false;
                    ClusterMO cl1 = clusterVmwareMoFactory.build(vmwareContext, cl.first());
                    List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();
                    if (hosts != null && hosts.size() > 0) {
                        for (Pair<ManagedObjectReference, String> host : hosts) {
                            HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                            if (!mounthostids.contains(host1.getMor().getValue())) {
                                isMount = true;
                                break;
                            }
                        }
                    }
                    if (isMount) {
                        Map<String, String> map = new HashMap<>();
                        String objectId = vcConnectionHelpers.mor2ObjectId(cl1.getMor(),
                            vmwareContext.getServerAddress());
                        map.put(CLUSTER_ID, objectId);
                        map.put(CLUSTER_NAME, cl1.getName());
                        lists.add(map);
                    }
                }
                if (lists.size() > 0) {
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception e) {
            logger.error("getClustersByDsObjectId error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    public String getMountClustersByDsObjectId(String dataStoreObjectId, Map<String, String> inludeclustermap)
        throws VcenterException {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());

            // 取得该存储下所有已经挂载的主机ID
            List<String> mounthostids = new ArrayList<>();
            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
            DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, dsmor);
            if (dsmo != null) {
                List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                if (dhms != null && dhms.size() > 0) {
                    for (DatastoreHostMount dhm : dhms) {
                        if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted()) {
                            mounthostids.add(dhm.getKey().getValue());
                        }
                    }
                }
            }

            /*
             * 取得所有集群，并通过mounthostids进行过滤，过滤掉已经挂载的主机
             * 扫描集群下所有主机，只有集群下所有主机都没有挂载了该存储就不显示
             */
            List<Pair<ManagedObjectReference, String>> cls = rootFsMo.getAllClusterOnRootFs();
            if (cls != null && cls.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> cl : cls) {
                    boolean isMount = false;
                    ClusterMO cl1 = clusterVmwareMoFactory.build(vmwareContext, cl.first());
                    List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();
                    if (hosts != null && hosts.size() > 0) {
                        for (Pair<ManagedObjectReference, String> host : hosts) {
                            HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                            if (mounthostids.contains(host1.getMor().getValue())) {
                                isMount = true;
                                break;
                            }
                        }
                    }
                    if (isMount && (null == inludeclustermap || null != inludeclustermap.get(cl.first().getValue()))) {
                        Map<String, String> map = new HashMap<>();
                        String objectId = vcConnectionHelpers.mor2ObjectId(cl1.getMor(),
                            vmwareContext.getServerAddress());
                        map.put(CLUSTER_ID, objectId);
                        map.put(CLUSTER_NAME, cl1.getName());
                        lists.add(map);
                    }
                }
                if (lists.size() > 0) {
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception e) {
            logger.error("vmware getMountClustersByDsObjectId error:", e);
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    /**
     * 得到所有存储 除去已经挂载了当前主机的存储 20200918objectId
     *
     * @param hostObjectId hostObjectId
     * @param dataStoreType dataStoreType
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getDataStoresByHostObjectId(String hostObjectId, String dataStoreType) throws VcenterException {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());

            // 取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);
            String objHostId = null;
            if (hostmo != null) {
                objHostId = hostmo.getMor().getValue();
            }

            // 取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
            List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
            if (dss == null || dss.size() == 0) {
                return listStr;
            }
            List<Map<String, Object>> lists = new ArrayList<>();
            for (Pair<ManagedObjectReference, String> ds : dss) {
                DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, ds.first());
                if (dsmo != null && dataStoreType.equals(dsmo.getSummary().getType())) {
                    boolean isMount = true;
                    List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                    if (dhms != null && dhms.size() > 0) {
                        for (DatastoreHostMount dhm : dhms) {
                            if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted() && dhm.getKey()
                                .getValue()
                                .equals(objHostId)) {
                                isMount = false;
                                break;
                            }
                        }
                    }
                    if (isMount) {
                        String objectId = vcConnectionHelpers.mor2ObjectId(dsmo.getMor(),
                            vmwareContext.getServerAddress());
                        Map<String, Object> map = new HashMap<>();
                        map.put(ID, dsmo.getMor().getValue());
                        map.put(NAME, dsmo.getName());
                        map.put(OBJECT_ID, objectId);
                        map.put(STATUS, dsmo.getSummary().isAccessible());
                        map.put(TYPE, dsmo.getSummary().getType());
                        map.put(CAPACITY, dsmo.getSummary().getCapacity() / ToolUtils.GI);
                        map.put(FREE_SPACE, dsmo.getSummary().getFreeSpace() / ToolUtils.GI);

                        lists.add(map);
                    }
                }
            }
            if (lists.size() > 0) {
                listStr = gson.toJson(lists);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    /**
     * 得到挂载了当前主机的所有存储
     *
     * @param hostObjectId hostObjectId
     * @param dataStoreType dataStoreType
     * @return String
     * @throws Exception Exception
     */
    public String getMountDataStoresByHostObjectId(String hostObjectId, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());

            // 取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);
            String objHostId = null;
            if (hostmo != null) {
                objHostId = hostmo.getMor().getValue();
            }

            // 取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
            List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
            if (dss != null && dss.size() > 0) {
                List<Map<String, Object>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> ds : dss) {
                    DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, ds.first());
                    if (dsmo == null || !dataStoreType.equals(dsmo.getSummary().getType())) {
                        continue;
                    }
                    List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                    if (dhms != null && dhms.size() > 0) {
                        for (DatastoreHostMount dhm : dhms) {
                            if (dhm.getMountInfo() != null && dhm.getMountInfo().isMounted() && dhm.getKey()
                                .getValue()
                                .equals(objHostId)) {
                                String objectId = vcConnectionHelpers.mor2ObjectId(dsmo.getMor(),
                                    vmwareContext.getServerAddress());
                                Map<String, Object> map = new HashMap<>();
                                map.put(ID, dsmo.getMor().getValue());
                                map.put(NAME, dsmo.getName());
                                map.put(OBJECT_ID, objectId);
                                map.put(STATUS, dsmo.getSummary().isAccessible());
                                map.put(TYPE, dsmo.getSummary().getType());
                                map.put(CAPACITY, dsmo.getSummary().getCapacity() / ToolUtils.GI);
                                map.put(FREE_SPACE, dsmo.getSummary().getFreeSpace() / ToolUtils.GI);

                                lists.add(map);
                                break;
                            }
                        }
                    }
                }
                if (lists.size() > 0) {
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception ex) {
            logger.error("vmware getMountDataStore by hostId error:", ex);
            throw ex;
        }
        return listStr;
    }

    /**
     * 得到所有存储 除去已经挂载了当前集群的存储 扫描集群下所有主机，只要有一个主机没挂当前存储就要显示，只有集群下所有主机都挂载了该存储就不显示
     *
     * @param clusterObjectId clusterObjectId
     * @param dataStoreType dataStoreType
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType) throws VcenterException {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());

            // 取得该存储下所有已经挂载的主机ID
            List<String> hostids = new ArrayList<>();
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(clusterObjectId);
            ClusterMO clusterMo = clusterVmwareMoFactory.build(vmwareContext, objmor);
            if (clusterMo != null) {
                List<Pair<ManagedObjectReference, String>> hosts = clusterMo.getClusterHosts();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                        hostids.add(host1.getMor().getValue());
                    }
                }
            }

            // 取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
            List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
            if (dss != null && dss.size() > 0) {
                List<Map<String, Object>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> ds : dss) {
                    DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, ds.first());
                    if (dsmo == null || !dataStoreType.equals(dsmo.getSummary().getType())) {
                        continue;
                    }
                    boolean isMount = false;
                    List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                    if (dhms != null && dhms.size() > 0) {
                        // 整理挂载信息
                        List<String> dsHostIds = new ArrayList<>();
                        for (DatastoreHostMount dhm : dhms) {
                            if (dhm.getMountInfo().isMounted()) {
                                dsHostIds.add(dhm.getKey().getValue());
                            }
                        }
                        for (String hostid : hostids) {
                            if (!dsHostIds.contains(hostid)) {
                                isMount = true;
                                break;
                            }
                        }
                    }
                    if (isMount) {
                        String objectId = vcConnectionHelpers.mor2ObjectId(dsmo.getMor(),
                            vmwareContext.getServerAddress());
                        Map<String, Object> map = new HashMap<>();
                        map.put(ID, dsmo.getMor().getValue());
                        map.put(NAME, dsmo.getName());
                        map.put(OBJECT_ID, objectId);
                        map.put(STATUS, dsmo.getSummary().isAccessible());
                        map.put(TYPE, dsmo.getSummary().getType());
                        map.put(CAPACITY, dsmo.getSummary().getCapacity() / ToolUtils.GI);
                        map.put(FREE_SPACE, dsmo.getSummary().getFreeSpace() / ToolUtils.GI);

                        lists.add(map);
                    }
                }
                if (lists.size() > 0) {
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    /**
     * 得到所有存储 扫描集群下所有主机，只要有一个主机没挂载了当前存储就要显示
     *
     * @param clusterObjectId clusterObjectId
     * @param dataStoreType dataStoreType
     * @return String
     * @throws Exception Exception
     */
    public String getMountDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType) throws Exception {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());

            // 取得该存储下所有已经挂载的主机ID
            List<String> hostids = new ArrayList<>();
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(clusterObjectId);
            ClusterMO clusterMo = clusterVmwareMoFactory.build(vmwareContext, objmor);
            if (clusterMo != null) {
                List<Pair<ManagedObjectReference, String>> hosts = clusterMo.getClusterHosts();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                        hostids.add(host1.getMor().getValue());
                    }
                }
            }

            // 取得所有主机，并通过mounthostids进行过滤，过滤掉已经挂载的主机
            List<Pair<ManagedObjectReference, String>> dss = rootFsMo.getAllDatastoreOnRootFs();
            if (dss != null && dss.size() > 0) {
                List<Map<String, Object>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> ds : dss) {
                    DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, ds.first());
                    if (dsmo != null && dataStoreType.equals(dsmo.getSummary().getType())) {
                        List<DatastoreHostMount> dhms = dsmo.getHostMounts();
                        if (dhms == null || dhms.size() == 0) {
                            continue;
                        }

                        // 整理挂载信息
                        List<String> dsHostIds = new ArrayList<>();
                        for (DatastoreHostMount dhm : dhms) {
                            if (dhm.getMountInfo().isMounted()) {
                                dsHostIds.add(dhm.getKey().getValue());
                            }
                        }
                        for (String hostid : hostids) {
                            if (!dsHostIds.contains(hostid)) {
                                continue;
                            }
                            String objectId = vcConnectionHelpers.mor2ObjectId(dsmo.getMor(),
                                vmwareContext.getServerAddress());
                            Map<String, Object> map = new HashMap<>();
                            map.put(ID, dsmo.getMor().getValue());
                            map.put(NAME, dsmo.getName());
                            map.put(OBJECT_ID, objectId);
                            map.put(STATUS, dsmo.getSummary().isAccessible());
                            map.put(TYPE, dsmo.getSummary().getType());
                            map.put(CAPACITY, dsmo.getSummary().getCapacity() / ToolUtils.GI);
                            map.put(FREE_SPACE, dsmo.getSummary().getFreeSpace() / ToolUtils.GI);

                            lists.add(map);
                            break;
                        }
                    }
                }
                if (lists.size() > 0) {
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception ex) {
            logger.error("vmware  getMountDataStore by slusterObjI derror:{}", ex.getMessage());
            throw ex;
        }
        return listStr;
    }

    /**
     * 得到指定集群下的所有主机 20200918objectId
     *
     * @param clusterObjectId clusterObjectId
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getHostsOnCluster(String clusterObjectId) throws VcenterException {
        String listStr = "";
        try {
            // 得到当前的context
            String serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference clmor = vcConnectionHelpers.objectId2Mor(clusterObjectId);
            ClusterMO cl1 = clusterVmwareMoFactory.build(vmwareContext, clmor);
            List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();
            if (hosts != null && hosts.size() > 0) {
                List<Map<String, String>> lists = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                    Map<String, String> map = new HashMap<>();
                    String objectId = vcConnectionHelpers.mor2ObjectId(host1.getMor(),
                        vmwareContext.getServerAddress());
                    map.put(HOST_ID, objectId);
                    map.put(HOST_NAME, host1.getName());
                    lists.add(map);
                }
                if (lists.size() > 0) {
                    listStr = gson.toJson(lists);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    /**
     * 得到指定集群下的所有主机,以及指定主机所属集群下的所有主机 20200918objectId
     *
     * @param clusterObjectId clusterObjectId
     * @param hostObjectId hostObjectId
     * @return List
     * @throws VcenterException VcenterException
     */
    public List<Pair<ManagedObjectReference, String>> getHostsOnCluster(String clusterObjectId, String hostObjectId)
        throws VcenterException {
        List<Pair<ManagedObjectReference, String>> hosts = null;
        try {
            String serverguid = null;
            if (!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            }
            if (!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

                // 集群下的所有主机
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(clusterObjectId);
                    ClusterMO clusterMo = clusterVmwareMoFactory.build(vmwareContext, objmor);
                    hosts = clusterMo.getClusterHosts();
                } else if (!StringUtils.isEmpty(hostObjectId)) {
                    // 目标主机所在集群下的其它主机
                    ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
                    HostMO hostMo = hostVmwareFactory.build(vmwareContext, objmor);
                    try {
                        ManagedObjectReference cluster = hostMo.getHyperHostCluster();
                        if (cluster != null) {
                            ClusterMO clusterMo = clusterVmwareMoFactory.build(hostMo.getContext(), cluster);
                            logger.info("Host cluster name:{}", clusterMo.getName());
                            hosts = clusterMo.getClusterHosts();
                        }
                    } catch (Exception e) {
                        logger.error("Number of hosts in cluster:{}", null == hosts ? "null" : hosts.size());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("get Host On Cluster error:", e);
            throw new VcenterException(e.getMessage());
        }

        return hosts;
    }

    /**
     * 得到集群下所有没有挂载的主机 20200918objectId
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @param clusters clusters
     * @return String
     * @throws VcenterException VcenterException
     */
    public List<String> getUnmoutHostsOnCluster(String dataStoreObjectId, List<Map<String, String>> clusters)
        throws VcenterException {
        List<String> hostlist = null;
        try {
            String unmountHostStr = getHostsByDsObjectId(dataStoreObjectId);
            List<Map<String, String>> unmountHostlists = gson.fromJson(unmountHostStr,
                new TypeToken<List<Map<String, String>>>() { }.getType());
            if (clusters != null && clusters.size() > 0) {
                hostlist = new ArrayList<>();
                for (Map<String, String> cluster : clusters) {
                    String hostStr = getHostsOnCluster(cluster.get(CLUSTER_ID));
                    List<Map<String, String>> hostStrlists = gson.fromJson(hostStr,
                        new TypeToken<List<Map<String, String>>>() { }.getType());
                    if (hostStrlists != null && hostStrlists.size() > 0) {
                        for (Map<String, String> hostmap : hostStrlists) {
                            if (unmountHostlists.contains(hostmap)) {
                                hostlist.add(ToolUtils.getStr(hostmap.get(HOST_NAME)));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return hostlist;
    }

    /**
     * renameDataStore
     *
     * @param newName newName
     * @param dataStoreObjectId dataStoreObjectId
     * @return String
     */
    public String renameDataStore(String newName, String dataStoreObjectId) {
        String result = SUCCESS_RESULT;
        logger.info("==start rename DataStore==");
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
            VmwareContext serverContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
            DatastoreMO dsMo = datastoreVmwareMoFactory.build(serverContext, dsmor);
            dsMo.renameDatastore(newName);

            // 刷新
            logger.info("==refreshDatastore afterend rename DataStore==");
            dsMo.refreshDatastore();
            logger.info("==end rename DataStore==");
        } catch (Exception e) {
            result = FAILED_RESULT;
            logger.error("renameDataStore error:{}", e.getMessage());
        }
        return result;
    }

    /**
     * expand oriented datastore capacity
     *
     * @param dsname dsname
     * @param addCapacity addCapacity
     * @param dataStoreObjectId dataStoreObjectId
     * @return String
     */
    public String expandVmfsDatastore(String dsname, Integer addCapacity, String dataStoreObjectId) {
        String result = SUCCESS_RESULT;
        logger.info("==start expand DataStore==");
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

            // 获取挂载了该存储的主机信息
            String hostStr = getMountHostsByDsObjectId(dataStoreObjectId);
            if (StringUtils.isEmpty(hostStr)) {
                logger.info("get mount hosts return null!");
                result = FAILED_RESULT;
                return result;
            }
            List<Map<String, String>> hostMapList = gson.fromJson(hostStr,
                new TypeToken<List<Map<String, String>>>() { }.getType());
            HostMO host1 = null;
            HostDatastoreSystemMO hdsMo = null;
            for (Map<String, String> hostMap : hostMapList) {
                ManagedObjectReference hostMor = vcConnectionHelpers.objectId2Mor(hostMap.get(HOST_ID));
                host1 = hostVmwareFactory.build(vmwareContext, hostMor);
                if (null != host1) {
                    hdsMo = host1.getHostDatastoreSystemMo();
                    HostStorageSystemMO hostStorageSystemMo = host1.getHostStorageSystemMo();
                    hostStorageSystemMo.rescanVmfs();
                    break;
                }
                logger.info("===hostMo hostName:{}===", host1.getName());
            }
            if (host1 != null && hdsMo != null) {
                ManagedObjectReference dataStoreMor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
                DatastoreMO dsMo = datastoreVmwareMoFactory.build(vmwareContext, dataStoreMor);
                logger.info("==datastore name={}==", dsMo.getName());
                List<VmfsDatastoreOption> vmfsDatastoreOptions = hdsMo.queryVmfsDatastoreExpandOptions(dsMo);
                VmfsDatastoreInfo datastoreInfo = (VmfsDatastoreInfo) hdsMo.getDatastoreInfo(dataStoreMor);
                if (vmfsDatastoreOptions != null && vmfsDatastoreOptions.size() > 0) {
                    VmfsDatastoreOption vmfsDatastoreOption = vmfsDatastoreOptions.get(0);
                    VmfsDatastoreExpandSpec spec = (VmfsDatastoreExpandSpec) vmfsDatastoreOption.getSpec();
                    HostVmfsVolume vmfs = datastoreInfo.getVmfs();
                    String uuid = vmfs.getUuid();
                    Long totalSectors = addCapacity * 1l * ToolUtils.GI / vmfs.getBlockSize();
                    long originSectors = spec.getPartition().getTotalSectors();
                    spec.getPartition().setTotalSectors(totalSectors + originSectors);
                    logger.info("===set expand params end===");

                    // 刷新vmfs存储，需等待2秒
                    List<DatastoreHostMount> hostMountInfos = dsMo.getHostMounts();
                    for (DatastoreHostMount datastoreHostMount : hostMountInfos) {
                        HostMO hostmo = hostVmwareFactory.build(vmwareContext, datastoreHostMount.getKey());
                        logger.info("===hostmo{}:===", hostmo);
                        hostmo.getHostStorageSystemMo().refreshStorageSystem();
                    }
                    Thread.sleep(THREAD_SLEEP_2_SECENDS);
                    hdsMo.expandVmfsDatastore(dsMo, spec);
                    logger.info("==refreshDatastore after expandVmfsDatastore==");
                    dsMo.refreshDatastore();
                } else {
                    logger.info("==queryVmfsDatastoreExpandOptions return null==");
                }
            } else {
                logger.info("===host1 is null:{}, hdsMo is null:{}===", host1 == null, hdsMo == null);
                result = FAILED_RESULT;
            }
            logger.info("===end expand DataStore===");
        } catch (Exception e) {
            result = FAILED_RESULT;
            logger.error("expandVmfsDatastore error:{}", e.getMessage());
        }
        return result;
    }

    /**
     * recycle vmfs datastore capacity
     *
     * @param datastoreObjectId datastoreObjectId
     * @return String
     * @throws VcenterException VcenterException
     */
    public String recycleVmfsCapacity(String datastoreObjectId) throws VcenterException {
        String result = SUCCESS_RESULT;
        String serverguid = vcConnectionHelpers.objectId2Serverguid(datastoreObjectId);
        try {
            VmwareContext serverContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference dataStoreMor = vcConnectionHelpers.objectId2Mor(datastoreObjectId);
            DatastoreMO datastoreMo = new DatastoreMO(serverContext, dataStoreMor);
            DatastoreSummary summary = datastoreMo.getSummary();
            if (!summary.getType().equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
                logger.info("datastore is not VMFS!datastoreObjectId={}", datastoreObjectId);
                return FAILED_RESULT;
            }
            VmfsDatastoreInfo vmfsDatastoreInfo = datastoreMo.getVmfsDatastoreInfo();
            HostVmfsVolume hostVmfsVolume = vmfsDatastoreInfo.getVmfs();
            List<String> vmfsUuids = new ArrayList<>();
            vmfsUuids.add(hostVmfsVolume.getUuid());
            String hostStr = getMountHostsByDsObjectId(datastoreObjectId);
            if (StringUtils.isEmpty(hostStr)) {
                logger.info("get mount hosts return null!datastoreObjectId={}", datastoreObjectId);
                return FAILED_RESULT;
            }
            List<Map<String, String>> hostMapList = gson.fromJson(hostStr,
                new TypeToken<List<Map<String, String>>>() { }.getType());
            HostStorageSystemMO hdsMo = null;
            for (Map<String, String> hostMap : hostMapList) {
                ManagedObjectReference hostMor = vcConnectionHelpers.objectId2Mor(hostMap.get(HOST_ID));
                HostMO host1 = hostVmwareFactory.build(serverContext, hostMor);
                if (null != host1) {
                    hdsMo = host1.getHostStorageSystemMo();
                    break;
                }
            }
            hdsMo.unmapVmfsVolumeExTask(vmfsUuids);
            logger.info("recycleVmfsCapacity end!");
        } catch (Exception e) {
            logger.error("recycleVmfsCapacity error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return result;
    }

    /**
     * create nfs datastore
     *
     * @param serverHost serverHost
     * @param exportPath exportPath
     * @param nfsName nfsName
     * @param accessModeReq accessModeReq
     * @param hostObjectIds hostObjectIds
     * @param type type
     * @param securityType securityType
     * @return String
     * @throws VcenterException VcenterException
     */
    public String createNfsDatastore(String serverHost, String exportPath, String nfsName, String accessModeReq,
        List<Map<String, String>> hostObjectIds, String type, String securityType) throws VcenterException {
        String response = "";
        logger.info("start creat nfs datastore");
        String accessMode = accessModeReq;
        accessMode = StringUtils.isEmpty(accessMode) || "readWrite".equals(accessMode) ? "readWrite" : "readOnly";
        try {
            VmwareContext vmwareContext;
            ManagedObjectReference managedObjectReference;
            DmeVmwareRelation dmeVmwareRelation = new DmeVmwareRelation();
            if (hostObjectIds != null && hostObjectIds.size() != 0) {
                for (Map<String, String> hosts : hostObjectIds) {
                    for (Map.Entry<String, String> host : hosts.entrySet()) {
                        String serverguid = vcConnectionHelpers.objectId2Serverguid(host.getKey());
                        managedObjectReference = vcConnectionHelpers.objectId2Mor(host.getKey());
                        vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
                        if (managedObjectReference != null && vmwareContext != null) {
                            HostMO hostMo = hostVmwareFactory.build(vmwareContext, managedObjectReference);

                            logger.info("vmware create nfs datastore begin!hostName={}", hostMo.getHostName());
                            HostDatastoreSystemMO hostDatastoreSystemMo = hostMo.getHostDatastoreSystemMo();
                            ManagedObjectReference datastore = hostDatastoreSystemMo.createNfsDatastore(serverHost, 0,
                                exportPath, nfsName, accessMode, type, securityType);
                            String datastoreObjectId = vcConnectionHelpers.mor2ObjectId(datastore, serverguid);
                            dmeVmwareRelation.setStoreId(datastoreObjectId);
                            dmeVmwareRelation.setStoreName(nfsName);
                            dmeVmwareRelation.setStoreType(ToolUtils.STORE_TYPE_NFS);
                        } else {
                            logger.error("can not find target host!");
                        }
                    }
                }
                response = gson.toJson(dmeVmwareRelation);
            } else {
                response = FAILED_RESULT;
            }
            logger.info("end creat nfs datastore");
        } catch (Exception e) {
            logger.error("vmware creat nfs error:", e);
            throw new VcenterException("vmware create nfs datastore error!" + e.getMessage());
        }
        return response;
    }

    public void hostRescanVmfs(String hostIp) throws VcenterException {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO hostMo = hostVmwareFactory.build(vmwareContext, host.first());
                        String hostName = hostMo.getHostName();
                        if (hostIp.equals(hostName)) {
                            HostStorageSystemMO hssMo = hostMo.getHostStorageSystemMo();
                            hssMo.rescanVmfs();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("vmware host rescanVmfs error:", e);
            throw new VcenterException(e.getMessage());
        }
    }

    public void hostRescanHba(String hostIp) throws VcenterException {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO hostMo = hostVmwareFactory.build(vmwareContext, host.first());
                        String hostName = hostMo.getHostName();
                        if (hostIp.equals(hostName)) {
                            // 在查找可用LUN前先扫描hba，已发现新的卷
                            List<String> devices = getHbaDeviceByHost(hostMo);
                            if (devices != null && devices.size() > 0) {
                                for (String device : devices) {
                                    hostMo.getHostStorageSystemMo().rescanHba(device);
                                }
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("vmware host rescan HBA error:", e);
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 得到主机对应的可用LUN
     *
     * @param hostName hostName
     * @return String
     * @throws VcenterException VcenterException
     */
    public String getLunsOnHost(String hostName) throws VcenterException {
        String lunStr = "";
        try {
            HostMO hostMo = null;
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                        if (host1.getName().equals(hostName)) {
                            hostMo = host1;
                            break;
                        }
                    }
                }
                if (hostMo != null) {
                    HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMo();
                    List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                    if (hostScsiDisks != null) {
                        List<Map<String, Object>> lunlist = new ArrayList<>();
                        for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("uuid", hostScsiDisk.getUuid());
                            map.put("devicePath", hostScsiDisk.getDevicePath());
                            map.put("deviceName", hostScsiDisk.getDeviceName());
                            map.put("localDisk", hostScsiDisk.isLocalDisk());
                            map.put("block", hostScsiDisk.getCapacity().getBlock());
                            map.put("blockSize", hostScsiDisk.getCapacity().getBlockSize());
                            lunlist.add(map);
                        }
                        if (lunlist.size() > 0) {
                            lunStr = gson.toJson(lunlist);
                        }
                    } else {
                        throw new Exception("host " + hostName + " no available LUN。");
                    }
                } else {
                    throw new Exception("has not the host!host name is " + hostName);
                }
            }
        } catch (Exception e) {
            logger.error("getLunsOnHost error!hostName={},error:{}", hostName, e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return lunStr;
    }

    /**
     * 得到主机对应的可用LUN 20200918objectId
     *
     * @param hostObjectId hostObjectId
     * @param capacity capacity
     * @param volumeWwn volumeWwn
     * @return String
     * @throws VcenterException VcenterException
     */
    public Map<String, Object> getLunsOnHost(String hostObjectId, int capacity, String volumeWwn)
        throws VcenterException {
        Map<String, Object> remap;
        HostScsiDisk candidateHostScsiDisk;
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            HostMO hostMo = hostVmwareFactory.build(vmwareContext, objmor);

            // 在查找可用LUN前先扫描hba，已发现新的卷
            List<String> devices = getHbaDeviceByHost(hostMo);
            if (devices != null && devices.size() > 0) {
                for (String device : devices) {
                    hostMo.getHostStorageSystemMo().rescanHba(device);
                }
            }
            HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMo();
            List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
            candidateHostScsiDisk = getObjectLuns(hostScsiDisks, capacity, volumeWwn);
            remap = new HashMap<>();
            remap.put(HOST_FIELD, hostMo);
            remap.put(HOST_SCSI_DISK, candidateHostScsiDisk);
        } catch (Exception e) {
            logger.error("host:{}, error:{}", hostObjectId, e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return remap;
    }

    /**
     * 得到主机的hba设备名
     *
     * @param hostMo hostMo
     * @return String
     * @throws VcenterException VcenterException
     */
    public List<String> getHbaDeviceByHost(HostMO hostMo) {
        List<String> devices = null;
        try {
            if (hostMo == null) {
                return devices;
            }
            List<HostHostBusAdapter> hbas = hostMo.getHostStorageSystemMo().getStorageDeviceInfo().getHostBusAdapter();
            if (hbas != null && hbas.size() > 0) {
                devices = new ArrayList<>();
                for (HostHostBusAdapter hba : hbas) {
                    if (hba instanceof HostInternetScsiHba) {
                        HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                        if (!StringUtils.isEmpty(iscsiHba.getDevice())) {
                            devices.add(iscsiHba.getDevice());
                        }
                    } else if (hba instanceof HostFibreChannelHba) {
                        HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                        if (!StringUtils.isEmpty(fcHba.getDevice())) {
                            devices.add(fcHba.getDevice());
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("get Hba Device By Host error:", e);
        }
        return devices;
    }

    /**
     * 得到集群下所有主机对应的可用LUN 20200918objectId
     *
     * @param clusterObjectId clusterObjectId
     * @param capacity capacity
     * @param volumeWwn volumeWwn
     * @return String
     * @throws VcenterException VcenterException
     */
    public Map<String, Object> getLunsOnCluster(String clusterObjectId, int capacity, String volumeWwn)
        throws VcenterException {
        Map<String, Object> remap;
        HostScsiDisk candidateHostScsiDisk;
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(clusterObjectId);
            ClusterMO cl1 = clusterVmwareMoFactory.build(vmwareContext, objmor);
            List<Pair<ManagedObjectReference, String>> hosts = cl1.getClusterHosts();
            if (hosts != null && hosts.size() > 0) {
                Map<String, HostMO> hostMap = new HashMap<>();
                List<HostScsiDisk> objHostScsiDisks = new ArrayList<>();
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    HostMO hostMo = hostVmwareFactory.build(vmwareContext, host.first());

                    // 在查找可用LUN前先扫描hba，已发现新的卷
                    List<String> devices = getHbaDeviceByHost(hostMo);
                    if (devices != null && devices.size() > 0) {
                        for (String device : devices) {
                            hostMo.getHostStorageSystemMo().rescanHba(device);
                        }
                    }
                    HostDatastoreSystemMO hostDatastoreSystem = hostMo.getHostDatastoreSystemMo();
                    List<HostScsiDisk> hostScsiDisks = hostDatastoreSystem.queryAvailableDisksForVmfs();
                    if (hostScsiDisks != null && hostScsiDisks.size() > 0) {
                        objHostScsiDisks.addAll(hostScsiDisks);
                        for (HostScsiDisk hsd : hostScsiDisks) {
                            hostMap.put(hsd.getDevicePath(), hostMo);
                        }
                    }
                }

                if (objHostScsiDisks.size() > 0) {
                    candidateHostScsiDisk = getObjectLuns(objHostScsiDisks, capacity, volumeWwn);
                    remap = new HashMap<>();
                    remap.put(HOST_FIELD, hostMap.get(candidateHostScsiDisk.getDevicePath()));
                    remap.put(HOST_SCSI_DISK, candidateHostScsiDisk);
                } else {
                    throw new Exception("cluster:" + clusterObjectId + " non-existent LUN。");
                }
            } else {
                throw new Exception("cluster:" + clusterObjectId + " non-existent HOST。");
            }
        } catch (Exception e) {
            logger.error("cluster get lun error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return remap;
    }

    /**
     * 得到主机对应的可用LUN 20200918objectId
     *
     * @param hostScsiDisks hostScsiDisks
     * @param capacity capacity
     * @param volumeWwn volumeWwn
     * @return String
     * @throws VcenterException VcenterException
     */
    public HostScsiDisk getObjectLuns(List<HostScsiDisk> hostScsiDisks, int capacity, String volumeWwn)
        throws VcenterException {
        HostScsiDisk candidateHostScsiDisk = null;
        try {
            if (hostScsiDisks != null && hostScsiDisks.size() > 0 && capacity > 0) {
                for (HostScsiDisk hostScsiDisk : hostScsiDisks) {
                    if (hostScsiDisk.getCanonicalName().equals("naa." + volumeWwn)) {
                        candidateHostScsiDisk = hostScsiDisk;
                        break;
                    }
                }
                if (candidateHostScsiDisk != null) {
                    logger.info("Create datastore via on disk {}", candidateHostScsiDisk.getDevicePath());
                } else {
                    throw new Exception("host No find available LUN。");
                }
            } else {
                throw new Exception("host No available LUN。");
            }
        } catch (Exception e) {
            logger.error("get LUN error:", e);
            throw new VcenterException(e.getMessage());
        }
        return candidateHostScsiDisk;
    }

    /**
     * 创建vmfs存储 20200918objectId
     *
     * @param hsdmap hsdmap
     * @param capacity capacity
     * @param datastoreName datastoreName
     * @param vmfsMajorVersion vmfsMajorVersion
     * @param blockSize blockSize
     * @param unmapGranularity unmapGranularity
     * @param unmapPriority unmapPriority
     * @return String
     * @throws VcenterException VcenterException
     */
    public String createVmfsDataStore(Map<String, Object> hsdmap, int capacity, String datastoreName,
        int vmfsMajorVersion, int blockSize, int unmapGranularity, String unmapPriority) throws VcenterException {
        String dataStoreStr = "";
        logger.info("begin create vmfs datastore!");
        try {
            if (hsdmap != null && hsdmap.get(DmeConstants.HOST) != null) {
                HostScsiDisk objhsd = (HostScsiDisk) hsdmap.get(HOST_SCSI_DISK);
                HostMO hostMo = (HostMO) hsdmap.get(HOST_FIELD);
                if (hostMo != null && objhsd != null) {
                    long totalSectors = capacity * 1L * ToolUtils.GI / objhsd.getCapacity().getBlockSize();
                    logger.info("Vmfs totalSectors=={}", totalSectors);
                    ManagedObjectReference datastore;
                    try {
                        datastore = hostMo.getHostDatastoreSystemMo()
                            .createVmfsDatastore(datastoreName, objhsd, vmfsMajorVersion, blockSize, totalSectors,
                                unmapGranularity, unmapPriority);

                        logger.info("rescanVmfs after createVmfsDatastore!datastore={}", datastore);
                        hostMo.getHostStorageSystemMo().rescanVmfs();
                    } catch (Exception e) {
                        throw new VcenterException(e.getMessage());
                    }
                    if (null != datastore) {
                        DatastoreMO dsMo = datastoreVmwareMoFactory.build(hostMo.getContext(), datastore);
                        Map<String, Object> dataStoremap = new HashMap<>();
                        String objectId = vcConnectionHelpers.mor2ObjectId(dsMo.getMor(),
                            dsMo.getContext().getServerAddress());
                        dataStoremap.put(NAME, dsMo.getName());
                        dataStoremap.put(ID, dsMo.getMor().getValue());
                        dataStoremap.put(OBJECT_ID, objectId);
                        dataStoremap.put(TYPE, dsMo.getMor().getType());
                        dataStoremap.put(CAPACITY, dsMo.getSummary().getCapacity());
                        dataStoremap.put(HOST_NAME, hostMo.getName());
                        dataStoreStr = gson.toJson(dataStoremap);
                    }
                } else {
                    throw new Exception("host:" + hostMo.getName() + " non-existent。");
                }
            } else {
                throw new Exception("host and LUN non-existent。");
            }
        } catch (Exception e) {
            logger.error("createVmfsDataStore error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return dataStoreStr;
    }

    /**
     * vmfs存储打标记 20200918objectId
     *
     * @param datastoreType datastoreType
     * @param datastoreId datastoreId
     * @param serviceLevelName serviceLevelName
     * @param vcenterinfo vCenterInfo
     * @return String
     */
    public String attachTag(String datastoreType, String datastoreId, String serviceLevelName,
        VCenterInfo vcenterinfo) {
        String attachTagStr = "";
        if (vcenterinfo == null || StringUtils.isEmpty(vcenterinfo.getHostIp())) {
            logger.error("vCenter Info is null");
            return attachTagStr;
        }
        SessionHelper sessionHelper = null;
        try {
            if (StringUtils.isEmpty(datastoreType)) {
                logger.info("DataStore Type is null,unable tag。");
                return attachTagStr;
            }
            if (StringUtils.isEmpty(datastoreId)) {
                logger.info("DataStore Id is null,unable tag。");
                return attachTagStr;
            }
            if (StringUtils.isEmpty(serviceLevelName)) {
                logger.info("Service Level Name is null,unable tag。");
                return attachTagStr;
            }
            sessionHelper = sessionHelperFactory.build();
            sessionHelper.login(vcenterinfo.getHostIp(), String.valueOf(vcenterinfo.getHostPort()),
                vcenterinfo.getUserName(), CipherUtils.decryptString(vcenterinfo.getPassword()));
            TaggingWorkflow taggingWorkflow = taggingWorkflowFactory.build(sessionHelper);

            List<String> taglist = taggingWorkflow.listTags();
            for (String tagid : taglist) {
                TagModel tagModel = taggingWorkflow.getTag(tagid);
                if (tagModel.getName().equals(serviceLevelName)) {
                    DynamicID objDynamicId = new DynamicID(datastoreType, datastoreId);
                    taggingWorkflow.attachTag(tagid, objDynamicId);
                    logger.info("Service Level:{} Associated", serviceLevelName);
                    attachTagStr = "Associated";
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("attachTag error!datastoreId={},error:", datastoreId, e.getMessage());
        } finally {
            if (sessionHelper != null) {
                sessionHelper.logout();
            }
        }
        return attachTagStr;
    }

    /**
     * 删除vmfs存储
     *
     * @param datastoreobjectid datastoreobjectid
     * @return String
     * @throws VcenterException VcenterException
     */
    public boolean deleteVmfsDataStore(String datastoreobjectid) throws VcenterException {
        boolean isDelete = false;
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
                List<Pair<ManagedObjectReference, String>> hosts = rootFsMo.getAllHostOnRootFs();
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                        HostDatastoreSystemMO hdsMo = host1.getHostDatastoreSystemMo();
                        if (null != datastoreobjectid) {
                            isDelete = hdsMo.deleteDatastore(vcConnectionHelpers.objectId2Mor(datastoreobjectid));
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("vmware delete vmfs error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return isDelete;
    }

    /**
     * 将存储挂载到集群下其它主机 20200918objectId
     *
     * @param datastoreStr datastoreStr
     * @param clusterObjectId clusterObjectId
     * @param hostObjectId hostObjectId
     * @throws VcenterException VcenterException
     **/
    public void mountVmfsOnCluster(String datastoreStr, String clusterObjectId, String hostObjectId)
        throws VcenterException {
        if (StringUtils.isEmpty(datastoreStr)) {
            logger.info("datastoreStr is null");
            return;
        }
        if (StringUtils.isEmpty(hostObjectId) && StringUtils.isEmpty(clusterObjectId)) {
            logger.info("host:{} and cluster:{} is null", hostObjectId, clusterObjectId);
            return;
        }
        Map<String, Object> dsmap = gson.fromJson(datastoreStr, new TypeToken<Map<String, Object>>() { }.getType());
        String objHostName = "";
        String objDataStoreName = "";
        if (dsmap != null) {
            objHostName = ToolUtils.getStr(dsmap.get(HOST_NAME));
            objHostName = objHostName == null ? "" : objHostName;
            objDataStoreName = ToolUtils.getStr(dsmap.get(NAME));
        }
        try {
            String serverguid = null;
            if (!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            }
            if (StringUtils.isEmpty(serverguid)) {
                logger.error("mountVmfsOnCluster serverguid is null");
                throw new VcenterException("mount vmfs on cluster error!serverguid is null");
            }
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            if (!StringUtils.isEmpty(clusterObjectId)) {
                logger.info("mount Vmfs to Cluster begin");
                // 集群下的所有主机
                List<Pair<ManagedObjectReference, String>> hosts = getHostsOnCluster(clusterObjectId, hostObjectId);
                if (hosts != null && hosts.size() > 0) {
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        try {
                            HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                            logger.info("Host under Cluster: {}", host1.getName());

                            // 只挂载其它的主机
                            if (host1 != null && !objHostName.equals(host1.getName())) {
                                mountVmfs(objDataStoreName, host1);
                            }
                        } catch (Exception e) {
                            logger.error("mount Vmfs On Cluster error:{}", e.getMessage());
                        }
                    }
                }
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                try {
                    logger.info("mount Vmfs to host begin");
                    ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
                    HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);

                    // 只挂载其它的主机
                    if (hostmo != null && !objHostName.equals(hostmo.getName())) {
                        mountVmfs(objDataStoreName, hostmo);
                    }
                } catch (Exception e) {
                    logger.error("mount Vmfs On Cluster error:{}", e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("mount Vmfs On Cluster error:", e);
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 将vmfs从主机或集群上卸载
     *
     * @param datastoreStr datastoreStr
     * @param clusterObjectId clusterObjectId
     * @param hostObjectId hostObjectId
     * @throws VcenterException VcenterException
     **/
    public void unmountVmfsOnHostOrCluster(String datastoreStr, String clusterObjectId, String hostObjectId)
        throws VcenterException {
        try {
            if (StringUtils.isEmpty(datastoreStr)) {
                logger.info("unmountVmfs datastore datastoreStr is null");
                return;
            }
            if (StringUtils.isEmpty(hostObjectId) && StringUtils.isEmpty(clusterObjectId)) {
                logger.info("unmountVmfs host hostObjectId and clusterObjectId is null");
                return;
            }
            Map<String, Object> dsmap = gson.fromJson(datastoreStr, new TypeToken<Map<String, Object>>() { }.getType());
            String objHostName = "";
            String objDataStoreName = "";
            if (dsmap != null) {
                objHostName = ToolUtils.getStr(dsmap.get(HOST_NAME));
                objHostName = objHostName == null ? "" : objHostName;
                objDataStoreName = ToolUtils.getStr(dsmap.get(NAME));
            }
            String serverguid = null;
            if (!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            }
            if (!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    // 集群下的所有主机
                    List<Pair<ManagedObjectReference, String>> hosts = getHostsOnCluster(clusterObjectId, hostObjectId);
                    if (hosts == null && hosts.size() == 0) {
                        return;
                    }
                    for (Pair<ManagedObjectReference, String> host : hosts) {
                        try {
                            HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());

                            // 从挂载的主机卸载
                            if (host1 != null && objHostName.equals(host1.getName())) {
                                unmountVmfs(objDataStoreName, host1);
                            }
                        } catch (Exception e) {
                            logger.error("unmount Vmfs On Cluster error:", e);
                        }
                    }
                } else if (!StringUtils.isEmpty(hostObjectId)) {
                    try {
                        ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
                        HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);

                        // 从挂载的主机卸载
                        if (hostmo != null) {
                            unmountVmfs(objDataStoreName, hostmo);
                        }
                    } catch (Exception e) {
                        logger.error("mount Vmfs On Cluster error:", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("unmount Vmfs On Cluster error:", e);
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 挂载存储 20200918objectId
     *
     * @param datastoreName datastoreName
     * @param hostMo hostMo
     * @throws VcenterException VcenterException
     **/
    public void mountVmfs(String datastoreName, HostMO hostMo) {
        try {
            if (StringUtils.isEmpty(datastoreName)) {
                logger.info("datastore Name is null");
                return;
            }
            if (hostMo == null) {
                logger.info("host info is null");
                return;
            }

            // 挂载前重新扫描datastore
            logger.info("refreshStorageSystem before mount Vmfs!");
            hostMo.getHostStorageSystemMo().refreshStorageSystem();

           /* // 查询目前未挂载的卷
            for (HostFileSystemMountInfo mount : hostMo.getHostStorageSystemMo()
                .getHostFileSystemVolumeInfo()
                .getMountInfo()) {
                if (mount.getVolume() instanceof HostVmfsVolume && datastoreName.equals(mount.getVolume().getName())) {
                    HostVmfsVolume volume = (HostVmfsVolume) mount.getVolume();

                    // 挂载卷
                    hostMo.getHostStorageSystemMo().mountVmfsVolume(volume.getUuid());
                    logger.info("mount Vmfs success!");
                }
            }

            // 挂载后重新扫描datastore
            logger.info("refreshStorageSystem after mount Vmfs!");
            hostMo.getHostStorageSystemMo().refreshStorageSystem();*/
        } catch (Exception e) {
            logger.error(" mount vmfs volume error!datastoreName={},error:{}", datastoreName, e.getMessage());
        }
    }

    /**
     * 卸载存储 20201016objectId
     *
     * @param datastoreName datastoreName
     * @param hostMo hostMo
     **/
    public void unmountVmfs(String datastoreName, HostMO hostMo) {

        if (StringUtils.isEmpty(datastoreName)) {
            logger.info("unmountVmfs datastore Name is null");
            return;
        }
        if (hostMo == null) {
            logger.info("unmountVmfs host is null");
            return;
        }
        try {
            // 卸载前重新扫描datastore  20201019 暂时屏蔽此方法。DME侧卸载后，vcenter侧调用重新扫描接口，会直接删除此vmfs 原因不详
            hostMo.getHostStorageSystemMo().rescanVmfs();
            for (HostFileSystemMountInfo mount : hostMo.getHostStorageSystemMo()
                .getHostFileSystemVolumeInfo()
                .getMountInfo()) {
                if (mount.getVolume() instanceof HostVmfsVolume && datastoreName.equals(mount.getVolume().getName())) {
                    HostVmfsVolume volume = (HostVmfsVolume) mount.getVolume();
                    hostMo.getHostStorageSystemMo().unmountVmfsVolume(volume.getUuid());
                    logger.info("unmount Vmfs success,volume name={},host name={}", volume.getName(), hostMo.getName());
                }
            }

            // 重新扫描
            logger.info("==refreshStorageSystem after unmountVmfs==");
            hostMo.getHostStorageSystemMo().refreshStorageSystem();
        } catch (Exception e) {
            logger.error("unmount Vmfs Volume:{},error:{}", datastoreName, e.getMessage());
        }
    }

    /**
     * 在主机上扫描卷和Datastore 20200918objectId
     *
     * @param clusterObjectId clusterObjectId
     * @param hostObjectId hostObjectId
     * @throws VcenterException VcenterException
     **/
    public void scanDataStore(String clusterObjectId, String hostObjectId) throws VcenterException {
        try {
            String serverguid = null;
            if (!StringUtils.isEmpty(clusterObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            } else if (!StringUtils.isEmpty(hostObjectId)) {
                serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            }
            if (!StringUtils.isEmpty(serverguid)) {
                VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

                // 集群下的所有主机
                List<Pair<ManagedObjectReference, String>> hosts = null;
                if (!StringUtils.isEmpty(clusterObjectId)) {
                    hosts = getHostsOnCluster(clusterObjectId, hostObjectId);
                    if (hosts != null && hosts.size() > 0) {
                        for (Pair<ManagedObjectReference, String> host : hosts) {
                            try {
                                HostMO host1 = hostVmwareFactory.build(vmwareContext, host.first());
                                logger.info("Host under Cluster: {}", host1.getName());
                                host1.getHostStorageSystemMo().rescanVmfs();
                            } catch (Exception ex) {
                                logger.error("under Cluster scan Data Store error:{}", ex.getMessage());
                            }
                        }
                    }
                } else if (!StringUtils.isEmpty(hostObjectId)) {
                    try {
                        ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
                        HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);
                        hostmo.getHostStorageSystemMo().rescanVmfs();
                    } catch (Exception ex) {
                        logger.error("scan Data Store error:{}", ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("scan Data Store error:", e);
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 为指定的虚拟机创建磁盘
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @param vmObjectId vmObjectId
     * @param rdmDeviceName rdmDeviceName
     * @param size size
     * @throws VcenterException VcenterException
     **/
    public void createDisk(String dataStoreObjectId, String vmObjectId, String rdmDeviceName, int size)
        throws VcenterException {
        String serverguid = vcConnectionHelpers.objectId2Serverguid(vmObjectId);
        try {
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
            DatastoreMO datastoreMo = datastoreVmwareMoFactory.build(vmwareContext, dsmor);
            String vmdkDatastorePath = String.format("[%s]", datastoreMo.getName());
            VirtualMachineMO virtualMachineMo = virtualMachineMoFactorys.build(vmwareContext,
                vcConnectionHelpers.objectId2Mor(vmObjectId));
            virtualMachineMo.createDisk(vmdkDatastorePath, VirtualDiskType.RDM, VirtualDiskMode.PERSISTENT,
                rdmDeviceName, size * DIGIT_1024, datastoreMo.getMor(), DEFAULT_CONTROLLER_KEY);
        } catch (Exception e) {
            throw new VcenterException(e.getMessage());
        }
    }

    public List<Object> getDatastoreMountsOnHost(String vmObjectId) throws VcenterException {
        List<Object> list = new ArrayList<>();
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(vmObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            VirtualMachineMO virtualMachineMo = virtualMachineMoFactorys.build(vmwareContext,
                vcConnectionHelpers.objectId2Mor(vmObjectId));
            HostMO hostMo = virtualMachineMo.getRunningHost();
            List<Pair<ManagedObjectReference, String>> datastoreMountsOnHost = hostMo.getDatastoreMountsOnHost();
            for (Pair<ManagedObjectReference, String> pair : datastoreMountsOnHost) {
                ManagedObjectReference dsMor = pair.first();
                DatastoreMO datastoreMo = datastoreVmwareMoFactory.build(vmwareContext, dsMor);
                String objectId = vcConnectionHelpers.mor2ObjectId(dsMor, vmwareContext.getServerAddress());
                DatastoreSummary summary = datastoreMo.getSummary();
                if (summary.getType().equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
                    JsonObject jsonObject = gson.fromJson(gson.toJson(summary), JsonObject.class);
                    jsonObject.addProperty(OBJECT_ID, objectId);
                    list.add(jsonObject);
                }
            }
        } catch (Exception e) {
            throw new VcenterException(e.getMessage());
        }

        return list;
    }

    /**
     * 挂载Nfs存储 20200918objectId
     *
     * @param datastoreobjectid datastoreobjectid
     * @param hostobjectid hostobjectid
     * @param logicPortIp logicPortIp
     * @param mountType mountType
     * @throws VcenterException VcenterException
     **/
    public void mountNfs(String datastoreobjectid, String hostobjectid, String logicPortIp, String mountType) {
        try {
            if (datastoreobjectid == null) {
                logger.info("param datastore is null");
                return;
            }
            if (hostobjectid == null) {
                logger.info("host is null");
                return;
            }
            if (logicPortIp == null) {
                logger.info("logicPortIp is null");
                return;
            }
            String serverguid = vcConnectionHelpers.objectId2Serverguid(datastoreobjectid);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            DatastoreMO datastoreMo = datastoreVmwareMoFactory.build(vmwareContext,
                vcConnectionHelpers.objectId2Mor(datastoreobjectid));
            HostMO hostMo = hostVmwareFactory.build(vmwareContext, vcConnectionHelpers.objectId2Mor(hostobjectid));

            // 挂载前重新扫描datastore
            hostMo.getHostStorageSystemMo().refreshStorageSystem();
            logger.info("Rescan datastore before mounting");

            // 挂载NFS
            NasDatastoreInfo nasdsinfo = (NasDatastoreInfo) datastoreMo.getInfo();
            hostMo.getHostDatastoreSystemMo()
                .createNfsDatastore(nasdsinfo.getNas().getRemoteHost(), 0, nasdsinfo.getNas().getRemotePath(),
                    datastoreMo.getName(), mountType, nasdsinfo.getNas().getType(),
                    nasdsinfo.getNas().getSecurityType());
            logger.info("mount nfs success:{}:", hostMo.getName(), datastoreMo.getName());

            // 挂载后重新扫描datastore
            hostMo.getHostStorageSystemMo().refreshStorageSystem();
            logger.info("Rescan datastore after mounting");
        } catch (Exception e) {
            logger.error("vmware mount nfs error:{}", e.getMessage());
        }
    }

    /**
     * 卸载Nfs存储
     *
     * @param dsmo dsmo
     * @param hostMo hostMo
     * @param datastoreobjectid datastoreobjectid
     */
    public void unmountNfsOnHost(DatastoreMO dsmo, HostMO hostMo, String datastoreobjectid) {
        try {
            if (dsmo == null) {
                logger.info("datastore is null");
                return;
            }
            if (hostMo == null) {
                logger.info("host is null");
                return;
            }

            // 卸载前重新扫描datastore
            //hostMo.getHostStorageSystemMo().refreshStorageSystem();
            //logger.info("Rescan datastore before unmounting");

            // 从主机卸载datastore
            hostMo.getHostDatastoreSystemMo().deleteDatastore(vcConnectionHelpers.objectId2Mor(datastoreobjectid));

            // 卸载后重新扫描datastore
            hostMo.getHostStorageSystemMo().refreshStorageSystem();
            logger.info("Rescan datastore after unmounting");
        } catch (Exception e) {
            logger.error("unmount nfs error:{}", e.getMessage());
        }
    }

    public void unmountNfsOnHost(String dataStoreObjectId, String hostObjId) throws VcenterException {
        try {
            if (StringUtils.isEmpty(dataStoreObjectId)) {
                logger.info("param dataStoreObjectId is null");
                return;
            }
            if (StringUtils.isEmpty(hostObjId)) {
                logger.info("param hostObjId is null");
                return;
            }
            String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
            DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, dsmor);
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjId);
            HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);

            // 卸载
            unmountNfsOnHost(dsmo, hostmo, dataStoreObjectId);
        } catch (Exception e) {
            logger.error("unmount nfs On host error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
    }

    public void unmountNfsOnCluster(String dataStoreObjectId, String clusterId) throws VcenterException {
        try {
            if (StringUtils.isEmpty(dataStoreObjectId)) {
                logger.info("unmountNfsOnCluster dataStore object id is null");
                return;
            }
            if (StringUtils.isEmpty(clusterId)) {
                logger.info("unmountNfsOnCluster clusterId is null");
                return;
            }

            String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            RootFsMO rootFsMo = rootVmwareMoFactory.build(vmwareContext, vmwareContext.getRootFolder());
            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
            DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, dsmor);
            ClusterMO clusterMo = rootFsMo.findClusterById(clusterId);
            logger.info("cluster name: {}", clusterMo.getName());

            // 卸载
            unmountNfsOnCluster(dsmo, clusterMo, dataStoreObjectId);
        } catch (Exception e) {
            logger.error("unmount nfs On cluster error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 卸载Nfs存储
     *
     * @param dsmo dsmo
     * @param clusterMo clusterMo
     * @param datastoreobjectid datastoreobjectid
     **/
    public void unmountNfsOnCluster(DatastoreMO dsmo, ClusterMO clusterMo, String datastoreobjectid) {
        try {
            if (dsmo == null) {
                logger.info("datastore is null");
                return;
            }
            if (clusterMo == null) {
                logger.info("cluster is null");
                return;
            }
            logger.info("Hosts that need to be unmounted:{}", clusterMo.getName());

            // 卸载前重新扫描datastore hostMo.getHostStorageSystemMo().rescanVmfs();
            logger.info("Rescan datastore before unmounting");

            // 卸载NFS
            clusterMo.unmountDatastore(vcConnectionHelpers.objectId2Mor(datastoreobjectid));
            logger.info("unmount nfs success!cluster name={},datastore name={}", clusterMo.getName(), dsmo.getName());
        } catch (Exception e) {
            logger.error("unmount nfs error:", e);
        }
    }

    /**
     * Get host's vmKernel IP,only provisioning provisioning
     *
     * @param hostObjectId hostObjectId
     * @return String String
     * @throws VcenterException VcenterException
     **/
    public String getVmKernelIpByHostObjectId(String hostObjectId) throws VcenterException {
        String listStr = "";
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

            // 取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);
            if (hostmo != null) {
                List<VirtualNicManagerNetConfig> nics = hostmo.getHostVirtualNicManagerNetConfig();
                if (nics == null || nics.size() == 0) {
                    return listStr;
                }
                for (VirtualNicManagerNetConfig nic : nics) {
                    if ("vSphereProvisioning".equals(nic.getNicType())) {
                        List<Map<String, Object>> lists = new ArrayList<>();
                        List<HostVirtualNic> subnics = nic.getCandidateVnic();
                        if (subnics == null || subnics.size() == 0) {
                            return listStr;
                        }
                        for (HostVirtualNic subnic : subnics) {
                            if (nic.getSelectedVnic().contains(subnic.getKey())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put(DEVICE_FIELD, subnic.getDevice());
                                map.put("key", subnic.getKey());
                                map.put("portgroup", subnic.getPortgroup());
                                map.put("ipAddress", subnic.getSpec().getIp().getIpAddress());
                                map.put("mac", subnic.getSpec().getMac());
                                map.put("port", subnic.getPort());
                                lists.add(map);
                            }
                        }
                        if (lists.size() > 0) {
                            listStr = gson.toJson(lists);
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return listStr;
    }

    public List<PbmProfile> getAllSelfPolicyInallcontext() {
        List<PbmProfile> pbmProfiles = new ArrayList<>();
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                pbmProfiles.addAll(getAllSelfPolicy(vmwareContext));
            }
        } catch (Exception e) {
            logger.error("get all vm policy failed!{}", e.getMessage());
        }
        return pbmProfiles;
    }

    private List<PbmProfile> getAllSelfPolicy(VmwareContext vmwareContext) {
        List<PbmProfile> pbmProfiles = new ArrayList<>();
        try {
            PbmServiceInstanceContent spbmsc;

            // Get PBM Profile Manager & Associated Capability Metadata
            spbmsc = vmwareContext.getPbmServiceContent();
            ManagedObjectReference profileMgr = spbmsc.getProfileManager();

            // query profiles
            PbmProfileResourceType storageResourceType = PbmUtil.getStorageResourceType();
            List<PbmProfileId> pbmProfileIds = vmwareContext.getPbmService()
                .pbmQueryProfile(profileMgr, storageResourceType, null);
            List<PbmProfile> pbmprofiles = vmwareContext.getPbmService().pbmRetrieveContent(profileMgr, pbmProfileIds);

            // 获取通过插件创建的策略
            for (PbmProfile profile : pbmprofiles) {
                if (POLICY_DESC.equalsIgnoreCase(profile.getDescription())) {
                    pbmProfiles.add(profile);
                }
            }
        } catch (com.vmware.pbm.InvalidArgumentFaultMsg | RuntimeFaultFaultMsg invalidArgumentFaultMsg) {
            logger.error("getAllSelfPolicy error!{}", invalidArgumentFaultMsg.getMessage());
        }
        return pbmProfiles;
    }

    public List<TagModel> getAllTagsByCategoryId(String categoryid, SessionHelper sessionHelper) throws Exception {
        List<TagModel> tagList = new ArrayList<>();

        TaggingWorkflow taggingWorkflow = taggingWorkflowFactory.build(sessionHelper);
        List<String> tags = taggingWorkflow.listTagsForCategory(categoryid);
        for (String tagid : tags) {
            tagList.add(taggingWorkflow.getTag(tagid));
        }
        return tagList;
    }

    public String getCategoryId(SessionHelper sessionHelper) {
        String categoryid = "";

        // TaggingWorkflow taggingWorkflow = new TaggingWorkflow(sessionHelper)
        TaggingWorkflow taggingWorkflow = taggingWorkflowFactory.build(sessionHelper);
        List<String> categorylist = taggingWorkflow.listTagCategory();
        for (String category : categorylist) {
            CategoryModel categoryModel = taggingWorkflow.getTagCategory(category);
            if (categoryModel.getName().equalsIgnoreCase(CATEGORY_NAME)) {
                categoryid = categoryModel.getId();
            }
        }
        if ("".equalsIgnoreCase(categoryid)) {
            // 创建category
            CategoryTypes.CreateSpec createSpec = new CategoryTypes.CreateSpec();
            createSpec.setName(CATEGORY_NAME);
            createSpec.setDescription(CATEGORY_NAME);
            createSpec.setCardinality(CategoryModel.Cardinality.SINGLE);
            Set<String> associableTypes = new HashSet<>();
            associableTypes.add("Datastore");
            createSpec.setAssociableTypes(associableTypes);
            categoryid = taggingWorkflow.createTagCategory(createSpec);
        }

        return categoryid;
    }

    public void createPbmProfileInAllContext(String categoryName, String tagName) {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                createPbmProfile(vmwareContext, categoryName, tagName);
            }
        } catch (Exception e) {
            logger.error("createPbmProfileInAllContext error!{}", e.getMessage());
        }
    }

    private void createPbmProfile(VmwareContext vmwareContext, String categoryName, String tagName)
        throws RuntimeFaultFaultMsg, InvalidArgumentFaultMsg, com.vmware.vim25.RuntimeFaultFaultMsg,
               com.vmware.pbm.InvalidArgumentFaultMsg, PbmFaultProfileStorageFaultFaultMsg, PbmDuplicateNameFaultMsg {
        PbmServiceInstanceContent spbmsc;
        spbmsc = vmwareContext.getPbmServiceContent();
        ManagedObjectReference profileMgr = spbmsc.getProfileManager();
        List<PbmCapabilityMetadataPerCategory> metadata = vmwareContext.getPbmService()
            .pbmFetchCapabilityMetadata(profileMgr, PbmUtil.getStorageResourceType(), null);

        // Step 1: Create Property Instance with tags from the specified Category
        PbmCapabilityMetadata tagCategoryInfo = PbmUtil.getTagCategoryMeta(categoryName, metadata);
        if (tagCategoryInfo == null) {
            throw new InvalidArgumentFaultMsg("Specified Tag Category does not exist", null);
        }

        // Fetch Property Metadata of the Tag Category
        List<PbmCapabilityPropertyMetadata> propMetaList = tagCategoryInfo.getPropertyMetadata();
        PbmCapabilityPropertyMetadata propMeta = propMetaList.get(0);

        // Create aa New Property Instance based on the Tag Category Id
        PbmCapabilityPropertyInstance prop = new PbmCapabilityPropertyInstance();
        prop.setId(propMeta.getId());
        PbmCapabilityDiscreteSet tagSetMeta = (PbmCapabilityDiscreteSet) propMeta.getAllowedValue();
        if (tagSetMeta == null || tagSetMeta.getValues().isEmpty()) {
            throw new com.vmware.vim25.RuntimeFaultFaultMsg(
                "Specified Tag Category '" + categoryName + "' does not have any associated tags", null);
        }

        // Create aa New Discrete Set for holding Tag Values
        PbmCapabilityDiscreteSet tagSet = new PbmCapabilityDiscreteSet();
        for (Object obj : tagSetMeta.getValues()) {
            if (tagName.equalsIgnoreCase(((PbmCapabilityDescription) obj).getValue().toString())) {
                tagSet.getValues().add(((PbmCapabilityDescription) obj).getValue());
            }
        }
        prop.setValue(tagSet);

        // Step 2: Associate Property Instance with aa Rule
        PbmCapabilityConstraintInstance rule = new PbmCapabilityConstraintInstance();
        rule.getPropertyInstance().add(prop);

        // Step 3: Associate Rule with aa Capability Instance
        PbmCapabilityInstance capability = new PbmCapabilityInstance();
        capability.setId(tagCategoryInfo.getId());
        capability.getConstraint().add(rule);

        // Step 4: Add Rule to an RuleSet
        PbmCapabilitySubProfile ruleSet = new PbmCapabilitySubProfile();
        ruleSet.getCapability().add(capability);

        // Step 5: Add Rule-Set to Capability Constraints
        PbmCapabilitySubProfileConstraints constraints = new PbmCapabilitySubProfileConstraints();
        ruleSet.setName("Rule-Set" + (constraints.getSubProfiles().size() + 1));
        constraints.getSubProfiles().add(ruleSet);

        // Step 6: Build Capability-Based Profile
        PbmCapabilityProfileCreateSpec spec = new PbmCapabilityProfileCreateSpec();
        spec.setName(tagName);
        spec.setDescription(POLICY_DESC);
        spec.setResourceType(PbmUtil.getStorageResourceType());
        spec.setConstraints(constraints);

        // Step 7: Create Storage Profile
        vmwareContext.getPbmService().pbmCreate(profileMgr, spec);
    }

    public void createTag(String tagName, SessionHelper sessionHelper) {
        TaggingWorkflow taggingWorkflow = taggingWorkflowFactory.build(sessionHelper);
        taggingWorkflow.createTag(tagName, "", getCategoryId(sessionHelper));
        sessionHelper.logout();
    }

    private void removePbmProfile(VmwareContext vmwareContext, List<PbmProfile> pbmProfiles)
        throws RuntimeFaultFaultMsg {
        List<PbmProfileId> pbmProfileIds = new ArrayList<>();
        for (PbmProfile profile : pbmProfiles) {
            pbmProfileIds.add(profile.getProfileId());
        }
        if (pbmProfileIds.size() > 0) {
            PbmServiceInstanceContent spbmsc = vmwareContext.getPbmServiceContent();
            ManagedObjectReference profileMgr = spbmsc.getProfileManager();
            vmwareContext.getPbmService().pbmDelete(profileMgr, pbmProfileIds);
        }
    }

    public void removePbmProfileInAllContext(List<PbmProfile> pbmProfiles) {
        try {
            VmwareContext[] vmwareContexts = vcConnectionHelpers.getAllContext();
            for (VmwareContext vmwareContext : vmwareContexts) {
                removePbmProfile(vmwareContext, pbmProfiles);
            }
        } catch (Exception e) {
            logger.error("removePbmProfileInAllContext error!{}", e.getMessage());
        }
    }

    public void removeAllTags(List<TagModel> tagModels, SessionHelper sessionHelper) throws Exception {
        TaggingWorkflow taggingWorkflow = taggingWorkflowFactory.build(sessionHelper);
        for (TagModel tagModel : tagModels) {
            taggingWorkflow.deleteTag(tagModel.getId());
        }
    }

    /**
     * 主机配置iscsi
     *
     * @param hostObjectId hostObjectId
     * @param vmKernel vmKernel
     * @param ethPorts ethPorts
     * @throws VcenterException VcenterException
     **/
    public void configureIscsi(String hostObjectId, Map<String, String> vmKernel, List<Map<String, Object>> ethPorts)
        throws VcenterException {
        try {
            if (ethPorts == null) {
                logger.error("configure Iscsi error!ethPorts is null.");
                throw new VcenterException("configure Iscsi error!ethPorts is null.");
            }
            if (vmKernel == null) {
                logger.error("configure Iscsi error!vmKernel is null.");
                throw new VcenterException("configure Iscsi error!vmKernel is null.");
            }
            if (StringUtils.isEmpty(hostObjectId)) {
                logger.error("configure Iscsi error:host ObjectId is null.");
                throw new VcenterException("configure Iscsi error:host ObjectId is null.");
            }
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

            // 取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);

            // 查找对应的iscsi适配器
            String iscsiHbaDevice = null;
            List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMo().getStorageDeviceInfo().getHostBusAdapter();
            for (HostHostBusAdapter hba : hbas) {
                if (hba instanceof HostInternetScsiHba) {
                    HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                    if (!StringUtils.isEmpty(iscsiHba.getDevice())) {
                        iscsiHbaDevice = iscsiHba.getDevice();
                        break;
                    }
                }
            }
            if (StringUtils.isEmpty(iscsiHbaDevice)) {
                logger.error("find iscsi Hba device error!No iSCSI adapter found");
                throw new VcenterException("find iscsi Hba Device error!No iSCSI adapter found");
            }

            // 得到vmKernel适配器,网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
            logger.info("iscsiHbaDevice:{}", iscsiHbaDevice);
            boundVmKernel(hostmo, vmKernel, iscsiHbaDevice);

            // 添加发现目标
            addIscsiSendTargets(hostmo, ethPorts, iscsiHbaDevice);
            logger.info("configure Iscsi success! iscsiHbaDevice:{},host:{}", iscsiHbaDevice, hostmo.getName());
        } catch (Exception e) {
            logger.error("vmware error:{}", e.getMessage());
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
     *
     * @param hostmo hostmo
     * @param vmKernel vmKernel
     * @param iscsiHbaDevice iscsiHbaDevice
     * @throws VcenterException VcenterException
     **/
    public void boundVmKernel(HostMO hostmo, Map<String, String> vmKernel, String iscsiHbaDevice)
        throws VcenterException {
        try {
            if (vmKernel == null) {
                logger.error("configure Iscsi error:vmKernel is null.");
                throw new Exception("configure Iscsi error:vmKernel is null.");
            }
            if (hostmo == null) {
                logger.error("boundVmKernel:host is null.");
                throw new Exception("host is null.");
            }
            if (StringUtils.isEmpty(iscsiHbaDevice)) {
                logger.error("boundVmKernel param iscsiHbaDevice is null!");
                throw new Exception("boundVmKernel param iscsiHbaDevice is null!");
            }

            // 得到vmKernel适配器
            String vmKernelDevice = ToolUtils.getStr(vmKernel.get(DEVICE_FIELD));
            if (StringUtils.isEmpty(vmKernelDevice)) {
                logger.error("find vmKernel Device error:No vmKernel adapter found");
                throw new Exception("find vmKernel Device error:No vmKernel adapter found");
            }
            logger.info("vmKernelDevice:{}", vmKernelDevice);

            // 网络端口邦定，将vmKernelDevice邦定到iscsiHbaDevice
            List<IscsiPortInfo> iscsiPortInfos = hostmo.getIscsiManagerMo().queryBoundVnics(iscsiHbaDevice);
            for (IscsiPortInfo info : iscsiPortInfos) {
                if (vmKernelDevice.equalsIgnoreCase(info.getVnicDevice())) {
                    logger.info("already bind Vnic! iscsiHbaDevice:{},vmKernelDevice:", iscsiHbaDevice, vmKernelDevice);
                    return;
                }
            }
            hostmo.getIscsiManagerMo().bindVnic(iscsiHbaDevice, vmKernelDevice);
            logger.info("bind Vnic success! iscsiHbaDevice:{},vmKernelDevice:{}", iscsiHbaDevice, vmKernelDevice);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 添加发现目标
     *
     * @param hostmo hostmo
     * @param ethPorts ethPorts
     * @param iscsiHbaDevice iscsiHbaDevice
     * @throws VcenterException VcenterException
     **/
    public void addIscsiSendTargets(HostMO hostmo, List<Map<String, Object>> ethPorts, String iscsiHbaDevice)
        throws VcenterException {
        try {
            if (ethPorts == null) {
                logger.error("configure Iscsi error:ethPorts is null.");
                throw new Exception("configure Iscsi error:ethPorts is null.");
            }
            if (hostmo == null) {
                logger.error("configure Iscsi error:host is null.");
                throw new Exception("configure Iscsi error:host is null.");
            }
            if (StringUtils.isEmpty(iscsiHbaDevice)) {
                logger.error("find iscsi Hba Device error:No iSCSI adapter found");
                throw new Exception("find iscsi Hba Device error:No iSCSI adapter found");
            }

            // 添加发现目标
            if (ethPorts != null && ethPorts.size() > 0) {
                List<HostInternetScsiHbaSendTarget> targets = new ArrayList<>();
                for (Map<String, Object> ethPortInfo : ethPorts) {
                    // 组装发现目标
                    HostInternetScsiHbaSendTarget target = new HostInternetScsiHbaSendTarget();
                    target.setAddress(ToolUtils.getStr(ethPortInfo.get("mgmtIp")));
                    targets.add(target);
                }
                if (targets.size() > 0) {
                    // 向iscsi添加目标
                    hostmo.getHostStorageSystemMo().addInternetScsiSendTargets(iscsiHbaDevice, targets);
                    logger.info(
                        "add Iscsi Send Targets success! iscsiHbaDevice:" + iscsiHbaDevice + " targets:" + gson.toJson(
                            targets));
                }

                // 添加完成后，重新扫描hba
                hostmo.getHostStorageSystemMo().rescanHba(iscsiHbaDevice);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
    }

    /**
     * 删除NFS dataStore
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @param hostObjIds hostObjIds
     * @throws VcenterException VcenterException
     **/
    public boolean deleteNfs(String dataStoreObjectId, List<String> hostObjIds) throws VcenterException {
        boolean reValue = true;
        logger.info("vmware delete nfs begin!");
        if (StringUtils.isEmpty(dataStoreObjectId)) {
            logger.info("delete dataStore error! datasotreObject id is null");
            return reValue;
        }
        if (hostObjIds == null || hostObjIds.size() == 0) {
            logger.info("delete datasotre error!param hostObjIds is null");
            return reValue;
        }
        String serverguid = vcConnectionHelpers.objectId2Serverguid(dataStoreObjectId);
        try {
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

            ManagedObjectReference dsmor = vcConnectionHelpers.objectId2Mor(dataStoreObjectId);
            DatastoreMO dsmo = datastoreVmwareMoFactory.build(vmwareContext, dsmor);
            for (String hostObjId : hostObjIds) {
                ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjId);
                HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);
                logger.info("Host name: {}", hostmo.getName());

                // 主机删除存储
                deleteNfs(dsmo, hostmo, dataStoreObjectId);
                break;
            }
        } catch (Exception e) {
            reValue = false;
            logger.error("vmware delete nfs error!{}", gson.toJson(e));
        }
        logger.info("vmware delete nfs end!reValue={}", reValue);
        return reValue;
    }

    public void deleteNfs(DatastoreMO dsmo, HostMO hostMo, String datastoreobjectid) throws Exception {
        // 删除NFS
        logger.info("=========vmware delete nfs process begin=========");
        hostMo.getHostDatastoreSystemMo().deleteDatastore(vcConnectionHelpers.objectId2Mor(datastoreobjectid));
        logger.info("=========vmware delete nfs process end=========");

        // 删除后重新扫描datastore
        hostMo.getHostStorageSystemMo().rescanVmfs();
    }

    /**
     * 通过主机ID得到主机的hba
     *
     * @param hostObjectId hostObjectId
     * @return List
     * @throws VcenterException VcenterException
     */
    public Map<String, Object> getHbaByHostObjectId(String hostObjectId) throws VcenterException {
        Map<String, Object> map = new HashMap<>();
        try {
            if (StringUtils.isEmpty(hostObjectId)) {
                logger.error("get Hba by host id error:host ObjectId is null.");
                throw new Exception("get Hba by host id error:host ObjectId is null.");
            }
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

            // 取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);

            // 查找对应的iscsi适配器
            List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMo().getStorageDeviceInfo().getHostBusAdapter();
            for (HostHostBusAdapter hba : hbas) {
                if (hba instanceof HostInternetScsiHba) {
                    HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                    map.put(TYPE, ISCSI_TYPE);
                    map.put(NAME, iscsiHba.getIScsiName());
                } else if (hba instanceof HostFibreChannelHba) {
                    HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                    map.put(TYPE, FC_TYPE);
                    logger.info("host = {},fc hba long = {}", hostObjectId,
                        ToolUtils.normalizeWwn(fcHba.getPortWorldWideName()));
                    map.put(NAME, ToolUtils.normalizeWwn(fcHba.getPortWorldWideName()));
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return map;
    }

    /**
     * 通过主机ID得到主机的hba
     *
     * @param hostObjectId hostObjectId
     * @return List
     * @throws VcenterException VcenterException
     */
    public List<Map<String, Object>> getHbasByHostObjectId(String hostObjectId) throws VcenterException {
        List<Map<String, Object>> hbalist = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(hostObjectId)) {
                logger.error("get Hba error:host ObjectId is null.");
                throw new Exception("get Hba error:host ObjectId is null.");
            }
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

            // 取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            HostMO hostmo = hostVmwareFactory.build(vmwareContext, objmor);

            // 查找对应的iscsi适配器
            List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMo().getStorageDeviceInfo().getHostBusAdapter();
            for (HostHostBusAdapter hba : hbas) {
                if (hba instanceof HostInternetScsiHba) {
                    Map<String, Object> map = new HashMap<>();
                    HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                    map.put(TYPE, ISCSI_TYPE);
                    map.put(NAME, iscsiHba.getIScsiName());
                    hbalist.add(map);
                } else if (hba instanceof HostFibreChannelHba) {
                    Map<String, Object> map = new HashMap<>();
                    HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                    map.put(TYPE, FC_TYPE);
                    logger.info("host = {},fc hba long = {}", hostObjectId,
                        ToolUtils.normalizeWwn(fcHba.getPortWorldWideName()));
                    map.put(NAME, ToolUtils.normalizeWwn(fcHba.getPortWorldWideName()));
                    hbalist.add(map);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return hbalist;
    }

    /**
     * 通过集群ID得到主机的hba
     *
     * @param clusterObjectId clusterObjectId
     * @return List
     * @throws VcenterException VcenterException
     */
    public List<Map<String, Object>> getHbasByClusterObjectId(String clusterObjectId) throws VcenterException {
        List<Map<String, Object>> hbalist = new ArrayList<>();
        try {
            if (StringUtils.isEmpty(clusterObjectId)) {
                logger.error("get Hba error:cluster ObjectId is null.");
                throw new Exception("get Hba error:cluster ObjectId is null.");
            }
            String serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);

            // 取得该存储下所有已经挂载的主机ID
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(clusterObjectId);
            ClusterMO objmo = clusterVmwareMoFactory.build(vmwareContext, objmor);
            List<Pair<ManagedObjectReference, String>> hosts = objmo.getClusterHosts();
            if (hosts != null && hosts.size() > 0) {
                for (Pair<ManagedObjectReference, String> host : hosts) {
                    List<Map<String, Object>> subhbalist = new ArrayList<>();
                    HostMO hostmo = hostVmwareFactory.build(vmwareContext, host.first());
                    List<HostHostBusAdapter> hbas = hostmo.getHostStorageSystemMo()
                        .getStorageDeviceInfo()
                        .getHostBusAdapter();
                    for (HostHostBusAdapter hba : hbas) {
                        if (hba instanceof HostInternetScsiHba) {
                            Map<String, Object> map = new HashMap<>();
                            HostInternetScsiHba iscsiHba = (HostInternetScsiHba) hba;
                            map.put(TYPE, ISCSI_TYPE);
                            map.put(NAME, iscsiHba.getIScsiName());
                            subhbalist.add(map);
                        } else if (hba instanceof HostFibreChannelHba) {
                            Map<String, Object> map = new HashMap<>();
                            HostFibreChannelHba fcHba = (HostFibreChannelHba) hba;
                            map.put(TYPE, FC_TYPE);
                            logger.info("hostname = {},fc hba long = {}", hostmo.getName(),
                                ToolUtils.normalizeWwn(fcHba.getPortWorldWideName()));
                            map.put(NAME, ToolUtils.normalizeWwn(fcHba.getPortWorldWideName()));
                            subhbalist.add(map);
                        }
                    }
                    if (subhbalist.size() > 0) {
                        hbalist.addAll(subhbalist);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new VcenterException(e.getMessage());
        }
        return hbalist;
    }

    /**
     * 使用主机测试目标机的连通性
     *
     * @param hostObjectId hostObjectId
     * @param ethPorts ethPorts
     * @param vmKernel vmKernel
     * @param vcenterinfo vCenterInfo
     * @return String
     * @throws VcenterException VcenterException
     */
    public String testConnectivity(String hostObjectId, List<Map<String, Object>> ethPorts,
        Map<String, String> vmKernel, VCenterInfo vcenterinfo) {
        String reStr = null;
        if (StringUtils.isEmpty(hostObjectId)) {
            logger.error("host object id is null");
            return reStr;
        }
        if (ethPorts == null || ethPorts.size() == 0) {
            logger.error("ethPorts is null");
            return reStr;
        }
        if (vcenterinfo == null || StringUtils.isEmpty(vcenterinfo.getHostIp())) {
            logger.error("vCenter Info is null");
            return reStr;
        }
        Client vmomiClient = null;
        SessionManager sessionManager = null;
        try {
            HttpConfiguration httpConfig = new HttpConfigurationImpl();
            httpConfig.setTimeoutMs(TEST_CONNECTIVITY_TIMEOUT);
            httpConfig.setThumbprintVerifier(new AllowAllThumbprintVerifier());

            HttpClientConfiguration clientConfig = HttpClientConfiguration.Factory.newInstance();
            clientConfig.setHttpConfiguration(httpConfig);
            try {
                if (context == null) {
                    context = VmodlContext.getContext();
                    context.loadVmodlPackages(new String[] {"com.vmware.vim.binding.vmodl.reflect"});
                }
            } catch (Exception e) {
                logger.error("context is not ready!{}", e.getMessage());
            }
            if (context == null) {
                context = VmodlContext.initContext(
                    new String[] {"com.vmware.vim.binding.vim", "com.vmware.vim.binding.vmodl.reflect"});
            }

            logger.info("vcenter info=={}", gson.toJson(vcenterinfo));
            vmomiClient = Client.Factory.createClient(
                new URI("https://" + vcenterinfo.getHostIp() + ":" + vcenterinfo.getHostPort() + "/sdk"), VERSION,
                context, clientConfig);
            com.vmware.vim.binding.vmodl.ManagedObjectReference svcRef
                = new com.vmware.vim.binding.vmodl.ManagedObjectReference();
            svcRef.setType("ServiceInstance");
            svcRef.setValue("ServiceInstance");
            ServiceInstance instance = vmomiClient.createStub(ServiceInstance.class, svcRef);
            ServiceInstanceContent serviceInstanceContent = instance.retrieveContent();

            sessionManager = vmomiClient.createStub(SessionManager.class, serviceInstanceContent.getSessionManager());
            sessionManager.login(vcenterinfo.getUserName(), CipherUtils.decryptString(vcenterinfo.getPassword()), "en");
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjectId);
            com.vmware.vim.binding.vmodl.ManagedObjectReference hostmor
                = new com.vmware.vim.binding.vmodl.ManagedObjectReference();
            hostmor.setType(objmor.getType());
            hostmor.setValue(objmor.getValue());
            HostSystem hostSystem = vmomiClient.createStub(HostSystem.class, hostmor);
            com.vmware.vim.binding.vmodl.ManagedObjectReference methodexecutor
                = hostSystem.retrieveManagedMethodExecuter();

            String moid = "ha-cli-handler-network-diag";
            ManagedMethodExecuter methodExecuter = vmomiClient.createStub(ManagedMethodExecuter.class, methodexecutor);
            if (ethPorts != null && ethPorts.size() > 0) {
                String device = null;
                if (vmKernel != null) {
                    device = ToolUtils.getStr(vmKernel.get(DEVICE_FIELD));
                }

                List<Map<String, Object>> reEthPorts = new ArrayList<>();
                ExecutorService taskExecutor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

                // 用于判断所有的线程是否结束
                final CountDownLatch latch = new CountDownLatch(ethPorts.size());
                for (Map<String, Object> ethPort : ethPorts) {
                    final String deviceFinal = device;
                    Runnable run = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String mgmtIp = ToolUtils.getStr(ethPort.get("mgmtIp"));
                                if (!StringUtils.isEmpty(mgmtIp)) {
                                    try {
                                        ManagedMethodExecuter.SoapArgument soapArgument0
                                            = new ManagedMethodExecuter.SoapArgument();
                                        soapArgument0.setName(HOST_FIELD);
                                        soapArgument0.setVal(
                                            "<host xmlns:xsi=\"http:// www.w3.org/2001/XMLSchema-instance\" "
                                                + "xmlns:xsd=\"http:// www.w3.org/2001/XMLSchema\" "
                                                + "xmlns=\"urn:vim25\">" + mgmtIp + "</host>");
                                        List<ManagedMethodExecuter.SoapArgument> soapArgumentList = new ArrayList<>();
                                        soapArgumentList.add(soapArgument0);

                                        if (!StringUtils.isEmpty(deviceFinal)) {
                                            ManagedMethodExecuter.SoapArgument soapArgument1
                                                = new ManagedMethodExecuter.SoapArgument();
                                            soapArgument1.setName("interface");
                                            soapArgument1.setVal(
                                                "<interface xmlns:xsi=\"http:// www.w3.org/2001/XMLSchema-instance\" "
                                                    + "xmlns:xsd=\"http:// www.w3.org/2001/XMLSchema\""
                                                    + " xmlns=\"urn:vim25\">" + deviceFinal + "</interface>");
                                            soapArgumentList.add(soapArgument1);
                                        }

                                        ManagedMethodExecuter.SoapResult soapResult = methodExecuter.executeSoap(moid,
                                            "urn:vim25/6.5", "vim.EsxCLI.network.diag.ping",
                                            soapArgumentList.toArray(new ManagedMethodExecuter.SoapArgument[0]));

                                        String re = new String(soapResult.getResponse().getBytes("ISO-8859-1"),
                                            "UTF-8");
                                        logger.info("mgmtIp={},re={}", mgmtIp, re);
                                        String packetLost = xmlFormat(re);
                                        if (!StringUtils.isEmpty(packetLost) && !"100".equals(packetLost)) {
                                            ethPort.put(CONNECT_STATUS, "true");
                                        } else {
                                            ethPort.put(CONNECT_STATUS, "false");
                                        }
                                    } catch (Exception e) {
                                        ethPort.put(CONNECT_STATUS, "false");
                                        logger.error("testConnectivity error!mgmtIp={},errMsg={}", mgmtIp,
                                            e.getMessage());
                                    }
                                    reEthPorts.add(ethPort);
                                }
                            } finally {
                                latch.countDown();
                            }
                        }
                    };
                    taskExecutor.execute(run);
                }
                try {
                    // 等待所有线程执行完毕
                    latch.await();
                } catch (InterruptedException e) {
                    logger.error("latch.await error:{}", e.getMessage());
                }

                // 关闭线程池
                taskExecutor.shutdown();

                if (reEthPorts.size() > 0) {
                    reStr = gson.toJson(reEthPorts);
                }
            }
        } catch (Exception ex) {
            logger.error("error:", ex);
        } finally {
            if (sessionManager != null) {
                sessionManager.logout();
            }
            if (vmomiClient != null) {
                vmomiClient.shutdown();
            }
        }

        return reStr;
    }

    public String xmlFormat(String unformattedXml) {
        String packetLost = null;
        try {
            final Document document = parseXmlFile(unformattedXml);
            NodeList sms = document.getElementsByTagName("Summary");
            Element sm = (Element) sms.item(0);
            packetLost = sm.getElementsByTagName("PacketLost").item(0).getFirstChild().getNodeValue();
        } catch (Exception e) {
            logger.error("xmlFormat error:{}", e.toString());
        }
        return packetLost;
    }

    private Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断数据存储中是否有注册的虚拟机，有则返回true，没有返回false
     *
     * @param objectid 数据存储的objectid
     * @return boolean boolean
     */
    public boolean hasVmOnDatastore(String objectid) {
        String serverguid = vcConnectionHelpers.objectId2Serverguid(objectid);
        try {
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            DatastoreMO ds1 = datastoreVmwareMoFactory.build(vmwareContext, vcConnectionHelpers.objectId2Mor(objectid));
            List<ManagedObjectReference> vms = ds1.getVm();
            if (vms != null && vms.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error("hasVmOnDatastore error:{}", e.getMessage());
        }
        return false;
    }

    // 通过hostObjId获取名称
    public String getHostName(String hostObjId) {
        String name = null;
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(hostObjId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(hostObjId);
            HostMO hostMo = hostVmwareFactory.build(vmwareContext, objmor);
            if (hostMo != null) {
                name = hostMo.getName();
            }
        } catch (Exception e) {
            logger.error("query hostName by hostObjId error!hostObjId={},error:{}", hostObjId, e.getMessage());
        }
        return name;
    }

    public String getClusterName(String clusterObjId) {
        String name = null;
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(clusterObjId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(clusterObjId);
            ClusterMO clusterMo = clusterVmwareMoFactory.build(vmwareContext, objmor);
            if (clusterMo != null) {
                name = clusterMo.getName();
            }
        } catch (Exception e) {
            logger.error("query clusterName on datastore error:", e);
        }
        return name;
    }

    public String getDataStoreName(String dsObjId) {
        String name = null;
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(dsObjId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(dsObjId);
            DatastoreMO dataStoreMo = datastoreVmwareMoFactory.build(vmwareContext, objmor);

            if (null != dataStoreMo) {
                name = dataStoreMo.getName();
            }
        } catch (Exception e) {
            logger.error("query clusterName on datastore error:", e);
        }
        return name;
    }

    /**
     * 刷新datastore容量
     *
     * @param objectid 存储objectid
     */
    public void refreshDatastore(String objectid) {
        String serverguid = vcConnectionHelpers.objectId2Serverguid(objectid);
        try {
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            DatastoreMO ds1 = datastoreVmwareMoFactory.build(vmwareContext, vcConnectionHelpers.objectId2Mor(objectid));
            ds1.refreshDatastore();
        } catch (Exception e) {
            logger.error("query vms on datastore error:", e);
        }
    }

    /**
     * 刷新vmfs相关的volume容量
     *
     * @param objectid 主机objectid
     */
    public void refreshStorageSystem(String objectid) {
        String serverguid = vcConnectionHelpers.objectId2Serverguid(objectid);
        try {
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            HostStorageSystemMO ds1 = new HostStorageSystemMO(vmwareContext,
                vcConnectionHelpers.objectId2Mor(objectid));
            ds1.refreshStorageSystem();
        } catch (Exception e) {
            logger.error("query vms on datastore error:", e);
        }
    }

    // 通过vmObjId获取所属主机信息
    public Map<String, String> getHostByVmObjectId(String vmObjectId) {
        Map<String, String> map = null;
        try {
            String serverguid = vcConnectionHelpers.objectId2Serverguid(vmObjectId);
            VmwareContext vmwareContext = vcConnectionHelpers.getServerContext(serverguid);
            ManagedObjectReference objmor = vcConnectionHelpers.objectId2Mor(vmObjectId);
            VirtualMachineMO virtualMachineMo = virtualMachineMoFactorys.build(vmwareContext, objmor);
            if (virtualMachineMo != null) {
                map = new HashMap<>();
                HostMO hostMo = virtualMachineMo.getRunningHost();
                String objectId = vcConnectionHelpers.mor2ObjectId(hostMo.getMor(), vmwareContext.getServerAddress());
                map.put(HOST_ID, objectId);
                map.put(HOST_NAME, hostMo.getName());
            }
        } catch (Exception e) {
            logger.error("query host by vmObjectId:{},error:{}", vmObjectId, e.getMessage());
        }
        return map;
    }
}
