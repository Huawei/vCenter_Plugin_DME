import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { RoutesRoutingModule } from './routes-routing.module';

import { DashboardComponent } from './dashboard/dashboard.component';
import { StorageComponent } from './storage/storage.component';
import { DetailComponent } from './storage/detail/detail.component';
import {
  MapStatusFilter,
  ProTypeFilter,
  VolServiceLevelFilter,
  StoragePoolStatusFilter,
  StoragePoolTypeFilter,
  VolStatusFilter,
  VolStoragePoolFilter,
  VolProtectionStatusFilter,
  FsStatusFilter,
  FsTypeFilter,
  DtreeSecModFilter,
  DtreeQuotaFilter,
  HardwareStatusFilter,
  DiskStatusFilter,
  DiskTypeFilter,
  FcStatusFilter,
  EthStatusFilter,
  FcoeStatusFilter,
  LogicRunningStatusFilter,
  LogicStatusFilter,
  StorageStatusFilter, LogicDdnsStatusFilter, LogicRoleFilter,
} from "./storage/filter.component";

const COMPONENTS = [DashboardComponent, StorageComponent, StorageStatusFilter,
  StoragePoolStatusFilter, VolStatusFilter, ProTypeFilter,
  MapStatusFilter, VolStoragePoolFilter, VolServiceLevelFilter, VolProtectionStatusFilter,
  FsStatusFilter, FsTypeFilter, DtreeSecModFilter, DtreeQuotaFilter, HardwareStatusFilter,
  DiskStatusFilter, DiskTypeFilter, FcStatusFilter, EthStatusFilter, FcoeStatusFilter,
  LogicRunningStatusFilter, LogicStatusFilter, StorageStatusFilter, LogicDdnsStatusFilter,
  LogicRoleFilter];
const COMPONENTS_DYNAMIC = [];

@NgModule({
  imports: [SharedModule, RoutesRoutingModule],
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, DetailComponent, StoragePoolTypeFilter],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class RoutesModule {}
