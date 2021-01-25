import {
  Component,
  OnInit,
  AfterViewInit,
  ChangeDetectorRef,
  OnDestroy,
  NgZone, ViewChild
} from '@angular/core';

import {HttpClient} from '@angular/common/http';
import { CommonService } from '../common.service';
import {MakePerformance, NfsService} from "../nfs/nfs.service";
import { GlobalsService }     from "../../shared/globals.service";
import {TranslatePipe} from "@ngx-translate/core";
import {ServicelevelService, SLStoragePool} from "./servicelevel.service";
import {ServiceLevelFilter} from "../vmfs/list/filter.component";
import {SLSPStatusFilter} from "./filter.component";

@Component({
  selector: 'app-servicelevel',
  templateUrl: './servicelevel.component.html',
  styleUrls: ['./servicelevel.component.scss'],
  providers: [ CommonService, TranslatePipe,MakePerformance, NfsService, ServicelevelService]
})
export class ServicelevelComponent implements OnInit, AfterViewInit, OnDestroy {


  // 详情页面弹出控制
  popShow = false;
  // 容量曲线图
  volumeCapacity={};
  storagePoolCapacity={};
  // 性能曲线图
  volumeMaxResponseTime={};
  volumeDensity={};
  volumeThroughput={};
  volumeBandwidth={};
  storagePoolDensity={};

  range = new Range();

  // 详情列表按钮控制
  storagePoolRadioCheck = 'basic';
  volumeRadioCheck = 'basic';

  // 服务等级列表搜索
  searchName = '';
  // 服务等级列表排序
  sortItem = '';
  sortItems = [
    {
      id: 'name',
      value: 'tier.names'
    },
    {
      id: 'totalCapacity',
      value: 'tier.totals'
    },
    {
      id: 'latency',
      value: 'tier.latencys'
    },
    {
      id: 'maxIOPS',
      value: 'tier.maxIOPSs'
    },
    {
      id: 'minIOPS',
      value: 'tier.minIOPSs'
    },
    {
      id: 'maxBandWidth',
      value: 'tier.maxBandWidths'
    },
    {
      id: 'minBandWidth',
      value: 'tier.minBandWidths'
    }
  ];

  // 选中的服务等级
  selectedModel: Servicelevel =
  {
        id: '1',
        name: '服务等级_CCC2',
        description: 'description',
        type: 'BLOCK', // FILE BLOCK
        protocol: 'iSCSI', // FC, iSCSI
        totalCapacity: 1000,
        usedCapacity: 600,
        usedRate: 40,
        freeCapacity: 400,
        capabilities: {
            resourceType: 'thin', // default_type、thin、thick
            compression: 'default_type', // default_type, enabled, disabled,
            deduplication: 'enabled', // default_type, enabled, disabled,
            iopriority: {
                enabled: true,
                policy: 1, // IO优先级枚举值, 取值范围：1：低；2：中；3：高,
            },
            smarttier: {
                enabled: true,
                policy: 1, // 数据迁移等级，取值范围：0：不迁移, 1：自动迁移, 2：向低性能层迁移, 3：向高性能层迁移
            },
            qos: {
                enabled: true,
                qosParam: {
                  latency: 25,
                  latencyU: 'ms',
                  minBandWidth: 2000,
                  minIOPS: 5000,
                  maxBandWidth: 10000,
                  maxIOPS: 20000,
                }
            }
        }
  };

  // 服务等级列表
  serviceLevels = [];
  // 服务等级列表 服务器返回数据
  serviceLevelsRes = [];


  sortUpDown = {
    isFirst: true,
    s: 'desc'
  }

  // ===============storage pool==============
  // 表格loading标志
  storeagePoolIsloading = false;
  // 数据列表
  storagePoolList: SLStoragePool[] = [];
  storagePoolTotal = 0;
  // ===============storage pool end==============

  // ===============volume==============
  // 表格loading标志
  volumeIsloading = false;
  // 数据列表
  volumeList: Volume[] = [];
  volumeTotal = 0;

  // ===============volume end==============

  // ===============applicationType==============
  // 表格loading标志
  applicationTypeIsloading = false;
  // 数据总数
  applicationTypeTotal = 0;
  // 数据列表
  applicationTypeList: ApplicationType[] = [];
  // 选中列表
  applicationTypeSelected: ApplicationType[];
  // ===============applicationType end==============

  tipModalSuccess = false;
  tipModalFail = false;

  tierLoading = false;
  syncLoading = false;

  @ViewChild('sLSPStatusFilter') sLSPStatusFilter: SLSPStatusFilter;

  constructor(private ngZone: NgZone,
              private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private gs: GlobalsService,
              private makePerformance: MakePerformance, private translatePipe:TranslatePipe) { }

  ngOnInit(): void {
  }

  ngOnDestroy() {
  }

  ngAfterViewInit() {
    //this.ngZone.runOutsideAngular(() => this.initChart());
    this.refresh();
  }

  clickName(o: any){
    this.popShow=true;
    this.volumeRadioCheck = 'basic';
    this.storagePoolRadioCheck = 'basic';
    this.selectedModel=o;

    this.initChart(this.range);
  }

  closeS(){
    this.popShow=false;
    this.volumeRadioCheck = 'basic';
    this.storagePoolRadioCheck = 'basic';
  }

  // ===============storage pool==============
  storagePoolRefresh(){
    setTimeout(()=>{
      this.storeagePoolIsloading = true;
      this.http.post('servicelevel/listStoragePoolsByServiceLevelId', this.selectedModel.id).subscribe((response: any) => {
        if (response.code == '200'){
          this.storagePoolList = response.data;
        } else{
          this.storagePoolList = [];
        }
        this.storagePoolList.forEach(item => item.usedCapacity = Number(((item.consumedCapacity)/item.totalCapacity * 100).toFixed(2)));
        this.storagePoolTotal = this.storagePoolList.length;
        this.storeagePoolIsloading = false;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法

        this.lastStorePool24HPeak();
      }, err => {
        console.error('ERROR', err);
        this.storeagePoolIsloading = false;
      });
    }, 200);
  }
  // ===============storage pool end==============

  // ===============volume pool==============
  volumeRefresh(){
    setTimeout(()=>{
      this.volumeIsloading = true;
      this.http.post('servicelevel/listVolumesByServiceLevelId', this.selectedModel.id).subscribe((response: any) => {
        if (response.code == '200'){
          this.volumeList = response.data;
        } else{
          this.volumeList = [];
        }
        this.volumeTotal = this.volumeList.length;
        this.volumeIsloading = false;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法

        this.lastVloume24HPeak();
      }, err => {
        console.error('ERROR', err);
        this.volumeIsloading = false;
      });
    }, 200);
  }
  // ===============volume pool end==============

  // ===============applicationType pool==============
  applicationTypeRefresh(){
    this.applicationTypeIsloading = true;
    this.applicationTypeList = [];
    this.applicationTypeTotal = 0;
    this.applicationTypeIsloading = false;
    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
  }
  // ===============applicationType pool end==============

  // 刷新服务等级列表
  refresh(){
    this.tierLoading = true;
    this.http.post('servicelevel/listservicelevel', {}).subscribe((response: any) => {
      this.tierLoading = false;
      if(response.code == '200'){
        this.serviceLevelsRes = this.recursiveNullDelete(response.data);
        for (const i of this.serviceLevelsRes){
          if (i.totalCapacity == 0){
            i.usedRate = 0.0;
          } else {
            i.usedRate =  ((i.usedCapacity / i.totalCapacity * 100).toFixed(2));
          }
          i.usedCapacity = (i.usedCapacity/1024).toFixed(2);
          i.totalCapacity = (i.totalCapacity/1024).toFixed(2);
          i.freeCapacity = (i.freeCapacity/1024).toFixed(2);

          if(!i.capabilities){
            i.capabilities = {};
          }
        }
        this.search();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  // 服务等级列表搜索
  search(){
    if (this.searchName !== ''){
      this.serviceLevels = this.serviceLevelsRes.filter(item => item.name.indexOf(this.searchName) > -1);
    } else{
      this.serviceLevels = this.serviceLevelsRes;
    }
    this.sortItemsChange();
  }

  sortBtnClick(){
    if(this.sortItem != '' || !this.sortUpDown.isFirst){
      if(this.sortUpDown.s == "desc"){
        this.sortUpDown.s = "asc";
      } else if(this.sortUpDown.s == "asc"){
        this.sortUpDown.s = "desc";
      }
    }
    this.sortUpDown.isFirst = false;
    this.sortItemsChange();
  }

  // 服务等级列表排序
  sortItemsChange(){
    let o = this.sortItem;
    if (o == ''){
      o = 'totalCapacity';
    }
    this.serviceLevels = this.serviceLevels.sort(this.compare(o, this.sortUpDown.s));
  }

  recursiveNullDelete(obj: any){
    for (const property in obj){
      if (obj[property] === null){
        delete obj[property];
      } else if (obj[property] instanceof Object){
        this.recursiveNullDelete(obj[property]);
      } else if (property == 'minBandWidth' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'maxBandWidth' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'minIOPS' && obj[property] == 0){
        delete obj[property];
      } else if (property == 'maxIOPS' && obj[property] == 0){
        delete obj[property];
      } else if(property == 'latency' && obj[property] == 0){
        delete obj[property];
      }
    }
    return obj;
  }

  // 获取指定属性值
  recursivePropertyies(prop: string, obj: object){
    for (const property in obj){
      if (property === prop){
        return obj[prop];
      } else if (obj[property] instanceof Object){
        const r = this.recursivePropertyies(prop, obj[property]);
        if (r !== undefined){
          return r;
        }
      }
    }
  }

  // 比较函数
  compare(prop: any, sort: string){
    return (obj1: any, obj2: any) => {
      let val1 = this.recursivePropertyies(prop, obj1);
      let val2 = this.recursivePropertyies(prop, obj2);
      if (val1 === undefined) {
        val1 = '';
      }
      if (val2 === undefined) {
        val2 = '';
      }
      if(parseFloat(val1).toString() != "NaN"){
        val1 = parseFloat(val1);
      }
      if(parseFloat(val2).toString() != "NaN"){
        val2 = parseFloat(val2);
      }
      if (val1 < val2) {
        if (sort === 'asc'){
          return -1;
        } else {
          return 1;
        }
      } else if (val1 > val2) {
        if (sort === 'asc'){
          return 1;
        } else {
          return -1;
        }
      } else {
          return 0;
      }
    };
  }


  lastVloume24HPeak(){
    let obj_ids = [];
    this.volumeList.forEach((item)=>{
        if(item.instanceId){
          obj_ids.push(item.instanceId);
        }
    });
    const p = {
      "obj_type_id" : "1125921381679104",
      "indicator_ids" : ["1125921381744641","1125921381744642","1125921381744643"],
      "obj_ids" : obj_ids,
      "interval" : "MINUTE",
      "range" : "LAST_1_DAY"
    };
    this.http.post('datastorestatistichistrory/volume', p).subscribe((response: any) => {
      if (response.code == '200'){
        this.volumeList.forEach((item)=>{
            let i = response.data[item.instanceId];
            if (i != undefined){
              let iops = i['1125921381744641'].max;
              let latency = i['1125921381744642'].max;
              let bandwidth = i['1125921381744643'].max;

              item.iops = parseFloat(iops[Object.keys(iops)[0]]).toFixed(2);
              item.latency = parseFloat(latency[Object.keys(latency)[0]]).toFixed(2);
              item.bandwidth = parseFloat(bandwidth[Object.keys(bandwidth)[0]]).toFixed(2);
            }
        });
      } else{
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
      this.storeagePoolIsloading = false;
    });
  }

  lastStorePool24HPeak(){
    let obj_ids = [];
    this.storagePoolList.forEach((item)=>{
      if(item.storageInstanceId){
        obj_ids.push(item.storageInstanceId);
      }
    });
    const p = {
      "obj_type_id" : "1125912791744512",
      "indicator_ids" : ["1125912791810049","1125912791810050","1125912791810051"],
      "obj_ids" : obj_ids,
      "interval" : "MINUTE",
      "range" : "LAST_1_DAY"
    };
    this.http.post('datastorestatistichistrory/servicelevelStoragePool', p).subscribe((response: any) => {
      if (response.code == '200'){
        this.storagePoolList.forEach((item)=>{
          let i = response.data[item.storage_instance_id];
          if (i != undefined){
            let iops = i['1125912791810049'].max;
            let latency = i['1125912791810050'].max;
            let bandwidth = i['1125912791810051'].max;

            item.iops = parseFloat(iops[Object.keys(iops)[0]]).toFixed(2);
            item.latency = parseFloat(latency[Object.keys(latency)[0]]).toFixed(2);
            item.bandwidth = parseFloat(bandwidth[Object.keys(bandwidth)[0]]).toFixed(2);
          }
        });
      } else{
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
      this.storeagePoolIsloading = false;
    });
  }

  async initChart(range: Range) {
    this.makePerformance.setChartSingle(
      300,
      // '卷最大I/O响应时间(ms)',
      this.translatePipe.transform('performance.volumeMaximumIORespTime'),
      '',
      ["1125921381744655"],
      [this.selectedModel.id],
      range.range,
      'datastorestatistichistrory/servicelevelLun',
      null,
      null).then(
        res => {
      this.volumeMaxResponseTime = res;
      this.cdr.detectChanges();
    });
    this.makePerformance.setChartSingle(
      300,
      // '卷I/O密度(IOPS/TB)',
      this.translatePipe.transform('performance.volumeIODensity'),
      '',
      ["1223"],
      [this.selectedModel.id],
      range.range,
      'datastorestatistichistrory/servicelevelLun',
      null,
      null).then(
      res => {
        this.volumeDensity = res;
        this.cdr.detectChanges();
      });
    this.makePerformance.setChartSingle(
      300,
      // '卷总吞吐量(IOPS)',
      this.translatePipe.transform('performance.totalVolumeThroughput'),
      '',
      ["1125921381744641"],
      [this.selectedModel.id],
      range.range,
      'datastorestatistichistrory/servicelevelLun',
      null,
      null).then(
      res => {
        this.volumeThroughput = res;
        this.cdr.detectChanges();
      });
    this.makePerformance.setChartSingle(
      300,
      // '卷总带宽(MB/s)',
      this.translatePipe.transform('performance.volumeTotalBandwidth'),
      '',
      ["1125921381744643"],
      [this.selectedModel.id],
      range.range,
      'datastorestatistichistrory/servicelevelLun',
      null,
      null).then(
      res => {
        this.volumeBandwidth = res;
        this.cdr.detectChanges();
      });
    this.makePerformance.setChartSingle(
      300,
      // '存储池I/O密度(IOPS/TB)',
      this.translatePipe.transform('performance.storagePoolIODensity'),
      '',
      ["111"],
      [this.selectedModel.id],
      range.range,
      'datastorestatistichistrory/servicelevelLun',
      null,
      null).then(
      res => {
        this.storagePoolDensity = res;
        this.cdr.detectChanges();
      });
  }

  /**
   * 同步虚拟机存储策略
   */
  syncStoragePolicy(){
    this.syncLoading = true;
    const url = "servicelevel/manualupdate";
    this.http.post(url, {}).subscribe((response: any) => {
      this.syncLoading = false;
      if (response.code == '200'){
         this.tipModalSuccess = true;
      } else{
        this.tipModalFail = true;
      }
    }, err => {
      console.error('ERROR', err);
      alert("同步失败");
    });
  }
}

class Servicelevel {
  id: string;
  name: string;
  description: string;
  type: string; // FILE BLOCK
  protocol: string; // FC, iSCSI
  totalCapacity: number;
  usedCapacity: number;
  freeCapacity: number;
  usedRate: number;
  capabilities: {
    resourceType: string; // default_type、thin、thick
    compression: string; // default_type, enabled, disabled;
    deduplication: string; // default_type, enabled, disabled;
    iopriority: {
      enabled: boolean;
      policy: number; // IO优先级枚举值, 取值范围：1：低；2：中；3：高;
    };
    smarttier: {
      enabled: boolean;
      policy: number; // 数据迁移等级，取值范围：0：不迁移; 1：自动迁移; 2：向低性能层迁移; 3：向高性能层迁移
    };
    qos: {
      enabled: boolean;
      qosParam: {
        latency: number;
        latencyU: string;
        minBandWidth: number;
        minIOPS: number;
        maxBandWidth: number;
        maxIOPS: number;
      };
    };
  };
}

interface Volume {
  id: string;
  name: string;
  status: string;
  capacity: number;
  alloctype: string;
  instanceId: string;
  capacityUsage: number;

  latency: string;
  iops: string;
  bandwidth: string;
}


class ApplicationType {
  id: string;
  storageDevice: string;
  applicationType;
}

class Range{
  range: string;
  interval: string;
  constructor(){
    this.range = "LAST_1_DAY" ;
    this.interval = "MINUTE";
  }
}
