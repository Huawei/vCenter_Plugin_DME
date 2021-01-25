import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { VmfsRoutingModule } from './vmfs-routing.module';

import { VmfsListComponent } from './list/list.component';
import { AttributeComponent } from './volume-attribute/attribute.component';
import { PerformanceComponent } from './volume-performance/performance.component';
import { MountComponent } from './mount/mount.component';
import { AddComponent } from './add/add.component';
import { ServiceLevelComponent } from './serviceLevel/serviceLevel.component';
import { NgxEchartsModule } from 'ngx-echarts';
import {FormsModule} from "@angular/forms";
import {ClarityModule, ClrCheckbox} from "@clr/angular";
import {CommonModule} from "@angular/common";
import {TranslateModule} from "@ngx-translate/core";
import {ExpandComponent} from "./expand/expand.component";
import {ModifyComponent} from "./modify/modify.component";
import {ReclaimComponent} from "./reclaim/reclaim.component";
import {DeleteComponent} from "./delete/delete.component";
import {DeviceFilter, ProtectionStatusFilter, ServiceLevelFilter, StatusFilter} from "./list/filter.component";
import {StorageService} from "../storage/storage.service";

const COMPONENTS = [VmfsListComponent];
const COMPONENTS_DYNAMIC = [];

@NgModule({
  imports: [SharedModule, VmfsRoutingModule, NgxEchartsModule, FormsModule, ClarityModule, CommonModule, TranslateModule],
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, AttributeComponent, PerformanceComponent, MountComponent, AddComponent,
    ServiceLevelComponent, ExpandComponent, ModifyComponent, DeleteComponent, ReclaimComponent, StatusFilter, DeviceFilter,
    ServiceLevelFilter, ProtectionStatusFilter],
  entryComponents: COMPONENTS_DYNAMIC,
  providers: [StorageService]
})
export class VmfsModule {}
