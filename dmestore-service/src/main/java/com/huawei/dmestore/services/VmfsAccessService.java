package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.VmfsDataInfo;
import com.huawei.dmestore.model.VmfsDatastoreVolumeDetail;

import java.util.List;
import java.util.Map;

/**
 * VmfsAccessService
 *
 * @author lianqiang
 * @since 2020-09-15
 **/
public interface VmfsAccessService {
    /**
     * List vmfs
     *
     * @return List
     * @throws DmeException when error
     */
    List<VmfsDataInfo> listVmfs() throws DmeException;

    /**
     * List vmfs Performance
     *
     * @param wwns wwn
     * @return List
     * @throws DmeException when error
     */
    List<VmfsDataInfo> listVmfsPerformance(List<String> wwns) throws DmeException;

    /**
     * Create vmfs include:
     * ServiceVolumeBasicParams对象包含如下属性
     * param str name: 名称 必 vmfs stor的名称
     * param str volumeName: 名称 必 卷的名称
     * param int capacity: 容量，单位GB 必
     * param int count: 数量 必
     * param str service_level_id: 服务等级id 若未选择服务等级，可选择存储设备、存储池、设置QoS、Thin、Workload
     * param str service_level_name; 服务等级名称
     * param int version: 版本
     * param int blockSize: 块大小，单位KB
     * param int spaceReclamationGranularity   空间回收粒度 单位K
     * param str spaceReclamationPriority: 空间回收优先权
     * param str host: 主机  必  与cluster二选其一,不可同时存在
     * param str hostId: 主机
     * param str cluster: 集群 必 与host二选其一,不可同时存在
     * param str clusterId: 集群
     * param str storage_id 存储设备id
     * param str pool_raw_id 卷所属存储池在存储设备上的id
     * param integer workload_type_id 应用类型id
     * param str alloctype 卷分配类型，取值范围 thin，thick
     * 卷qos属性
     * param str  control_policy 控制策略
     * param integer  latency 时延，单位ms
     * param integer  maxbandwidth 最大带宽
     * param integer  maxiops 最大iops
     * param integer  minbandwidth 最小带宽
     * param integer  miniops 最小iops
     * param str qosname Smart QoS名称
     *
     * @param params include Parameters above
     * @throws DmeException when error
     */
    void createVmfs(Map<String, Object> params) throws DmeException;

    /**
     * Mount vmfs include
     * param list dataStoreObjectIds: datastore object id列表 必
     * param str host: 主机名称 必 （主机与集群二选一）
     * param str hostId: 主机
     * param str cluster: 集群名称 必（主机与集群二选一）
     * param str clusterId: 集群
     *
     * @param params include dataStoreObjectIds,host,hostId,cluster,clusterId
     * @throws DmeException when error
     */
    void mountVmfs(Map<String, Object> params) throws DmeException;

    /**
     * unmount Vmfs
     *
     * @param params include dataStoreObjectIds,host,hostId,cluster,clusterId
     * @throws DmeException when error
     */
    void unmountVmfs(Map<String, Object> params) throws DmeException;

    /**
     * delete vmfs
     *
     * @param params include dataStoreObjectIds,host,hostId,cluster,clusterId
     * @throws DmeException when error
     */
    void deleteVmfs(Map<String, Object> params) throws DmeException;

    /**
     * vCenter VMFS存储卷详细信息查询
     *
     * @param storageObjectId VMFS存储ID
     * @return java.util.List com.dmeplugin.dmestore.model.VmfsDatastoreVolumeDetail
     * @throws DmeException always
     **/
    List<VmfsDatastoreVolumeDetail> volumeDetail(String storageObjectId) throws DmeException;

    /**
     * vCenter VMFS存储扫描发现
     *
     * @return boolean
     * @throws DmeException always
     **/
    boolean scanVmfs() throws DmeException;

    /**
     * 通过vmfs storageId查询VC的主机 (DME侧关联的主机的启动器和VC主机的启动器要一致)
     *
     * @param storageId storageId
     * @return List 返回VC主机列表，单个主机的信息以map方式存储属性和属性值
     * @throws DmeException DmeException
     */
    List<Map<String, Object>> getHostsByStorageId(String storageId) throws DmeException;

    /**
     * 通过vmfs storageId查询vc 集群信息 （DME侧关联的主机组信息下所有主机的启动器和集群下的主机的启动器一致）
     *
     * @param storageId storageId
     * @return 返回集群列表，单个集群的信息以map方式存储属性和属性值
     * @throws DmeException DmeException
     */
    List<Map<String, Object>> getHostGroupsByStorageId(String storageId) throws DmeException;

    /**
     * query vmfs
     *
     * @param dataStoreObjectId dataStoreObjectId
     * @return List VmfsDataInfo
     * @throws Exception when error
     */
    List<VmfsDataInfo> queryVmfs(String dataStoreObjectId) throws Exception;

    /**
     * 根据vmfs名字查询指定vmfs
     *
     * @param name name
     * @return Boolean
     */
    boolean queryDatastoreByName(String name);

    /**
     * DME侧主机检查
     *
     * @param hostIp hostIp
     * @param hostId hostId
     * @return java.lang.String
     * @throws DmeException DmeException
     **/
    String checkOrCreateToHost(String hostIp, String hostId) throws DmeException;
}
