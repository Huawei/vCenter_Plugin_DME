import { Injectable } from '@angular/core';
import {ClrDatagridStateInterface} from '@clr/angular';

@Injectable()
export class CommonService {

  params(query: any = {}) { // 对query进行处理
    const p = Object.assign({}, query);
    return p;
  }

  // table数据处理
  refresh(state: ClrDatagridStateInterface, query: any) {
    console.log(state);
    let sort;
    sort = state.sort;
    // 排序 排序规则order:  true降序  false升序 ;   排序值by: 字符串  按照某个字段排序  在html里[clrDgSortBy]自定义
    if (sort) {
      query.order = sort.reverse ? 'desc' : 'asc';
      query.sort = sort.by;
    }
    // 过滤器   过滤内容
    if (state.filters) {
      for (const filter of state.filters) {
        const {property, value} =  filter as {property: string, value: string};
        query[property] = value;
      }
    }
    // 分页器数据current: 1 当前页;    size: 5 分页大小
    if (state.page) {
      query.page = state.page.current;
      query.per_page = state.page.size;
    }
    const qq = { params : this.params(query)};
    console.log(qq)
    return qq;
  }
}
