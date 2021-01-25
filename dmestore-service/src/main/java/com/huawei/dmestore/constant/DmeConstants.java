package com.huawei.dmestore.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * DmeConstants
 *
 * @author wangxy
 * @since 2020-09-15
 **/
public class DmeConstants {
    /**
     * nfs share detail
     **/
    public static final String DME_NFS_SHARE_DETAIL_URL = "/rest/fileservice/v1/nfs-shares/{nfs_share_id}";

    /**
     * 旧URL ："/rest/fileservice/v1/nfs-shares/{nfs_share_id}/auth_clients"
     **/
    public static final String DME_NFS_SHARE_AUTH_CLIENTS_URL = "/rest/fileservice/v1/nfs-auth-clients/query";

    /**
     * 旧URL ："/rest/fileservice/v1/nfs-shares/{nfs_share_id}/auth_clients" old dme
     **/
    public static final String DME_NFS_SHARE_AUTH_CLIENTS_URL_OLD
        = "/rest/fileservice/v1/nfs-shares/{nfs_share_id}/auth_clients";

    /**
     * 旧URL ："/rest/fileservice/v1/nfs-shares/summary"
     **/
    public static final String DME_NFS_SHARE_URL = "/rest/fileservice/v1/nfs-shares/query";

    /**
     * 旧URL ："/rest/fileservice/v1/nfs-shares/summary"
     **/
    public static final String DME_NFS_SHARE_URL_OLD = "/rest/fileservice/v1/nfs-shares/summary";

    /**
     * nfs share delete
     **/
    public static final String DME_NFS_SHARE_DELETE_URL = "/rest/fileservice/v1/nfs-shares/delete";

    /**
     * 新版本参数有变更，已修正
     **/
    public static final String API_NFSSHARE_CREATE = "/rest/fileservice/v1/nfs-shares";

    /**
     * fs query
     **/
    public static final String DME_NFS_FILESERVICE_QUERY_URL = "/rest/fileservice/v1/filesystems/query";

    /**
     * fs detail
     **/
    public static final String DME_NFS_FILESERVICE_DETAIL_URL = "/rest/fileservice/v1/filesystems/{file_system_id}";

    /**
     * delete fs
     **/
    public static final String DME_NFS_FS_DELETE_URL = "/rest/fileservice/v1/filesystems/delete";

    /**
     * create fs with custom
     * old url:/rest/fileservice/v1/filesystems/customize
     **/
    public static final String API_FS_CREATE = "/rest/fileservice/v1/filesystems/customize-filesystems";

    /**
     * create fs with custom. old DME
     * old url:/rest/fileservice/v1/filesystems/customize
     **/
    public static final String API_FS_CREATE_OLD = "/rest/fileservice/v1/filesystems/customize";

    /**
     * logic port detail
     **/
    public static final String DME_NFS_LOGICPORT_DETAIL_URL = "/rest/storagemgmt/v1/logic-ports/{logic_port_id}";

    /**
     * failover groups query
     **/
    public static final String API_FAILOVERGROUPS = "/rest/storagemgmt/v1/storage-port/failover-groups?storage_id=";

    /**
     * logic ports query
     **/
    public static final String API_LOGICPORTS_LIST = "/rest/storagemgmt/v1/logic-ports/query";

    /**
     * logic ports query.old dme
     **/
    public static final String API_LOGICPORTS_LIST_OLD = "/rest/storagemgmt/v1/storage-port/logic-ports";

    /**
     * bond ports query
     **/
    public static final String API_BANDPORTS_LIST = "/rest/storagemgmt/v1/storage-port/bond-ports?storage_id=";

    /**
     * volume base url
     **/
    public static final String DME_VOLUME_BASE_URL = "/rest/blockservice/v1/volumes";

    /**
     * query one volume to verify connectivity
     **/
    public static final String REFRES_STATE_URL = "/rest/blockservice/v1/volumes?limit=1";

    /**
     * volume delete
     **/
    public static final String DME_VOLUME_DELETE_URL = "/rest/blockservice/v1/volumes/delete";

    /**
     * 旧URL /rest/blockservice/v1/volumes/customize-volumes
     * 新的参数结构和参数有变更，已修改为最新匹配
     **/
    public static final String DME_CREATE_VOLUME_UNLEVEL_URL = "/rest/blockservice/v1/volumes/customize";

    /**
     * query volume by service level
     **/
    public static final String QUERY_SERVICE_LEVEL_VOLUME_URL
        = "/rest/blockservice/v1/volumes?service_level_id={serviceLevelId}";

    /**
     * map the disk to the host
     **/
    public static final String DME_HOST_MAPPING_URL = "/rest/blockservice/v1/volumes/host-mapping";

    /**
     * unmap the disk from the host
     **/
    public static final String DME_HOST_UNMAPPING_URL = "/rest/blockservice/v1/volumes/host-unmapping";

    /**
     * hostgroup unmapping
     **/
    public static final String HOSTGROUP_UNMAPPING = "/rest/blockservice/v1/volumes/hostgroup-unmapping";

    /**
     * hostgroup mapping
     **/
    public static final String MOUNT_VOLUME_TO_HOSTGROUP_URL = "/rest/blockservice/v1/volumes/hostgroup-mapping";

    /**
     * volume expand
     **/
    public static final String API_VMFS_EXPAND = "/rest/blockservice/v1/volumes/expand";

    /**
     * update service level
     **/
    public static final String API_SERVICELEVEL_UPDATE = "/rest/blockservice/v1/volumes/update-service-level";

    /**
     * task query
     **/
    public static final String DME_TASK_BASE_URL = "/rest/taskmgmt/v1/tasks";

    /**
     * task detail query
     **/
    public static final String QUERY_TASK_URL = "/rest/taskmgmt/v1/tasks/{task_id}";

    /**
     * instance query by classname
     **/
    public static final String DME_RESOURCE_INSTANCE_LIST = "/rest/resourcedb/v1/instances/%s";

    /**
     * instance detail query
     **/
    public static final String QUERY_INSTANCE_URL = "/rest/resourcedb/v1/instances/{className}/{instanceId}";

    /**
     * instance query limit 1000
     **/
    public static final String LIST_INSTANCE_URL = "/rest/resourcedb/v1/instances/{className}?pageSize=1000";

    /**
     * storage base url
     **/
    public static final String API_STORAGES = "/rest/storagemgmt/v1/storages";

    /**
     * storage detail query
     **/
    public static final String DME_STORAGE_DETAIL_URL = "/rest/storagemgmt/v1/storages/{storage_id}/detail";

    /**
     * workload query
     **/
    public static final String GET_WORKLOADS_URL = "/rest/storagemgmt/v1/storages/{storage_id}/workloads";

    /**
     * host query
     **/
    public static final String CREATE_DME_HOST_URL = "/rest/hostmgmt/v1/hosts";

    /**
     * host summary query
     **/
    public static final String DME_HOST_SUMMARY_URL = "/rest/hostmgmt/v1/hosts/summary";

    /**
     * host summary query
     **/
    public static final String GET_DME_HOST_URL = "/rest/hostmgmt/v1/hosts/{host_id}/summary";

    /**
     * host initiator query
     **/
    public static final String GET_DME_HOSTS_INITIATORS_URL = "/rest/hostmgmt/v1/hosts/{host_id}/initiators";

    /**
     * history data query
     **/
    public static final String STATISTIC_QUERY = "/rest/metrics/v1/data-svc/history-data/action/query";

    /**
     * authentication
     **/
    public static final String LOGIN_DME_URL = "/rest/plat/smapp/v1/sessions";

    /**
     * hostgroup query url
     **/
    public static final String CREATE_DME_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups";

    /**
     * hostgroup summary query url
     **/
    public static final String GET_DME_HOSTGROUPS_URL = "/rest/hostmgmt/v1/hostgroups/summary";

    /**
     * one hostgroup summary query url
     **/
    public static final String GET_DME_HOSTGROUP_URL = "/rest/hostmgmt/v1/hostgroups/{hostgroup_id}/summary";

    /**
     * hosts on hostgroup query url
     **/
    public static final String GET_DME_HOSTS_IN_HOSTGROUP_URL
        = "/rest/hostmgmt/v1/hostgroups/{hostgroup_id}/hosts/list";

    /**
     * instance relation query
     **/
    public static final String LIST_RELATION_URL = "/rest/resourcedb/v1/relations/{relationName}/instances";

    /**
     * instance relation query
     **/
    public static final String QUERY_RELATION_URL
        = "/rest/resourcedb/v1/relations/{relationName}/instances/{instanceId}";

    /**
     * 旧版本url : /rest/fileservice/v1/dtrees/summary
     */
    public static final String API_DTREES_LIST = "/rest/fileservice/v1/dtrees/query";

    /**
     * 旧版本url : /rest/fileservice/v1/dtrees/summary
     */
    public static final String API_DTREES_LIST_OLD = "/rest/fileservice/v1/dtrees/summary";

    /**
     * service level query
     **/
    public static final String LIST_SERVICE_LEVEL_URL = "/rest/service-policy/v1/service-levels";

    /**
     * datasets query
     **/
    public static final String DATASETS_QUERY_URL = "/rest/metrics/v1/datasets/{dataSet}?pageSize=5000";

    /**
     * Constant definition
     */
    public static final int MAXLEN = 255;

    /**
     * http prefix
     **/
    public static final String HTTP = "http";

    /**
     * hostIp lable
     **/
    public static final String HOSTIP = "hostIp";

    /**
     * token acquisition tag
     **/
    public static final String ACCESSSESSION = "accessSession";

    /**
     * datas field
     **/
    public static final String DATAS = "datas";

    /**
     * hosts field
     **/
    public static final String HOSTS = "hosts";

    /**
     * initiators field
     **/
    public static final String INITIATORS = "initiators";

    /**
     * hostgroups field
     **/
    public static final String HOSTGROUPS = "hostgroups";

    /**
     * host field
     **/
    public static final String HOST = "host";

    /**
     * id field
     **/
    public static final String ID = "id";

    /**
     * cluster field
     **/
    public static final String CLUSTER = "cluster";

    /**
     * hostids field
     **/
    public static final String HOSTIDS = "hostids";

    /**
     * service_level_id field
     **/
    public static final String SERVICELEVELID = "service_level_id";

    /**
     * task_id field
     **/
    public static final String TASKID = "task_id";

    /**
     * storage_id field
     **/
    public static final String STORAGEID = "storage_id";

    /**
     * host_id field
     **/
    public static final String HOSTID = "host_id";

    /**
     * volume_id field
     **/
    public static final String VOLUMEID = "volume_id";

    /**
     * volumeIds field
     **/
    public static final String VOLUMEIDS = "volumeIds";

    /**
     * control_policy field
     **/
    public static final String CONTROLPOLICY = "control_policy";

    /**
     * alloctype field
     **/
    public static final String ALLOCTYPE = "alloctype";

    /**
     * volumes field
     **/
    public static final String VOLUMES = "volumes";

    /**
     * dataStoreObjectIds field
     **/
    public static final String DATASTOREOBJECTIDS = "dataStoreObjectIds";

    /**
     * dataStoreNames field
     **/
    public static final String DATASTORENAMES = "dataStoreNames";

    /**
     * ethPorts field
     **/
    public static final String ETHPORTS = "ethPorts";

    /**
     * vmKernel field
     **/
    public static final String VMKERNEL = "vmKernel";

    /**
     * hostObjectId field
     **/
    public static final String HOSTOBJECTID = "hostObjectId";

    /**
     * status field
     **/
    public static final String TASK_DETAIL_STATUS_FILE = "status";

    /**
     * digit 3
     **/
    public static final int TASK_SUCCESS = 3;

    /**
     * digit 100
     **/
    public static final int HTTPS_STATUS_CHECK_FLAG = 100;

    /**
     * https success code prefix 2
     **/
    public static final int HTTPS_STATUS_SUCCESS_PRE = 2;

    /**
     * collections default length
     **/
    public static final int COLLECTION_CAPACITY_16 = 16;

    /**
     * error code 503
     **/
    public static final String ERROR_CODE_503 = "503";

    /**
     * https status success
     **/
    public static final int HTTPS_STATUS_SUCCESS_200 = 200;

    /**
     * VMFS type
     **/
    public static final String STORE_TYPE_VMFS = "VMFS";

    /**
     * default_page_size
     **/
    public static final int DEFAULT_PAGE_SIZE = 1000;

    /**
     * 容量初始分配策略。仅支持华为V3/V5设备，Dorado系列不支持该参数。
     * 取值范围：automatic：自动，highest_performance：高性能层，performance：性能层，capacity：容量层。默认值：automatic
     **/
    public static final Map<String, String> INITIAL_DISTRIBUTE_POLICY = new HashMap() {
        {
            put("0", "automatic");
            put("1", "highest_performance");
            put("2", "performance");
            put("3", "capacity");
        }
    };

    /**
     * 预取策略，影响磁盘读取。
     * 取值范围：no_prefetch: 不预取，constant_prefetch：固定预取，variable_prefetch：可变预取，intelligent_prefetch：智能预取。
     * 默认值：intelligent_prefetch
     **/
    public static final Map<String, String> PREFETCH_POLICY = new HashMap() {
        {
            put("0", "no_prefetch");
            put("1", "constant_prefetch");
            put("2", "variable_prefetch");
            put("3", "intelligent_prefetch");
        }
    };

    /**
     * 数据迁移策略。
     * no_migration：不迁移，automatic_migration：自动迁移，migration_to_higher：向高性能层迁移，migration_to_lower：向低性能层迁移
     * 默认值：no_migration。
     **/
    public static final Map<String, String> SMART_TIER = new HashMap() {
        {
            put("0", "no_migration");
            put("1", "automatic_migration");
            put("2", "migration_to_higher");
            put("3", "migration_to_lower");
        }
    };

    /**
     * RAID级别。
     **/
    public static final Map<String, String> RAID_LEVEL_MAP = new HashMap() {
        {
            put("1", "RAID10");
            put("2", "RAID5");
            put("3", "RAID0");
            put("4", "RAID1");
            put("5", "RAID6");
            put("6", "RAID50");
            put("7", "RAID3");
            put("11", "RAIDTP");
        }
    };

    private DmeConstants() {
    }
}
