import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class NfsDeleteService{
  constructor(private http: HttpClient) {}
  delNfs(params={}){
    return this.http.post('accessnfs/deletenfs', params);
  }
}
