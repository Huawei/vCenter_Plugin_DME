package com.huawei.vmware.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.huawei.dmestore.constant.DpSqlFileConstants;
import com.vmware.vim25.ManagedObjectReference;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className ClusterMOFactoryTest
 * @description TODO
 * @date 2020/12/1 20:31
 */
public class ClusterVmwareMoFactoryTest {

    @InjectMocks
    ClusterVmwareMoFactory factory;

    VmwareContext vmwareContext;
    ManagedObjectReference managedObjectReference;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vmwareContext = mock(VmwareContext.class);
        managedObjectReference = spy(ManagedObjectReference.class);
        String[] ALL_TABLES = DpSqlFileConstants.ALL_TABLES;
        System.out.println(ALL_TABLES);
    }

    @Test
    public void getInstance() {
        ClusterVmwareMoFactory.getInstance();
    }

    @Test
    public void build() throws Exception {
        factory.build(vmwareContext, managedObjectReference);
    }
}