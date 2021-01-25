import { Injectable, Inject, Optional } from '@angular/core';
import {
  CanActivate,
  CanActivateChild,
  CanLoad,
  Route,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlSegment,
  Router
} from '@angular/router';
import { DOCUMENT } from '@angular/common';
import { TokenService } from './token.service';

function getQueryString(name) {
  var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
  return unescape(r[2]);
  }
  return null;
}

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate, CanActivateChild, CanLoad {
  private gotoUrl(url?: string) {
    setTimeout(() => {
      if (/^https?:\/\//g.test(url!)) {
        this.document.location.href = url as string;
      } else {
        this.router.navigateByUrl(url);
      }
    });
  }

  private checkJWT(model: any, offset?: number): boolean {
    return !!model?.token;
  }

  private process(): boolean {
    const tourl = getQueryString('view')
    let res = this.checkJWT(this.token.get<any>(), 1000);
    res = true
    if (tourl) { // 如果带有?view=storage则跳转到当前页面
      var newURL = location.href.split("?")[0];
      window.history.pushState('object', document.title, newURL); // 去除多余参数  避免二次内部跳转失败
      this.gotoUrl('/' + tourl);
    }
    return res;
  }

  constructor(
    private router: Router,
    private token: TokenService,
    @Optional() @Inject(DOCUMENT) private document: any
  ) {}

  // lazy loading
  canLoad(route: Route, segments: UrlSegment[]): boolean {
    return this.process();
  }
  // route
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.process();
  }
  // all children route
  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return this.process();
  }
}
