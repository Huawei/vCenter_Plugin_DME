package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.SimpleServiceLevel;

import java.util.List;
import java.util.Map;

/**
 * VmfsOperationService
 *
 * @author lianqiang
 * @since 2020-09-15
 **/
public interface VmfsOperationService {
    /**
     * 更新VMFS存储
     *
     * @param volumeId volumeId
     * @param params params
     * @throws DmeException DmeException
     */
    void updateVmfs(String volumeId, Map<String, Object> params) throws DmeException;

    /**
     * vmfs存储扩容
     *
     * @param volumes volumes
     * @throws DmeException DmeException
     */
    void expandVmfs(Map<String, String> volumes) throws DmeException;

    /**
     * vmfs存储空间回收
     *
     * @param vmfsUuids vmfsUuids
     * @throws DmeException DmeException
     */
    void recycleVmfsCapacity(List<String> vmfsUuids) throws DmeException;


    /**
     * 是否能vmfs存储空间回收
     *
     * @param vmfsUuids vmfsUuids
     * @throws DmeException DmeException
     */
    boolean canRecycleVmfsCapacity(List<String> vmfsUuids) throws DmeException;


    /**
     * vmfs存储空间回收
     *
     * @param vmfsObjIds vmfsObjIds
     * @throws DmeException DmeException
     */
    void recycleVmfsCapacityByDataStoreIds(List<String> vmfsObjIds) throws DmeException;

    /**
     * 更新vmfs存储
     *
     * @param params params
     * @throws DmeException DmeException
     */
    void updateVmfsServiceLevel(Map<String, Object> params) throws DmeException;

    /**
     * 获取服务等级列表
     *
     * @param params params
     * @return List SimpleServiceLevel
     * @throws DmeException DmeException
     */
    List<SimpleServiceLevel> listServiceLevelVmfs(Map<String, Object> params) throws DmeException;
}
