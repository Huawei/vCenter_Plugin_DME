package com.huawei.dmestore.dao;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DpSqlFileConstants;
import com.huawei.dmestore.entity.DmeVmwareRelation;
import com.huawei.dmestore.exception.DataBaseException;
import com.huawei.dmestore.exception.DmeSqlException;
import com.huawei.dmestore.utils.ToolUtils;

import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DmeVmwareRalationDao
 *
 * @author yy
 * @since 2020-09-02
 **/
public class DmeVmwareRalationDao extends H2DataBaseDao {
    private static final String ID = "ID";

    private static final String STORE_ID = "STORE_ID";

    private static final String STORE_NAME = "STORE_NAME";

    private static final String VOLUME_ID = "VOLUME_ID";

    private static final String VOLUME_NAME = "VOLUME_NAME";

    private static final String VOLUME_WWN = "VOLUME_WWN";

    private static final String VOLUME_SHARE = "VOLUME_SHARE";

    private static final String VOLUME_FS = "VOLUME_FS";

    private static final String SHARE_ID = "SHARE_ID";

    private static final String SHARE_NAME = "SHARE_NAME";

    private static final String FS_ID = "FS_ID";

    private static final String FS_NAME = "FS_NAME";

    private static final String LOGICPORT_ID = "LOGICPORT_ID";

    private static final String LOGICPORT_NAME = "LOGICPORT_NAME";

    private static final String STORE_TYPE = "STORE_TYPE";

    private static final String CREATETIME = "CREATETIME";

    private static final String UPDATETIME = "UPDATETIME";

    private static final String STATE = "STATE";

    private static final String STORAGE_DEVICE_ID = "STORAGE_DEVICE_ID";

    public List<DmeVmwareRelation> getDmeVmwareRelation(String storeType) throws DmeSqlException {
        List<DmeVmwareRelation> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = " SELECT * FROM   " + DpSqlFileConstants.DP_DME_VMWARE_RELATION + " where state = 1 ";
            if (!StringUtils.isEmpty(storeType)) {
                sql = sql + " and STORE_TYPE = '" + storeType + "'";
            }
            LOGGER.info("getDmeVmwareRelation!sql={}, connection is not null:{}", sql, con == null ? false : true);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DmeVmwareRelation dvr = new DmeVmwareRelation();
                dvr.setId(rs.getInt(ID));
                dvr.setStoreId(rs.getString(STORE_ID));
                dvr.setStoreName(rs.getString(STORE_NAME));
                dvr.setVolumeId(rs.getString(VOLUME_ID));
                dvr.setVolumeName(rs.getString(VOLUME_NAME));
                dvr.setVolumeWwn(rs.getString(VOLUME_WWN));
                dvr.setVolumeShare(rs.getString(VOLUME_SHARE));
                dvr.setVolumeFs(rs.getString(VOLUME_FS));
                dvr.setShareId(rs.getString(SHARE_ID));
                dvr.setShareName(rs.getString(SHARE_NAME));
                dvr.setFsId(rs.getString(FS_ID));
                dvr.setFsName(rs.getString(FS_NAME));
                dvr.setLogicPortId(rs.getString(LOGICPORT_ID));
                dvr.setLogicPortName(rs.getString(LOGICPORT_NAME));
                dvr.setStoreType(rs.getString(STORE_TYPE));
                dvr.setCreateTime(rs.getTimestamp(CREATETIME));
                dvr.setUpdateTime(rs.getTimestamp(UPDATETIME));
                dvr.setState(rs.getInt(STATE));
                dvr.setStorageDeviceId(rs.getString(STORAGE_DEVICE_ID));
                lists.add(dvr);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("getDmeVmwareRelation error: {}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public DmeVmwareRelation getDmeVmwareRelationByDsId(String storeId) throws DmeSqlException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DmeVmwareRelation dvr = null;
        try {
            con = getConnection();
            String sql = " SELECT * FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION + " WHERE state = 1";
            if (!StringUtils.isEmpty(storeId)) {
                sql = sql + " and STORE_ID='" + storeId + "'";
            }
            LOGGER.info("getDmeVmwareRelationByDsId!sql={}, connection is not null:{}", sql,
                con == null ? false : true);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                dvr = new DmeVmwareRelation();
                dvr.setId(rs.getInt(ID));
                dvr.setStoreId(rs.getString(STORE_ID));
                dvr.setStoreName(rs.getString(STORE_NAME));
                dvr.setVolumeId(rs.getString(VOLUME_ID));
                dvr.setVolumeName(rs.getString(VOLUME_NAME));
                dvr.setVolumeWwn(rs.getString(VOLUME_WWN));
                dvr.setVolumeShare(rs.getString(VOLUME_SHARE));
                dvr.setVolumeFs(rs.getString(VOLUME_FS));
                dvr.setShareId(rs.getString(SHARE_ID));
                dvr.setShareName(rs.getString(SHARE_NAME));
                dvr.setFsId(rs.getString(FS_ID));
                dvr.setFsName(rs.getString("FS_NAME"));
                dvr.setLogicPortId(rs.getString(LOGICPORT_ID));
                dvr.setLogicPortName(rs.getString("LOGICPORT_NAME"));
                dvr.setStoreType(rs.getString("STORE_TYPE"));
                dvr.setCreateTime(rs.getTimestamp("CREATETIME"));
                dvr.setUpdateTime(rs.getTimestamp("UPDATETIME"));
                dvr.setState(rs.getInt(STATE));
                dvr.setStorageDeviceId(rs.getString(STORAGE_DEVICE_ID));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get Dme Vmware Relation: {}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return dvr;
    }

    public List<String> getAllWwnByType(String storeType) throws DmeSqlException {
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = "SELECT VOLUME_WWN FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION + " WHERE state = 1 ";
        try {
            con = getConnection();
            if (!StringUtils.isEmpty(storeType)) {
                sql = sql + " and STORE_TYPE = '" + storeType + "'";
            }
            LOGGER.info("getAllWwnByType!sql={}, connection is not null:{}", sql, con == null ? false : true);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString(VOLUME_WWN));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme access info! sql={}", sql);
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public List<String> getAllStorageIdByType(String storeType) throws DmeSqlException {
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT STORE_ID FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION + " WHERE state = 1 ";
            if (!StringUtils.isEmpty(storeType)) {
                sql = sql + " and STORE_TYPE='" + storeType + "' ";
            }
            LOGGER.info("getAllStorageIdByType!sql={}, connection is not null:{}", sql, con == null ? false : true);
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString(STORE_ID));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme store info:{}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    public void update(List<DmeVmwareRelation> list) {
        LOGGER.info("update do nothing, list size={}", list.size());
    }

    public void update(List<DmeVmwareRelation> list, String storeType) {
        if (storeType != null && storeType.equalsIgnoreCase(DmeConstants.STORE_TYPE_VMFS)) {
            updateVmfs(list);
        }
    }

    private void updateVmfs(List<DmeVmwareRelation> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "UPDATE " + DpSqlFileConstants.DP_DME_VMWARE_RELATION
                + " SET STORE_ID=?,VOLUME_ID=?,STORE_NAME=?,VOLUME_NAME=?,UPDATETIME=? where VOLUME_WWN=?";
            pstm = con.prepareStatement(sql);
            LOGGER.info("updateVmfs!sql={}, connection is not null:{}", sql, con == null ? false : true);
            con.setAutoCommit(false);
            for (DmeVmwareRelation relation : list) {
                pstm.setString(DpSqlFileConstants.DIGIT_1, relation.getStoreId());
                pstm.setString(DpSqlFileConstants.DIGIT_2, relation.getVolumeId());
                pstm.setString(DpSqlFileConstants.DIGIT_3, relation.getStoreName());
                pstm.setString(DpSqlFileConstants.DIGIT_4, relation.getVolumeName());
                pstm.setDate(DpSqlFileConstants.DIGIT_5, new Date(System.currentTimeMillis()));
                pstm.setString(DpSqlFileConstants.DIGIT_6, relation.getVolumeWwn());
                pstm.setString(DpSqlFileConstants.DIGIT_7, relation.getStorageType());
                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error("updateVmfs error:{}", ex.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public void save(List<DmeVmwareRelation> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "insert into DP_DME_VMWARE_RELATION(STORE_ID,STORE_NAME,VOLUME_ID,VOLUME_NAME,VOLUME_WWN,"
                + "VOLUME_SHARE,VOLUME_FS,STORE_TYPE,STORAGE_TYPE,SHARE_ID,SHARE_NAME,FS_ID,FS_NAME,LOGICPORT_ID,"
                + "LOGICPORT_NAME,STORAGE_DEVICE_ID) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pstm = con.prepareStatement(sql);
            LOGGER.info("save relation!sql={}, connection is not null:{}", sql, con == null ? false : true);

            // 不自动提交
            con.setAutoCommit(false);
            for (DmeVmwareRelation relation : list) {
                pstm.setString(DpSqlFileConstants.DIGIT_1, relation.getStoreId());
                pstm.setString(DpSqlFileConstants.DIGIT_2, relation.getStoreName());
                pstm.setString(DpSqlFileConstants.DIGIT_3, relation.getVolumeId());
                pstm.setString(DpSqlFileConstants.DIGIT_4, relation.getVolumeName());
                pstm.setString(DpSqlFileConstants.DIGIT_5, relation.getVolumeWwn());
                pstm.setString(DpSqlFileConstants.DIGIT_6, relation.getVolumeShare());
                pstm.setString(DpSqlFileConstants.DIGIT_7, relation.getVolumeFs());
                pstm.setString(DpSqlFileConstants.DIGIT_8, relation.getStoreType());
                pstm.setString(DpSqlFileConstants.DIGIT_9, relation.getStorageType());
                pstm.setString(DpSqlFileConstants.DIGIT_10, relation.getShareId());
                pstm.setString(DpSqlFileConstants.DIGIT_11, relation.getShareName());
                pstm.setString(DpSqlFileConstants.DIGIT_12, relation.getFsId());
                pstm.setString(DpSqlFileConstants.DIGIT_13, relation.getFsName());
                pstm.setString(DpSqlFileConstants.DIGIT_14, relation.getLogicPortId());
                pstm.setString(DpSqlFileConstants.DIGIT_15, relation.getLogicPortName());
                pstm.setString(DpSqlFileConstants.DIGIT_16, relation.getStorageDeviceId());
                LOGGER.info("pstm.addBatch StoreType={}", sql, relation.getStoreType());
                pstm.addBatch();
            }
            pstm.executeBatch();

            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(ex.getMessage());
            }
            LOGGER.error("Failed to save datastorage dme relation: {}", ex.getMessage());
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public void deleteByWwn(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "delete from DP_DME_VMWARE_RELATION where VOLUME_WWN=?";
            pstm = con.prepareStatement(sql);
            con.setAutoCommit(false);
            for (String wwn : list) {
                pstm.setString(1, wwn);

                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error(ex.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public void deleteByStorageId(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = getConnection();
            String sql = "delete from DP_DME_VMWARE_RELATION where STORE_ID=?";
            pstm = con.prepareStatement(sql);
            con.setAutoCommit(false);
            for (String wwn : list) {
                pstm.setString(1, wwn);
                pstm.addBatch();
            }
            pstm.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                // 回滚
                con.rollback();
            } catch (SQLException e) {
                LOGGER.error("deleteByStorageId error, errorMsg:{}", e.getMessage());
            }
        } finally {
            closeConnection(con, pstm, null);
        }
    }

    public String getFsIdByStorageId(String storeId) throws DmeSqlException {
        return getNfsContainId(storeId, FS_ID);
    }

    public String getShareIdByStorageId(String storeId) throws DmeSqlException {
        return getNfsContainId(storeId, SHARE_ID);
    }

    /**
     * 根据存储ID获取fsId列表
     *
     * @param storeId storeId
     * @return List
     * @throws DmeSqlException DmeSqlException
     */
    public String getLogicPortIdByStorageId(String storeId) throws DmeSqlException {
        return getNfsContainId(storeId, LOGICPORT_ID);
    }

    /**
     * 根据存储ID获取fsId列表
     *
     * @param storeId storeId
     * @return List
     * @throws DmeSqlException DmeSqlException
     */
    public List<String> getFsIdsByStorageId(String storeId) throws DmeSqlException {
        return getNfsContainIds(storeId, FS_ID);
    }

    /**
     * 根据存储ID获取shareId列表
     *
     * @param storeId storeId
     * @return List
     * @throws DmeSqlException DmeSqlException
     */
    public List<String> getShareIdsByStorageId(String storeId) throws DmeSqlException {
        return getNfsContainIds(storeId, SHARE_ID);
    }

    /**
     * 根据存储ID获取logicPortId列表
     *
     * @param storeId storeId
     * @return List
     * @throws DmeSqlException DmeSqlException
     */
    public List<String> getLogicPortIdsByStorageId(String storeId) throws DmeSqlException {
        return getNfsContainIds(storeId, LOGICPORT_ID);
    }

    /**
     * NFS存储 获取指定存储下的fileId的一个值
     *
     * @param storeId storeId
     * @param fileId fileId
     * @return List
     * @throws DmeSqlException DmeSqlException
     */
    private String getNfsContainId(String storeId, String fileId) throws DmeSqlException {
        String id = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT " + fileId + " FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION
                + " WHERE state = 1 and STORE_TYPE='" + ToolUtils.STORE_TYPE_NFS + "'and STORE_ID = '" + storeId + "' ";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getString(fileId);
                break;
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme nfs relation fileId: {}, {}", fileId, e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return id;
    }

    /**
     * NFS存储 获取指定存储下的fileId的集合
     */
    private List<String> getNfsContainIds(String storeId, String fileId) throws DmeSqlException {
        return getFileIdsByCondition(storeId, fileId, ToolUtils.STORE_TYPE_NFS);
    }

    /**
     * 根据存储ID获取磁盘ID列表
     *
     * @param storeId storeId
     * @return List
     * @throws DmeSqlException DmeSqlException
     */
    public List<String> getVolumeIdsByStorageId(String storeId) throws DmeSqlException {
        return getFileIdsByCondition(storeId, "volume_id", DmeConstants.STORE_TYPE_VMFS);
    }

    private List<String> getFileIdsByCondition(String storeId, String fileId, String storeType) throws DmeSqlException {
        List<String> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT " + fileId + " FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION
                + " WHERE state=1 and STORE_TYPE='" + storeType + "'and STORE_ID='" + storeId + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lists.add(rs.getString(fileId));
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get dme nfs relations fileId: {}, error:{}", fileId, e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    /**
     * VMFS 通过vmfsDataStorageIds查询关联的DME存储的信息
     *
     * @param storageIds storageIds
     * @return List
     * @throws DmeSqlException DmeSqlException
     */
    public List<DmeVmwareRelation> getDmeVmwareRelationsByStorageIds(List<String> storageIds) throws DmeSqlException {
        List<DmeVmwareRelation> lists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String condition = concatValues(storageIds);

            String sql = " SELECT * FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION
                + " WHERE state=1 and STORE_ID IN (" + condition + ")";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                DmeVmwareRelation dvr = new DmeVmwareRelation();
                dvr.setId(rs.getInt(ID));
                dvr.setStoreId(rs.getString(STORE_ID));
                dvr.setStoreName(rs.getString(STORE_NAME));
                dvr.setVolumeId(rs.getString(VOLUME_ID));
                dvr.setVolumeName(rs.getString(VOLUME_NAME));
                dvr.setVolumeWwn(rs.getString(VOLUME_WWN));
                dvr.setVolumeShare(rs.getString(VOLUME_SHARE));
                dvr.setVolumeFs(rs.getString(VOLUME_FS));
                dvr.setShareId(rs.getString(SHARE_ID));
                dvr.setShareName(rs.getString(SHARE_NAME));
                dvr.setState(rs.getInt(STATE));
                dvr.setStorageDeviceId(rs.getString(STORAGE_DEVICE_ID));
                lists.add(dvr);
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get Dme Vmware Relation: {}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return lists;
    }

    protected String concatValues(Collection<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        StringBuilder buff = new StringBuilder();
        for (String value : values) {
            buff.append(",");
            buff.append("'" + value + "'");
        }
        String sql = buff.toString();
        if (sql.length() > 0) {
            sql = sql.substring(1);
        }
        return sql;
    }

    /**
     * 根据volumeId获取对应的vmfs datastore
     *
     * @param volumeId volumeId
     * @return String
     * @throws DmeSqlException DmeSqlException
     */
    public String getVmfsNameByVolumeId(String volumeId) throws DmeSqlException {
        String vmfsDatastoreName = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION
                + " WHERE state=1 and STORE_TYPE='" + DmeConstants.STORE_TYPE_VMFS + "'and VOLUME_ID='" + volumeId
                + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                vmfsDatastoreName = rs.getString(STORE_NAME);
                break;
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get vmfs datastore on: {},{}", volumeId, e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return vmfsDatastoreName;
    }

    public String getDataStoreByName(String name) throws DmeSqlException {
        String vmfsDatastoreName = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION
                + " WHERE state=1 and STORE_NAME='" + name + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                vmfsDatastoreName = rs.getString(STORE_NAME);
                break;
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get vmfs datastore on: {},{}", name, e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return vmfsDatastoreName;
    }


    public String getStorageModelByWwn(String wwn) throws DmeSqlException{
        String storageModel = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + DpSqlFileConstants.DP_DME_VMWARE_RELATION
                + " WHERE state=1 and STORE_TYPE='" + DmeConstants.STORE_TYPE_VMFS + "'and VOLUME_WWN ='" + wwn
                + "'";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                storageModel = rs.getString("STORAGE_TYPE");
                break;
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get vmfs datastore on: {},{}", wwn, e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return storageModel;
    }
}
