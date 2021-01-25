import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsModifyService{
  constructor(private http: HttpClient) {}
  getNfsDetailById(storeObjectId:string){
    return this.http.get('operatenfs/editnfsstore',{params: {storeObjectId}} );
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
  updateNfs(params= {}){
    return this.http.post('operatenfs/updatenfsdatastore', params);
  }
}
export class NfsModify{
  dataStoreObjectId: string;
  nfsName:string;//   DataStoname
  sameName:boolean;// false true 如果是false就传
  shareName:string;//  共享名称
  fsName:string;//  文件系统名称
  fileSystemId: string;
  qosFlag:boolean;// qos策略开关 false true false关闭
  contolPolicy :string;//  上下线选择标记  枚举值 up low
// up 取值如下
  maxBandwidth: number; //
  maxIops: number; //
//low取值
  minBandwidth: number; //
  minIops: number; //
  latency: number; //
  thin:boolean;// true  代表thin false代表thick
  deduplicationEnabled:boolean;// 重删 true false
  compressionEnabled:boolean;// 压缩 true false
  autoSizeEnable:boolean;// 自动扩容 true false
  shareId: string;
  constructor(){
    this.sameName=true;
  }
}
