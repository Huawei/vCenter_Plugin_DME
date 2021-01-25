package com.huawei.dmestore.model;

/**
 * QosParam
 *
 * @author lianq
 * @ClassName: QosParam
 * @since 2020-09-15
 */
public class QosParam {
    /**
     * enabled .
     */
    private Boolean enabled;
    /**
     * responTime .
     */
    private Integer latency;
    /**
     * Unit of response time eg:ms s.
     */
    private String latencyUnit;
    /**
     * minBandWidth .
     */
    private Integer minBandWidth;
    /**
     * minIOPS .
     */
    private Integer minIOPS;
    /**
     * maxBandWidth .
     */
    private Integer maxBandWidth;
    /**
     * maxIOPS .
     */
    private Integer maxIOPS;
    /**
     * smartQos .
     */
    private SmartQos smartQos;

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
     * getMinBandWidth .
     *
     * @return Integer .
     */
    public Integer getMinBandWidth() {
        return minBandWidth;
    }

    /**
     * setMinBandWidth .
     *
     * @param param .
     */
    public void setMinBandWidth(final Integer param) {
        this.minBandWidth = param;
    }

    /**
     * getMinIOPS .
     *
     * @return Integer .
     */
    public Integer getMinIOPS() {
        return minIOPS;
    }

    /**
     * setMinIOPS .
     *
     * @param param .
     */
    public void setMinIOPS(final Integer param) {
        this.minIOPS = param;
    }

    /**
     * getMaxBandWidth .
     *
     * @return Integer .
     */
    public Integer getMaxBandWidth() {
        return maxBandWidth;
    }

    /**
     * setMaxBandWidth .
     *
     * @param param .
     */
    public void setMaxBandWidth(final Integer param) {
        this.maxBandWidth = param;
    }

    /**
     * getMaxIOPS.
     *
     * @return Integer.
     */
    public Integer getMaxIOPS() {
        return maxIOPS;
    }

    /**
     * setMaxIOPS .
     *
     * @param param .
     */
    public void setMaxIOPS(final Integer param) {
        this.maxIOPS = param;
    }

    /**
     * getEnabled .
     *
     * @return Boolean.
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * setLatency .
     *
     * @param param .
     */
    public void setEnabled(final Boolean param) {
        this.enabled = param;
    }

    /**
     * getSmartQos .
     *
     * @return SmartQos .
     */
    public SmartQos getSmartQos() {
        return smartQos;
    }

    /**
     * setSmartQos .
     *
     * @param param .
     */
    public void setSmartQos(final SmartQos param) {
        this.smartQos = param;
    }

    @Override
    public final String toString() {
        return "QosParam{"
            + "enabled=" + enabled
            + ", latency=" + latency
            + ", latencyUnit='" + latencyUnit + '\''
            + ", minBandWidth=" + minBandWidth
            + ", minIOPS=" + minIOPS
            + ", maxBandWidth=" + maxBandWidth
            + ", maxIOPS=" + maxIOPS
            + ", smartQos=" + smartQos
            + '}';
    }
}
