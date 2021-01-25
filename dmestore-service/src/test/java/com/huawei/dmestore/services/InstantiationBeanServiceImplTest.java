package com.huawei.dmestore.services;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className InstantiationBeanServiceImplTest
 * @description TODO
 * @date 2020/11/20 14:38
 */
public class InstantiationBeanServiceImplTest {

    @Mock
    private SystemService systemService;
    @Mock
    private VCenterInfoService vCenterInfoService;

    @InjectMocks
    private InstantiationBeanService instantiationBeanService = new InstantiationBeanServiceImpl();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void init() {
        instantiationBeanService.init();
    }
}