import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class ShareService {
  constructor(private http: HttpClient) {}

  getData(param) {
    return this.http.get('accessnfs/share/' + param);
  }
}
export class ShareDetail{
  fsName: string;
  name: string;
  sharePath: string;
  description: string;
  owningDtreeId: string;
  owningDtreeName: string;
  deviceName: string;
  authClientList: AuthClient[];
}
export class AuthClient{
   accessval: string;
   allSquash: string;
   id: string;
   name: string;
   parentId: string;
   rootSquash: string;
   secure: string;
   sync: string;
   type: string;
   vstoreIdInStorage: string;
   vstoreName: string;
}

