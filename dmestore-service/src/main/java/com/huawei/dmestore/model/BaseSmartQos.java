package com.huawei.dmestore.model;

/**
 * BaseSmartQos
 *
 * @author lianqiang
 * @since 2020-09-09
 */
public class BaseSmartQos {
    /**
     * latencyUnit .
     */
    private String name;

    /**
     * latency .
     */
    private Integer latency;

    /**
     * maxbandwidth .
     */
    private Integer maxbandwidth;

    /**
     * maxiops .
     */
    private Integer maxiops;

    /**
     * minbandwidth .
     */
    private Integer minbandwidth;

    /**
     * miniops .
     */
    private Integer miniops;

    /**
     * enabled .
     */
    private Boolean enabled;

    /**
     * latencyUnit .
     */
    private String latencyUnit;

    /**
     * getLatencyUnit.
     *
     * @return .
     */
    public String getLatencyUnit() {
        return latencyUnit;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setLatencyUnit(final String param) {
        this.latencyUnit = param;
    }

    /**
     * getEnabled .
     *
     * @return .
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setEnabled(final Boolean param) {
        this.enabled = param;
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
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getLatency .
     *
     * @return .
     */
    public Integer getLatency() {
        return latency;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setLatency(final Integer param) {
        this.latency = param;
    }

    /**
     * getMaxbandwidth .
     *
     * @return .
     */
    public Integer getMaxbandwidth() {
        return maxbandwidth;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMaxbandwidth(final Integer param) {
        this.maxbandwidth = param;
    }

    /**
     * getMaxiops .
     *
     * @return .
     */
    public Integer getMaxiops() {
        return maxiops;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMaxiops(final Integer param) {
        this.maxiops = param;
    }

    /**
     * getMinbandwidth .
     *
     * @return .
     */
    public Integer getMinbandwidth() {
        return minbandwidth;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMinbandwidth(final Integer param) {
        this.minbandwidth = param;
    }

    /**
     * getMiniops .
     *
     * @return .
     */
    public Integer getMiniops() {
        return miniops;
    }

    /**
     * setLatencyUnit .
     *
     * @param param .
     */
    public void setMiniops(final Integer param) {
        this.miniops = param;
    }
}
