package com.huawei.dmestore.model;

/**
 * storage type show tag
 *
 *@ClassName:  StorageTypeShow
 *@author lianq
 *@since  2020-01-07
 */
public class StorageTypeShow {
    /**
     * qos策略 1 支持复选(上限、下限) 2支持单选（上限或下限） 3只显示和支持上限
     */
    private Integer qosTag;

    /**
     * 1 支持应用类型 2不支持应用类型
     */
    private Integer workLoadShow;
    /**
     * 归属控制器 true 支持 false 不支持
     */
    private Boolean ownershipController;

    /**
     * 资源分配类型  1 可选thin/thick 2 可选thin
     */
    private Integer allocationTypeShow;
    /**
     * 重复数据删除 true 支持 false 不支持
     */
    private Boolean deduplicationShow;
    /**
     * 数据压缩 true 支持 false 不支持
     */
    private Boolean compressionShow;

    /**
     * 容量初始分配策略 true 支持 false 不支持
     */
    private Boolean capacityInitialAllocation;

    /**
     * SmartTier策略 true 支持 false 不支持
     */
    private Boolean smartTierShow;

    /**
     * 预取策略 true 支持 false 不支持
     */
    private Boolean prefetchStrategyShow;

    /**
     * 存储详情下展示情况 1 仅展示存储池和lun 2 展示存储池/lun/文件系统/共享/dtree
     */
    private Integer storageDetailTag;

    public Integer getQosTag() {
        return qosTag;
    }

    public void setQosTag(Integer qosTag) {
        this.qosTag = qosTag;
    }

    public Integer getWorkLoadShow() {
        return workLoadShow;
    }

    public void setWorkLoadShow(Integer workLoadShow) {
        this.workLoadShow = workLoadShow;
    }

    public Integer getAllocationTypeShow() {
        return allocationTypeShow;
    }

    public void setAllocationTypeShow(Integer allocationTypeShow) {
        this.allocationTypeShow = allocationTypeShow;
    }

    public Boolean getDeduplicationShow() {
        return deduplicationShow;
    }

    public void setDeduplicationShow(Boolean deduplicationShow) {
        this.deduplicationShow = deduplicationShow;
    }

    public Boolean getCompressionShow() {
        return compressionShow;
    }

    public void setCompressionShow(Boolean compressionShow) {
        this.compressionShow = compressionShow;
    }

    public Boolean getCapacityInitialAllocation() {
        return capacityInitialAllocation;
    }

    public void setCapacityInitialAllocation(Boolean capacityInitialAllocation) {
        this.capacityInitialAllocation = capacityInitialAllocation;
    }

    public Boolean getSmartTierShow() {
        return smartTierShow;
    }

    public void setSmartTierShow(Boolean smartTierShow) {
        this.smartTierShow = smartTierShow;
    }

    public Boolean getPrefetchStrategyShow() {
        return prefetchStrategyShow;
    }

    public void setPrefetchStrategyShow(Boolean prefetchStrategyShow) {
        this.prefetchStrategyShow = prefetchStrategyShow;
    }

    public Integer getStorageDetailTag() {
        return storageDetailTag;
    }

    public void setStorageDetailTag(Integer storageDetailTag) {
        this.storageDetailTag = storageDetailTag;
    }

    public Boolean getOwnershipController() {
        return ownershipController;
    }

    public void setOwnershipController(Boolean ownershipController) {
        this.ownershipController = ownershipController;
    }
}
