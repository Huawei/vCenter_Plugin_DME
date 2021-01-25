import {ChangeDetectorRef, Component, OnInit, AfterViewInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CommonService} from '../common.service';
import {GlobalsService} from '../../shared/globals.service';
import {ClrForm} from "@clr/angular";

@Component({
  selector: 'app-iscsi',
  templateUrl: './iscsi.component.html',
  styleUrls: ['./iscsi.component.scss'],
  providers: [CommonService]
})
export class IscsiComponent implements OnInit, AfterViewInit {

  @ViewChild(ClrForm, {static: true}) rdmFormGroup;
  @ViewChild('myForm', {static: true}) myForm;
  testPortConnectedUrl = "accesshost/testconnectivity";
  portGetUrl = 'dmestorage/getstorageethports';
  portGetUrlParams = {
    params: {
      storageSn: ''
    }
  };
  ipsGetUrl = 'accessvmware/getvmkernelipbyhostobjectid';
  ipsGetUrlParams = {
    params: {
      hostObjectId: ''
    }
  };

  storageGetUrl = 'dmestorage/storages';

  configIscsiUrl = 'accesshost/configureiscsi';

  // 存储下拉框数据
  storageDevices = [];

  // ip下拉框数据
  ips = [];

  // 提交数据
  configModel = {
    hostObjectId: '',
    vmKernel: {
      device: ''
    },
    ethPorts: [],
    sn: ''
  };

  hostObjectId = '';
  // port列表
  portLoading = false;
  portList = [];
  portTotal = 0;

  tipModalSuccess = false;
  tipModalFail = false;

  isSubmit = false;


  ipLoading = false;
  dsDeviceLoading = false;
  submitLoading = false;
  constructor(private cdr: ChangeDetectorRef,
              private http: HttpClient,
              private commonService: CommonService,
              private gs: GlobalsService) { }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    const ctx = this.gs.getClientSdk().app.getContextObjects();
    console.log(ctx);
    if(ctx == null){
      this.hostObjectId = 'urn:vmomi:HostSystem:host-1034:674908e5-ab21-4079-9cb1-596358ee5dd1';
    } else{
      this.hostObjectId = ctx[0].id;
    }
    this.ipsGetUrlParams.params.hostObjectId = this.hostObjectId;
    this.configModel.hostObjectId = this.hostObjectId;
    this.loadIps();
    this.loadStorageDevice();
  }

  loadIps(){
    this.ipLoading = true;
    this.http.get(this.ipsGetUrl, this.ipsGetUrlParams).subscribe((result: any) => {
      this.ipLoading = false;
      if (result.code === '200'){
        this.ips = result.data;
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
    this.cdr.detectChanges();
  }

  loadStorageDevice(){
    this.dsDeviceLoading = true;
    this.http.get(this.storageGetUrl, {}).subscribe((result: any) => {
      this.dsDeviceLoading = false;
      if (result.code === '200'){
        this.storageDevices = result.data;
        setTimeout(() => {
          this.loadPorts();
        }, 1000);
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  loadPorts(){
    // 有存储 有ip才去load
    if (this.configModel.sn !== '' && this.configModel.vmKernel.device !== ''){
      this.portLoading = true;
      this.portGetUrlParams.params.storageSn = this.configModel.sn;
      this.http.get(this.portGetUrl, this.portGetUrlParams).subscribe((result: any) => {
        this.portLoading = false;
        if (result.code === '200'){
          result.data.forEach((item) => {
            item.connectStatus = '';
          });
          this.portList = result.data;
          this.portTotal = result.data.length;
          this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
          // 连通状态
          this.testPortConnected();
        }
      }, err => {
        console.error('ERROR', err);
        this.portLoading = false;
      });
    }
  }

  testPortConnected(){
    const p = new testConnectParams();
    const testPortList = [];
    this.portList.forEach((item)=>{
       if (item.mgmtIp && item.mgmtIp != ""){
         testPortList.push(item);
       }
    });
    p.ethPorts = testPortList;
    p.hostObjectId = this.configModel.hostObjectId;
    p.vmKernel = this.configModel.vmKernel;
    this.http.post(this.testPortConnectedUrl, p).subscribe((result: any) => {
      if (result.code === '200' && result.data){
        result.data.forEach((i)=>{
            this.portList.forEach((j)=>{
               if(i.id == j.id){
                 j.connectStatus = i.connectStatus;
               }
            });
        });
        this.cdr.detectChanges(); // 此方法变化检测，异步处理数据都要添加此方法
      } else{
        alert("测试连通性出错");
      }
    }, err => {
      console.error('ERROR', err);
    });
  }

  submit(){
    this.isSubmit = true;
    if (this.myForm.form.invalid) {
      this.rdmFormGroup.markAsTouched();
      return;
    } else{
      if(this.configModel.ethPorts.length == 0){
        return;
      }
    }

    this.submitLoading = true;
    this.http.post(this.configIscsiUrl, this.configModel).subscribe((result: any) => {
      this.submitLoading = false;
      if(result.code == '200'){
        this.tipModalSuccess = true;
      } else{
        this.tipModalFail = true;
      }
      this.cdr.detectChanges();
    }, err => {
      console.error('ERROR', err);
    });
    this.cdr.detectChanges();
  }

  closeWin(){
    this.gs.getClientSdk().modal.close();
  }
}


class testConnectParams{
  ethPorts: any[];
  hostObjectId: string;
  vmKernel: object;
}
