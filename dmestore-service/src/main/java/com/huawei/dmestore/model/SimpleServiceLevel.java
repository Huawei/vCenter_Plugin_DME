package com.huawei.dmestore.model;

/**
 * SimpleServiceLevel
 *
 * @author lianq
 * @ClassName: SimpleServiceLevel
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class SimpleServiceLevel {
    /**
     * capabilities .
     */
    private SimpleCapabilities capabilities;
    /**
     * id .
     */
    private String id;
    /**
     * name .
     */
    private String name;
    /**
     * description .
     */
    private String description;
    /**
     * type .
     */
    private String type;
    /**
     * protocol .
     */
    private String protocol;
    /**
     * totalCapacity .
     */
    private Double totalCapacity;
    /**
     * freeCapacity .
     */
    private Double freeCapacity;
    /**
     * usedCapacity .
     */
    private Double usedCapacity;

    /**
     * getCapabilities .
     *
     * @return SimpleCapabilities 。
     */
    public SimpleCapabilities getCapabilities() {
        return capabilities;
    }

    /**
     * setCapabilities .
     *
     * @param param .
     */
    public void setCapabilities(final SimpleCapabilities param) {
        this.capabilities = param;
    }

    /**
     * getId .
     *
     * @return String 。
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
     * getDescription .
     *
     * @return String 。
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription .
     *
     * @param param .
     */
    public void setDescription(final String param) {
        this.description = param;
    }

    /**
     * getType .
     *
     * @return String 。
     */
    public String getType() {
        return type;
    }

    /**
     * setType .
     *
     * @param param .
     */
    public void setType(final String param) {
        this.type = param;
    }

    /**
     * getProtocol .
     *
     * @return String 。
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * setProtocol .
     *
     * @param param .
     */
    public void setProtocol(final String param) {
        this.protocol = param;
    }

    /**
     * getTotalCapacity .
     *
     * @return Double 。
     */
    public Double getTotalCapacity() {
        return totalCapacity;
    }

    /**
     * setTotalCapacity .
     *
     * @param param .
     */
    public void setTotalCapacity(final Double param) {
        this.totalCapacity = param;
    }

    /**
     * getFreeCapacity .
     *
     * @return Double 。
     */
    public Double getFreeCapacity() {
        return freeCapacity;
    }

    /**
     * setFreeCapacity .
     *
     * @param param .
     */
    public void setFreeCapacity(final Double param) {
        this.freeCapacity = param;
    }

    /**
     * getUsedCapacity .
     *
     * @return Double 。
     */
    public Double getUsedCapacity() {
        return usedCapacity;
    }

    /**
     * setUsedCapacity .
     *
     * @param param .
     */
    public void setUsedCapacity(final Double param) {
        this.usedCapacity = param;
    }
}
