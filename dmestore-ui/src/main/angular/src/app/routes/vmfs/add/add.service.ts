import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AddService {
  constructor(private http: HttpClient) {}
  // 获取所有的主机
  getHostList() {
    return this.http.get('accessvmware/listhost');
  }

  // 获取所有的集群
  getClusterList() {
    return this.http.get('accessvmware/listcluster');
  }

  // 创建vmfs
  createVmfs(params = {}) {
    return  this.http.post('accessvmfs/createvmfs', params);
  }
  // 获取存储
  getStorages() {
    return this.http.get('dmestorage/storages');
  }
  // 通过存储ID获取存储池数据 (vmfs添加mediaType为block)
  getStoragePoolsByStorId(storageId: string, mediaType: string) {
    return this.http.get('dmestorage/storagepools?storageId='+ storageId + '&mediaType=' + mediaType);
  }

  /**
   * 获取所有的服务等级数据
   * @param params
   */
  getServiceLevelList(params = {}) {
    return this.http.post('servicelevel/listservicelevel', params);
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
}

