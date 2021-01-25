package com.huawei.dmestore.entity;

/**
 * DMEbase.
 *
 * @author Administrator
 * @since 2020-12-08
 */
public class Dmebase {
    /**
     * hostIp.
     */
    private String hostIp;
    /**
     * hostPort.
     */
    private int hostPort;
    /**
     * loginAccount.
     */
    private String loginAccount;
    /**
     * loginPwd.
     */
    private String loginPwd;

    /**
     * 构造方法.
     *
     * @param param1 hostIp 服务器IP
     * @param param2 hostPort 端口
     * @param param3 loginAccount 登录账号
     * @param param4 loginPwd 登录密码
     */
    public Dmebase(final String param1,
                   final int param2,
                   final String param3,
                   final String param4) {
        this.hostIp = param1;
        this.hostPort = param2;
        this.loginAccount = param3;
        this.loginPwd = param4;
    }

    /**
     * getHostIp .
     *
     * @return String .
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * setHostIp .
     *
     * @param param .
     */
    public void setHostIp(final String param) {
        this.hostIp = param;
    }

    /**
     * getHostPort .
     *
     * @return int .
     */
    public int getHostPort() {
        return hostPort;
    }

    /**
     * setHostIp .
     *
     * @param param .
     */
    public void setHostPort(final int param) {
        this.hostPort = param;
    }

    /**
     * getLoginAccount .
     *
     * @return String .
     */
    public String getLoginAccount() {
        return loginAccount;
    }

    /**
     * setLoginAccount .
     *
     * @param param .
     */
    public void setLoginAccount(final String param) {
        this.loginAccount = param;
    }

    /**
     * getLoginPwd .
     *
     * @return String .
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * setHostIp .
     *
     * @param param .
     */
    public void setLoginPwd(final String param) {
        this.loginPwd = param;
    }

    /**
     * toString .
     *
     * @return
     */
    @Override
    public String toString() {
        return "Esight{" + "hostIp='" + hostIp + '\''
            + ", hostPort=" + hostPort
            + ", loginAccount='" + loginAccount
            + '\'' + ", loginPwd='******'" + '}';
    }
}
