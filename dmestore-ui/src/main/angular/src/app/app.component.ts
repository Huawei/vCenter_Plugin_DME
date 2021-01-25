import { Component, OnInit, AfterViewInit } from '@angular/core';
import { PreloaderService } from '@core';
import { GlobalsService }     from "./shared/globals.service";
@Component({
  selector: 'app-root',
  template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit, AfterViewInit {
  constructor(private preloader: PreloaderService, public gs: GlobalsService) {}

  ngOnInit() {
    console.log(this.gs.getClientSdk().app.getContextObjects())
  }

  ngAfterViewInit() {
    this.preloader.hide();
  }
}
