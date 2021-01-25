package com.huawei.dmestore.model;


/**
 * StoragePool
 *
 * @author lianq
 * @ClassName: StoragePool
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class StoragePool {
    /**
     * 存储名称.
     **/
    private String storageName;
    /**
     * 空闲容量.
     **/
    private Double freeCapacity;
    /**
     * 名称.
     **/
    private String name;
    /**
     * 存储池id.
     **/
    private String id;
    /**
     * 运行状态.
     **/
    private String runningStatus;
    /**
     * 健康状态.
     **/
    private String healthStatus;
    /**
     * 总容量.
     **/
    private Double totalCapacity;
    /**
     * 已用容量.
     **/
    private Double consumedCapacity;
    /**
     * 已用容量百分比(容量利用率).
     **/
    private String consumedCapacityPercentage;
    /**
     * storagePoolId.
     **/
    private String storagePoolId;
    /**
     * storageInstanceId.
     **/
    private String storageInstanceId;
    /**
     * storageDeviceId.
     **/
    private String storageDeviceId;
    /**
     * 订阅率 = 订阅容量/总容量.
     **/
    private Double subscriptionRate;
    /**
     * 类型（块）file 或者 block.
     **/
    private String mediaType;
    /**
     * 0-无效，1-RAID 10，2-RAID 5，3-RAID 0，
     * 4-RAID 1，5-RAID 6，6-RAID 50，7-RAID 3.
     * RAID级别
     */
    private String tier0RaidLv;
    /**
     * tier1RaidLv .
     */
    private String tier1RaidLv;
    /**
     * tier2RaidLv .
     */
    private String tier2RaidLv;
    /**
     * 存储设备id.
     **/
    private String storageId;
    /**
     * 存储池上创建LUN或者文件系统时的可用容量 单位MB.
     **/
    private Double dataSpace;
    /**
     * 时延 iops 带宽 公用存储设备的.
     * 订阅容量
     **/
    private Double subscribedCapacity;
    /**
     * 去重容量.
     **/
    private Double dedupedCapacity;
    /**
     * 压缩容量.
     **/
    private Double compressedCapacity;
    /**
     * 保护容量.
     **/
    private Double protectionCapacity;
    /**
     * 硬盘类型.
     **/
    private String physicalType;
    /**
     * 存储池所处硬盘id.
     **/
    private String diskPoolId;

    /**
     * 存储池所在存储中的ID.
     **/
    private String poolId;
    /**
     * 服务等级.
     **/
    private String serviceLevelName;

    /**
     * iops.
     **/
    private Float maxIops;

    /**
     * 带宽.
     **/
    private Float maxBandwidth;

    /**
     * 时延.
     **/
    private Float maxLatency;

    /**
     * RAID级别--转化后的值.
     **/
    private String raidLevel;

    /**
     * getStorageName .
     *
     * @return String.
     */
    public String getStorageName() {
        return storageName;
    }

    /**
     * setStorageName
     *
     * @param param
     */
    public void setStorageName(final String param) {
        this.storageName = param;
    }

    /**
     * getFreeCapacity .
     *
     * @return Double.
     */
    public Double getFreeCapacity() {
        return freeCapacity;
    }

    /**
     * setFreeCapacity .
     *
     * @param param .
     */
    public void setFreeCapacity(final Double param) {
        this.freeCapacity = param;
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
     * getId .
     *
     * @return String.
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
     * getRunningStatus .
     *
     * @return String.
     */
    public String getRunningStatus() {
        return runningStatus;
    }

    /**
     * setRunningStatus .
     *
     * @param param .
     */
    public void setRunningStatus(final String param) {
        this.runningStatus = param;
    }

    /**
     * getHealthStatus .
     *
     * @return String.
     */
    public String getHealthStatus() {
        return healthStatus;
    }

    /**
     * setHealthStatus .
     *
     * @param param .
     */
    public void setHealthStatus(final String param) {
        this.healthStatus = param;
    }

    /**
     * getTotalCapacity .
     *
     * @return Double.
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
     * getConsumedCapacity .
     *
     * @return Double.
     */
    public Double getConsumedCapacity() {
        return consumedCapacity;
    }

    /**
     * setConsumedCapacity .
     *
     * @param param .
     */
    public void setConsumedCapacity(final Double param) {
        this.consumedCapacity = param;
    }

    /**
     * getConsumedCapacityPercentage .
     *
     * @return String.
     */
    public String getConsumedCapacityPercentage() {
        return consumedCapacityPercentage;
    }

    /**
     * setConsumedCapacityPercentage .
     *
     * @param param .
     */
    public void setConsumedCapacityPercentage(final String param) {
        this.consumedCapacityPercentage = param;
    }

    /**
     * getStoragePoolId .
     *
     * @return String.
     */
    public String getStoragePoolId() {
        return storagePoolId;
    }

    /**
     * setStoragePoolId .
     *
     * @param param .
     */
    public void setStoragePoolId(final String param) {
        this.storagePoolId = param;
    }

    /**
     * getStorageInstanceId .
     *
     * @return String.
     */
    public String getStorageInstanceId() {
        return storageInstanceId;
    }

    /**
     * setStorageInstanceId .
     *
     * @param param .
     */
    public void setStorageInstanceId(final String param) {
        this.storageInstanceId = param;
    }

    /**
     * getStorageDeviceId .
     *
     * @return String.
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

    /**
     * getSubscriptionRate .
     *
     * @return String.
     */
    public Double getSubscriptionRate() {
        return subscriptionRate;
    }

    /**
     * setSubscriptionRate .
     *
     * @param param .
     */
    public void setSubscriptionRate(final Double param) {
        this.subscriptionRate = param;
    }

    /**
     * getMediaType .
     *
     * @return String.
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * setMediaType .
     *
     * @param param .
     */
    public void setMediaType(final String param) {
        this.mediaType = param;
    }

    /**
     * getTier0RaidLv .
     *
     * @return String.
     */
    public String getTier0RaidLv() {
        return tier0RaidLv;
    }

    /**
     * setTier0RaidLv .
     *
     * @param param .
     */
    public void setTier0RaidLv(final String param) {
        this.tier0RaidLv = param;
    }

    /**
     * getTier1RaidLv .
     *
     * @return String.
     */
    public String getTier1RaidLv() {
        return tier1RaidLv;
    }

    /**
     * setTier1RaidLv .
     *
     * @param param .
     */
    public void setTier1RaidLv(final String param) {
        this.tier1RaidLv = param;
    }

    /**
     * getTier2RaidLv .
     *
     * @return String.
     */
    public String getTier2RaidLv() {
        return tier2RaidLv;
    }

    /**
     * setTier2RaidLv .
     *
     * @param param .
     */
    public void setTier2RaidLv(final String param) {
        this.tier2RaidLv = param;
    }

    /**
     * getStorageId .
     *
     * @return String.
     */
    public String getStorageId() {
        return storageId;
    }

    /**
     * setStorageId .
     *
     * @param param .
     */
    public void setStorageId(final String param) {
        this.storageId = param;
    }

    /**
     * getDataSpace .
     *
     * @return Double.
     */
    public Double getDataSpace() {
        return dataSpace;
    }

    /**
     * setDataSpace .
     *
     * @param param .
     */
    public void setDataSpace(final Double param) {
        this.dataSpace = param;
    }

    /**
     * getSubscribedCapacity .
     *
     * @return Double.
     */
    public Double getSubscribedCapacity() {
        return subscribedCapacity;
    }

    /**
     * setSubscribedCapacity .
     *
     * @param param .
     */
    public void setSubscribedCapacity(final Double param) {
        this.subscribedCapacity = param;
    }

    /**
     * getPhysicalType .
     *
     * @return String.
     */
    public String getPhysicalType() {
        return physicalType;
    }

    /**
     * setPhysicalType .
     *
     * @param param .
     */
    public void setPhysicalType(final String param) {
        this.physicalType = param;
    }

    /**
     * getDiskPoolId .
     *
     * @return String.
     */
    public String getDiskPoolId() {
        return diskPoolId;
    }

    /**
     * setDiskPoolId .
     *
     * @param param .
     */
    public void setDiskPoolId(final String param) {
        this.diskPoolId = param;
    }

    /**
     * setPoolId .
     *
     * @return String.
     */
    public String getPoolId() {
        return poolId;
    }

    /**
     * setPoolId .
     *
     * @param param .
     */
    public void setPoolId(final String param) {
        this.poolId = param;
    }

    /**
     * getServiceLevelName .
     *
     * @return String.
     */
    public String getServiceLevelName() {
        return serviceLevelName;
    }

    /**
     * setServiceLevelName .
     *
     * @param param .
     */
    public void setServiceLevelName(final String param) {
        this.serviceLevelName = param;
    }

    /**
     * getMaxIops .
     *
     * @return Float.
     */
    public Float getMaxIops() {
        return maxIops;
    }

    /**
     * setMaxIops .
     *
     * @param param .
     */
    public void setMaxIops(final Float param) {
        this.maxIops = param;
    }

    /**
     * getMaxBandwidth .
     *
     * @return Float.
     */
    public Float getMaxBandwidth() {
        return maxBandwidth;
    }

    /**
     * setMaxBandwidth .
     *
     * @param param .
     */
    public void setMaxBandwidth(final Float param) {
        this.maxBandwidth = param;
    }

    /**
     * getMaxLatency .
     *
     * @return Float.
     */
    public Float getMaxLatency() {
        return maxLatency;
    }

    /**
     * setMaxLatency .
     *
     * @param param .
     */
    public void setMaxLatency(final Float param) {
        this.maxLatency = param;
    }

    /**
     * getDedupedCapacity .
     *
     * @return Double.
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
     * @return Double.
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
     * getProtectionCapacity .
     *
     * @return Double.
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

    public String getRaidLevel() {
        return raidLevel;
    }

    public void setRaidLevel(String raidLevel) {
        this.raidLevel = raidLevel;
    }
}
