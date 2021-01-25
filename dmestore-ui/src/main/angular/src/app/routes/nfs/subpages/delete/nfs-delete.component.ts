import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {GlobalsService} from "../../../../shared/globals.service";
import {ActivatedRoute, Router} from "@angular/router";
import {NfsDeleteService} from "./nfs-delete.service";

@Component({
  selector: 'app-add',
  templateUrl: './nfs-delete.component.html',
  styleUrls: ['./nfs-delete.component.scss'],
  providers: [NfsDeleteService]
})
export class NfsDeleteComponent implements OnInit{
  viewPage: string;
  pluginFlag: string;//来至插件的标记
  dataStoreObjectId:string;
  errorMsg: string;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  delSuccessShow = false; // 删除提示窗口
  constructor(private deleteService: NfsDeleteService, private cdr: ChangeDetectorRef,
              private gs: GlobalsService,
              private activatedRoute: ActivatedRoute,private router:Router){
  }
  ngOnInit(): void {

    //入口是DataSource
    this.viewPage='delete_plugin'
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.dataStoreObjectId = queryParam.objectid;
      this.pluginFlag =queryParam.flag;
    });
    if(this.pluginFlag==null){
      //入口来至Vcenter
      const ctx = this.gs.getClientSdk().app.getContextObjects();
      if(ctx!=null){
        this.dataStoreObjectId=ctx[0].id;
      }
      this.viewPage='delete_vcenter'
    }
  }
  delNfs(){
    this.modalHandleLoading=true;
    var params={
      "dataStoreObjectId": this.dataStoreObjectId
    };
    this.deleteService.delNfs(params).subscribe((result: any) => {
      this.modalHandleLoading=false;
      if (result.code === '200'){
        // 删除成功提示
        this.delSuccessShow = true;
      }else{
        this.errorMsg = '1';
        console.log("Delete failed:",result.description)
      }
      this.cdr.detectChanges();
    });
  }

  backToNfsList(){
    this.modalHandleLoading=false;
    this.router.navigate(['nfs']);
  }

  closeModel(){
    this.modalHandleLoading=false;
    this.gs.getClientSdk().modal.close();
  }

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
