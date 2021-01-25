import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {CapacityChart, LogicPort} from "../storage.service";

@Injectable()
export class DetailService {
  constructor(private http: HttpClient) {}

  getPoolList(params = {}){
    return this.http.get('dmestorage/storages', { params });
  }

  getStorageDetail(storageId: string){
    return this.http.get('dmestorage/storage', {params: {storageId}});
  }

  getStoragePoolList(storageId: string){
    return this.http.get('dmestorage/storagepools', {params: {storageId}});
  }
  liststoragepoolperformance(storagePoolIds:string[]) {
    return this.http.get('dmestorage/liststoragepoolperformance', {params: {storagePoolIds}});
  }
  listVolumesperformance(volumeId:string[]) {
    return this.http.get('dmestorage/listVolumesPerformance', {params: {volumeId}});
  }
  getVolumeListList(storageId: string){
    return this.http.get('dmestorage/volumes', {params: {storageId}});
  }
  getVolumeListListByPage(params:any){
    return this.http.get('dmestorage/volumes/byPage?storageId='+params.storageId+'&pageSize='+params.pageSize+'&pageNo='+params.pageNo);
  }
  getFileSystemList(storageId: string){
    return this.http.get('dmestorage/filesystems', {params: {storageId}});
  }
  getDtreeList(storageId: string){
    return this.http.get('dmestorage/dtrees', {params: {storageId}});
  }
  getShareList(storageId: string){
    return this.http.get('dmestorage/nfsshares', {params: {storageId}});
  }
  getControllerList(storageDeviceId: string){
    return this.http.get('dmestorage/storagecontrollers', {params: {storageDeviceId}});
  }
  getDiskList(storageDeviceId: string){
    return this.http.get('dmestorage/storagedisks', {params: {storageDeviceId}});
  }
  getFCPortList(param: any){
    return this.http.get('dmestorage/storageport', {params: param});
  }
  getBondPortList(storageId: string){
    return this.http.get('dmestorage/bandports', {params: {storageId}});
  }
  getLogicPortList(storageId: string){
    return this.http.get('dmestorage/logicports', {params: {storageId}});
  }
  getFailoverGroups(storageId: string){
    return this.http.get('dmestorage/failovergroups', {params: {storageId}});
  }
}

export class StorageDetail{
  azIds: string[];
  bandPorts: string;
  blockCapacity: number;
  compressedCapacity: number;
  dTrees:string;
  dedupedCapacity: number;
  event: number;
  fileCapacity: number;
  fileSystem: string;
  freeEffectiveCapacity: number;
  id: string;
  ip: string;
  location: string;
  logicPorts: string;
  maintenanceOvertime: string;
  maintenanceStart: string;
  model: string;
  name: string;
  nfsShares: string;
  optimizeCapacity: number;
  patchVersion: string;
  productVersion: string;
  protectionCapacity: number;
  sn: string;
  status: string;
  storageControllers: string;
  storageDisks: string;
  storagePool:string;
  subscriptionCapacity: number;
  synStatus: string;
  totalCapacity: number;
  totalEffectiveCapacity: number;
  usedCapacity: number;
  vendor: string;
  volume: string;
  warning: number;
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

export class StoragePool{
  freeCapacity: number;// 空闲容量
  name: string;// 名称
  id: string;// id
  runningStatus: string;// 运行状态
  healthStatus: string;// 健康状态
  totalCapacity: number;// 总容量
  consumedCapacity: number;//已用容量
  consumedCapacityPercentage: string;// 已用容量百分比(容量利用率)
  storagePoolId: string;
  storageInstanceId: string;
  storageDeviceId: string;
  subscriptionRate: number; //订阅率
  //补充字段
  mediaType: string;//类型（块）
  mediaTypeDesc: string;//类型（块）
  tier0RaidLv: string; // RAID级别
  tier1RaidLv: string; // RAID级别
  tier1RaidLvDesc: string; // RAID级别描述
  tier2RaidLv: string; // RAID级别
  tier2RaidLvDesc: string; // RAID级别描述
  raidLevel: string; // RAID级别描述
  storageId: string; // 存储设备id
  dataSpace: number; // 存储池上创建LUN或者文件系统时的可用容量 单位MB
  subscribedCapacity: number; //订阅容量
  physicalType: string;//硬盘类型
  physicalTypeDesc: string;//硬盘类型
  diskPoolId:string;//存储池所处硬盘id
  poolId:string;
  maxBandwidth: number;
  maxIops: number;
  maxLatency: number;
  serviceLevelName: string;//服务等级
}

export interface StoragePoolMap {
  storageId:string;
  storagePoolList:StoragePool[];
  logicPort:LogicPort[];
}

export class Volume{
  id: string; //卷的唯一标识
  name: string; //名称
  status: string; //状态
  attached: boolean;// 映射状态
  alloctype: string;//分配类型
  serviceLevelName: string;//服务等级
  storageId: string;//存储设备id
  poolRawId: string;//存储池id
  capacityUsage: string;//容量利用率
  protectionStatus: boolean;//保护状态
  hostIds: string[];
  hostGroupIds: string;
  storagePoolName: string;//存储池名称
  capacity: number;//总容量 单位GB
  //关联的datastore
  datastores: string;
  bandwith: number;
  iops: number;
  lantency: number;
  wwn:string;
}
export class Dtrees{
  name: string;
  fsName: string; //所属文件系统名称
  quotaSwitch: boolean; //配额
  securityStyle: string;//安全模式
  tierName: string;//服务等级名称
  nfsCount: number;//nfs
  cifsCount: number;
}
export class NfsShare {
  name: string; //名称
  sharePath: string; //共享路径
  storageId: string; //存储设备id
  tierName: string; //服务等级
  owningDtreeName: string;//所属dtree
  fsName: string; //所属文件系统名字在
  owningDtreeId: string; //所属dtreeid
}
export class StorageDisk{
  capacity: number;
  diskDomain: string;
  logicalType:string;
  name: string;
  performance: string;
  physicalType: string;
  poolId: string;
  speed: number
  status: string;
  storageDeviceId: string;
  iops:number;
  useage:number;
  bandwith:number;
  lantency:number;
}
export interface PoolList {
   name: string;
   status: string;
  diskType: string;
  type: string;
  serviceLevel: string;
  raidLevel: string;
}
export class CapacityDistribution{
  protection: string;
  fileSystem: string;
  volume: string;
  freeCapacity: string;
  chart: CapacityChart;
}
export class StorageController{
  name:string;
  status:string;
  softVer:string;
  cpuInfo:string;
  cpuUsage:number;
  iops:number;
  ops:number;
  bandwith:number;
  lantency:number;
}
export class CapacitySavings{
  bars:number[];
  unit:string;
  beforeSave:number;
  dedupe:number;
  compression:number;
  afterSave:number;
  max:number;
  rate:string;
  constructor(){
    this.bars=[];
  }
}

