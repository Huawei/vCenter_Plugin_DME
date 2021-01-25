package com.huawei.vmware.util;

import static org.mockito.Mockito.spy;

import com.vmware.vim25.GuestOsDescriptor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VmwareHelperTest
 * @description TODO
 * @date 2020/12/2 11:42
 */
public class VmwareHelperTest {

    @InjectMocks
    VmwareHelper vmwareHelper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isReservedScsiDeviceNumber() {
        vmwareHelper.isReservedScsiDeviceNumber(109);
    }

    @Test
    public void getRecommendedDiskControllerFromDescriptor() throws Exception {
        GuestOsDescriptor guestOsDescriptor = spy(GuestOsDescriptor.class);
        guestOsDescriptor.setRecommendedDiskController("pvscsi");
        vmwareHelper.getRecommendedDiskControllerFromDescriptor(guestOsDescriptor);
    }
}