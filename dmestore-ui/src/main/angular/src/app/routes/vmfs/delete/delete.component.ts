import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit, ViewChild} from "@angular/core";
import {ActivatedRoute, Router} from '@angular/router';
import {DeleteService} from "../delete/delete.service";
import {GlobalsService} from "../../../shared/globals.service";
import {ClrModal, ClrWizard} from "@clr/angular";

@Component({
  selector: 'app-list',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DeleteService],
})
export class DeleteComponent implements OnInit{

  constructor(private remoteSrv: DeleteService, private route: ActivatedRoute, private cdr: ChangeDetectorRef, private router:Router, private globalsService: GlobalsService) {

  }

  // 存储ID
  objectId;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // 待删除VMFS的卷ID
  objectIds = [];

  delShow = false;
  delSuccessShow = false; // 删除提示窗口
  modalHandleLoading = false; // 数据处理loading
  isOperationErr = false; // 错误信息

  ngOnInit(): void {
    this.initData();
  }

  initData() {
    this.delShow = true;
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        let objIds = [];
        if (this.resource === 'list') { // 列表入口
          objIds = queryParam.objectId.split(',');
        } else { // dataStore入口
          const ctx = this.globalsService.getClientSdk().app.getContextObjects();
          objIds.push(ctx[0].id);
        }
        this.objectIds = objIds;
        console.log('del vmfs objectIds:' + objIds);
        console.log('this.resource', this.resource);
      });
    });

  }

  /**
   * 取消
   */
  cancel() {

    this.delShow = false;
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  // 删除VMFS 处理函数
  delHandleFunc() {
    const delInfos = {
      dataStoreObjectIds: this.objectIds
    }
    this.modalHandleLoading = true;
    this.remoteSrv.delVmfs(delInfos).subscribe((result: any) => {
      // 隐藏删除提示页面
      this.modalHandleLoading = false;
      if (result.code === '200'){
        console.log('DEL success');
        // 删除成功提示
        this.delSuccessShow = true;
      } else {
        console.log('DEL faild: ' + result.description);
        this.isOperationErr = true;
      }
      this.cdr.detectChanges();
    });
  }

  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    this.cancel();
  }

}
