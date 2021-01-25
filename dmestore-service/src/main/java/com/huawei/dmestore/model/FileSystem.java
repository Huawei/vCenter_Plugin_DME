package com.huawei.dmestore.model;

/**
 * FileSystem
 *
 * @author lianq
 * @ClassName: FileSystem
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class FileSystem {
    /**
     * id .
     */
    private String id;
    /**
     * 名称 .
     */
    private String name;
    /**
     * 文件系统类型 .
     */
    private String type;
    /**
     * 状态 .
     */
    private String healthStatus;
    /**
     * 分配策略(分配类型) .
     */
    private String allocType;
    /**
     * 容量使用率 .
     */
    private Integer capacityUsageRatio;
    /**
     * 存储池名字 .
     */
    private String storagePoolName;
    /**
     * nfs .
     */
    private Integer nfsCount;
    /**
     * cifs .
     */
    private Integer cifsCount;
    /**
     * dtree .
     */
    private Integer dtreeCount;
    /**
     * 总容量 .
     */
    private Double capacity;
    /**
     * 文件系统在存储池上已经分配的容量 .
     */
    private Double allocateQuotaInPool;
    /**
     * 可用容量 .
     */
    private Double availableCapacity;
    /**
     * Thin文件系统能缩容的最小空间。单位GB，保留三位小数 .
     */
    private Double minSizeFsCapacity;
    /**
     * 存储设备Id .
     */
    private String storageId;

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
     * setId .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getHealthStatus .
     *
     * @return String .
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
     * getAllocType .
     *
     * @return String .
     */
    public String getAllocType() {
        return allocType;
    }

    /**
     * setAllocType .
     *
     * @param param .
     */
    public void setAllocType(final String param) {
        this.allocType = param;
    }

    /**
     * getCapacityUsageRatio .
     *
     * @return Integer .
     */
    public Integer getCapacityUsageRatio() {
        return capacityUsageRatio;
    }

    /**
     * setCapacityUsageRatio .
     *
     * @param param .
     */
    public void setCapacityUsageRatio(final Integer param) {
        this.capacityUsageRatio = param;
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
     * setId .
     *
     * @param param .
     */
    public void setStoragePoolName(final String param) {
        this.storagePoolName = param;
    }

    /**
     * getNfsCount .
     *
     * @return Integer .
     */
    public Integer getNfsCount() {
        return nfsCount;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setNfsCount(final Integer param) {
        this.nfsCount = param;
    }

    /**
     * getCifsCount .
     *
     * @return Integer .
     */
    public Integer getCifsCount() {
        return cifsCount;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setCifsCount(final Integer param) {
        this.cifsCount = param;
    }

    /**
     * getDtreeCount .
     *
     * @return Integer .
     */
    public Integer getDtreeCount() {
        return dtreeCount;
    }

    /**
     * setDtreeCount .
     *
     * @param param .
     */
    public void setDtreeCount(final Integer param) {
        this.dtreeCount = param;
    }

    /**
     * getCapacity .
     *
     * @return Double .
     */
    public Double getCapacity() {
        return capacity;
    }

    /**
     * setCapacity .
     *
     * @param param .
     */
    public void setCapacity(final Double param) {
        this.capacity = param;
    }

    /**
     * getAllocateQuotaInPool .
     *
     * @return Double .
     */
    public Double getAllocateQuotaInPool() {
        return allocateQuotaInPool;
    }

    /**
     * setAllocateQuotaInPool .
     *
     * @param param .
     */
    public void setAllocateQuotaInPool(final Double param) {
        this.allocateQuotaInPool = param;
    }

    /**
     * getAvailableCapacity .
     *
     * @return Double .
     */
    public Double getAvailableCapacity() {
        return availableCapacity;
    }

    /**
     * setAvailableCapacity .
     *
     * @param param .
     */
    public void setAvailableCapacity(final Double param) {
        this.availableCapacity = param;
    }

    /**
     * getMinSizeFsCapacity .
     *
     * @return Double .
     */
    public Double getMinSizeFsCapacity() {
        return minSizeFsCapacity;
    }

    /**
     * setMinSizeFsCapacity .
     *
     * @param param .
     */
    public void setMinSizeFsCapacity(final Double param) {
        this.minSizeFsCapacity = param;
    }

    /**
     * getStorageId .
     *
     * @return String .
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
     * getType .
     *
     * @return String .
     */
    public String getType() {
        return type;
    }

    /**
     * setType .
     *
     * @param param .
     */
    public void setType(final String param) {
        this.type = param;
    }
}
