import {
  Component,
  OnInit,
  ChangeDetectionStrategy, ChangeDetectorRef,
} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { CommonService } from '../common.service';
import { GlobalsService }     from "../../shared/globals.service";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-bestpractice',
  templateUrl: './bestpractice.component.html',
  styleUrls: ['./bestpractice.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [CommonService, TranslatePipe]
})
export class BestpracticeComponent implements OnInit {

  // ================最佳实践列表=============
  rowSelected = []; // 当前选中数据
  isLoading = true; // table数据loading
  list: Bestpractice[] = []; // 数据列表
  total = 0; // 总数据数量

  query = { // 查询数据
  };
  // =================END===============

  // ================主机列表=============
  hostModalShow = false;
  hostSelected = []; // 主机选中列表
  hostIsLoading = false; // table数据loading
  hostList: Host[] = []; // 数据列表
  hostTotal = 0; // 总数据数量
  currentBestpractice: Bestpractice;
  // ================END====================

  tipModal = false;
  ips = '';
  applyType = '1';


  tipModalSuccess = false;
  tipModalFail = false;

  applyLoading = false;
  checkLoading = false;
  constructor(private cdr: ChangeDetectorRef,
              public gs: GlobalsService,
              private http: HttpClient,
              private commonService: CommonService,
              private translatePipe:TranslatePipe) { }

  ngOnInit(): void {
    this.practiceRefresh();
  }

  /**
   * 列表实时最佳实践 参数封装
   * @returns {any[]}
   */
  packApplyPracticeParams() {
    const params = [];
    this.ips = '';
    this.rowSelected.forEach((item) => {
      const i = {hostSetting:'', hostObjectIds: []};
      i.hostSetting = item.hostSetting;
      item.hostList.forEach((s) => {
        i.hostObjectIds.push(s.hostObjectId);
        if (s.needReboot == "true"){
          this.ips += s.hostName+",";
        }
      });
      params.push(i);
    });
    return params;
  }

  /**
   * 主机列表实时最佳实践 参数封装
   * @returns {any[]}
   */
  packApplyPracticeParamsByHost(){
    const params = [];
    const i = {hostSetting:'', hostObjectIds: []};
    i.hostSetting = this.currentBestpractice.hostSetting;
    let ips = '';
    this.hostSelected.forEach((s) => {
      i.hostObjectIds.push(s.hostObjectId);
      if (s.needReboot == "true"){
        ips += s.hostName+",";
      }
    });
    params.push(i);

    return params;
  }

  /**
   * 实时最佳实践
   * @param params
   */
  applyPractice(params){
    this.applyLoading = true;
    this.http.post('v1/bestpractice/update/bylist', params).subscribe((result: any) => {
      this.applyLoading = false;
      if (result.code == '200'){
        this.tipModalSuccess = true;
        if(this.applyType != '1'){
          this.hostModalShow = false;
        }
        this.practiceRefresh();
      } else{
        this.tipModalFail = true;
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
  }

  openTip(){
    this.tipModal = true;
  }

  closeTip(){
    this.tipModal = false;
  }

  applyClick(type: string){
    this.applyType = type;
    let params;
    if(this.applyType == '1'){
      params = this.packApplyPracticeParams();
    } else {
      params = this.packApplyPracticeParamsByHost();
    }
    if(this.ips.length != 0){
      this.openTip();
    } else{
      this.applyPractice(params);
    }
  }

  tipOk(){
    this.closeTip();
    let params;
    if(this.applyType == '1'){
      params = this.packApplyPracticeParams();
    } else {
      params = this.packApplyPracticeParamsByHost();
    }
    this.applyPractice(params);
  }

  recheck() {
    this.checkLoading = true;
    this.http.post('v1/bestpractice/check', {}).subscribe((result: any) => {
      this.checkLoading = false;
      if (result.code == '200'){
        this.tipModalSuccess = true;
        this.practiceRefresh();
      } else{
        this.tipModalFail = true;
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
  }

  practiceRefresh(){
    this.isLoading = true;
    this.http.get('v1/bestpractice/records/all', {}).subscribe((result: any) => {
          if (result.code === '200'){
            this.list = result.data;
            // bug修改：列表页面级别过滤 中英文问题
            if (this.list) {
              this.list.forEach(item => {
                let levelDesc;
                switch (item.level) {
                  case "Critical":
                    levelDesc = this.translatePipe.transform("bestPractice.critical");
                    break;
                  case "Major":
                    levelDesc = this.translatePipe.transform("bestPractice.major");
                    break;
                  case "Warning":
                    levelDesc = this.translatePipe.transform("bestPractice.warning");
                    break;
                  case "Info":
                    levelDesc = this.translatePipe.transform("bestPractice.info");
                    break;
                  default:
                    levelDesc = "--";
                    break;
                }
                item.levelDesc = levelDesc;
              });
            }
            this.total = result.data.length;
            this.isLoading = false;
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          }
    }, err => {
      console.error('ERROR', err);
    });
  }

  openHostList(bestpractice: Bestpractice){
    this.hostModalShow = true;
    this.currentBestpractice = bestpractice;
    this.hostRefresh();
  }

  hostRefresh(){
    if (this.hostModalShow === true){
      this.hostIsLoading = true;
      this.hostList = this.currentBestpractice.hostList;
      this.hostTotal = this.currentBestpractice.hostList.length;
      this.hostIsLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }
  }
}

class Bestpractice {
  hostSetting: string;
  recommendValue: number;
  level: string;
  levelDesc: string;
  count: number;
  hostList: Host[];
}

class Host {
  hostSetting: string;
  level: string;
  hostName: string;
  recommendValue: number;
  actualValue: number;
  hostObjectId: string;
  needReboot: string;
  hostId: string;
  autoRepair: string;
}
