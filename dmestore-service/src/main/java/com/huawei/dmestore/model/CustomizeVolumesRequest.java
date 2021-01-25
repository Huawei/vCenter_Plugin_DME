package com.huawei.dmestore.model;

/**
 * CustomizeVolumesRequest
 *
 * @author wangxiangyong
 * @Description: CustomizeVolumesRequest
 * @since 2020-12-08
 **/
public class CustomizeVolumesRequest {
    /**
     * customizeVolumes .
     */
    private CustomizeVolumes customizeVolumes;
    /**
     * mapping .
     */
    private ServiceVolumeMapping mapping;

    /**
     * getCustomizeVolumes .
     *
     * @return CustomizeVolumes .
     */
    public CustomizeVolumes getCustomizeVolumes() {
        return customizeVolumes;
    }

    /**
     * setMapping .
     *
     * @param param .
     */
    public void setCustomizeVolumes(final CustomizeVolumes param) {
        this.customizeVolumes = param;
    }

    /**
     * getMapping .
     *
     * @return ServiceVolumeMapping .
     */
    public ServiceVolumeMapping getMapping() {
        return mapping;
    }

    /**
     * setMapping .
     *
     * @param param .
     */
    public void setMapping(final ServiceVolumeMapping param) {
        this.mapping = param;
    }
}
