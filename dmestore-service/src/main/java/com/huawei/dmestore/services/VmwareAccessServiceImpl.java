package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.DmeVmwareRalationDao;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.exception.VcenterException;
import com.huawei.dmestore.utils.ToolUtils;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * VmwareAccessServiceImpl
 *
 * @author yy
 * @since 2020-09-02
 **/
public class VmwareAccessServiceImpl implements VmwareAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(VmwareAccessServiceImpl.class);

    private static final String OBJECT_ID = "objectId";

    private Gson gson = new Gson();

    private DmeVmwareRalationDao dmeVmwareRalationDao;

    private VCSDKUtils vcsdkUtils;

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    public void setDmeVmwareRalationDao(DmeVmwareRalationDao dmeVmwareRalationDao) {
        this.dmeVmwareRalationDao = dmeVmwareRalationDao;
    }

    @Override
    public List<Map<String, String>> listHosts() throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            // 取得vcenter中的所有host。
            String listStr = vcsdkUtils.getAllHosts();
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("list hosts error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getHostsByDsObjectId(String dataStoreObjectId) throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getHostsByDsObjectId(dataStoreObjectId);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get Hosts By DsObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> listClusters() throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getAllClusters();
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("list listClusters error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getClustersByDsObjectId(String dataStoreObjectId) throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getClustersByDsObjectId(dataStoreObjectId);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get Clusters By DsObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getDataStoresByHostObjectId(String hostObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getDataStoresByHostObjectId(hostObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                // 根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储 objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (DmeSqlException e) {
            LOG.error("get DataStores By HostObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            // 根据存储类型，取得vcenter中的所有存储。
            String listStr = vcsdkUtils.getDataStoresByClusterObjectId(clusterObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                // 根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (VcenterException e) {
            LOG.error("get DataStores By ClusterObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getVmKernelIpByHostObjectId(String hostObjectId) throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            // 根据存储类型，取得vcenter中的所有存储。
            String listStr = vcsdkUtils.getVmKernelIpByHostObjectId(hostObjectId);
            if (!StringUtils.isEmpty(listStr)) {
                lists = gson.fromJson(listStr, new TypeToken<List<Map<String, String>>>() { }.getType());
            }
        } catch (VcenterException e) {
            LOG.error("get vmkernel ip by hostobjectid error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getMountDataStoresByHostObjectId(String hostObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            // 根据存储类型，取得vcenter中的所有存储
            String listStr = vcsdkUtils.getMountDataStoresByHostObjectId(hostObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());

                // 根据dataStoreType取得数据库对应存储信息，然后以数据库信息为准过滤存储objectId
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get mount DataStores By HostObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public List<Map<String, String>> getMountDataStoresByClusterObjectId(String clusterObjectId, String dataStoreType)
        throws DmeException {
        List<Map<String, String>> lists = null;
        try {
            String listStr = vcsdkUtils.getMountDataStoresByClusterObjectId(clusterObjectId, dataStoreType);
            if (!StringUtils.isEmpty(listStr)) {
                lists = new ArrayList<>();
                List<Map<String, String>> tmplists = gson.fromJson(listStr,
                    new TypeToken<List<Map<String, String>>>() { }.getType());
                List<String> dvrlist = dmeVmwareRalationDao.getAllStorageIdByType(dataStoreType);
                if (dvrlist != null && dvrlist.size() > 0) {
                    for (Map<String, String> dsmap : tmplists) {
                        if (dsmap != null && dsmap.get(OBJECT_ID) != null && dvrlist.contains(
                            ToolUtils.getStr(dsmap.get(OBJECT_ID)))) {
                            lists.add(dsmap);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("get mount DataStores By ClusterObjectId error:", e);
            throw new DmeException(e.getMessage());
        }
        return lists;
    }

    @Override
    public DmeVmwareRelation getDmeVmwareRelationByDsId(String storeId) throws DmeSqlException {
        return dmeVmwareRalationDao.getDmeVmwareRelationByDsId(storeId);
    }
}
