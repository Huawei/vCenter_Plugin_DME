package com.huawei.vmware.mo;

import org.junit.Test;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName VmwareHypervisorHostNetworkSummaryTest.java
 * @Description TODO
 * @createTime 2020年12月03日 10:08:00
 */
public class VmwareHypervisorHostNetworkSummaryTest {
    @Test
    public void test() {
        VmwareHypervisorHostNetworkSummary summary = new VmwareHypervisorHostNetworkSummary();
        summary.setHostIp("10.143.132.11");
        summary.setHostMacAddress("11:22:33:44");
        summary.setHostNetmask("10.143.132.1");

        System.out.println(summary.getHostIp() + summary.getHostMacAddress() + summary.getHostNetmask());
    }

}