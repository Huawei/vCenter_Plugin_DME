import {
  Component,
  OnInit,
  AfterViewInit,
  OnDestroy,
  ChangeDetectionStrategy,
  ChangeDetectorRef,
  NgZone, ViewChild,
} from '@angular/core';
import { DashboardService } from './dashboard.srevice';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ClrForm} from '@clr/angular';
import {HttpClient} from '@angular/common/http';
import {Router} from "@angular/router";
import {TranslatePipe, TranslateService} from "@ngx-translate/core";
import { GlobalsService }     from "../../shared/globals.service";
import {NfsService} from "../nfs/nfs.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styles: [
    `
      .mat-raised-button {
        margin-right: 8px;
        margin-top: 8px;
      }
    `,
  ],
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./dashboard.component.scss'],
  providers: [DashboardService, TranslateService, TranslatePipe],
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {

  storageNumChart = null;
  storageNum = {
    total: 0,
    normal: 0,
    abnormal: 0
  };

  storageCapacityChart= null;
  storageCapacity = {
    totalCapacity: 0,
    usedCapacity: 0,
    freeCapacity: 0,
    utilization: 0,
    capacityUnit: "TB"
  };

  bestPracticeViolations = {
        critical : 0,
        major: 0,
        warning: 0,
        info: 0
  };

  capadataStoreName = this.translateService.instant("overview.allDataStore");
  top5dataStoreName = this.translateService.instant("overview.allDataStore");
  storeageTopN = [];

  popShow = false;
  connectAlertSuccess = false;
  connectAlertFail = false;
  connectModel = { hostIp: '', port: '26335', userName: '', password: '', hostPort: ''};
  hostModel = { hostIp: '', hostPort: '', code: ''};

  bestShowLoading = false;
  top5ShowLoading = false;

  connectForm = new FormGroup({
    port: new FormControl('', [
        Validators.required,
        Validators.pattern('^[0-9]*$')]),
    ip: new FormControl('', [
        Validators.required,
        Validators.pattern('((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)')]),
    username: new FormControl('', [
        Validators.required,
        Validators.maxLength(100)]),
    password: new FormControl('', [
        Validators.required])
  });
  @ViewChild(ClrForm, {static: true}) clrForm;


  constructor(
    private dashboardSrv: DashboardService,
    private translateService: TranslateService,
    private ngZone: NgZone,
    private http: HttpClient,
    private cdr: ChangeDetectorRef,
    private router:Router,
    public gs: GlobalsService,
    private translatePipe:TranslatePipe
  ) {}

  ngOnInit() {}

  ngAfterViewInit() {
    this.refresh();
    // this.ngZone.runOutsideAngular(() => this.initChart());
  }

  ngOnDestroy() {
  }

  refresh(){
    this.gs.loading = true;
    this.http.get('accessdme/refreshaccess', {}).subscribe((result: any) => {
      this.hostModel = result.data;
      console.log('this.hostModel', this.hostModel);
      console.log('this.hostModel.code', result.code== '200');
      if (result.code == '200'){
        this.gs.loading = false  // 设置全局loading 为 FALSE
        this.loadStorageNum();
        this.loadstorageCapacity('0', 'overview.allDataStore');
        this.loadBestPracticeViolations();
        this.loadTop5DataStore('0', 'overview.allDataStore');
      } else{
        this.gs.loading = false;
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    }, err => {
      console.error('ERROR', err);
    });
  }

  // //type 0 :VMFS and NFS, 1:VMFS, 2:NFS
  loadTop5DataStore(type: string, name: string){
    this.top5dataStoreName = this.translateService.instant(name);
    this.top5ShowLoading = true;
    this.http.get('overview/getdatastoretopn', { params: {type: type}}).subscribe((result: any) => {
      this.top5ShowLoading = false;
      if (result.code === '200'){
        result.data.forEach((item) => {
          item.totalCapacity = item.totalCapacity.toFixed(2);
          item.usedCapacity = item.usedCapacity.toFixed(2);
          item.freeCapacity = item.freeCapacity.toFixed(2);
          item.utilization = item.utilization.toFixed(2);
        });
        this.storeageTopN = result.data;
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
  }


  storageCapacityChartInit(chart){
    this.storageCapacityChart = chart;
  }
  loadstorageCapacity(type: string, name: string){
    this.capadataStoreName = this.translateService.instant(name);
    this.storageCapacityChart.showLoading();
    this.http.get('overview/getdatastoreoverview', { params: {type: type}}).subscribe((result: any) => {
      let os;
      if (result.code === '200'){
        result.data.totalCapacity = result.data.totalCapacity.toFixed(2);
        result.data.usedCapacity = result.data.usedCapacity.toFixed(2);
        result.data.freeCapacity = result.data.freeCapacity.toFixed(2);
        result.data.utilization = result.data.utilization.toFixed(2);
        this.storageCapacity = result.data;
        os = [
          {
            name: this.translatePipe.transform('overview.used'),
            value: result.data.usedCapacity
          },
          {
            name: this.translatePipe.transform('overview.free'),
            value: result.data.freeCapacity
          }
        ];
      } else {
        os = [
          {
            name: this.translatePipe.transform('overview.used'),
            value: 0
          },
          {
            name: this.translatePipe.transform('overview.free'),
            value: 0
          }
        ];
      }

      this.dashboardSrv.storageCapacityOption.series[0].data = os;
      this.dashboardSrv.storageCapacityOption.title.text = this.storageCapacity.utilization + ' %';
      this.storageCapacityChart.setOption(this.dashboardSrv.storageCapacityOption, true);
      this.storageCapacityChart.hideLoading();
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
  }

  storageNumChartInit(chart){
    this.storageNumChart = chart;
  }

  loadStorageNum(){
    this.storageNumChart.showLoading();
    this.http.get('overview/getstoragenum', {}).subscribe((result: any) => {
      let os;
      if (result.code === '200'){
        this.storageNum = result.data;
        os = [
          {
            name: this.translatePipe.transform('overview.normal'),
            value: result.data.normal
          },
          {
            name: this.translatePipe.transform('overview.abnormal'),
            value: result.data.abnormal
          }
        ];
      } else {
        os = [
          {
            name: this.translatePipe.transform('overview.normal'),
            value: 0
          },
          {
            name: this.translatePipe.transform('overview.abnormal'),
            value: 0
          }
        ];
      }
      this.dashboardSrv.storageNumOption.series[0].data = os;
      this.storageNumChart.setOption(this.dashboardSrv.storageNumOption, true);
      this.storageNumChart.hideLoading();
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadBestPracticeViolations(){
    this.bestShowLoading = true;
    this.http.get('overview/getbestpracticeviolations', {}).subscribe((result: any) => {
      this.bestShowLoading = false;
      if (result.code === '200'){
        this.bestPracticeViolations = result.data;
        this.cdr.detectChanges();
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  // 提交表单连接DME
  submit() {
    if (this.connectForm.invalid) {
      this.clrForm.markAsTouched();
    } else {
      this.gs.loading = true;
      this.connectModel.hostPort = this.connectModel.port;
      this.http.post('accessdme/access', this.connectModel).subscribe((result: any) => {
        this.gs.loading = false;
        if (result.code == '200'){
          this.refresh();
          this.connectAlertSuccess = true;
          this.popShow = false;
        } else{
          this.connectAlertFail = true;
        }
        this.cdr.detectChanges();
      });
      this.resetForm();
    }
  }

  resetForm() {
    const connectModel = { hostIp: '', port: '26335', userName: '', password: '', hostPort: ''}
    this.connectForm.reset(connectModel);
  }

  showPop(){
    this.popShow = true;
    this.resetForm();
  }

  //跳转页面的方法
  toBestParcticeView(type){
    this.router.navigate(['bestpractice'],{
      queryParams:{
        type: type
      }
    });
  }

  toDatastoreDeviceView(){
    this.router.navigate(['storage'],{
      queryParams:{
      }
    });
  }


  toBestParcticeViewSdk(type: string){
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'com.huawei.dmestore.bestpractiseView'
    });
  }

  toDatastoreDeviceViewSdk(){
    this.gs.getClientSdk().app.navigateTo({
      targetViewId: 'com.huawei.dmestore.storageView'
    });
  }
}
