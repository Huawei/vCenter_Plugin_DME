import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { IscsiRoutingModule } from './iscsi-routing.module';
import { IscsiComponent } from './iscsi.component';
import {SharedModule} from '../../shared';


@NgModule({
  declarations: [IscsiComponent],
  imports: [
    CommonModule,
    SharedModule,
    IscsiRoutingModule
  ]
})
export class IscsiModule { }
