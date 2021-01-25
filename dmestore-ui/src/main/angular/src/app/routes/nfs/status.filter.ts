import {Component, OnInit} from "@angular/core";
import {ClrDatagridFilterInterface} from "@clr/angular";
import {Subject} from "rxjs";
import {List} from "./nfs.service";

@Component({
  selector: "status-filter",
  template: `
      <clr-radio-container>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="options" (change)="changeFunc($event)" [(ngModel)]="options" value="" />
          <label>{{'enum.status.all' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="options" (change)="changeFunc($event)" [(ngModel)]="options" value="normal"/>
          <label>{{'enum.status.normal' | translate}}</label>
        </clr-radio-wrapper>
        <clr-radio-wrapper>
          <input type="radio" clrRadio name="options" (change)="changeFunc($event)" [(ngModel)]="options" value="faulty" />
          <label>{{'enum.status.faulty' | translate}}</label>
        </clr-radio-wrapper>
      </clr-radio-container>
  `
})
export class StatusFilter implements ClrDatagridFilterInterface<List>,OnInit {
  ngOnInit(): void {
  }
  changes = new Subject<any>();
  options;
  normal = false;
  abnormal = false;

  readonly status: any;

  accepts(item: List): boolean {
    if (!this.options) {
      return true;
    }
    const  capital  = item.status;
    console.log("capital", capital);
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
