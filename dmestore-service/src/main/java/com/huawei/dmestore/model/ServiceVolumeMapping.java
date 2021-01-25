package com.huawei.dmestore.model;

/**
 * ServiceVolumeMapping
 *
 * @author wangxiangyong
 * @since 2020-12-08
 **/
public class ServiceVolumeMapping {
    /**
     * hostId .
     */
    private String hostId;
    /**
     * hostgroupId .
     */
    private String hostgroupId;

    /**
     * getHostId .
     *
     * @return String .
     */
    public String getHostId() {
        return hostId;
    }

    /**
     * setHostId .
     *
     * @param param .
     */
    public void setHostId(final String param) {
        this.hostId = param;
    }

    /**
     * getHostgroupId .
     *
     * @return String .
     */
    public String getHostgroupId() {
        return hostgroupId;
    }

    /**
     * setHostgroupId .
     *
     * @param param .
     */
    public void setHostgroupId(final String param) {
        this.hostgroupId = param;
    }
}
