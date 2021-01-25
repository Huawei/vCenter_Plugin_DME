package com.huawei.vmware;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.huawei.dmestore.entity.VCenterInfo;
import com.huawei.dmestore.services.VCenterInfoService;
import com.huawei.vmware.util.TestVmwareContextFactory;
import com.huawei.vmware.util.VmwareContext;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className SpringBootConnectionHelperTest
 * @description TODO
 * @date 2020/12/3 15:15
 */
public class SpringBootConnectionHelperTest {

    @Mock
    private VCenterInfoService vCenterInfoService;
    @InjectMocks
    SpringBootConnectionHelpers springBootConnectionHelper = new SpringBootConnectionHelpers();
    VCenterInfo vCenterInfo ;
    VmwareContext vmwareContext;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vCenterInfo = spy(VCenterInfo.class);
        vCenterInfo.setPushEventLevel(21);
        vCenterInfo.setId(21);
        vCenterInfo.setHostIp("21");
        vCenterInfo.setUserName("21");
        vCenterInfo.setPassword("21");
        vCenterInfo.setCreateTime(new Date());
        vCenterInfo.setState(true);
        vCenterInfo.setPushEvent(true);
        vCenterInfo.setHostPort(21);
        vmwareContext = mock(VmwareContext.class);


    }

    @Test
    public void getvCenterInfoService() {
        springBootConnectionHelper.getvCenterInfoService();
    }

    @Test
    public void setvCenterInfoService() {
        springBootConnectionHelper.setvCenterInfoService(vCenterInfoService);
    }

    @Test
    public void getServerContext() throws Exception {
        when(vCenterInfoService.getVcenterInfo()).thenReturn(vCenterInfo);
        springBootConnectionHelper.getServerContext("321");

    }

    @Test
    public void getAllContext() throws Exception {
        when(vCenterInfoService.getVcenterInfo()).thenReturn(vCenterInfo);
        when(TestVmwareContextFactory.getContext("21", 21, "21", "21")).thenReturn(vmwareContext);
        springBootConnectionHelper.getAllContext();

    }
}