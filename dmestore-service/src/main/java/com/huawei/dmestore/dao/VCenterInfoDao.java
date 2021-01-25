package com.huawei.dmestore.dao;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.constant.DpSqlFileConstants;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DataBaseException;
import com.huawei.dmestore.exception.DmeSqlException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * VCenterInfoDao
 *
 * @author yy
 * @since 2020-09-15
 **/
public class VCenterInfoDao extends H2DataBaseDao {
    public int addVcenterInfo(VCenterInfo vcenterinfo) throws DmeSqlException {
        checkVcenterInfo(vcenterinfo);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("INSERT INTO " + DpSqlFileConstants.DP_DME_VCENTER_INFO
                + " (HOST_IP,USER_NAME,PASSWORD,STATE,CREATE_TIME,PUSH_EVENT,PUSH_EVENT_LEVEL,HOST_PORT) "
                + "VALUES (?,?,?,?,CURRENT_TIMESTAMP,?,?,?)");
            ps.setString(DpSqlFileConstants.DIGIT_1, vcenterinfo.getHostIp());
            ps.setString(DpSqlFileConstants.DIGIT_2, vcenterinfo.getUserName());
            ps.setString(DpSqlFileConstants.DIGIT_3, vcenterinfo.getPassword());
            ps.setBoolean(DpSqlFileConstants.DIGIT_4, vcenterinfo.isState());
            ps.setBoolean(DpSqlFileConstants.DIGIT_5, vcenterinfo.isPushEvent());
            ps.setInt(DpSqlFileConstants.DIGIT_6, vcenterinfo.getPushEventLevel());
            ps.setInt(DpSqlFileConstants.DIGIT_7, vcenterinfo.getHostPort());
            int row = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                vcenterinfo.setId(rs.getInt(1));
            }
            return row;
        } catch (SQLException e) {
            LOGGER.error("Failed to add vCenter info: {}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public int updateVcenterInfo(VCenterInfo vcenterinfo) throws DmeSqlException {
        checkVcenterInfo(vcenterinfo);
        checkId(vcenterinfo.getId());
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("UPDATE " + DpSqlFileConstants.DP_DME_VCENTER_INFO
                + " SET HOST_IP=?,USER_NAME=?,PASSWORD=?,STATE=?,PUSH_EVENT=?,PUSH_EVENT_LEVEL=?,"
                + "HOST_PORT=? WHERE ID=?");
            ps.setString(DpSqlFileConstants.DIGIT_1, vcenterinfo.getHostIp());
            ps.setString(DpSqlFileConstants.DIGIT_2, vcenterinfo.getUserName());
            ps.setString(DpSqlFileConstants.DIGIT_3, vcenterinfo.getPassword());
            ps.setBoolean(DpSqlFileConstants.DIGIT_4, vcenterinfo.isState());
            ps.setBoolean(DpSqlFileConstants.DIGIT_5, vcenterinfo.isPushEvent());
            ps.setInt(DpSqlFileConstants.DIGIT_6, vcenterinfo.getPushEventLevel());
            ps.setInt(DpSqlFileConstants.DIGIT_7, vcenterinfo.getHostPort());
            ps.setInt(DpSqlFileConstants.DIGIT_8, vcenterinfo.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to update vCenter info: {}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    public VCenterInfo getVcenterInfo() throws DmeSqlException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(
                "SELECT * FROM " + DpSqlFileConstants.DP_DME_VCENTER_INFO + " ORDER BY CREATE_TIME DESC LIMIT 1");
            rs = ps.executeQuery();
            if (rs.next()) {
                VCenterInfo vcenterinfo = new VCenterInfo();
                vcenterinfo.setId(rs.getInt("ID"));
                vcenterinfo.setHostIp(rs.getString("HOST_IP"));
                vcenterinfo.setHostPort(rs.getInt("HOST_PORT"));
                vcenterinfo.setUserName(rs.getString("USER_NAME"));
                vcenterinfo.setPassword(rs.getString("PASSWORD"));
                vcenterinfo.setCreateTime(rs.getTimestamp("CREATE_TIME"));
                vcenterinfo.setState(rs.getBoolean("STATE"));
                vcenterinfo.setPushEvent(rs.getBoolean("PUSH_EVENT"));
                vcenterinfo.setPushEventLevel(rs.getInt("PUSH_EVENT_LEVEL"));
                return vcenterinfo;
            }
        } catch (DataBaseException | SQLException e) {
            LOGGER.error("Failed to get vCenter info: {}", e.getMessage());
            throw new DmeSqlException(e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return null;
    }

    private void checkIp(String ip) throws DmeSqlException {
        if (ip == null || ip.length() > DmeConstants.MAXLEN) {
            throw new DmeSqlException("parameter ip is not correct");
        }
    }

    private void checkUserName(String userName) throws DmeSqlException {
        if (userName == null || userName.length() > DmeConstants.MAXLEN) {
            throw new DmeSqlException("parameter userName is not correct");
        }
    }

    private void checkPassword(String password) throws DmeSqlException {
        if (password == null || password.length() > DmeConstants.MAXLEN) {
            throw new DmeSqlException("parameter password is not correct");
        }
    }

    private void checkId(int id) throws DmeSqlException {
        if (id < 1) {
            throw new DmeSqlException("parameter is is not correct");
        }
    }

    private void checkVcenterInfo(VCenterInfo vcenterinfo) throws DmeSqlException {
        checkIp(vcenterinfo.getHostIp());
        checkUserName(vcenterinfo.getUserName());
        checkPassword(vcenterinfo.getPassword());
    }
}
