package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.BestPracticeCheckRecordBean;
import com.huawei.dmestore.model.NfsDataInfo;
import com.huawei.dmestore.model.Storage;
import com.huawei.dmestore.model.VmfsDataInfo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OverviewServiceImpl
 *
 * @author chenjie
 * @since 2020-09-15
 **/
public class OverviewServiceImpl implements OverviewService {
    private static final String STORAGE_TYPE_VMFS = "1";

    private static final String STORAGE_TYPE_NFS = "2";

    private static final String STORAGE_DEVICE_STATUS_NORMAL = "1";

    private static final String CRITICAL = "Critical";

    private static final String MAJOR = "Major";

    private static final String WARNING = "Warning";

    private static final String INFO = "Info";

    private static final String UNIT_GB = "GB";

    private static final String TOTAL_CAPACITY = "totalCapacity";

    private static final String FREE_CAPACITY = "freeCapacity";

    private static final String USED_CAPACITY = "usedCapacity";

    private static final String UTILIZATION = "utilization";

    private static final String CAPACITY_UNIT = "capacityUnit";

    private static final double DOUBLE_100 = 100.0;

    private static final int DIGITAL_16 = 16;

    private VmfsAccessService vmfsAccessService;

    private DmeNFSAccessService dmeNfsAccessService;

    private DmeStorageService dmeStorageService;

    private BestPracticeProcessService bestPracticeProcessService;

    @Override
    public Map<String, Object> getStorageNum() throws DmeException {
        Map<String, Object> map = new HashMap<>(DIGITAL_16);
        int normal = 0;
        int abnormal = 0;
        int total;
        List<Storage> storages = dmeStorageService.getStorages();
        total = storages.size();
        for (Storage storage : storages) {
            // 运行状态 0-离线 1-正常 2-故障 9-未管理。
            if (STORAGE_DEVICE_STATUS_NORMAL.equals(storage.getStatus())) {
                normal++;
            } else {
                abnormal++;
            }
        }
        map.put("total", total);
        map.put("normal", normal);
        map.put("abnormal", abnormal);
        return map;
    }

    /**
     * get DataStore Capacity Summary
     *
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @return Map
     */
    @Override
    public Map<String, Object> getDataStoreCapacitySummary(String type) {
        Map<String, Object> map = new HashMap();
        try {
            double[] ds;
            if (STORAGE_TYPE_VMFS.equals(type)) {
                ds = computeVmfsDataStoreCapacity();
            } else if (STORAGE_TYPE_NFS.equals(type)) {
                ds = computeNfsDataStoreCapacity();
            } else {
                ds = new double[4];
                double[] ds1 = computeVmfsDataStoreCapacity();
                double[] ds2 = computeNfsDataStoreCapacity();
                ds[0] = ds1[0] + ds2[0];
                ds[1] = ds1[1] + ds2[1];
                ds[2] = ds1[2] + ds2[2];
                ds[3] = ds[2] / (ds[0] == 0.0 ? 1 : ds[0]) * DOUBLE_100;
            }
            map.put(TOTAL_CAPACITY, ds[0]);
            map.put(FREE_CAPACITY, ds[1]);
            map.put(USED_CAPACITY, ds[2]);
            map.put(UTILIZATION, ds[3]);
            map.put(CAPACITY_UNIT, UNIT_GB);
        } catch (DmeException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * getDataStoreCapacityTopN
     *
     * @param type 0 :VMFS and NFS, 1:VMFS, 2:NFS
     * @param topn top n
     * @return List
     */
    @Override
    public List<Map<String, Object>> getDataStoreCapacityTopN(String type, int topn) {
        List<Map<String, Object>> returnMap;
        try {
            if (STORAGE_TYPE_VMFS.equals(type)) {
                returnMap = getVmfsInfos();
            } else if (STORAGE_TYPE_NFS.equals(type)) {
                returnMap = getNfsInfos();
            } else {
                returnMap = getVmfsInfos();
                returnMap.addAll(getNfsInfos());
            }
            returnMap.sort(Comparator.comparing(o -> (double) ((Map) o).get(UTILIZATION)).reversed());
        } catch (DmeException e) {
            throw new RuntimeException(e);
        }
        return returnMap.size() > topn ? returnMap.subList(0, topn) : returnMap;
    }

    /**
     * critical : 5,
     * major: 2,
     * warning: 3,
     * info: 44
     *
     * @return
     */
    @Override
    public Map<String, Object> getBestPracticeViolations() {
        Map<String, Object> map = new HashMap<>(DIGITAL_16);
        try {
            int critical = 0;
            int major = 0;
            int warning = 0;
            int info = 0;
            List<BestPracticeCheckRecordBean> rs = bestPracticeProcessService.getCheckRecord();
            for (BestPracticeCheckRecordBean recordBean : rs) {
                if (CRITICAL.equals(recordBean.getLevel())) {
                    critical++;
                } else if (MAJOR.equals(recordBean.getLevel())) {
                    major++;
                } else if (WARNING.equals(recordBean.getLevel())) {
                    warning++;
                } else if (INFO.equals(recordBean.getLevel())) {
                    info++;
                }

                map.put("critical", critical);
                map.put("major", major);
                map.put("warning", warning);
                map.put("info", info);
            }
        } catch (DmeException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private double[] computeVmfsDataStoreCapacity() throws DmeException {
        double[] ds = new double[4];
        double totalCapacity = 0;
        double freeCapacity = 0;
        double usedCapacity;
        double utilization;
        List<VmfsDataInfo> vmfsDataInfos = vmfsAccessService.listVmfs();
        if (vmfsDataInfos != null && vmfsDataInfos.size() > 0) {
            for (VmfsDataInfo vmfsDataInfo : vmfsDataInfos) {
                totalCapacity += vmfsDataInfo.getCapacity();
                freeCapacity += vmfsDataInfo.getFreeSpace();
            }
            usedCapacity = totalCapacity - freeCapacity;
            utilization = usedCapacity / totalCapacity * DOUBLE_100;
            ds[0] = totalCapacity;
            ds[1] = freeCapacity;
            ds[2] = usedCapacity;
            ds[3] = utilization;
        }

        return ds;
    }

    private double[] computeNfsDataStoreCapacity() throws DmeException {
        double[] ds = new double[4];
        double totalCapacity = 0;
        double freeCapacity = 0;
        double usedCapacity;
        double utilization;
        List<NfsDataInfo> nfsDataInfos = dmeNfsAccessService.listNfs();
        if (nfsDataInfos != null && nfsDataInfos.size() > 0) {
            for (NfsDataInfo nfsDataInfo : nfsDataInfos) {
                totalCapacity += nfsDataInfo.getCapacity();
                freeCapacity += nfsDataInfo.getFreeSpace();
            }
            usedCapacity = totalCapacity - freeCapacity;
            utilization = usedCapacity / totalCapacity * DOUBLE_100;
            ds[0] = totalCapacity;
            ds[1] = freeCapacity;
            ds[2] = usedCapacity;
            ds[3] = utilization;
        }

        return ds;
    }

    private List<Map<String, Object>> getVmfsInfos() throws DmeException {
        List<Map<String, Object>> maps = new ArrayList<>();
        List<VmfsDataInfo> vmfsDataInfos = vmfsAccessService.listVmfs();
        if (vmfsDataInfos != null) {
            for (VmfsDataInfo vmfsDataInfo : vmfsDataInfos) {
                Map<String, Object> object = new HashMap();
                object.put("id", vmfsDataInfo.getDeviceId());
                object.put("name", vmfsDataInfo.getName());
                object.put(FREE_CAPACITY, vmfsDataInfo.getFreeSpace());
                object.put(TOTAL_CAPACITY, vmfsDataInfo.getCapacity());
                double usedCapacity = vmfsDataInfo.getCapacity() - vmfsDataInfo.getFreeSpace();
                object.put(USED_CAPACITY, usedCapacity);
                object.put(UTILIZATION, usedCapacity / vmfsDataInfo.getCapacity() * DOUBLE_100);
                object.put(CAPACITY_UNIT, UNIT_GB);

                maps.add(object);
            }
        }

        return maps;
    }

    private List<Map<String, Object>> getNfsInfos() throws DmeException {
        List<Map<String, Object>> returnMap = new ArrayList<>();
        List<NfsDataInfo> nfsDataInfos = dmeNfsAccessService.listNfs();
        if (nfsDataInfos != null) {
            for (NfsDataInfo nfsDataInfo : nfsDataInfos) {
                Map<String, Object> object = new HashMap();
                double usedCapacity = nfsDataInfo.getCapacity() - nfsDataInfo.getFreeSpace();
                object.put("id", nfsDataInfo.getDeviceId());
                object.put("name", nfsDataInfo.getName());
                object.put(TOTAL_CAPACITY, nfsDataInfo.getCapacity());
                object.put(FREE_CAPACITY, nfsDataInfo.getFreeSpace());
                object.put(USED_CAPACITY, usedCapacity);
                object.put(UTILIZATION, usedCapacity / nfsDataInfo.getCapacity() * DOUBLE_100);
                object.put(CAPACITY_UNIT, UNIT_GB);

                returnMap.add(object);
            }
        }

        return returnMap;
    }

    public VmfsAccessService getVmfsAccessService() {
        return vmfsAccessService;
    }

    public void setVmfsAccessService(VmfsAccessService vmfsAccessService) {
        this.vmfsAccessService = vmfsAccessService;
    }

    public DmeNFSAccessService getDmeNfsAccessService() {
        return dmeNfsAccessService;
    }

    public void setDmeNfsAccessService(DmeNFSAccessService dmeNfsAccessService) {
        this.dmeNfsAccessService = dmeNfsAccessService;
    }

    public DmeStorageService getDmeStorageService() {
        return dmeStorageService;
    }

    public void setDmeStorageService(DmeStorageService dmeStorageService) {
        this.dmeStorageService = dmeStorageService;
    }

    public BestPracticeProcessService getBestPracticeProcessService() {
        return bestPracticeProcessService;
    }

    public void setBestPracticeProcessService(BestPracticeProcessService bestPracticeProcessService) {
        this.bestPracticeProcessService = bestPracticeProcessService;
    }
}
