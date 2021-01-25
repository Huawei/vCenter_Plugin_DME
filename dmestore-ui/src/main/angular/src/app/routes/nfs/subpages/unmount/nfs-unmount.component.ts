import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {Host, NfsUnmountService} from "./nfs-unmount.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DataStore} from "../mount/nfs-mount.service";
@Component({
  selector: 'app-unmount',
  templateUrl: './nfs-unmount.component.html',
  styleUrls: ['./nfs-unmount.component.scss'],
  providers: [NfsUnmountService]
})
export class NfsUnmountComponent implements OnInit{
  routePath: string;
  viewPage: string;
  pluginFlag: string;//来至插件的标记
  rowSelected = []; // 当前选中数据
  dataStoreList: DataStore[];
  errorMsg: string;
  hostList: Host[];
  dataStoreObjectId:string;
  hostId:string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  unmountTipsShow=false;
  unmountSuccessShow = false; // 卸载成功窗口
  constructor(private unmountService: NfsUnmountService, private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router, private cdr: ChangeDetectorRef){
  }
  ngOnInit(): void {
    this.activatedRoute.url.subscribe(url => {
      this.routePath=url[0].path;
    });
    if ("dataStore"===this.routePath){
      //入口是DataSource
      this.modalLoading=true;
      this.viewPage='unmount_pugin'
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.dataStoreObjectId = queryParam.objectId;
        this.pluginFlag =queryParam.flag;
      });
      if(this.pluginFlag==null){
        //入口来至Vcenter
        //this.dsObjectId=ctx[0].id;
        const ctx = this.gs.getClientSdk().app.getContextObjects();
        if(ctx!=null){
          this.dataStoreObjectId =ctx[0].id;
        }
        this.viewPage='unmount_vcenter'
      }
      this.getMountedHostList(this.dataStoreObjectId);
    }
    else if("host"=== this.routePath){
      this.modalLoading=true;
      this.viewPage='unmount_host';
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if (ctx!=null){
        this.hostId=ctx[0].id;
        // this.hostId='urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
        this.getDataStoreByHostId(this.hostId);
      }
      this.modalLoading=false;
    } else if ("cluster"=== this.routePath) {
      this.modalLoading=true;
      this.viewPage='unmount_host';
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if (ctx!=null){
        this.hostId=ctx[0].id;
        this.getDataStoreByClusterId(this.hostId);
      }
      this.modalLoading=false;
    }
  }
  backToNfsList(){
    this.errorMsg = null;
    this.modalLoading=false;
    this.router.navigate(['nfs']);
  }
  unMount(){
    if(this.viewPage=='unmount_vcenter'||this.viewPage=='unmount_pugin'){
      if (this.hostList.length==1){
        this.unmountTipsShow=true;
      }else{
        this.unmountHandleFunc();
      }
    }else{
      this.unmountHandleFunc();
    }
  }
  closeModel(){
    this.errorMsg = null;
    this.modalLoading=false;
    this.gs.getClientSdk().modal.close();
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
  //获取NFS下挂载的主机列表
  getMountedHostList(hostId:string){
    this.unmountService.getMountedHostList(hostId).subscribe((r: any) => {
      this.modalLoading=false;
      if (r.code=='200'){
        this.hostList=r.data;
        this.cdr.detectChanges();
      }
    });
  }
  //获取主机下一挂载的NFS列表
  getDataStoreByHostId(hostId:string){
    this.unmountService.getMountedByHostObjId(hostId, 'NFS').subscribe((r: any) => {
      this.modalLoading=false;
      if (r.code=='200'){
        r.data.forEach(item => {
          // const data = {
          //   objectId: item.
          // }
        })
        this.dataStoreList=r.data;
        this.cdr.detectChanges();
      }
    });
  }
  //获取集群下一挂载的NFS列表
  getDataStoreByClusterId(hostId:string){
    this.unmountService.getMountedByClusterObjId(hostId, 'NFS').subscribe((r: any) => {
      this.modalLoading=false;
      if (r.code=='200'){
        this.dataStoreList=r.data;
        this.cdr.detectChanges();
      }
    });
  }
  unmountHandleFunc(){
    var params={
      "dataStoreObjectId": this.dataStoreObjectId,
      "hostId":this.hostId
    };
    this.modalHandleLoading=true;
    this.unmountService.unmount(params).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
       this.unmountSuccessShow = true;
      }else{
        this.errorMsg = '1';
        console.log("unMount Failed:",result.description)
      }
      this.cdr.detectChanges();
    });
  }
  // checkHost(){
  //   this.modalHandleLoading=true;
  //   //选择主机后获取虚拟网卡
  //   this.getVmkernelListByObjectId(this.hostId);
  // }
  //
  // getVmkernelListByObjectId(hostObjectId:string){
  //   this.unmountService.getVmkernelListByObjectId(hostObjectId)
  //     .subscribe((r: any) => {
  //       this.modalHandleLoading=false;
  //       if (r.code === '200'){
  //         this.dataStoreList = r.data;
  //         this.cdr.detectChanges();
  //       }
  //     });
  // }

  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    if(this.pluginFlag=='plugin'){
      this.backToNfsList();
    }else{
      this.closeModel();
    }
  }
}
