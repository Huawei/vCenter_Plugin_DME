package com.huawei.dmestore.model;

/**
 * VmfsDataInfo
 *
 * @ClassName: VmfsDataInfo
 * @Company: GH-CA
 * @author: yy
 * @since 2020-09-02
 **/
public class VmfsDataInfo {
    /**
     * vmware中跳转用唯一id .
     **/
    private String objectid;
    /**
     * 名称 vmware中vmfs的名称.
     **/
    private String name;
    /**
     * dme中状态.
     **/
    private String status;
    /**
     * vmware中总容量 单位GB.
     **/
    private Double capacity;
    /**
     * vmware中空闲容量 单位GB.
     **/
    private Double freeSpace;
    /**
     * vmware中置备容量  capacity+uncommitted-freeSpace 单位GB.
     **/
    private Double reserveCapacity;
    /**
     * dme中存储设备ID.
     **/
    private String deviceId;
    /**
     * dme中存储设备名称.
     **/
    private String device;
    /**
     * dme中服务等级.
     **/
    private String serviceLevelName;
    /**
     * dme中保护状态.
     **/
    private Boolean vmfsProtected;
    /**
     * dme中QoS上限.
     **/
    private Integer maxIops;
    /**
     * dme中QoS下限.
     **/
    private Integer minIops;
    /**
     * dme中iops 性能统计数据.
     **/
    private Float iops;
    /**
     * dme中带宽上限 单位MB/s.
     **/
    private Integer maxBandwidth;
    /**
     * dme中带宽下限 单位MB/s.
     **/
    private Integer minBandwidth;
    /**
     * dme中bandwidth 性能统计数据.
     **/
    private Float bandwidth;
    /**
     * dme中读响应时间 单位ms  性能统计数据.
     **/
    private Float readResponseTime;
    /**
     * dme中写响应时间 单位ms   性能统计数据.
     **/
    private Float writeResponseTime;
    /**
     * dme中时延 单位ms.
     **/
    private Float latency;
    /**
     * dme中卷ID.
     **/
    private String volumeId;
    /**
     * dme中卷名称.
     **/
    private String volumeName;
    /**
     * dme中wwn.
     **/
    private String wwn;

    /**
     * getObjectid .
     *
     * @return String.
     */
    public String getObjectid() {
        return objectid;
    }

    /**
     * setObjectid .
     *
     * @param param .
     */
    public void setObjectid(final String param) {
        this.objectid = param;
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
     * getStatus .
     *
     * @return String.
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
     * @return Double.
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
     * getFreeSpace .
     *
     * @return Double.
     */
    public Double getFreeSpace() {
        return freeSpace;
    }

    /**
     * setFreeSpace .
     *
     * @param param .
     */
    public void setFreeSpace(final Double param) {
        this.freeSpace = param;
    }

    /**
     * getReserveCapacity .
     *
     * @return Double.
     */
    public Double getReserveCapacity() {
        return reserveCapacity;
    }

    /**
     * setReserveCapacity .
     *
     * @param param .
     */
    public void setReserveCapacity(final Double param) {
        this.reserveCapacity = param;
    }

    /**
     * getDeviceId .
     *
     * @return String.
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * setDeviceId .
     *
     * @param param .
     */
    public void setDeviceId(final String param) {
        this.deviceId = param;
    }

    /**
     * getDevice .
     *
     * @return String.
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
     * getObjectid .
     *
     * @return String.
     */
    public Boolean getVmfsProtected() {
        return vmfsProtected;
    }

    /**
     * setVmfsProtected .
     *
     * @param param .
     */
    public void setVmfsProtected(final Boolean param) {
        this.vmfsProtected = param;
    }

    /**
     * getMaxIops .
     *
     * @return Integer.
     */
    public Integer getMaxIops() {
        return maxIops;
    }

    /**
     * setMaxIops .
     *
     * @param param .
     */
    public void setMaxIops(final Integer param) {
        this.maxIops = param;
    }

    /**
     * getMinIops .
     *
     * @return Integer.
     */
    public Integer getMinIops() {
        return minIops;
    }

    /**
     * setMinIops .
     *
     * @param param .
     */
    public void setMinIops(final Integer param) {
        this.minIops = param;
    }

    /**
     * getIops .
     *
     * @return Float.
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
     * getMaxBandwidth .
     *
     * @return Integer.
     */
    public Integer getMaxBandwidth() {
        return maxBandwidth;
    }

    /**
     * setMaxBandwidth .
     *
     * @param param .
     */
    public void setMaxBandwidth(final Integer param) {
        this.maxBandwidth = param;
    }

    /**
     * getMinBandwidth .
     *
     * @return Integer.
     */
    public Integer getMinBandwidth() {
        return minBandwidth;
    }

    /**
     * setMinBandwidth .
     *
     * @param param .
     */
    public void setMinBandwidth(final Integer param) {
        this.minBandwidth = param;
    }

    /**
     * getBandwidth .
     *
     * @return Float.
     */
    public Float getBandwidth() {
        return bandwidth;
    }

    /**
     * setBandwidth .
     *
     * @param param .
     */
    public void setBandwidth(final Float param) {
        this.bandwidth = param;
    }

    /**
     * getReadResponseTime .
     *
     * @return Float.
     */
    public Float getReadResponseTime() {
        return readResponseTime;
    }

    /**
     * setReadResponseTime .
     *
     * @param param .
     */
    public void setReadResponseTime(final Float param) {
        this.readResponseTime = param;
    }

    /**
     * getWriteResponseTime .
     *
     * @return Float.
     */
    public Float getWriteResponseTime() {
        return writeResponseTime;
    }

    /**
     * setWriteResponseTime .
     *
     * @param param .
     */
    public void setWriteResponseTime(final Float param) {
        this.writeResponseTime = param;
    }

    /**
     * getLatency .
     *
     * @return Float.
     */
    public Float getLatency() {
        return latency;
    }

    /**
     * setLatency .
     *
     * @param param .
     */
    public void setLatency(final Float param) {
        this.latency = param;
    }

    /**
     * getVolumeId .
     *
     * @return String.
     */
    public String getVolumeId() {
        return volumeId;
    }

    /**
     * setVolumeId .
     *
     * @param param .
     */
    public void setVolumeId(final String param) {
        this.volumeId = param;
    }

    /**
     * getVolumeName .
     *
     * @return String.
     */
    public String getVolumeName() {
        return volumeName;
    }

    /**
     * setVolumeName .
     *
     * @param param .
     */
    public void setVolumeName(final String param) {
        this.volumeName = param;
    }

    /**
     * getWwn .
     *
     * @return String.
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
}
