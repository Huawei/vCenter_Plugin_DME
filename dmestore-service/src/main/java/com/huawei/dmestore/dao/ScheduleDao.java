package com.huawei.dmestore.dao;

import com.huawei.dmestore.constant.DpSqlFileConstants;
import com.huawei.dmestore.entity.ScheduleConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ScheduleDao
 *
 * @author yy
 * @since 2020-09-02
 **/
public class ScheduleDao extends H2DataBaseDao {
    public List<ScheduleConfig> getScheduleList() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<ScheduleConfig> scheduleconfiglist = new ArrayList<>();
        try {
            con = getConnection();
            ps = con.prepareStatement("SELECT * FROM " + DpSqlFileConstants.DP_DME_TASK_INFO);
            rs = ps.executeQuery();
            scheduleconfiglist = new ArrayList<>();
            while (rs.next()) {
                scheduleconfiglist.add(buildSchedule(rs));
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to get dmes: {}", e.getMessage());
        } finally {
            closeConnection(con, ps, rs);
        }
        return scheduleconfiglist;
    }

    public int updateTaskTime(Integer taskId, String taskCron) throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement("UPDATE " + DpSqlFileConstants.DP_DME_TASK_INFO + " SET CRON=? WHERE ID=?");
            ps.setString(DpSqlFileConstants.DIGIT_1, taskCron);
            ps.setInt(DpSqlFileConstants.DIGIT_2, taskId);

            return ps.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.error("Failed to update task time: {}", ex.getMessage());
            throw ex;
        } finally {
            closeConnection(con, ps, rs);
        }
    }

    private ScheduleConfig buildSchedule(ResultSet rs) throws SQLException {
        ScheduleConfig scheduleConfig = new ScheduleConfig();
        scheduleConfig.setClassName(rs.getString("CLASS_NAME"));
        scheduleConfig.setCron(rs.getString("CRON"));
        scheduleConfig.setJobName(rs.getString("JOB_NAME"));
        scheduleConfig.setMethod(rs.getString("METHOD"));
        scheduleConfig.setId(rs.getInt("ID"));
        return scheduleConfig;
    }
}
