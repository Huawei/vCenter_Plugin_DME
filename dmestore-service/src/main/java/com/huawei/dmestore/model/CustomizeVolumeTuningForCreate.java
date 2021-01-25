package com.huawei.dmestore.model;

/**
 * CustomizeVolumeTuningForCreate
 *
 * @author wangxiangyong
 * @Description: CustomizeVolumeTuningForCreate
 * @since 2020-12-08
 **/
public class CustomizeVolumeTuningForCreate {
    /**
     * smartqos .
     */
    private SmartQosForRdmCreate smartqos;

    /**
     * compressionEnabled .
     */
    private Boolean compressionEnabled;

    /**
     * dedupeEnabled .
     */
    private Boolean dedupeEnabled;

    /**
     * smarttier .
     */
    private String smarttier;

    /**
     * 卷分配类型，取值范围 thin，thick .
     **/
    private String alloctype;

    /**
     * workloadTypeId .
     */
    private Integer workloadTypeId;

    /**
     * getSmartqos .
     *
     * @return SmartQosForRdmCreate .
     */
    public SmartQosForRdmCreate getSmartqos() {
        return smartqos;
    }

    /**
     * setSmartqos .
     *
     * @param param .
     */
    public void setSmartqos(final SmartQosForRdmCreate param) {
        this.smartqos = param;
    }

    /**
     * getCompressionEnabled .
     *
     * @return Boolean .
     */
    public Boolean getCompressionEnabled() {
        return compressionEnabled;
    }

    /**
     * setSmartqos .
     *
     * @param param .
     */
    public void setCompressionEnabled(final Boolean param) {
        this.compressionEnabled = param;
    }

    /**
     * getDedupeEnabled .
     *
     * @return Boolean .
     */
    public Boolean getDedupeEnabled() {
        return dedupeEnabled;
    }

    /**
     * setSmartqos .
     *
     * @param param .
     */
    public void setDedupeEnabled(final Boolean param) {
        this.dedupeEnabled = param;
    }

    /**
     * getSmarttier .
     *
     * @return String .
     */
    public String getSmarttier() {
        return smarttier;
    }

    /**
     * setSmartqos .
     *
     * @param param .
     */
    public void setSmarttier(final String param) {
        this.smarttier = param;
    }

    /**
     * getAlloctype .
     *
     * @return String .
     */
    public String getAlloctype() {
        return alloctype;
    }

    /**
     * setSmartqos .
     *
     * @param param .
     */
    public void setAlloctype(final String param) {
        this.alloctype = param;
    }

    /**
     * getWorkloadTypeId .
     *
     * @return Integer .
     */
    public Integer getWorkloadTypeId() {
        return workloadTypeId;
    }

    /**
     * setSmartqos .
     *
     * @param param .
     */
    public void setWorkloadTypeId(final Integer param) {
        this.workloadTypeId = param;
    }
}
