package com.huawei.dmestore.services;

import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.utils.CipherUtils;
import com.huawei.vmware.VcConnectionHelpers;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * PluginRegisterServiceImpl
 *
 * @author liugq
 * @since 2020-09-15
 **/
public class PluginRegisterServiceImpl implements PluginRegisterService {
    @Autowired
    private DmeAccessService dmeAccessService;

    @Autowired
    private SystemService systemService;

    @Autowired
    private VCenterInfoService vcenterinfoservice;

    @Autowired
    private VcConnectionHelpers vcConnectionHelpers;

    @Override
    public void installService(String vcenterIp, String vcenterPort, String vcenterUsername, String vcenterPassword,
        String dmeIp, String dmePort, String dmeUsername, String dmePassword) throws DmeException {
        try {
            // 保存vcenter信息,如有已有vcenter信息，需要更新
            VCenterInfo vcenterinfo = new VCenterInfo();
            vcenterinfo.setHostIp(vcenterIp);
            vcenterinfo.setUserName(vcenterUsername);
            vcenterinfo.setPassword(CipherUtils.encryptString(vcenterPassword));
            vcenterinfo.setHostPort(Integer.parseInt(vcenterPort));
            vcenterinfoservice.saveVcenterInfo(vcenterinfo);

            vcConnectionHelpers.setServerurl("https:// " + vcenterIp + ":" + vcenterPort + "/sdk");
            vcConnectionHelpers.setUsername(vcenterUsername);
            vcConnectionHelpers.setPassword(vcenterPassword);

            if (!"".equalsIgnoreCase(dmeIp)) {
                // 调用接口，创建dme连接信息,如有已有dme信息，需要更新
                Map params = new HashMap();
                params.put("hostIp", dmeIp);
                params.put("hostPort", dmePort);
                params.put("userName", dmeUsername);
                params.put("password", dmePassword);
                dmeAccessService.accessDme(params);
            }
        } catch (DmeException e) {
            throw new DmeException("503", e.getMessage());
        }
    }

    @Override
    public void uninstallService() {
        systemService.cleanData();
    }
}
