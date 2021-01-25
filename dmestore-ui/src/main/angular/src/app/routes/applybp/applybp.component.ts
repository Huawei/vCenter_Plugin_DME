import {ChangeDetectorRef, Component, OnInit, AfterViewInit} from '@angular/core';
import {GlobalsService} from "../../shared/globals.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-applybp',
  templateUrl: './applybp.component.html',
  styleUrls: ['./applybp.component.scss']
})
export class ApplybpComponent implements OnInit, AfterViewInit {


  hiddenTip = true;
  // 1 执行中， 2 完成， 3 出错
  status = 1;

  ips = '';
  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private gs: GlobalsService) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    // /update/byCluster
    console.log(this.gs.getClientSdk().app.getClientInfo());
    console.log(this.gs.getClientSdk().app.getNavigationData());
    this.http.post('v1/bestpractice/update/all', {}).subscribe((result: any) => {
      if (result.code == '200'){
        this.status = 2;
        if(result.data){
          result.data.forEach((item)=>{
              if(item.needReboot){
                this.ips = item.hostName + ",";
              }
          });
        }
        if(this.ips != ''){
           this.hiddenTip = false;
        } else{
           this.hiddenTip = true;
        }
      } else{
        this.status = 3;
      }
    }, err => {
      this.status = 3;
      console.error('ERROR', err);
    });
  }

  okClick(){
    this.gs.getClientSdk().modal.close();
  }

}
