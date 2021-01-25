package com.huawei.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SmartQos
 *
 * @author lianq
 * @ClassName: SmartQos
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class SmartQos {
    /**
     * name.
     */
    private String name;
    /**
     * 控制策略,0：保护IO下限，1：控制IO上限.
     */
    private Integer latency;
    /**
     * maxbandwidth.
     */
    private Integer maxbandwidth;
    /**
     * maxiops.
     */
    private Integer maxiops;
    /**
     * minbandwidth.
     */
    private Integer minbandwidth;
    /**
     * miniops.
     */
    private Integer miniops;
    /**
     * enabled.
     */
    private Boolean enabled;
    /**
     * for update.
     */
    @JsonProperty(value = "control_policy")
    private String controlPolicy;
    /**
     * latencyUnit.
     */
    private String latencyUnit;

    /**
     * getLatencyUnit .
     *
     * @return String .
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
     * @return Boolean .
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * setEnabled .
     *
     * @param param .
     */
    public void setEnabled(final Boolean param) {
        this.enabled = param;
    }

    /**
     * getControlPolicy .
     *
     * @return String .
     */
    public String getControlPolicy() {
        return controlPolicy;
    }

    /**
     * setControlPolicy .
     *
     * @param param .
     */
    public void setControlPolicy(final String param) {
        this.controlPolicy = param;
    }

    /**
     * getName .
     *
     * @return String .
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
     * getLatency .
     *
     * @return Integer .
     */
    public Integer getLatency() {
        return latency;
    }

    /**
     * setLatency .
     *
     * @param param .
     */
    public void setLatency(final Integer param) {
        this.latency = param;
    }

    /**
     * getMaxbandwidth .
     *
     * @return Integer .
     */
    public Integer getMaxbandwidth() {
        return maxbandwidth;
    }

    /**
     * setMaxbandwidth .
     *
     * @param param .
     */
    public void setMaxbandwidth(final Integer param) {
        this.maxbandwidth = param;
    }

    /**
     * getMaxiops .
     *
     * @return Integer .
     */
    public Integer getMaxiops() {
        return maxiops;
    }

    /**
     * setMaxiops .
     *
     * @param param .
     */
    public void setMaxiops(final Integer param) {
        this.maxiops = param;
    }

    /**
     * getMinbandwidth .
     *
     * @return Integer .
     */
    public Integer getMinbandwidth() {
        return minbandwidth;
    }

    /**
     * setMinbandwidth .
     *
     * @param param .
     */
    public void setMinbandwidth(final Integer param) {
        this.minbandwidth = param;
    }

    /**
     * getMiniops .
     *
     * @return Integer .
     */
    public Integer getMiniops() {
        return miniops;
    }

    /**
     * setMiniops .
     *
     * @param param .
     */
    public void setMiniops(final Integer param) {
        this.miniops = param;
    }
}
