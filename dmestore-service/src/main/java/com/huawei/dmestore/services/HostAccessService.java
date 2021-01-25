package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.EthPortInfo;

import java.util.List;
import java.util.Map;

/**
 * HostAccessService
 *
 * @author yy
 * @since 2020-09-03
 **/
public interface HostAccessService {
    /**
     * Configure the host with iSCSI, params include:
     * String hostObjectId：host object id
     * Map vmKernel 选择的vmkernel
     * List ethPorts: 选择的以太网端口列表
     *
     * @param params params include:hostObjectId,vmKernel,ethPorts
     * @throws DmeException when error
     */
    void configureIscsi(Map<String, Object> params) throws DmeException;

    /**
     * Test Connectivity:
     * String hostObjectId：host object id
     * List 要测试的以太网端口列表
     * Map 虚拟网卡信息
     *
     * @param params params include:hostObjectId,ethPorts，vmKernel
     * @return List
     * @throws DmeException when error
     */
    List<EthPortInfo> testConnectivity(Map<String, Object> params) throws DmeException;
}
