import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ModifyService {
  constructor(private http: HttpClient) {}

  /**
   * 修改VMFS
   * @param volumeId
   * @param params
   */
  updateVmfs(volumeId: string, params = {}) {
    return  this.http.put('operatevmfs/updatevmfs?volumeId=' + volumeId, params);
  }

  /**
   * 通过objectId 获取vmfs
   * @param objectId
   */
  getVmfsById(objectId) {
    return this.http.get('accessvmfs/queryvmfs?dataStoreObjectId='+objectId);
  }

  // 附列表数据
  getChartData(volumeIds: string[] ) {
    return this.http.get('accessvmfs/listvmfsperformance', {params: {volumeIds}});
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
  getStorageDetail(storageId: string){
    return this.http.get('dmestorage/storage', {params: {storageId}});
  }
  /**
   * 通过objectId 获取vmfs存储数据
   * @param objectId
   */
  getStorageById(objectId:string) {
    return this.http.get('accessvmware/relation?datastoreObjectId='+objectId);
  }
}
