package com.huawei.dmestore.model;

/**
 * CapabilitiesIopriority
 *
 * @author lianq
 * @Description: CapabilitiesIopriority
 * @since 2020-12-01
 **/
public class CapabilitiesIopriority {
    /**
     * IO优先级，默认false .
     */
    private Boolean enabled;

    /**
     * IO优先级枚举值, 取值范围：1：低；2：中；3：高 .
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

    @Override
    public final String toString() {
        return "CapabilitiesIopriority{" + "enabled=" + enabled + ", policy=" + policy + '}';
    }
}
