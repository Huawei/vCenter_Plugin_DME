package com.huawei.dmestore.model;

import java.util.List;

/**
 * BestPracticeCheckRecordBean
 *
 * @author wangxiangyong
 * @since 2020-09-09
 */
public class BestPracticeCheckRecordBean {
    /**
     * hostSetting .
     */
    private String hostSetting;

    /**
     * recommendValue .
     */
    private String recommendValue;

    /**
     * level .
     */
    private String level;

    /**
     * count .
     */
    private int count;

    /**
     * hostList .
     */
    private List<BestPracticeBean> hostList;

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
     * getRecommendValue .
     *
     * @return String .
     */
    public String getRecommendValue() {
        return recommendValue;
    }

    /**
     * setRecommendValue .
     *
     * @param param .
     */
    public void setRecommendValue(final String param) {
        this.recommendValue = param;
    }

    /**
     * getLevel .
     *
     * @return String .
     */
    public String getLevel() {
        return level;
    }

    /**
     * setRecommendValue .
     *
     * @param param .
     */
    public void setLevel(final String param) {
        this.level = param;
    }

    /**
     * getCount .
     *
     * @return int .
     */
    public int getCount() {
        return count;
    }

    /**
     * setRecommendValue .
     *
     * @param param .
     */
    public void setCount(final int param) {
        this.count = param;
    }

    /**
     * getHostList .
     *
     * @return List<>.
     */
    public List<BestPracticeBean> getHostList() {
        return hostList;
    }

    /**
     * setRecommendValue .
     *
     * @param param .
     */
    public void setHostList(final List<BestPracticeBean> param) {
        this.hostList = param;
    }
}
