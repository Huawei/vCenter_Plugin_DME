package com.huawei.dmestore.task;

import static org.mockito.Mockito.spy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

/**
 * @author lianq
 * @className ApplicationContextHelperTest
 * @description TODO
 * @date 2020/12/2 18:57
 */
public class ApplicationContextHelperTest {

    @InjectMocks
    ApplicationContextHelper applicationContextHelper = new ApplicationContextHelper();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setApplicationContext() {
        ApplicationContext applicationContext = spy(ApplicationContext.class);
        applicationContextHelper.setApplicationContext(applicationContext);
    }

    @Test
    public void getBean() {
        applicationContextHelper.getBean("321");
    }
}