package com.huawei.dmestore.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className BestPracticeUpResultResponseTest
 * @description TODO
 * @date 2020/12/3 11:17
 */
public class BestPracticeUpResultResponseTest {

    @InjectMocks
    BestPracticeUpResultResponse bestPracticeUpResultResponse = new BestPracticeUpResultResponse();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        BestPracticeUpResultResponse bestPracticeUpResultResponse = new BestPracticeUpResultResponse();
        bestPracticeUpResultResponse.setHostObjectId("321");
        bestPracticeUpResultResponse.setHostName("321");
        List<BestPracticeUpResultBase> result = new ArrayList<>();
        bestPracticeUpResultResponse.setResult(result);
        bestPracticeUpResultResponse.setNeedReboot(true);
        bestPracticeUpResultResponse.getHostObjectId();
        bestPracticeUpResultResponse.getHostName();
        bestPracticeUpResultResponse.getResult();
        bestPracticeUpResultResponse.getNeedReboot();
    }

    @Test
    public void testToString() {
        bestPracticeUpResultResponse.toString();
    }
}