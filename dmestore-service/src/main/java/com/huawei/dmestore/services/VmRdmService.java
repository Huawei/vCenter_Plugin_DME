package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.VmRdmCreateBean;

import java.util.List;
import java.util.Map;

/**
 * VmRdmService
 *
 * @author wangxiangyong
 * @since 2020-09-15
 **/
public interface VmRdmService {
    /**
     * vCenter 虚拟机RDM创建
     *
     * @param dataStoreObjectId vCenter存储名称对象ID
     * @param vmObjectId vCenter虚拟机ID
     * @param createBean DME卷创建基础信息
     * @throws DmeException DmeException
     **/
    void createRdm(String dataStoreObjectId, String vmObjectId, VmRdmCreateBean createBean) throws DmeException;

    /**
     * 获取DME主机信息
     *
     * @return List
     * @throws DmeException DmeException
     **/
    List<Map<String, Object>> getAllDmeHost() throws DmeException;

    /**
     * 获取虚拟机对应主机的存储信息
     *
     * @param vmObjectId vCenter虚拟机objectId
     * @return java.util.List
     * @throws DmeException DmeException
     **/
    List<Object> getDatastoreMountsOnHost(String vmObjectId) throws DmeException;
}
