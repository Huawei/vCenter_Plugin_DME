import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import construct = Reflect.construct;

@Injectable()
export class VmfsListService {
  constructor(private http: HttpClient) {}

  // 主列表数据
  getData() {
    return this.http.get('accessvmfs/listvmfs');
  }
  // 附列表数据
  getChartData(wwns: string[] ) {
    return this.http.get('accessvmfs/listvmfsperformance', {params: {wwns}});
  }
  // 获取存储
  getStorages() {
    return this.http.get('dmestorage/storages');
  }

  getStorageDetail(storageId: string){
    return this.http.get('dmestorage/storage', {params: {storageId}});
  }

  // 通过存储ID获取存储池数据 (vmfs添加mediaType为block)
  getStoragePoolsByStorId(storageId: string, mediaType: string) {
    return this.http.get('dmestorage/storagepools?storageId='+ storageId + '&mediaType=' + mediaType);
  }

  // 获取所有的主机
  getHostList() {
    return this.http.get('accessvmware/listhost');
  }

  // 获取所有的集群
  getClusterList() {
    return this.http.get('accessvmware/listcluster');
  }

  // 通过名称获取主机
  getHostListByObjectId(objectId: string){
    return this.http.get('accessvmware/gethostsbydsobjectid?dataStoreObjectId=' + objectId);
  }
  // 通过名称获取集群
  getClusterListByObjectId(objectId: string){
    return this.http.get('accessvmware/getclustersbydsobjectid?dataStoreObjectId=' + objectId);
  }


  // 查询挂载的主机
  getMountHostOrCluSter(objectId: string) {
    return this.http.get('accessvmware/getclustersbydsobjectid?dataStoreObjectId=' + objectId);
  }

  // 获取所有的服务等级数据
  getServiceLevelList(params = {}) {
    return this.http.post('servicelevel/listservicelevel', params);
  }

  // 创建vmfs
  createVmfs(params = {}) {
     return  this.http.post('accessvmfs/createvmfs', params);
  }

  // 修改VMFS
  updateVmfs(volumeId: string, params = {}) {
    return  this.http.put('operatevmfs/updatevmfs?volumeId=' + volumeId, params);
  }
  // 删除
  delVmfs(params = {}) {
    return  this.http.post('accessvmfs/deletevmfs', params);
  }
  // 获取已挂载主机
  getMountHost(objectId) {
    return  this.http.get('accessvmfs/gethostsbystorageid/'+objectId);
  }
  // 获取已挂载集群
  getMountCluster(objectId) {
    return  this.http.get('accessvmfs/gethostgroupsbystorageid/'+objectId);
  }
  // 卸载
  unmountVMFS(params = {}) {
    return  this.http.post('accessvmfs/ummountvmfs', params);
  }
  // 挂载
  mountVmfs(params = {}) {
    return  this.http.post('accessvmfs/mountvmfs', params);
  }
  // 空间回收
  reclaimVmfs(params = {}) { // vmfs空间回收
    return  this.http.post('operatevmfs/recyclevmfsbydatastoreids', params);
  }

  // 空间回收判断 true 可以回收 false 不可以回收
  reclaimVmfsJudge(params = {}) { // vmfs空间回收
    return  this.http.post('operatevmfs/canrecyclevmfsbydatastoreid', params);
  }

  // 修改服务等级
  changeServiceLevel(params = {}) {
    return  this.http.post('operatevmfs/updatevmfsservicelevel', params);
  }

  // 扩容
  expandVMFS(params = {}) {
    return  this.http.post('operatevmfs/expandvmfs', params);
  }
  // 扫描任务
  scanVMFS(storageType: string) {
    return  this.http.get('accessdme/scandatastore', {params: {storageType}});
  }

  // 获取WorkLoads
  getWorkLoads(storageId: string) {
    return  this.http.get('accessdme/getworkloads', {params: {storageId}});
  }

  /**
   * 校验vmfs名称
   * @param name
   */
  checkVmfsName(name: string) {
    return this.http.get('accessvmfs/querydatastorebyname', {params: {name}});
  }

  /**
   * 校验卷名称
   * @param volName
   */
  checkVolName(volName: string) {
    return this.http.get('dmestorage/queryvolumebyname', {params: {name:volName}});
  }

  /**
   * 通过objectId 获取vmfs存储数据
   * @param objectId
   */
  getStorageById(objectId:string) {
    return this.http.get('accessvmware/relation?datastoreObjectId='+objectId);
  }
}
// vmfs列表
export interface VmfsInfo {
  objectid: string; // 跳转用唯一id
  name: string;
  status: string;
  capacity: number;
  freeSpace: number;
  reserveCapacity: number;
  deviceId: string;
  device: string;
  serviceLevelName: string;
  vmfsProtected: boolean;
  maxIops: number;
  minIops: number;
  iops: number;
  maxBandwidth: number;
  minBandwidth: number;
  bandwidth: number;
  readResponseTime: number;
  writeResponseTime: number;
  latency: number;
  volumeId: string;
  volumeName: string;
  wwn: string;
  usedCapacity: number; // 使用容量
  capacityUsage: number; // 利用率
}
// 存储
export interface StorageList {
  name: string;
  id: string;
  storageTypeShow:StorageTypeShow;
}
export interface StorageTypeShow {
  qosTag:number;// qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
  workLoadShow:number;// 1 支持应用类型 2不支持应用类型
  ownershipController:boolean;// 归属控制器 true 支持 false 不支持
  allocationTypeShow:number;// 资源分配类型  1 可选thin/thick 2 可选thin
  deduplicationShow:boolean;// 重复数据删除 true 支持 false 不支持
  compressionShow:boolean; // 数据压缩 true 支持 false 不支持
  capacityInitialAllocation:boolean;// 容量初始分配策略 true 支持 false 不支持
  smartTierShow:boolean;// SmartTier策略 true 支持 false 不支持
  prefetchStrategyShow:boolean;// 预取策略 true 支持 false 不支持
  storageDetailTag:number;// 存储详情下展示情况 1 仅展示存储池和lun 2 展示存储池/lun/文件系统/共享/dtree
}
// 存储池
export interface StoragePoolList {
  id:string;
  name: string;
  poolId: string;
  storage_id: string;
  storage_name: string;
}
export interface StoragePoolMap {
  storageId:string;
  storagePoolList:StoragePoolList[];
  workloadList:Workload[];
}

// 主机列表
export interface HostList {
  hostId: string;
  hostName: string;
}
// 集群列表
export interface ClusterList {
  clusterId: string;
  clusterName: string;
}
// 服务等级
export interface ServiceLevelList {
  id: string;
  name: string;
  freeCapacity: number;
  usedCapacity: number;
  totalCapacity: number;
  description: string;
  type: string;
  protocol: string;
  capabilities: {
    resourceType: string;
    compression: boolean;
    deduplication: boolean;
    smarttier: {
      enabled: boolean;
      policy: number;
    };
    qos: {
      enabled: boolean;
      "qosParam": {
        "enabled": boolean;
        "latency": number;
        "latencyUnit": string;
        "minBandWidth": number;
        "minIOPS": number;
        "maxBandWidth": number;
        "maxIOPS": number;
        "smartQos": string;
      },
      smartQos: {
        latency: string;
        latencyUnit: string;
        minBandWidth: number;
        minIOPS: number;
        maxBandWidth: number;
        maxIOPS: number;
        control_policy: string;
      };
    };
  };
  show: boolean;
}
export interface HostOrCluster {
  deviceId: string;
  deviceName: string;
  deviceType: string;
}
export interface Workload{
    id: string;
    name: string;
    type: string;
    block_size: string;
    create_type: string;
    enable_compress: string;
    enable_dedup: string;
}
export class GetForm {
  // 获取添加form表单（初始化的添加表单）
  getAddForm() {
    const addform = {
      name: null, // vmfs stor的名称 必
      volumeName: null, // 卷名称 必
      isSameName: true, // 卷名称与数据存储名称相同 true相同 false 不同
      capacity: null, // 容量，单位GB 必
      capacityUnit: 'GB', // 容量单位 （最后需转换为GB）
      count: null, // 数量 必
      service_level_id: null, // 服务等级id 若未选择服务等级，可选择存储设备、存储池、设置QoS、Thin、Workload
      service_level_name: null, // 服务等级名称  必
      version: '5', // 版本
      blockSize: '1024', // 块大小，单位KB
      spaceReclamationGranularity: '1024', // 空间回收粒度 单位K
      spaceReclamationPriority: 'low', // 空间回收优先权
      host: null, // 主机  必
      hostId: null, // 主机  必
      cluster: null, // 集群
      clusterId: null, // 集群
      storage_id: null, // 存储设备id
      pool_raw_id: null, // 卷所属存储池在存储设备上的id
      workload_type_id: null, // 应用类型id
      alloctype: 'thin', // 卷分配类型，取值范围 thin，thick
      control_policy: null, // 控制策略
      control_policyUpper: undefined, // 控制策略上限
      control_policyLower: undefined, // 控制策略下限
      qosFlag: false, // 控制策略
      smartTier: null, // SmartTier
      smartTierFlag: false, // SmartTier
      latencyChoose: false, // 时延 选中
      latency: null, // 时延，单位ms
      maxbandwidth: null, // 最大带宽
      maxbandwidthChoose: false, // 最大带宽 选中
      maxiops: null, // 最大iops
      maxiopsChoose: false, // 最大iops 选中
      minbandwidth: null, // 最小带宽
      minbandwidthChoose: false, // 最小带宽 选中
      miniops: null, // 最小iops
      miniopsChoose: false, // 最小iops 选中
      qosname: null, // Smart QoS名称
      deviceName: null,
      hostDataloadSuccess: false, // 主机数据是否加载完毕
      culDataloadSuccess: false // 集群数据是否加载完毕
    };
    return addform;
  }
  // 获取修改form表单（初始化的添加表单）
  getEditForm() {
    const editForm = {
      name: null,
      isSameName: true, // 卷名称与vmfs名称是否相同
      volumeId: null, // 卷ID
      control_policy: undefined, // 控制策略,
      control_policyUpper: undefined, // 控制策略上限
      control_policyLower: undefined, // 控制策略下限
      max_iops: null,
      maxiopsChoose: false, // 最大iops 选中
      max_bandwidth: null,
      maxbandwidthChoose: false, // 最大带宽 选中
      newVoName: null, // 新卷名称
      oldDsName: null, // 旧VMFS名称
      newDsName: null, // 新VMFS名称
      min_iops: null,
      miniopsChoose: false, // 最小iops 选中
      min_bandwidth: null,
      minbandwidthChoose: false, // 最小带宽 选中
      dataStoreObjectId: null, // objectID,
      service_level_name: null, // 服务等级名称
      latency: null,
      latencyChoose: false, // 时延 选中
      qosFlag: false,
      smartTier: null, // SmartTier
      smartTierFlag: false, // SmartTier
    };
    return editForm;
  }
  // 扩容form（初始化的添加表单）
  getExpandForm() {
    const expandForm = {
      vo_add_capacity: null, // 扩容大小默认GB
      capacityUnit: 'GB', // 容量单位 （最后需转换为GB）
      volume_id: '', // 卷ID
      ds_name: '', // vmfsName
      obj_id: '' // dataStoreObjectId
    };
    return expandForm;
  }
  // 服务等级变更
  getChangeLevelForm() {
    const changeLevelForm = {
      volume_ids: [], // 卷ID
      ds_name: '', // vmfsName
      service_level_id: '', // 服务等级id
      service_level_name: '', // 服务等级名称
    };
    return changeLevelForm;
  }
  // 挂载form
  getMountForm() {
    const mountForm = {
      hostId: null,
      host: null,
      clusterId: null,
      cluster: null,
      dataStoreObjectIds: [], // datastore object id列表 必,
      mountType: '1' // 挂载的设备类型 1 服务器、2 集群 前端自用参数
    };
    return mountForm;
  }
  getUnmountForm() {
    const unmount = {
      name: null,
      hostId: null,
      clusterId: null,
      dataStoreObjectIds: [],
      mountType: '1' // 挂载的设备类型 1 服务器、0 集群 前端自用参数
    };
    return unmount;
  }
}

