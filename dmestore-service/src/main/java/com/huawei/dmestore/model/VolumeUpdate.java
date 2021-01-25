package com.huawei.dmestore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VolumeUpdate
 *
 * @author lianq
 * @ClassName: VolumeUpdate
 * @since 2020-12-08
 */
public class VolumeUpdate {
    /**
     * name .
     */
    private String name;
    /**
     * ownerController .
     */
    @JsonProperty("owner_controller")
    private String ownerController;
    /**
     * prefetchPolicy .
     */
    @JsonProperty("prefetch_policy")
    private String prefetchPolicy;
    /**
     * prefetchValue .
     */
    @JsonProperty("prefetch_value")
    private String prefetchValue;
    /**
     * tuning .
     */
    private CustomizeVolumeTuning tuning;

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
     * getOwnerController .
     *
     * @return String .
     */
    public String getOwnerController() {
        return ownerController;
    }

    /**
     * setOwnerController .
     *
     * @param param .
     */
    public void setOwnerController(final String param) {
        this.ownerController = param;
    }

    /**
     * getPrefetchPolicy .
     *
     * @return String .
     */
    public String getPrefetchPolicy() {
        return prefetchPolicy;
    }

    /**
     * setPrefetchPolicy .
     *
     * @param param .
     */
    public void setPrefetchPolicy(final String param) {
        this.prefetchPolicy = param;
    }

    /**
     * getPrefetchValue .
     *
     * @return String .
     */
    public String getPrefetchValue() {
        return prefetchValue;
    }

    /**
     * setPrefetchValue .
     *
     * @param param .
     */
    public void setPrefetchValue(final String param) {
        this.prefetchValue = param;
    }

    /**
     * getTuning .
     *
     * @return CustomizeVolumeTuning .
     */
    public CustomizeVolumeTuning getTuning() {
        return tuning;
    }

    /**
     * setTuning .
     *
     * @param param .
     */
    public void setTuning(final CustomizeVolumeTuning param) {
        this.tuning = param;
    }

    @Override
    public final String toString() {
        return "VolumeUpdate{"
            + "name='" + name + '\''
            + ", ownerController='" + ownerController + '\''
            + ", prefetchPolicy='" + prefetchPolicy + '\''
            + ", prefetchValue='" + prefetchValue + '\''
            + ", tuning=" + tuning
            + '}';
    }
}
