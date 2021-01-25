import {
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  Component,
  NgZone,
  OnInit,
  ViewChild
} from '@angular/core';
import {
  CapacityDistribution, CapacitySavings, DetailService, Dtrees, NfsShare, PoolList, StorageController, StorageDetail,
  StorageDisk,
  StoragePool,
  Volume
} from './detail.service';
import { EChartOption } from 'echarts';
import {
  AxisLine, AxisPointer,
  ChartOptions,
  FileSystem, Legend, LegendData, LineStyle, MakePerformance,
  NfsService, Serie,
  SplitLine,
  TextStyle,
  Title, Tooltip,
  XAxis,
  YAxis
} from '../../nfs/nfs.service';
import {ActivatedRoute, Router} from "@angular/router";
import {CapacityChart, CapacitySerie} from "../storage.service";
import {BondPort, EthernetPort, FailoverGroup, FCoEPort, FCPort, LogicPort} from "./port.service";
import {FormControl, FormGroup} from "@angular/forms";
import {GlobalsService} from "../../../shared/globals.service";
import {ClrDatagridPagination} from "@clr/angular";
import {TranslatePipe} from "@ngx-translate/core";
import {DeviceFilter} from "../../vmfs/list/filter.component";
import {
  DtreeQuotaFilter, DtreeSecModFilter,
  FsStatusFilter, FsTypeFilter,
  MapStatusFilter,
  ProTypeFilter,
  StoragePoolStatusFilter,
  StoragePoolTypeFilter, VolProtectionStatusFilter, VolServiceLevelFilter,
  VolStatusFilter, VolStoragePoolFilter
} from "../filter.component";
@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [DetailService, TranslatePipe, MakePerformance, NfsService],
})
export class DetailComponent implements OnInit, AfterViewInit {

  cd : CapacityDistribution;
  capSave:CapacitySavings;
  options = {
    tooltip: {
      trigger: 'item',
      formatter: ' {b}: {c} ({d}%)'
    },
    title: {
      text: '123',
      textAlign: 'center',
      padding: 0,
      textVerticalAlign: 'middle',
      textStyle: {
        fontSize: 22,
        color: '#63B3F7'
      },
      subtextStyle: {
        fontSize: 12,
        color: '#c2c6dc',
        align: 'center'
      },
      left: '50%',
      top: '50%',
      // subtext: '234'
    },

    series: [
      {
        name: '',
        type: 'pie',
        radius: ['50%', '70%'],
        center: ['50%', '50%'],

        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: false,
            fontSize: '30',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: [
          {value: 335, name: '直接访问'},
          {value: 310, name: '邮件营销'},
          {value: 234, name: '联盟广告'},
          {value: 135, name: '视频广告'},
          {value: 1548, name: '搜索引擎'}
        ]
      }
    ],
    color: ['#FF0000', '#FF9538', '#63B3F7']
  };
  // 创建表格对象
  // IOPS+QoS上下限
  iopsChart: EChartOption = {};
  // 带宽+QoS上下限
  bandwidthChart: EChartOption = {};
  range;
  // 定时函数执行时间 默认一天
  poolRadio = 'table1'; // 存储池列表切换
  volumeRadio = 'table1'; // volume列表切换
  storageId = '1234';
  storageName= "";
  constructor(private nfsService:NfsService, private makePerformance: MakePerformance,
              private detailService: DetailService, private cdr: ChangeDetectorRef, private ngZone: NgZone,
              private gs: GlobalsService,private activatedRoute: ActivatedRoute,private router:Router,
              private translatePipe:TranslatePipe) { }
  detail: StorageDetail;
  storagePool: StoragePool[];
  volumes: Volume[];
  volumeTotal=0;
  poolTotal=0;
  fsTotal=0;
  dtreeTotal=0;
  shareTotal=0;
  conTotal=0;
  diskTotal=0;
  fcTotal=0;
  ethTotal=0;
  fcoeTotal=0;
  bondTotal=0;
  logicTotal=0;
  failTotal=0;

  fsList: FileSystem[];
  dtrees: Dtrees[];
  shares: NfsShare[];
  controllers:StorageController[];
  disks: StorageDisk[];

  fcs:FCPort[];
  eths: EthernetPort[];
  fcoes: FCoEPort[];
  bonds: BondPort[];
  logicports:LogicPort[];
  fgs:FailoverGroup[];
  storagePoolIds=[];
  volumeIds=[];

  selectRange = 'LAST_1_DAY';
  startTime = null;
  // endTime
  endTime = null;

  totalPageNum = '/' + ' ' + '0'; // 卷信息分页个数

  rangeTime = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });
  // ranges
  ranges =  [
    {key: 'LAST_5_MINUTE', value: '最近5分钟'},
    {key: 'LAST_1_HOUR', value: '最近1小时'},
    {key: 'LAST_1_DAY', value: '最近1天'},
    {key: 'LAST_1_WEEK', value: '最近1周'},
    {key: 'LAST_1_MONTH', value: '最近1个月'},
    {key: 'LAST_1_QUARTER', value: '最近1个季度'},
    {key: 'HALF_1_YEAR', value: '最近半年'},
    {key: 'LAST_1_YEAR', value: '最近1年'},
  ];
  isLoading = false;
  @ViewChild('paginationVolume')pagination:ClrDatagridPagination;

  volumeFooter = '1 - 10'; // 卷信息当前页数据
  volumeTotalPage = 10;// 卷信息总页数
  volumeCurrentPage = 1;// 卷信息当前页
  volumePageSize = 10; // 当前页数据数

  @ViewChild('storagePoolTypeFilter') storagePoolTypeFilter:StoragePoolTypeFilter;
  @ViewChild('storagePoolStatusFilter') storagePoolStatusFilter:StoragePoolStatusFilter;
  @ViewChild('volStatusFilter') volStatusFilter:VolStatusFilter;
  @ViewChild('portTypeFilter') portTypeFilter:ProTypeFilter;
  @ViewChild('mapStatusFilter') mapStatusFilter:MapStatusFilter;
  @ViewChild('volStoragePoolFilter') volStoragePoolFilter:VolStoragePoolFilter;
  @ViewChild('volServiceLevelFilter') volServiceLevelFilter:VolServiceLevelFilter;
  @ViewChild('volProtectionStatusFilter') volProtectionStatusFilter:VolProtectionStatusFilter;
  @ViewChild('fsStatusFilter') fsStatusFilter:FsStatusFilter;
  @ViewChild('fsTypeFilter') fsTypeFilter:FsTypeFilter;
  @ViewChild('dtreeQuotaFilter') dtreeQuotaFilter:DtreeQuotaFilter;
  @ViewChild('dtreeQuotaFilter') dtreeSecModFilter:DtreeSecModFilter;


  storageDetailTag:number;
  //portList:
  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(queryParam => {
      this.storageId = queryParam.id;
      this.storageName = queryParam.name;
    });
    this.getStorageDetail(true);
    this.getStoragePoolList(true);

  }
  ngAfterViewInit() {
    this.ngZone.runOutsideAngular(() => this.initChart());
  }
  // 初始化表格对象
  async initChart() {
    this.gs.loading=true;
    const fsNames:string[] = [];
    fsNames.push(this.storageId);
     // IOPS
    this.setChart(150,this.translatePipe.transform('vmfs.iops'),"IO/s",
     NfsService.storageIOPS,fsNames,this.selectRange,NfsService.storageUrl, this.startTime, this.endTime).then(res=>{
      this.gs.loading=false;
      this.iopsChart = res;
      this.cdr.detectChanges();
    });

    // 带宽
    this.setChart(150, this.translatePipe.transform('nfs.qos_bandwidth'), 'MB/s',
      NfsService.storageBDWT, fsNames, this.selectRange, NfsService.storageUrl, this.startTime, this.endTime).then(res => {
      this.gs.loading=false;
      this.bandwidthChart = res;
      this.cdr.detectChanges();
    });
  }

  changeTab(page: string){
    this.gs.loading=false;
    if (page === 'conf'){
      this.getStorageDetail(false);
    }
    if (page === 'pool'){
      this.getStoragePoolList(false);
    }
    if (page === 'volume'){
      this.getStorageVolumeList(false);
    }
    if (page === 'fs'){
      this.getFileSystemList(false);
    }
    if (page === 'dtrees'){
      this.getDtreeList(false);
    }
    if (page === 'shares'){
      this.getShareList(false);
    }
    if (page === 'capacity'){
      this.initCapacity();
    }
    if (page === 'hardware'){
      this.getControllerList(false);
    }
    if (page === 'controller'){
      this.getControllerList(false);
    }
    if (page === 'disk'){
      this.getDisksList(false);
    }
    if (page === 'port'){
      this.getPortsList();
    }

  }
  getStorageDetail(fresh: boolean){
    if (fresh){
      this.gs.loading=true;
      this.detailService.getStorageDetail(this.storageId).subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.detail = r.data;
          this.storageDetailTag = this.detail.storageTypeShow.storageDetailTag;
          this.initCapacity();
          this.cdr.detectChanges();
        }else{

        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.detail === null){
        this.gs.loading=true;
        this.detailService.getStorageDetail(this.storageId).subscribe((r: any) => {
          this.gs.loading=false;
          if (r.code === '200'){
            this.detail = r.data;
            this.initCapacity();
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  refreshStoragePool() {
    this.storagePoolStatusFilter.initStatus();
    this.storagePoolTypeFilter.initType();
    this.getStoragePoolList(true);
  }
  getStoragePoolList(fresh: boolean){
    if (fresh){
      this.gs.loading=true;
      this.detailService.getStoragePoolList(this.storageId).subscribe((r: any) =>{
        this.gs.loading=false;
        if (r.code === '200'){
          this.storagePool = r.data;
          const allName = this.storagePool.map(item => item.name);
          this.volStoragePoolFilter.initAllName(allName);
          this.poolTotal=this.storagePool.length==null?0:this.storagePool.length;
          this.storageListInitHandle();
          this.cdr.detectChanges();
          this.liststoragepoolperformance();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.storagePool === null){
        this.gs.loading=true;
        this.detailService.getStoragePoolList(this.storageId).subscribe((r: any) =>{
          this.gs.loading=false;
          if (r.code === '200'){
            this.storagePool = r.data;
            const allName = this.storagePool.map(item => item.name);
            this.volStoragePoolFilter.initAllName(allName);
            this.poolTotal=this.storagePool.length==null?0:this.storagePool.length;
            this.storageListInitHandle();
            this.cdr.detectChanges();
            this.liststoragepoolperformance();
          }
        });
      }
    }
  }

  /**
   * 存储池数据初始化
   */
  storageListInitHandle() {
    if(this.storagePool) {
      this.storagePool.forEach(item => {
        // switch (item.tier1RaidLv) {
        //   case "1":
        //     item.tier1RaidLvDesc = "RAID10";
        //     break;
        //   case "2":
        //     item.tier1RaidLvDesc = "RAID5";
        //     break;
        //   case "3":
        //     item.tier1RaidLvDesc = "RAID0";
        //     break;
        //   case "4":
        //     item.tier1RaidLvDesc = "RAID1";
        //     break;
        //   case "5":
        //     item.tier1RaidLvDesc = "RAID6";
        //     break;
        //   case "6":
        //     item.tier1RaidLvDesc = "RAID50";
        //     break;
        //   case "7":
        //     item.tier1RaidLvDesc = "RAID3";
        //     break;
        //   case "11":
        //     item.tier1RaidLvDesc = "RAIDTP";
        //     break;
        //   default:
        //     item.tier1RaidLvDesc = item.tier1RaidLv;
        //     break;
        // }
        // switch (item.physicalType) {
        //   case "sata":
        //     item.physicalTypeDesc = "SATA";
        //     break;
        //   case "sas":
        //     item.physicalTypeDesc = "SAS";
        //     break;
        //   case "ssd":
        //     item.physicalTypeDesc = "SSD";
        //     break;
        //   case "nl-sas":
        //     item.physicalTypeDesc = "NL-SAS";
        //     break;
        //   case "unknown":
        //     item.physicalTypeDesc = this.translatePipe.transform('storage.detail.storagePool.unknown');
        //     break;
        //   default:
        //     item.physicalTypeDesc = item.physicalType;
        //     break;
        // }
        switch (item.mediaType) {
          case "file":
            item.mediaTypeDesc = this.translatePipe.transform('enum.type.file');
            break;
          case "block":
            item.mediaTypeDesc = this.translatePipe.transform('enum.type.block');
            break;
          default:
            item.mediaTypeDesc = item.mediaType;
            break;
        }
      });
    }
  }
  liststoragepoolperformance(){
    if (this.storagePool === null || this.storagePool.length <= 0){ return; }
    const storagePoolIds = [];
    this.storagePool.forEach(item => {
      storagePoolIds.push(item.storageInstanceId);
    });
    this.storagePoolIds = storagePoolIds;
    this.detailService.liststoragepoolperformance(this.storagePoolIds).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: StoragePool [] = result.data;
        if ( chartList !== null && chartList.length > 0){
          this.storagePool.forEach(item => {
            chartList.forEach(charItem => {
              if (item.storageInstanceId === charItem.id){
                item.maxBandwidth=charItem.maxBandwidth;
                item.maxIops=charItem.maxIops;
                item.maxLatency=charItem.maxLatency;
              }
            });
          });
          this.cdr.detectChanges();
        }
      }
    });
  }
  refreshVolList() {
    this.volStatusFilter.initStatus();
    this.portTypeFilter.initType();
    this.mapStatusFilter.initStatus();
    this.volStoragePoolFilter.initPoolNameStatus();
    this.volServiceLevelFilter.initServiceLevel();
    this.volProtectionStatusFilter.initProtectionStatus();
    this.getStorageVolumeList(true);
  }
  getStorageVolumeList(fresh: boolean){
    console.log('paginationVolume');
    const reqParams = {
      storageId: this.storageId,
      pageSize: this.volumePageSize,
      pageNo: (this.volumeCurrentPage - 1) * this.volumePageSize,
    }
    console.log('reqParams', reqParams)
    if (fresh){
      this.gs.loading=true;
      this.detailService.getVolumeListListByPage(reqParams).subscribe((r: any) => {
        this.gs.loading=false;
        console.log("result", r);
        if (r.code === '200'){

          this.volumes = r.data.volumeList;
          this.volumeTotal=r.data.count;

          // 设置总页数、footer
          this.setVolumeTotalAndFooter();

          // this.totalPageNum = '/' + ' ' + Math.ceil(this.volumeTotal/(query ? query.pageSize:10));
          console.log('this.totalPageNum', this.totalPageNum);
          if (this.volumeRadio === 'table2') {
            this.listVolumesperformance();
          }
        }
        this.cdr.detectChanges();
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.volumes == null){
        this.gs.loading=true;
        this.detailService.getVolumeListListByPage(reqParams).subscribe((r: any) => {
          this.gs.loading=false;
          console.log("result", r);
          if (r.code === '200'){
            this.volumes = r.data.volumeList;
            this.volumeTotal=r.data.count;

            // 设置总页数、footer
            this.setVolumeTotalAndFooter();
            // this.totalPageNum = '/' + ' ' + Math.ceil(this.volumeTotal/(query ? query.pageSize:10));
            console.log('this.totalPageNum', this.totalPageNum);

            if (this.volumeRadio === 'table2') {
              this.listVolumesperformance();
            }
          }
          this.cdr.detectChanges();
        });
      }
    }
  }
  listVolumesperformance(){
    if (this.volumes === null || this.volumes.length <= 0){ return; }

    const volumeIds = [];
    this.volumes.forEach(item => {
      volumeIds.push(item.wwn);
    });
    this.volumeIds = volumeIds;
    this.gs.loading=true;
    this.detailService.listVolumesperformance(this.volumeIds).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: Volume [] = result.data;
        if ( chartList !== null && chartList.length > 0){
          this.volumes.forEach(item => {
            chartList.forEach(charItem => {
              if (item.wwn === charItem.wwn){
                item.bandwith=charItem.bandwith;
                item.iops=charItem.iops;
                item.lantency=charItem.lantency;
              }
            });
          });
        }
      }
      this.gs.loading=false;
      this.cdr.detectChanges();
    });
  }
  refreshFileSystem() {
    this.fsStatusFilter.initStatus();
    this.fsTypeFilter.initType();
    this.getFileSystemList(true);
  }
  getFileSystemList(fresh: boolean){
    if (fresh){
      this.gs.loading=true;
      this.detailService.getFileSystemList(this.storageId).subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.fsList = r.data;
          this.fsTotal=this.fsList==null?0:this.fsList.length;
        }
        this.cdr.detectChanges();
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.fsList == null){
        this.gs.loading=true;
        this.detailService.getFileSystemList(this.storageId).subscribe((r: any) => {
          this.gs.loading=false;
          if (r.code === '200'){
            this.fsList = r.data;
            this.fsTotal=this.fsList==null?0:this.fsList.length;
          }
          this.cdr.detectChanges();
        });
      }
    }
  }
  refreshDtree() {
    this.dtreeSecModFilter.initSecMod();
    this.dtreeQuotaFilter.initQuota();
    this.getDtreeList(true);
  }
  getDtreeList(fresh: boolean){
    if (fresh){
      this.gs.loading=true;
      this.detailService.getDtreeList(this.storageId).subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.dtrees = r.data;
          this.dtreeTotal=this.dtrees==null?0:this.dtrees.length;
        }
        this.cdr.detectChanges();
      });
    }else {
      if (this.dtrees == null){
        // 此处防止重复切换tab每次都去后台请求数据
        this.gs.loading=true;
        this.detailService.getDtreeList(this.storageId).subscribe((r: any) => {
          this.gs.loading=false;
          if (r.code === '200'){
            this.dtrees = r.data;
            this.dtreeTotal=this.dtrees==null?0:this.dtrees.length;
          }
          this.cdr.detectChanges();
        });
      }
    }
  }
  getShareList(fresh: boolean){
    console.log(11)
    if (fresh){
      console.log(22)
      this.gs.loading=true;
      this.detailService.getShareList(this.storageId).subscribe((r: any) => {
        this.gs.loading=false;
        console.log(33)
        if (r.code === '200'){
          this.shares = r.data;
          this.shareTotal=this.shares==null?0:this.shares.length;
        }
        this.cdr.detectChanges();
      });
    }else {
      console.log(44)
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.shares == null){
        console.log(55)
        this.gs.loading=true;
        this.detailService.getShareList(this.storageId).subscribe((r: any) => {
          this.gs.loading=false;
          console.log(66)
          if (r.code === '200'){
            this.shares = r.data;
            this.shareTotal=this.shares==null?0:this.shares.length;
          }
          this.cdr.detectChanges();
        });
      }
    }
  }
  getControllerList(fresh: boolean){
    if (fresh){
      this.gs.loading=true;
      this.detailService.getControllerList(this.storageId).subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.controllers = r.data;
          this.conTotal=this.controllers==null?0:this.controllers.length;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.controllers == null){
        this.gs.loading=true;
        this.detailService.getControllerList(this.storageId).subscribe((r: any) => {
          this.gs.loading=false;
          if (r.code === '200'){
            this.controllers = r.data;
            this.conTotal=this.controllers==null?0:this.controllers.length;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getDisksList(fresh: boolean){
    if (fresh){
      this.gs.loading=true;
      this.detailService.getDiskList(this.storageId).subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.disks = r.data;
          this.diskTotal=this.disks==null?0:this.disks.length;
          this.cdr.detectChanges();
        }
      });
    }else {
      // 此处防止重复切换tab每次都去后台请求数据
      if (this.disks == null){
        this.gs.loading=true;
        this.detailService.getDiskList(this.storageId).subscribe((r: any) => {
          this.gs.loading=false;
          if (r.code === '200'){
            this.disks = r.data;
            this.diskTotal=this.disks==null?0:this.disks.length;
            this.cdr.detectChanges();
          }
        });
      }
    }
  }
  getPortsList(){
    this.getFCPortList();
    this.getFCoEPortList();
    this.getEthernetPortList();
    this.getBondPortList();
    this.getLogicPortsList();
    this.getFailoverGroups();
  }
  getFCPortList(){
    this.gs.loading=true;
      this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"FC"}).subscribe((r: any) => {
        this.gs.loading=false;
        if (r.code === '200'){
          this.fcs = r.data;
          this.fcTotal=this.fcs==null?0:this.fcs.length;
          this.cdr.detectChanges();
        }
      });
  }
  getFCoEPortList(){
    this.gs.loading=true;
    this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"FCoE"}).subscribe((r: any) => {
      this.gs.loading=false;
      if (r.code === '200'){
        this.fcoes = r.data;
        this.fcoeTotal=this.fcoes==null?0:this.fcoes.length;
        this.cdr.detectChanges();
      }
    });
  }
  getEthernetPortList(){
    this.gs.loading=true;
    this.detailService.getFCPortList({"storageDeviceId":this.storageId,"portType":"ETH"}).subscribe((r: any) => {
      this.gs.loading=false;
      if (r.code === '200'){
        this.eths = r.data;
        this.ethTotal=this.eths==null?0:this.eths.length;
        this.cdr.detectChanges();
      }
    });
  }
  getBondPortList(){
    this.gs.loading=true;
    this.detailService.getBondPortList(this.storageId).subscribe((r: any) => {
      this.gs.loading=false;
      if (r.code === '200'){
        this.bonds = r.data;
        this.bondTotal=this.bonds==null?0:this.bonds.length;
        this.cdr.detectChanges();
      }
    });
  }
  getLogicPortsList(){
    this.gs.loading=true;
    this.detailService.getLogicPortList(this.storageId).subscribe((r: any) => {
      this.gs.loading=false;
      if (r.code === '200'){
        this.logicports = r.data;
        this.logicTotal=this.logicports==null?0:this.logicports.length;
        this.cdr.detectChanges();
      }
    });
  }
  getFailoverGroups(){
    this.gs.loading=true;
    this.detailService.getFailoverGroups(this.storageId).subscribe((r: any) => {
      this.gs.loading=false;
      if (r.code === '200'){
        this.fgs = r.data;
        this.failTotal=this.fgs==null?0:this.fgs.length;
        this.cdr.detectChanges();
      }
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
  initCapacity(){
    this.initCapacityDistribution();
    this.buildCapacitySavings();
  }

  buildCapacitySavings(){
    this.capSave=new CapacitySavings();
    const usedCapacity=this.detail.usedCapacity; //默认单位是GB
    const beforeSave=Math.trunc(usedCapacity);//先取整数
    if(beforeSave< 4096){
      this.capSave.unit="GB";
      this.capSave.max=beforeSave+4-beforeSave%4;
      this.capSave.beforeSave=this.detail.usedCapacity;
      this.capSave.dedupe=this.detail.dedupedCapacity;
      this.capSave.compression=this.detail.compressedCapacity;
      this.capSave.afterSave=this.detail.optimizeCapacity;
    }else{
      this.capSave.unit="TB";

      this.capSave.beforeSave=this.detail.usedCapacity?this.detail.usedCapacity/1024:null;
      this.capSave.dedupe=this.detail.dedupedCapacity?this.detail.dedupedCapacity/1024:null;
      this.capSave.compression=this.detail.compressedCapacity?this.detail.compressedCapacity/1024:null;
      this.capSave.afterSave=this.detail.optimizeCapacity?this.detail.optimizeCapacity/1024:null;
      const max=Math.trunc(beforeSave/1024);
      this.capSave.max=max+4-max%4;
    }
    const bars=[0];
    for(var i=0;i<4;i++){
      bars.push(this.capSave.max/4*(i+1))
    }
    this.capSave.bars=bars;
    this.capSave.rate=(this.capSave.beforeSave/this.capSave.afterSave).toFixed(0) +": 1";
    console.log('capSave',this.capSave);

  }
  initCapacityDistribution(){
    this.cd = new CapacityDistribution();
    const p= 0;
    this.detail.protectionCapacity
    this.cd.protection = this.formatCapacity(this.detail.protectionCapacity);
    this.cd.fileSystem =this.formatCapacity(this.detail.fileCapacity);
    this.cd.volume =this.formatCapacity(this.detail.blockCapacity);
     this.cd.freeCapacity= this.getFreeCapacity(this.detail.totalEffectiveCapacity,this.detail.usedCapacity);

    const cc = new CapacityChart(this.formatCapacity(this.detail.totalEffectiveCapacity));
    const cs = new CapacitySerie(this.detail.protectionCapacity
      ,this.detail.fileCapacity,this.detail.blockCapacity,
      this.detail.totalEffectiveCapacity-this.detail.usedCapacity);
    cc.series.push(cs);
    this.cd.chart = cc;
  }
  backToList(){
    this.router.navigate(['storage']);
  }
  parseUsage(c:string){
    if (c===null || c==='') return 0;
    return Number.parseFloat(c).toFixed(2);
  }
  formatArry(str: string){
    if(str===null){
      return '';
    }
    str=str.replace("[","")
      .replace("]","");
    const strs=str.split(",");
    let r=""
    strs.forEach(s=>{
      r+=s.replace('"','').replace('"','')+",";
    });
    return r.substr(0,r.length-1);
  }
  getFreeCapacity(t:number,u:number){
    if(t==null ||t==0) return this.formatCapacity(0);
    if(u==null || u==0){
      return this.formatCapacity(t);
    }
    return this.formatCapacity(t-u);
  }

  /**
   * 设置折线图 ( 折线1 虚线UpperLine、折线2 虚线LowerLine、
   * 折线3Read、折线4Write)
   * @param height
   * @param title 标题
   * @param subtext 副标题
   * @param indicatorIds  获取参数指标（带宽的读写等） 0 读 1写
   * @param objIds 卷ID（vmfs）、fsId(nfs) 只能放一个值即length为1
   * @param range 时间段 LAST_5_MINUTE LAST_1_HOUR LAST_1_DAY LAST_1_WEEK LAST_1_MONTH LAST_1_QUARTER HALF_1_YEAR LAST_1_YEAR BEGIN_END_TIME INVALID
   * @param url 请求url
   */
  setChart(height: number, title: string, subtext: string, indicatorIds: any[], objIds: any[], range: string, url: string, startTime:string, endTime:string) {
    // 生成chart optiond对象
    const chart:ChartOptions = this.getNewChart(height, title, subtext);
    return new Promise((resolve, reject) => {
      const params = {
        indicator_ids: indicatorIds,
        obj_ids: objIds,
        range: range,
        begin_time: startTime,
        end_time: endTime,
      }
      this.nfsService.getLineChartData(url, params).subscribe((result: any) => {
        console.log('chartData: ', title, result);
        if (result.code === '200' && result.data !== null && result.data !== null) {
          const resData = result.data;
          // 设置标题
          chart.title.text = title;
          // 设置副标题
          chart.title.subtext = subtext;
          // 上限对象
          const upperData = resData[objIds[0]][indicatorIds[0]];
          // 下限对象
          const lowerData = resData[objIds[0]][indicatorIds[1]];
          // 上限最大值
          const pmaxData = this.makePerformance.getUpperOrLower(upperData, 'upper');
          // 下限最大值
          let lmaxData = this.makePerformance.getUpperOrLower(lowerData, 'lower');
          // 上、下限数据
          const uppers: any[] = upperData.series;
          const lower: any[] = lowerData.series;
          // 设置X轴
          this.makePerformance.setXAxisData(uppers, chart);
          // 设置y轴最大值
          chart.yAxis.max = (pmaxData > lmaxData ? pmaxData : lmaxData);
          console.log('chart.yAxis.pmaxData', pmaxData);
          console.log('chart.yAxis.lmaxData', lmaxData);
          // 设置Read 折线图数据
          uppers.forEach(item => {
            for (const key of Object.keys(item)) {
              // chartData.value = item[key];
              chart.series[0].data.push({value: Number(item[key]), symbol: 'none'});
            }
          });
          // 设置write 折线图数据
          lower.forEach(item => {
            for (const key of Object.keys(item)) {
              chart.series[1].data.push({value: Number(item[key]), symbol: 'none'});
            }
          });
          resolve(chart);
        } else {
          console.log('get chartData fail: ', result.description);
        }
      });
    });
  }
  /**
   * 获取一个chart的option对象 (option格式 折线1 虚线UpperLine、折线2 虚线LowerLine、
   * 折线3Read、折线4Write)
   * @param height
   * @param title
   * @param subtext
   */
  getNewChart(height: number, title: string, subtext: string) {
    const chart: ChartOptions = new ChartOptions();
    // 高度
    chart.height = height;
    // 标题
    const titleInfo:Title = new Title();
    titleInfo.text = title;
    titleInfo.subtext = subtext;
    titleInfo.textAlign = 'bottom';
    const textStyle:TextStyle  = new TextStyle();
    textStyle.fontStyle = 'normal';
    titleInfo.textStyle = textStyle;

    chart.title = titleInfo;

    // x轴
    const xAxis: XAxis = new XAxis();
    xAxis.type = 'category';
    xAxis.boundaryGap = false;
    xAxis.data = [];

    chart.xAxis = xAxis;

    // y轴
    const yAxis: YAxis = new YAxis();
    yAxis.type = 'value';
    yAxis.min = 0;
    yAxis.splitNumber = 2;
    yAxis.boundaryGap = ['50%', '50%'];
    const axisLine: AxisLine = new AxisLine();
    axisLine.show = false;
    yAxis.axisLine = axisLine;
    const splitLine = new SplitLine();
    splitLine.show = true;
    const lineStyle = new LineStyle();
    lineStyle.type = 'dashed';
    splitLine.lineStyle = lineStyle;
    yAxis.splitLine = splitLine;

    chart.yAxis = yAxis;
    // 提示框
    const tooltip: Tooltip = new Tooltip();
    tooltip.trigger = 'axis';
    tooltip.formatter = '{b} <br/> {a0}: {c0}<br/>{a1}: {c1}<br/>';
    const axisPointer: AxisPointer = new AxisPointer();
    axisPointer.axis = 'x';
    axisPointer.type = 'line';
    tooltip.axisPointer = axisPointer;
    chart.tooltip = tooltip;

    // 指标
    const legend: Legend = new Legend();
    const legendData: LegendData[] = [];
    legendData.push(this.makePerformance.setLengdData('Read', 'circle'));
    legendData.push(this.makePerformance.setLengdData('Write', 'circle'));
    legend.x = 'right';
    legend.y = 'top';
    legend.selectedMode  = true;
    legend.data = legendData;

    chart.legend = legend;
    // 指标颜色
    const colors:string[] = ['#6870c4', '#01bfa8'];
    chart.color = colors;

    // 数据(格式)
    const series: Serie[] = [];
    series.push(this.makePerformance.setSerieData('Read', 'line', true, 'solid', '#6870c4', null))
    series.push(this.makePerformance.setSerieData('Write', 'line', true, 'solid', '#01bfa8', null))

    chart.series = series;

    return chart;
  }

  /**
   * 开始结束时间触发
   */
  changeDate() {
    if (!this.rangeTime.controls.start.hasError('matStartDateInvalid')
      && !this.rangeTime.controls.end.hasError('matEndDateInvalid')
      && this.rangeTime.controls.start.value !== null && this.rangeTime.controls.end.value !== null) { // 需满足输入规范且不为空
      this.startTime = this.rangeTime.controls.start.value._d.getTime();
      this.endTime = this.rangeTime.controls.end.value._d.getTime();
      console.log('startTime', this.startTime);
      console.log('endTime', this.endTime);
      this.changeFunc();
    } else {
      return;
    }
  }

  // 切换卷函数
  changeFunc() {
    console.log(this.selectRange);
    if (this.selectRange === 'BEGIN_END_TIME') {
      if (this.startTime === null || this.endTime === null) {
        console.log('开始结束时间不能为空');
        return;
      }
    } else { // 初始化开始结束时间
      this.startTime = null;
      this.endTime = null;
    }
    if (this.selectRange) {
      console.log('this.selectVolName+this.selectRange', this.selectRange);
      // 请求后台重新加载折线图
      this.initChart();
    } else {
      console.log('未选择卷或range');
    }
  }
  params(query: any = {}) { // 对query进行处理
    const p = Object.assign({}, query);
    return p;
  }
  getVolDataByPage(pagination:any) {
    this.getStorageVolumeList(true);
    return pagination;
  }

  /**
   * 卷翻页
   * @param object
   * @param pageObj
   */
  jumpPage(object:any) {
    const obj = object.target;
    if (object.keyCode === 13 || object.keyCode === 108 || object.type === 'blur') { // 按下enter键触发
      if(obj.value && obj.value > 0 && obj.value  != this.volumeCurrentPage) {
        obj.value=obj.value.replace(/[^1-9]/g,'');

        if (obj.value <= this.volumeTotalPage) {
          this.volumeCurrentPage = obj.value;
        } else {
          this.volumeCurrentPage = this.volumeTotalPage;
          obj.value =  this.volumeCurrentPage;
        }
        this.getStorageVolumeList(true);
      }else{
        obj.value=this.volumeCurrentPage;
      }
    }
  }

  /**
   * 卷信息列表 当前页显示数据change函数
   * @param object
   */
  pageSizeChange(object:any) {
    this.volumeCurrentPage = 1;
    this.getStorageVolumeList(true);
  }
  /**
   * 卷信息列表 首页
   * @param object
   */
  volmeFirstPage() {
    if (this.volumeCurrentPage !== 1) {
      this.volumeCurrentPage = 1;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 尾页
   * @param object
   */
  volmeLastPage() {
    if (this.volumeCurrentPage !== this.volumeTotalPage) {
      this.volumeCurrentPage = this.volumeTotalPage;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 上一页
   */
  volumePreviousPage() {
    if (this.volumeCurrentPage !== 1) {
      this.volumeCurrentPage = this.volumeCurrentPage - 1;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 下一页
   */
  volumeNextPage() {
    if (this.volumeCurrentPage !== this.volumeTotalPage) {
      this.volumeCurrentPage = this.volumeCurrentPage + 1;
      this.getStorageVolumeList(true);
    }
  }

  /**
   * 卷信息列表 设置页码总数、footer
   */
  setVolumeTotalAndFooter() {
    this.volumeTotalPage = Math.ceil(this.volumeTotal/this.volumePageSize);
    const last = this.volumeTotalPage === this.volumeCurrentPage ?
      this.volumeTotal:this.volumeCurrentPage*this.volumePageSize;
    const first = (this.volumeCurrentPage - 1)*this.volumePageSize + 1;
    this.volumeFooter = first + ' - ' + last;
  }
}
