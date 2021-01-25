import { Component, OnInit } from '@angular/core';
import {FileSystemService, FsDetail} from './file-system.service';
import {GlobalsService} from '../../../shared/globals.service';

@Component({
  selector: 'app-file-system',
  templateUrl: './file-system.component.html',
  styleUrls: ['./file-system.component.scss'],
  providers: [FileSystemService, GlobalsService]
})
export class FileSystemComponent implements OnInit {
  fsDetails: FsDetail[];
  fsDetail: FsDetail = new FsDetail();
  fsNames: string[] = [];
  defaultSelect: string;
  constructor(private fsService: FileSystemService, private gs: GlobalsService) { }

  ngOnInit(): void {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    this.getFsDetail(ctx[0].id);
  }
  getFsDetail(objectId){
    this.fsService.getData(objectId).subscribe((result: any) => {
      this.fsDetails = result.data;
      this.fsDetails.forEach(f => {
        this.fsNames.push(f.name);
      });
      this.defaultSelect = this.fsNames[0];
      this.fsDetail = this.getFsDetailByName(this.defaultSelect);
    });
  }

  getFsDetailByName(name): any {
    const detail = this.fsDetails.filter(item => item.name === name)[0];
    return detail;
  }
  changeFs(){
    this.fsDetail = this.getFsDetailByName(this.defaultSelect);
  }
}
