import {Component, OnInit} from '@angular/core';
import {ShareDetail, ShareService} from './share.service';
import {GlobalsService} from '../../../shared/globals.service';

@Component({
  selector: 'app-share',
  templateUrl: './share.component.html',
  styleUrls: ['./share.component.scss'],
  providers: [ShareService, GlobalsService]
})
export class ShareComponent implements OnInit {
  shareDetail: ShareDetail;

  constructor(private remoteSrv: ShareService, private gs: GlobalsService) { }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    this.getShareDetail(ctx[0].id);
  }
  getShareDetail(objectId){
    this.remoteSrv.getData(objectId)
      .subscribe((result: any) => {
       this.shareDetail = result.data;
       console.log('shareDetail:');
       console.log(this.shareDetail);
      });
  }
}
