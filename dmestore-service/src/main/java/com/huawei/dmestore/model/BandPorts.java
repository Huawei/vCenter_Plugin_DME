package com.huawei.dmestore.model;

/**
 * BandPorts
 *
 * @author lianq
 *
 * @since 2020-12-07
 */
public class BandPorts {
    /**
     * id .
     */
    private String id;

    /**
     * name .
     */
    private String name;

    /**
     * healthStatus .
     */
    private String healthStatus;

    /**
     * runningStatus .
     */
    private String runningStatus;

    /**
     * mtu.
     */
    private String mtu;

    /**
     * getId .
     *
     * @return .
     */
    public String getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final String param) {
        this.id = param;
    }

    /**
     * getName .
     *
     * @return .
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
     * getHealthStatus .
     *
     * @return .
     */
    public String getHealthStatus() {
        return healthStatus;
    }

    /**
     * setHealthStatus .
     *
     * @param param .
     */
    public void setHealthStatus(final String param) {
        this.healthStatus = param;
    }

    /**
     * getRunningStatus .
     *
     * @return String .
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
     * getMtu .
     *
     * @return .
     */
    public String getMtu() {
        return mtu;
    }

    /**
     * setMtu .
     *
     * @param param .
     */
    public void setMtu(final String param) {
        this.mtu = param;
    }
}
