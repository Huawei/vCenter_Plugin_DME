import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class LogicportService {
  constructor(private http: HttpClient) {}

  getData(param) {
    return this.http.get('accessnfs/logicport/'+param);
  }
}
export class LogicDetail{
   name: string;
   ip: string;
   status: string;
   runningStatus: string;
   activePort: string;
   currentPort: string;
   failoverGroup: string;
}
