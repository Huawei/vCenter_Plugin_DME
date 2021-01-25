package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.NfsDataInfo;
import com.huawei.dmestore.model.NfsDataStoreFsAttr;
import com.huawei.dmestore.model.NfsDataStoreLogicPortAttr;
import com.huawei.dmestore.model.NfsDataStoreShareAttr;

import java.util.List;
import java.util.Map;

/**
 * DmeNFSAccessService
 *
 * @author yy
 * @since 2020-09-03
 **/
public interface DmeNFSAccessService {
    /**
     * NFS DataStore share attribute
     *
     * @param storageObjectId storage object id
     * @return Map
     * @throws DmeException when error
     */
    NfsDataStoreShareAttr getNfsDatastoreShareAttr(String storageObjectId) throws DmeException;

    /**
     * NFS DataStore logic attribute
     *
     * @param storageObjectId storage object id
     * @return Map
     * @throws DmeException when error
     */
    NfsDataStoreLogicPortAttr getNfsDatastoreLogicPortAttr(String storageObjectId) throws DmeException;

    /**
     * NFS DataStore fielsystem attribute
     *
     * @param storageObjectId storage object id
     * @return List
     * @throws DmeException when error
     */
    List<NfsDataStoreFsAttr> getNfsDatastoreFsAttr(String storageObjectId) throws DmeException;

    /**
     * scan NFS datastore
     *
     * @return boolean
     * @throws DmeException when error
     */
    boolean scanNfs() throws DmeException;

    /**
     * get NFS datastore
     *
     * @return List
     * @throws DmeException when error
     */
    List<NfsDataInfo> listNfs() throws DmeException;

    /**
     * List nfs Performance
     *
     * @param fsIds fs id
     * @return List
     * @throws DmeException when error
     */
    List<NfsDataInfo> listNfsPerformance(List<String> fsIds) throws DmeException;

    /**
     * NFS DataStore fielsystem attribute
     *
     * @param params include dataStoreObjectId,hosts,mountType
     * @throws DmeException when error
     */
    void mountNfs(Map<String, Object> params) throws DmeException;

    /**
     * unmount NFS
     *
     * @param params storage object id
     * @throws DmeException when error
     */
    void unmountNfs(Map<String, Object> params) throws DmeException;

    /**
     * delete NFS
     *
     * @param params storage object id
     * @throws DmeException when error
     */
    void deleteNfs(Map<String, Object> params) throws DmeException;

    /**
     * List nfs Performance
     *
     * @param storageId storageId
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, Object>> getHostsMountDataStoreByDsObjectId(String storageId) throws DmeException;

    /**
     * NFS DataStore fielsystem attribute
     *
     * @param storageId storage object id
     * @return List
     * @throws DmeException when error
     */
    List<Map<String, Object>> getClusterMountDataStoreByDsObjectId(String storageId) throws DmeException;
}
