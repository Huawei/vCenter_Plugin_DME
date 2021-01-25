package com.huawei.dmestore.task;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.Scheduler;

/**
 * @author lianq
 * @className QuartzConfigTest
 * @description TODO
 * @date 2020/12/2 19:55
 */
public class QuartzConfigTest {

    @InjectMocks
    QuartzConfig quartzConfig = new QuartzConfig();
    private Scheduler scheduler;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        scheduler = spy(Scheduler.class);
    }

    @Test
    public void getScheduler() {
        quartzConfig.getScheduler();
    }

    @Test
    public void destory() {
        quartzConfig.destory();
    }
}