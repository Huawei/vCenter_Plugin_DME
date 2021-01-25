import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ExpandService} from './expand.service';
import {ActivatedRoute, Router} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsInfo, VmfsListService} from '../list/list.service';
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './expand.component.html',
  styleUrls: ['./expand.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ExpandService],
})
export class ExpandComponent implements OnInit{

  constructor(private remoteSrv: ExpandService, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }

  // 服务器/集群ID
  objectId;

  // 扩容form
  expandForm = new GetForm().getExpandForm();

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // vmfs数据
  vmfsInfo: VmfsInfo;

  // 弹窗隐藏/显示
  expandShow = false;
  isOperationErr = false; // 错误信息
  capacityErr = false; // 扩容容量错误信息
  modalHandleLoading = false; // 数据处理loading
  modalLoading = false; // 数据加载loading
  expandErr = false; // 扩容容量错误信息
  expandSuccessShow = false; // 扩容成功提示

  ngOnInit(): void {
    this.initData();
  }

  /**
   * 初始化数据
   */
  initData() {
    this.expandShow = true;
    this.modalHandleLoading = false;
    this.modalLoading = true;
    this.expandErr = false;
    // 设备类型 操作类型初始化
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        if (this.resource === 'list') {
          this.objectId = queryParam.objectId;
        } else {
          const ctx = this.globalsService.getClientSdk().app.getContextObjects();
          this.objectId = ctx[0].id;
        }
        this.remoteSrv.getStorageById(this.objectId).subscribe((result: any) => {
          console.log('VmfsInfo:', result);
          if (result.code === '200' && null != result.data) {
            // form表单数据初始化
            this.expandForm.volume_id = result.data.volumeId;
            this.expandForm.ds_name = result.data.storeName;
          }
          this.modalLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    });
    // 初始化form表单
    this.expandForm = new GetForm().getExpandForm();
  }

  /**
   * 取消/关闭页面
   */
  cancel() {
    this.expandShow = false;
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  /**
   * 扩容
   */
  expandHandleFunc() {
    if (this.expandForm.vo_add_capacity) {
      // 容量单位转换
      switch (this.expandForm.capacityUnit) {
        case 'TB':
          this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity * 1024;
          break;
        case 'MB':
          this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity / 1024;
          break;
        // case 'KB':
        //   this.expandForm.vo_add_capacity = this.expandForm.vo_add_capacity / (1024 * 1024);
        //   break;
        default: // 默认GB 不变
          break;
      }
      this.expandForm.capacityUnit = 'GB';
      this.expandForm.obj_id = this.objectId;
      this.modalHandleLoading = true;
      // 参数封装
      this.remoteSrv.expandVMFS(this.expandForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code === '200'){
          console.log('expand success:' + name);
          this.expandSuccessShow = true; // 扩容成功提示
        }else {
          console.log('expand: ' + name  + ' Reason:' + result.description);
          // 错误信息 展示
          this.isOperationErr = true;
        }
        this.cdr.detectChanges();

      });
    }
  }

  /**
   * 扩容容量校验
   */
  expandOnblur() {
    let expand = this.expandForm.vo_add_capacity;
    console.log('expand', expand);
    if (expand && expand !== null && expand !== '') {
      if (expand > 0) {
        switch (this.expandForm.capacityUnit) {
          case 'TB':
            if ((expand*1024).toString().indexOf(".")!==-1) { // 小数
              this.expandErr = true;
              expand = '';
            } else {
              this.expandErr = false;
            }
            break;
          default: // 默认GB 不变
            if (expand.toString().indexOf(".")!==-1) { // 小数
              this.expandErr = true;
              expand = '';
            } else {
              this.expandErr = false;
            }
            break;
        }
      } else {
        this.expandErr = true;
        expand = '';
      }
    } else {
      expand = '';
    }
    console.log('expand2', expand);
    console.log('this.expandErr', this.expandErr);
    this.expandForm.vo_add_capacity = expand;
  }
  /**
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    this.cancel();
  }
}
