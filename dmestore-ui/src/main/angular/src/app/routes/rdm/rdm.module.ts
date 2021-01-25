import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {SharedModule} from '../../shared';

import { RdmRoutingModule } from './rdm-routing.module';
import { RdmComponent } from './rdm.component';
import {FormsModule} from '@angular/forms';


@NgModule({
  declarations: [RdmComponent],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    RdmRoutingModule
  ]
})
export class RdmModule { }
