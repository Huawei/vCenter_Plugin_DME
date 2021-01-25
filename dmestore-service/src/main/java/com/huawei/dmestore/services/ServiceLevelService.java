package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.DmeDatasetsQueryResponse;
import com.huawei.dmestore.model.SimpleServiceLevel;
import com.huawei.dmestore.model.StoragePool;
import com.huawei.dmestore.model.Volume;

import java.util.List;
import java.util.Map;

/**
 * ServiceLevelService
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public interface ServiceLevelService {
    List<SimpleServiceLevel> listServiceLevel(Map<String, Object> params) throws DmeException;

    List<StoragePool> getStoragePoolInfosByServiceLevelId(String serviceLevelId) throws DmeException;

    List<Volume> getVolumeInfosByServiceLevelId(String serviceLevelId) throws DmeException;

    void updateVmwarePolicy() throws DmeException;

    DmeDatasetsQueryResponse statLunDatasetsQuery(String serviceLevelId, String interval) throws DmeException;

    DmeDatasetsQueryResponse statStoragePoolDatasetsQuery(String serviceLevelId, String interval) throws DmeException;

    DmeDatasetsQueryResponse lunPerformanceDatasetsQuery(String serviceLevelId, String interval) throws DmeException;

    DmeDatasetsQueryResponse storagePoolPerformanceDatasetsQuery(String serviceLevelId, String interval)
        throws DmeException;
}
