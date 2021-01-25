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
 * @className BackgroundScanDatastoreTaskTest
 * @description TODO
 * @date 2020/12/2 19:03
 */
public class BackgroundScanDatastoreTaskTest {

    @InjectMocks
    BackgroundScanDatastoreTask backgroundScanDatastoreTask = new BackgroundScanDatastoreTask();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void scanDatastore() {
        backgroundScanDatastoreTask.scanDatastore();
    }

    @Test
    public void execute() {
        backgroundScanDatastoreTask.execute();
    }

    @Test
    public void testExecute() throws JobExecutionException {
        JobExecutionContext jobExecutionContext = spy(JobExecutionContext.class);
        backgroundScanDatastoreTask.execute(jobExecutionContext);

    }
}