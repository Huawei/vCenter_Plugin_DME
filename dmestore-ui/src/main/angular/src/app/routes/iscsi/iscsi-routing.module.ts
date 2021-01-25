import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { IscsiComponent } from './iscsi.component';

const routes: Routes = [{ path: '', component: IscsiComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class IscsiRoutingModule { }
