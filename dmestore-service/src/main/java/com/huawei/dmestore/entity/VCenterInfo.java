package com.huawei.dmestore.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * VCenterInfo .
 *
 * @author Administrator .
 * @since 2020-12-08
 */
public class VCenterInfo implements Serializable {
    private static final long serialVersionUID = 3811172759222907501L;

    /**
     * hostPortConstant = 443 .
     */
    private final Integer hostPortConstant = 443;

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
     * HA status .
     */
    private boolean state;

    /**
     * Alarm status .
     */
    private boolean pushEvent;

    /**
     * 1:crtical 2:majorandcritical 3:all .
     */
    private int pushEventLevel;

    /**
     * getPushEventLevel .
     *
     * @return int .
     */
    public int getPushEventLevel() {
        return pushEventLevel;
    }

    /**
     * setPushEventLevel .
     *
     * @param param .
     */
    public void setPushEventLevel(final int param) {
        this.pushEventLevel = param;
    }

    /**
     * getId .
     *
     * @return int .
     */
    public int getId() {
        return id;
    }

    /**
     * setPushEventLevel .
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
     * setPushEventLevel .
     *
     * @param param .
     */
    public void setHostIp(final String param) {
        this.hostIp = param;
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
     * setPushEventLevel .
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
     * setPassword .
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
     * setCreateTime .
     *
     * @param param .
     */
    public void setCreateTime(final Date param) {
        this.createTime = param;
    }

    /**
     * isState .
     *
     * @return boolean .
     */
    public boolean isState() {
        return state;
    }

    /**
     * setState .
     *
     * @param param .
     */
    public void setState(final boolean param) {
        this.state = param;
    }

    /**
     * isPushEvent .
     *
     * @return boolean .
     */
    public boolean isPushEvent() {
        return pushEvent;
    }

    /**
     * setPushEvent .
     *
     * @param param .
     */
    public void setPushEvent(final boolean param) {
        this.pushEvent = param;
    }

    /**
     * getHostPort .
     *
     * @return int .
     */
    public int getHostPort() {
        if (hostPort == 0) {
            return hostPortConstant;
        }
        return hostPort;
    }

    /**
     * setHostPort .
     *
     * @param param .
     */
    public void setHostPort(final int param) {
        this.hostPort = param;
    }
}
