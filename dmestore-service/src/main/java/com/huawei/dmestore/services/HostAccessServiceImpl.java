package com.huawei.dmestore.services;

import com.huawei.dmestore.constant.DmeConstants;
import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.model.EthPortInfo;
import com.huawei.dmestore.utils.VCSDKUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * DmeNFSAccessService
 *
 * @author yy
 * @since 2020-09-03
 **/
public class HostAccessServiceImpl implements HostAccessService {
    private static final Logger LOG = LoggerFactory.getLogger(HostAccessServiceImpl.class);

    private Gson gson = new Gson();

    private VCSDKUtils vcsdkUtils;

    private VCenterInfoService vcenterinfoservice;

    public void setvCenterInfoService(VCenterInfoService vcenterService) {
        this.vcenterinfoservice = vcenterService;
    }

    public VCSDKUtils getVcsdkUtils() {
        return vcsdkUtils;
    }

    public void setVcsdkUtils(VCSDKUtils vcsdkUtils) {
        this.vcsdkUtils = vcsdkUtils;
    }

    @Override
    public void configureIscsi(Map<String, Object> params) throws DmeException {
        if (params != null) {
            if (params.get(DmeConstants.ETHPORTS) == null) {
                LOG.error("configure Iscsi error:ethPorts is null.");
                throw new DmeException("configure Iscsi error:ethPorts is null.");
            }
            if (params.get(DmeConstants.VMKERNEL) == null) {
                LOG.error("configure Iscsi error:vmKernel is null.");
                throw new DmeException("configure Iscsi error:vmKernel is null.");
            }
            if (StringUtils.isEmpty(params.get(DmeConstants.HOSTOBJECTID))) {
                LOG.error("configure Iscsi error:host ObjectId is null.");
                throw new DmeException("configure Iscsi error:host ObjectId is null.");
            }
            List<Map<String, Object>> ethPorts = (List<Map<String, Object>>) params.get("ethPorts");
            Map<String, String> vmKernel = (Map<String, String>) params.get("vmKernel");
            String hostObjectId = params.get("hostObjectId").toString();
            vcsdkUtils.configureIscsi(hostObjectId, vmKernel, ethPorts);
        } else {
            throw new DmeException("configure Iscsi parameter exception:" + params);
        }
    }

    @Override
    public List<EthPortInfo> testConnectivity(Map<String, Object> params) throws DmeException {
        List<EthPortInfo> relists = null;
        try {
            if (params != null) {
                if (params.get(DmeConstants.ETHPORTS) == null) {
                    LOG.error("test connectivity error:ethPorts is null.");
                    throw new DmeException("test connectivity error:ethPorts is null.");
                }
                if (StringUtils.isEmpty(params.get(DmeConstants.HOSTOBJECTID))) {
                    LOG.error("test connectivity error:host ObjectId is null.");
                    throw new DmeException("test connectivity error:host ObjectId is null.");
                }
                List<Map<String, Object>> ethPorts = (List<Map<String, Object>>) params.get("ethPorts");
                String hostObjectId = params.get("hostObjectId").toString();
                Map<String, String> vmKernel = null;
                if (params.get(DmeConstants.VMKERNEL) != null) {
                    vmKernel = (Map<String, String>) params.get("vmKernel");
                }
                VCenterInfo vcenterInfo = vcenterinfoservice.getVcenterInfo();
                if (vcenterInfo != null) {
                    String conStr = vcsdkUtils.testConnectivity(hostObjectId, ethPorts, vmKernel, vcenterInfo);
                    if (!StringUtils.isEmpty(conStr)) {
                        relists = gson.fromJson(conStr, new TypeToken<List<EthPortInfo>>() {
                        }.getType());
                    }
                }
            } else {
                throw new DmeException("test connectivity parameter exception:" + params);
            }
        } catch (DmeException e) {
            LOG.error("test connectivity error:", e);
            throw new DmeException(e.getMessage());
        }
        return relists;
    }
}
