package com.huawei.vmware.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.vmware.vim25.ManagedObjectReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className VirtualMachineMOFactoryTest
 * @description TODO
 * @date 2020/12/2 11:17
 */
public class VirtualMachineMoFactorysTest {

    @InjectMocks
    VirtualMachineMoFactorys factory;

    VmwareContext vmwareContext;
    ManagedObjectReference managedObjectReference;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareContext = mock(VmwareContext.class);
        managedObjectReference = spy(ManagedObjectReference.class);
    }

    @Test
    public void getInstance() {
        VirtualMachineMoFactorys.getInstance();
    }

    @Test
    public void build() throws Exception {
        factory.build(vmwareContext, managedObjectReference);
    }
}