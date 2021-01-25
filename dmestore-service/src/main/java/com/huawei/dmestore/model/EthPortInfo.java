package com.huawei.dmestore.model;

/**
 * EthPortInfo
 *
 * @author yy
 * @Description: EthPortInfo
 * @since 2020-09-03
 */
public class EthPortInfo {
    /**
     * DME Storage 1.0.RC1 系统资源北向模型 01.pdf 存储端口.
     **/
    private String ownerType;

    /**
     * IPv4掩码 .
     **/
    private String ipv4Mask;

    /**
     * logicalType .
     */
    private String logicalType;

    /**
     * storageDeviceId .
     */
    private String storageDeviceId;

    /**
     * 端口名称 .
     **/
    private String portName;

    /**
     * ownerId .
     */
    private String ownerId;

    /**
     * 端口ID .
     **/
    private String portId;

    /**
     * 绑定名称.
     **/
    private String bondName;

    /**
     * MAC地址.
     **/
    private String mac;

    /**
     * IPv6地址.
     **/
    private String mgmtIpv6;

    /**
     * iSCSI名称.
     **/
    private String iscsiName;

    /**
     * ownerName.
     */
    private String ownerName;

    /**
     * 最后监控时间.
     **/
    private Long lastMonitorTime;

    /**
     * IPv4地址.
     **/
    private String mgmtIp;

    /**
     * confirmStatus .
     */
    private String confirmStatus;

    /**
     * CMDB 实例 ID.
     **/
    private String id;

    /**
     * 最后修改时间.
     **/
    private Long lastModified;

    /**
     * 连接状态.
     **/
    private String connectStatus;

    /**
     * classId.
     */
    private Integer classId;

    /**
     * 监控状态.
     **/
    private String dataStatus;

    /**
     * Mbit/s.
     **/
    private Integer maxSpeed;

    /**
     * resId.
     */
    private String resId;

    /**
     * local.
     */
    private Boolean local;

    /**
     * 端口类型.
     **/
    private String portType;

    /**
     * className .
     */
    private String className;

    /**
     * numberOfInitiators .
     */
    private Integer numberOfInitiators;

    /**
     * 绑定ID.
     **/
    private String bondId;

    /**
     * regionId .
     */
    private String regionId;

    /**
     * 名称.
     **/
    private String name;

    /**
     * 位置.
     **/
    private String location;

    /**
     * 原始ID.
     **/
    private String nativeId;

    /**
     * dataSource .
     */
    private String dataSource;

    /**
     * IPv6掩码.
     **/
    private String ipv6Mask;

    /**
     * 状态.
     **/
    private String status;

    /**
     * Mbit/s.
     **/
    private Integer speed;

    /**
     * WWN .
     **/
    private String wwn;

    /**
     * 光模块状态.
     **/
    private String sfpStatus;

    /**
     * getOwnerType .
     *
     * @return String .
     */
    public String getOwnerType() {
        return ownerType;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setOwnerType(final String param) {
        this.ownerType = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setIpv4Mask(final String param) {
        this.ipv4Mask = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setLogicalType(final String param) {
        this.logicalType = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setStorageDeviceId(final String param) {
        this.storageDeviceId = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setPortName(final String param) {
        this.portName = param;
    }

    /**
     * getOwnerId .
     *
     * @return String .
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setOwnerId(final String param) {
        this.ownerId = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setPortId(final String param) {
        this.portId = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setBondName(final String param) {
        this.bondName = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setMac(final String param) {
        this.mac = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setMgmtIpv6(final String param) {
        this.mgmtIpv6 = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setIscsiName(final String param) {
        this.iscsiName = param;
    }

    /**
     * getOwnerName .
     *
     * @return String .
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setOwnerName(final String param) {
        this.ownerName = param;
    }

    /**
     * getLastMonitorTime .
     *
     * @return String .
     */
    public Long getLastMonitorTime() {
        return lastMonitorTime;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setLastMonitorTime(final Long param) {
        this.lastMonitorTime = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setMgmtIp(final String param) {
        this.mgmtIp = param;
    }

    /**
     * getConfirmStatus .
     *
     * @return String .
     */
    public String getConfirmStatus() {
        return confirmStatus;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setConfirmStatus(final String param) {
        this.confirmStatus = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setId(final String param) {
        this.id = param;
    }

    /**
     * getLastModified .
     *
     * @return String .
     */
    public Long getLastModified() {
        return lastModified;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setLastModified(final Long param) {
        this.lastModified = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setConnectStatus(final String param) {
        this.connectStatus = param;
    }

    /**
     * getClassId .
     *
     * @return Integer .
     */
    public Integer getClassId() {
        return classId;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setClassId(final Integer param) {
        this.classId = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setDataStatus(final String param) {
        this.dataStatus = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setMaxSpeed(final Integer param) {
        this.maxSpeed = param;
    }

    /**
     * getResId .
     *
     * @return String .
     */
    public String getResId() {
        return resId;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setResId(final String param) {
        this.resId = param;
    }

    /**
     * getLocal .
     *
     * @return Boolean .
     */
    public Boolean getLocal() {
        return local;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setLocal(final Boolean param) {
        this.local = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setPortType(final String param) {
        this.portType = param;
    }

    /**
     * getClassName .
     *
     * @return String .
     */
    public String getClassName() {
        return className;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setClassName(final String param) {
        this.className = param;
    }

    /**
     * getNumberOfInitiators .
     *
     * @return Integer .
     */
    public Integer getNumberOfInitiators() {
        return numberOfInitiators;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setNumberOfInitiators(final Integer param) {
        this.numberOfInitiators = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setBondId(final String param) {
        this.bondId = param;
    }

    /**
     * getRegionId .
     *
     * @return String .
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setRegionId(final String param) {
        this.regionId = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setName(final String param) {
        this.name = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setLocation(final String param) {
        this.location = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setNativeId(final String param) {
        this.nativeId = param;
    }

    /**
     * getDataSource .
     *
     * @return String .
     */
    public String getDataSource() {
        return dataSource;
    }

    /**
     * setOwnerType.
     *
     * @param param .
     */
    public void setDataSource(final String param) {
        this.dataSource = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setIpv6Mask(final String param) {
        this.ipv6Mask = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setStatus(final String param) {
        this.status = param;
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setSpeed(final Integer param) {
        this.speed = param;
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
     * setOwnerType.
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
     * setOwnerType.
     *
     * @param param .
     */
    public void setSfpStatus(final String param) {
        this.sfpStatus = param;
    }
}
