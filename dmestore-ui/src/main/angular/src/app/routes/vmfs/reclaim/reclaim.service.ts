import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ReclaimService {
  constructor(private http: HttpClient) {}
  // 空间回收
  reclaimVmfs(params = {}) { // vmfs空间回收
    return  this.http.post('operatevmfs/recyclevmfsbydatastoreids', params);
  }

  // 空间回收
  reclaimVmfsJudge(params = {}) { // vmfs空间回收
    return  this.http.post('operatevmfs/canrecyclevmfsbydatastoreid', params);
  }
}

