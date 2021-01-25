package com.huawei.dmestore.model;


import java.util.Arrays;

/**
 * StorageDetail
 *
 * @author lianq
 * @ClassName: StorageDetail
 * @since 2020-12-08
 */
public class StorageDetail {
    /**
     * id .
     */
    private String id;
    /**
     * 名称 .
     */
    private String name;
    /**
     * ip地址 .
     */
    private String ip;
    /**
     * 状态 运行状态 0-离线 1-正常 2-故障 9-未管理 .
     */
    private String status;
    /**
     * 同步状态 同步状态 0-未同步 1-同步中 2-同步完成.
     */
    private String synStatus;
    /**
     * 设备序列号 .
     */
    private String sn;
    /**
     * 厂商 .
     */
    private String vendor;
    /**
     * 产品型号 .
     */
    private String model;
    /**
     * 已用容量 （单位:MB） .
     */
    private Double usedCapacity;
    /**
     * 裸容量（单位:MB）.
     */
    private Double totalCapacity;
    /**
     * 可得容量 .
     */
    private Double totalEffectiveCapacity;
    /**
     * 空闲容量 .
     */
    private Double freeEffectiveCapacity;
    /**
     * 订阅容量 .
     */
    private Double subscriptionCapacity;
    /**
     * 保护容量 /容量分布.
     */
    private Double protectionCapacity;
    /**
     * 文件系统 .
     */
    private Double fileCapacity;
    /**
     * 卷 .
     */
    private Double blockCapacity;
    /**
     * 去重容量.
     **/
    private Double dedupedCapacity;
    /**
     * 压缩容量.
     **/
    private Double compressedCapacity;
    /**
     * 节省后容量.
     **/
    private Double optimizeCapacity;
    /**
     * azIds .
     */
    private String[] azIds;
    /**
     * storagePool .
     */
    private String storagePool;
    /**
     * volume .
     */
    private String volume;
    /**
     * fileSystem .
     */
    private String fileSystem;
    /**
     * dTrees .
     */
    private String dTrees;
    /**
     * nfsShares .
     */
    private String nfsShares;
    /**
     * bandPorts .
     */
    private String bandPorts;
    /**
     * logicPorts .
     */
    private String logicPorts;
    /**
     * storageControllers .
     */
    private String storageControllers;
    /**
     * storageDisks .
     */
    private String storageDisks;
    /**
     * productVersion .
     */
    private String productVersion;
    /**
     * 警告 .
     */
    private String warning;
    /**
     * 事件 .
     */
    private String event;
    /**
     * 位置.
     **/
    private String location;
    /**
     * 补丁版本.
     **/
    private String patchVersion;
    /**
     * 维保开始时间.
     **/
    private String maintenanceStart;
    /**
     * 维保结束时间.
     **/
    private String maintenanceOvertime;
    /**
     * 存储类型不同导致页面及属性差异对象
     */
    private StorageTypeShow storageTypeShow;

    public StorageTypeShow getStorageTypeShow() {
        return storageTypeShow;
    }

    public void setStorageTypeShow(StorageTypeShow storageTypeShow) {
        this.storageTypeShow = storageTypeShow;
    }

    /**
     * getPatchVersion .
     *
     * @return String .
     */
    public String getPatchVersion() {
        return patchVersion;
    }

    /**
     * setPatchVersion .
     *
     * @param param .
     */
    public void setPatchVersion(final String param) {
        this.patchVersion = param;
    }

    /**
     * getMaintenanceStart .
     *
     * @return String .
     */
    public String getMaintenanceStart() {
        return maintenanceStart;
    }

    /**
     * setMaintenanceStart .
     *
     * @param param .
     */
    public void setMaintenanceStart(final String param) {
        this.maintenanceStart = param;
    }

    /**
     * getMaintenanceOvertime .
     *
     * @return String .
     */
    public String getMaintenanceOvertime() {
        return maintenanceOvertime;
    }

    /**
     * setMaintenanceOvertime .
     *
     * @param param .
     */
    public void setMaintenanceOvertime(final String param) {
        this.maintenanceOvertime = param;
    }

    /**
     * getWarning .
     *
     * @return String .
     */
    public String getWarning() {
        return warning;
    }

    /**
     * setWarning .
     *
     * @param param .
     */
    public void setWarning(final String param) {
        this.warning = param;
    }

    /**
     * getEvent .
     *
     * @return String .
     */
    public String getEvent() {
        return event;
    }

    /**
     * setEvent .
     *
     * @param param .
     */
    public void setEvent(final String param) {
        this.event = param;
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
     * getVolume .
     *
     * @return String .
     */
    public String getVolume() {
        return volume;
    }

    /**
     * setVolume .
     *
     * @param param .
     */
    public void setVolume(final String param) {
        this.volume = param;
    }

    /**
     * getFileSystem .
     *
     * @return String .
     */
    public String getFileSystem() {
        return fileSystem;
    }

    /**
     * setFileSystem .
     *
     * @param param .
     */
    public void setFileSystem(final String param) {
        this.fileSystem = param;
    }

    /**
     * getdTrees .
     *
     * @return String .
     */
    public String getdTrees() {
        return dTrees;
    }

    /**
     * setdTrees .
     *
     * @param param .
     */
    public void setdTrees(final String param) {
        this.dTrees = param;
    }

    /**
     * getNfsShares .
     *
     * @return String .
     */
    public String getNfsShares() {
        return nfsShares;
    }

    /**
     * setNfsShares .
     *
     * @param param .
     */
    public void setNfsShares(final String param) {
        this.nfsShares = param;
    }

    /**
     * getBandPorts .
     *
     * @return String .
     */
    public String getBandPorts() {
        return bandPorts;
    }

    /**
     * setBandPorts .
     *
     * @param param .
     */
    public void setBandPorts(final String param) {
        this.bandPorts = param;
    }

    /**
     * getLogicPorts .
     *
     * @return String .
     */
    public String getLogicPorts() {
        return logicPorts;
    }

    /**
     * setLogicPorts .
     *
     * @param param .
     */
    public void setLogicPorts(final String param) {
        this.logicPorts = param;
    }

    /**
     * getStorageControllers .
     *
     * @return String .
     */
    public String getStorageControllers() {
        return storageControllers;
    }

    /**
     * setStorageControllers .
     *
     * @param param .
     */
    public void setStorageControllers(final String param) {
        this.storageControllers = param;
    }

    /**
     * getStorageDisks .
     *
     * @return String .
     */
    public String getStorageDisks() {
        return storageDisks;
    }

    /**
     * setStorageDisks .
     *
     * @param param .
     */
    public void setStorageDisks(final String param) {
        this.storageDisks = param;
    }

    /**
     * getId .
     *
     * @return String .
     */
    public String getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final String param) {
        this.id = param;
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
     * getIp .
     *
     * @return String .
     */
    public String getIp() {
        return ip;
    }

    /**
     * setIp .
     *
     * @param param .
     */
    public void setIp(final String param) {
        this.ip = param;
    }

    /**
     * getStatus .
     *
     * @return String .
     */
    public String getStatus() {
        return status;
    }

    /**
     * setStatus .
     *
     * @param param .
     */
    public void setStatus(final String param) {
        this.status = param;
    }

    /**
     * getSynStatus .
     *
     * @return String .
     */
    public String getSynStatus() {
        return synStatus;
    }

    /**
     * setSynStatus .
     *
     * @param param .
     */
    public void setSynStatus(final String param) {
        this.synStatus = param;
    }

    /**
     * getSn .
     *
     * @return String .
     */
    public String getSn() {
        return sn;
    }

    /**
     * setSn .
     *
     * @param param .
     */
    public void setSn(final String param) {
        this.sn = param;
    }

    /**
     * getVendor .
     *
     * @return String .
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * setVendor .
     *
     * @param param .
     */
    public void setVendor(final String param) {
        this.vendor = param;
    }

    /**
     * getModel .
     *
     * @return String .
     */
    public String getModel() {
        return model;
    }

    /**
     * setModel .
     *
     * @param param .
     */
    public void setModel(final String param) {
        this.model = param;
    }

    /**
     * getProductVersion .
     *
     * @return String .
     */
    public String getProductVersion() {
        return productVersion;
    }

    /**
     * setProductVersion .
     *
     * @param param .
     */
    public void setProductVersion(final String param) {
        this.productVersion = param;
    }

    /**
     * getLocation .
     *
     * @return String .
     */
    public String getLocation() {
        return location;
    }

    /**
     * setLocation .
     *
     * @param param .
     */
    public void setLocation(final String param) {
        this.location = param;
    }

    /**
     * getAzIds .
     *
     * @return String[] .
     */
    public String[] getAzIds() {
        return azIds;
    }

    /**
     * setAzIds .
     *
     * @param param .
     */
    public void setAzIds(final String[] param) {
        this.azIds = param;
    }

    /**
     * getUsedCapacity .
     *
     * @return Double .
     */
    public Double getUsedCapacity() {
        return usedCapacity;
    }

    /**
     * setUsedCapacity .
     *
     * @param param .
     */
    public void setUsedCapacity(final Double param) {
        this.usedCapacity = param;
    }

    /**
     * getTotalCapacity .
     *
     * @return Double .
     */
    public Double getTotalCapacity() {
        return totalCapacity;
    }

    /**
     * setTotalCapacity .
     *
     * @param param .
     */
    public void setTotalCapacity(final Double param) {
        this.totalCapacity = param;
    }

    /**
     * getTotalEffectiveCapacity .
     *
     * @return Double .
     */
    public Double getTotalEffectiveCapacity() {
        return totalEffectiveCapacity;
    }

    /**
     * setTotalEffectiveCapacity .
     *
     * @param param .
     */
    public void setTotalEffectiveCapacity(final Double param) {
        this.totalEffectiveCapacity = param;
    }

    /**
     * getFreeEffectiveCapacity .
     *
     * @return Double .
     */
    public Double getFreeEffectiveCapacity() {
        return freeEffectiveCapacity;
    }

    /**
     * setFreeEffectiveCapacity .
     *
     * @param param .
     */
    public void setFreeEffectiveCapacity(final Double param) {
        this.freeEffectiveCapacity = param;
    }

    /**
     * getSubscriptionCapacity .
     *
     * @return Double .
     */
    public Double getSubscriptionCapacity() {
        return subscriptionCapacity;
    }

    /**
     * setSubscriptionCapacity .
     *
     * @param param .
     */
    public void setSubscriptionCapacity(final Double param) {
        this.subscriptionCapacity = param;
    }

    /**
     * getProtectionCapacity .
     *
     * @return Double .
     */
    public Double getProtectionCapacity() {
        return protectionCapacity;
    }

    /**
     * setProtectionCapacity .
     *
     * @param param .
     */
    public void setProtectionCapacity(final Double param) {
        this.protectionCapacity = param;
    }

    /**
     * getFileCapacity .
     *
     * @return String .
     */
    public Double getFileCapacity() {
        return fileCapacity;
    }

    /**
     * setFileCapacity .
     *
     * @param param .
     */
    public void setFileCapacity(final Double param) {
        this.fileCapacity = param;
    }

    /**
     * getBlockCapacity .
     *
     * @return Double .
     */
    public Double getBlockCapacity() {
        return blockCapacity;
    }

    /**
     * setBlockCapacity .
     *
     * @param param .
     */
    public void setBlockCapacity(final Double param) {
        this.blockCapacity = param;
    }

    /**
     * getDedupedCapacity .
     *
     * @return Double .
     */
    public Double getDedupedCapacity() {
        return dedupedCapacity;
    }

    /**
     * setDedupedCapacity .
     *
     * @param param .
     */
    public void setDedupedCapacity(final Double param) {
        this.dedupedCapacity = param;
    }

    /**
     * getCompressedCapacity .
     *
     * @return Double .
     */
    public Double getCompressedCapacity() {
        return compressedCapacity;
    }

    /**
     * setCompressedCapacity .
     *
     * @param param .
     */
    public void setCompressedCapacity(final Double param) {
        this.compressedCapacity = param;
    }

    /**
     * getOptimizeCapacity .
     *
     * @return Double .
     */
    public Double getOptimizeCapacity() {
        return optimizeCapacity;
    }

    /**
     * setOptimizeCapacity .
     *
     * @param param .
     */
    public void setOptimizeCapacity(final Double param) {
        this.optimizeCapacity = param;
    }

    @Override
    public final String toString() {
        return "StorageDetail{"
            + "id='" + id + '\''
            + ", name='" + name + '\''
            + ", ip='" + ip + '\''
            + ", status='" + status + '\''
            + ", synStatus='" + synStatus + '\''
            + ", sn='" + sn + '\''
            + ", vendor='" + vendor + '\''
            + ", model='" + model + '\''
            + ", usedCapacity=" + usedCapacity
            + ", totalCapacity=" + totalCapacity
            + ", totalEffectiveCapacity=" + totalEffectiveCapacity
            + ", freeEffectiveCapacity=" + freeEffectiveCapacity
            + ", subscriptionCapacity=" + subscriptionCapacity
            + ", protectionCapacity=" + protectionCapacity
            + ", fileCapacity=" + fileCapacity
            + ", blockCapacity=" + blockCapacity
            + ", dedupedCapacity=" + dedupedCapacity
            + ", compressedCapacity=" + compressedCapacity
            + ", optimizeCapacity=" + optimizeCapacity
            + ", azIds=" + Arrays.toString(azIds)
            + ", storagePool='" + storagePool + '\''
            + ", volume='" + volume + '\''
            + ", fileSystem='" + fileSystem + '\''
            + ", dTrees='" + dTrees + '\''
            + ", nfsShares='" + nfsShares + '\''
            + ", bandPorts='" + bandPorts + '\''
            + ", logicPorts='" + logicPorts + '\''
            + ", storageControllers='" + storageControllers + '\''
            + ", storageDisks='" + storageDisks + '\''
            + ", productVersion='" + productVersion + '\''
            + ", warning='" + warning + '\''
            + ", event='" + event + '\''
            + ", location='" + location + '\''
            + ", patchVersion='" + patchVersion + '\''
            + ", maintenanceStart='" + maintenanceStart + '\''
            + ", maintenanceOvertime='" + maintenanceOvertime + '\''
            + '}';
    }
}
