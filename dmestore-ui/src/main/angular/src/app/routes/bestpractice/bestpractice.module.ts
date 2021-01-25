import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { BestpracticeRoutingModule } from './bestpractice-routing.module';

import { BestpracticeComponent } from './bestpractice.component';
import {TranslateModule} from "@ngx-translate/core";

const COMPONENTS = [BestpracticeComponent];
const COMPONENTS_DYNAMIC = [];

@NgModule({
  imports: [SharedModule, BestpracticeRoutingModule, TranslateModule],
  declarations: [...COMPONENTS, ...COMPONENTS_DYNAMIC],
  entryComponents: COMPONENTS_DYNAMIC,
})
export class BestpracticeModule { }
