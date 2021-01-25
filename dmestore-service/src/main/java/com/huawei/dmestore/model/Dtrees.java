package com.huawei.dmestore.model;

/**
 * Dtrees
 *
 * @author lianq
 * @Description: Dtrees
 * @since 2020-12-08
 */
public class Dtrees {
    /**
     * name .
     */
    private String name;

    /**
     * 所属文件系统名称 .
     */
    private String fsName;

    /**
     * 配额 .
     */
    private boolean quotaSwitch;

    /**
     * 安全模式 .
     */
    private String securityStyle;

    /**
     * 服务等级名称 .
     */
    private String tierName;

    /**
     * nfs .
     */
    private Integer nfsCount;

    /**
     * name .
     */
    private Integer cifsCount;

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
     * getFsName .
     *
     * @return String .
     */
    public String getFsName() {
        return fsName;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setFsName(final String param) {
        this.fsName = param;
    }

    /**
     * isQuotaSwitch .
     *
     * @return boolean .
     */
    public boolean isQuotaSwitch() {
        return quotaSwitch;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setQuotaSwitch(final boolean param) {
        this.quotaSwitch = param;
    }

    /**
     * getSecurityStyle .
     *
     * @return String .
     */
    public String getSecurityStyle() {
        return securityStyle;
    }

    /**
     * setName .
     *
     * @param param .
     */
    public void setSecurityStyle(final String param) {
        this.securityStyle = param;
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
     * getNfsCount .
     *
     * @return Integer .
     */
    public Integer getNfsCount() {
        return nfsCount;
    }

    /**
     * setName .
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
     * setCifsCount .
     *
     * @param param .
     */
    public void setCifsCount(final Integer param) {
        this.cifsCount = param;
    }

    @Override
    public final String toString() {
        return "Dtrees{" + "name='" + name + '\'' + ", fsName='" + fsName + '\'' + ", quotaSwitch=" + quotaSwitch
            + ", securityStyle='" + securityStyle + '\'' + ", tierName='" + tierName + '\'' + ", nfsCount=" + nfsCount
            + ", cifsCount=" + cifsCount + '}';
    }
}
