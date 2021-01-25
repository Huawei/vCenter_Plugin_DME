import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {ClrDatagridFilterInterface} from "@clr/angular";
import {Subject} from "rxjs";
import {List} from "./nfs.service";
import {StorageList, StorageService} from "../storage/storage.service";

@Component({
  selector: "device-filter",
  template: `
      <clr-radio-container>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="options" (change)="changeFunc($event)" [(ngModel)]="options" value="" />
          <label>{{'enum.status.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper *ngFor="let item of storageList">
          <input type="radio" clrRadio name="options" (change)="changeFunc($event)" [(ngModel)]="options" value="{{item.name}}"/>
          <label>{{item.name}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `
})
export class DeviceFilter implements ClrDatagridFilterInterface<List>,OnInit {
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
  options;
  normal = false;
  abnormal = false;
  storageList: StorageList[] = [];
  readonly status: any;

  accepts(item: List): boolean {
    if (!this.options) {
      return true;
    }
    const  capital  = item.device;
    if (this.options === '') {
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
}
