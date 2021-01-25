package com.huawei.dmestore.model;

/**
 * BestPracticeUpResultBase
 *
 * @author wangxiangyong
 * @since 2020-09-09
 */
public class BestPracticeUpResultBase {
    /**
     * hostSetting .
     */
    private String hostSetting;

    /**
     * needReboot .
     */
    private boolean needReboot;

    /**
     * hostObjectId .
     */
    private String hostObjectId;

    /**
     * updateResult .
     */
    private boolean updateResult;

    /**
     * getHostSetting .
     *
     * @return String .
     */
    public String getHostSetting() {
        return hostSetting;
    }

    /**
     * setHostSetting .
     *
     * @param param .
     */
    public void setHostSetting(final String param) {
        this.hostSetting = param;
    }

    /**
     * getNeedReboot .
     *
     * @return boolean.
     */
    public boolean getNeedReboot() {
        return needReboot;
    }

    /**
     * setHostSetting .
     *
     * @param param .
     */
    public void setNeedReboot(final boolean param) {
        this.needReboot = param;
    }

    /**
     * getHostObjectId .
     *
     * @return String .
     */
    public String getHostObjectId() {
        return hostObjectId;
    }

    /**
     * setHostSetting .
     *
     * @param param .
     */
    public void setHostObjectId(final String param) {
        this.hostObjectId = param;
    }

    /**
     * getUpdateResult .
     *
     * @return boolean .
     */
    public boolean getUpdateResult() {
        return updateResult;
    }

    /**
     * setHostSetting .
     *
     * @param param .
     */
    public void setUpdateResult(final boolean param) {
        this.updateResult = param;
    }

    @Override
    public final String toString() {
        return "BestPracticeUpResultBase{" + "hostSetting='" + hostSetting + '\'' + ", needReboot=" + needReboot
            + ", hostObjectId='" + hostObjectId + '\'' + ", updateResult=" + updateResult + '}';
    }
}
