package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className NfsDataStoreFsAttrTest
 * @description TODO
 * @date 2020/12/3 14:15
 */
public class NfsDataStoreFsAttrTest {

    @InjectMocks
    NfsDataStoreFsAttr nfsDataStoreFsAttr = new NfsDataStoreFsAttr();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        NfsDataStoreFsAttr nfsDataStoreFsAttr = new NfsDataStoreFsAttr();
        nfsDataStoreFsAttr.setController("321");
        nfsDataStoreFsAttr.setName("321");
        nfsDataStoreFsAttr.setDescription("321");
        nfsDataStoreFsAttr.setDevice("321");
        nfsDataStoreFsAttr.setStoragePoolName("321");
        nfsDataStoreFsAttr.setProvisionType("321");
        nfsDataStoreFsAttr.setApplicationScenario("321");
        nfsDataStoreFsAttr.setDataDeduplication(true);
        nfsDataStoreFsAttr.setDateCompression(true);
        nfsDataStoreFsAttr.setFileSystemId("321");

        nfsDataStoreFsAttr.getController();
        nfsDataStoreFsAttr.getName();
        nfsDataStoreFsAttr.getDescription();
        nfsDataStoreFsAttr.getDevice();
        nfsDataStoreFsAttr.getStoragePoolName();
        nfsDataStoreFsAttr.getProvisionType();
        nfsDataStoreFsAttr.getApplicationScenario();
        nfsDataStoreFsAttr.getDataDeduplication();
        nfsDataStoreFsAttr.getDateCompression();
        nfsDataStoreFsAttr.getFileSystemId();


    }

    @Test
    public void testToString() {
        nfsDataStoreFsAttr.toString();
    }
}