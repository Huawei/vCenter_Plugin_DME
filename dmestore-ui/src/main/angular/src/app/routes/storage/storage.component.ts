import {
  Component,
  OnInit,
  AfterViewInit,
  ChangeDetectionStrategy,
  ChangeDetectorRef, NgZone, ViewChild
} from '@angular/core';
import {StorageService, StorageList} from './storage.service';
import { Router} from "@angular/router";
import { StorageStatusFilter} from "./filter.component";
@Component({
  selector: 'app-storage',
  templateUrl: './storage.component.html',
  styleUrls: ['./storage.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [StorageService],
})
export class StorageComponent implements OnInit, AfterViewInit {

  list: StorageList[] = []; // 数据列表
  total = 0; // 总数据数量
  isLoading = true; // table数据loading
  rowSelected = []; // 当前选中数据
  radioCheck = 'table1'; // 切换列表页显示
  buttonTrigger ='list'; // 切换列表页显示
  obj_ids=[];
  @ViewChild('storageStatusFilter') storageStatusFilter:StorageStatusFilter;
  constructor(private remoteSrv: StorageService, private cdr: ChangeDetectorRef, private ngZone: NgZone,private router:Router) {}
  // 生命周期： 初始化数据
  ngOnInit() {
    this.refresh();
  }

  ngAfterViewInit() {

  }

  scanData() {
    console.log("this.storageStatusFilter", this.storageStatusFilter);
    this.storageStatusFilter.initStatus();
    this.refresh();
  }
  // table数据处理
  refresh() {
    this.isLoading = true;

    this.remoteSrv.getData()
        .subscribe((result: any) => {
          this.list = result.data;
          this.total = result.total_count;
          this.isLoading = false;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          //this.getStrageCharts();
          this.listperformance();
        });
  }
  // 性能视图列表
  listperformance(){
    if (this.list === null || this.list.length <= 0){ return; }
    this.list.forEach(item => {
      this.obj_ids.push(item.id);
    });
    this.remoteSrv.listperformance(this.obj_ids).subscribe((result: any) => {
      if (result.code === '200'){
        const chartList: StorageList [] = result.data;
        if ( chartList !== null && chartList.length > 0){
          this.list.forEach(item => {
            chartList.forEach(charItem => {
              if (item.id === charItem.id){
                item.maxOps=charItem.maxOps;
              }
            });
          });
          this.cdr.detectChanges();
        }
      }
    });
  }
  check(param){
    console.log(param);
    this.radioCheck = param;
    this.cdr.detectChanges();
  }
  //跳转详情页面的方法
  toDetailView(id,name){
    this.router.navigate(['storage/detail'],{
      queryParams:{
        id,name
      }
    });
  }
  // //组装存储列表的表格数据
  // getStrageCharts(){
  //
  //   this.strorageCharts = [];
  //   if (this.list !== null && this.list.length > 0){
  //     this.list.forEach(s=>{
  //       const chart = new StorageChart();
  //       chart.id = s.id;
  //       chart.name = s.name;
  //       chart.ip = s.ip;
  //       chart.model = s.model;
  //       chart.capacity  = this.formatCapacity(s.totalPoolCapacity);
  //       chart.usedCapacity = this.formatCapacity(s.usedCapacity);
  //       chart.freeCapacity = this.formatCapacity(s.totalPoolCapacity-s.usedCapacity);
  //       chart.alarms = 2;
  //       chart.events = 1;
  //       const title=((s.usedCapacity/s.totalPoolCapacity)*100).toFixed(2)+"%";
  //       const cc = new CapacityChart(title);
  //       const cs = new CapacitySerie(Number.parseInt(chart.usedCapacity),Number.parseInt(chart.freeCapacity),null,null);
  //       cc.series.push(cs);
  //       chart.chart=cc;
  //       const d=[120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120, 200, 150, 80, 70, 110, 130, 120];
  //       const iops =new PerforChart(d);
  //       const bandwidth =new PerforChart(d);
  //       chart.iops=iops;
  //       chart.bandwidth=bandwidth;
  //       this.strorageCharts.push(chart);
  //     })
  //   }
  // }
  // //状态转换
  // parseStatus(status: string,flag: number){
  //   let s:string = '';
  //   if (flag === 1){
  //     switch (Number.parseInt(status)){
  //       case 0: s='离线';break;
  //       case 1: s='正常';break;
  //       case 2: s='故障';break;
  //       case 9: s='未管理';break;
  //       default: s= '--';
  //     }
  //   }else if (flag === 2){
  //     switch (Number.parseInt(status)){
  //       case 0: s='未同步';break;
  //       case 1: s='同步中';break;
  //       case 2: s='已同步';break;
  //       default: s= '--';
  //     }
  //   }
  //   return s;
  // }
  // 容量单位转换
  formatCapacity(c: number){
    if(c==null) return '--';
    if (c < 1024){
      return c.toFixed(3)+" MB";
    }else if(c >= 1024 && c< 1048576){
      return (c/1024).toFixed(3) +" GB";
    }else if(c>= 1048576){
      return (c/1024/1024).toFixed(3)+" TB"
    }
  }
}
