package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className NfsSharesTest
 * @description TODO
 * @date 2020/12/3 11:23
 */
public class NfsSharesTest {

    @InjectMocks
    NfsShares nfsShares = new NfsShares();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        NfsShares nfsShares = new NfsShares();
        nfsShares.setName("321");
        nfsShares.setSharePath("321");
        nfsShares.setStorageId("321");
        nfsShares.setTierName("321");
        nfsShares.setOwningDtreeName("321");
        nfsShares.setFsName("321");
        nfsShares.setOwningDtreeId("321");

        nfsShares.getName();
        nfsShares.getSharePath();
        nfsShares.getStorageId();
        nfsShares.getTierName();
        nfsShares.getOwningDtreeName();
        nfsShares.getFsName();
        nfsShares.getOwningDtreeId();


    }

    @Test
    public void testToString() {
        nfsShares.toString();
    }
}