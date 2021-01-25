package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;

import java.util.Map;

/**
 * DataStoreStatisticHistoryService
 *
 * @author liuxh
 * @since 2020-09-03
 **/
public interface DataStoreStatisticHistoryService {
    /**
     * LAST_5_MINUTE
     */
    String RANGE_LAST_5_MINUTE = "LAST_5_MINUTE";
    /**
     * LAST_1_HOUR
     */
    String RANGE_LAST_1_HOUR = "LAST_1_HOUR";
    /**
     * LAST_1_DAY
     */
    String RANGE_LAST_1_DAY = "LAST_1_DAY";
    /**
     * LAST_1_WEEK
     */
    String RANGE_LAST_1_WEEK = "LAST_1_WEEK";
    /**
     * LAST_1_MONTH
     */
    String RANGE_LAST_1_MONTH = "LAST_1_MONTH";
    /**
     * LAST_1_QUARTER
     */
    String RANGE_LAST_1_QUARTER = "LAST_1_QUARTER";
    /**
     * HALF_1_YEAR
     */
    String RANGE_HALF_1_YEAR = "HALF_1_YEAR";
    /**
     * LAST_1_YEAR
     */
    String RANGE_LAST_1_YEAR = "LAST_1_YEAR";
    /**
     * BEGIN_END_TIME
     */
    String RANG_BEGIN_END_TIME = "BEGIN_END_TIME";
    /**
     * ONE_MINUTE
     */
    String INTERVAL_ONE_MINUTE = "ONE_MINUTE";
    /**
     * MINUTE
     */
    String INTERVAL_MINUTE = "MINUTE";
    /**
     * HALF_HOUR
     */
    String INTERVAL_HALF_HOUR = "HALF_HOUR";
    /**
     * HOUR
     */
    String INTERVAL_HOUR = "HOUR";
    /**
     * DAY
     */
    String INTERVAL_DAY = "DAY";

    /**
     * 获取VMFS历史性能数据
     *
     * @author wangxy
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryVmfsStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 获取VMFS当前性能数据值
     *
     * @author wangxy
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryVmfsStatisticCurrent(Map<String, Object> params) throws DmeException;

    /**
     * 获取NFS历史性能数据值
     *
     * @author wangxy
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryNfsStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 获取NFS当前性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryNfsStatisticCurrent(Map<String, Object> params) throws DmeException;

    /**
     * 查询磁盘(卷)历史性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryVolumeStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询fileSystem历史性能数据(Nfsdatasotrage)
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryFsStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询controller历史性能数据(SYS_Controller)
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryControllerStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询StoragePort历史性能数据(SYS_StoragePort)
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStoragePortStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询storageDisk历史性能数据(SYS_StorageDisk)
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStorageDiskStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询服务等级历史性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryServiceLevelStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询服务等级卷历史性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryServiceLevelLunStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询服务等级存储池历史性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryServiceLevelStoragePoolStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询存储池历史性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStoragePoolStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询存储池当前性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStoragePoolCurrentStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询存储设备历史性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStorageDevcieStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询存储设备当前性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStorageDevcieCurrentStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询控制器当前性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryControllerCurrentStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询存储端口当前性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStoragePortCurrentStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询存储磁盘当前性能数据
     *
     * @author liuxh
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryStorageDiskCurrentStatistic(Map<String, Object> params) throws DmeException;

    /**
     * 查询指定资源类型的历史性能数据
     *
     * @author liuxh
     * @param resType 实例类型
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryHistoryStatistic(String resType, Map<String, Object> params) throws DmeException;

    /**
     * 查询指定资源类型的当前性能数据
     *
     * @author liuxh
     * @param resType 实例类型
     * @param params key required: obj_ids, indicator_ids, range
     * @return Map
     * @throws DmeException 异常
     */
    Map<String, Object> queryCurrentStatistic(String resType, Map<String, Object> params) throws DmeException;
}
