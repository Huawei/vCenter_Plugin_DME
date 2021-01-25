import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {Host, List, NfsService,UpdateNfs} from './nfs.service';
import {GlobalsService} from '../../shared/globals.service';
import {LogicPort, StorageList, StorageService} from '../storage/storage.service';
import {StoragePool, StoragePoolMap} from "../storage/detail/detail.service";
import {ClrDatagridSortOrder, ClrWizard, ClrWizardPage} from "@clr/angular";
import {VmfsListService} from "../vmfs/list/list.service";
import {Router} from "@angular/router";
import {AddNfs, NfsAddService, Vmkernel} from "./subpages/add/nfs-add.service";
import {FormControl, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-nfs',
  templateUrl: './nfs.component.html',
  styleUrls: ['./nfs.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [NfsService,StorageService,VmfsListService,NfsAddService],
})
export class NfsComponent implements OnInit {
  descSort = ClrDatagridSortOrder.DESC;
  list: List[] = []; // 数据列表
  radioCheck = 'list'; // 切换列表页显示
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  // ==========弹窗参数=======
  modifyShow = false;
  expand = false; // 扩容弹出框
  mountObj = '1';
  fsIds = [];
  unit='GB';
  hostList: Host[] = [];
  addForm = new AddNfs();
  addFormGroup = new FormGroup({
    storagId: new FormControl('',
      Validators.required),
    storagePoolId: new FormControl(true,
      Validators.required),
    currentPortId: new FormControl('',Validators.required
    ),
    nfsName: new FormControl('',
      Validators.required),
    size: new FormControl('',
      Validators.required),
    hostObjectId: new FormControl('',
      Validators.required),
    vkernelIp: new FormControl('',
      Validators.required),
    accessMode: new FormControl('',
      Validators.required),
    sameName: new FormControl(true,
      Validators.required),
    fsName: new FormControl('',
      ),
    shareName: new FormControl('',
      ),
    type: new FormControl('',
      ),
    securityType: new FormControl(false,
      ),
    unit: new FormControl('GB',
      Validators.required),
  });
  storageList: StorageList[] = [];
  storagePools: StoragePool[] = [];
  storagePoolMap:StoragePoolMap[] = [];

  updateNfs: UpdateNfs = new UpdateNfs();
  addModelShow = false; // 添加窗口
  errorMsg: string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  checkedPool:any;
  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;
  addSuccessShow = false; // 添加成功提示
  modifySuccessShow = false; // 添加成功提示

  logicPorts: LogicPort[] = [];
  oldNfsName:string;
  oldShareName:string;
  oldFsName:string;
  matchErr=false;
  nfsNameRepeatErr=false;
  shareNameRepeatErr=false;
  fsNameRepeatErr=false;

  vmkernelList: Vmkernel[]=[];

  maxbandwidthChoose=false; // 最大带宽 选中
  maxiopsChoose=false; // 最大iops 选中
  minbandwidthChoose=false; // 最小带宽 选中
  miniopsChoose=false; // 最小iops 选中
  latencyChoose=false; // 时延 选中

  errMessage = '';
  constructor(private addService: NfsAddService, private remoteSrv: NfsService, private cdr: ChangeDetectorRef, public gs: GlobalsService ,
              private storageService: StorageService,private vmfsListService: VmfsListService,private router:Router) { }
  ngOnInit(): void {
    this.getNfsList();
  }
  // 获取nfs列表
  getNfsList() {
    this.isLoading = true;
    // 进行数据加载
    this.remoteSrv.getData()
      .subscribe((result: any) => {
        this.list = result.data;
        if (this.list!=null){
          this.total=this.list.length;
        }
        //处理利用率排序问题
        this.handleSortingFeild();
        this.isLoading = false;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        // 获取性能列表
        this.listnfsperformance();
      });
  }

  handleSortingFeild(){
    if(this.list!=null){
      this.list.forEach(n=>{
        n.capacityUsage=(n.capacity - n.freeSpace)/n.capacity;
      });
    }
  }
// 性能视图列表
  listnfsperformance(){
    if (this.list === null || this.list.length <= 0){ return; }
    const fsIds = [];
    this.list.forEach(item => {
      fsIds.push(item.fsId);
    });
    this.fsIds = fsIds;
    this.remoteSrv.getChartData(this.fsIds).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: List [] = result.data;
        if ( chartList !== null && chartList.length > 0){
         this.list.forEach(item => {
           chartList.forEach(charItem => {
             if (item.fsId === charItem.fsId){
               item.ops = charItem.ops;
               item.bandwidth = charItem.bandwidth;
               item.readResponseTime = charItem.readResponseTime;
               item.writeResponseTime = charItem.writeResponseTime;
             }
           });
         });
         this.cdr.detectChanges();
        }
      }
    });
  }

  // 页面跳转
  jumpTo(page: ClrWizardPage) {
    if (page && page.completed) {
      this.wizard.navService.currentPage = page;
    } else {
      this.wizard.navService.setLastEnabledPageCurrent();
    }
    this.wizard.open();
  }
  addView(){
    // const flag = 'plugin';
    // this.router.navigate(['nfs/add'],{
    //   queryParams:{
    //     flag
    //   }
    // });
    this.addModelShow = true;
    this.storageList = null;
    this.storagePoolMap = [];
    // 添加页面默认打开首页
    this.jumpTo(this.addPageOne);

    this.storageService.getData().subscribe((s: any) => {
      this.modalLoading=false;
      if (s.code === '200'){
        this.storageList = s.data;
        this.modalLoading=false;

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
      }
    });
    this.hostList = null;
    this.addService.getHostList().subscribe((r: any) => {
      this.modalLoading=false;
      if (r.code === '200'){
        this.hostList = r.data;
        this.cdr.detectChanges();
      }
    });
    this.addForm = new AddNfs();
    // 初始化form
    this.addFormGroup.reset(this.addForm);
    this.checkedPool= null;
    this.errorMsg='';
    // 获取存储列表
    this.cdr.detectChanges();
  }
  modifyData() {
    const flag="plugin";
    const objectid=this.rowSelected[0].objectid;
    this.router.navigate(['nfs/modify'],{
      queryParams:{
        objectid,flag
      }
    });
  }
  modifyCommit(){
    console.log('提交参数：');
    this.updateNfs.name=this.updateNfs.nfsName;
    console.log(this.updateNfs);
    this.remoteSrv.updateNfs(this.updateNfs).subscribe((result: any) => {
      if (result.code === '200'){
        this.modifyShow = false;
      }else{
        this.modifyShow = true;
        this.errMessage = '编辑失败！'+result.description;
      }
    });
  }
  expandView(){
    const flag = 'plugin';
    const fsId=this.rowSelected[0].fsId;
    const objectId=this.rowSelected[0].objectid;
    this.router.navigate(['nfs/expand'],{
      queryParams:{
        objectId,fsId,flag
      }
    });
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
    //  控制策略若未选清空数据
    this.qosFunc(this.addForm);
    this.addService.addNfs(this.addForm).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        // 打开成功提示窗口
        this.addSuccessShow = true;
        // 添加成功后刷新数据
        this.getNfsList();
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

              this.storagePoolMap.filter(item => item.storageId == this.addForm.storagId)[0].storagePoolList = r.data;
            }
            this.cdr.detectChanges();
          });
      } else {
        console.log("storagePoolList exists")
        this.storagePools = storagePoolList;
      }
      if (!logicPorts) {
        this.selectLogicPort();
      } else {
        console.log("logicPorts exists")
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

  // 弹出缩容页面
  reduceView(){
    const flag = 'plugin';
    const fsId=this.rowSelected[0].fsId;
    const objectId=this.rowSelected[0].objectid;
    this.router.navigate(['nfs/reduce'],{
      queryParams:{
        objectId,fsId,flag
      }
    });
  }
  // 挂载
  mount(){
    this.jumpPage(this.rowSelected[0].objectid,"nfs/dataStore/mount");
    const flag = 'plugin';
    const objectId=this.rowSelected[0].objectid;
    const dsName=this.rowSelected[0].name;
    this.router.navigate(["nfs/dataStore/mount"],{
      queryParams:{
        objectId,flag,dsName
      }
    });
  }
  jumpPage(objectId:string,url:string){
    const flag = 'plugin';
    this.router.navigate([url],{
      queryParams:{
        objectId,flag
      }
    });
  }
  // 卸载按钮点击事件
  unmountBtnFunc() {
    if(this.rowSelected!=null && this.rowSelected.length==1){
      this.jumpPage(this.rowSelected[0].objectid,"nfs/dataStore/unmount");
    }
  }
  // 删除按钮点击事件
  delBtnFunc() {
    const flag = 'plugin';
    const objectid=this.rowSelected[0].objectid;
    this.router.navigate(['nfs/delete'],{
      queryParams:{
        objectid,flag
      }
    })
  }
  navigateTo(objectid){
    console.log('页面跳转了');
    console.log(objectid);
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'vsphere.core.datastore.summary',
      objectId: objectid
    });
  }

  formatCapacity(c: number){
    if (c < 1024){
      return c.toFixed(3)+" GB";
    }else if(c >= 1024 && c< 1048576){
      return (c/1024).toFixed(3) +" TB";
    }else if(c>= 1048576){
      return (c/1024/1024).toFixed(3)+" PB"
    }
  }
  // 点刷新那个功能是分两步，一步是刷新，然后等我们这边的扫描任务，任务完成后返回你状态，任务成功后，你再刷新列表页面。
  scanDataStore() {
    this.isLoading = true;
    this.vmfsListService.scanVMFS('nfs').subscribe((res: any) => {
      this.isLoading = false;
      if (res.code === '200') {
        this.getNfsList();
        console.log('Scan success');
        this.router.navigate(['nfs'], {
          queryParams: {t: new Date().getTime()}
        });
      } else {
        console.log('Scan faild');
        this.router.navigate(['nfs'], {
          queryParams: {t: new Date().getTime()}
        });
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

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

  backToNfsList(){
    this.modalLoading=false;
    this.confirmActResult();
  }
  checkHost(){
    this.modalLoading=true;
    this.addForm.vkernelIp = undefined;
    //选择主机后获取虚拟网卡
    this.addService.getVmkernelListByObjectId(this.addForm.hostObjectId)
      .subscribe((r: any) => {
        this.modalLoading=false;
        if (r.code === '200'){
          this.vmkernelList = r.data;
          if (this.vmkernelList && this.vmkernelList.length > 0) {
            this.addForm.vkernelIp=this.vmkernelList[0].ipAddress;
          }
        }
        this.cdr.detectChanges();
      });
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

  /**
   * 确认关闭窗口
   */
  confirmActResult() {
    this.addModelShow = false;
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
