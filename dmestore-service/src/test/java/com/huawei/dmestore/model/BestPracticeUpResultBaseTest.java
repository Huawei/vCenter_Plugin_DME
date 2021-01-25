package com.huawei.dmestore.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className BestPracticeUpResultBaseTest
 * @description TODO
 * @date 2020/12/3 11:08
 */
public class BestPracticeUpResultBaseTest {

    @InjectMocks
    BestPracticeUpResultBase bestPracticeUpResultBase = new BestPracticeUpResultBase();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        bestPracticeUpResultBase.setHostSetting("321");
        bestPracticeUpResultBase.setNeedReboot(true);
        bestPracticeUpResultBase.setHostObjectId("321");
        bestPracticeUpResultBase.setUpdateResult(true);
        bestPracticeUpResultBase.getHostSetting();
        bestPracticeUpResultBase.getNeedReboot();
        bestPracticeUpResultBase.getHostObjectId();
        bestPracticeUpResultBase.getUpdateResult();
    }

    @Test
    public void testToString() {
        bestPracticeUpResultBase.toString();
    }
}