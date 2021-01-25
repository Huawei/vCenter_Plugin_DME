import {
  Component,
  OnInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef, ElementRef, ViewChild
} from '@angular/core';
import {
  VmfsListService,
  VmfsInfo,
  StorageList,
  StoragePoolList,
  HostList,
  ClusterList,
  ServiceLevelList, HostOrCluster, GetForm, Workload, StoragePoolMap,
} from './list.service';
import {ClrWizard, ClrWizardPage} from '@clr/angular';
import {GlobalsService} from '../../../shared/globals.service';
import {Router} from "@angular/router";
import {DeviceFilter, ProtectionStatusFilter, ServiceLevelFilter, StatusFilter} from "./filter.component";
import {FormControl, FormGroup, Validators} from "@angular/forms";


@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [VmfsListService],
})
export class VmfsListComponent implements OnInit {
  get params() { // 对query进行处理
    const p = Object.assign({}, this.query);
    return p;
  }

  constructor(private remoteSrv: VmfsListService, private cdr: ChangeDetectorRef,
              public gs: GlobalsService, private router:Router) {}
  // 添加页面窗口
  @ViewChild('wizard') wizard: ClrWizard;
  @ViewChild('addPageOne') addPageOne: ClrWizardPage;
  @ViewChild('addPageTwo') addPageTwo: ClrWizardPage;

  @ViewChild('statusFilter') statusFilter:StatusFilter;
  @ViewChild('deviceFilter') deviceFilter:DeviceFilter;
  @ViewChild('serviceLevelFilter') serviceLevelFilter: ServiceLevelFilter;
  @ViewChild('protectionStatusFilter') protectionStatusFilter:ProtectionStatusFilter;

  expendActive = false; // 示例
  list: VmfsInfo[] = []; // 数据列表
  radioCheck = 'list'; // 切换列表页显示
  levelCheck = 'level'; // 是否选择服务等级：level 选择服务器等级 customer 未选择服务等级
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  wwns = []; // wwn 用来查询chart data
  query = { // 查询数据
    q: 'user:VMware',
    sort: 'stars',
    order: 'desc',
    page: 0,
    per_page: 5,
  };

  modifyShow = false;
  modifySuccessShow = false; // 编辑程功窗口

  popListShow = false; // 添加弹出层显示
  addSuccessShow = false; // 添加成功弹窗
  // 添加表单数据
  form = new GetForm().getAddForm();
  addForm = new FormGroup({
    name: new FormControl('',
      Validators.required),
    isSameName: new FormControl(true,
      Validators.required),
    volumeName: new FormControl('',
      ),
    version: new FormControl('5',
      Validators.required),
    capacity: new FormControl('',
      Validators.required),
    capacityUnit: new FormControl('GB',
      Validators.required),
    count: new FormControl('',
      Validators.required),
    blockSize: new FormControl('',
      Validators.required),
    spaceReclamationGranularity: new FormControl('',
      Validators.required),
    spaceReclamationPriority: new FormControl('',
      Validators.required),
    chooseDevice: new FormControl('',
      Validators.required)
  });
  // 编辑form提交数据
  modifyForm = new GetForm().getEditForm();
  modifyNameChanged = false;
  // 扩容form
  expandForm = new GetForm().getExpandForm();
  // 变更服务等级
  changeServiceLevelForm = new GetForm().getChangeLevelForm();
  storageList: StorageList[] = []; // 存储数据
  storagePoolList: StoragePoolList[] = []; // 存储池ID
  storagePoolMap:StoragePoolMap[] = [];
  storage:StorageList; // 存储详情（编辑页面使用参数）

  workloads:Workload[] = []; // Workload
  blockSizeOptions = []; // 块大小选择
  srgOptions = []; // 空间回收粒度初始化
  deviceList: HostOrCluster[] = []; // 主机AND集群
  chooseDevice: HostOrCluster; // 已选择的主机/集群

  serviceLevelList: ServiceLevelList[] = []; // 服务等级列表
  mountShow = false; // 挂载窗口
  mountSuccessShow = false; // 挂载成功窗口
  delShow = false; // 删除窗口
  delSuccessShow = false; // 删除成功窗口
  unmountShow = false; // 卸载窗口
  unmountSuccessShow = false; // 卸载窗口
  unmountTipsShow = false; // 卸载窗口
  reclaimShow = false; // 空间回收窗口
  reclaimSuccessShow = false; // 空间回收成功窗口
  changeServiceLevelShow = false; // 变更服务等级
  changeServiceLevelSuccessShow = false; // 变更服务等级成功
  expandShow = false; // 扩容
  expandSuccessShow = false; // 扩容成功提示
  hostList: HostList[] = []; // 挂载页面 主机列表
  clusterList: ClusterList[] = []; // 挂载页面集群列表
  // 挂载form表单
  mountForm = new GetForm().getMountForm();
  chooseHost: HostList; // 已选择的主机
  chooseCluster: ClusterList; // 已选择的集群

  chooseUnmountHost: HostOrCluster = null; // 已选择卸载的主机
  chooseUnmountCluster: HostOrCluster = null; // 已选择卸载的集群
  mountedHost: HostOrCluster[] = []; // 已挂载的主机
  mountedCluster: HostOrCluster[] = []; // 已挂载的集群
  storageType = 'vmfs'; // 扫描类型（扫描接口）
  unmountForm = new GetForm().getUnmountForm(); // 卸载form
  notChooseUnmountDevice = false; // 卸载页面未选择设备 提示信息展示 true：展示 false:影藏

  isServiceLevelData = true; // 编辑页面 所选数据是否为服务等级 true:是 false:否 若为是则不显示控制策略以及交通管制对象
  mountHostData = true; // 挂载页面主机是否加载完毕 true是 false否
  mountClusterData = true; // 挂载页面集群是否加载完毕 true是 false否
  serviceLevelIsNull = false; // 未选择服务等级true未选择 false选择 添加、服务登记变更

  modalLoading = false; // 数据加载loading
  modalHandleLoading = false; // 数据处理loading
  isOperationErr = false; // 错误信息
  isReclaimErr = false; // 错误信息
  nameChecking = false; // 名称校验
  capacityErr = false; // 容量错误信息
  expandErr = false; // 扩容容量错误信息
  mountErr = false; // 扩容容量错误信息

  matchErr = false; // 名称校验 是否只由字母与数字组成 true：是 false 否
  vmfsNameRepeatErr = false; // vmfs名称是否重复 true：是 false 否
  volNameRepeatErr = false; // Vol名称是否重复 true：是 false 否

  showLowerFlag = false;
  showSmartTierFlag = false;
  showAlloctypeThick = false; // 资源调优option全部展示
  showWorkLoadFlag = false; // 应用类型展示
  latencyIsSelect = false; // 时延为下拉框

  isFirstLoadChartData = true;


  ngOnInit() {
    // 列表数据
    this.refresh();
  }
  // 修改
  modifyBtnClick() {
    console.log('this.rowSelected[0]', this.rowSelected[0]);
    // 名称变化校验初始化
    this.modifyNameChanged = false;
    // 初始化form
    this.modifyForm = new GetForm().getEditForm();
    console.log('this.rowSelected[0]', this.modifyForm );
    if (this.rowSelected.length === 1) {
      this.modalLoading = false;
      this.modalHandleLoading = false;
      this.isOperationErr = false;
      // 名称错误提示初始化
      this.vmfsNameRepeatErr = false;
      this.volNameRepeatErr = false;
      this.matchErr = false;

      this.modifyForm.name = this.rowSelected[0].name;
      this.modifyForm.oldDsName = this.rowSelected[0].name;
      this.modifyForm.volumeId = this.rowSelected[0].volumeId;
      this.modifyForm.dataStoreObjectId = this.rowSelected[0].objectid;
      // 获取存储数据
      this.storage = null;

      // 服务等级名称： 服务等级类型只能修改卷名称 非服务等级可修改卷名称+归属控制+QOS策略等
      this.modifyForm.service_level_name = this.rowSelected[0].serviceLevelName;
      console.log("this.rowSelected[0].serviceLevelName", !this.rowSelected[0].serviceLevelName)
      if (this.rowSelected[0].serviceLevelName) { // vmfs为服务等级类型
        this.isServiceLevelData = true;
        this.modifyForm.control_policy = null;
      } else {// vmfs为 自定义类型
        this.isServiceLevelData = false;

        this.modifyForm.max_iops = this.rowSelected[0].maxIops;
        this.modifyForm.max_bandwidth = this.rowSelected[0].maxBandwidth;
        this.modifyForm.min_iops = this.rowSelected[0].minIops;
        this.modifyForm.min_bandwidth = this.rowSelected[0].minBandwidth;
        // 默认隐藏smartTier
        this.showSmartTierFlag = false;
        // 默认隐藏下限
        this.showLowerFlag = false;
        // 时延默认为输入
        this.latencyIsSelect = false;

        // 获取存储数据
        this.modifyGetStorage(this.rowSelected[0].deviceId);

      }
      console.log("this.modifyForm.control_policy", this.modifyForm.control_policy)
      this.modifyShow = true;
    }
  }

  /**
   * 修改页面获取存储数据
   * @param objectId
   */
  modifyGetStorage(storageId) {
    this.modalHandleLoading = true;
    if (storageId) {
      this.remoteSrv.getStorageDetail(storageId).subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code == '200') {
          this.storage = result.data;
          const storageTypeShow = this.storage.storageTypeShow;
          if (storageTypeShow) {
            // smartTier 展示与隐藏
            const smartTierShow = storageTypeShow.smartTierShow;
            this.showSmartTierFlag = smartTierShow;
            // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
            const qosTag = storageTypeShow.qosTag;
            if (qosTag == 3) {
              this.showLowerFlag = true;
            } else {
              this.showLowerFlag = false;
            }
            this.latencyIsSelect = qosTag == 1;
          }
        }
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    }
  }

  // 修改 处理
  modifyHandleFunc() {

    // 设置修改的卷名称以及修改后的名称
    if (this.modifyForm.isSameName) {
      this.modifyForm.newVoName = this.modifyForm.name;
    } else {
      // 若不修改卷名称则将旧的卷名称设置为newVol
      this.modifyForm.newVoName = this.rowSelected[0].volumeName;
    }
    if (!this.isServiceLevelData) {
      // 控制策略若未选清空数据
      this.qosEditFunc(this.modifyForm);
      console.log("this.modifyForm.control_policyUpper == '1'", this.modifyForm.control_policyUpper == '1');
      if (this.modifyForm.control_policyUpper == '1') { // 上限+全选（上下限）
        this.modifyForm.control_policy = '1';
      } else if(this.modifyForm.control_policyLower == '0') {// 下限
        this.modifyForm.control_policy = '0';
      } else {
        this.modifyForm.control_policy = null;
      }
    }

    this.modifyForm.newDsName = this.modifyForm.name;
    this.modalHandleLoading = true;
    this.remoteSrv.updateVmfs(this.modifyForm.volumeId, this.modifyForm).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200') {
        console.log('modify success:' + this.modifyForm.oldDsName);
        // 关闭编辑窗口
        this.modifyShow = false;
        // 重新请求数据
        // this.scanDataStore();
        // 打开成功提示窗口
        this.modifySuccessShow = true;
      } else {
        console.log('modify faild：' + this.modifyForm.oldDsName + result.description);
        this.isOperationErr = true;
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }
  // table数据处理
  refresh() {
    this.isLoading = true;
    // 进行数据加载
    this.remoteSrv.getData()
        .subscribe((result: any) => {
          if (result.code === '200' && null != result.data ) {
            this.list = result.data;
            if (null !== this.list) {
              this.total = this.list.length;
              // 获取chart 数据
              const wwns = [];
              this.list.forEach(item => {
                item.usedCapacity = item.capacity - item.freeSpace;
                item.capacityUsage = ((item.capacity - item.freeSpace)/item.capacity);
                wwns.push(item.wwn);
              });
              // 设置卷ID集合
              this.wwns = wwns;
              if (this.radioCheck === 'chart') {
                this.getPerformanceData();
              }
            }
          } else {
          }
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
  }
  // 点刷新那个功能是分两步，一步是刷新，然后等我们这边的扫描任务，任务完成后返回你状态，任务成功后，你再刷新列表页面。
  scanDataStore() {
    // 初始化筛选
    this.isLoading = true;
    this.statusFilter.initStatus();
    this.deviceFilter.initDevice();
    this.serviceLevelFilter.initServiceLevel();
    this.protectionStatusFilter.initProtectionStatus();
    this.isFirstLoadChartData = true;
    this.remoteSrv.scanVMFS(this.storageType).subscribe((res: any) => {
      if (res.code === '200') {
        this.refresh();
        console.log('Scan success');
      } else {
        console.log('Scan faild');
      }
      // this.isLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  /**
   * 性能数据点击事件
   */
  charBtnClickFunc() {
    if (this.isFirstLoadChartData) {
      this.getPerformanceData();
    }
  }

  /**
   * 获取性能数据
   */
  getPerformanceData() {
    // 获取chart 数据
    const wwns = [];
    this.list.forEach(item => {
      wwns.push(item.wwn);
    });
    // 设置卷ID集合
    this.wwns = wwns;
    if (this.wwns.length > 0) {
      this.isLoading = true;
      this.remoteSrv.getChartData(this.wwns).subscribe((chartResult: any) => {
        if (chartResult.code === '200' && chartResult.data != null) {
          const chartList: VmfsInfo [] = chartResult.data;
          this.list.forEach(item => {
            chartList.forEach(charItem => {
              // 若属同一个卷则将chartItem的带宽、iops、读写相应时间 值赋予列表
              if (item.wwn === charItem.wwn) {
                item.iops = charItem.iops;
                item.bandwidth = charItem.bandwidth;
                item.readResponseTime = charItem.readResponseTime;
                item.writeResponseTime = charItem.writeResponseTime;
              }
            });
          });
        } else {
        }
        this.isLoading = false;
        this.isFirstLoadChartData = false; // 非第一次加载chartData 后续点击性能图标将不发送请求
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    }
  }
  // 获取所有存储数据
  getStorageList() {
    this.remoteSrv.getStorages().subscribe((result: any) => {
      if (result.code === '200' && result.data !== null) {
        this.storageList = result.data;
        const allPoolMap:StoragePoolMap[] = []

        result.data.forEach(item  => {
          const poolMap:StoragePoolMap = {
            storageId:item.id,
            storagePoolList:null,
            workloadList:null
          }
          allPoolMap.push(poolMap);
        });

        this.storagePoolMap = allPoolMap;

        this.getStoragePoolsByStorId();
      }
      this.modalLoading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  // 获取存储池数据
  getStoragePoolsByStorId() {
    this.form.pool_raw_id = undefined;
    this.storagePoolList = [];
    this.workloads = [];
    this.form.pool_raw_id = undefined;
    this.form.workload_type_id = undefined;
    if (null !== this.form.storage_id && '' !== this.form.storage_id) {
      // qos上显现
      this.addQosUpperAndLower();
      this.addSmartTierInit();
      this.addAllocationTypeShowInit();
      this.addWorkLoadShowInit();
      this.addLatencyChoose();

      const storagePoolMap = this.storagePoolMap.filter(item => item.storageId == this.form.storage_id);

      const storagePoolList = storagePoolMap[0].storagePoolList;
      const workloads = storagePoolMap[0].workloadList;
      // 存储池
      if (!storagePoolList) {
        this.remoteSrv.getStoragePoolsByStorId(this.form.storage_id, 'block').subscribe((result: any) => {
          if (result.code === '200' && result.data !== null) {
            this.storagePoolList = result.data;

            this.storagePoolMap.filter(item => item.storageId == this.form.storage_id)[0].storagePoolList = result.data;

            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          }
        });
      } else {
        this.storagePoolList = storagePoolList;
      }
      // 获取workLoad
      if (!workloads && this.showWorkLoadFlag) {
        this.remoteSrv.getWorkLoads(this.form.storage_id).subscribe((result: any) => {
          if (result.code === '200' && result.data !== null) {
            this.workloads = result.data;

            this.storagePoolMap.filter(item => item.storageId == this.form.storage_id)[0].workloadList = result.data;

            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          }
        });
      } else {
        this.workloads = workloads;
      }
    }
  }

  // 初始化块大小（修改版本触发事件）
  setBlockSizeOptions() {
    const options = [];
    const versionVal = this.form.version + '';
    if (versionVal === '6') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
      const option2 = {key: 64, value : '64KB'};
      options.push(option2);
    } else if (versionVal === '5') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
    }
    // 设置blockSize 可选值
    this.blockSizeOptions = [];
    this.blockSizeOptions = options;
    this.form.blockSize = this.blockSizeOptions[0].key;
    // 重置空间回收粒度
    this.setSrgOptions();
  }
  // 初始化空间回收粒度
  setSrgOptions() {
    const options = [];
    const blockValue = this.form.blockSize + '';
    const versionVal = this.form.version + '';
    if (blockValue === '1024') {
      const option1 = {key: 1024, value : '1MB'};
      options.push(option1);
      if(versionVal === '5') {
        const option2 = {key: 8, value : '8KB'};
        options.push(option2);
      }
    } else if (blockValue === '64') {
      const option1 = {key: 64, value : '64KB'};
      options.push(option1);
      if (versionVal === '5') {
        const option2 = {key: 8, value : '8KB'};
        options.push(option2);
      }
    }

    this.srgOptions = [];
    this.srgOptions = options;
    this.form.spaceReclamationGranularity = this.srgOptions[0].key;

    // 容量设置
    this.capacityOnblur();
  }

  // 设置设备数据
  setDeviceList() {
    // 初始化数据
    this.deviceList = [];
    // const nullDevice =  {
    //   deviceId: '',
    //   deviceName: '',
    //   deviceType: '',
    // };
    // this.deviceList.push(nullDevice);
    this.chooseDevice = undefined;

    // 初始添加页面的主机集群信息
    this.setHostDatas().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });

    this.setClusterDatas().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  // 设置主机数据
  setHostDatas() {
    return new Promise((resolve, reject) => {
      this.remoteSrv.getHostList().subscribe((result: any) => {
        let hostList: HostList[] = []; // 主机列表
        if (result.code === '200' && result.data !== null) {
          hostList = result.data;
          hostList.forEach(item => {

            const hostInfo = {
              deviceId: item.hostId,
              deviceName: item.hostName,
              deviceType: 'host',
            };
            this.deviceList.push(hostInfo);
          });
        }
        this.form.hostDataloadSuccess = true;
        resolve(this.deviceList);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    });
  }
  // 设置集群数据
  setClusterDatas() {
    return new Promise((resolve, reject) => {
      this.remoteSrv.getClusterList().subscribe((result: any) => {
        let clusterList: ClusterList [] = []; // 集群列表
        if (result.code === '200' && result.data !== null) {
          clusterList = result.data;
          clusterList.forEach(item => {

            const clusterInfo = {
              deviceId: item.clusterId,
              deviceName: item.clusterName,
              deviceType: 'cluster',
            };
            this.deviceList.push(clusterInfo);
          });
        }
        resolve(this.deviceList);
        this.form.culDataloadSuccess = true;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    });
  }
  // 点击addBtn触发事件
  addBtnClickFunc() {
    // 展示loading
    this.modalLoading = true;
    this.modalHandleLoading = false;
    this.isOperationErr = false;
    // 容量错误提示
    this.capacityErr = false;
    // this.gs.loading = true;
    // 名称错误提示初始化
    this.vmfsNameRepeatErr = false;
    this.volNameRepeatErr = false;
    this.matchErr = false;
    // 下限隐藏初始化
    this.showLowerFlag = false;
    this.showSmartTierFlag = false;
    this.showAlloctypeThick = false;
    this.showWorkLoadFlag = false;
    this.latencyIsSelect = false;

    // 初始化表单
    this.form = new GetForm().getAddForm();
    // 初始化form
    this.addForm.reset(this.form);
    // this.addForm.markAsTouched();
    // 添加页面显示
    this.popListShow = true;
    // 添加页面默认打开首页
    this.jumpTo(this.addPageOne);
    // 版本、块大小、粒度下拉框初始化
    this.setBlockSizeOptions();

    // 设置主机/集群
    this.setDeviceList();

    // Page2默认打开服务等级也页面
    this.levelCheck = 'level';

    // 初始化服务等级数据
    this.setServiceLevelList();

    // 初始化存储池
    this.storagePoolList = [];

    this.storagePoolMap = [];

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
  // 获取服务等级数据
  setServiceLevelList() {
    // 初始化服务等级选择参数
    this.serviceLevelIsNull = false;
    // 获取服务等级数据
    this.remoteSrv.getServiceLevelList().subscribe((result: any) => {
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.filter(item => item.totalCapacity !== 0);
      }
      // 隐藏loading
      this.modalLoading = false;
      // this.gs.loading = false;
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }
  // 添加vmfs 处理
  addVmfsHanlde() {
    const selectResult = this.serviceLevelList.find(item => item.show === true);
    if ((this.levelCheck === 'level' && selectResult && selectResult.totalCapacity !== 0) || this.levelCheck !== 'level') { // 选择服务等级
      if (selectResult) {
        this.form.service_level_id = selectResult.id;
        this.form.service_level_name = selectResult.name;
      }
      // 数据预处----卷名称
      if (this.form.isSameName) { // 卷名称与vmfs名称相同（PS：不同时为必填）
        this.form.volumeName = this.form.name;
      }
      // 数据预处----容量 （后端默认单位为GB）
      switch (this.form.capacityUnit) {
        case 'TB':
          this.form.capacity = this.form.capacity * 1024;
          break;
        case 'MB':
          this.form.capacity = this.form.capacity / 1024;
          break;
        case 'KB':
          this.form.capacity = this.form.capacity / (1024 * 1024);
          break;
        default: // 默认GB 不变
          break;
      }
      // 主机/集群数据处理
      if (this.chooseDevice.deviceType === 'host') {
        this.form.host = this.chooseDevice.deviceName;
        this.form.hostId = this.chooseDevice.deviceId;
      } else {
        this.form.cluster = this.chooseDevice.deviceName;
        this.form.clusterId = this.chooseDevice.deviceId;
      }
      if (this.levelCheck === 'customer') { // 未选择 服务等级 需要将服务等级数据设置为空
        this.form.service_level_id = null;
        this.form.service_level_name = null;
      }
      // 控制策略若未选清空数据
      if( this.levelCheck == 'customer') {
        this.qosFunc(this.form);
        if (this.form.control_policyUpper == '1') { // 上限+全选（上下限）
            this.form.control_policy = '1';
        } else if(this.form.control_policyLower == '0') {// 下限
          this.form.control_policy = '0';
        } else {
          this.form.control_policy = null;
        }
        // smartTiger
        if (!this.showSmartTierFlag || !this.form.smartTierFlag) {
          this.form.smartTier = null;
        }
      }

      // 打开 loading
      // this.gs.loading = true;
      this.modalHandleLoading = true;
      this.remoteSrv.createVmfs(this.form).subscribe((result: any) => {
        // 关闭 loading
        // this.gs.loading = false;
        this.modalHandleLoading = false;
        if (result.code === '200') {
          console.log('创建成功');
          // 关闭窗口;
          this.wizard.close();
          // 重新请求数据
          // this.scanDataStore();
          // 打开成功提示窗口
          this.addSuccessShow = true;
        } else {
          console.log('创建失败：' + result.description);
          // 失败信息
          this.isOperationErr = true;
        }
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });
    } else {
      this.serviceLevelIsNull = true;
    }
  }

  // 容量单位转换
  capacityChange(obj: any) {
    const objValue = obj.value.match(/\d+(\.\d{0,2})?/) ? obj.value.match(/\d+(\.\d{0,2})?/)[0] : '';

    if (objValue !== '') {

      let capatityG;
      // 数据预处----容量 （后端默认单位为GB）
      switch (this.form.capacityUnit) {
        case 'TB':
          capatityG = objValue * 1024;
          break;
        case 'MB':
          capatityG = objValue / 1024;
          break;
        case 'KB':
          capatityG = objValue / (1024 * 1024);
          break;
        default: // 默认GB 不变
          capatityG = objValue;
          break;
      }

      // 版本号5 最小容量为1G 版本号6最小2G
      if (capatityG < 1 && this.form.version === '5') {
        capatityG = 1;
      } else if (capatityG < 2 && this.form.version === '6') {
        capatityG = 2;
      }
      switch (this.form.capacityUnit) {
        case 'TB':
          capatityG = capatityG / 1024;
          break;
        case 'MB':
          capatityG = capatityG * 1024;
          break;
        case 'KB':
          capatityG = capatityG * (1024 * 1024);
          break;
        default: // 默认GB 不变
          capatityG = capatityG;
          break;
      }

      obj.value = capatityG;
    } else {
      obj.value = objValue;
    }
  }

  // 未选择服务等级 时调用方法
  customerClickFunc() {
    this.levelCheck = 'customer';
    // 切换服务等级与自定义隐藏错误信息
    this.isOperationErr = false;
    this.serviceLevelIsNull = false;
    this.storageList = null;
    this.storagePoolList = null;
    this.showSmartTierFlag = false;
    this.showAlloctypeThick = false;
    this.showWorkLoadFlag = false;
    this.latencyIsSelect = false;
    this.form.workload_type_id = null;

    // loading
    this.modalLoading = true;

    this.form.storage_id = null;
    this.form.pool_raw_id = null;
    this.form.qosFlag = false;

    this.storagePoolMap = [];

    this.getStorageList();
  }
  // 选择服务等级时
  serviceLevelBtnFunc() {
    this.levelCheck = 'level';
    this.serviceLevelIsNull = false;
    // 切换服务等级与自定义隐藏错误信息
    this.isOperationErr = false;

    this.setServiceLevelList();
  }
  // 页面跳转
  navigateTo(objectid: string){
    console.log('页面跳转了');
    console.log(objectid);
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'vsphere.core.datastore.summary',
      objectId: objectid
    });
  }

  // 删除VMFS 处理函数
  delHandleFunc() {
    const objectIds = this.rowSelected.map(item => item.objectid);
    console.log('del vmfs objectIds:' + objectIds);
    const delInfos = {
      dataStoreObjectIds: objectIds
    }
    this.modalHandleLoading = true;
    this.remoteSrv.delVmfs(delInfos).subscribe((result: any) => {

      this.modalHandleLoading = false;
      if (result.code === '200'){
        console.log('DEL success' + this.rowSelected[0].name + ' success');
        // 关闭删除页面
        this.delShow = false;
        // 重新请求数据
        // this.scanDataStore();
        // 打开成功提示窗口
        this.delSuccessShow = true;
      } else {
        console.log('DEL faild: ' + result.description);
        this.isOperationErr = true;
      }
      this.cdr.detectChanges();
    });
  }

  // 挂载按钮点击事件
  mountBtnFunc() {
    // 初始化表单
    if (this.rowSelected.length === 1) {

      this.modalLoading = true;
      this.modalHandleLoading = false;
      this.isOperationErr = false;
      this.mountErr = false;

      this.mountForm = new GetForm().getMountForm();
      const objectIds = [];
      objectIds.push(this.rowSelected[0].objectid);
      this.mountForm.dataStoreObjectIds = objectIds;
      console.log('this.mountForm', this.mountForm);

      // 加载主机与集群数据
      this.mountDeviceLoad();

      // // 打开挂载页面
      this.mountShow = true;
      // this.jumpPage(this.rowSelected[0].objectid,"vmfs/dataStore/mount");
    }
  }

  /**
   * 挂载页面 主机集群数据加载
   */
  mountDeviceLoad() {
    // 初始化主机
    this.mountHostData = false;
    this.hostList = [];
    this.chooseHost = undefined;
    this.initMountHost().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });

    // 初始化集群
    this.mountClusterData = false;
    this.clusterList = [];
    this.chooseCluster = undefined;
    this.initMountCluster().then(res => {
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  jumpPage(objectId:string,url:string){
    const resource = 'list';
    this.router.navigate([url],{
      queryParams:{
        objectId,resource
      }
    });
  }
  // 挂载  集群数据初始化
  initMountCluster() {
    return new Promise((resolve, reject) => {
      // 获取集群 通过ObjectId过滤已挂载的集群
      this.remoteSrv.getClusterListByObjectId(this.rowSelected[0].objectid).subscribe((result: any) => {
        if (result.code === '200' && result.data !== null){
          result.data.forEach(item => {
            this.clusterList.push(item);
          });
        }
        this.mountClusterData = true;
        this.modalLoading = false;
        resolve(this.clusterList);
        this.cdr.detectChanges();
      });
    });
  }
  // 挂载 主机数据初始化
  initMountHost() {
    return new Promise((resolve, reject) => {
      // 获取服务器 通过ObjectId过滤已挂载的服务器
      this.remoteSrv.getHostListByObjectId(this.rowSelected[0].objectid).subscribe((result: any) => {
        if (result.code === '200' && result.data !== null){
          result.data.forEach(item => {
            this.hostList.push(item);
          });
        }
        this.mountHostData = true;
        resolve(this.hostList);
        this.cdr.detectChanges();
      });
    });
  }
  // 挂载提交
  mountSubmit(){

    console.log('this.chooseHost', this.chooseHost);
    if (this.chooseHost || this.chooseCluster) {
      this.mountErr = false;
      // 数据封装
      if (this.mountForm.mountType === '1'){ // 服务器
        this.mountForm.hostId = this.chooseHost.hostId;
        this.mountForm.host = this.chooseHost.hostName;
      }else if (this.mountForm.mountType === '2'){ // 集群
        this.mountForm.cluster = this.chooseCluster.clusterName;
        this.mountForm.clusterId = this.chooseCluster.clusterId;
      }

      this.modalHandleLoading = true;
      this.remoteSrv.mountVmfs(this.mountForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code  ===  '200'){
          console.log('挂载成功');
          // 关闭挂载页面
          this.mountShow = false;
          // 刷新数据
          // this.scanDataStore();
          // 打开成功提示窗口
          this.mountSuccessShow = true;
        } else {
          console.log('挂载异常：' + result.description);
          this.isOperationErr = true;
        }
        this.cdr.detectChanges();
      });
    }
  }
  // 卸载按钮点击事件
  unmountBtnFunc() {
    if (this.rowSelected.length === 1) {
      this.modalLoading = true;
      this.modalHandleLoading = false;
      this.isOperationErr = false;

      // 初始化卸载 页面未选择设备 提示数据展示
      this.notChooseUnmountDevice = false;
      // 初始话已选择数据
      this.chooseUnmountCluster = null;
      this.chooseUnmountHost = null;
      // 获取已挂载的集群 主机数据
      this.unmountForm = new GetForm().getUnmountForm();
      this.unmountForm.name = this.rowSelected[0].name;
      this.mountedHost = null;
      this.mountedCluster = null;
      // 获取主机
      this.remoteSrv.getMountHost(this.rowSelected[0].objectid).subscribe((result: any) => {
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
          console.log('this.serviceLevelList', this.serviceLevelList);
        }
        // 获取集群
        this.remoteSrv.getMountCluster(this.rowSelected[0].objectid).subscribe((result: any) => {
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
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
        });
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      });

      this.unmountShow = true;
    }
  }

  // 卸载确认
  unMountConfirm() {
    this.unmountTipsShow = true;
  }
  // 卸载处理函数
  unmountHandleFunc() {
    this.unmountTipsShow = false;
    console.log('this.chooseUnmountHost', this.chooseUnmountHost);
    console.log('this.chooseUnmountCluster', this.chooseUnmountCluster);
    console.log('this.flag', (!this.chooseUnmountHost && this.unmountForm.mountType === '1') || (!this.chooseUnmountCluster && this.unmountForm.mountType === '2'));
    if ((!this.chooseUnmountHost && this.unmountForm.mountType === '1') || (!this.chooseUnmountCluster && this.unmountForm.mountType === '2')) {
      this.notChooseUnmountDevice = true;
    } else {
      this.unmountForm.dataStoreObjectIds.push(this.rowSelected[0].objectid);
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
          console.log('unmount ' + this.rowSelected[0].name + ' success');
          // 关闭卸载页面
          this.unmountShow = false;
          // 重新请求数据
          // this.scanDataStore();
          // 打开成功提示窗口
          this.unmountSuccessShow = true;
        } else {
          console.log('unmount ' + this.rowSelected[0].name + ' fail：' + result.description);
          this.isOperationErr = true;
        }
        this.cdr.detectChanges();
      });
    }
  }
  // 回收空间 处理
  reclaimHandleFunc() {
    const vmfsObjectIds = this.rowSelected[0].objectid;
    this.modalHandleLoading = true;
    const reclaimIds = [];
    reclaimIds.push(vmfsObjectIds)
    this.remoteSrv.reclaimVmfs(reclaimIds).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200'){
        // 关闭回收空间页面
        this.reclaimShow = false;
        // 空间回收完成重新请求数据
        // this.scanDataStore();
        // 打开成功提示窗口
        this.reclaimSuccessShow = true;
      }else {
        this.isOperationErr = true;
      }
      this.cdr.detectChanges();
    });

  }
  // 变更服务等级 按钮点击事件
  changeServiceLevelBtnFunc() {

    if (this.rowSelected.length === 1) {
      this.modalLoading = true;
      this.modalHandleLoading = false;
      this.isOperationErr = false;
      // 初始化表单
      this.changeServiceLevelForm = new GetForm().getChangeLevelForm();
      // 设置表单默认参数
      const volumeIds = [];
      volumeIds.push(this.rowSelected[0].volumeId);
      this.changeServiceLevelForm.volume_ids = volumeIds;
      this.changeServiceLevelForm.ds_name = this.rowSelected[0].name;

      // 显示修改服务等级页面
      this.changeServiceLevelShow = true;
      // 初始化服务等级列表数据
      this.setServiceLevelList();
    }
  }
  // 变更服务等级 服务等级点击事件
  changeSLDataClickFunc(serviceLevId: string, serviceLevName: string) {
    this.changeServiceLevelForm.service_level_id = serviceLevId;
    this.changeServiceLevelForm.service_level_name = serviceLevName;
  }

  // 变更服务等级 处理
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
          // 关闭修改服务等级页面
          this.changeServiceLevelShow = false;
          // 重新请求数据
          // this.scanDataStore();
          // 打开成功提示窗口
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
  // 扩容按钮点击事件
  expandBtnFunc() {
    if (this.rowSelected.length === 1) {
      // 错误信息 隐藏
      this.isOperationErr = false;
      this.expandErr = false;
      // 初始化form表单
      this.expandForm = new GetForm().getExpandForm();

      this.expandShow = true;
      console.log(this.rowSelected[0]);
      this.expandForm.volume_id = this.rowSelected[0].volumeId;
      this.expandForm.ds_name = this.rowSelected[0].name;
      this.expandForm.obj_id = this.rowSelected[0].objectid;
    }
  }
  // 扩容处理
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
      this.modalHandleLoading = true;
      // 参数封装
      this.remoteSrv.expandVMFS(this.expandForm).subscribe((result: any) => {
        this.modalHandleLoading = false;
        if (result.code === '200'){
          console.log('expand success:' + name);
          // 隐藏扩容页面
          this.expandShow = false;
          // 重新请求数据
          // this.scanDataStore();
          // 打开成功提示窗口
          this.expandSuccessShow = true;
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
  // 空间回收按钮点击事件
  reclaimBtnClick() {
    if (this.rowSelected.length >= 1) {
      this.reclaimShow = true;

      this.isReclaimErr = false;

      this.isOperationErr = false;
      this.modalHandleLoading = false;
      const vmfsObjectIds = this.rowSelected[0].objectid;

      this.modalHandleLoading = true;
      this.remoteSrv.reclaimVmfsJudge(vmfsObjectIds).subscribe((result:any) => {
        this.modalHandleLoading = false;
        if (result.code == '200') {
          if (!result.data) {
            this.isReclaimErr = true;
          }
        }
        this.cdr.detectChanges();
      });

    }
  }

  // 删除按钮点击事件
  delBtnClickFUnc() {
    if (this.rowSelected.length >= 1) {
      this.delShow = true;

      this.modalHandleLoading = false;
      this.isOperationErr = false;
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
   * 容量
   * @param obj
   */
  capacityOnblur() {
    // 容量
    let capacity = this.form.capacity;
    // 标准容量 单位G
    let capacityG;
    console.log('capacity', capacity)
    if (capacity && capacity !== null && capacity !== '') {

      if (capacity > 0) {
        switch (this.form.capacityUnit) {
          case "TB":
            capacityG = capacity * 1024 + '';
            console.log('capacityG2', capacityG);
            if (capacityG.indexOf(".")!==-1) { // 小数
              this.capacityErr = true;
              capacity = '';
            } else{ // 整数
              if (this.form.version === '5') {
                if (capacity < 1/1024) {
                  capacity = '';
                  this.capacityErr = true;
                } else {
                  this.capacityErr = false;
                }
              } else {
                if (capacity < 2/1024) {
                  capacity = '';
                  this.capacityErr = true;
                }else {
                  this.capacityErr = false;
                }
              }
            }
            break;
          case "MB":
            capacityG = capacity / 1024 + '';
            if (capacityG.indexOf(".")!==-1) { // 小数
              this.capacityErr = true;
              capacity = '';
            } else { // 整数
              if (this.form.version === '5') {
                if (capacity < 1*1024) {
                  capacity = '';
                  this.capacityErr = true;
                }else {
                  this.capacityErr = false;
                }
              } else {
                if (capacity < 2*1024) {
                  capacity = '';
                  this.capacityErr = true;
                }else {
                  this.capacityErr = false;
                }
              }
            }
            break;
          default:
            capacityG = capacity + '';
            if (capacityG.indexOf(".")!==-1) { // 小数
              capacity = '';
              this.capacityErr = true;
            } else {// 整数
              if (this.form.version === '5') {
                if (capacity < 1) {
                  capacity = '';
                  this.capacityErr = true;
                } else {
                  this.capacityErr = false;
                }
              } else {
                if (capacity < 2) {
                  capacity = '';
                  this.capacityErr = true;
                } else {
                  this.capacityErr = false;
                }
              }

            }
            break;
        }
      } else {
        capacity = '';
        this.capacityErr = true;
      }
    } else {
      capacity = '';
    }
    this.form.capacity = capacity;
    console.log('this.form.capacityUnit', this.form.capacityUnit);
    console.log('this.form.capacity', this.form.capacity);
    console.log('this.form.count', this.form.count);
  }

  /**
   * 数量变化
   */
  countBlur() {
    let count = this.form.count;
    if (count && count !== null && count !== '') {
      if ((count+'').indexOf(".")!==-1) { // 小数
        count = '';
        this.capacityErr = true;
      } else {
        this.capacityErr = false;
      }
    } else {
      count = '';
    }
    this.form.count =  count;
  }

  /**
   * add 下一页
   */
  addNextPage() {
    if (this.form.capacity !== '' && this.form.count !== '' && this.form.capacity > 0 && this.form.count > 0) {
      this.wizard.next();
    }
  }

  /**
   * 带宽 blur
   * @param type
   * @param operationType add modify
   * @param valType
   */
  qosBlur(type:String, operationType:string) {

    let objVal;
    if (type === 'add') {
      switch (operationType) {
        case 'maxbandwidth':
          objVal = this.form.maxbandwidth;
          break;
        case 'maxiops':
          objVal = this.form.maxiops;
          break;
        case 'minbandwidth':
          objVal = this.form.minbandwidth;
          break;
        case 'miniops':
          objVal = this.form.miniops;
          break;
        default:
          objVal = this.form.latency;
          break;
      }
    } else {
      switch (operationType) {
        case 'max_bandwidth':
          objVal = this.modifyForm.max_bandwidth;
          break;
        case 'max_iops':
          objVal = this.modifyForm.max_iops;
          break;
        case 'min_bandwidth':
          objVal = this.modifyForm.min_bandwidth;
          break;
        case 'min_iops':
          objVal = this.modifyForm.min_iops;
          break;
        default:
          objVal = this.modifyForm.latency;
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
          this.form.maxbandwidth = objVal;
          break;
        case 'maxiops':
          this.form.maxiops = objVal;
          break;
        case 'minbandwidth':
          this.form.minbandwidth = objVal;
          break;
        case 'miniops':
          this.form.miniops = objVal;
          break;
        default:
          this.form.latency = objVal;
          break;
      }
    } else {
      switch (operationType) {
        case 'max_bandwidth':
          this.modifyForm.max_bandwidth = objVal;
          break;
        case 'max_iops':
          this.modifyForm.max_iops = objVal;
          break;
        case 'min_bandwidth':
          this.modifyForm.min_bandwidth = objVal;
          break;
        case 'min_iops':
          this.modifyForm.min_iops = objVal;
          break;
        default:
          this.modifyForm.latency = objVal;
          break;
      }
    }
  }

  /**
   * 关闭成功提示窗口
   */
  closeSuccessTips() {
    this.addSuccessShow = false;
    this.modifySuccessShow = false;
    this.expandSuccessShow = false;
    this.reclaimSuccessShow = false;
    this.changeServiceLevelSuccessShow = false;
    this.mountSuccessShow = false;
    this.unmountSuccessShow = false;
    this.delSuccessShow = false;
    this.isFirstLoadChartData = true;
    // this.backToListPage();
    // 重新请求数据
    this.refresh();
  }

  /**
   * 名称校验
   * @param isVmfs true vmfs、false volume
   */
  nameCheck(isVmfs: boolean) {
    // 初始化
    this.vmfsNameRepeatErr = false;
    this.volNameRepeatErr = false;
    this.matchErr = false;

    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');
    if (isVmfs) {
      if (this.form.name) {
        if (reg5.test(this.form.name)) {
          // 校验VMFS名称重复
          this.checkVmfsName(this.form.name);
          if (this.form.isSameName) {
            this.form.volumeName = this.form.name;
          }
        } else {
          this.matchErr = true;
          this.form.name = null;
        }
      } else {
        this.matchErr = true;
      }
    } else {
      if (this.form.volumeName) {
        if (reg5.test(this.form.volumeName)) {
          // 校验Vol名称重复
          this.checkVolName(this.form.volumeName);
        } else {
          this.matchErr = true;
          this.form.volumeName = null;
        }
      } else {
        this.matchErr = true;
      }
    }
  }

  /**
   * vmfs重名校验
   */
  checkVmfsName(name: string) {
    this.modalHandleLoading = true;
    this.remoteSrv.checkVmfsName(name).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200') { // result.data true 不重复 false 重复
        this.vmfsNameRepeatErr = !result.data;
        if (this.vmfsNameRepeatErr) { // 名称重复
          this.form.name = null;
          this.volNameRepeatErr = false;
          this.matchErr = false;
        } else {
          if (this.form.isSameName) {
            this.checkVolName(name);
          }
        }
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  /**
   * vol重名校验
   */
  checkVolName(name: string) {
    this.modalHandleLoading = true;
    // 校验VMFS名称重复
    this.remoteSrv.checkVolName(name).subscribe((result: any) => {
      this.modalHandleLoading = false;
      if (result.code === '200') { // result.data true 不重复 false 重复
        this.volNameRepeatErr = !result.data;
        if (!this.vmfsNameRepeatErr && this.volNameRepeatErr) {
          this.form.name = null;
        }
        if (this.volNameRepeatErr) {
          this.form.volumeName = null;
          this.vmfsNameRepeatErr = false;
          this.matchErr = false;
        }
      }
      console.log("this.modalHandleLoading", this.modalHandleLoading)
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  /**
   * 编辑功能名称校验
   */
  modifyNameCheck() {
    this.vmfsNameRepeatErr = false;
    this.volNameRepeatErr = false;
    this.matchErr = false;
    let reg5:RegExp = new RegExp('^[0-9a-zA-Z-"_""."]*$');

    if (this.modifyForm.name) {
      if (reg5.test(this.modifyForm.name)) {
        // 校验VMFS名称重复
        // this.modalHandleLoading = true;
        if (this.modifyNameChanged) { // 名称发生变化则进行名称校验
          this.nameChecking = true;
          this.remoteSrv.checkVmfsName(this.modifyForm.name).subscribe((result: any) => {
            // this.modalHandleLoading = false;
            if (result.code === '200') { // result.data true 不重复 false 重复
              this.vmfsNameRepeatErr = !result.data;
              if (this.vmfsNameRepeatErr) { // 名称重复
                this.modifyForm.name = null;
                this.volNameRepeatErr = false;
                this.matchErr = false;
                this.nameChecking = false;
              } else {
                if (this.modifyForm.isSameName) {
                  // this.modalHandleLoading = true;
                  // 校验VMFS名称重复
                  this.remoteSrv.checkVolName(this.modifyForm.name).subscribe((result: any) => {
                    // this.modalHandleLoading = false;
                    if (result.code === '200') { // result.data true 不重复 false 重复
                      this.volNameRepeatErr = !result.data;
                      if (!this.vmfsNameRepeatErr && this.volNameRepeatErr) {
                        this.modifyForm.name = null;
                      }
                      if (this.volNameRepeatErr) {
                        this.modifyForm.name = null;
                        this.vmfsNameRepeatErr = false;
                        this.matchErr = false;
                      } else {
                        // 数据提交
                        this.modifyHandleFunc();
                      }
                    }
                    this.nameChecking = false;
                    this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
                  });
                } else {
                  // 数据提交
                  this.modifyHandleFunc();
                }
              }
            }
            this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          });
        } else {
          this.modifyHandleFunc();
        }

      } else {
        this.matchErr = true;
        this.modifyForm.name = null;
      }
    } else {
      this.matchErr = true;
    }
  }

  qosFunc(form) {
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    const qosTag = this.getStorageQosTag(form.storage_id);
    if (!form.qosFlag) {// 关闭状态
      this.initAddMinInfo(form);
      this.initAddMaxInfo(form);
    }else {
      if (form.control_policyUpper == '1') {
        if (!form.maxbandwidthChoose) {
          form.maxbandwidth = null;
        }
        if (!form.maxiopsChoose) {
          form.maxiops = null;
        }
        if (qosTag == 2 || qosTag == 3) {
          this.initAddMinInfo(form);
        }
      } else {
        this.initAddMaxInfo(form);
      }
      if (form.control_policyLower == '0') {
        if(qosTag == 2){
          this.initAddMaxInfo(form);
        }else if (qosTag == 3) {
          this.initAddMinInfo(form);
        } else {
          if (!form.minbandwidthChoose) {
            form.minbandwidth = null;
          }
          if (!form.miniopsChoose) {
            form.miniops = null;
          }
          if (!form.latencyChoose) {
            form.latency = null;
          }
        }
      } else {
        this.initAddMinInfo(form);
      }
      if (form.control_policyUpper != '1' && form.control_policyLower != '0') {
        this.initAddMinInfo(form);
        this.initAddMaxInfo(form);
        form.control_policy = null;
      }
    }
  }
  initAddMinInfo(form) {
    form.control_policyLower = undefined;
    form.minbandwidthChoose = false;
    form.minbandwidth = null;
    form.miniopsChoose = false;
    form.miniops = null;
    form.latencyChoose = false;
    form.latency = null;
  }
  initAddMaxInfo(form) {
    form.control_policyUpper = undefined;
    form.maxbandwidthChoose = false;
    form.maxbandwidth = null;
    form.maxiopsChoose = false;
    form.maxiops = null;
  }
  qosEditFunc(form) {
    console.log("editform.qosFlag", form.qosFlag);
    const qosTag = this.storage.storageTypeShow.qosTag;
    if (!form.qosFlag) {// 关闭状态
      this.initEditMinInfo(form);
      this.initEditMaxInfo(form);
    }else {
      if (form.control_policyUpper == '1') {
        if (!form.maxbandwidthChoose) {
          form.max_bandwidth = null;
        }
        if (!form.maxiopsChoose) {
          form.max_iops = null;
        }
        if (qosTag == 2 || qosTag == 3) {
          this.initEditMinInfo(form);
        }
      } else {
        this.initEditMaxInfo(form);
      }
      if (form.control_policyLower == '0') {
        if(qosTag == 2) {
          this.initEditMaxInfo(form);
        } else if (qosTag == 3) {
          this.initEditMinInfo(form);
        } else {
          if (!form.minbandwidthChoose) {
            form.min_bandwidth = null;
          }
          if (!form.miniopsChoose) {
            form.min_iops = null;
          }
          if (!form.latencyChoose) {
            form.latency = null;
          }
        }
      } else {
        this.initEditMinInfo(form);
      }
      if (form.control_policyUpper != '1' && form.control_policyLower != '0') {
        this.initEditMinInfo(form);
        this.initEditMaxInfo(form);
        form.control_policy = null;
      }
    }
  }
  initEditMinInfo(form) {
    form.control_policyLower = undefined;
    form.minbandwidthChoose = false;
    form.min_bandwidth = null;
    form.miniopsChoose = false;
    form.min_iops = null;
    form.latencyChoose = false;
    form.latency = null;
  }
  initEditMaxInfo(form) {
    form.control_policyUpper = undefined;
    form.maxiopsChoose = false;
    form.max_iops = null;
    form.maxbandwidthChoose = false;
    form.max_bandwidth = null;
  }

  /**
   * 编辑页面  名称变化Func
   */
  modifyNameChange() {
    const oldName = this.rowSelected[0].volumeName;
    if (oldName !== this.modifyForm.name) {
      this.modifyNameChanged = true;
    } else {
      this.modifyNameChanged = false;
    }
    console.log("this.modifyForm.name", this.modifyForm.name);
  }

  /**
   * 添加页面 qos 上下限 单选、多选、隐藏
   * smartTiger 初始化
   */
  addQosUpperAndLower() {
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    const qosTag = this.getStorageQosTag(this.form.storage_id);
    this.form.control_policyLower = undefined;
    this.form.control_policyUpper = undefined;
    const upperObj = document.getElementById("control_policyUpper") as HTMLInputElement;
    const lowerObj = document.getElementById("control_policyLower") as HTMLInputElement;
    if (upperObj && upperObj.checked) {
      upperObj.checked = false;
    }
    if (lowerObj && lowerObj.checked) {
      lowerObj.checked = false;
    }
    if (qosTag == 3) {
      this.showLowerFlag = true;
    } else {
      this.showLowerFlag = false;
    }
  }

  /**
   * 添加页面 smartTier
   */
  addSmartTierInit() {
    this.form.smartTier = null;
    this.form.smartTierFlag = false;
    this.showSmartTierFlag = this.getStroageSmartTierShow(this.form.storage_id);
  }

  /**
   * 获取选中的存储的 SmartTier
   * @param storageId
   */
  getStroageSmartTierShow(storageId){
    const storageTypeShow = this.storageList.filter(item => item.id == storageId);
    // SmartTier策略 true 支持 false 不支持
    const smartTierShow = storageTypeShow[0].storageTypeShow.smartTierShow;
    return smartTierShow;
  }

  /**
   * 获取选中的存储的 allocationTypeShow
   * @param storageId
   */
  getAllocationTypeShow(storageId) {
    const storageTypeShow = this.storageList.filter(item => item.id == storageId);
    // 资源分配类型  1 可选thin/thick 2 可选thin
    const allocationTypeShow = storageTypeShow[0].storageTypeShow.allocationTypeShow;
    return allocationTypeShow;
  }

  /**
   * 添加页面 资源调优thick展示与隐藏
   */
  addAllocationTypeShowInit() {
    this.form.alloctype = 'thin';
    const allocationTypeShow = this.getAllocationTypeShow(this.form.storage_id);
    this.showAlloctypeThick = allocationTypeShow == 1;
  }

  /**
   * 获取应用类型 展示与隐藏
   * @param storageId
   */
  getWorkLoadShow(storageId) {
    const storageTypeShow = this.storageList.filter(item => item.id == storageId);
    // 1 支持应用类型 2不支持应用类型
    const workLoadShow = storageTypeShow[0].storageTypeShow.workLoadShow;
    return workLoadShow;
  }

  /**
   * 添加页应用类型展示与隐藏 初始化
   */
  addWorkLoadShowInit() {
    this.form.workload_type_id = null;
    const workLoadShow = this.getWorkLoadShow(this.form.storage_id);
    this.showWorkLoadFlag = workLoadShow == 1;
  }

  /**
   * 添加页面 时延为下拉框
   */
  addLatencyChoose() {
    this.form.latency = null;
    const qosTag = this.getStorageQosTag(this.form.storage_id);
    this.latencyIsSelect = qosTag == 1;
  }

  /**
   * 获取选中的存储的 QosTag
   */
  getStorageQosTag(storageId) {
    const storageTypeShow = this.storageList.filter(item => item.id == storageId);
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    const qosTag = storageTypeShow[0].storageTypeShow.qosTag;
    return qosTag;
  }

  /**
   * 控制策略变更
   * @param upperObj
   * @param lowerObj
   * @param isUpper true:upper、false:lower
   */
  controlPolicyChangeFunc(upperId, lowerId, isEdit, form, isUpper) {
    const upperObj = document.getElementById(upperId) as HTMLInputElement;
    const lowerObj = document.getElementById(lowerId) as HTMLInputElement;
    // qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只支持上限
    let qosTag;
    if (isEdit) {
      qosTag = this.storage.storageTypeShow.qosTag;
    } else {
      qosTag = this.getStorageQosTag(this.form.storage_id);
    }


    let upperChecked;
    if(upperObj) {
      upperChecked =  upperObj.checked;
    }
    let lowerChecked;
    if (lowerObj) {
      lowerChecked = lowerObj.checked;
    }
    if (isUpper) {
      if(upperChecked) {
        form.control_policyUpper = '1';
      }else {
        form.control_policyUpper = undefined;
      }
      if(qosTag == 2 && upperChecked) { // 单选
        console.log("单选1", qosTag)
        form.control_policyLower = undefined;
        lowerObj.checked = false;
      }
    } else {
      if(lowerChecked) {
       form.control_policyLower = '0';
      }else {
        form.control_policyLower = undefined;
      }
      if (lowerChecked && qosTag == 2) {
        console.log("单选2", qosTag)
        form.control_policyUpper = undefined;
        upperObj.checked = false;
      }
    }
    if (isEdit) {
      this.modifyForm = form;
    } else {
      this.form = form;
    }
    console.log("lowerChecked", this.form)
  }
}
