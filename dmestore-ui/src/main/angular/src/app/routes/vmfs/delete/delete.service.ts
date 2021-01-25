import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class DeleteService {
  constructor(private http: HttpClient) {}
  // 删除
  delVmfs(params = {}) {
    return  this.http.post('accessvmfs/deletevmfs', params);
  }
}

