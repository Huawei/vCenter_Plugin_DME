package com.huawei.dmestore.entity;

import com.huawei.dmestore.utils.CipherUtils;

import java.io.Serializable;

/**
 * DME
 *
 * @Description: DME entity.
 * @since 2020-12-08
 */
public class DME extends Dmebase implements Serializable {
    private static final long serialVersionUID = -9063117479397179007L;

    /**
     * id .
     */
    private int id;

    /**
     * aliasName .
     */
    private String aliasName;

    /**
     * latestStatus .
     */
    private String latestStatus;

    /**
     * HA状态：0/null-未同步 1-已同步 2-未同步(取消订阅).
     */
    private String reservedInt1;

    /**
     * 保活状态: 0/null-未订阅 1-已订阅 2-未订阅(取消订阅) .
     */
    private String reservedInt2;

    /**
     * reservedStr1 .
     */
    private String reservedStr1;

    /**
     * reservedStr2 .
     */
    private String reservedStr2;

    /**
     * lastModify .
     */
    private String lastModify;

    /**
     * createTime .
     */
    private String createTime;

    /**
     * systemId .
     */
    private String systemId;

    /**
     * HA Provider状态：0/null-未创建 1-已创建 2-创建失败 .
     */
    private int haProvider;

    /**
     * 告警订阅创建状态：0/null-未创建 1-已创建 2-创建有失败 .
     */
    private int alarmDefinition;

    /**
     * DME .
     *
     * @param param1 hostIp .
     * @param param2 hostPort
     * @param param3 loginAccount
     * @param param4 loginPwd
     */
    public DME(final String param1, final int param2, final String param3, final String param4) {
        super(param1, param2, param3, param4);
    }

    /**
     * DME.
     */
    public DME() {
        super(null, 0, null, null);
    }

    /**
     * getHaProvider .
     *
     * @return int.
     */
    public int getHaProvider() {
        return haProvider;
    }

    /**
     * setHaProvider .
     *
     * @param param .
     */
    public void setHaProvider(final int param) {
        this.haProvider = param;
    }

    /**
     * getAlarmDefinition .
     *
     * @return int.
     */
    public int getAlarmDefinition() {
        return alarmDefinition;
    }

    /**
     * setAlarmDefinition .
     *
     * @param param .
     */
    public void setAlarmDefinition(final int param) {
        this.alarmDefinition = param;
    }

    /**
     * getHaProvider .
     *
     * @return String.
     */
    public String getLatestStatus() {
        return latestStatus;
    }

    /**
     * setLatestStatus .
     *
     * @param param .
     */
    public void setLatestStatus(final String param) {
        this.latestStatus = param;
    }

    /**
     * getHaProvider .
     *
     * @return String.
     */
    public String getReservedInt1() {
        return reservedInt1;
    }

    /**
     * setReservedInt1 .
     *
     * @param param .
     */
    public void setReservedInt1(final String param) {
        this.reservedInt1 = param;
    }

    /**
     * getHaProvider .
     *
     * @return String.
     */
    public String getReservedInt2() {
        return reservedInt2;
    }

    /**
     * setReservedInt2 .
     *
     * @param param .
     */
    public void setReservedInt2(final String param) {
        this.reservedInt2 = param;
    }

    /**
     * getHaProvider .
     *
     * @return String.
     */
    public String getReservedStr1() {
        return reservedStr1;
    }

    /**
     * setReservedStr1 .
     *
     * @param param .
     */
    public void setReservedStr1(final String param) {
        this.reservedStr1 = param;
    }

    /**
     * getHaProvider .
     *
     * @return String.
     */
    public String getReservedStr2() {
        return reservedStr2;
    }

    /**
     * setReservedStr2 .
     *
     * @param param .
     */
    public void setReservedStr2(final String param) {
        this.reservedStr2 = param;
    }

    /**
     * getHaProvider .
     *
     * @return String.
     */
    public int getId() {
        return id;
    }

    /**
     * setId .
     *
     * @param param .
     */
    public void setId(final int param) {
        this.id = param;
    }

    /**
     * getAliasName .
     *
     * @return String.
     */
    public String getAliasName() {
        return aliasName;
    }

    /**
     * setAliasName .
     *
     * @param param .
     */
    public void setAliasName(final String param) {
        this.aliasName = param;
    }

    /**
     * getLastModify .
     *
     * @return String.
     */
    public String getLastModify() {
        return lastModify;
    }

    /**
     * setLastModify .
     *
     * @param param .
     */
    public void setLastModify(final String param) {
        this.lastModify = param;
    }

    /**
     * getCreateTime .
     *
     * @return String.
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * setCreateTime .
     *
     * @param param .
     */
    public void setCreateTime(final String param) {
        this.createTime = param;
    }

    /**
     * setSystemId .
     *
     * @param param .
     */
    public void setSystemId(final String param) {
        this.systemId = param;
    }

    /**
     * getSystemId .
     *
     * @return String.
     */
    public String getSystemId() {
        return systemId;
    }

    @Override
    public final String toString() {
        return "dme [id=" + id + ", hostIp=" + getHostIp() + ", hostPort=" + getHostPort() + ", loginAccount="
            + getLoginAccount() + ", loginPwd=******" + ", latestStatus=" + latestStatus + ", reservedInt1="
            + reservedInt1 + ", reservedInt2=" + reservedInt2 + ", reservedStr1=" + reservedStr1 + ", reservedStr2="
            + reservedStr2 + ", lastModify=" + lastModify + ", createTime=" + createTime + "]";
    }

    /**
     * 加密dme对象的登录密码.
     *
     * @param param dme
     */
    public static void updateEsightWithEncryptedPassword(final DME param) {
        if (param != null) {
            param.setLoginPwd(CipherUtils.encryptString(param.getLoginPwd()));
        }
    }

    /**
     * 解密dme对象的登录密码.
     *
     * @param param dme .
     */
    public static void decryptedPassword(final DME param) {
        if (param != null) {
            param.setLoginPwd(CipherUtils.decryptString(param.getLoginPwd()));
        }
    }
}
