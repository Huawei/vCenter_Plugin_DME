package com.huawei.dmestore.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

/**
 * @author lianq
 * @className BestPracticeUpdateByTypeRequestTest
 * @description TODO
 * @date 2020/12/3 11:26
 */
public class BestPracticeUpdateByTypeRequestTest {

    @InjectMocks
    BestPracticeUpdateByTypeRequest bestPracticeUpdateByTypeRequest = new BestPracticeUpdateByTypeRequest();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        bestPracticeUpdateByTypeRequest.setHostSetting("321");
        List<String> hostObjectIds = new ArrayList<>();
        bestPracticeUpdateByTypeRequest.setHostObjectIds(hostObjectIds);

        bestPracticeUpdateByTypeRequest.getHostSetting();
        bestPracticeUpdateByTypeRequest.getHostObjectIds();


    }

    @Test
    public void testToString() {
        bestPracticeUpdateByTypeRequest.toString();
    }
}