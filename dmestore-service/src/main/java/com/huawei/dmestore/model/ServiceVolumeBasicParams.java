package com.huawei.dmestore.model;

/**
 * ServiceVolumeBasicParams
 *
 * @author wangxiangyong.
 * @since 2020-12-08
 **/
public class ServiceVolumeBasicParams {
    /**
     * name .
     */
    private String name;
    /**
     * capacity .
     */
    private Integer capacity;
    /**
     * count .
     */
    private Integer count;
    /**
     * startSuffix .
     */
    private Integer startSuffix;
    /**
     * unit .
     */
    private String unit;

    /**
     * getName.
     *
     * @return String.
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
     * getCapacity .
     *
     * @return int.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * setCapacity .
     *
     * @param param .
     */
    public void setCapacity(final Integer param) {
        this.capacity = param;
    }

    /**
     * getCount .
     *
     * @return int.
     */
    public int getCount() {
        return count;
    }

    /**
     * setCount .
     *
     * @param param .
     */
    public void setCount(final Integer param) {
        this.count = param;
    }

    /**
     * getStartSuffix .
     *
     * @return Integer.
     */
    public Integer getStartSuffix() {
        return startSuffix;
    }

    /**
     * setStartSuffix .
     *
     * @param param .
     */
    public void setStartSuffix(final Integer param) {
        this.startSuffix = param;
    }

    /**
     * getUnit .
     *
     * @return String.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * setUnit .
     *
     * @param param .
     */
    public void setUnit(final String param) {
        this.unit = param;
    }
}
