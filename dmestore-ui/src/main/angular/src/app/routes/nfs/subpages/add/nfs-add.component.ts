import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {AddNfs, Host, NfsAddService, Vmkernel} from "./nfs-add.service";
import {ClrWizard, ClrWizardPage} from "@clr/angular";
import {LogicPort, StorageList, StorageService} from "../../../storage/storage.service";
import {StoragePool, StoragePoolMap} from "../../../storage/detail/detail.service";
@Component({
  selector: 'app-add',
  templateUrl: './nfs-add.component.html',
  styleUrls: ['./nfs-add.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsAddService,StorageService]
})
export class NfsAddComponent implements OnInit{
  viewPage: string;
  pluginFlag: string;
  addForm = new AddNfs();
  errorMsg: string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  unit='GB';
  checkedPool:any;
  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  storagePoolMap:StoragePoolMap[] = [];

  logicPorts: LogicPort[] = [];
  hostList: Host[] = [];
  vmkernelList: Vmkernel[]=[];
  capacityErr=true;


  maxbandwidthChoose=false; // 最大带宽 选中
  maxiopsChoose=false; // 最大iops 选中
  minbandwidthChoose=false; // 最小带宽 选中
  miniopsChoose=false; // 最小iops 选中
  latencyChoose=false; // 时延 选中
  addSuccessShow = false; // 添加成功提示

  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  constructor(private addService: NfsAddService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,private storageService: StorageService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }

  ngOnInit(): void {
    this.modalLoading=true;
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      this.viewPage='delete_vcenter'
    }

    this.storagePoolMap = [];

    //入口是DataSource
    this.viewPage='add_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      this.viewPage='add_vcenter'
    }
    this.storageService.getData().subscribe((s: any) => {
      this.modalLoading=false;
      if (s.code === '200'){
        this.storageList = s.data;

        const allPoolMap:StoragePoolMap[] = []

        s.data.forEach(item  => {
          const poolMap:StoragePoolMap = {
            storageId:item.id,
            storagePoolList:null,
            logicPort:null
          }
          allPoolMap.push(poolMap);
        });

        this.storagePoolMap = allPoolMap;

        this.modalLoading=false;
      }
    });
    this.addService.getHostList().subscribe((r: any) => {
      this.modalLoading=false;
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.addForm = new AddNfs();
    this.checkedPool= null;
    this.errorMsg='';
    // 获取存储列表
    this.cdr.detectChanges();
  }
  jumpTo(page: ClrWizardPage, wizard: ClrWizard) {
    console.log('wizard:')
    console.log(this.wizard)
    if (page && page.completed) {
      wizard.navService.currentPage = page;
    } else {
      wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
  }
  addNfs(){
    //
    this.modalHandleLoading=true;

    this.checkedPool = this.storagePools.filter(item => item.id === this.addForm.storagePoolId)[0];

    this.addForm.poolRawId=this.checkedPool.poolId;
    this.addForm.storagePoolId= this.checkedPool.id;
    // 单位换算
    switch (this.unit) {
      case 'TB':
        this.addForm.size = this.addForm.size * 1024;
        break;
      case 'MB':
        this.addForm.size = this.addForm.size / 1024;
        break;
      case 'KB':
        this.addForm.size = this.addForm.size / (1024 * 1024);
        break;
      default: // 默认GB 不变
        break;
    }
    this.qosFunc(this.addForm);
    this.addService.addNfs(this.addForm).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        // 打开成功提示窗口
        this.addSuccessShow = true;
      }else{
        this.errorMsg = '1';
        console.log("Delete failed:",result.description)
      }
      this.cdr.detectChanges();
    });
  }
  selectStoragePool(){
    this.modalLoading=true;
    this.storagePools = [];
    this.addForm.storagePoolId = undefined;
    this.logicPorts = [];
    this.addForm.currentPortId = undefined;
    if (this.addForm.storagId) {

      const storagePoolMap = this.storagePoolMap.filter(item => item.storageId == this.addForm.storagId);

      const storagePoolList = storagePoolMap[0].storagePoolList;
      const logicPorts = storagePoolMap[0].logicPort;

      // 选择存储后获取存储池
      if (!storagePoolList) {
        this.storageService.getStoragePoolListByStorageId("file",this.addForm.storagId)
          .subscribe((r: any) => {
            if (r.code === '200'){
              this.storagePools = r.data;
            }
            this.cdr.detectChanges();
          });
      } else {
        this.storagePools = storagePoolList;
      }
      if (!logicPorts) {
        this.selectLogicPort();
      } else {
        this.logicPorts = logicPorts;
        this.modalLoading=false;
      }
    }
  }
  selectLogicPort(){
    // 选择存储后逻辑端口
    this.storageService.getLogicPortListByStorageId(this.addForm.storagId)
      .subscribe((r: any) => {
        this.modalLoading=false;
        if (r.code === '200'){
          this.logicPorts = r.data;
          this.storagePoolMap.filter(item => item.storageId == this.addForm.storagId)[0].logicPort = r.data;
        }
        this.cdr.detectChanges();
      });
  }
  checkHost(){
    this.modalLoading=true;

    //选择主机后获取虚拟网卡
    this.addService.getVmkernelListByObjectId(this.addForm.hostObjectId)
      .subscribe((r: any) => {
        this.modalLoading=false;
        if (r.code === '200'){
          this.vmkernelList = r.data;
          this.addForm.vkernelIp=this.vmkernelList[0].ipAddress;
        }
        this.cdr.detectChanges();
      });
  }
  backToNfsList(){
    this.modalLoading=false;
    this.router.navigate(['nfs']);
  }
  closeModel(){
    this.modalLoading=false;
    this.gs.getClientSdk().modal.close();
  }
  qosBlur(type:String, operationType:string) {

    let objVal;
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          objVal = this.addForm.maxBandwidth;
          break;
        case 'maxiops':
          objVal = this.addForm.maxIops;
          break;
        case 'minbandwidth':
          objVal = this.addForm.minBandwidth;
          break;
        case 'miniops':
          objVal = this.addForm.minIops;
          break;
        default:
          objVal = this.addForm.latency;
          break;
      }
    }
    if (objVal && objVal !== '') {
      if (objVal.toString().match(/\d+(\.\d{0,2})?/)) {
        objVal = objVal.toString().match(/\d+(\.\d{0,2})?/)[0];
      } else {
        objVal = '';
      }
    }
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          this.addForm.maxBandwidth = objVal;
          break;
        case 'maxiops':
          this.addForm.maxIops = objVal;
          break;
        case 'minbandwidth':
          this.addForm.minBandwidth = objVal;
          break;
        case 'miniops':
          this.addForm.minIops = objVal;
          break;
        default:
          this.addForm.latency = objVal;
          break;
      }
    }
  }
  matchErr=false;
  nfsNameRepeatErr=false;
  shareNameRepeatErr=false;
  fsNameRepeatErr=false;
  oldNfsName:string;
  oldShareName:string;
  oldFsName:string;
  checkNfsName(){
    if(this.addForm.nfsName==null) return false;
    if(this.oldNfsName==this.addForm.nfsName) return false;
    this.oldNfsName=this.addForm.nfsName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');
    if(reg5.test(this.addForm.nfsName)){
      //验证重复
      this.matchErr=false;
      if (this.addForm.sameName){
        this.checkNfsNameExist(this.addForm.nfsName);
        this.checkShareNameExist(this.addForm.nfsName);
        this.checkFsNameExist(this.addForm.nfsName);
      }else{
        this.checkNfsNameExist(this.addForm.nfsName);
      }
    }else{
      //
      this.matchErr=true;
      //不满足的时候置空
      this.addForm.nfsName=null;
      console.log('验证不通过');
    }
  }
  checkShareName(){
    if(this.addForm.shareName==null) return false;
    if(this.oldShareName=this.addForm.shareName) return false;

    this.oldShareName=this.addForm.shareName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');
    if(reg5.test(this.addForm.shareName)){
      //验证重复
      this.matchErr=false;
        this.checkShareNameExist(this.addForm.shareName);
    }else{
      this.matchErr=true;
      this.addForm.shareName=null;
    }
  }
  checkFsName(){
    if(this.addForm.fsName==null) return false;
    if(this.oldFsName=this.addForm.fsName) return false;

    this.oldFsName=this.addForm.fsName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');
    if(reg5.test(this.addForm.fsName)){
      //验证重复
      this.matchErr=false;
      this.checkShareNameExist(this.addForm.fsName);
    }else{
      this.matchErr=true;
      this.addForm.fsName=null;
    }
  }
  checkNfsNameExist(name:string){
    this.addService.checkNfsNameExist(name).subscribe((r:any)=>{
        if (r.code=='200'){
          if(r.data){
            this.nfsNameRepeatErr=false;
          }else{
            this.nfsNameRepeatErr=true;
            this.addForm.nfsName=null;
          }
        }
    });
  }
  checkShareNameExist(name:string){
    this.addService.checkShareNameExist(name).subscribe((r:any)=>{
      if (r.code=='200'){
        if(r.data){
          this.shareNameRepeatErr=false;
        }else{
          this.shareNameRepeatErr=true;
          this.addForm.nfsName=null;
        }
      }
    });
  }
  checkFsNameExist(name:string){
    this.addService.checkFsNameExist(name).subscribe((r:any)=>{
      if (r.code=='200'){
        if(r.data){
          this.shareNameRepeatErr=false;
        }else{
          this.shareNameRepeatErr=true;
          this.addForm.nfsName=null;
        }
      }
    });
  }


  /**
   * 确认关闭窗口
   */
  confirmActResult() {
    this.wizard.close();// 关闭弹窗
    if (this.pluginFlag=='plugin'){
      this.backToNfsList();
    }else{
      this.closeModel();
    }
  }
  qosFunc(form) {
    console.log("form.qosFlag", form.qosFlag);
    console.log("form.contolPolicy", form.contolPolicy == 'up');
    console.log("form.contolPolicy2", form.contolPolicy == 'low');
    console.log("form.contolPolicy2", form);
    if (!form.qosFlag) {// 关闭状态
      form.contolPolicy = '';
      form.maxBandwidthChoose = false;
      form.maxBandwidth = null;
      form.maxIopsChoose = false;
      form.maxIops = null;
      form.minBandwidthChoose = false;
      form.minBandwidth = null;
      form.minIopsChoose = false;
      form.minIops = null;
      form.latencyChoose = false;
      form.latency = null;
    }else {
      if (form.contolPolicy == 'up') {
        form.minBandwidthChoose = false;
        form.minBandwidth = null;
        form.minIopsChoose = false;
        form.minIops = null;
        form.latencyChoose = false;
        form.latency = null;
        if (!form.maxBandwidthChoose) {
          form.maxBandwidth = null;
        }
        if (!form.maxIopsChoose) {
          form.maxIops = null;
        }
      } else if (form.contolPolicy == 'low') {
        form.maxBandwidthChoose = false;
        form.maxBandwidth = null;
        form.maxIopsChoose = false;
        form.maxIops = null;
        if (!form.minBandwidthChoose) {
          form.minBandwidth = null;
        }
        if (!form.minIopsChoose) {
          form.minIops = null;
        }
        if (!form.latencyChoose) {
          form.latency = null;
        }
      } else {
        form.contolPolicy = '';
        form.maxBandwidthChoose = false;
        form.maxBandwidth = null;
        form.maxIopsChoose = false;
        form.maxIops = null;
        form.minBandwidthChoose = false;
        form.minBandwidth = null;
        form.minIopsChoose = false;
        form.minIops = null;
        form.latencyChoose = false;
        form.latency = null;
      }
    }
  }
}

