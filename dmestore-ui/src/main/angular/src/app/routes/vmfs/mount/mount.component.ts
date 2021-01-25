import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {
  ClusterList,
  GetForm,
  HostList,
  HostOrCluster,
  ServiceLevelList,
  VmfsInfo,
  VmfsListService
} from '../list/list.service';
import {DataStore, MountService} from "./mount.service";
import {GlobalsService} from "../../../shared/globals.service";

@Component({
  selector: 'app-list',
  templateUrl: './mount.component.html',
  styleUrls: ['./mount.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [MountService],
})
export class MountComponent implements OnInit{

  constructor(private remoteSrv: MountService, private activatedRoute: ActivatedRoute, private cdr: ChangeDetectorRef,
              private router:Router, private globalsService: GlobalsService) {

  }
  // dataStore数据
  dataStores: DataStore[] = [];
  // 选择DataStore
  chooseMountDataStore: DataStore[] = [];
  // mountForm:
  // 设备类型 host 主机、cluster 集群
  dataType;
  // 操作类型 mount挂载、unmount卸载
  operationType;
  // 数据加载
  isLoading = true;
  // 未选择挂载的DataStore
  notChooseMountDevice = false;
  // 未选择卸载的DataStore
  notChooseUnmountDevice = false;
  // 挂载的form
  mountForm = new GetForm().getMountForm();
  // 以list/DataStore 为入口 挂载展示/隐藏
  mountShow = false;

  // 以主机集群为入口的挂载/卸载
  hostMountShow = false;

  // 服务器/集群ID
  hostOrClusterId;
  // objectid
  objectId;

  // 操作来源 list:列表页面、dataStore：在DataStore菜单页面操作、others:在主机或集群为入口
  resource;

  // 以列表或者dataStore为入口
  chooseHost: HostList; // 已选择的主机
  mountHostData = true; // 挂载页面主机是否加载完毕 true是 false否
  hostList: HostList[] = []; // 挂载页面 主机列表
  mountClusterData = true; // 挂载页面集群是否加载完毕 true是 false否
  clusterList: ClusterList[] = []; // 挂载页面集群列表
  chooseCluster: ClusterList; // 已选择的集群

  // 卸载
  unmountShow = false; // 卸载窗口
  unmountForm = new GetForm().getUnmountForm(); // 卸载form
  chooseUnmountHost: HostOrCluster = null; // 已选择卸载的主机
  chooseUnmountCluster: HostOrCluster = null; // 已选择卸载的集群
  mountedHost: HostOrCluster[] = []; // 已挂载的主机
  mountedCluster: HostOrCluster[] = []; // 已挂载的集群
  mountSuccessShow = false; // 挂载成功窗口
  unmountSuccessShow = false; // 卸载窗口
  // vmfs数据
  vmfsInfo = {
    name: ''
  };

  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  isOperationErr = false; // 错误信息

  ngOnInit(): void {
    // 初始化隐藏窗口
    this.unmountShow = false;
    this.mountShow = false;
    this.hostMountShow = false;
    this.modalLoading = true;
    this.modalHandleLoading = false;
    this.isOperationErr = false;
    this.initData();
  }

  initData() {
    // 设备类型 操作类型初始化
    this.activatedRoute.url.subscribe(url => {
      console.log('url', url);
      if (url.length > 1) {
        this.dataType = url[0] + '';
        this.operationType = url[1] + '';
      } else {
        this.operationType = url[0] + '';
      }
      this.activatedRoute.queryParams.subscribe(queryParam => {
        this.resource = queryParam.resource;
        const ctx = this.globalsService.getClientSdk().app.getContextObjects();
        if (this.resource !== 'others') { // 以列表/dataStore 为入口
          if (this.resource === 'list') {// 以列表为入口
            this.objectId = queryParam.objectId;
          } else { // 以dataStore为入口
            this.objectId = ctx[0].id;
          }
          if (this.operationType === 'mount') {
            this.mountShow = true;
          } else {
            this.unmountShow = true;
          }

          // 获取vmfs数据
          this.remoteSrv.getVmfsById(this.objectId)
            .subscribe((result: any) => {
              console.log('result:', result);
              if (result.code === '200' && null != result.data) {
                this.vmfsInfo = result.data.filter(item => item.objectid === this.objectId)[0];
              }
              console.log('this.vmfsInfo ', this.vmfsInfo );

              this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
            });

        } else { // 以集群为入口
          this.hostOrClusterId = ctx[0].id;
          this.hostMountShow = true;
          console.log("this.hostMountShow", this.hostMountShow)
        }

        this.cdr.detectChanges();
      });

    });
    console.log('this.objectId', this.objectId);
    console.log('this.hostOrClusterId', this.hostOrClusterId);
    // 数据初始化
    this.getDataStore();
  }
  /**
   * 挂载：初始化dataStore
   */
  getDataStore() {
    // 初始化挂载form
    // 初始化dataStore
    this.dataStores = [];
    console.log('this.dataType', this.dataType === 'host');
    console.log('this.operationType', this.operationType === 'unmount');
    console.log('this.resource', this.resource === 'others');

    // 初始化挂载/卸载 form
    if (this.operationType === 'mount') {
      this.mountForm = new GetForm().getMountForm();
      if (this.dataType === 'host') { // 主机
        this.mountForm.mountType = '1';
      } else { // 集群
        this.mountForm.mountType = '2';
      }
    } else {
      this.unmountForm = new GetForm().getUnmountForm();
      if (this.dataType === 'host') { // 主机
        this.unmountForm.mountType = '1';
      } else { // 集群
        this.unmountForm.mountType = '2';
      }
    }
    // 挂载、卸载 数据初始化
    if (this.resource === 'others') { // 以主机/集群为入口
      if (this.operationType === 'mount') {
        this.mountDataStore();
      } else {
        this.unmountDataStore();
      }
    } else { // 以列表/dataStore为入口
      if (this.operationType === 'mount') { // 挂载

        // 初始化主机
        this.mountHostData = false;
        this.hostList = [];

        this.chooseCluster = undefined;
        this.chooseHost =  undefined;
        this.initMountHost();

      } else { // 卸载

        this.isLoading = true;
        console.log('this.unmountShow', this.unmountShow);
        // 初始化卸载 页面未选择设备 提示数据展示
        this.notChooseUnmountDevice = false;
        // 初始话已选择数据
        this.chooseUnmountCluster = null;
        this.chooseUnmountHost = null;
        // 获取主机
        this.remoteSrv.getMountHost(this.objectId).subscribe((result: any) => {
          console.log(result);
          if (result.code === '200' && result.data !== null && result.data.length >= 1) {
            this.unmountForm.mountType = '1';
            const mountHost: HostOrCluster [] = [];
            result.data.forEach(item => {
              const hostInfo = {
                deviceId: item.hostId,
                deviceName: item.hostName,
                deviceType: 'host'
              };
              mountHost.push(hostInfo);
            });
            this.mountedHost = mountHost;
          }
          // 获取集群
          this.remoteSrv.getMountCluster(this.objectId).subscribe((result: any) => {
            console.log(result);
            if (result.code === '200' && result.data !== null && result.data.length >= 1) {
              this.unmountForm.mountType = '2';
              const mountCluster: HostOrCluster [] = [];
              result.data.forEach(item => {
                const hostInfo = {
                  deviceId: item.hostGroupId,
                  deviceName: item.hostGroupName,
                  deviceType: 'cluster'
                };
                mountCluster.push(hostInfo);
              });
              this.mountedCluster = mountCluster;
            }
            this.modalLoading = false;
            this.isLoading = false;
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
      }
    }
  }

  /**
   * 主机/集群入口 挂载数初始化
   */
  mountDataStore() {
    switch (this.dataType) {
      case 'host':
        // 设置主机相关参数
        this.mountForm.hostId = this.hostOrClusterId;

        // 获取dataStore
        this.remoteSrv.getDataStoreByHostId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log('mountHostData:', result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
          this.isLoading = false;
          this.modalLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        break;
      case 'cluster':
        // 设置集群相关参数
        this.mountForm.clusterId = this.hostOrClusterId;

        // 获取dataStore
        this.remoteSrv.getDataStoreByClusterId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log('mountHostData:', result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
          this.isLoading = false;
          this.modalLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        break;
      default:
        break;
    }
  }

  /**
   * 主机/集群入口 卸载数据初始化
   */
  unmountDataStore() {

    switch (this.dataType) {
      case 'host':
        this.isLoading = false;
        // 获取dataStore
        this.remoteSrv.getMountedByHostObjId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log('mountedHostData:', result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
          this.isLoading = false;
          this.modalLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        break;
      case 'cluster':
        this.isLoading = false;
        this.remoteSrv.getMountedByClusterObjId(this.hostOrClusterId, 'VMFS').subscribe((result: any) => {
          console.log('mountedClusterData:', result);
          if (result.code === '200' && null != result.data) {
            this.dataStores = result.data;
          }
          this.isLoading = false;
          this.modalLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        break;
      default:
        break;
    }
  }

  /**
   * 表单提交（挂载/卸载）
   */
  mountOrUnmountFunc() {
    if (this.resource === 'others') {
      if (this.operationType === 'unmount') {
        this.unMountHandleFunc();
      } else {
        console.log("开始挂载");
        this.mountHandleFunc();
      }
    } else {
      if (this.operationType === 'unmount') {
        this.unmountHandleFunc();
      } else {
        this.mountSubmit();
      }
    }
  }

  /**
   * 取消/关闭函数
   */
  cancel() {
    console.log("关闭页面");
    // dataStore/列表入口 窗口隐藏
    this.hostMountShow = false;
    this.mountShow = false;
    this.unmountShow = false;
    if (this.resource !== 'others') {
      if (this.operationType === 'mount') {
        this.mountShow = false;
      } else {
        this.unmountShow = false;
      }
    }
    // 父窗口关闭
    if (this.resource === 'list') { // 主机/集群入口
      this.router.navigate(['vmfs/list']);
    } else { // dataStore/列表入口
      this.globalsService.getClientSdk().modal.close();
    }
  }

  /**
   * 主机/集群入口 挂载处理
   */
  mountHandleFunc() {
    if (this.chooseMountDataStore.length < 1) {
      this.notChooseMountDevice = true;
    } else {
      this.notChooseMountDevice = false;
      const dataStoreObjectIds = [];
      this.chooseMountDataStore.forEach(item => {
        dataStoreObjectIds.push(item.objectId);
      });
      this.mountForm.dataStoreObjectIds = dataStoreObjectIds;

      this.modalHandleLoading = true;
      console.log('开始挂载。。。。');
      this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        console.log("result:", result)
        if (result.code  ===  '200'){
          console.log('挂载成功');
          this.mountSuccessShow = true;
        } else {
          console.log('挂载异常：' + result.description);
          this.isOperationErr = true;
        }
        this.cdr.detectChanges();
      });
    }
  }

  /**
   * 主机/集群入口 卸载处理
   */
  unMountHandleFunc() {
    if (this.chooseMountDataStore.length < 1) {
      this.notChooseUnmountDevice = true;
    } else {
      this.notChooseUnmountDevice = false;

      if (this.unmountForm.mountType === '1') {
        this.unmountForm.hostId = this.hostOrClusterId;
      } else {
        this.unmountForm.clusterId = this.hostOrClusterId;
      }
      const unmountObjIds = this.chooseMountDataStore.map(item => item.objectId);
      this.unmountForm.dataStoreObjectIds = unmountObjIds;
      this.modalHandleLoading = true;
      this.remoteSrv.unmountVMFS(this.unmountForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code === '200'){
          console.log('unmount  success');
          this.unmountSuccessShow = true;
        } else {
          console.log('unmount  fail：' + result.description);
          this.isOperationErr = true;
        }
        this.cdr.detectChanges();
      });
    }
  }

  // 挂载 主机数据初始化
  initMountHost() {
    return new Promise((resolve, reject) => {
      // 获取服务器 通过ObjectId过滤已挂载的服务器
      this.remoteSrv.getHostListByObjectId(this.objectId).subscribe((result: any) => {
        if (result.code === '200' && result.data !== null){
          result.data.forEach(item => {
            this.hostList.push(item);
          });
        }

        this.mountHostData = true;

        // 初始化集群
        this.mountClusterData = false;
        this.clusterList = [];

        this.initMountCluster().then(res => {
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });

        this.cdr.detectChanges();
      });
    });
  }
  // 挂载  集群数据初始化
  initMountCluster() {
    return new Promise((resolve, reject) => {
      // 获取集群 通过ObjectId过滤已挂载的集群
      this.remoteSrv.getClusterListByObjectId(this.objectId).subscribe((result: any) => {
        if (result.code === '200'){
          result.data.forEach(item => {
            this.clusterList.push(item);
          });
        }
        this.modalLoading = false;
        this.mountClusterData = true;
        this.cdr.detectChanges();
      });
    });
  }
  // 挂载提交
  mountSubmit(){
    // 数据封装
    if (this.mountForm.mountType === '1'){ // 服务器
      this.mountForm.hostId = this.chooseHost.hostId;
      this.mountForm.host = this.chooseHost.hostName;
    }else if (this.mountForm.mountType === '2'){ // 集群
      this.mountForm.cluster = this.chooseCluster.clusterName;
      this.mountForm.clusterId = this.chooseCluster.clusterId;
    }
    const objectIds = [];
    objectIds.push(this.objectId);
    this.mountForm.dataStoreObjectIds = objectIds;

    this.modalHandleLoading = true;
    this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code  ===  '200'){
        console.log('挂载成功');
        this.mountSuccessShow = true;
      } else {
        console.log('挂载异常：' + result.description);
        this.isOperationErr = true;
      }
      this.cdr.detectChanges();
    });
  }

  // 卸载处理函数
  unmountHandleFunc() {
    console.log('this.chooseUnmountHost', this.chooseUnmountHost);
    console.log('this.chooseUnmountCluster', this.chooseUnmountCluster);
    console.log('this.flag', (!this.chooseUnmountHost && this.unmountForm.mountType === '1') || (!this.chooseUnmountCluster && this.unmountForm.mountType === '2'));
    if ((!this.chooseUnmountHost && this.unmountForm.mountType === '1') || (!this.chooseUnmountCluster && this.unmountForm.mountType === '2')) {
      this.notChooseUnmountDevice = true;
    } else {
      this.unmountForm.dataStoreObjectIds.push(this.objectId);
      if (this.unmountForm.mountType === '1') {
        this.unmountForm.hostId = this.chooseUnmountHost.deviceId;
      } else {
        this.unmountForm.clusterId = this.chooseUnmountCluster.deviceId;
      }
      console.log('this.unmountForm', this.unmountForm);
      this.notChooseUnmountDevice = false;

      this.modalHandleLoading = true;
      this.remoteSrv.unmountVMFS(this.unmountForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code === '200'){
          console.log('unmount  success');
          this.unmountSuccessShow = true;
        } else {
          console.log('unmount  fail：' + result.description);
          this.isOperationErr = true;
        }
        this.cdr.detectChanges();
      });
    }
  }
  /**
   * 容量格式化
   * @param c 容量值
   * @param isGB true GB、false MB
   */
  formatCapacity(c: number, isGB:boolean){
    c = Number(c);
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
