package com.huawei.dmestore.model;

import java.util.List;

/**
 * VolumeListRestponse
 *
 * @author Administrator
 * @since 2020-12-08
 **/
public class VolumeListRestponse {
    /**
     * volumeList .
     */
    private List<Volume> volumeList;
    /**
     * count .
     */
    private int count;

    /**
     * getVolumeList .
     *
     * @return List<> .
     */
    public List<Volume> getVolumeList() {
        return volumeList;
    }

    /**
     * setVolumeList .
     *
     * @param param .
     */
    public void setVolumeList(final List<Volume> param) {
        this.volumeList = param;
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
     * setCount .
     *
     * @param param .
     */
    public void setCount(final int param) {
        this.count = param;
    }
}
