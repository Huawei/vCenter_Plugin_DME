import {ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {ClrDatagridFilter, ClrDatagridFilterInterface, ClrDatagridStringFilterInterface} from "@clr/angular";
import {ServiceLevelList, VmfsInfo, VmfsListService} from "./list.service";
import {Subject} from "rxjs";
import {StorageList, StorageService} from "../../storage/storage.service";

@Component({
  selector: "vmfs-filter",
  template: `
      <clr-radio-container>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="" />
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="normal" />
          <label>{{'enum.status.normal' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="creating"/>
          <label>{{'enum.status.creating' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="mapping" />
          <label>{{'enum.status.mapping' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="unmapping" />
          <label>{{'enum.status.unmapping' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="deleting" />
          <label>{{'enum.status.deleting' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="error" />
          <label>{{'enum.status.error' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="options" value="expanding" />
          <label>{{'enum.status.expanding' | translate}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `,
  providers: [VmfsListService],
})
export class StatusFilter implements ClrDatagridFilterInterface<VmfsInfo> {
  changes = new Subject<any>();
  options;

  readonly status: any;

  accepts(item: VmfsInfo): boolean {
    if (!this.options) {
      return true;
    }
    const  capital  = item.status;
    console.log("capital", capital);
    if (this.options === '' || this.options === 'all') {
      return true;
    } else {
      return this.options === capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }
  initStatus() {
    this.options = undefined;
  }
}


@Component({
  selector: "device-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="device" (change)="changeFunc($event)" [(ngModel)]="device" value="" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper *ngFor="let item of storageList">
        <input type="radio" clrRadio name="device" (change)="changeFunc($event)" [(ngModel)]="device" value="{{item.name}}"/>
        <label>{{item.name}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
  providers: [VmfsListService, StorageService],
})
export class DeviceFilter implements ClrDatagridFilterInterface<VmfsInfo>, OnInit{
  constructor(private storageService: StorageService,private cdr: ChangeDetectorRef){}

  ngOnInit(): void {
    this.storageService.getData().subscribe((s: any) => {
      if (s.code === '200'){
        this.storageList = s.data;
        this.cdr.detectChanges();
      }
    });
  }
  changes = new Subject<any>();
  device;
  storageList: StorageList[] = [];
  readonly status: any;

  accepts(item: VmfsInfo): boolean {
    if (!this.device) {
      return true;
    }
    const  capital  = item.device;
    if (this.device === '') {
      return true;
    } else {
      return this.device === capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  initDevice() {
    this.device = undefined;
  }

}

@Component({
  selector: "serviceLevel-filter",
  template: `
      <clr-radio-container>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="serviceLevel" (change)="changeFunc($event)" [(ngModel)]="serviceLevel" value="all" />
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="serviceLevel" (change)="changeFunc($event)" [(ngModel)]="serviceLevel" value="" />
          <label>{{'vmfs.filter.empty' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper *ngFor="let item of serviceLevelList">
          <input type="radio" clrRadio name="serviceLevel" (change)="changeFunc($event)" [(ngModel)]="serviceLevel" value="{{item.name}}"/>
          <label>{{item.name}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `,
  providers: [VmfsListService],
})
export class ServiceLevelFilter implements ClrDatagridFilterInterface<VmfsInfo>, OnInit{


  changes = new Subject<any>();
  serviceLevel;
  serviceLevelList: ServiceLevelList[]; // 服务等级列表

  readonly status: any;

  constructor(private vmfsListService: VmfsListService,private cdr: ChangeDetectorRef){}

  ngOnInit(): void {
    this.vmfsListService.getServiceLevelList().subscribe((result: any) => {
      console.log(result);
      if (result.code === '200' && result.data !== null) {
        this.serviceLevelList = result.data.filter(item => item.totalCapacity !== 0);
        console.log('this.serviceLevelList', this.serviceLevelList);
      }
      this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
    });
  }

  accepts(item: VmfsInfo): boolean {
    if (this.serviceLevel == undefined) {
      return true;
    }
    const  capital  = item.serviceLevelName;
    if (this.serviceLevel == 'all') {
      return true;
    } else {
      return this.serviceLevel == capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  initServiceLevel() {
    this.serviceLevel = undefined;
  }

}

@Component({
  selector: "protection-status-filter",
  template: `
      <clr-radio-container>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="protectionStatus" (change)="changeFunc($event)" [(ngModel)]="protectionStatus" value=""/>
          <label>{{'vmfs.filter.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="protectionStatus" (change)="changeFunc($event)" [(ngModel)]="protectionStatus" value="true" />
          <label>{{'vmfs.protectionStatus.protected' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="protectionStatus" (change)="changeFunc($event)" [(ngModel)]="protectionStatus" value="false" />
          <label>{{'vmfs.protectionStatus.unprotected' | translate}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `,
  providers: [VmfsListService],
})
export class ProtectionStatusFilter implements ClrDatagridFilterInterface<VmfsInfo>{
  changes = new Subject<any>();
  protectionStatus;

  readonly status: any;

  accepts(item: VmfsInfo): boolean {
    if (!this.protectionStatus) {
      return true;
    }
    const  capital  = item.vmfsProtected.toString();
    console.log("capital", capital);
    if (this.protectionStatus === '' || this.protectionStatus === 'all') {
      return true;
    } else {
      return this.protectionStatus === capital;
    }
  }

  isActive(): boolean {
    return true;
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  initProtectionStatus() {
    this.protectionStatus = undefined;
  }
}

