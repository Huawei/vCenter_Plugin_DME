package com.huawei.dmestore.model;

/**
 * SmartQosForRdmCreate
 *
 * @author wangxiangyong
 * @since 2020-12-08
 */
public class SmartQosForRdmCreate extends BaseSmartQos {
    /**
     * 控制策略， 0：保护IO下限，1：控制IO上限.
     **/
    private String controlPolicy;

    /**
     * getControlPolicy .
     *
     * @return String .
     */
    public String getControlPolicy() {
        return controlPolicy;
    }

    /**
     * setControlPolicy .
     *
     * @param param .
     */
    public void setControlPolicy(final String param) {
        this.controlPolicy = param;
    }
}
