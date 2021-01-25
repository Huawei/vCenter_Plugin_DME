package com.huawei.dmestore.model;

import java.util.List;

/**
 * CreateVolumesRequest
 *
 * @author wangxiangyong
 * @Description: CreateVolumesRequest
 * @since 2020-12-08
 **/
public class CreateVolumesRequest {
    /**
     * serviceLevelId .
     */
    private String serviceLevelId;
    /**
     * volumes .
     */
    private List<ServiceVolumeBasicParams> volumes;
    /**
     * mapping .
     */
    private ServiceVolumeMapping mapping;

    /**
     * getServiceLevelId .
     *
     * @return String .
     */
    public String getServiceLevelId() {
        return serviceLevelId;
    }

    /**
     * setMapping .
     *
     * @param param .
     */
    public void setServiceLevelId(final String param) {
        this.serviceLevelId = param;
    }

    /**
     * getVolumes .
     *
     * @return List<> .
     */
    public List<ServiceVolumeBasicParams> getVolumes() {
        return volumes;
    }

    /**
     * setMapping .
     *
     * @param param .
     */
    public void setVolumes(final List<ServiceVolumeBasicParams> param) {
        this.volumes = param;
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
