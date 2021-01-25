import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsUnmountService{
  constructor(private http: HttpClient) {}
  //获取ds挂载的集群和主机列表
  getMountedHostList(storageId: string){
    return this.http.get('accessnfs/gethostsbystorageid/'+storageId);
  }
  getMountedClusterList(storageId: string){
    return this.http.get('accessnfs/gethostgroupsbystorageid/'+storageId);
  }
  unmount(params={}){
    return this.http.post('accessnfs/unmountnfs', params);
  }

  getVmkernelListByObjectId(hostObjectId:string){
    return this.http.get('accessvmware/getvmkernelipbyhostobjectid',{params: {hostObjectId}} );
  }

  /**
   * 通过hostID查询已挂载的VMFS
   * @param hostObjectId
   */
  getMountedByHostObjId(hostObjectId, dataStoreType) {
    return  this.http.get('accessvmware/getmountdatastoresbyhostobjectid?hostObjectId='+hostObjectId+'&dataStoreType='+dataStoreType);
  }
  /**
   * 通过clusterID查询已挂载的VMFS
   * @param hostObjectId
   */
  getMountedByClusterObjId(clusterObjId, dataStoreType) {
    return  this.http.get('accessvmware/getmountdatastoresbyclusterobjectid?clusterObjectId='+clusterObjId+'&dataStoreType='+dataStoreType);
  }

}
export class Host {
  hostId: string;
  hostName: string;
}
// 集群列表
export class Cluster {
  clusterId: string;
  clusterName: string;
}
