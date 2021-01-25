package com.huawei.dmestore.model;

/**
 * NfsDataInfo
 *
 * @ClassName: NfsDataInfo
 * @Company: GH-CA
 * @author: yy
 * @since 2020-12-08
 **/
public final class NfsDataInfo {
    /**
     * 跳转用唯一id.
     **/
    private String objectid;
    /**
     * 名称.
     **/
    private String name;
    /**
     * 状态.
     **/
    private String status;
    /**
     * 总容量 单位GB.
     **/
    private Double capacity;
    /**
     * 空闲容量 单位GB.
     **/
    private Double freeSpace;
    /**
     * 置备容量  capacity+uncommitted-freeSpace 单位GB.
     **/
    private Double reserveCapacity;
    /**
     * 存储设备ID.
     **/
    private String deviceId;
    /**
     * 存储设备名称.
     **/
    private String device;
    /**
     * 逻辑端口.
     **/
    private String logicPort;
    /**
     * 逻辑端口 id.
     **/
    private String logicPortId;
    /**
     * share ip.
     **/
    private String shareIp;
    /**
     * share path.
     **/
    private String sharePath;
    /**
     * share 名称.
     **/
    private String share;
    /**
     * share id.
     **/
    private String shareId;
    /**
     * fs.
     **/
    private String fs;
    /**
     * fs id.
     **/
    private String fsId;
    /**
     * OPS.
     **/
    private Float ops;
    /**
     * 带宽 单位MB/s.
     **/
    private Float bandwidth;
    /**
     * 读响应时间 单位ms.
     **/
    private Float readResponseTime;
    /**
     * 写响应时间 单位ms.
     **/
    private Float writeResponseTime;

    /**
     * getObjectid.
     *
     * @return String.
     */
    public String getObjectid() {
        return objectid;
    }

    /**
     * setObjectid .
     *
     * @param objectId .
     */
    public void setObjectid(final String objectId) {
        this.objectid = objectId;
    }

    /**
     * getName.
     *
     * @return String.
     */
    public String getName() {
        return name;
    }

    /**
     * setName.
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getStatus.
     *
     * @return String.
     */
    public String getStatus() {
        return status;
    }

    /**
     * setStatus.
     *
     * @param param .
     */
    public void setStatus(final String param) {
        this.status = param;
    }

    /**
     * getCapacity.
     *
     * @return Double .
     */
    public Double getCapacity() {
        return capacity;
    }

    /**
     * setCapacity.
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
     * setFreeSpace.
     *
     * @param param .
     */
    public void setFreeSpace(final Double param) {
        this.freeSpace = param;
    }

    /**
     * getReserveCapacity.
     *
     * @return Double .
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
     * getDeviceId.
     *
     * @return String.
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     * setDeviceId.
     *
     * @param param .
     */
    public void setDeviceId(final String param) {
        this.deviceId = param;
    }

    /**
     * getDevice.
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
     * getLogicPort .
     *
     * @return .
     */
    public String getLogicPort() {
        return logicPort;
    }

    /**
     * setLogicPort .
     *
     * @param param .
     */
    public void setLogicPort(final String param) {
        this.logicPort = param;
    }

    /**
     * getLogicPortId .
     *
     * @return String .
     */
    public String getLogicPortId() {
        return logicPortId;
    }

    /**
     * setLogicPortId .
     *
     * @param param .
     */
    public void setLogicPortId(final String param) {
        this.logicPortId = param;
    }

    /**
     * getShareIp .
     *
     * @return String .
     */
    public String getShareIp() {
        return shareIp;
    }

    /**
     * setShareIp .
     *
     * @param param .
     */
    public void setShareIp(final String param) {
        this.shareIp = param;
    }

    /**
     * getSharePath .
     *
     * @return String .
     */
    public String getSharePath() {
        return sharePath;
    }

    /**
     * setSharePath.
     *
     * @param param .
     */
    public void setSharePath(final String param) {
        this.sharePath = param;
    }

    /**
     * getShare.
     *
     * @return String .
     */
    public String getShare() {
        return share;
    }

    /**
     * setShare .
     *
     * @param param .
     */
    public void setShare(final String param) {
        this.share = param;
    }

    /**
     * getShareId .
     *
     * @return String .
     */
    public String getShareId() {
        return shareId;
    }

    /**
     * setShareId .
     *
     * @param param .
     */
    public void setShareId(final String param) {
        this.shareId = param;
    }

    /**
     * getFs.
     *
     * @return .
     */
    public String getFs() {
        return fs;
    }

    /**
     * setFs .
     *
     * @param param .
     */
    public void setFs(final String param) {
        this.fs = param;
    }

    /**
     * getFsId.
     *
     * @return .
     */
    public String getFsId() {
        return fsId;
    }

    /**
     * setFsId .
     *
     * @param param .
     */
    public void setFsId(final String param) {
        this.fsId = param;
    }

    /**
     * getOps .
     *
     * @return , .
     */
    public Float getOps() {
        return ops;
    }

    /**
     * setOps .
     *
     * @param param 。
     */
    public void setOps(final Float param) {
        this.ops = param;
    }

    /**
     * getBandwidth .
     *
     * @return .
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
     * @return .
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
     * @return .
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
}
