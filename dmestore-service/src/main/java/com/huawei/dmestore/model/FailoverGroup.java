package com.huawei.dmestore.model;

/**
 * FailoverGroup
 *
 * @author lianq
 * @Description: FailoverGroup
 * @since 2020-10-14
 */
public class FailoverGroup {
    /**
     * 漂移组类型，枚举（system、VLAN、customized） .
     */
    private String failoverGroupType;
    /**
     * 漂移组id .
     */
    private String id;
    /**
     * 漂移组名称 .
     */
    private String name;

    /**
     * getFailoverGroupType .
     *
     * @return String .
     */
    public String getFailoverGroupType() {
        return failoverGroupType;
    }

    /**
     * setFailoverGroupType .
     *
     * @param param .
     */
    public void setFailoverGroupType(final String param) {
        this.failoverGroupType = param;
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
     * setFailoverGroupType .
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
}
