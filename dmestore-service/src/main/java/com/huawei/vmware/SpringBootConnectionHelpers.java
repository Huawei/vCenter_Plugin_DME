package com.huawei.vmware;

import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.services.VCenterInfoService;
import com.huawei.dmestore.utils.CipherUtils;
import com.huawei.vmware.util.TestVmwareContextFactory;
import com.huawei.vmware.util.VmwareContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * SpringBootConnectionHelper
 *
 * @author Administrator
 * @since 2020-12-10
 */
public class SpringBootConnectionHelpers extends VcConnectionHelpers {
    private static final Logger log = LoggerFactory.getLogger(SpringBootConnectionHelpers.class);
    private VCenterInfoService vcenterInfoService;

    /**
     * getvCenterInfoService
     *
     * @return VCenterInfoService
     */
    public VCenterInfoService getvCenterInfoService() {
        return vcenterInfoService;
    }

    /**
     * setvCenterInfoService
     *
     * @param param vcenterInfoService
     */
    public void setvCenterInfoService(VCenterInfoService param) {
        this.vcenterInfoService = param;
    }


    @Override
    public VmwareContext getServerContext(String serverguid) throws Exception {
        try {
            VCenterInfo vcenterInfo = vcenterInfoService.getVcenterInfo();
            if (null != vcenterInfo) {
                this.setServerurl(vcenterInfo.getHostIp());
                this.setServerport(vcenterInfo.getHostPort());
                this.setUsername(vcenterInfo.getUserName());
                this.setPassword(CipherUtils.decryptString(vcenterInfo.getPassword()));
            }
        } catch (Exception e) {
            log.error("get local vcenter info error",e.getMessage());
        }
        return TestVmwareContextFactory.getContext(getServerurl(), getServerport(), getUsername(), getPassword());
    }

    @Override
    public VmwareContext[] getAllContext() throws Exception {
        try {
            VCenterInfo vcenterInfo = vcenterInfoService.getVcenterInfo();
            if (null != vcenterInfo) {
                this.setServerurl(vcenterInfo.getHostIp());
                this.setServerport(vcenterInfo.getHostPort());
                this.setUsername(vcenterInfo.getUserName());
                this.setPassword(CipherUtils.decryptString(vcenterInfo.getPassword()));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        VmwareContext vmwareContext =
            TestVmwareContextFactory.getContext(getServerurl(), getServerport(), getUsername(), getPassword());
        List<VmwareContext> vmwareContextList = new ArrayList<>();
        vmwareContextList.add(vmwareContext);
        return vmwareContextList.toArray(new VmwareContext[0]);
    }
}
