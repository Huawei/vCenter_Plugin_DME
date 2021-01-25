package com.huawei.dmestore.model;

/**
 * NfsShares
 *
 * @author lianq
 * @ClassName: NfsShare
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class NfsShares {
    /**
     * 名称 .
     */
    private String name;
    /**
     * 共享路径 .
     */
    private String sharePath;
    /**
     * 存储设备id .
     */
    private String storageId;
    /**
     * 服务等级 .
     */
    private String tierName;
    /**
     * 所属dtree .
     */
    private String owningDtreeName;
    /**
     * 所属文件系统名字在 .
     */
    private String fsName;
    /**
     * 所属dtreeid .
     */
    private String owningDtreeId;

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
     * getSharePath .
     *
     * @return String .
     */
    public String getSharePath() {
        return sharePath;
    }

    /**
     * setSharePath .
     *
     * @param param .
     */
    public void setSharePath(final String param) {
        this.sharePath = param;
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
     * getTierName .
     *
     * @return String .
     */
    public String getTierName() {
        return tierName;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setTierName(final String param) {
        this.tierName = param;
    }

    /**
     * getOwningDtreeName .
     *
     * @return String .
     */
    public String getOwningDtreeName() {
        return owningDtreeName;
    }

    /**
     * setOwningDtreeName .
     *
     * @param param .
     */
    public void setOwningDtreeName(final String param) {
        this.owningDtreeName = param;
    }

    /**
     * getFsName .
     *
     * @return String .
     */
    public String getFsName() {
        return fsName;
    }

    /**
     * setFsName .
     *
     * @param param .
     */
    public void setFsName(final String param) {
        this.fsName = param;
    }

    /**
     * getOwningDtreeId .
     *
     * @return String .
     */
    public String getOwningDtreeId() {
        return owningDtreeId;
    }

    /**
     * setOwningDtreeId .
     *
     * @param param .
     */
    public void setOwningDtreeId(final String param) {
        this.owningDtreeId = param;
    }

    @Override
    public final String toString() {
        return "NfsShares{"
            + "name='" + name + '\''
            + ", sharePath='" + sharePath + '\''
            + ", storageId='" + storageId + '\''
            + ", tierName='" + tierName + '\''
            + ", owningDtreeName='" + owningDtreeName + '\''
            + ", fsName='" + fsName + '\''
            + ", owningDtreeId='" + owningDtreeId + '\''
            + '}';
    }
}
