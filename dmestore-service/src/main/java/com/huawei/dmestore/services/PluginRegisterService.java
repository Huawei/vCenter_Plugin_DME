package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;

/**
 * PluginRegisterService
 *
 * @author liugq
 * @since 2020-09-15
 **/
public interface PluginRegisterService {
    void installService(String vcenterIp, String vcenterPort, String vcenterUsername, String vcenterPassword,
        String dmeIp, String dmePort, String dmeUsername, String dmePassword) throws DmeException;

    /**
     * uninstallService
     */
    void uninstallService();
}
