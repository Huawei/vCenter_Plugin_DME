package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DpSqlFileConstants;
import com.huawei.dmestore.dao.SystemDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * SystemServiceImpl
 *
 * @author yy
 * @since 2020-09-15
 **/
public class SystemServiceImpl implements SystemService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemServiceImpl.class);

    private SystemDao systemDao;

    @Override
    public void initDb() {
        try {
            // 20200904 add table DP_DME_ACCESS_INFO
            systemDao.checkExistAndCreateTable(DpSqlFileConstants.DP_DME_ACCESS_INFO,
                DpSqlFileConstants.DP_DME_ACCESS_INFO_SQL);

            // 20200904 add table DP_DME_VMWARE_RELATION
            systemDao.checkExistAndCreateTable(DpSqlFileConstants.DP_DME_VMWARE_RELATION,
                DpSqlFileConstants.DP_DME_VMWARE_RELATION_SQL);

            // 20200916 add table DP_DME_BEST_PRACTICE_CHECK
            systemDao.checkExistAndCreateTable(DpSqlFileConstants.DP_DME_BEST_PRACTICE_CHECK,
                DpSqlFileConstants.DP_DME_BEST_PRACTICE_CHECK_SQL);

            // 20200922 add table DP_DME_TASK_INFO
            systemDao.checkExistAndCreateTable(DpSqlFileConstants.DP_DME_TASK_INFO,
                DpSqlFileConstants.DP_DME_TASK_INFO_SQL);

            // 20201012 add table DP_DME_VCENTER_INFO
            systemDao.checkExistAndCreateTable(DpSqlFileConstants.DP_DME_VCENTER_INFO,
                DpSqlFileConstants.DP_DME_VCENTER_INFO_SQL);

            LOGGER.info("creating table over...");
            systemDao.initData(DpSqlFileConstants.DP_DME_BEST_PRACTICE_CHECK_ALTER_SQL);

            systemDao.initData(DpSqlFileConstants.DP_DME_TASK_DATA_SYNCSERVICELEVEL_SQL);

            systemDao.initData(DpSqlFileConstants.DP_DME_TASK_DATA_SYNCBESTPRACTISE_SQL);

            systemDao.initData(DpSqlFileConstants.DP_DME_TASK_DATA_SCANDATASTORE_SQL);

            systemDao.initData(DpSqlFileConstants.DP_DME_TASK_DATA_REFRESHRESOURCEINSTANCE_SQL);

            LOGGER.info("init data over...");
        } catch (SQLException e) {
            LOGGER.error("Failed to init DB: {}", e.getMessage());
        }
    }

    @Override
    public boolean isTableExists(String tableName) {
        try {
            return systemDao.checkTable(tableName);
        } catch (SQLException e) {
            LOGGER.error("Cannot check table exist!errorMsg:{}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isColumnExists(String tableName, String columnName) {
        try {
            return systemDao.isColumnExists(tableName, columnName);
        } catch (SQLException ex) {
            LOGGER.error("Cannot check column exist: {}", ex.getMessage());
            return false;
        }
    }

    @Override
    public void cleanData() {
        LOGGER.info("Clean data from all tables...");
        systemDao.cleanAllData();
        systemDao.dropAllTable();
    }

    public void setSystemDao(SystemDao systemDao) {
        this.systemDao = systemDao;
    }

    public SystemDao getSystemDao() {
        return systemDao;
    }
}
