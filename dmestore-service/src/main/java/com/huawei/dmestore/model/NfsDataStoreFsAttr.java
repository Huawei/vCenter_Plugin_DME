package com.huawei.dmestore.model;

import java.io.Serializable;

/**
 * NfsDataStoreFsAttr
 *
 * @author wangxiangyong
 * @since 2020-12-08
 **/
public class NfsDataStoreFsAttr implements Serializable {
    /**
     * name .
     */
    private String name;

    /**
     * description .
     */
    private String description;

    /**
     * device .
     */
    private String device;

    /**
     * storagePoolName .
     */
    private String storagePoolName;

    /**
     * provisionType .
     */
    private String provisionType;

    /**
     * applicationScenario .
     */
    private String applicationScenario;

    /**
     * dataDeduplication .
     */
    private Boolean dataDeduplication;

    /**
     * dateCompression .
     */
    private Boolean dateCompression;

    /**
     * controller .
     */
    private String controller;

    /**
     * fileSystemId .
     */
    private String fileSystemId;

    /**
     * getController .
     *
     * @return String .
     */
    public String getController() {
        return controller;
    }

    /**
     * setController .
     *
     * @param param .
     */
    public void setController(final String param) {
        this.controller = param;
    }

    /**
     * getName .
     *
     * @return String .
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
     * getDescription .
     *
     * @return String .
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
     * getDevice .
     *
     * @return String .
     */
    public String getDevice() {
        return device;
    }

    /**
     * setDevice .
     *
     * @param param .
     */
    public void setDevice(final String param) {
        this.device = param;
    }

    /**
     * getStoragePoolName .
     *
     * @return String .
     */
    public String getStoragePoolName() {
        return storagePoolName;
    }

    /**
     * setStoragePoolName .
     *
     * @param param .
     */
    public void setStoragePoolName(final String param) {
        this.storagePoolName = param;
    }

    /**
     * getProvisionType .
     *
     * @return String .
     */
    public String getProvisionType() {
        return provisionType;
    }

    /**
     * setProvisionType .
     *
     * @param param .
     */
    public void setProvisionType(final String param) {
        this.provisionType = param;
    }

    /**
     * getApplicationScenario .
     *
     * @return String .
     */
    public String getApplicationScenario() {
        return applicationScenario;
    }

    /**
     * setApplicationScenario .
     *
     * @param param .
     */
    public void setApplicationScenario(final String param) {
        this.applicationScenario = param;
    }

    /**
     * getDataDeduplication .
     *
     * @return Boolean .
     */
    public Boolean getDataDeduplication() {
        return dataDeduplication;
    }

    /**
     * setDataDeduplication .
     *
     * @param param .
     */
    public void setDataDeduplication(final Boolean param) {
        this.dataDeduplication = param;
    }

    /**
     * getDateCompression .
     *
     * @return Boolean .
     */
    public Boolean getDateCompression() {
        return dateCompression;
    }

    /**
     * setDateCompression .
     *
     * @param param .
     */
    public void setDateCompression(final Boolean param) {
        this.dateCompression = param;
    }

    /**
     * getFileSystemId .
     *
     * @return String .
     */
    public String getFileSystemId() {
        return fileSystemId;
    }

    /**
     * setFileSystemId .
     *
     * @param param .
     */
    public void setFileSystemId(final String param) {
        this.fileSystemId = param;
    }

    @Override
    public final String toString() {
        return "NfsDataStoreFsAttr{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", device='"
            + device + '\'' + ", storagePoolName='" + storagePoolName + '\'' + ", provisionType='" + provisionType
            + '\'' + ", applicationScenario='" + applicationScenario + '\'' + ", dataDeduplication=" + dataDeduplication
            + ", dateCompression=" + dateCompression + ", controller='" + controller + '\'' + ", fileSystemId='"
            + fileSystemId + '\'' + '}';
    }
}
