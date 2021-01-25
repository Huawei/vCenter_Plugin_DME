package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className DtreesTest
 * @description TODO
 * @date 2020/12/3 11:20
 */
public class DtreesTest {

    @InjectMocks
    Dtrees dtrees = new Dtrees();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        dtrees.setName("321");
        dtrees.setFsName("321");
        dtrees.setQuotaSwitch(true);
        dtrees.setSecurityStyle("321");
        dtrees.setTierName("321");
        dtrees.setNfsCount(21);
        dtrees.setCifsCount(21);

        dtrees.getName();
        dtrees.getFsName();
        dtrees.isQuotaSwitch();
        dtrees.getSecurityStyle();
        dtrees.getTierName();
        dtrees.getNfsCount();
        dtrees.getCifsCount();


    }

    @Test
    public void testToString() {
        dtrees.toString();
    }
}