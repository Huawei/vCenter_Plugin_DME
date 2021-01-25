import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsAddService{
  constructor(private http: HttpClient) {}
  getHostList(){
    return this.http.get('accessvmware/listhost');
  }
  getStoragePoolListByStorageId(params={}){
    return this.http.get('dmestorage/storagepools', params);
  }
  getVmkernelListByObjectId(hostObjectId:string){
    return this.http.get('accessvmware/getvmkernelipbyhostobjectid',{params: {hostObjectId}} );
  }
  checkNfsNameExist(name:string){
    return this.http.get('accessvmfs/querydatastorebyname',{params: {name}} );
  }
  checkShareNameExist(name:string){
    return this.http.get('dmestorage/querysharebyname',{params: {name}} );
  }
  checkFsNameExist(name:string){
    return this.http.get('dmestorage/queryfsbyname',{params: {name}} );
  }
  addNfs(params= {}){
    return this.http.post('operatenfs/createnfsdatastore', params);
  }
}
// =================添加NFS参数 start=========
export class AddNfs{
  storagId:string;// 存储设备id
  storagePoolId:string;//  存储池id (storage_pool_id)
  poolRawId:string;//  存储池在指定存储设备上的id（poolId）
  currentPortId:string;//  逻辑端口id
  nfsName:string;//   DataStoname
  sameName:boolean;// false true 如果是false就传
  shareName:string;//  共享名称
  fsName:string;//  文件系统名称
  size: number; //  ?待确认默认单位（界面可选。前端可转换）
  type:string;//  nfs协议版本
  advance :boolean;//false true  true 是有高级选项
  qosFlag:boolean;// qos策略开关 false true false关闭
  contolPolicy :string;//  上下线选择标记  枚举值 up low
// up 取值如下
  maxBandwidth: number; //
  maxBandwidthChoose: false; //
  maxIops: number; //
  maxIopsChoose: false; //
//low取值
  minBandwidth: number; //
  minBandwidthChoose: false; //
  minIops: number; //
  minIopsChoose: false; //
  latency: number; //
  latencyChoose: false; //
  thin:boolean;// true  代表thin false代表thick
  deduplicationEnabled:boolean;// 重删 true false
  compressionEnabled:boolean;// 压缩 true false
  autoSizeEnable:boolean;// 自动扩容 true false
  vkernelIp:string;//  虚拟网卡ip
  hostObjectId:string;//  挂载主机的Objectid
  accessMode:string;//  挂载方式 分 只读 和读写
  securityType:string;//安全类型
  unit:string;// 单位
  constructor(){
    this.sameName = true;
    this.advance = false;
    this.qosFlag = false;
    this.deduplicationEnabled = false;
    this.compressionEnabled = false;
    this.autoSizeEnable = false;
    this.thin = true;
    this.unit = 'GB';
  }
}
export class StoragePool{
  freeCapacity: number;// 空闲容量
  name: string;// 名称
  id: string;// id
  runningStatus: string;// 运行状态
  healthStatus: string;// 健康状态
  totalCapacity: number;// 总容量
  consumedCapacity: number;//已用容量
  consumedCapacity_percentage: string;// 已用容量百分比(容量利用率)
  storagePoolId: string;
  storageInstanceId: string;
  storageDeviceId: string;
  subscriptionRate: number; //订阅率
  //补充字段
  mediaType: string;//类型（块）
  tier0RaidLv: string; // RAID级别
  tier1RaidLv: string; // RAID级别
  tier2RaidLv: string; // RAID级别
  storageId: string; // 存储设备id
  dataSpace: number; // 存储池上创建LUN或者文件系统时的可用容量 单位MB
  subscribedCapacity: number; //订阅容量
  physicalType: string;//硬盘类型
  diskPoolId:string;//存储池所处硬盘id
}
export class Host{
  hostId: string;
  hostName: string;
  objectId: string;
}
export class Vmkernel{
  portgroup: string;
  ipAddress: string;
  device: string;
  key: string;
  mac: string;
}
