package com.huawei.dmestore.model;

import java.util.List;

/**
 * CustomizeVolumes
 *
 * @author wangxiangyong
 * @Description: CustomizeVolumes
 * @since 2020-12-08
 **/
public class CustomizeVolumes {
    /**
     * availabilityZone .
     */
    private String availabilityZone;

    /**
     * 0：自动，1：高性能层，2：性能层，3：容量层 默认值：自动 .
     **/
    private String initialDistributePolicy;

    /**
     * OA、OB，默认为空代表自动 .
     **/
    private String ownerController;
    /**
     * poolRawId .
     */
    private String poolRawId;

    /**
     * 预取策略，影响磁盘读取。取值范围 0: 不预取，1：固定预取，
     * 2：可变预取，3：智能预取 默认值：智能预取.
     **/
    private String prefetchPolicy;
    /**
     * prefetchValue .
     */
    private String prefetchValue;
    /**
     * storageId .
     */
    private String storageId;
    /**
     * tuning .
     */
    private CustomizeVolumeTuningForCreate tuning;
    /**
     * volumeSpecs .
     */
    private List<ServiceVolumeBasicParams> volumeSpecs;

    /**
     * getAvailabilityZone .
     *
     * @return String .
     */
    public String getAvailabilityZone() {
        return availabilityZone;
    }

    /**
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setAvailabilityZone(final String param) {
        this.availabilityZone = param;
    }

    /**
     * getInitialDistributePolicy .
     *
     * @return String .
     */
    public String getInitialDistributePolicy() {
        return initialDistributePolicy;
    }

    /**
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setInitialDistributePolicy(final String param) {
        this.initialDistributePolicy = param;
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
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setOwnerController(final String param) {
        this.ownerController = param;
    }

    /**
     * getPoolRawId .
     *
     * @return String .
     */
    public String getPoolRawId() {
        return poolRawId;
    }

    /**
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setPoolRawId(final String param) {
        this.poolRawId = param;
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
     * setAvailabilityZone .
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
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setPrefetchValue(final String param) {
        this.prefetchValue = param;
    }

    /**
     * getStorageId .
     *
     * @return String .
     */
    public String getStorageId() {
        return storageId;
    }

    /**
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setStorageId(final String param) {
        this.storageId = param;
    }

    /**
     * getTuning .
     *
     * @return CustomizeVolumeTuningForCreate .
     */
    public CustomizeVolumeTuningForCreate getTuning() {
        return tuning;
    }

    /**
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setTuning(final CustomizeVolumeTuningForCreate param) {
        this.tuning = param;
    }

    /**
     * getVolumeSpecs .
     *
     * @return List<> .
     */
    public List<ServiceVolumeBasicParams> getVolumeSpecs() {
        return volumeSpecs;
    }

    /**
     * setAvailabilityZone .
     *
     * @param param .
     */
    public void setVolumeSpecs(final List<ServiceVolumeBasicParams> param) {
        this.volumeSpecs = param;
    }
}
