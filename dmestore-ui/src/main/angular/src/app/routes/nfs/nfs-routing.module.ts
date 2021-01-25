import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { NfsComponent } from './nfs.component';
import {LogicportComponent} from './logicport/logicport.component';
import {ShareComponent} from './share/share.component';
import {FileSystemComponent} from './file-system/file-system.component';
import {NfsPerformanceComponent} from "./performance/performance.component";
import {NfsMountComponent} from "./subpages/mount/nfs-mount.component";
import {NfsUnmountComponent} from "./subpages/unmount/nfs-unmount.component";
import {NfsReduceComponent} from "./subpages/reduce/nfs-reduce.component";
import {NfsExpandComponent} from "./subpages/expand/nfs-expand.component";
import {NfsDeleteComponent} from "./subpages/delete/nfs-delete.component";
import {NfsAddComponent} from "./subpages/add/nfs-add.component";
import {NfsModifyComponent} from "./subpages/modify/nfs-modify.component";

const routes: Routes = [
  { path: '', component: NfsComponent },
  { path: 'logicport', component: LogicportComponent },
  { path: 'performance', component: NfsPerformanceComponent },
  { path: 'share', component: ShareComponent },
  {path:'host/mount',component: NfsMountComponent},
  {path:'host/unmount',component: NfsUnmountComponent},
  {path:'dataStore/mount',component: NfsMountComponent},
  {path:'dataStore/unmount',component: NfsUnmountComponent},
  {path:'cluster/mount',component: NfsMountComponent},
  {path:'cluster/unmount',component: NfsUnmountComponent},
  {path:'add',component: NfsAddComponent},
  {path:'modify',component: NfsModifyComponent},
  {path:'reduce',component: NfsReduceComponent},
  {path:'expand',component: NfsExpandComponent},
  {path:'delete',component: NfsDeleteComponent},
  { path: 'fs', component: FileSystemComponent }
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class NfsRoutingModule { }
