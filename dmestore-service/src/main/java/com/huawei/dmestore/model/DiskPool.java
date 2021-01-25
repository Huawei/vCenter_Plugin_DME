package com.huawei.dmestore.model;

/**
 * DiskPool
 *
 * @author lianq
 * @Description: DiskPool entity.
 * @since 2020-12-07
 */
public class DiskPool {
    /**
     * id .
     */
    private String id;

    /**
     * 原始id .
     */
    private String nativeId;

    /**
     * 上次修改时间 .
     */
    private String lastModified;

    /**
     * 最后监控时间 .
     */
    private String lastMonitorTime;

    /**
     * 监控状态 .
     */
    private String dataStatus;

    /**
     * name .
     */
    private String name;

    /**
     * 状态 .
     */
    private String status;

    /**
     * 运行状态 .
     */
    private String runningStatus;

    /**
     * 加密类型 .
     */
    private String encryptDiskType;

    /**
     * totalCapacity
     */
    private Double totalCapacity;

    /**
     * 已用容量 .
     */
    private Double usedCapacity;

    /**
     * 空闲容量 .
     */
    private Double freeCapacity;

    /**
     * 热备容量 .
     */
    private Double spareCapacity;

    /**
     * 已用热备容量 .
     */
    private Double usedSpareCapacity;

    /**
     * 在设备上的硬盘域Id .
     */
    private String poolId;

    /**
     * storageDeviceId .
     */
    private String storageDeviceId;

    /**
     * 使用率 .
     */
    private Double usageRate;

    /**
     * getUsageRate .
     *
     * @return Double .
     */
    public Double getUsageRate() {
        return usageRate;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setUsageRate(final Double param) {
        this.usageRate = param;
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
     * setUsageRate .
     *
     * @param param .
     */
    public void setId(final String param) {
        this.id = param;
    }

    /**
     * getNativeId .
     *
     * @return String .
     */
    public String getNativeId() {
        return nativeId;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setNativeId(final String param) {
        this.nativeId = param;
    }

    /**
     * getLastModified .
     *
     * @return String .
     */
    public String getLastModified() {
        return lastModified;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setLastModified(final String param) {
        this.lastModified = param;
    }

    /**
     * getLastMonitorTime .
     *
     * @return String .
     */
    public String getLastMonitorTime() {
        return lastMonitorTime;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setLastMonitorTime(final String param) {
        this.lastMonitorTime = param;
    }

    /**
     * getDataStatus .
     *
     * @return String .
     */
    public String getDataStatus() {
        return dataStatus;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setDataStatus(final String param) {
        this.dataStatus = param;
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
     * setUsageRate .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
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
     * setUsageRate .
     *
     * @param param .
     */
    public void setStatus(final String param) {
        this.status = param;
    }

    /**
     * getRunningStatus .
     *
     * @return String .
     */
    public String getRunningStatus() {
        return runningStatus;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setRunningStatus(final String param) {
        this.runningStatus = param;
    }

    /**
     * getEncryptDiskType .
     *
     * @return String .
     */
    public String getEncryptDiskType() {
        return encryptDiskType;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setEncryptDiskType(final String param) {
        this.encryptDiskType = param;
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
     * setUsageRate .
     *
     * @param param .
     */
    public void setTotalCapacity(final Double param) {
        this.totalCapacity = param;
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
     * setUsageRate .
     *
     * @param param .
     */
    public void setUsedCapacity(final Double param) {
        this.usedCapacity = param;
    }

    /**
     * getFreeCapacity .
     *
     * @return Double .
     */
    public Double getFreeCapacity() {
        return freeCapacity;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setFreeCapacity(final Double param) {
        this.freeCapacity = param;
    }

    /**
     * getSpareCapacity .
     *
     * @return Double .
     */
    public Double getSpareCapacity() {
        return spareCapacity;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setSpareCapacity(final Double param) {
        this.spareCapacity = param;
    }

    /**
     * getUsedSpareCapacity .
     *
     * @return Double .
     */
    public Double getUsedSpareCapacity() {
        return usedSpareCapacity;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setUsedSpareCapacity(final Double param) {
        this.usedSpareCapacity = param;
    }

    /**
     * getPoolId .
     *
     * @return String .
     */
    public String getPoolId() {
        return poolId;
    }

    /**
     * setUsageRate .
     *
     * @param param .
     */
    public void setPoolId(final String param) {
        this.poolId = param;
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
}
