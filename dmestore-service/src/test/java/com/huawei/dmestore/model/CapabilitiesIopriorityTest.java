package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className CapabilitiesIopriorityTest
 * @description TODO
 * @date 2020/12/3 11:40
 */
public class CapabilitiesIopriorityTest {

    @InjectMocks
    CapabilitiesIopriority capabilitiesIopriority = new CapabilitiesIopriority();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        CapabilitiesIopriority capabilitiesIopriority = new CapabilitiesIopriority();
        capabilitiesIopriority.setEnabled(true);
        capabilitiesIopriority.setPolicy(1);

        capabilitiesIopriority.getEnabled();
        capabilitiesIopriority.getPolicy();


    }

    @Test
    public void testToString() {
        capabilitiesIopriority.toString();
    }
}