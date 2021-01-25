package com.huawei.dmestore.services;

import com.huawei.dmestore.dao.VCenterInfoDao;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeSqlException;

import java.util.HashMap;
import java.util.Map;

/**
 * VCenterInfoServiceImpl
 *
 * @author liugq
 * @since 2020-09-15
 **/
public class VCenterInfoServiceImpl extends DMEOpenApiService implements VCenterInfoService {
    private VCenterInfoDao vcenterInfoDao;

    public VCenterInfoDao getVcenterInfoDao() {
        return vcenterInfoDao;
    }

    public void setVcenterInfoDao(VCenterInfoDao vcenterInfoDao) {
        this.vcenterInfoDao = vcenterInfoDao;
    }

    @Override
    public int addVcenterInfo(VCenterInfo info) throws DmeSqlException {
        return vcenterInfoDao.addVcenterInfo(info);
    }

    @Override
    public int saveVcenterInfo(final VCenterInfo info) throws DmeSqlException {
        VCenterInfo vcenterInfo = vcenterInfoDao.getVcenterInfo();
        int returnValue;
        if (vcenterInfo != null) {
            vcenterInfo.setUserName(info.getUserName());
            vcenterInfo.setState(info.isState());
            vcenterInfo.setPushEvent(info.isPushEvent());
            vcenterInfo.setPushEventLevel(info.getPushEventLevel());
            vcenterInfo.setHostIp(info.getHostIp());
            vcenterInfo.setHostPort(info.getHostPort());
            if (info.getPassword() != null && !"".equals(info.getPassword())) {
                vcenterInfo.setPassword(info.getPassword());
            }
            returnValue = vcenterInfoDao.updateVcenterInfo(vcenterInfo);
        } else {
            returnValue = addVcenterInfo(info);
        }

        return returnValue;
    }

    @Override
    public Map<String, Object> findVcenterInfo() throws DmeSqlException {
        Map<String, Object> returnMap = new HashMap<>();
        VCenterInfo vcenterInfo = vcenterInfoDao.getVcenterInfo();
        if (vcenterInfo != null) {
            returnMap.put("USER_NAME", vcenterInfo.getUserName());
            returnMap.put("STATE", vcenterInfo.isState());
            returnMap.put("HOST_IP", vcenterInfo.getHostIp());
            returnMap.put("HOST_PORT", vcenterInfo.getHostPort());
            returnMap.put("PUSH_EVENT", vcenterInfo.isPushEvent());
            returnMap.put("PUSH_EVENT_LEVEL", vcenterInfo.getPushEventLevel());
        }
        return returnMap;
    }

    @Override
    public VCenterInfo getVcenterInfo() throws DmeSqlException {
        return vcenterInfoDao.getVcenterInfo();
    }
}
