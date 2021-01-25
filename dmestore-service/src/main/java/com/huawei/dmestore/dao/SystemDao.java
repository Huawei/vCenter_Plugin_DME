package com.huawei.dmestore.dao;

import com.huawei.dmestore.constant.DpSqlFileConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * SystemDao
 *
 * @author yy
 * @since 2020-09-15
 **/
public class SystemDao extends H2DataBaseDao {
    public boolean checkTable(String sqlFile) throws SQLException {
        Connection con = null;
        ResultSet resultSet = null;
        boolean isTableExist;
        try {
            con = getConnection();
            resultSet = con.getMetaData().getTables(null, null, sqlFile, null);
            isTableExist = resultSet.next();
        } catch (SQLException ex) {
            LOGGER.error("Failed to check table: {}", ex.getMessage());
            throw ex;
        } finally {
            closeConnection(con, null, resultSet);
        }

        return isTableExist;
    }

    /**
     * 判断表是否存在，不存在则创建表
     *
     * @param tableName 表名
     * @param createTableSql 创建表的SQL
     * @throws SQLException SQLException
     */
    public void checkExistAndCreateTable(String tableName, String createTableSql) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            rs = con.getMetaData().getTables(null, null, tableName, null);
            if (!rs.next()) {
                ps = con.prepareStatement(createTableSql);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            LOGGER.error("Failed to check exist and create table: {}", ex.getMessage());
            throw ex;
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public void initData(String datasql) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(datasql);
            ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Failed to initData: {}", ex.getMessage());
            throw ex;
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public boolean isColumnExists(String tableName, String columnName) throws SQLException {
        Connection con = null;
        PreparedStatement ps1 = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM " + tableName + " LIMIT 1";
            ps1 = con.prepareStatement(sql);
            rs = ps1.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();
            for (int index = 0; index < resultSetMetaData.getColumnCount(); index++) {
                if (resultSetMetaData.getColumnName(index + 1).equalsIgnoreCase(columnName)) {
                    return true;
                }
            }
            return false;
        } finally {
            closeConnection(con, rs, ps1);
        }
    }

    public void cleanAllData() {
        Connection con = null;
        PreparedStatement ps1 = null;
        try {
            con = getConnection();
            for (String table : DpSqlFileConstants.ALL_TABLES) {
                try {
                    ps1 = con.prepareStatement("DELETE FROM " + table);
                    ps1.execute();
                    ps1.close();
                    ps1 = null;
                } catch (SQLException e) {
                    LOGGER.error("Cannot delete data from {}", table);
                }
            }
        } finally {
            closeConnection(con, ps1, null);
        }
    }
    public void dropAllTable() {
        Connection con = null;
        PreparedStatement ps1 = null;
        try {
            con = getConnection();
            for (String table : DpSqlFileConstants.ALL_TABLES) {
                try {
                    ps1 = con.prepareStatement("DROP TABLE " + table);
                    ps1.execute();
                    ps1.close();
                    ps1 = null;
                } catch (SQLException e) {
                    LOGGER.error("Cannot drop data from {}" + e.getMessage(), table);
                }
            }
        } finally {
            closeConnection(con, ps1, null);
        }
    }
}
