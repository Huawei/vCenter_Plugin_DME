import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ExpandService {
  constructor(private http: HttpClient) {}

  /**
   * 扩容
   * @param params
   */
  expandVMFS(params = {}) {
    return  this.http.post('operatevmfs/expandvmfs', params);
  }

  /**
   * 通过objectId 获取vmfs
   * @param objectId
   */
  getVmfsById(objectId) {
    return this.http.get('accessvmfs/listvmfs?objectId='+objectId);
  }

  /**
   * 通过objectId 获取vmfs存储数据
   * @param objectId
   */
  getStorageById(objectId) {
    return this.http.get('accessvmware/relation?datastoreObjectId='+objectId);
  }
}
