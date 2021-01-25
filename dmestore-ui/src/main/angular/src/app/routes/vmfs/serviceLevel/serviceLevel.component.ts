import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ServiceLevelService} from './serviceLevel.service';
import {ActivatedRoute, Router} from '@angular/router';
import {GetForm, ServiceLevelList, VmfsInfo, VmfsListService} from '../list/list.service';
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './serviceLevel.component.html',
  styleUrls: ['./serviceLevel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [ServiceLevelService],
})
export class ServiceLevelComponent implements OnInit{

  constructor(private remoteSrv: ServiceLevelService, private route: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }

  // 未选择服务等级true未选择 false选择
  serviceLevelIsNull = false;
  // 服务等级列表
  serviceLevelList: ServiceLevelList[] = [];

  // 变更服务等级
  changeServiceLevelForm = new GetForm().getChangeLevelForm();

  // 服务器/集群ID
  objectId;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作
  resource;

  // vmfs数据
  vmfsInfo: VmfsInfo;

  // 变更服务等级 隐藏/展示
  changeServiceLevelShow = false;
  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  isOperationErr = false; // 错误信息
  changeServiceLevelSuccessShow = false; // 变更服务等级成功

  ngOnInit(): void {
    this.changeServiceLevelShow = false;
    this.modalLoading = true;
    this.modalHandleLoading = false;
    this.isOperationErr = false;

    // 初始化表单
    this.changeServiceLevelForm = new GetForm().getChangeLevelForm();
    this.route.url.subscribe(url => {
      console.log('url', url);
      this.route.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        if (this.resource === 'list') {// 以列表为入口
          this.objectId = queryParam.objectId;
        } else { // 以dataStore为入口
          const ctx = this.globalsService.getClientSdk().app.getContextObjects();
          console.log('ctx', ctx);
          this.objectId = ctx[0].id;

        }

        // todo 获取vmfs数据
        this.remoteSrv.getStorageById(this.objectId).subscribe((result: any) => {
          if (result.code === '200' && null != result.data) {
            // 表单数据初始化
            const volumeIds = [];
            volumeIds.push(result.data.volumeId);
            this.changeServiceLevelForm.volume_ids = volumeIds;
            this.changeServiceLevelForm.ds_name = result.data.storeName;
          }
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
      });
    });
    // 初始化dataStore数据
    this.getServiceLevels();
  }

  /**
   * 获取服务等级数据
   */
  getServiceLevels() {
    // 初始化服务等级选择参数
    this.serviceLevelIsNull = false;
    // 获取服务等级数据
    this.remoteSrv.getServiceLevelList().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.filter(item => item.totalCapacity !== 0);
        console.log('this.serviceLevelList', this.serviceLevelList);
      }
      this.changeServiceLevelShow = true;
      this.modalLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }
  /**
   * 取消
   */
  cancel() {
    this.changeServiceLevelShow = false;
    if (this.resource === 'list') { // 列表入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  /**
   * 服务登记变更处理
   */
  changeSLHandleFunc() {
    const selectResult = this.serviceLevelList.find(item => item.show === true);
    console.log('selectResult', selectResult);
    if (selectResult && selectResult.totalCapacity !== 0) {
      this.serviceLevelIsNull = false;
      this.changeServiceLevelForm.service_level_id = selectResult.id;
      this.changeServiceLevelForm.service_level_name = selectResult.name;

      this.modalHandleLoading = true;
      this.remoteSrv.changeServiceLevel(this.changeServiceLevelForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code === '200'){
          console.log('change service level success:' + name);
          this.changeServiceLevelSuccessShow = true;
        } else {
          console.log('change service level faild: ' + name  + ' Reason:' + result.description);
          this.isOperationErr = true;
        }
        this.cdr.detectChanges();
      });
    } else {
      this.serviceLevelIsNull = true;
      console.log('服务等级不能为空！');
    }
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
   * 确认操作结果并关闭窗口
   */
  confirmActResult() {
    this.cancel();
  }
}
