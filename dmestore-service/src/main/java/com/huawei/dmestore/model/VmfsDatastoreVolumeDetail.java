package com.huawei.dmestore.model;

/**
 * VmfsDatastoreVolumeDetail
 *
 * @author Administrator .
 * @Description: vmfs Datastore劵详情
 * @since 2020-12-08
 **/
public class VmfsDatastoreVolumeDetail {
    /**
     * name .
     */
    private String name;
    /**
     * wwn .
     */
    private String wwn;
    /**
     * smartTier .
     */
    private String smartTier;
    /**
     * evolutionaryInfo .
     */
    private String evolutionaryInfo;
    /**
     * device .
     */
    private String device;
    /**
     * storagePool .
     */
    private String storagePool;
    /**
     * serviceLevel .
     */
    private String serviceLevel;
    /**
     * storage .
     */
    private String storage;
    /**
     * dudeplication .
     */
    private boolean dudeplication;
    /**
     * provisionType .
     */
    private String provisionType;
    /**
     * compression .
     */
    private boolean compression;
    /**
     * applicationType .
     */
    private String applicationType;
    /**
     * controlPolicy .
     */
    private String controlPolicy;
    /**
     * trafficControl .
     */
    private String trafficControl;

    /**
     * smartQos vmfs高级选项qos相关数据.
     **/
    private SmartQos smartQos;

    public SmartQos getSmartQos() {
        return smartQos;
    }

    public void setSmartQos(SmartQos smartQos) {
        this.smartQos = smartQos;
    }

    /**
     * getCompression .
     *
     * @return boolean .
     */
    public boolean getCompression() {
        return compression;
    }

    /**
     * setCompression .
     *
     * @param param .
     */
    public void setCompression(final boolean param) {
        this.compression = param;
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
     * getWwn .
     *
     * @return String .
     */
    public String getWwn() {
        return wwn;
    }

    /**
     * setWwn .
     *
     * @param param .
     */
    public void setWwn(final String param) {
        this.wwn = param;
    }

    /**
     * getSmartTier .
     *
     * @return String .
     */
    public String getSmartTier() {
        return smartTier;
    }

    /**
     * setSmartTier .
     *
     * @param param .
     */
    public void setSmartTier(final String param) {
        this.smartTier = param;
    }

    /**
     * getEvolutionaryInfo .
     *
     * @return String .
     */
    public String getEvolutionaryInfo() {
        return evolutionaryInfo;
    }

    /**
     * setEvolutionaryInfo .
     *
     * @param param .
     */
    public void setEvolutionaryInfo(final String param) {
        this.evolutionaryInfo = param;
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
     * getStoragePool .
     *
     * @return String .
     */
    public String getStoragePool() {
        return storagePool;
    }

    /**
     * setStoragePool .
     *
     * @param param .
     */
    public void setStoragePool(final String param) {
        this.storagePool = param;
    }

    /**
     * getServiceLevel .
     *
     * @return String .
     */
    public String getServiceLevel() {
        return serviceLevel;
    }

    /**
     * setServiceLevel .
     *
     * @param param .
     */
    public void setServiceLevel(final String param) {
        this.serviceLevel = param;
    }

    /**
     * getStorage .
     *
     * @return String .
     */
    public String getStorage() {
        return storage;
    }

    /**
     * setStorage .
     *
     * @param param .
     */
    public void setStorage(final String param) {
        this.storage = param;
    }

    /**
     * setDudeplication .
     *
     * @return String .
     */
    public boolean getDudeplication() {
        return dudeplication;
    }

    /**
     * setDudeplication .
     *
     * @param param .
     */
    public void setDudeplication(final boolean param) {
        this.dudeplication = param;
    }

    /**
     * getProvisionType .
     *
     * @return String .
     */
    public boolean isDudeplication() {
        return dudeplication;
    }

    /**
     * isCompression .
     *
     * @return String .
     */
    public boolean isCompression() {
        return compression;
    }

    /**
     * getApplicationType .
     *
     * @return String .
     */
    public String getApplicationType() {
        return applicationType;
    }

    /**
     * setApplicationType .
     *
     * @param param .
     */
    public void setApplicationType(final String param) {
        this.applicationType = param;
    }

    /**
     * getControlPolicy .
     *
     * @return String .
     */
    public String getControlPolicy() {
        return controlPolicy;
    }

    /**
     * setControlPolicy .
     *
     * @param param .
     */
    public void setControlPolicy(final String param) {
        this.controlPolicy = param;
    }

    /**
     * getTrafficControl .
     *
     * @return String .
     */
    public String getTrafficControl() {
        return trafficControl;
    }

    /**
     * setTrafficControl .
     *
     * @param param .
     */
    public void setTrafficControl(final String param) {
        this.trafficControl = param;
    }

    @Override
    public final String toString() {
        return "VmfsDatastoreVolumeDetail{"
            + "name='" + name + '\''
            + ", wwn='" + wwn + '\''
            + ", smartTier='" + smartTier + '\''
            + ", evolutionaryInfo='" + evolutionaryInfo + '\''
            + ", device='" + device + '\''
            + ", storagePool='" + storagePool + '\''
            + ", serviceLevel='" + serviceLevel + '\''
            + ", storage='" + storage + '\''
            + ", dudeplication=" + dudeplication
            + ", provisionType='" + provisionType + '\''
            + ", compression=" + compression
            + ", applicationType='" + applicationType + '\''
            + ", controlPolicy='" + controlPolicy + '\''
            + ", trafficControl='" + trafficControl + '\''
            + '}';
    }
}
