import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '@shared/shared.module';
import { NfsRoutingModule } from './nfs-routing.module';
import { NfsComponent } from './nfs.component';
import { LogicportComponent } from './logicport/logicport.component';
import { ShareComponent } from './share/share.component';
import { FileSystemComponent } from './file-system/file-system.component';
import {FormsModule} from '@angular/forms';
import {NfsPerformanceComponent} from "./performance/performance.component";
import {NgxEchartsModule} from "ngx-echarts";
import {ClarityModule} from "@clr/angular";
import {NfsMountComponent} from "./subpages/mount/nfs-mount.component";
import {NfsUnmountComponent} from "./subpages/unmount/nfs-unmount.component";
import {NfsReduceComponent} from "./subpages/reduce/nfs-reduce.component";
import {NfsExpandComponent} from "./subpages/expand/nfs-expand.component";
import {NfsAddComponent} from "./subpages/add/nfs-add.component";
import {NfsDeleteComponent} from "./subpages/delete/nfs-delete.component";
import {NfsModifyComponent} from "./subpages/modify/nfs-modify.component";
import {StatusFilter} from "./status.filter";
import {DeviceFilter} from "./device.filter";
import {TranslateModule} from "@ngx-translate/core";


@NgModule({
  declarations: [NfsComponent, LogicportComponent, ShareComponent,
    FileSystemComponent,NfsPerformanceComponent,NfsMountComponent,NfsUnmountComponent
    ,NfsReduceComponent,NfsExpandComponent,NfsAddComponent,NfsDeleteComponent,NfsModifyComponent,StatusFilter,DeviceFilter
  ],
  imports: [
    FormsModule,
    CommonModule,
    SharedModule,
    NgxEchartsModule,
    FormsModule,
    ClarityModule,
    NfsRoutingModule,
    TranslateModule
  ]
})
export class NfsModule { }
