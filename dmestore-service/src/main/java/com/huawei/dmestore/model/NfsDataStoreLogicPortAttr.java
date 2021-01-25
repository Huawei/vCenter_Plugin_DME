package com.huawei.dmestore.model;

import java.io.Serializable;

/**
 * NfsDataStoreLogicPortAttr
 *
 * @author wangxiangyong
 * @since 2020-12-08
 **/
public class NfsDataStoreLogicPortAttr implements Serializable {
    /**
     * name .
     */
    private String name;
    /**
     * ip .
     */
    private String ip;
    /**
     * status .
     */
    private String status;
    /**
     * runningStatus .
     */
    private String runningStatus;
    /**
     * activePort .
     */
    private String activePort;
    /**
     * currentPort .
     */
    private String currentPort;
    /**
     * failoverGroup .
     */
    private String failoverGroup;

    /**
     * getName .
     *
     * @return String 。
     */
    public String getName() {
        return name;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getIp .
     *
     * @return String 。
     */
    public String getIp() {
        return ip;
    }

    /**
     * setIp .
     *
     * @param param .
     */
    public void setIp(final String param) {
        this.ip = param;
    }

    /**
     * getStatus .
     *
     * @return String 。
     */
    public String getStatus() {
        return status;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setStatus(final String param) {
        this.status = param;
    }

    /**
     * getRunningStatus .
     *
     * @return String 。
     */
    public String getRunningStatus() {
        return runningStatus;
    }

    /**
     * setRunningStatus .
     *
     * @param param .
     */
    public void setRunningStatus(final String param) {
        this.runningStatus = param;
    }

    /**
     * getActivePort .
     *
     * @return String 。
     */
    public String getActivePort() {
        return activePort;
    }

    /**
     * setActivePort .
     *
     * @param param .
     */
    public void setActivePort(final String param) {
        this.activePort = param;
    }

    /**
     * getCurrentPort .
     *
     * @return String 。
     */
    public String getCurrentPort() {
        return currentPort;
    }

    /**
     * setCurrentPort .
     *
     * @param param .
     */
    public void setCurrentPort(final String param) {
        this.currentPort = param;
    }

    /**
     * getFailoverGroup .
     *
     * @return String 。
     */
    public String getFailoverGroup() {
        return failoverGroup;
    }

    /**
     * setFailoverGroup .
     *
     * @param param .
     */
    public void setFailoverGroup(final String param) {
        this.failoverGroup = param;
    }
}
