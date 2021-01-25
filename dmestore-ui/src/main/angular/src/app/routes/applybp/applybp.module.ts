import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ApplybpRoutingModule } from './applybp-routing.module';
import { ApplybpComponent } from './applybp.component';
import {SharedModule} from '../../shared';


@NgModule({
  declarations: [ApplybpComponent],
  imports: [
    CommonModule,
    SharedModule,
    ApplybpRoutingModule
  ]
})
export class ApplybpModule { }
