package com.huawei.dmestore.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * DmeVmwareRelation
 *
 * @ClassName: DmeVmwareRelation
 * @Company: GH-CA
 * @author: yy
 * @since 2020-09-02
 **/
public class DmeVmwareRelation implements Serializable {
    /**
     * id .
     */
    private int id;
    /**
     * storeId .
     */
    private String storeId;
    /**
     * storeName .
     */
    private String storeName;
    /**
     * volumeId .
     */
    private String volumeId;
    /**
     * volumeName .
     */
    private String volumeName;
    /**
     * volumeWwn .
     */
    private String volumeWwn;
    /**
     * volumeShare .
     */
    private String volumeShare;
    /**
     * volumeFs .
     */
    private String volumeFs;
    /**
     * storageDeviceId .
     */
    private String storageDeviceId;
    /**
     * shareId .
     */
    private String shareId;
    /**
     * shareName .
     */
    private String shareName;
    /**
     * fsId .
     */
    private String fsId;
    /**
     * fsName .
     */
    private String fsName;
    /**
     * logicPortId .
     */
    private String logicPortId;
    /**
     * logicPortName .
     */
    private String logicPortName;
    /**
     * storeType .
     */
    private String storeType;
    /**
     * 存储设备类型
     */
    private String storageType;
    /**
     * createTime .
     */
    private Date createTime;
    /**
     * updateTime .
     */
    private Date updateTime;
    /**
     * state .
     */
    private int state;

    /**
     * getId .
     *
     * @return int .
     */
    public int getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final int param) {
        this.id = param;
    }

    /**
     * getStoreId .
     *
     * @return String .
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * setStoreId .
     *
     * @param param .
     */
    public void setStoreId(final String param) {
        this.storeId = param;
    }

    /**
     * getStoreName .
     *
     * @return String .
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * setStoreName .
     *
     * @param param .
     */
    public void setStoreName(final String param) {
        this.storeName = param;
    }

    /**
     * getVolumeId .
     *
     * @return String .
     */
    public String getVolumeId() {
        return volumeId;
    }

    /**
     * setVolumeId .
     *
     * @param param .
     */
    public void setVolumeId(final String param) {
        this.volumeId = param;
    }

    /**
     * getVolumeName .
     *
     * @return String .
     */
    public String getVolumeName() {
        return volumeName;
    }

    /**
     * setVolumeName .
     *
     * @param param .
     */
    public void setVolumeName(final String param) {
        this.volumeName = param;
    }

    /**
     * getVolumeWwn .
     *
     * @return String .
     */
    public String getVolumeWwn() {
        return volumeWwn;
    }

    /**
     * setVolumeWwn .
     *
     * @param param .
     */
    public void setVolumeWwn(final String param) {
        this.volumeWwn = param;
    }

    /**
     * getVolumeShare .
     *
     * @return String .
     */
    public String getVolumeShare() {
        return volumeShare;
    }

    /**
     * setVolumeShare .
     *
     * @param param .
     */
    public void setVolumeShare(final String param) {
        this.volumeShare = param;
    }

    /**
     * getVolumeFs .
     *
     * @return String .
     */
    public String getVolumeFs() {
        return volumeFs;
    }

    /**
     * setVolumeFs .
     *
     * @param param .
     */
    public void setVolumeFs(final String param) {
        this.volumeFs = param;
    }

    /**
     * getShareId .
     *
     * @return String .
     */
    public String getShareId() {
        return shareId;
    }

    /**
     * setShareId .
     *
     * @param param .
     */
    public void setShareId(final String param) {
        this.shareId = param;
    }

    /**
     * getShareName .
     *
     * @return String .
     */
    public String getShareName() {
        return shareName;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setShareName(final String param) {
        this.shareName = param;
    }

    /**
     * getFsId .
     *
     * @return String .
     */
    public String getFsId() {
        return fsId;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setFsId(final String param) {
        this.fsId = param;
    }

    /**
     * getFsName .
     *
     * @return String .
     */
    public String getFsName() {
        return fsName;
    }

    /**
     * setFsName .
     *
     * @param param .
     */
    public void setFsName(final String param) {
        this.fsName = param;
    }

    /**
     * getLogicPortId .
     *
     * @return String .
     */
    public String getLogicPortId() {
        return logicPortId;
    }

    /**
     * setLogicPortId .
     *
     * @param param .
     */
    public void setLogicPortId(final String param) {
        this.logicPortId = param;
    }

    /**
     * getLogicPortName .
     *
     * @return String .
     */
    public String getLogicPortName() {
        return logicPortName;
    }

    /**
     * setLogicPortName .
     *
     * @param param .
     */
    public void setLogicPortName(final String param) {
        this.logicPortName = param;
    }

    /**
     * getStoreType .
     *
     * @return String .
     */
    public String getStoreType() {
        return storeType;
    }

    /**
     * setStoreType .
     *
     * @param param .
     */
    public void setStoreType(final String param) {
        this.storeType = param;
    }

    /**
     * getCreateTime .
     *
     * @return Date .
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime .
     *
     * @param param .
     */
    public void setCreateTime(final Date param) {
        this.createTime = param;
    }

    /**
     * getUpdateTime .
     *
     * @return Date .
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * setUpdateTime .
     *
     * @param param .
     */
    public void setUpdateTime(final Date param) {
        this.updateTime = param;
    }

    /**
     * getState .
     *
     * @return int .
     */
    public int getState() {
        return state;
    }

    /**
     * setState .
     *
     * @param param .
     */
    public void setState(final int param) {
        this.state = param;
    }

    /**
     * getStorageDeviceId .
     *
     * @return String .
     */
    public String getStorageDeviceId() {
        return storageDeviceId;
    }

    /**
     * setStorageDeviceId .
     *
     * @param param .
     */
    public void setStorageDeviceId(final String param) {
        this.storageDeviceId = param;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }
}
