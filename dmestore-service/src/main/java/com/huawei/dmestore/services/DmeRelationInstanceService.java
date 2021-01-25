package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.RelationInstance;

import java.util.List;
import java.util.Map;

/**
 * DmeRelationInstanceService
 *
 * @author liuxh
 * @since 2020-09-15
 **/
public interface DmeRelationInstanceService {
    /**
     * list relation instance
     *
     * @param relationName relation name
     * @return List
     * @throws DmeException when error
     */
    List<RelationInstance> queryRelationByRelationName(String relationName) throws DmeException;

    /**
     * 按资源关系类型名称和条件sourceInstanceId查询资源关系实例
     *
     * @param relationName 资源关系类型名称
     * @param sourceInstanceId 源实例ID
     * @return List
     * @throws DmeException when error
     */
    List<RelationInstance> queryRelationByRelationNameConditionSourceInstanceId(String relationName,
        String sourceInstanceId) throws DmeException;

    /**
     * 按资源关系类型名称和实例ID查询资源关系实例
     *
     * @param relationName enum:M_DjTierContainsLun M_DjTierContainsStoragePool M_DjTierContainsStoragePort
     * @param instanceId uuid
     * @return RelationInstance
     * @throws DmeException when error
     */
    RelationInstance queryRelationByRelationNameInstanceId(String relationName, String instanceId) throws DmeException;

    /**
     * 按资源类型和资源实例查询单个资源实例
     *
     * @param instanceName enum:SYS_Lun SYS_StorageDevice
     * @param instanceId instance id
     * @return String
     * @throws DmeException when error
     */
    Object queryInstanceByInstanceNameId(String instanceName, String instanceId) throws DmeException;

    /**
     * list instance
     *
     * @return Map
     * @throws DmeException when error
     */
    Map<String, Map<String, Object>> getServiceLevelInstance();

    /**
     * list lun instance
     *
     * @return Map
     * @throws DmeException when error
     */
    Map<String, Map<String, Object>> getLunInstance() throws DmeException;

    /**
     * list device instance
     *
     * @return Map
     * @throws DmeException when error
     */
    Map<String, Map<String, Object>> getStorageDeviceInstance();

    /**
     * list pool instance
     *
     * @return Map
     * @throws DmeException when error
     */
    Map<String, Map<String, Object>> getStoragePoolInstance();

    /**
     * refresh instance
     */
    void refreshResourceInstance() throws DmeException;
}

