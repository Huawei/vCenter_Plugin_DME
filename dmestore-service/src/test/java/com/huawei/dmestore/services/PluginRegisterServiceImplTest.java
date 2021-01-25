package com.huawei.dmestore.services;

import com.huawei.dmestore.exception.DmeException;
import com.huawei.vmware.VcConnectionHelpers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className PluginRegisterServiceImplTest
 * @description TODO
 * @date 2020/11/19 10:17
 */
public class PluginRegisterServiceImplTest {

    @Mock
    private DmeAccessService dmeAccessService;
    @Mock
    private SystemService systemService;
    @Mock
    private VCenterInfoService vCenterInfoService;
    @Mock
    private VcConnectionHelpers vcConnectionHelpers;

    @InjectMocks
    PluginRegisterService pluginRegisterService = new PluginRegisterServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void installService() throws DmeException {
        pluginRegisterService.installService("321","321","321","321","321","321","321","321");
    }

    @Test
    public void uninstallService() {
        pluginRegisterService.uninstallService();
    }
}