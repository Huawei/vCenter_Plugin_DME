import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { VmfsListComponent } from './list/list.component';
import { AttributeComponent } from './volume-attribute/attribute.component';
import { PerformanceComponent } from './volume-performance/performance.component';
import {MountComponent} from "./mount/mount.component";
import {AddComponent} from "./add/add.component";
import {ServiceLevelComponent} from "./serviceLevel/serviceLevel.component";
import {ExpandComponent} from "./expand/expand.component";
import {ModifyComponent} from "./modify/modify.component";
import {DeleteComponent} from "./delete/delete.component";
import {ReclaimComponent} from "./reclaim/reclaim.component";

const routes: Routes = [
  { path: 'list', component: VmfsListComponent, data: { title: 'Vmfs List' } },
  { path: 'attribute', component: AttributeComponent, data: { title: 'Vmfs volumeAttribute' } },
  { path: 'performance', component: PerformanceComponent, data: { title: 'Vmfs volume-performance' } },
  // 以主机或集群为入口选择挂载Datastore
  { path: 'host/mount', component: MountComponent, data: { title: 'Vmfs host mount' } },
  { path: 'cluster/mount', component: MountComponent, data: { title: 'Vmfs cluster mount' } },
  // 以主机或集群为入口选择卸载Datastore
  { path: 'cluster/unmount', component: MountComponent, data: { title: 'Vmfs cluster unmount' } },
  { path: 'host/unmount', component: MountComponent, data: { title: 'Vmfs host unmount' } },
  // 在Datastore菜单中点击"添加"、"修改"、"扩容"、"挂载"、"卸载"、"删除"、"空间回收"、"服务等级变更"
  { path: 'add', component: AddComponent, data: { title: 'Vmfs ADD' } },
  { path: 'modify', component: ModifyComponent, data: { title: 'Vmfs Modify' } },
  { path: 'expand', component: ExpandComponent, data: { title: 'Vmfs expand' } },
  { path: 'mount', component: MountComponent, data: { title: 'Vmfs mount' } },
  { path: 'unmount', component: MountComponent, data: { title: 'Vmfs unmount' } },
  { path: 'delete', component: DeleteComponent, data: { title: 'Vmfs delete' } },
  { path: 'reclaim', component: ReclaimComponent, data: { title: 'Vmfs reclaim' } },
  { path: 'serviceLevel', component: ServiceLevelComponent, data: { title: 'Vmfs update serviceLevel' } },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class VmfsRoutingModule {}
