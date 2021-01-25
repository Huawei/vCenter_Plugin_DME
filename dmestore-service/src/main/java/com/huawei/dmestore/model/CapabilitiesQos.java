package com.huawei.dmestore.model;

/**
 * CapabilitiesQos
 *
 * @author lianq
 * @ClassName: CapabilitiesQos
 * @Company: GH-CA
 * @since 2020-09-03
 */
public class CapabilitiesQos {
    /**
     * smartQos .
     */
    private SmartQos smartQos;

    /**
     * qosParam .
     */
    private QosParam qosParam;

    /**
     * enabled .
     */
    private Boolean enabled;

    /**
     * getSmartQos .
     *
     * @return SmartQos .
     */
    public SmartQos getSmartQos() {
        return smartQos;
    }

    /**
     * getQosParam .
     *
     * @return QosParam .
     */
    public QosParam getQosParam() {
        return qosParam;
    }

    /**
     * setQosParam .
     *
     * @param param .
     */
    public void setQosParam(final QosParam param) {
        this.qosParam = param;
    }

    /**
     * setSmartQos .
     *
     * @param param .
     */
    public void setSmartQos(final SmartQos param) {
        this.smartQos = param;
    }

    /**
     * getEnabled .
     *
     * @return Boolean .
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * setQosParam .
     *
     * @param param .
     */
    public void setEnabled(final Boolean param) {
        this.enabled = param;
    }
}
