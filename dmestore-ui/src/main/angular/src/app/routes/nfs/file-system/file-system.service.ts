import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class FileSystemService {
  constructor(private http: HttpClient) {}

  getData(param) {
    return this.http.get('accessnfs/fileservice/'+param);
  }
}
export class FsDetail{
   name: string;
   description: string;
   device: string;
   storagePoolName: string;
   provisionType: string;
   applicationScenario: string;
   dataDeduplication: boolean;
   dateCompression: boolean;
   controller: string;
   fileSystemId:string;
}

