import {Component, NgZone, OnInit, ChangeDetectorRef} from '@angular/core';
import { EChartOption } from 'echarts';
import { VmfsPerformanceService } from './performance.service';
import {NfsService, MakePerformance} from "../../nfs/nfs.service";
import {VolumeInfo} from "../volume-attribute/attribute.service";
import {FormControl, FormGroup} from "@angular/forms";
import {GlobalsService} from "@shared/globals.service";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-performance',
  templateUrl: './performance.component.html',
  styleUrls: ['./performance.component.scss'],
  providers: [VmfsPerformanceService, MakePerformance, NfsService, TranslatePipe],
})
export class PerformanceComponent implements OnInit{

  rangeTime = new FormGroup({
    start: new FormControl(),
    end: new FormControl()
  });
  // 创建表格对象
  // IOPS+QoS上下限
  iopsChart: EChartOption = {};
  // 带宽+QoS上下限
  bandwidthChart: EChartOption = {};
  // 响应时间+QoS下限
  latencyChart: EChartOption = {};

  // obj_type_id  (卷类型ID)
  objTypeId;
  // indicator_ids 获取参数指标（上下限等） 0 上限 1下限
  // obj_ids 卷ID
  // objIds: Array<string> = ['1282FFE20AA03E4EAC9A814C687B780A'];
  /*{
    "obj_type_id" : "1125921381679104",
    "indicator_ids" : ["1125921381744648","1125921381744649"],
    "obj_ids" : ["1282FFE20AA03E4EAC9A814C687B780A"],
    "interval" : "ONE_MINUTE",
    "range" : "LAST_1_DAY"
  }*/
  // interval 时间间隔单位 ONE_MINUTE MINUTE HALF_HOUR HOUR DAY WEEK MONTH
  interval;
  // range 时间段 LAST_5_MINUTE LAST_1_HOUR LAST_1_DAY LAST_1_WEEK LAST_1_MONTH LAST_1_QUARTER HALF_1_YEAR LAST_1_YEAR BEGIN_END_TIME INVALID
  range = 'LAST_1_DAY';
  // begin_time 开始时间 时间戳(例：1552477343834)
  // beginTime = 1552477343834;
  // end_time 结束时间 时间戳
  // endTime = 1552567343000;
  // 定时函数执行时间 默认一天
  timeInterval = 1 * 60 * 60 * 1000;
  // 卷ID
  // 卷信息
  volumeInfoList: VolumeInfo[];
  // 选中的卷数据
  selectVolume: VolumeInfo;
  // 选中的卷名称
  selectVolName: string;
  // 卷名称集合
  volNames: string[] = [];

  // ranges
  ranges = NfsService.perRanges;
  // select range
  selectRange;
  // startTime
  startTime = null;

  // endTime
  endTime = null;

  constructor(private nfsService: NfsService, private makePerformance: MakePerformance,
              private perService: VmfsPerformanceService, private ngZone: NgZone,
              private cdr: ChangeDetectorRef, private gs: GlobalsService, private translatePipe:TranslatePipe) {
  }


  ngOnInit(): void {
    // 初始化卷信息
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    // const objectId = 'urn:vmomi:Datastore:datastore-1212:674908e5-ab21-4079-9cb1-596358ee5dd1';
    const objectId=ctx[0].id;
    this.getVolumeInfoByVolID(objectId);
    this.selectRange = 'LAST_1_DAY';
  }
  // 初始化表格对象
  initChart() {

    console.log('this.rang', this.range)
    const volIds:string[] = [];
    volIds.push(this.selectVolume.wwn);
    // volIds.push('1282FFE20AA03E4EAC9A814C687B780A');
    // IOPS
    this.makePerformance.setChart(300,this.translatePipe.transform('vmfs.iops'), 'IO/s', NfsService.vmfsIOPS, volIds, this.selectRange, NfsService.vmfsUrl, this.startTime, this.endTime).then(res => {
      this.iopsChart = res;
      this.cdr.detectChanges();
    });

    // 带宽
    this.makePerformance.setChart(300,this.translatePipe.transform('vmfs.bandwidth'), 'MB/s', NfsService.vmfsBDWT, volIds, this.selectRange, NfsService.vmfsUrl, this.startTime, this.endTime).then(res => {
      this.bandwidthChart = res;
      this.cdr.detectChanges();
    });
    // 响应时间
    this.makePerformance.setChart(300,this.translatePipe.transform('vmfs.latency'), 'ms', NfsService.vmfsLatency, volIds, this.selectRange, NfsService.vmfsUrl, this.startTime, this.endTime).then(res => {
      this.latencyChart = res;
      this.cdr.detectChanges();
    });
  }
  // 切换卷函数
  changeVolFunc() {
    console.log(this.selectVolName && this.selectRange);
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
      console.log('this.selectVolName+this.selectRange', this.selectVolName, this.selectRange);
      // 获取已选择的卷
      this.selectVolume = this.makePerformance.getVolByName(this.selectVolName, this.volumeInfoList);
      // 请求后台重新加载折线图
      this.initChart();
    } else {
      console.log('未选择卷或range');
    }
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
      this.changeVolFunc();
    } else {
      return;
    }
  }

  getVolumeInfoByVolID(objectId: string){
    console.log('objectId: ' + objectId);
    this.perService.getData(objectId).subscribe((result: any) => {
      console.log('volumns:', result);
      if (result.code === '200') {
        this.volumeInfoList = result.data;
        this.volumeInfoList.forEach(item => {
          this.volNames.push(item.name);
        });
        // 设置默认选中数据
        this.selectVolName = this.volNames[0];
        this.selectVolume = this.getVolByName(this.selectVolName);

        this.initChart();
      } else {
        console.log(result.description);
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }
  // 通过名称获取卷信息
  getVolByName(name): any {
    const volumeInfo = this.volumeInfoList.filter(item  => item.name === name)[0];
    return volumeInfo;
  }
}
