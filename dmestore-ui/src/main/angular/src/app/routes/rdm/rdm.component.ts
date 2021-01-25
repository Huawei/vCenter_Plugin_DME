import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from '../common.service';
import {GlobalsService} from '../../shared/globals.service';
import {ClrWizard, ClrWizardPage} from "@clr/angular";

@Component({
  selector: 'app-rdm',
  templateUrl: './rdm.component.html',
  styleUrls: ['./rdm.component.scss'],
  providers: [CommonService]
})
export class RdmComponent implements OnInit {

  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  serviceLevelIsNull = false;
  isOperationErr = false;
  policyEnable = {
    smartTier: false,
    qosPolicy: false,
    resourceTuning: false
  };

  configModel = new customizeVolumes();
  storageDevices:Storages[] = [];
  storagePools = [];
  ownerControllers:StorageController[] = [];
  hostList = [];
  dataStoreObjectId = '';
  levelCheck = 'level';
  dataStores = [];

  searchName = '';
  serviceLevelsRes = [];
  serviceLevels = [];
  serviceLevelId = '';

  vmObjectId = '';

  //qos 框控制
  options1 = null;
  options2 = null;
  options3 = null;
  options4 = null;
  options5= null;

  qos1Show = false;
  qos2Show = false;
  qos3Show = false;
  qos4Show = false;

  dsLoading = false;
  dsDeviceLoading = false;
  slLoading = false;
  tierLoading = false;
  submitLoading = false;
  rdmSuccess = false;
  rdmError = false;

  // 归属控制器 true 支持 false 不支持
  ownershipController = true;

  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
    this.loadStorageDevice();
    // this.loadHosts();
    this.tierFresh();
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
    if(ctx != null){
      this.vmObjectId = ctx[0].id;
    } else{
      this.vmObjectId = 'urn:vmomi:VirtualMachine:vm-1046:674908e5-ab21-4079-9cb1-596358ee5dd1';
    }
    this.ownershipController = true;
    this.loadDataStore();
  }

  // 刷新服务等级列表
  tierFresh(){
    this.tierLoading = true;
    this.http.post('servicelevel/listservicelevel', {}).subscribe((response: any) => {
      this.tierLoading = false;
      if(response.code == '200'){
        this.serviceLevelsRes = this.recursiveNullDelete(response.data);
        this.serviceLevelsRes = this.serviceLevelsRes.filter(item => item.totalCapacity !== 0);
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

  serviceLevelClick(id: string){
     this.serviceLevelId = id;
     this.serviceLevelIsNull = false;
  }

  // 服务等级列表搜索
  search(){
    if (this.searchName !== ''){
      this.serviceLevels = this.serviceLevelsRes.filter(item => item.name.indexOf(this.searchName) > -1);
    } else{
      this.serviceLevels = this.serviceLevelsRes;
    }
  }

  changeQosRedio(){
    this.qos1Show = false;
    this.qos2Show = false;
    this.qos3Show = false;
    this.qos4Show = false;
  }

  changeQosInput(type: string){
    const c = this.configModel.tuning.smartqos;
    if(c.controlPolicy == '1'){
      if(type == 'box'){
        this.qos1Show = (!this.options1 && !this.options2);
        return this.qos1Show;
      }
      if(type == 'band'){
        this.qos2Show = this.options1 && (c.maxbandwidth == '' || c.maxbandwidth == null);
        return this.qos2Show;
      }
      if(type == 'iops'){
        this.qos3Show = this.options2 && (c.maxiops == '' || c.maxiops == null);
        return this.qos3Show;
      }
    }
    if(this.configModel.tuning.smartqos.controlPolicy == '0'){
      if(type == 'box'){
        this.qos1Show = (!this.options3 && !this.options4 && !this.options5);
        return this.qos1Show;
      }
      if(type == 'band') {
        this.qos2Show = this.options3 && (c.minbandwidth == '' || c.minbandwidth == null);
        return this.qos2Show;
      }
      if(type == 'iops'){
        this.qos3Show = this.options4 && (c.miniops == '' || c.miniops == null);
        return this.qos3Show;
      }
      if(type == 'latency'){
        this.qos4Show = this.options5 && (c.latency == '' || c.latency == null);
        return this.qos4Show;
      }
    }
  }

  submit(): void {
    if (!this.ownershipController) {
      this.configModel.ownerController = null;
    }
    let b = JSON.parse(JSON.stringify(this.configModel));
    let body = {};
    if (this.configModel.storageType == '2'){
      if(!this.policyEnable.smartTier){
        b.tuning.smarttier = null;
      }
      if(!this.policyEnable.qosPolicy){
        b.tuning.smartqos = null;
      } else{
        let box = this.changeQosInput('box');
        let band = this.changeQosInput('band');
        let iops = this.changeQosInput('iops');
        let latency = this.changeQosInput('latency');
        if(box || band || iops || latency){
          return;
        }
        if(this.configModel.tuning.smartqos.controlPolicy == '1'){
          b.tuning.smartqos.minbandwidth = null;
          b.tuning.smartqos.miniops = null;
          b.tuning.smartqos.latency = null;
        } else{
          b.tuning.smartqos.maxbandwidth = null;
          b.tuning.smartqos.maxiops = null;
        }
      }
      if(!this.policyEnable.resourceTuning){
        b.tuning.alloctype = null;
        b.tuning.dedupeEnabled = null;
        b.tuning.compressionEnabled = null;
      }
      if(!this.policyEnable.smartTier && !this.policyEnable.qosPolicy && !this.policyEnable.resourceTuning){
        b.tuning = null;
      }

      body = {
        customizeVolumesRequest: {
          customizeVolumes: b
        }
      };
    }
    if (this.configModel.storageType == '1'){
      if(this.serviceLevelId == '' || this.serviceLevelId == null){
        this.serviceLevelIsNull = true;
        return;
      }
      body = {
        createVolumesRequest: {
          serviceLevelId: this.serviceLevelId,
          volumes: this.configModel.volumeSpecs
        }
      };
    }
    console.log(b);
    this.submitLoading = true;
    this.http.post('v1/vmrdm/createRdm?vmObjectId='+this.vmObjectId+'&dataStoreObjectId='+this.dataStoreObjectId
      , body).subscribe((result: any) => {
        this.submitLoading = false;
        if (result.code == '200'){
          this.rdmSuccess = true;
        } else{
          this.rdmError = true;
        }
    }, err => {
      console.error('ERROR', err);
    });
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

  loadStorageDevice(){
    this.dsDeviceLoading = true;
    this.http.get('dmestorage/storages', {}).subscribe((result: any) => {
      this.dsDeviceLoading = false;
      if (result.code === '200'){
        this.storageDevices = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadStoragePool(storageId: string){
    this.slLoading = true;
    const chooseStorage = this.storageDevices.filter(item => item.id == storageId);
    this.ownershipController = chooseStorage[0].storageTypeShow.ownershipController;
    // 查询卷对应归属控制器
    if (this.ownershipController) {
      this.ownerControllers = [];
      if (storageId) {
        this.http.get('dmestorage/storagecontrollers?storageDeviceId='+storageId).subscribe((result:any) => {
          if (result.code == '200') {
            this.ownerControllers = result.data;
          }
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
      }
    }
    console.log("this.ownershipController", this.ownershipController);
    this.http.get('dmestorage/storagepools', {params: {storageId, media_type: "all"}}).subscribe((result: any) => {
      this.slLoading = false;
      if (result.code === '200'){
        this.storagePools = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadDataStore(){
    this.dsLoading = true;
    this.http.get('v1/vmrdm/vCenter/datastoreOnHost', { params: {vmObjectId : this.vmObjectId}}).subscribe((result: any) => {
      this.dsLoading = false;
      if (result.code === '200'){
        this.dataStores = JSON.parse(result.data);
      } else{
        this.dataStores = [];
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }, err => {
      console.error('ERROR', err);
    });
  }

  /**
   * 容量格式化
   * @param c 容量值
   * @param isGB true GB、false MB
   */
  formatCapacity(c: number, isGB:boolean){
    let cNum;
    if (c < 1024){
      cNum = isGB ? c.toFixed(3)+'GB':c.toFixed(3)+'MB';
    }else if(c >= 1024 && c< 1048576){
      cNum = isGB ? (c/1024).toFixed(3) + 'TB' : (c/1024).toFixed(3) + 'GB';
    }else if(c>= 1048576){
      cNum = isGB ? (c/1024/1024).toFixed(3) + 'PB':(c/1024/1024).toFixed(3) + 'TB';
    }
    return cNum;
  }

  /**
   * add 下一页
   */
  addNextPage() {
    this.wizard.next();
  }

  closeWin(){
    this.gs.getClientSdk().modal.close();
  }

}


class customizeVolumes{
  storageType: string;
  availabilityZone: string;
  initialDistributePolicy: string;
  ownerController: string;
  poolRawId: string;
  prefetchPolicy: string;
  prefetchValue: string;
  storageId: string;
  tuning: any;
  volumeSpecs: volumeSpecs[];
  constructor(){
    this.storageType = '1';
    this.volumeSpecs = [new volumeSpecs()];
    this.tuning = new tuning();
    this.initialDistributePolicy = '0';
    this.ownerController = '0';
    this.prefetchPolicy = '3';
  }
}

class volumeSpecs{
  capacity: number;
  count: number;
  name: string;
  unit: string;
  constructor(){
     this.name = '';
     this.capacity = null;
     this.count = 1;
     this.unit = 'GB';
  }
}


class tuning{
  alloctype:string;
  compressionEnabled: boolean;
  dedupeEnabled: boolean;
  smartqos: smartqos;
  smarttier: string;
  workloadTypeId: string;
  constructor(){
    this.smartqos = new smartqos();
    this.alloctype = 'thick';
    this.smarttier = '0';
    this.compressionEnabled = null;
    this.dedupeEnabled = null;
  }
}

class smartqos{
  controlPolicy: string;
  latency: number;
  maxbandwidth: number;
  maxiops: number;
  minbandwidth: number;
  miniops: number;
  name:string;
  constructor(){
    this.controlPolicy = '1'
  }
}
// 存储
export interface Storages {
  name: string;
  id: string;
  storageTypeShow:StorageTypeShow;
}
export interface StorageTypeShow {
  qosTag:number;// qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
  workLoadShow:number;// 1 支持应用类型 2不支持应用类型
  ownershipController:boolean;// 归属控制器 true 支持 false 不支持
  allocationTypeShow:number;// 资源分配类型  1 可选thin/thick 2 可选thin
  deduplicationShow:boolean;// 重复数据删除 true 支持 false 不支持
  compressionShow:boolean; // 数据压缩 true 支持 false 不支持
  capacityInitialAllocation:boolean;// 容量初始分配策略 true 支持 false 不支持
  smartTierShow:boolean;// SmartTier策略 true 支持 false 不支持
  prefetchStrategyShow:boolean;// 预取策略 true 支持 false 不支持
  storageDetailTag:number;// 存储详情下展示情况 1 仅展示存储池和lun 2 展示存储池/lun/文件系统/共享/dtree
}
export class StorageController{
  name:string;
  status:string;
  softVer:string;
  cpuInfo:string;
  cpuUsage:number;
  iops:number;
  ops:number;
  bandwith:number;
  lantency:number;
}
