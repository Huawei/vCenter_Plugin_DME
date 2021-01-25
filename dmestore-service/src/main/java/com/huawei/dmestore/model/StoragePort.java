package com.huawei.dmestore.model;

/**
 * StoragePort
 *
 * @author lianq
 * @ClassName: Port
 * @Description: 补充端口类型得列表数据
 * @since 2020-12-08
 */
public class StoragePort {
    /**
     * id .
     */
    private String id;
    /**
     * 原始id .
     */
    private String nativeId;
    /**
     * 最后一次修改时间 .
     */
    private Long last_Modified;
    /**
     * 最后监控时间 .
     */
    private Long lastMonitorTime;
    /**
     * 监控状态 .
     */
    private String dataStatus;
    /**
     * 名称 .
     */
    private String name;
    /**
     * 端口id .
     */
    private String portId;
    /**
     * 端口名称: P2 .
     */
    private String portName;
    /**
     * 位置: CTE0.B0.P2 .
     */
    private String location;
    /**
     * 连接状态 .
     */
    private String connectStatus;
    /**
     * 状态 .
     */
    private String status;
    /**
     * 端口类型 .
     */
    private String portType;
    /**
     * MAC地址 .
     */
    private String mac;
    /**
     * IPv4地址 .
     */
    private String mgmtIp;
    /**
     * IPv4掩码 .
     */
    private String ipv4Mask;
    /**
     * IPv6地址 .
     */
    private String mgmtIpv6;
    /**
     * IPv6掩码 .
     */
    private String ipv6Mask;
    /**
     * iSCSI名称 .
     */
    private String iscsiName;
    /**
     * 绑定ID .
     */
    private String bondId;
    /**
     * 绑定名称 .
     */
    private String bondName;
    /**
     * wwn .
     */
    private String wwn;
    /**
     * 光模块状态 .
     */
    private String sfpStatus;
    /**
     * 逻辑类型 .
     */
    private String logicalType;
    /**
     * 启动器个数 .
     */
    private Integer numOfInitiators;
    /**
     * 端口速率 .
     */
    private Integer speed;
    /**
     * 端口最大速率 .
     */
    private Integer maxSpeed;
    /**
     * 存储设备ID .
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
     * getNativeId .
     *
     * @return String .
     */
    public String getNativeId() {
        return nativeId;
    }

    /**
     * setNativeId .
     *
     * @param param .
     */
    public void setNativeId(final String param) {
        this.nativeId = param;
    }

    /**
     * getLast_Modified .
     *
     * @return Long .
     */
    public Long getLast_Modified() {
        return last_Modified;
    }

    /**
     * setLast_Modified .
     *
     * @param param .
     */
    public void setLast_Modified(final Long param) {
        this.last_Modified = param;
    }

    /**
     * getLastMonitorTime .
     *
     * @return Long .
     */
    public Long getLastMonitorTime() {
        return lastMonitorTime;
    }

    /**
     * setLastMonitorTime .
     *
     * @param param .
     */
    public void setLastMonitorTime(final Long param) {
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
     * setDataStatus .
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
     * setName .
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
    }

    /**
     * getPortId .
     *
     * @return String .
     */
    public String getPortId() {
        return portId;
    }

    /**
     * setPortId .
     *
     * @param param .
     */
    public void setPortId(final String param) {
        this.portId = param;
    }

    /**
     * getPortName .
     *
     * @return String .
     */
    public String getPortName() {
        return portName;
    }

    /**
     * setPortName .
     *
     * @param param .
     */
    public void setPortName(final String param) {
        this.portName = param;
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
     * getConnectStatus .
     *
     * @return String .
     */
    public String getConnectStatus() {
        return connectStatus;
    }

    /**
     * setConnectStatus .
     *
     * @param param .
     */
    public void setConnectStatus(final String param) {
        this.connectStatus = param;
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
     * getPortType .
     *
     * @return String .
     */
    public String getPortType() {
        return portType;
    }

    /**
     * setPortType .
     *
     * @param param .
     */
    public void setPortType(final String param) {
        this.portType = param;
    }

    /**
     * getMac .
     *
     * @return String .
     */
    public String getMac() {
        return mac;
    }

    /**
     * setMac .
     *
     * @param param .
     */
    public void setMac(final String param) {
        this.mac = param;
    }

    /**
     * getMgmtIp .
     *
     * @return String .
     */
    public String getMgmtIp() {
        return mgmtIp;
    }

    /**
     * setMgmtIp .
     *
     * @param param .
     */
    public void setMgmtIp(final String param) {
        this.mgmtIp = param;
    }

    /**
     * getIpv4Mask .
     *
     * @return String .
     */
    public String getIpv4Mask() {
        return ipv4Mask;
    }

    /**
     * setIpv4Mask .
     *
     * @param param .
     */
    public void setIpv4Mask(final String param) {
        this.ipv4Mask = param;
    }

    /**
     * getMgmtIpv6 .
     *
     * @return String .
     */
    public String getMgmtIpv6() {
        return mgmtIpv6;
    }

    /**
     * setMgmtIpv6 .
     *
     * @param param .
     */
    public void setMgmtIpv6(final String param) {
        this.mgmtIpv6 = param;
    }

    /**
     * getIpv6Mask .
     *
     * @return String .
     */
    public String getIpv6Mask() {
        return ipv6Mask;
    }

    /**
     * setIpv6Mask .
     *
     * @param param .
     */
    public void setIpv6Mask(final String param) {
        this.ipv6Mask = param;
    }

    /**
     * getIscsiName .
     *
     * @return String .
     */
    public String getIscsiName() {
        return iscsiName;
    }

    /**
     * setIscsiName .
     *
     * @param param .
     */
    public void setIscsiName(final String param) {
        this.iscsiName = param;
    }

    /**
     * getBondId .
     *
     * @return String .
     */
    public String getBondId() {
        return bondId;
    }

    /**
     * setBondId .
     *
     * @param param .
     */
    public void setBondId(final String param) {
        this.bondId = param;
    }

    /**
     * getBondName .
     *
     * @return String .
     */
    public String getBondName() {
        return bondName;
    }

    /**
     * setBondName .
     *
     * @param param .
     */
    public void setBondName(final String param) {
        this.bondName = param;
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
     * getSfpStatus .
     *
     * @return String .
     */
    public String getSfpStatus() {
        return sfpStatus;
    }

    /**
     * setSfpStatus .
     *
     * @param param .
     */
    public void setSfpStatus(final String param) {
        this.sfpStatus = param;
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
     * getNumOfInitiators .
     *
     * @return Integer .
     */
    public Integer getNumOfInitiators() {
        return numOfInitiators;
    }

    /**
     * setNumOfInitiators .
     *
     * @param param .
     */
    public void setNumOfInitiators(final Integer param) {
        this.numOfInitiators = param;
    }

    /**
     * getSpeed .
     *
     * @return Integer .
     */
    public Integer getSpeed() {
        return speed;
    }

    /**
     * setSpeed .
     *
     * @param param .
     */
    public void setSpeed(final Integer param) {
        this.speed = param;
    }

    /**
     * getMaxSpeed .
     *
     * @return Integer .
     */
    public Integer getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * setMaxSpeed .
     *
     * @param param .
     */
    public void setMaxSpeed(final Integer param) {
        this.maxSpeed = param;
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
}
