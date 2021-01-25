package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * DmeAccessService
 *
 * @author yy
 * @since 2020-09-02
 **/
public interface DmeAccessService {
    /**
     * Access DME
     *
     * @author yy
     * @param params key required: obj_ids, indicator_ids, range
     * @throws DmeException 异常
     */
    void accessDme(Map<String, Object> params) throws DmeException;

    /**
     * refreshDme
     *
     * @author wangxy
     * @return Map
     */
    Map<String, Object> refreshDme();

    /**
     * Public method access
     *
     * @param url url
     * @param method Http Method
     * @param requestBody request Body
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    ResponseEntity<String> access(String url, HttpMethod method, String requestBody) throws DmeException;

    /**
     * Public method access
     *
     * @param url url
     * @param method Http Method
     * @param jsonBody request Body
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    ResponseEntity<String> accessByJson(String url, HttpMethod method, String jsonBody) throws DmeException;

    /**
     * Access workload info
     *
     * @param storageId storage id
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    List<Map<String, Object>> getWorkLoads(String storageId) throws DmeException;

    /**
     * Query Dme Hosts
     *
     * @param hostIp host ip
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    List<Map<String, Object>> getDmeHosts(String hostIp) throws DmeException;

    /**
     * Query Dme Host's initiators
     *
     * @param hostId host id
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    List<Map<String, Object>> getDmeHostInitiators(String hostId) throws DmeException;

    /**
     * Query Dme hostgroup
     *
     * @param hostGroupName hostGroup Name
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    List<Map<String, Object>> getDmeHostGroups(String hostGroupName) throws DmeException;

    /**
     * create host
     *
     * @param params create host's param
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    Map<String, Object> createHost(Map<String, Object> params) throws DmeException;

    /**
     * create hostgroup
     *
     * @param params create hostgroup's param
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    Map<String, Object> createHostGroup(Map<String, Object> params) throws DmeException;

    /**
     * get host's detailed
     *
     * @param hostId host id
     * @return ResponseBodyBean
     * @throws DmeException when error
     */
    Map<String, Object> getDmeHost(String hostId) throws DmeException;

    /**
     * delete Volume
     *
     * @param ids host ids
     * @throws DmeException when error
     */
    void deleteVolumes(List<String> ids) throws DmeException;

    /**
     * unMap Host
     *
     * @param hostId host id
     * @param ids datastore ids
     * @throws DmeException when error
     */
    void unMapHost(String hostId, List<String> ids) throws DmeException;

    /**
     * scan Datastore
     *
     * @param storageType storage type:VMFS,NFS,ALL
     * @throws DmeException when error
     */
    void scanDatastore(String storageType) throws DmeException;

    /**
     * Configure task time
     *
     * @param taskId task Id
     * @param taskCron task cron
     * @throws DmeException when error
     */
    void configureTaskTime(Integer taskId,String taskCron) throws DmeException;

    /**
     * get hostGroup's detail
     *
     * @param hsotGroupId hostGroups id
     * @return Map
     * @throws DmeException when error
     */
    Map<String,Object> getDmeHostGroup(String hsotGroupId) throws DmeException;

    /**
     * get hostGroup's host
     *
     * @param hostGroupId hostGroups id
     * @return List
     * @throws DmeException when error
     */
    List<Map<String,Object>> getDmeHostInHostGroup(String hostGroupId) throws DmeException;

    /**
     * Configure task time
     *
     * @param hostId host Id
     * @param volumeIds task cron
     * @throws DmeException when error
     */
    void hostMapping(String hostId, List<String> volumeIds) throws DmeException;
}
