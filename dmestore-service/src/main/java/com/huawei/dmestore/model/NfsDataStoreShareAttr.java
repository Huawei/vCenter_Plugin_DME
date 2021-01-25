package com.huawei.dmestore.model;

import java.util.List;

/**
 * NfsDataStoreShareAttr
 *
 * @author wangxiangyong
 * @since 2020-12-08
 **/
public class NfsDataStoreShareAttr {
    /**
     * fsName .
     */
    private String fsName;
    /**
     * name .
     */
    private String name;
    /**
     * sharePath .
     */
    private String sharePath;
    /**
     * description .
     */
    private String description;
    /**
     * owningDtreeId .
     */
    private String owningDtreeId;
    /**
     * owningDtreeName .
     */
    private String owningDtreeName;
    /**
     * deviceName .
     */
    private String deviceName;
    /**
     * authClientList .
     */
    private List<AuthClient> authClientList;

    /**
     * getFsName .
     *
     * @return String.
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
     * getName .
     *
     * @return String.
     */
    public String getName() {
        return name;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getSharePath .
     *
     * @return String.
     */
    public String getSharePath() {
        return sharePath;
    }

    /**
     * setSharePath .
     *
     * @param param .
     */
    public void setSharePath(final String param) {
        this.sharePath = param;
    }

    /**
     * getDescription .
     *
     * @return String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription .
     *
     * @param param .
     */
    public void setDescription(final String param) {
        this.description = param;
    }

    /**
     * getOwningDtreeId .
     *
     * @return String.
     */
    public String getOwningDtreeId() {
        return owningDtreeId;
    }

    /**
     * setOwningDtreeId .
     *
     * @param param .
     */
    public void setOwningDtreeId(final String param) {
        this.owningDtreeId = param;
    }

    /**
     * getOwningDtreeName .
     *
     * @return String.
     */
    public String getOwningDtreeName() {
        return owningDtreeName;
    }

    /**
     * setOwningDtreeName .
     *
     * @param param .
     */
    public void setOwningDtreeName(final String param) {
        this.owningDtreeName = param;
    }

    /**
     * getDeviceName .
     *
     * @return String.
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     * setDeviceName .
     *
     * @param param .
     */
    public void setDeviceName(final String param) {
        this.deviceName = param;
    }

    /**
     * getAuthClientList .
     *
     * @return List<>.
     */
    public List<AuthClient> getAuthClientList() {
        return authClientList;
    }

    /**
     * setAuthClientList .
     *
     * @param param .
     */
    public void setAuthClientList(final List<AuthClient> param) {
        this.authClientList = param;
    }

    @Override
    public final String toString() {
        return "NfsDataStoreShareAttr{"
            + "fsName='" + fsName + '\''
            + ", name='" + name + '\''
            + ", sharePath='" + sharePath + '\''
            + ", description='" + description + '\''
            + ", owningDtreeId='" + owningDtreeId + '\''
            + ", owningDtreeName='" + owningDtreeName + '\''
            + ", deviceName='" + deviceName + '\''
            + ", authClientList=" + authClientList
            + '}';
    }
}
