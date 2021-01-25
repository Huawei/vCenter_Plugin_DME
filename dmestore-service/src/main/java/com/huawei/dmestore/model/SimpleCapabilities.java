package com.huawei.dmestore.model;

/**
 * SimpleCapabilities
 *
 * @author lianq
 * @ClassName: SimpleCapabilities
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class SimpleCapabilities {
    /**
     * resourceType.
     */
    private String resourceType;
    /**
     * compression.
     */
    private Boolean compression;
    /**
     * deduplication.
     */
    private Boolean deduplication;
    /**
     * smarttier.
     */
    private CapabilitiesSmarttier smarttier;
    /**
     * iopriority.
     */
    private CapabilitiesIopriority iopriority;
    /**
     * qos.
     */
    private CapabilitiesQos qos;

    /**
     * getIopriority .
     *
     * @return CapabilitiesIopriority .
     */
    public CapabilitiesIopriority getIopriority() {
        return iopriority;
    }

    /**
     * setIopriority .
     *
     * @param param .
     */
    public void setIopriority(final CapabilitiesIopriority param) {
        this.iopriority = param;
    }

    /**
     * getQos .
     *
     * @return CapabilitiesQos .
     */
    public CapabilitiesQos getQos() {
        return qos;
    }

    /**
     * setQos .
     *
     * @param param .
     */
    public void setQos(final CapabilitiesQos param) {
        this.qos = param;
    }

    /**
     * getResourceType .
     *
     * @return String .
     */
    public String getResourceType() {
        return resourceType;
    }

    /**
     * setResourceType .
     *
     * @param param .
     */
    public void setResourceType(final String param) {
        this.resourceType = param;
    }

    /**
     * getCompression .
     *
     * @return Boolean .
     */
    public Boolean getCompression() {
        return compression;
    }

    /**
     * setCompression .
     *
     * @param param .
     */
    public void setCompression(final Boolean param) {
        this.compression = param;
    }

    /**
     * getDeduplication .
     *
     * @return Boolean .
     */
    public Boolean getDeduplication() {
        return deduplication;
    }

    /**
     * setDeduplication .
     *
     * @param param .
     */
    public void setDeduplication(final Boolean param) {
        this.deduplication = param;
    }

    /**
     * getSmarttier .
     *
     * @return CapabilitiesSmarttier .
     */
    public CapabilitiesSmarttier getSmarttier() {
        return smarttier;
    }

    /**
     * setSmarttier .
     *
     * @param param .
     */
    public void setSmarttier(final CapabilitiesSmarttier param) {
        this.smarttier = param;
    }
}
