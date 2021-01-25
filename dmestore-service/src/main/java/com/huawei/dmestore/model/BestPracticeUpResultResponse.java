package com.huawei.dmestore.model;

import java.util.List;

/**
 * BestPracticeUpResultResponse .
 *
 * @author wangxiangyong .
 * @since 2020-12-01
 **/
public class BestPracticeUpResultResponse {
    /**
     * hostObjectId .
     */
    private String hostObjectId;

    /**
     * hostName .
     */
    private String hostName;

    /**
     * result .
     */
    private List<BestPracticeUpResultBase> result;

    /**
     * needReboot .
     */
    private boolean needReboot;

    /**
     * getHostObjectId .
     *
     * @return String .
     */
    public String getHostObjectId() {
        return hostObjectId;
    }

    /**
     * setHostObjectId .
     *
     * @param param .
     */
    public void setHostObjectId(final String param) {
        this.hostObjectId = param;
    }

    /**
     * getHostName .
     *
     * @return String .
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * setHostObjectId .
     *
     * @param param .
     */
    public void setHostName(final String param) {
        this.hostName = param;
    }

    /**
     * getResult .
     *
     * @return List<> .
     */
    public List<BestPracticeUpResultBase> getResult() {
        return result;
    }

    /**
     * setHostObjectId .
     *
     * @param param .
     */
    public void setResult(final List<BestPracticeUpResultBase> param) {
        this.result = param;
    }

    /**
     * getNeedReboot .
     *
     * @return boolean .
     */
    public boolean getNeedReboot() {
        return needReboot;
    }

    /**
     * setHostObjectId .
     *
     * @param param .
     */
    public void setNeedReboot(final boolean param) {
        this.needReboot = param;
    }

    @Override
    public final String toString() {
        return "BestPracticeUpResultResponse{" + "hostObjectId='" + hostObjectId + '\'' + ", hostName='" + hostName
            + '\'' + ", result=" + result + ", needReboot=" + needReboot + '}';
    }
}
