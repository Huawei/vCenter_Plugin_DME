import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class ServicelevelService {
  constructor(private http: HttpClient) {}
}
export interface SLStoragePool {
  id: string;
  name: string;
  running_status: string;
  physicalType: string;
  storage_name: string;
  storage_instance_id: string;

  total_capacity: number;
  consumed_capacity: number;
  consumed_capacity_percentage: number;
  free_capacity: number;

  latency: string;
  iops: string;
  bandwidth: string;
  runningStatus: string;
  storageName: string;
  totalCapacity: number;
  consumedCapacity: number;
  usedCapacity: number; // 使用容量
  consumedCapacityPercentage: string;
  freeCapacity: number;
  storageInstanceId: string;
}
