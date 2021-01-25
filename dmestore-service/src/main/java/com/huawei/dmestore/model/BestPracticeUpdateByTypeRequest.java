package com.huawei.dmestore.model;

import java.util.List;

/**
 * BestPracticeUpdateByTypeRequest
 *
 * @author wangxiangyong
 * @since 2020-09-09
 */
public class BestPracticeUpdateByTypeRequest {
    /**
     * hostSetting .
     */
    private String hostSetting;

    /**
     * hostObjectIds .
     */
    private List<String> hostObjectIds;

    /**
     * getHostSetting .
     *
     * @return String .
     */
    public String getHostSetting() {
        return hostSetting;
    }

    /**
     * setHostObjectIds .
     *
     * @param param .
     */
    public void setHostSetting(final String param) {
        this.hostSetting = param;
    }

    /**
     * getHostObjectIds .
     *
     * @return List<> .
     */
    public List<String> getHostObjectIds() {
        return hostObjectIds;
    }

    /**
     * setHostObjectIds .
     *
     * @param param .
     */
    public void setHostObjectIds(final List<String> param) {
        this.hostObjectIds = param;
    }

    @Override
    public final String toString() {
        return "BestPracticeUpdateByTypeRequest{" + "hostSetting='" + hostSetting + '\'' + ", hostObjectIds="
            + hostObjectIds + '}';
    }
}
