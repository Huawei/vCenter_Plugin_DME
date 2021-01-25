package com.huawei.vmware.mo;

/**
 * VmwareHypervisorHostNetworkSummary
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class VmwareHypervisorHostNetworkSummary {
    private String hostIp;
    private String hostNetmask;
    private String hostMacAddress;

    /**
     * VmwareHypervisorHostNetworkSummary
     */
    public VmwareHypervisorHostNetworkSummary() {
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostNetmask() {
        return hostNetmask;
    }

    public void setHostNetmask(String hostNetmask) {
        this.hostNetmask = hostNetmask;
    }

    public String getHostMacAddress() {
        return hostMacAddress;
    }

    public void setHostMacAddress(String hostMacAddress) {
        this.hostMacAddress = hostMacAddress;
    }
}
