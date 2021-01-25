package com.huawei.dmestore.services;

import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.exception.DmeException;
import com.huawei.dmestore.utils.VCSDKUtils;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.mockito.Mockito.when;

/**
 * @author lianq
 * @className HostAccessServiceImplTest
 * @description TODO
 * @date 2020/11/20 10:24
 */
public class HostAccessServiceImplTest {

    private Gson gson = new Gson();
    @Mock
    private VCSDKUtils vcsdkUtils;
    @Mock
    private VCenterInfoService vCenterInfoService;
    @InjectMocks
    HostAccessService hostAccessService = new HostAccessServiceImpl();

    Map<String, Object> params;
    Map<String, String> vmKernel;
    List<Map<String, Object>> ethPorts;
    Map<String, Object> mapEthPorts;
    VCenterInfo vCenterInfo;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        params = new HashMap<>();
        vmKernel = new HashMap<>();
        vmKernel.put("321", "321");
        params.put("hostObjectId", "321");
        params.put("vmKernel", vmKernel);
        ethPorts = new ArrayList<>();
        mapEthPorts = new HashMap<>();
        mapEthPorts.put("321", "321");
        ethPorts.add(mapEthPorts);
        params.put("ethPorts", ethPorts);

    }

    @Test
    public void configureIscsi() throws DmeException {
        hostAccessService.configureIscsi(params);

    }

    @Test
    public void testConnectivity() throws DmeException {
        vCenterInfo = new VCenterInfo();
        vCenterInfo.setCreateTime(new Date());
        vCenterInfo.setHostIp("321");
        vCenterInfo.setId(321);
        vCenterInfo.setPassword("321");
        vCenterInfo.setPushEvent(true);
        vCenterInfo.setPushEventLevel(2);
        vCenterInfo.setUserName("321");
        vCenterInfo.setState(true);
        when(vCenterInfoService.getVcenterInfo()).thenReturn(vCenterInfo);
        List<Map<String, Object>> reEthPorts = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("connectStatus", "true");
        reEthPorts.add(map);
        when(vcsdkUtils.testConnectivity("321", ethPorts, vmKernel, vCenterInfo)).thenReturn(gson.toJson(reEthPorts));
        hostAccessService.testConnectivity(params);

    }
}