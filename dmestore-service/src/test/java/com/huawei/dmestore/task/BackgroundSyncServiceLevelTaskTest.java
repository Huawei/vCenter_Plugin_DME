package com.huawei.dmestore.task;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author lianq
 * @className BackgroundSyncServiceLevelTaskTest
 * @description TODO
 * @date 2020/12/2 19:05
 */
public class BackgroundSyncServiceLevelTaskTest {

    @InjectMocks
    BackgroundSyncServiceLevelTask backgroundSyncServiceLevelTask = new BackgroundSyncServiceLevelTask();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void execute() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = spy(JobExecutionContext.class);
        backgroundSyncServiceLevelTask.execute(jobExecutionContext);

    }
}