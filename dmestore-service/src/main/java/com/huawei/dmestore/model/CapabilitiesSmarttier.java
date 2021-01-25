package com.huawei.dmestore.model;

/**
 * CapabilitiesSmarttier
 *
 * @author lianq
 * @ClassName: CapabilitiesSmarttier
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class CapabilitiesSmarttier {
    /**
     * enabled .
     */
    private Boolean enabled;

    /**
     * policy .
     */
    private Integer policy;

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
     * getPolicy .
     *
     * @return Integer .
     */
    public Integer getPolicy() {
        return policy;
    }

    /**
     * setPolicy .
     *
     * @param param .
     */
    public void setPolicy(final Integer param) {
        this.policy = param;
    }
}
