package com.huawei.dmestore.model;

/**
 * CapacityAutonegotiation
 *
 * @author lianq
 * @Description: CapacityAutonegotiation
 * @since 2020-10-15
 */
public class CapacityAutonegotiation {
    /**
     * capacitymodeoff .
     */
    public static final String CAPACITY_MODE_OFF = "grow_off";

    /**
     * capacitymodeoff . old dme
     */
    public static final String CAPACITY_MODE_OFF_OLD = "off";

    /**
     * capacitymodeauto .
     */
    public static final String CAPACITY_MODE_AUTO = "grow_shrink";

    /**
     * autoSizeEnable .
     */
    private Boolean autoSizeEnable;

    /**
     * getAutoSizeEnable .
     *
     * @return Boolean .
     */
    public Boolean getAutoSizeEnable() {
        return autoSizeEnable;
    }

    /**
     * setAutoSizeEnable .
     *
     * @param param .
     */
    public void setAutoSizeEnable(final Boolean param) {
        this.autoSizeEnable = param;
    }
}
