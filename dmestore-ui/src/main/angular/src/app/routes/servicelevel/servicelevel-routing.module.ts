import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ServicelevelComponent } from './servicelevel.component';
import {VmfsListComponent} from "../vmfs/list/list.component";

const routes: Routes = [
  { path: '',
    component: ServicelevelComponent,
    data: { title: 'ServiceLevel' }}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ServicelevelRoutingModule {

}
