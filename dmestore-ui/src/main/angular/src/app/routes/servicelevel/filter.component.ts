import {Component} from "@angular/core";
import {ClrDatagridFilterInterface} from "@clr/angular";
import {Subject} from "rxjs";
import {SLStoragePool} from "./servicelevel.service";
import {ServicelevelService} from "./servicelevel.service";

@Component({
  selector: "sl-sp-status-filter",
  template: `
    <clr-radio-container>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="all" />
        <label>{{'vmfs.filter.all' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="offline"/>
        <label>{{'tier.offLine' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="normal"/>
        <label>{{'tier.normal' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="abnormal"/>
        <label>{{'tier.faulty' | translate}}</label>
      </clr-radio-wrapper>
      <clr-radio-wrapper>
        <input type="radio" clrRadio name="status" (change)="changeFunc($event)" [(ngModel)]="status" value="unknown"/>
        <label>{{'tier.unknown' | translate}}</label>
      </clr-radio-wrapper>
    </clr-radio-container>
  `,
  providers: [ServicelevelService],
})
export class SLSPStatusFilter implements ClrDatagridFilterInterface<SLStoragePool>{

  changes = new Subject<any>();
  status;
  readonly state: any;

  accepts(item: SLStoragePool): boolean {
    if (!this.status || this.status == 'all') {
      return true;
    } else {
      const  status = item.runningStatus;
      return this.status == status;
    }
  }

  changeFunc(value: any) {
    this.changes.next();
  }

  isActive(): boolean {
    return true;
  }

  initStatus() {
    this.status = undefined;
  }
}
