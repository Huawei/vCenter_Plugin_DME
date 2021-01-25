package com.huawei.dmestore.model;

/**
 * Storage
 *
 * @author lianq
 * @ClassName: Storage
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class Storage {
    /**
     * 存储设备ID.
     **/
    private String id;
    /**
     * 名称.
     **/
    private String name;
    /**
     * ip地址.
     **/
    private String ip;
    /**
     * 状态.
     **/
    private String status;
    /**
     * 状态.
     **/
    private String synStatus;
    /**
     * 厂商.
     **/
    private String vendor;
    /**
     * 型号.
     **/
    private String model;
    /**
     * 已用容量.
     **/
    private Double usedCapacity;
    /**
     * 裸容量.
     **/
    private Double totalCapacity;
    /**
     * totalEffectiveCapacity.
     **/
    private Double totalEffectiveCapacity;
    /**
     * 空闲容量.
     **/
    private Double freeEffectiveCapacity;
    /**
     * CPU利用率.
     **/
    private Double maxCpuUtilization;
    /**
     * iops.
     **/
    private Double maxIops;
    /**
     * 带宽.
     **/
    private Double maxBandwidth;
    /**
     * 时延.
     **/
    private Double maxLatency;
    /**
     * 最大ops.
     **/
    private Double maxOps;
    /**
     * 可用分区.
     **/
    private String[] azIds;
    /**
     * 设备序列号.
     **/
    private String sn;
    /**
     * 版本.
     **/
    private String version;
    /**
     * 最大ops.
     **/
    private String productVersion;
    /**
     * 总容量.
     **/
    private Double totalPoolCapacity;
    /**
     * 订阅容量.
     **/
    private Double subscriptionCapacity;
    /**
     * 最大ops.
     **/
    private Double capacityUtilization;
    /**
     * 最大ops.
     **/
    private Double subscriptionRate;
    /**
     * 位置 .
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
     * getCapacityUtilization .
     *
     * @return Double .
     */
    public Double getCapacityUtilization() {
        return capacityUtilization;
    }

    /**
     * setCapacityUtilization .
     *
     * @param param .
     */
    public void setCapacityUtilization(final Double param) {
        this.capacityUtilization = param;
    }

    /**
     * getVersion .
     *
     * @return String .
     */
    public String getVersion() {
        return version;
    }

    /**
     * setVersion .
     *
     * @param param .
     */
    public void setVersion(final String param) {
        this.version = param;
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
     * getMaxCpuUtilization .
     *
     * @return Double .
     */
    public Double getMaxCpuUtilization() {
        return maxCpuUtilization;
    }

    /**
     * setMaxCpuUtilization .
     *
     * @param param .
     */
    public void setMaxCpuUtilization(final Double param) {
        this.maxCpuUtilization = param;
    }

    /**
     * getMaxIops .
     *
     * @return Double .
     */
    public Double getMaxIops() {
        return maxIops;
    }

    /**
     * setMaxIops .
     *
     * @param param .
     */
    public void setMaxIops(final Double param) {
        this.maxIops = param;
    }

    /**
     * getMaxBandwidth .
     *
     * @return Double .
     */
    public Double getMaxBandwidth() {
        return maxBandwidth;
    }

    /**
     * setMaxBandwidth .
     *
     * @param param .
     */
    public void setMaxBandwidth(final Double param) {
        this.maxBandwidth = param;
    }

    /**
     * getMaxLatency .
     *
     * @return Double .
     */
    public Double getMaxLatency() {
        return maxLatency;
    }

    /**
     * setMaxLatency .
     *
     * @param param .
     */
    public void setMaxLatency(final Double param) {
        this.maxLatency = param;
    }

    /**
     * getAzIds .
     *
     * @return Object .
     */
    public Object getAzIds() {
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
     * getTotalPoolCapacity .
     *
     * @return Double .
     */
    public Double getTotalPoolCapacity() {
        return totalPoolCapacity;
    }

    /**
     * setTotalPoolCapacity .
     *
     * @param param .
     */
    public void setTotalPoolCapacity(final Double param) {
        this.totalPoolCapacity = param;
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
     * getSubscriptionRate .
     *
     * @return Double .
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
     * getMaxOps .
     *
     * @return Double .
     */
    public Double getMaxOps() {
        return maxOps;
    }

    /**
     * setMaxOps .
     *
     * @param param .
     */
    public void setMaxOps(final Double param) {
        this.maxOps = param;
    }
}
