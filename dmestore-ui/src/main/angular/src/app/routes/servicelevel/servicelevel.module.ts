import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ServicelevelRoutingModule } from './servicelevel-routing.module';
import { ServicelevelComponent } from './servicelevel.component';
import {SharedModule} from '../../shared';
import {SLSPStatusFilter} from "./filter.component";
import {StorageService} from "../storage/storage.service";
import {ServicelevelService} from "./servicelevel.service";
import {TranslateModule} from "@ngx-translate/core";
import {NgxEchartsModule} from "ngx-echarts";
import {FormsModule} from "@angular/forms";
import {ClarityModule} from "@clr/angular";

const COMPONENTS = [ServicelevelComponent, SLSPStatusFilter];
const COMPONENTS_DYNAMIC = [];

@NgModule({
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC, ServicelevelComponent],
  imports: [
    CommonModule,
    SharedModule,
    ServicelevelRoutingModule,
    TranslateModule,
    SharedModule,
    NgxEchartsModule, FormsModule, ClarityModule
  ],
  entryComponents: COMPONENTS_DYNAMIC,
  providers: [ServicelevelService]
})
export class ServicelevelModule { }
