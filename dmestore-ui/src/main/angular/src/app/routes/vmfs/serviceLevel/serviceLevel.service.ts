import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ServiceLevelService {
  constructor(private http: HttpClient) {}
  /**
   * 修改服务等级
   * @param params
   */
  changeServiceLevel(params = {}) {
    return  this.http.post('operatevmfs/updatevmfsservicelevel', params);
  }

  /**
   * 获取所有的服务等级数据
   * @param params
   */
  getServiceLevelList(params = {}) {
    return this.http.post('servicelevel/listservicelevel', params);
  }

  /**
   * 通过objectId 获取vmfs
   * @param objectId
   */
  getVmfsById(objectId) {
    return this.http.get('accessvmfs/queryvmfs?dataStoreObjectId='+objectId);
  }

  /**
   * 通过objectId 获取vmfs存储数据
   * @param objectId
   */
  getStorageById(objectId) {
    return this.http.get('accessvmware/relation?datastoreObjectId='+objectId);
  }
}
