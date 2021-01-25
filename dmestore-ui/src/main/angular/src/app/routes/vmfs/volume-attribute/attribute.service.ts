import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable()
export class AttributeService {
  constructor(private http: HttpClient) {}

  getData(storageObjectId: string) {
    return this.http.get('accessvmfs/volume/' + storageObjectId );
  }
}
export class VolumeInfo{
  name: string;
  wwn: string;
  storage: string;
  storagePool: string;
  serviceLevel: string;
  controlPolicy: string;
  trafficControl: string;
  smartTier: string;
  applicationType: string;
  provisionType: string;
  dudeplication: boolean;
  compression: boolean;
  smartQos:SmartQos;
}

export class SmartQos {
  /**
   * name.
   */
  name:string;
  /**
   * 控制策略,0：保护IO下限，1：控制IO上限.
   */
  latency:number;
  /**
   * maxbandwidth.
   */
  maxbandwidth:number;
  /**
   * maxiops.
   */
  maxiops:number;
  /**
   * minbandwidth.
   */
  minbandwidth:number;
  /**
   * miniops.
   */
  miniops:number;
  /**
   * enabled.
   */
  enabled:boolean;
  /**
   * for update.0 下限 1 上限 2上限&下限
   */
  controlPolicy:string;
  /**
   * latencyUnit.
   */
  latencyUnit:string;
}
