import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsModifyService} from "./nfs-modify.service";
import {UpdateNfs} from "../../nfs.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-modify.component.html',
  styleUrls: ['./nfs-modify.component.scss'],
  providers: [NfsModifyService]
})
export class NfsModifyComponent implements OnInit{
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  viewPage: string;
  pluginFlag: string;
  objectId: string;
  updateNfs=new UpdateNfs();
  errorMsg: string;

  maxbandwidthChoose=false; // 最大带宽 选中
  maxiopsChoose=false; // 最大iops 选中
  minbandwidthChoose=false; // 最小带宽 选中
  miniopsChoose=false; // 最小iops 选中
  latencyChoose=false; // 时延 选中
  modifySuccessShow = false; // 编辑程功窗口

  constructor(private modifyService: NfsModifyService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {
    this.modalLoading=true;
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.pluginFlag =queryParam.flag;
      this.objectId =queryParam.objectid;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      this.objectId=ctx[0].id;
    }
    this.modifyService.getNfsDetailById(this.objectId).subscribe((result: any) => {
      if (this.pluginFlag){
        this.viewPage='modify_plugin'
      }else{
        this.viewPage='modify_vcenter'
      }
      if (result.code === '200'){
        this.updateNfs=result.data;
        this.updateNfs.sameName=true;
        this.updateNfs.dataStoreObjectId=this.objectId;
        this.oldNfsName=this.updateNfs.nfsName;
        this.oldFsName=this.updateNfs.fsName;
        this.oldShareName=this.updateNfs.shareName
        this.modalLoading=false;
      }
    });
  }
  modifyCommit(){
    this.modalHandleLoading=true;
    console.log(this.updateNfs);
    if (this.updateNfs.sameName){
      this.updateNfs.fsName=this.updateNfs.nfsName;
    }
    this.qosFunc(this.updateNfs);
    this.modifyService.updateNfs(this.updateNfs).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        this.modifySuccessShow = true;
      }else{
        this.errorMsg = '1';
        console.log("Delete failed:",result.description)
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
          objVal = this.updateNfs.maxBandwidth;
          break;
        case 'maxiops':
          objVal = this.updateNfs.maxIops;
          break;
        case 'minbandwidth':
          objVal = this.updateNfs.minBandwidth;
          break;
        case 'miniops':
          objVal = this.updateNfs.minIops;
          break;
        default:
          objVal = this.updateNfs.latency;
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
          this.updateNfs.maxBandwidth = objVal;
          break;
        case 'maxiops':
          this.updateNfs.maxIops = objVal;
          break;
        case 'minbandwidth':
          this.updateNfs.minBandwidth = objVal;
          break;
        case 'miniops':
          this.updateNfs.minIops = objVal;
          break;
        default:
          this.updateNfs.latency = objVal;
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
    if(this.updateNfs.nfsName==null) return false;
    if(this.oldNfsName==this.updateNfs.nfsName) return false;
    this.oldNfsName=this.updateNfs.nfsName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');
    if(reg5.test(this.updateNfs.nfsName)){
      //验证重复
      this.matchErr=false;
      if (this.updateNfs.sameName){
        this.checkNfsNameExist(this.updateNfs.nfsName);
        this.checkShareNameExist(this.updateNfs.nfsName);
        this.checkFsNameExist(this.updateNfs.nfsName);
      }else{
        this.checkNfsNameExist(this.updateNfs.nfsName);
      }
    }else{
      //
      this.matchErr=true;
      //不满足的时候置空
      this.updateNfs.nfsName=null;
      console.log('验证不通过');
    }
  }
  checkShareName(){
    if(this.updateNfs.shareName==null) return false;
    if(this.oldShareName=this.updateNfs.shareName) return false;

    this.oldShareName=this.updateNfs.shareName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');
    if(reg5.test(this.updateNfs.shareName)){
      //验证重复
      this.matchErr=false;
      this.checkShareNameExist(this.updateNfs.shareName);
    }else{
      this.matchErr=true;
      this.updateNfs.shareName=null;
    }
  }
  checkFsName(){
    if(this.updateNfs.fsName==null) return false;
    if(this.oldFsName=this.updateNfs.fsName) return false;

    this.oldFsName=this.updateNfs.fsName;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');
    if(reg5.test(this.updateNfs.fsName)){
      //验证重复
      this.matchErr=false;
      this.checkShareNameExist(this.updateNfs.fsName);
    }else{
      this.matchErr=true;
      this.updateNfs.fsName=null;
    }
  }
  checkNfsNameExist(name:string){
    this.modifyService.checkNfsNameExist(name).subscribe((r:any)=>{
      if (r.code=='200'){
        if(r.data){
          this.nfsNameRepeatErr=false;
        }else{
          this.nfsNameRepeatErr=true;
          this.updateNfs.nfsName=null;
        }
      }
    });
  }
  checkShareNameExist(name:string){
    this.modifyService.checkShareNameExist(name).subscribe((r:any)=>{
      if (r.code=='200'){
        if(r.data){
          this.shareNameRepeatErr=false;
        }else{
          this.shareNameRepeatErr=true;
          this.updateNfs.nfsName=null;
        }
      }
    });
  }
  checkFsNameExist(name:string){
    this.modifyService.checkFsNameExist(name).subscribe((r:any)=>{
      if (r.code=='200'){
        if(r.data){
          this.shareNameRepeatErr=false;
        }else{
          this.shareNameRepeatErr=true;
          this.updateNfs.nfsName=null;
        }
      }
    });
  }

  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
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
