import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BestpracticeComponent } from './bestpractice.component';

const routes: Routes = [{ path: '', component: BestpracticeComponent }];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BestpracticeRoutingModule { }
