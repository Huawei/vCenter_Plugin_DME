package com.huawei.dmestore.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * DmeInfo.
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class DmeInfo implements Serializable {
    /**
     * id .
     */
    private int id;
    /**
     * hostIp .
     */
    private String hostIp;
    /**
     * hostPort .
     */
    private int hostPort;
    /**
     * userName .
     */
    private String userName;
    /**
     * password .
     */
    private String password;
    /**
     * createTime .
     */
    private Date createTime;
    /**
     * updateTime .
     */
    private Date updateTime;
    /**
     * state .
     */
    private int state;

    /**
     * getId .
     *
     * @return int .
     */
    public int getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final int param) {
        this.id = param;
    }

    /**
     * getHostIp .
     *
     * @return String .
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setHostIp(final String param) {
        this.hostIp = param;
    }

    /**
     * getHostPort .
     *
     * @return String .
     */
    public int getHostPort() {
        return hostPort;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setHostPort(final int param) {
        this.hostPort = param;
    }

    /**
     * getUserName .
     *
     * @return String .
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setUserName(final String param) {
        this.userName = param;
    }

    /**
     * getPassword .
     *
     * @return String .
     */
    public String getPassword() {
        return password;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setPassword(final String param) {
        this.password = param;
    }

    /**
     * getCreateTime .
     *
     * @return Date .
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setCreateTime(final Date param) {
        this.createTime = param;
    }

    /**
     * getUpdateTime .
     *
     * @return Date .
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setUpdateTime(final Date param) {
        this.updateTime = param;
    }

    /**
     * getState .
     *
     * @return int .
     */
    public int getState() {
        return state;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setState(final int param) {
        this.state = param;
    }
}
