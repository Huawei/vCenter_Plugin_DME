package com.huawei.dmestore.model;

import java.util.List;

/**
 * StorageDisk
 *
 * @author lianq
 * @ClassName: StorageDisk
 * @since 2020-12-08
 */
public class StorageDisk {
    /**
     * name .
     */
    private String name;
    /**
     * status .
     */
    private String status;
    /**
     * capacity .
     */
    private Double capacity;
    /**
     * poolId .
     */
    private String poolId;
    /**
     * id .
     */
    private String id;
    /**
     * storageDeviceId .
     */
    private String storageDeviceId;
    /**
     * iops .
     */
    private Float iops;
    /**
     * lantency .
     */
    private Float lantency;
    /**
     * bandwith .
     */
    private Float bandwith;
    /**
     * useage .
     */
    private Float useage;
    /**
     * 硬盘域 .
     */
    private List<DiskPool> diskPools;
    /**
     * 类型.
     */
    private String physicalType;

    /**
     * 角色.
     */
    private String logicalType;

    /**
     * 磁盘域.
     */
    private String diskDomain;
    /**
     * 转速.
     */
    private Long speed;
    /**
     * 性能.
     */
    private String performance;

    /**
     * getDiskPools .
     *
     * @return List<> .
     */
    public List<DiskPool> getDiskPools() {
        return diskPools;
    }

    /**
     * setDiskPools .
     *
     * @param param .
     */
    public void setDiskPools(final List<DiskPool> param) {
        this.diskPools = param;
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
     * setPoolId .
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

    /**
     * getLogicalType .
     *
     * @return String .
     */
    public String getLogicalType() {
        return logicalType;
    }

    /**
     * setLogicalType .
     *
     * @param param .
     */
    public void setLogicalType(final String param) {
        this.logicalType = param;
    }

    /**
     * getDiskDomain .
     *
     * @return String .
     */
    public String getDiskDomain() {
        return diskDomain;
    }

    /**
     * setDiskDomain .
     *
     * @param param .
     */
    public void setDiskDomain(final String param) {
        this.diskDomain = param;
    }

    /**
     * getSpeed .
     *
     * @return String .
     */
    public Long getSpeed() {
        return speed;
    }

    /**
     * setSpeed .
     *
     * @param param .
     */
    public void setSpeed(final Long param) {
        this.speed = param;
    }

    /**
     * getPerformance .
     *
     * @return String .
     */
    public String getPerformance() {
        return performance;
    }

    /**
     * setPerformance .
     *
     * @param param .
     */
    public void setPerformance(final String param) {
        this.performance = param;
    }

    /**
     * getPhysicalType .
     *
     * @return String .
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
     * getIops .
     *
     * @return Float .
     */
    public Float getIops() {
        return iops;
    }

    /**
     * setIops .
     *
     * @param param .
     */
    public void setIops(final Float param) {
        this.iops = param;
    }

    /**
     * getLantency .
     *
     * @return Float .
     */
    public Float getLantency() {
        return lantency;
    }

    /**
     * setLantency .
     *
     * @param param .
     */
    public void setLantency(final Float param) {
        this.lantency = param;
    }

    /**
     * getBandwith .
     *
     * @return Float .
     */
    public Float getBandwith() {
        return bandwith;
    }

    /**
     * setBandwith .
     *
     * @param param .
     */
    public void setBandwith(final Float param) {
        this.bandwith = param;
    }

    /**
     * getUseage .
     *
     * @return Float .
     */
    public Float getUseage() {
        return useage;
    }

    /**
     * setUseage .
     *
     * @param param .
     */
    public void setUseage(final Float param) {
        this.useage = param;
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
}
