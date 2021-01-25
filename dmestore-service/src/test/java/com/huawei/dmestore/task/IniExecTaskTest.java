package com.huawei.dmestore.task;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.huawei.dmestore.services.SystemServiceImpl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author lianq
 * @className IniExecTaskTest
 * @description TODO
 * @date 2020/12/2 19:07
 */
public class IniExecTaskTest {

    @InjectMocks
    IniExecTask iniExecTask = new IniExecTask();

    @Mock
    private ScheduleSetting scheduleSetting;

    @Mock
    private BackgroundScanDatastoreTask backgroundScanDatastoreTask;

    @Mock
    private SystemServiceImpl systemService;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onApplicationEvent() {

        ContextRefreshedEvent contextRefreshedEvent = mock(ContextRefreshedEvent.class);
        ApplicationContext applicationContext = spy(ApplicationContext.class);
        when(contextRefreshedEvent.getApplicationContext()).thenReturn(applicationContext);
        when(applicationContext.getParent()).thenReturn(null);
        iniExecTask.onApplicationEvent(contextRefreshedEvent);

    }
}