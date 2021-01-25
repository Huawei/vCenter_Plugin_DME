package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;

import java.util.Map;

/**
 * NfsOperationService
 *
 * @author lianqiang
 * @since 2020-09-15
 **/
public interface NfsOperationService {
    /**
     * NFS存储创建
     *
     * @param params params
     * @throws DmeException when error
     */
    void createNfsDatastore(Map<String, Object> params) throws DmeException;

    /**
     * 更新NFS存储
     *
     * @param params params
     * @throws DmeException when error
     */
    void updateNfsDatastore(Map<String, Object> params) throws DmeException;

    /**
     * NFS存储扩容和缩容
     *
     * @param params params
     * @throws DmeException when error
     */
    void changeNfsCapacity(Map<String, Object> params) throws DmeException;

    /**
     * NFS存储修改
     *
     * @param storeObjectId store Object Id
     * @return Map
     * @throws DmeException when error
     */
    Map<String,Object> getEditNfsStore(String storeObjectId) throws DmeException;
}
